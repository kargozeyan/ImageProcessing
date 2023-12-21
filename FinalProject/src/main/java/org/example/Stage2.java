package org.example;

import ij.IJ;
import ij.ImagePlus;
import ij.measure.Measurements;
import ij.measure.ResultsTable;
import ij.plugin.filter.ParticleAnalyzer;
import ij.process.ImageProcessor;
import ij.process.ImageStatistics;
import org.apache.commons.io.FilenameUtils;
import org.example.utils.Resources;

import java.nio.file.Path;
import java.util.stream.DoubleStream;

public class Stage2 {
    // WARNING: if run this is going to open a separate window of analyze for each image
    public static void main(String[] args) {
        Resources.getFileNames("ProcessedImages")
                .stream()
                .sorted()
                .map(Path::toFile)
                .map(f -> new String[]{f.getAbsolutePath(), FilenameUtils.getBaseName(f.getName())})
                .forEach(Stage2::evaluateAndLog);
    }

    private static void evaluateAndLog(String[] imageData) {
        System.out.printf("Analyzing %s...%n", imageData[1]);
        final ImagePlus image = IJ.openImage(imageData[0]);
        evaluatePageAndLog(image);
        evaluateFontAndLog(image);
    }

    private static void evaluatePageAndLog(ImagePlus image) {
        final ImageStatistics stats = image.getStatistics();

        System.out.printf("Page Width is %s%n", stats.roiWidth);
        System.out.printf("Page Height is %s%n", stats.roiHeight);
        System.out.printf("Page Orientation is %s%n", stats.angle);
    }

    private static void evaluateFontAndLog(ImagePlus image) {
        ImageProcessor processor = image.getProcessor();
        processor.smooth();
        processor.sharpen();

        IJ.run(image, "Find Edges", "");
        IJ.run(image, "Make Binary", "");

        ResultsTable resultsTable = new ResultsTable();
        ParticleAnalyzer analyzer = new ParticleAnalyzer(
                ParticleAnalyzer.SHOW_OUTLINES | ParticleAnalyzer.ADD_TO_MANAGER,
                Measurements.AREA | Measurements.RECT, resultsTable,
                0, Double.POSITIVE_INFINITY);
        analyzer.analyze(image);

        float[] column = resultsTable.getColumn(ResultsTable.AREA);

        double totalArea = DoubleStream.of(convertFloatsToDoubles(column)).sum();
        System.out.printf("Average Font Size is %s%n", Math.sqrt(totalArea / resultsTable.getCounter()));
    }

    private static double[] convertFloatsToDoubles(float[] input) {

        double[] output = new double[input.length];
        for (int i = 0; i < input.length; i++) {
            output[i] = input[i];
        }
        return output;
    }
}
