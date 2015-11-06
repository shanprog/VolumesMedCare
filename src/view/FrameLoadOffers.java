package view;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;

public class FrameLoadOffers extends JFrame {

    private JTextField filePath;
    private JButton loadButton;





    public FrameLoadOffers() throws HeadlessException {

        JPanel bottomPanel = new JPanel(new MigLayout());
        JButton fileChooseButton = new JButton("Папка");
        loadButton = new JButton("Загрузить");
        filePath = new JTextField(30);

        bottomPanel.add(filePath);
        bottomPanel.add(fileChooseButton, "wrap");
        bottomPanel.add(loadButton, "span 2, align center");


        fileChooseButton.addActionListener(e -> {

            JFileChooser fileChooser = new JFileChooser();

            fileChooser.setDialogTitle("Выбор папки загрузки");
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            int res = fileChooser.showOpenDialog(this);

            if (res == JFileChooser.APPROVE_OPTION) {
                filePath.setText(fileChooser.getSelectedFile().getPath());
            }
        });

        add(bottomPanel);

        setTitle("Предложения от мо: загрузка");
        pack();
        Dimension sizeWindow = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(sizeWindow.width / 2 - getSize().width / 2, sizeWindow.height / 2 - getSize().height / 2);

        setResizable(false);
        setVisible(true);
    }


    public void setLoadAction(ActionListener actionListener) {
        loadButton.addActionListener(actionListener);
        filePath.addActionListener(actionListener);
    }

    public String getFilePathText() {
        return filePath.getText().trim();
    }

}
