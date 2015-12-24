package view;

import javafx.stage.FileChooser;
import model.Constants;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

public class FrameOffersOutXls extends JFrame {

    private ArrayList<JCheckBox> checkboxes = new ArrayList<>();
    private JTextField filePath;
    private JButton outButton;

    public FrameOffersOutXls() throws HeadlessException {

        setTitle("Выгрузка предложений в xls");
        setLayout(new MigLayout("", "[70%][30%]", ""));

        for (Constants.OffersTabs o : Constants.OffersTabs.values()) {
            checkboxes.add(new JCheckBox(o.toString(), false));
        }

        JPanel panelCheck = new JPanel();
        panelCheck.setLayout(new MigLayout());

        panelCheck.setBorder(BorderFactory.createEtchedBorder());

        for (int i = 0; i < checkboxes.size(); i++) {
            if (i % 2 != 0)
                panelCheck.add(checkboxes.get(i), "wrap");
            else
                panelCheck.add(checkboxes.get(i));
        }


        JPanel panelButton = new JPanel();
        panelButton.setLayout(new MigLayout());

        JButton selAll = new JButton("Выбрать все");
        JButton selNan = new JButton("Убрать все");

        panelButton.add(selAll, "wrap, sg rb");
        panelButton.add(selNan, "wrap, sg rb");

        selAll.addActionListener(e -> {
            for (JCheckBox chb : checkboxes) {
                chb.setSelected(true);
            }
        });

        selNan.addActionListener(e -> {
            for (JCheckBox chb : checkboxes) {
                chb.setSelected(false);
            }
        });


        JPanel filePathPanel = new JPanel();
        filePathPanel.setLayout(new MigLayout());

        JButton fileChooseButton = new JButton("Путь");
        filePath = new JTextField(32);
        filePath.setText( System.getProperty("user.home") +"\\Desktop");


        filePathPanel.add(filePath);
        filePathPanel.add(fileChooseButton, "wrap");

        fileChooseButton.addActionListener(e -> {

            JFileChooser fileChooser = new JFileChooser();

            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            int res = fileChooser.showDialog(null, "Выбор папки");

            if (res == JFileChooser.APPROVE_OPTION) {
                filePath.setText(fileChooser.getSelectedFile().getPath());
            }

        });


        JPanel downButtonPanel = new JPanel();
        downButtonPanel.setLayout(new MigLayout());
        outButton = new JButton("Выгрузить");
        JButton cancelButton = new JButton("Отмена");
        downButtonPanel.add(outButton, "sg db");
        downButtonPanel.add(cancelButton, "sg db");

        cancelButton.addActionListener(e -> dispose());


        add(panelCheck);
        add(panelButton, "wrap");
        add(filePathPanel, "span 2, wrap");
        add(downButtonPanel, "align center, span 2");

        setLocationRelativeTo(null);
        pack();
        setVisible(true);
    }

    public ArrayList<JCheckBox> getCheckboxes() {
        return checkboxes;
    }

    public String getFilePath() {
        return filePath.getText().trim();
    }

    public void setOurActionListener(ActionListener actionListener) {
        outButton.addActionListener(actionListener);
    }
}
