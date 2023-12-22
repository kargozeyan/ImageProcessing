package org.example.stage3;

import ij.ImagePlus;
import ij.process.ImageProcessor;

public class HoughTransformer {
    private final ImageProcessor ip;
    private int[][] accumulator;

    public HoughTransformer(ImageProcessor ip) {
        this.ip = ip;
    }

    public void detectLines() {
        int width = ip.getWidth();
        int height = ip.getHeight();

        // Initialize accumulator
        accumulator = new int[180][width + height];

        // Perform Hough Transform
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (ip.getPixel(x, y) > 0) {
                    for (int theta = 0; theta < 180; theta++) {
                        int rho = (int) (x * Math.cos(Math.toRadians(theta)) +
                                y * Math.sin(Math.toRadians(theta)));
                        accumulator[theta][rho + width]++;
                    }
                }
            }
        }
    }

    public void overlayLines() {
        ImageProcessor overlayProcessor = ip.duplicate();
        overlayProcessor.setColor(255);

        int threshold = 100;

        for (int theta = 0; theta < 180; theta++) {
            for (int rho = 0; rho < accumulator[theta].length; rho++) {
                if (accumulator[theta][rho] > threshold) {
                    double cosTheta = Math.cos(Math.toRadians(theta));
                    double sinTheta = Math.sin(Math.toRadians(theta));
                    int x0 = (int) (rho * cosTheta);
                    int y0 = (int) (rho * sinTheta);

                    overlayProcessor.drawLine(x0 - 1000, y0 - 1000, x0 + 1000, y0 + 1000);
                }
            }
        }

        new ImagePlus("Lines Overlay", overlayProcessor).show();
    }
}
