package view;

import javax.swing.*;
import java.awt.event.ActionListener;

public class MyMenuBar extends JMenuBar {
    private JMenuItem itemLoad;
    private JMenuItem itemExit;

    public MyMenuBar() {

        JMenu menuFile = new JMenu("Файл");

        itemLoad = new JMenuItem("Загрузка предл. от мо");
        itemExit = new JMenuItem("Выход");

        menuFile.add(itemLoad);
        menuFile.addSeparator();
        menuFile.add(itemExit);


        add(menuFile);
    }

    public void setItemExitAction(ActionListener a) {
        itemExit.addActionListener(a);
    }


}
