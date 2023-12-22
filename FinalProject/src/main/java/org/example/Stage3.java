package org.example;

import ij.IJ;
import ij.ImagePlus;
import ij.process.ImageProcessor;
import org.apache.commons.io.FilenameUtils;
import org.example.stage3.HoughTransformer;
import org.example.utils.Resources;

import java.nio.file.Path;

public class Stage3 {

    // Same warning as in Stage 2
    public static void main(String[] args) {
        Resources.getFileNames("ProcessedImages")
                .stream()
                .sorted()
                .map(Path::toFile)
                .map(f -> new String[]{f.getAbsolutePath(), FilenameUtils.getBaseName(f.getName())})
                .forEach(Stage3::drawBaseLines);
    }

    private static void drawBaseLines(String[] imageData) {
        System.out.println(imageData[1]);
        ImagePlus imagePlus = IJ.openImage(imageData[0]);

        if (imagePlus == null) {
            System.err.println("Error: Could not open the image.");
            return;
        }

        ImageProcessor ip = imagePlus.getProcessor();
        ip.autoThreshold();

        HoughTransformer houghTransformer = new HoughTransformer(ip);
        houghTransformer.detectLines();
        houghTransformer.overlayLines();
    }
}