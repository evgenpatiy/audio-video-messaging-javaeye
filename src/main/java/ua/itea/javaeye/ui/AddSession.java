package ua.itea.javaeye.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.net.InetAddress;
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

import ua.itea.javaeye.utils.DbWorker;
import ua.itea.javaeye.utils.JavaEyeUtils;
import ua.itea.javaeye.utils.Session;

public class AddSession extends JFrame implements Runnable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7775937214872456459L;
	private JPanel buttonsPanel;
	private InetAddress localAddress;

	protected AddSession(JPanel buttonsPanel, InetAddress localAddress) throws HeadlessException {
		this.buttonsPanel = buttonsPanel;
		this.localAddress = localAddress;
	}

	@Override
	public void run() {

		DbWorker db = new DbWorker("javaeye.db");

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
				session.setLocalAddress(localAddress);
				session.setRemoteName(nameTextField.getText());
				try {
					session.setRemoteAddress(InetAddress.getByName(addressTextField.getText()));
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}

				session.setText(nameTextField.getText());

				// System.out.println(session);
				db.writeSessionToDb(session);

				session.setText(nameTextField.getText());
				session.addActionListener(newSessionEvent -> {
					System.out.println(session);
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
}
