package view;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class FrameMain extends JFrame {

    private MyMenuBar mainMenuBar;
    private PanelTreeMenu menuTreePanel;
    private JTabbedPane jTabbedPane;
//    private JButton tabCloseButton;


    public FrameMain() throws HeadlessException {

        setTitle("Объемы оказания медпомощи");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        mainMenuBar = new MyMenuBar();
        setJMenuBar(mainMenuBar);


        JSplitPane splitPanel = new JSplitPane();
        splitPanel.setDividerSize(7);
        splitPanel.setContinuousLayout(true);
        splitPanel.setOneTouchExpandable(true);
        splitPanel.setDividerLocation(210);

        splitPanel.addPropertyChangeListener(evt -> {
            if (evt.getPropertyName().equals("dividerLocation"))
                if ((Integer) evt.getNewValue() > 250)
                    splitPanel.setDividerLocation(250);
        });


        menuTreePanel = new PanelTreeMenu();
        jTabbedPane = new JTabbedPane(SwingConstants.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);

//        tabCloseButton = new JButton();

        JPanel rightPanel = new JPanel(new MigLayout("", "[100%]", "[100%]"));
        rightPanel.add(jTabbedPane, "width 100%, height 100%");

        splitPanel.setLeftComponent(menuTreePanel);
        splitPanel.setRightComponent(rightPanel);
//        splitPanel.setRightComponent(jTabbedPane);


        add(splitPanel);


        setSize(800, 600);
        setLocationRelativeTo(null);
    }

    public JFrame getFrame() {
        return this;
    }

    public MyMenuBar getMainMenuBar() {
        return mainMenuBar;
    }

    public PanelTreeMenu getMenuTreePanel() {
        return menuTreePanel;
    }

    public JTabbedPane getjTabbedPane() {
        return jTabbedPane;
    }

//    public JButton getTabCloseButton() {
//        return tabCloseButton;
//    }
}
