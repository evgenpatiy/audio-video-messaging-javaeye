/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.itea.javaeye.ui.panels;

import java.awt.image.BufferedImage;

import javax.swing.JLabel;

import ua.itea.javaeye.stream.MicStreamSender;
import ua.itea.javaeye.ui.SessionListItem;
import ua.itea.javaeye.utils.JavaEyeUtils;

/**
 *
 * @author yevgen
 */
public class RemoteViewPanel extends ViewPanel implements Runnable {
    private static final long serialVersionUID = 7928999741730995326L;
    private final VideoPanel videoPanel;
    private SessionListItem session = null;
    private JLabel sessionNameLabel = new JLabel();
    private JLabel sessionremoteAddressLabel = new JLabel();
    private MicStreamSender soundStream;

    public RemoteViewPanel() {
        this.videoPanel = new VideoPanel();
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                soundStream.setStop(true);
            }
        });

        setView(videoPanel);
        viewTitle = "Remote cam view";

        createViewPanel();
        showInfoPanel();
    }

    public void setSession(SessionListItem session) {
        this.session = session;
        soundStream = new MicStreamSender(session.getRemoteAddress());
    }

    public void updateImage(BufferedImage image) {
        videoPanel.updateImage(image);
    }

    public void close() {
        this.dispose();
        videoPanel.close();
    }

    @Override
    public void run() {
        (new Thread(soundStream)).start();
        setVisible(true);
        updateInfoPanel();
    }

    @Override
    public void showInfoPanel() {
        info.add(new JLabel(" Remote name: "));
        info.add(sessionNameLabel);
        info.add(new JLabel(" Remote IP: "));
        info.add(sessionremoteAddressLabel);
        info.add(new JLabel(" View resolution:"));
        info.add(new JLabel((int) JavaEyeUtils.dimension.getWidth() + "x" + (int) JavaEyeUtils.dimension.getHeight()));
        info.add(new JLabel(" Video Codec: "));
        info.add(new JLabel("H.264"));
    }

    @Override
    public void updateInfoPanel() {
        sessionNameLabel.setText(session.getRemoteName());
        sessionremoteAddressLabel.setText(session.getRemoteAddress().getHostAddress());
    }

}
