package com.example.caselabproject.services.implementations;

import com.example.caselabproject.exceptions.DocumentDoesNotExistException;
import com.example.caselabproject.exceptions.WordFileCreationException;
import com.example.caselabproject.models.entities.*;
import com.example.caselabproject.models.entities.Document;
import com.example.caselabproject.repositories.DocumentRepository;
import com.example.caselabproject.services.WordFileGenerator;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageMar;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Validated
public class WordFileGeneratorImpl implements WordFileGenerator {
    private final DocumentRepository documentRepository;
    @Value("${file.temp-dir}")
    private String tempDirectory;

    /**
     * Создает word файл для документа.
     *
     * @param documentId идентификатор документа, для которого необходимо сгенерировать word файл.
     * @return path, по которому расположен созданный документ.
     */
    @Override
    public String generateWordFileForDocumentById(Long documentId) {
        Document document = documentRepository
                .findById(documentId)
                .orElseThrow(() -> new DocumentDoesNotExistException(documentId));

        // Получаем данные, необходимые для заполнения word документа.
        DocumentConstructorType type = document.getDocumentConstructorType();
        String position = document.getCreator().getPosition();

        PersonalUserInfo personalUserInfo = document.getCreator().getPersonalUserInfo();
        String fullName = personalUserInfo.getLastName() + personalUserInfo.getFirstName() +
                personalUserInfo.getPatronymic();

        Map<String, String> params = new HashMap<>();
        for (Map.Entry<Field, String> fieldValue : document.getFieldsValues().entrySet()) {
            params.put(fieldValue.getKey().getName(), fieldValue.getValue());
        }

        return generateDocument(type.getName(), type.getPrefix(), document.getId(),
                params, position, fullName);
    }

    private String generateDocument(String title, String prefix, Long id, Map<String, String> params,
                                    String creatorPosition, String creatorFullName) {
        String path = tempDirectory + UUID.randomUUID() + ".doc";
        try (XWPFDocument document = new XWPFDocument();
             FileOutputStream fos = new FileOutputStream(path)) {
            // Установка полей
            CTPageMar pageMar = document.getDocument().getBody().addNewSectPr().addNewPgMar();
            pageMar.setLeft(1440);
            pageMar.setTop(1440);
            pageMar.setRight(720);
            pageMar.setBottom(1440);

            // Создание шапки документа
            XWPFParagraph headerParagraph = document.createParagraph();
            headerParagraph.setAlignment(ParagraphAlignment.LEFT);
            XWPFRun headerRun = headerParagraph.createRun();
            headerRun.setText("Код документа: " + prefix + id);

            // Создание параграфа с заголовком
            XWPFParagraph titleParagraph = document.createParagraph();
            titleParagraph.setAlignment(ParagraphAlignment.CENTER);
            titleParagraph.setSpacingAfter(360);
            XWPFRun titleRun = titleParagraph.createRun();
            titleRun.setBold(true);
            titleRun.setText(title);

            // Создание параграфов с основным контентом - полями и их значениями
            for (Map.Entry<String, String> param : params.entrySet()) {
                // Создание параграфа для имени параметра
                XWPFParagraph keyParagraph = document.createParagraph();
                keyParagraph.setAlignment(ParagraphAlignment.LEFT);
                XWPFRun keyRun = keyParagraph.createRun();
                keyRun.setText(param.getKey() + ": ");

                // Создание параграфа для значения параметра
                XWPFParagraph valueParagraph = document.createParagraph();
                valueParagraph.setAlignment(ParagraphAlignment.LEFT);
                valueParagraph.setIndentationFirstLine(720);
                XWPFRun valueRun = valueParagraph.createRun();
                valueRun.setUnderline(UnderlinePatterns.SINGLE);
                valueRun.setText(param.getValue());
            }

            // Создание футера с полным именем и должностью создателя документа
            XWPFParagraph footerParagraph = document.createParagraph();
            footerParagraph.setAlignment(ParagraphAlignment.LEFT);
            XWPFRun footerRun = footerParagraph.createRun();
            footerRun.addBreak();
            footerRun.setText(creatorPosition + ", " + creatorFullName);

            // Устанавливаем шрифт и междустрочный интервал для всех параграфов
            for (XWPFParagraph paragraph : document.getParagraphs()) {
                paragraph.setSpacingBetween(1.5); // Полуторный интервал
                for (XWPFRun run : paragraph.getRuns()) {
                    run.setFontFamily("Times New Roman");
                    run.setFontSize(14);
                }
            }

            document.write(fos);
            return path;
        } catch (IOException e) {
            throw new WordFileCreationException(id);
        }
    }
}
