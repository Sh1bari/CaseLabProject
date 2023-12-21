package com.example.caselabproject.services.implementations;


import com.example.caselabproject.models.enums.SubscriptionName;
import com.example.caselabproject.services.PdfFileService;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
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
                document.add(new Paragraph("Тариф "
                        + usedDay.getKey() +
                        "-" + usedDay.getValue() +
                        " дней - " + prices.get(usedDay.getKey()) + " руб", font));
            }
            document.add(new Paragraph("Итоговая цена " + total + "руб", font));
            document.close();
        } catch (IOException | DocumentException e) {
            throw new RuntimeException(e);
        }
    }
}
