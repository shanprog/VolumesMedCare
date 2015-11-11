package view;

import model.Constants;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.event.ActionListener;

public class ContentTablePanel extends JPanel {

    private JTable table;
    private JButton save;


    public ContentTablePanel() {



        setLayout(new MigLayout("", "[100%]", "[100%]"));

        save = new JButton("Сохранить");

        JToolBar panelMenu = new JToolBar();
        panelMenu.add(save);

        table = new JTable();
//        table = new JTable(100, 15);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        add(panelMenu, "north");
        add(new JScrollPane(table), "width 100%, height 100%");
    }

    public void setTableModel(TableModel tableModel) {
        table.setModel(tableModel);
    }

    public void setSaveButtonActionListener(ActionListener actionListener) {
        save.addActionListener(actionListener);
    }

    public JTable getTable() {
        return table;
    }
}
