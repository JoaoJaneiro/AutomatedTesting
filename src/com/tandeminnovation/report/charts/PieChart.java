package com.tandeminnovation.report.charts;


import com.lowagie.text.BadElementException;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.DefaultFontMapper;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import java.awt.*;
import java.awt.geom.Rectangle2D;



public class PieChart {

    /**
     * Build a Pie Chart
     * @param writer
     * @param pass
     * @param fail
     * @param skipped
     * @return
     */
    public static Image generatePieChart(PdfWriter writer, double pass, double fail, double skipped) {
        int width = 300;
        int height = 200;

        DefaultPieDataset dataSet = new DefaultPieDataset();
        dataSet.setValue("Passed" , pass);
        dataSet.setValue("Failed", fail);
        dataSet.setValue("Skipped", skipped);

        JFreeChart chart = ChartFactory.createPieChart("Test Chart Results", dataSet, true, true, false);

        PiePlot ColorConfigurator = (PiePlot)chart.getPlot();
        ColorConfigurator.setSectionPaint("Passed", Color.GREEN);
        ColorConfigurator.setSectionPaint("Failed" + fail , Color.RED);
        ColorConfigurator.setSectionPaint("Skipped" + skipped, Color.YELLOW);

        PdfContentByte contentByte = writer.getDirectContent();
        PdfTemplate template = contentByte.createTemplate(width, height);
        Graphics2D graphics2d = template.createGraphics(width, height, new DefaultFontMapper());
        Rectangle2D rectangle2d = new Rectangle2D.Double(0, 0, width, height);
        chart.draw(graphics2d, rectangle2d);
        graphics2d.dispose();

        Image chartImage = null;
        try {
            chartImage = Image.getInstance(template);
            chartImage.setAlignment(Image.MIDDLE);
        } catch (BadElementException e) {
            e.printStackTrace();
        }
        return chartImage;
    }

}
