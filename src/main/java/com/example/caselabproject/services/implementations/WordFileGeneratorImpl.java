package com.example.caselabproject.services.implementations;

import com.example.caselabproject.exceptions.document.DocumentDoesNotExistException;
import com.example.caselabproject.exceptions.file.WordFileCreationException;
import com.example.caselabproject.models.entities.Document;
import com.example.caselabproject.models.entities.DocumentConstructorType;
import com.example.caselabproject.models.entities.Field;
import com.example.caselabproject.models.entities.PersonalUserInfo;
import com.example.caselabproject.repositories.DocumentRepository;
import com.example.caselabproject.services.WordFileGenerator;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageMar;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Validated
public class WordFileGeneratorImpl implements WordFileGenerator {
    private final DocumentRepository documentRepository;

    /**
     * Создает word файл для документа.
     *
     * @param documentId идентификатор документа, для которого необходимо сгенерировать word файл.
     * @return документ в виде массива байт.
     */
    @Override
    public byte[] generateWordFileForDocumentById(Long documentId) {
        Document document = documentRepository
                .findById(documentId)
                .orElseThrow(() -> new DocumentDoesNotExistException(documentId));

        // Получаем данные, необходимые для заполнения word документа.
        DocumentConstructorType type = document.getDocumentConstructorType();
        String position = document.getCreator().getPosition();

        PersonalUserInfo personalUserInfo = document.getCreator().getPersonalUserInfo();
        String fullName = personalUserInfo.getLastName() + " " + personalUserInfo.getFirstName() +
                " " + personalUserInfo.getPatronymic();

        Map<String, String> params = new HashMap<>();
        for (Map.Entry<Field, String> fieldValue : document.getFieldsValues().entrySet()) {
            params.put(fieldValue.getKey().getName(), fieldValue.getValue());
        }

        return generateDocument(type.getName(), type.getPrefix(), document.getId(),
                document.getCreationDate(), params, position, fullName);
    }

    private byte[] generateDocument(String title, String prefix, Long id, LocalDateTime creationDate,
                                    Map<String, String> params, String creatorPosition, String creatorFullName) {
        try (XWPFDocument document = new XWPFDocument();
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            // Установка полей
            setPageFields(document);

            // Создание шапки документа
            createHeader(prefix, id, creationDate, document);

            // Создание параграфа с заголовком
            createTitle(title, document);

            // Создание параграфов с основным контентом - полями и их значениями
            createContentParagraphs(params, document);

            // Создание футера с полным именем и должностью создателя документа
            createFooter(creatorPosition, creatorFullName, document);

            // Устанавливаем шрифт и междустрочный интервал для всех параграфов
            setDocumentFont(document);

            document.write(byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new WordFileCreationException(id);
        }
    }


    /**
     * Создает шапку документа.
     */
    private void createHeader(String prefix, Long id, LocalDateTime creationDate, XWPFDocument document) {
        XWPFParagraph headerParagraph = document.createParagraph();
        headerParagraph.setAlignment(ParagraphAlignment.LEFT);

        XWPFRun headerRun = headerParagraph.createRun();
        headerRun.setText("Код документа: " + prefix + id);

        headerRun.addBreak();
        headerRun.setText("от " + creationDate.toLocalDate());
    }

    /**
     * Устанавливает поля документа.
     */
    private void setPageFields(XWPFDocument document) {
        CTPageMar pageMar = document.getDocument().getBody().addNewSectPr().addNewPgMar();
        pageMar.setLeft(1440);
        pageMar.setTop(1440);
        pageMar.setRight(720);
        pageMar.setBottom(1440);
    }

    /**
     * Создает футер с полным именем и должностью создателя документа
     */
    private void createFooter(String creatorPosition, String creatorFullName, XWPFDocument document) {
        XWPFParagraph footerParagraph = document.createParagraph();
        footerParagraph.setAlignment(ParagraphAlignment.LEFT);
        XWPFRun footerRun = footerParagraph.createRun();
        footerRun.addBreak();
        footerRun.setText(creatorPosition + ", " + creatorFullName);
    }

    /**
     * Создает параграфы с основным контентом - полями и их значениями.
     */
    private void createContentParagraphs(Map<String, String> params, XWPFDocument document) {
        for (Map.Entry<String, String> param : params.entrySet()) {
            // Создание параграфа для имени параметра
            createParamNameParagraph(document, param);

            // Создание параграфа для значения параметра
            createParamValueParagraph(document, param);
        }
    }

    /**
     * Создает параграф со значением переданного параметра.
     */
    private void createParamValueParagraph(XWPFDocument document, Map.Entry<String, String> param) {
        XWPFParagraph valueParagraph = document.createParagraph();
        valueParagraph.setAlignment(ParagraphAlignment.LEFT);
        valueParagraph.setIndentationFirstLine(720);

        XWPFRun valueRun = valueParagraph.createRun();
        valueRun.setUnderline(UnderlinePatterns.SINGLE);
        valueRun.setText(param.getValue());
    }

    /**
     * Создает параграф с именем переданного параметра.
     */
    private void createParamNameParagraph(XWPFDocument document, Map.Entry<String, String> param) {
        XWPFParagraph keyParagraph = document.createParagraph();
        keyParagraph.setAlignment(ParagraphAlignment.LEFT);

        XWPFRun keyRun = keyParagraph.createRun();
        keyRun.setText(param.getKey() + ": ");
    }

    /**
     * Создает заголовок документа.
     */
    private void createTitle(String title, XWPFDocument document) {
        XWPFParagraph titleParagraph = document.createParagraph();
        titleParagraph.setAlignment(ParagraphAlignment.CENTER);
        titleParagraph.setSpacingAfter(360);

        XWPFRun titleRun = titleParagraph.createRun();
        titleRun.setBold(true);
        titleRun.setText(title);
    }


    /**
     * Устанавливает шрифт и междустрочный интервал для всех параграфов документа.
     */
    private void setDocumentFont(XWPFDocument document) {
        for (XWPFParagraph paragraph : document.getParagraphs()) {
            paragraph.setSpacingBetween(1.5); // Полуторный интервал
            for (XWPFRun run : paragraph.getRuns()) {
                run.setFontFamily("Times New Roman");
                run.setFontSize(14);
            }
        }
    }
}
