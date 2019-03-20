package ua.itea.javaeye.utils;

import java.awt.HeadlessException;
import java.awt.image.BufferedImage;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.github.sarxos.webcam.Webcam;

import ua.itea.javaeye.handler.StreamFrameListener;
import ua.itea.javaeye.panels.LocalViewPanel;
import ua.itea.javaeye.panels.RemoteViewPanel;
import ua.itea.javaeye.stream.VideoStreamClient;

public class SessionUtils extends SessionElement implements Runnable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7775937214872456459L;
	private JPanel buttonsPanel;
	private final Webcam webcam;
	private final LocalViewPanel localCam;
	private final RemoteViewPanel remoteCam;

	public SessionUtils(JPanel buttonsPanel, Webcam webcam) throws HeadlessException {
		this.buttonsPanel = buttonsPanel;

		this.webcam = webcam;
		this.localCam = new LocalViewPanel(webcam);
		this.remoteCam = new RemoteViewPanel();
	}

	@Override
	public void run() {
		DbWorker db = new DbWorker("javaeye.db");
		VideoStreamClient videoClient = new VideoStreamClient(new StreamFrameListenerIMPL(), JavaEyeUtils.dimension);

		setWindowTitle("New session");
		setOkButton("Add");
		createWindow();

		okButton.addActionListener(event -> {
			Session session = new Session();
			if (getNameTextField().getText().isEmpty()) {
				JOptionPane.showMessageDialog(null, "Session couldn't be empty", "Session name error",
						JOptionPane.ERROR_MESSAGE);
			} else if (!JavaEyeUtils.validIP(getAddressTextField().getText())) {
				JOptionPane.showMessageDialog(null, "Provide correct remote IP", "Session address error",
						JOptionPane.ERROR_MESSAGE);
			} else {
				session.setLocalAddress(JavaEyeUtils.localAddress);
				session.setRemoteName(getNameTextField().getText());
				try {
					session.setRemoteAddress(InetAddress.getByName(getAddressTextField().getText()));
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}

				db.addSession(session);

				session.sessionButton.setText(getNameTextField().getText());
				/*
				 * session.sessionButton.addActionListener(newSessionEvent -> {
				 * remoteCam.setSession(session); (new Thread(localCam)).start(); (new
				 * Thread(remoteCam)).start();
				 * 
				 * videoClient .connect(new InetSocketAddress(session.getRemoteAddress(),
				 * JavaEyeUtils.streamServerPort)); });
				 */

				buttonsPanel.add(session);
				buttonsPanel.revalidate();

				System.out.println(session);

				dispose();
			}
		});
	}

	protected class StreamFrameListenerIMPL implements StreamFrameListener {
		private volatile long count = 0;

		@Override
		public void onFrameReceived(BufferedImage image) {
			remoteCam.updateImage(image);
		}

	}
}
