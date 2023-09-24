package org.example;

import ij.ImagePlus;
import ij.io.FileSaver;
import ij.plugin.PlugIn;
import ij.process.BinaryProcessor;
import ij.process.ByteProcessor;
import ij.process.ImageProcessor;
import org.example.utils.Resources;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class Task1 implements PlugIn {

    @Override
    public void run(String args) {
        int n = Resources.readLinesFrom(App.CRS_NAME).size();
        ImageProcessor imageProcessor = new BinaryProcessor(
                new ByteProcessor(n, n)
        );

        imageProcessor.setColor(Color.WHITE);
        imageProcessor.fill();

        imageProcessor.setColor(Color.BLACK);
        Resources.readLinesFrom(App.STU_NAME)
                .stream()
                .map(line -> line.split("\\W+"))
                .filter(tokens -> tokens.length > 1)
                .flatMap(tokens -> {
                    List<Pair> list = new LinkedList<>();
                    for (int i = 0; i < tokens.length - 1; i++) {
                        int x = Integer.parseInt(tokens[i]);
                        int y = Integer.parseInt(tokens[i + 1]);
                        Pair pair = new Pair(x, y);

                        list.add(pair);
                    }
                    return list.stream();
                })
                .forEach(pair -> imageProcessor.drawPixel(pair.x, pair.y));


        ImagePlus imagePlus = new ImagePlus("Task1", imageProcessor);

        FileSaver fileSaver = new FileSaver(imagePlus);
        fileSaver.saveAsPng(Resources.ABSOLUTE_PATH + App.PNG_NAME);

        imagePlus.show();
    }

    private static class Pair {
        int x, y;

        public Pair(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
