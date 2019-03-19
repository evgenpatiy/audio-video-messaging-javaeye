package ua.itea.javaeye.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.image.BufferedImage;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URL;
import java.net.UnknownHostException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.github.sarxos.webcam.Webcam;

import ua.itea.javaeye.agent.StreamClientAgent;
import ua.itea.javaeye.handler.StreamFrameListener;
import ua.itea.javaeye.panels.LocalViewPanel;
import ua.itea.javaeye.panels.RemoteViewPanel;
import ua.itea.javaeye.utils.DbWorker;
import ua.itea.javaeye.utils.JavaEyeUtils;
import ua.itea.javaeye.utils.Session;

public class AddSession extends JFrame implements Runnable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7775937214872456459L;
	private JPanel buttonsPanel;
	private final Webcam webcam;
	private final LocalViewPanel localCam;
	private final RemoteViewPanel remoteCam;

	protected AddSession(JPanel buttonsPanel, Webcam webcam) throws HeadlessException {
		this.buttonsPanel = buttonsPanel;

		this.webcam = webcam;
		this.localCam = new LocalViewPanel(webcam);
		this.remoteCam = new RemoteViewPanel();
	}

	@Override
	public void run() {
		DbWorker db = new DbWorker("javaeye.db");
		StreamClientAgent clientAgent = new StreamClientAgent(new StreamFrameListenerIMPL(), JavaEyeUtils.dimension);

		JPanel inputPanel = new JPanel();
		JTextField nameTextField = new JTextField();
		JTextField addressTextField = new JTextField();

		inputPanel.setLayout(new GridLayout(2, 2));
		inputPanel.setBorder(
				BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Session parameters "));
		inputPanel.add(new JLabel(" Session name:"));
		inputPanel.add(nameTextField);
		inputPanel.add(new JLabel(" Remote address:"));
		inputPanel.add(addressTextField);

		JButton addButton = new JButton("Add");
		addButton.addActionListener(event -> {
			Session session = new Session();
			if (nameTextField.getText().isEmpty()) {
				JOptionPane.showMessageDialog(null, "Session couldn't be empty", "Session name error",
						JOptionPane.ERROR_MESSAGE);
			} else if (!JavaEyeUtils.validIP(addressTextField.getText())) {
				JOptionPane.showMessageDialog(null, "Provide correct remote IP", "Session address error",
						JOptionPane.ERROR_MESSAGE);
			} else {
				session.setLocalAddress(JavaEyeUtils.localAddress);
				session.setRemoteName(nameTextField.getText());
				try {
					session.setRemoteAddress(InetAddress.getByName(addressTextField.getText()));
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}

				session.setHorizontalAlignment(SwingConstants.LEFT);
				URL iconURL = getClass().getResource("/img/eye.png");
				ImageIcon icon = new ImageIcon(iconURL);
				session.setIcon(icon);
				session.setText(nameTextField.getText());
				db.writeSessionToDb(session);

				session.addActionListener(newSessionEvent -> {
					System.out.println(session);

					remoteCam.setSession(session);
					(new Thread(localCam)).start();
					(new Thread(remoteCam)).start();

					clientAgent.connect(new InetSocketAddress(session.getRemoteAddress(), 20000));
				});

				buttonsPanel.add(session);
				buttonsPanel.revalidate();
				dispose();
			}
		});

		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(event -> {
			dispose();
		});

		setLayout(new BorderLayout());

		JPanel buttonsBox = new JPanel();
		buttonsBox.setLayout(new FlowLayout());
		buttonsBox.add(addButton);
		buttonsBox.add(cancelButton);

		add(inputPanel, BorderLayout.CENTER);
		add(buttonsBox, BorderLayout.SOUTH);

		setPreferredSize(new Dimension(300, 150));
		setTitle("Add session");

		URL iconURL = getClass().getResource("/img/eye.png");
		ImageIcon icon = new ImageIcon(iconURL);
		setIconImage(icon.getImage());

		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		pack();
		setVisible(true);
	}

	protected class StreamFrameListenerIMPL implements StreamFrameListener {
		private volatile long count = 0;

		@Override
		public void onFrameReceived(BufferedImage image) {
			remoteCam.updateImage(image);
		}

	}
}
