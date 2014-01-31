/* ***********************************************************************
 * Copyright 2013 G.Kokonov, All rights reserved.
 * ***********************************************************************
 * Date: 01.12.2013
 * Author: G.Kokonov
 * ***********************************************************************/
package com.gk.webdriver.utils;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.PixelGrabber;

import org.apache.log4j.Logger;

/**
 * A class with a method to compare two image files.
 * 
 */
public class CompareImages {
    private static final Logger LOGGER = Logger.getLogger(CompareImages.class);

    public enum Result {
        Matched, SizeMismatch, PixelMismatch
    };

    static Result CompareImage(String baseFile, String actualFile) {
        Result compareResult = Result.PixelMismatch;
        Image baseImage = Toolkit.getDefaultToolkit().getImage(baseFile);
        Image actualImage = Toolkit.getDefaultToolkit().getImage(actualFile);
        try {
            PixelGrabber baseImageGrab = new PixelGrabber(baseImage, 0, 0, -1,
                    -1, false);
            PixelGrabber actualImageGrab = new PixelGrabber(actualImage, 0, 0,
                    -1, -1, false);
            int[] baseImageData = null;
            int[] actualImageData = null;
            if (baseImageGrab.grabPixels()) {
                int width = baseImageGrab.getWidth();
                int height = baseImageGrab.getHeight();
                baseImageData = new int[width * height];
                baseImageData = (int[]) baseImageGrab.getPixels();
            }
            if (actualImageGrab.grabPixels()) {
                int width = actualImageGrab.getWidth();
                int height = actualImageGrab.getHeight();
                actualImageData = new int[width * height];
                actualImageData = (int[]) actualImageGrab.getPixels();
            }
            LOGGER.info(baseImageGrab.getHeight() + "<>"
                    + actualImageGrab.getHeight());
            LOGGER.info(baseImageGrab.getWidth() + "<>"
                    + actualImageGrab.getWidth());
            if ((baseImageGrab.getHeight() != actualImageGrab.getHeight())
                    || (baseImageGrab.getWidth() != actualImageGrab.getWidth())) {
                compareResult = Result.SizeMismatch;
            } else if (java.util.Arrays.equals(baseImageData, actualImageData)) {
                compareResult = Result.Matched;
            }
        } catch (Exception e) {
            LOGGER.error(e);
        }
        return compareResult;
    }
}
