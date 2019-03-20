package ua.itea.javaeye.utils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SessionElement extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -450608700814697926L;
	private String windowTitle;
	protected JButton okButton = new JButton();
	private JTextField nameTextField = new JTextField();
	private JTextField addressTextField = new JTextField();

	public void setWindowTitle(String windowTitle) {
		this.windowTitle = windowTitle;
	}

	public void setOkButton(String okButtonText) {
		this.okButton.setText(okButtonText);
	}

	public JTextField getNameTextField() {
		return nameTextField;
	}

	public JTextField getAddressTextField() {
		return addressTextField;
	}

	protected void createWindow() {
		JPanel inputPanel = new JPanel();

		inputPanel.setLayout(new GridLayout(2, 2));
		inputPanel.setBorder(
				BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Session parameters "));
		inputPanel.add(new JLabel(" Session name:"));
		inputPanel.add(nameTextField);
		inputPanel.add(new JLabel(" Remote address:"));
		inputPanel.add(addressTextField);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(event -> {
			dispose();
		});

		setLayout(new BorderLayout());

		JPanel buttonsBox = new JPanel();
		buttonsBox.setLayout(new FlowLayout());
		buttonsBox.add(okButton);
		buttonsBox.add(cancelButton);

		add(inputPanel, BorderLayout.CENTER);
		add(buttonsBox, BorderLayout.SOUTH);

		setPreferredSize(new Dimension(300, 150));
		setTitle(windowTitle);

		URL iconURL = getClass().getResource("/img/eye.png");
		ImageIcon icon = new ImageIcon(iconURL);
		setIconImage(icon.getImage());

		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		pack();
		setVisible(true);
	}
}
