package ua.itea.javaeye.ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.net.InetAddress;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class SessionListItem extends JLabel {
    private static final long serialVersionUID = -8614461107737396240L;
    @Getter
    @Setter
    private InetAddress localAddress;
    @Getter
    @Setter
    private InetAddress remoteAddress;
    @Getter
    private String remoteName;
    @Getter
    @Setter
    private int id;

    public JButton sessionButton = new JButton();
    public JButton editSessionButton = new JButton();
    public JButton deleteSessionButton = new JButton();
    private JPanel rightButtonsPanel = new JPanel();

    public SessionListItem() {
        setLayout(new BorderLayout());
        rightButtonsPanel.setLayout(new GridLayout(2, 1));

        URL iconURL = getClass().getResource("/img/edit.png");
        ImageIcon icon = new ImageIcon(iconURL);
        editSessionButton.setIcon(icon);
        editSessionButton.setToolTipText("Edit session");
        rightButtonsPanel.add(editSessionButton);

        iconURL = getClass().getResource("/img/delete.png");
        icon = new ImageIcon(iconURL);
        deleteSessionButton.setIcon(icon);
        deleteSessionButton.setToolTipText("Delete session");
        rightButtonsPanel.add(deleteSessionButton);

        iconURL = getClass().getResource("/img/eye.png");
        icon = new ImageIcon(iconURL);
        sessionButton.setIcon(icon);
        sessionButton.setToolTipText("Establish connect");
        sessionButton.setHorizontalAlignment(SwingConstants.LEFT);

        add(sessionButton, BorderLayout.CENTER);
        add(rightButtonsPanel, BorderLayout.EAST);
        this.setBorder(BorderFactory.createLoweredBevelBorder());
    }

    public void setRemoteName(String remoteName) {
        this.remoteName = remoteName;
        sessionButton.setText(remoteName);
    }
}
