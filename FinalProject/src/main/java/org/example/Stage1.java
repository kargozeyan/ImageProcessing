package org.example;

import ij.IJ;
import ij.ImagePlus;
import ij.process.ColorProcessor;
import org.apache.commons.io.FilenameUtils;
import org.example.utils.Resources;

import java.awt.*;
import java.io.File;
import java.nio.file.Path;

public class Stage1 {
    private static final int STEP_SIZE = 4;

    public static void main(String[] args) {
        Resources.getFileNames("CSHandwriting")
                .stream()
                .map(Path::toFile)
                .map(f -> new String[]{f.getAbsolutePath(), FilenameUtils.getBaseName(f.getName())})
                .forEach(Stage1::eliminatePrintedTextAndInstructorMarks);
    }

    private static void eliminatePrintedTextAndInstructorMarks(String[] imageData) {
        final ImagePlus image = IJ.openImage(imageData[0]);
        ColorProcessor processor = (ColorProcessor) image.getProcessor();

        for (int i = 0; i < image.getWidth(); i += STEP_SIZE) {
            for (int j = 0; j < image.getHeight(); j += STEP_SIZE) {
                double[] stats = calculateHSBStatisticsOfArea(processor, i, j);

                if (stats[2] > .65) {
                    colorArea(processor, i, j);
                } else {
                    if (stats[0] < 0.25 || stats[0] > 0.75) {
                        colorArea(processor, i, j);
                    }
                }
            }
        }
        IJ.saveAs(image, "png", new File(Resources.PROCESSED_IMAGE_PATH).getAbsolutePath() + File.separator + imageData[1] + "_bin");
        image.close();
    }


    private static double[] calculateHSBStatisticsOfArea(ColorProcessor processor, int sX, int sY) {
        double[] hsbStats = {0, 0, 0};
        int count = 0;
        for (int i = 0; i < STEP_SIZE; i++) {
            int cX = sX + i;
            if (cX >= processor.getWidth()) {
                continue;
            }
            for (int j = 0; j < STEP_SIZE; j++) {
                int cY = sY + j;
                if (cY >= processor.getHeight()) {
                    continue;
                }
                Color color = processor.getColor(cX, cY);
                float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
                hsbStats[0] += hsb[0];
                hsbStats[1] += hsb[1];
                hsbStats[2] += hsb[2];
                count++;
            }
        }
        hsbStats[0] /= count;
        hsbStats[1] /= count;
        hsbStats[2] /= count;
        return hsbStats;
    }

    private static void colorArea(ColorProcessor processor, int sX, int sY) {
        for (int i = 0; i < STEP_SIZE; i++) {
            int cX = sX + i;
            if (cX >= processor.getWidth()) {
                continue;
            }
            for (int j = 0; j < STEP_SIZE; j++) {
                int cY = sY + j;
                if (cY >= processor.getHeight()) {
                    continue;
                }
                processor.setColor(Color.white);
                processor.drawPixel(cX, cY);
            }
        }
    }
}
