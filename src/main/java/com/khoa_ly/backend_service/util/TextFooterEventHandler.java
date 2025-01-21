package com.khoa_ly.backend_service.util;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.event.AbstractPdfDocumentEvent;
import com.itextpdf.kernel.pdf.event.AbstractPdfDocumentEventHandler;
import com.itextpdf.kernel.pdf.event.PdfDocumentEvent;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.properties.TextAlignment;

public class TextFooterEventHandler extends AbstractPdfDocumentEventHandler {
    protected Document doc;
    protected PdfFont font;

    public TextFooterEventHandler(Document doc, PdfFont font) {
        this.doc = doc;
        this.font = font;
    }

    @Override
    public void onAcceptedEvent(AbstractPdfDocumentEvent currentEvent) {
        PdfDocumentEvent docEvent = (PdfDocumentEvent) currentEvent;
        Rectangle pageSize = docEvent.getPage().getPageSize();

        PdfDocument pdfDoc = docEvent.getDocument();
        int pageNumber = pdfDoc.getPageNumber(docEvent.getPage());
        int totalPageCount = pdfDoc.getNumberOfPages();

        float coordX = ((pageSize.getLeft() + doc.getLeftMargin())
                + (pageSize.getRight() - doc.getRightMargin())) / 2;
        float footerY = doc.getBottomMargin();
        Canvas canvas = new Canvas(docEvent.getPage(), pageSize);
        canvas
                .setFont(font)
                .setFontSize(5)
                .showTextAligned(String.format("Page %d of %d", pageNumber, totalPageCount), coordX, footerY, TextAlignment.CENTER)
                .close();
    }
}
