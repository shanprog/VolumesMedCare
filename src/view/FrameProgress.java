package view;

import javax.swing.*;
import java.awt.*;

public class FrameProgress extends JFrame {

    private JProgressBar progressBar;

    public FrameProgress(int min, int max) throws HeadlessException {

        progressBar = new JProgressBar(min, max);
        progressBar.setStringPainted(true);

        add(progressBar);
        pack();

        Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(scrSize.width / 2 - getSize().width / 2, scrSize.height / 2 - getSize().height / 2);
        setResizable(false);

        setVisible(true);
    }

    public void setValue(int val) {
        progressBar.setValue(val);
    }


}
