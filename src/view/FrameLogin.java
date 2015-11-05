package view;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class FrameLogin extends JFrame {

    private JTextField nameField;
    private JPasswordField passwordField;
    private JButton okButton;

    public FrameLogin() throws HeadlessException {
        setLayout(new MigLayout());

        nameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        JLabel nameLabel = new JLabel("Логин:", JLabel.RIGHT);
        JLabel passwordLabel = new JLabel("Пароль:", JLabel.RIGHT);

        okButton = new JButton("OK");
        JButton cancelButton = new JButton("Отмена");

        cancelButton.addActionListener(e -> System.exit(0));

        add(nameLabel);
        add(nameField, "growx, wrap");
        add(passwordLabel);
        add(passwordField, "growx, wrap");

        JPanel panel = new JPanel();
        panel.add(okButton);
        panel.add(cancelButton);

        add(panel, "cell 1 2, right");

        setTitle("Идентификация");
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        pack();
        setVisible(true);
    }

    public String getLogin() {
        return nameField.getText().trim();
    }

    public char[] getPassword() {
        return passwordField.getPassword();
    }

    public void setOkButtonAction(ActionListener actionListener) {
        okButton.addActionListener(actionListener);
        nameField.addActionListener(actionListener);
        passwordField.addActionListener(actionListener);
    }


}
