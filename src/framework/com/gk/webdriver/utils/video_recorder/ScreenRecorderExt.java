/* ***********************************************************************
 * Copyright 2013 G.Kokonov, All rights reserved.
 * ***********************************************************************
 * Date: 02.12.2013
 * Author: G.Kokonov
 * ***********************************************************************/
package com.gk.webdriver.utils.video_recorder;

import java.awt.AWTException;
import java.awt.GraphicsConfiguration;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.monte.media.Format;
import org.monte.media.Registry;
import org.monte.screenrecorder.ScreenRecorder;

/**
 * Extends ScreenRecorder class due to possibility to specify location and name
 * of the video file with recorder screen cast
 * 
 */
public class ScreenRecorderExt extends ScreenRecorder {
    private String fileName = null;

    /**
     * Creates ScreenRecorderExt instance with parameters
     * 
     * @param cfg
     * @param captureArea
     * @param fileFormat
     * @param screenFormat
     * @param mouseFormat
     * @param audioFormat
     * @param movieFolder
     * @param fileName
     * @throws IOException
     * @throws AWTException
     */
    protected ScreenRecorderExt(GraphicsConfiguration cfg,
            Rectangle captureArea,
            Format fileFormat, Format screenFormat, Format mouseFormat,
            Format audioFormat, File movieFolder, String fileName)
                    throws IOException, AWTException {
        super(cfg, captureArea, fileFormat, screenFormat, mouseFormat,
                audioFormat, movieFolder);
        this.fileName = fileName;
    }

    @Override
    protected File createMovieFile(Format fileFormat) throws IOException {
        if (!movieFolder.exists()) {
            movieFolder.mkdirs();
        } else if (!movieFolder.isDirectory()) {
            throw new IOException("\"" + movieFolder + "\" is not a directory.");
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss");

        return new File(movieFolder, fileName + "-"
                + dateFormat.format(new Date()) + "."
                + Registry.getInstance().getExtension(fileFormat));
    }
}
