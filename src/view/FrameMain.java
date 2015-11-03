package view;

import javax.swing.*;
import java.awt.*;

public class FrameMain extends JFrame {

    private MyMenuBar mainMenuBar;

    public FrameMain() throws HeadlessException {

        setTitle("Объемы оказания медпомощи");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        mainMenuBar = new MyMenuBar();


        setJMenuBar(mainMenuBar);



        setSize(400, 300);
        setLocationRelativeTo(null);
    }

    public JFrame getFrame() {
        return this;
    }

    public MyMenuBar getMainMenuBar() {
        return mainMenuBar;
    }
}
