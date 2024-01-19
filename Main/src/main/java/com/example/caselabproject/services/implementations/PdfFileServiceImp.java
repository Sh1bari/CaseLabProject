package com.example.caselabproject.services.implementations;


import com.example.caselabproject.exceptions.biling.PdfCreatingException;
import com.example.caselabproject.models.BillingDaysAndPrice;
import com.example.caselabproject.models.DTOs.response.organization.GetOrganizationResponseDto;
import com.example.caselabproject.models.entities.Organization;
import com.example.caselabproject.models.enums.SubscriptionName;
import com.example.caselabproject.services.PdfFileService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.Month;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class PdfFileServiceImp implements PdfFileService {


    @Override
    public void generatePdfBillingFile(Map<SubscriptionName, Integer> usages,
                                       Map<SubscriptionName, BigDecimal> prices,
                                       BigDecimal total) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("src/main/resources/files/billing.pdf"));
            document.open();
            //Щрифт
            BaseFont times = BaseFont.createFont("src/main/resources/fonts/times.ttf",
                    "cp1251", BaseFont.EMBEDDED);
            Font font = new Font(times, 14);
            //Заполнение документа
            for (Map.Entry<SubscriptionName, Integer> usedDay : usages.entrySet()) {
                addParagraphInDocument(document, new Chunk("Тариф "
                        + usedDay.getKey() +
                        " - " + usedDay.getValue() +
                        " дней - " + prices.get(usedDay.getKey()) + " руб", font));
            }
            addParagraphInDocument(document, new Chunk("Итоговая цена " + total + "руб", font));
            document.close();
        } catch (IOException | DocumentException e) {
            throw new PdfCreatingException(e.getMessage());
        }
    }

    @Override
    public Resource generatePdfBillingDetailsFile(GetOrganizationResponseDto organization,
                                                  Map<Integer, Map<Month, List<BillingDaysAndPrice>>> details) {
        Document document = new Document();
        try {
            String fileName = "src/main/resources/files/allBilling_" +
                    organization.getName() + ".pdf";
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();
            //Щрифт
            BaseFont times = BaseFont.createFont("src/main/resources/fonts/times.ttf",
                    "cp1251", BaseFont.EMBEDDED);
            Font font1 = new Font(times, 24);
            Font font2 = new Font(times, 22);
            Font font3 = new Font(times, 18);
            addParagraphTitleInDocument(document, new Chunk("Организация " + organization.getName(),
                    font1));
            //Заполнение документа
            for (Map.Entry<Integer, Map<Month, List<BillingDaysAndPrice>>> detailByYear : details.entrySet()) {
                addParagraphInDocument(document, new Chunk("Год: " + detailByYear.getKey(), font2));
                for (Map.Entry<Month, List<BillingDaysAndPrice>> detailByMonth : detailByYear.getValue().entrySet()){
                    addParagraphInDocument(document, new Chunk("Месяц: " + detailByMonth.getKey().toString(),
                            font2));
                    for (BillingDaysAndPrice billingStat : detailByMonth.getValue()){
                        addParagraphTextInDocument(document,
                                new Chunk("Подписка " + billingStat.getSubscription(), font3));
                        addParagraphTextInDocument(document,
                                new Chunk("Количество дней использования " + billingStat.getDays(), font3));
                        addParagraphTextInDocument(document,
                                new Chunk("Цена " + billingStat.getPrices(), font3));
                    }
                }
            }
            document.close();
            return new UrlResource(Path.of(fileName).toUri());
        } catch (IOException | DocumentException e) {
            throw new PdfCreatingException(e.getMessage());
        }
    }

    /**
     * Добавление параграфа в документ pdf с одним куском текста
     *
     * @param document      pdf документ
     * @param paragraphName имя параграфа
     */
    private void addParagraphInDocument(Document document,
                                        Chunk paragraphName) throws DocumentException {
        Paragraph paragraph = new Paragraph();
        //Устанавливаем отступ для первой стркои
        paragraph.setFirstLineIndent(20.0f);
        addChunkInParagraph(paragraph, paragraphName);
        document.add(paragraph);
    }

    /**
     * Добавление параграфа в документ pdf с одним куском текста
     *
     * @param document      pdf документ
     * @param paragraphName имя параграфа
     */
    private void addParagraphTextInDocument(Document document,
                                        Chunk paragraphName) throws DocumentException {
        Paragraph paragraph = new Paragraph();
        //Устанавливаем отступ для первой стркои
        paragraph.setFirstLineIndent(40.0f);
        addChunkInParagraph(paragraph, paragraphName);
        document.add(paragraph);
    }

    private void addParagraphTitleInDocument(Document document, Chunk paragraphName) throws DocumentException {
        Paragraph paragraph = new Paragraph();
        //Устанавливаем отступ для первой стркои
        paragraph.setFirstLineIndent(100.0f);
        addChunkInParagraph(paragraph, paragraphName);
        document.add(paragraph);
    }

    /**
     * Добавление текста в параграф
     *
     * @param paragraph параграф pdf документа
     * @param chunk     кусок текста
     */
    private void addChunkInParagraph(Paragraph paragraph, Chunk chunk) {
        //Устанавливаем размер строки
        chunk.setLineHeight(26);
        paragraph.add(chunk);
    }
}
