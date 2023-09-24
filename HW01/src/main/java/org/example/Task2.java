package org.example;

import ij.ImagePlus;
import ij.io.FileSaver;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;
import org.example.utils.Resources;

public class Task2 implements PlugInFilter {
    @Override
    public int setup(String args, ImagePlus imagePlus) {
        return DOES_8C;
    }

    @Override
    public void run(ImageProcessor imageProcessor) {
        int length = imageProcessor.getWidth();
        int half = length / 2;

        for (int i = 0; i < half; i++) {
            for (int j = 0; j < length; j++) {
                int temp = imageProcessor.getPixel(i, j);
                imageProcessor.putPixel(i, j, imageProcessor.getPixel(half + i, j));
                imageProcessor.putPixel(half + i, j, temp);
            }
        }

        for (int i = 0; i < length; i++) {
            for (int j = 0; j < half; j++) {
                int temp = imageProcessor.getPixel(i, j);
                imageProcessor.putPixel(i, j, imageProcessor.getPixel(i, half + j));
                imageProcessor.putPixel(i, half + j, temp);
            }
        }

        ImagePlus imagePlus = new ImagePlus("Task2", imageProcessor);

        FileSaver fileSaver = new FileSaver(imagePlus);
        fileSaver.saveAsPng(Resources.ABSOLUTE_PATH + "copy.png");

        imagePlus.show();
    }
}
