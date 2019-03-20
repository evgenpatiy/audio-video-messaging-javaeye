/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.itea.javaeye.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.github.sarxos.webcam.Webcam;

import ua.itea.javaeye.handler.StreamFrameListener;
import ua.itea.javaeye.panels.LocalViewPanel;
import ua.itea.javaeye.panels.RemoteViewPanel;
import ua.itea.javaeye.stream.VideoStreamClient;
import ua.itea.javaeye.utils.DbWorker;
import ua.itea.javaeye.utils.JavaEyeUtils;
import ua.itea.javaeye.utils.Session;
import ua.itea.javaeye.utils.SessionUtils;

/**
 *
 * @author yevgen
 */
public class SessionList extends JFrame implements Runnable {

	private static final long serialVersionUID = 2307756360605280092L;
	private final Webcam webcam;
	private final LocalViewPanel localCam;
	private final RemoteViewPanel remoteCam;
	private final VideoStreamClient videoClient;
	private DbWorker db;
	private JPanel sessionButtonsPanel = new JPanel();

	SessionList(Webcam webcam) {
		this.webcam = webcam;
		this.localCam = new LocalViewPanel(webcam);
		this.remoteCam = new RemoteViewPanel();
		this.db = new DbWorker(JavaEyeUtils.DBNAME);
		this.videoClient = new VideoStreamClient(new StreamFrameListenerIMPL(), JavaEyeUtils.dimension);
	}

	private void updateSessionList() {
		ArrayList<Session> sessionList = db.getSessionsList();
		for (Session session : sessionList) {
			session.sessionButton.addActionListener(event -> {
				// remoteCam.setSession(session);
				runSession(session);
			});
			sessionButtonsPanel.add(session);
		}
	}

	@Override
	public void run() {

		JPanel bottomButtonsPanel = new JPanel();
		JButton addSessionButton = new JButton("Session");

		sessionButtonsPanel.setLayout(new GridLayout(10, 1));

		if (!db.isDbExists()) {
			db.createDb();
		} else {
			updateSessionList();
		}

		System.out.println("SessionList runs on " + Thread.currentThread().getName());
		setPreferredSize(new Dimension(200, 500));
		setLayout(new BorderLayout());
		setTitle("Session list");

		URL iconURL = getClass().getResource("/img/plus.png");
		ImageIcon icon = new ImageIcon(iconURL);
		addSessionButton.setIcon(icon);
		addSessionButton.addActionListener(event -> {
			(new Thread(new SessionUtils(sessionButtonsPanel, webcam))).start();
		});

		iconURL = getClass().getResource("/img/eye.png");
		icon = new ImageIcon(iconURL);

		bottomButtonsPanel.setLayout(new FlowLayout());
		bottomButtonsPanel.add(addSessionButton);

		add(sessionButtonsPanel, BorderLayout.CENTER);
		add(bottomButtonsPanel, BorderLayout.SOUTH);

		setIconImage(icon.getImage());

		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		pack();
		setResizable(false);
		setVisible(true);
	}

	private void runSession(Session runSession) {
		remoteCam.setSession(runSession);
		(new Thread(localCam)).start();
		(new Thread(remoteCam)).start();
		videoClient.connect(new InetSocketAddress(runSession.getRemoteAddress(), JavaEyeUtils.streamServerPort));
	}

	protected class StreamFrameListenerIMPL implements StreamFrameListener {
		private volatile long count = 0;

		@Override
		public void onFrameReceived(BufferedImage image) {
			remoteCam.updateImage(image);
		}

	}
}
