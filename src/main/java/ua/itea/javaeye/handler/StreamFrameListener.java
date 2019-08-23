package ua.itea.javaeye.handler;

import java.awt.image.BufferedImage;

public interface StreamFrameListener {
    /**
     * @param image The received and decoded image
     */
    public void onFrameReceived(BufferedImage image);
}
