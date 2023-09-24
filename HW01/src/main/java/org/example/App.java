package org.example;

import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import org.example.utils.Resources;

public class App {
    public static final String CRS_NAME = "ear-f-83.crs";
    public static final String STU_NAME = "ear-f-83.stu";
    public static final String PNG_NAME = "ear-f-83.png";

    public static void main(String[] args) {
        ImageJ imageJ = new ImageJ(); // run ImageJ

        // Run Task1 PlugIn
        imageJ.runUserPlugIn("Task1", Task1.class.getName(), "", false);

        // Run Task2 FilterPlugIn
        ImagePlus imagePlus = IJ.openImage(Resources.ABSOLUTE_PATH + PNG_NAME);
        Task2 task2 = new Task2();
        task2.setup("", imagePlus);
        task2.run(imagePlus.getProcessor());
    }
}
