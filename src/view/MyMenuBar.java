package view;

import javax.swing.*;
import java.awt.event.ActionListener;

public class MyMenuBar extends JMenuBar {
    private JMenuItem itemLoad;
    private JMenuItem itemExit;

    private JMenuItem itemOffersOutXls;

    public MyMenuBar() {

        JMenu menuFile = new JMenu("Файл");
        JMenu menuOut = new JMenu("Выгрузка");

        itemLoad = new JMenuItem("Загрузка предл. от мо");
        itemExit = new JMenuItem("Выход");

        itemOffersOutXls = new JMenuItem("Выгрузка предложений в xls");

        menuFile.add(itemLoad);
        menuFile.addSeparator();
        menuFile.add(itemExit);

        menuOut.add(itemOffersOutXls);

        add(menuFile);
        add(menuOut);
    }

    public void setItemExitAction(ActionListener a) {
        itemExit.addActionListener(a);
    }

    public void setItemLoadAction(ActionListener a) {
        itemLoad.addActionListener(a);
    }

    public void setItemOffersOutXlsAction(ActionListener a) {
        itemOffersOutXls.addActionListener(a);
    }
}
