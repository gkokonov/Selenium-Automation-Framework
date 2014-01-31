/* ***********************************************************************
 * Copyright 2013 G.Kokonov, All rights reserved.
 * ***********************************************************************
 * Date: 01.12.2013
 * Author: G.Kokonov
 * ***********************************************************************/
package com.gk.webdriver.utils.video_recorder;

import static org.monte.media.FormatKeys.EncodingKey;
import static org.monte.media.FormatKeys.FrameRateKey;
import static org.monte.media.FormatKeys.KeyFrameIntervalKey;
import static org.monte.media.FormatKeys.MIME_AVI;
import static org.monte.media.FormatKeys.MediaTypeKey;
import static org.monte.media.FormatKeys.MimeTypeKey;
import static org.monte.media.VideoFormatKeys.CompressorNameKey;
import static org.monte.media.VideoFormatKeys.DepthKey;
import static org.monte.media.VideoFormatKeys.ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE;
import static org.monte.media.VideoFormatKeys.QualityKey;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.monte.media.Format;
import org.monte.media.FormatKeys.MediaType;
import org.monte.media.math.Rational;

/**
 * Record video with Monte Media Library. For “AVI” format you need to install
 * TSCC Codec, in order to play the video. Default location is user home folder -
 * videos.
 * 
 */
public class VideoRecorder {
    private ScreenRecorderExt screenRecorder;
    private static final Logger LOGGER = Logger.getLogger(VideoRecorder.class);

    private void setupScreenRecorder(String videoFolder, String videoName) {
        // Create an instance of GraphicsConfiguration to get the
        // Graphics configuration of the Screen. 
        // This is needed for ScreenRecorder class.      
        GraphicsConfiguration gc = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDefaultConfiguration();

        File file = new File(videoFolder);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screenSize.width;
        int height = screenSize.height;

        Rectangle captureSize = new Rectangle(0, 0, width, height);

        // Create a instance of ScreenRecorder with the required configurations   
        try {
            setScreenRecorder(new ScreenRecorderExt(gc, captureSize,
                    new Format(MediaTypeKey, MediaType.FILE, MimeTypeKey,
                            MIME_AVI),
                    new Format(
                            MediaTypeKey, MediaType.VIDEO, EncodingKey,
                            ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
                            CompressorNameKey,
                            ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE, DepthKey,
                            24, FrameRateKey, Rational.valueOf(15), QualityKey,
                            1.0f, KeyFrameIntervalKey, 15 * 60),
                    new Format(
                            MediaTypeKey, MediaType.VIDEO, EncodingKey, "black",
                            FrameRateKey, Rational.valueOf(30)),
                    null, file, videoName));
        } catch (IOException | AWTException e) {
            LOGGER.error(e);
            setScreenRecorder(null);
        }
    }

    /**
     * @return the screenRecorder
     */
    public ScreenRecorderExt getScreenRecorder() {
        return screenRecorder;
    }

    /**
     * @param screenRecorder
     *            the screenRecorder to set
     */
    public void setScreenRecorder(ScreenRecorderExt screenRecorder) {
        this.screenRecorder = screenRecorder;
    }

    public void startRecording(String videoFolder, String videoName) {
        try {
            setupScreenRecorder(videoFolder, videoName);
            getScreenRecorder().start();
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }

    public void stopRecording() {
        try {
            getScreenRecorder().stop();
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }

    public List<File> getMovieFiles() {
        return getScreenRecorder().getCreatedMovieFiles();
    }
}
