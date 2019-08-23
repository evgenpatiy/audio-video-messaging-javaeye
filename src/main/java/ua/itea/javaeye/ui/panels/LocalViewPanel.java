/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.itea.javaeye.ui.panels;

import java.net.InetAddress;

import javax.swing.JLabel;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;

/**
 *
 * @author yevgen
 */
public class LocalViewPanel extends ViewPanel implements Runnable {

    private static final long serialVersionUID = 7966377808938755807L;
    private final Webcam webcam;
    private final WebcamPanel webcamPanel;
    private final JLabel webcamFPSLabel = new JLabel();
    private InetAddress localAddress = null;

    public LocalViewPanel(Webcam webcam, InetAddress localAddress) {
        this.webcam = webcam;
        this.localAddress = localAddress;

        webcamPanel = new WebcamPanel(this.webcam);
        webcamPanel.setDoubleBuffered(true);
        setView(webcamPanel);
        viewTitle = "Local cam view";

        createViewPanel();
        showInfoPanel();
    }

    @Override
    public void run() {
        setVisible(true);
        while (true) {
            try {
                Thread.sleep(500);
                updateInfoPanel();
            } catch (InterruptedException e) {
            }
        }
    }

    @Override
    public void showInfoPanel() {
        info.add(new JLabel(" Webcam: "));
        info.add(new JLabel(webcam.getName()));
        info.add(new JLabel(" Webcam FPS: "));
        info.add(webcamFPSLabel);
        info.add(new JLabel(" View resolution:"));
        info.add(new JLabel((int) webcam.getViewSize().getWidth() + "x" + (int) webcam.getViewSize().getHeight()));
        info.add(new JLabel(" Local IP: "));
        info.add(new JLabel(localAddress.getHostAddress()));
        info.add(new JLabel(" Video Codec: "));
        info.add(new JLabel("H.264"));
    }

    @Override
    public void updateInfoPanel() {
        webcamFPSLabel.setText(String.format("%4.2f", webcam.getFPS()));
    }
}
