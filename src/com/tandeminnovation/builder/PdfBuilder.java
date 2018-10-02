package com.tandeminnovation.builder;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.tandeminnovation.report.charts.PieChart;

import java.awt.*;
import java.io.FileOutputStream;
import java.text.MessageFormat;
import java.util.Date;

public class PdfBuilder {

    private static Document document;
    private static PdfWriter writer;
    private static PdfPTable testResultTable;
    private static PdfPTable generalTable;
    private static PdfPCell celula;
    private static Image pieChart;

    /**
     * Build a new Document
     * @param file
     */
    public static void buildDocument(FileOutputStream file) {

        document = new Document();

        try {
            writer = PdfWriter.getInstance(document, file);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        document.open();
    }

    /**
     * Build the Header of the document
     * @param name
     * @param browser
     * @param url
     */
    public static void buildHeader(String name, String browser, String url) {

        Paragraph nameP = new Paragraph(MessageFormat.format("Suite: {0}", name),
                FontFactory.getFont(FontFactory.HELVETICA, 20, Font.BOLD, new Color(168, 108, 18)));
        Paragraph date = new Paragraph(MessageFormat.format("Date: {0}", new Date().toString()));
        Paragraph browserP = new Paragraph(MessageFormat.format("Browser: {0}", browser));
        Paragraph urlp = new Paragraph(MessageFormat.format("Page URL: {0}", url));

        try {
            document.add(nameP);
            document.add(date);
            document.add(browserP);
            document.add(urlp);
            document.add(new Paragraph());

        } catch (DocumentException e) {
            e.printStackTrace();
        }

    }

    /**
     * Build the Test Result Table
     */
    public static void buildResultTable() {

        testResultTable = new PdfPTable(6);
        testResultTable.setWidthPercentage(100);
        testResultTable.setSpacingBefore(15f);

        Paragraph tableTitle = new Paragraph("TEST RESULTS",
                new Font(Font.TIMES_ROMAN, Font.DEFAULTSIZE, Font.BOLD));
        PdfPCell cell = new PdfPCell(tableTitle);
        cell.setColspan(6);
        testResultTable.addCell(cell);

        cell = new PdfPCell(new Paragraph("Number"));
        cell.setBackgroundColor(Color.LIGHT_GRAY);
        testResultTable.addCell(cell);
        cell = new PdfPCell(new Paragraph("Name"));
        cell.setBackgroundColor(Color.LIGHT_GRAY);
        testResultTable.addCell(cell);
        cell = new PdfPCell(new Paragraph("Directory"));
        cell.setBackgroundColor(Color.LIGHT_GRAY);
        testResultTable.addCell(cell);
        cell = new PdfPCell(new Paragraph("Status"));
        cell.setBackgroundColor(Color.LIGHT_GRAY);
        testResultTable.addCell(cell);
        cell = new PdfPCell(new Paragraph("ScreenShot"));
        cell.setBackgroundColor(Color.LIGHT_GRAY);
        testResultTable.addCell(cell);
        cell = new PdfPCell(new Paragraph("Time (ms)"));
        cell.setBackgroundColor(Color.LIGHT_GRAY);
        testResultTable.addCell(cell);
    }

    /**
     * Build a generic table
     * @param className
     * @param methodName
     */
    public static void buildGeneralTable(String className, String methodName) {

        generalTable = new PdfPTable(2);
        generalTable.setWidthPercentage(100);
        generalTable.setSpacingBefore(15f);

        Paragraph tableTitle = new Paragraph("GENERAL FUNCTIONS",
                new Font(Font.TIMES_ROMAN, Font.DEFAULTSIZE, Font.BOLD));
        PdfPCell cell = new PdfPCell(tableTitle);
        cell.setColspan(2);
        generalTable.addCell(cell);

        cell = new PdfPCell(new Paragraph("Class"));
        cell.setBackgroundColor(Color.LIGHT_GRAY);
        generalTable.addCell(cell);
        cell = new PdfPCell(new Paragraph("Method"));
        cell.setBackgroundColor(Color.LIGHT_GRAY);
        generalTable.addCell(cell);

        celula = new PdfPCell(new Paragraph(className));
        generalTable.addCell(celula);
        celula = new PdfPCell(new Paragraph(methodName));
        generalTable.addCell(celula);

    }

    /**
     * Add elements to the Result Table
     * @param element
     */
    public static void addElementsToTable(String element) {

        celula = new PdfPCell(new Paragraph(element));
        if (element.equals("Passed")) {
            celula.setBackgroundColor(Color.GREEN);
        }
        if (element.equals("Failed")) {
            celula.setBackgroundColor(Color.RED);
        }
        if (element.equals("Skipped")) {
            celula.setBackgroundColor(Color.YELLOW);
        }
        testResultTable.addCell(celula);

    }

    /**
     * Create a pie char with the test Results
     * @param pass
     * @param fail
     * @param skip
     */
    public static void createPieChart(int pass, int fail, int skip) {

        pieChart = PieChart.generatePieChart(writer, pass, fail, skip);

    }

    /**
     * Add everything to the document
     */
    public static void addToDocument() {

        try {
            document.add(testResultTable);
            document.add(generalTable);
            document.add(pieChart);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

    }

    /**
     * Close document
     */
    public static void closeDocument() {
        document.close();
    }
}

