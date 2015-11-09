package view;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.TableModel;

public class ContentTablePanel extends JPanel {

    private JTable table;

    public ContentTablePanel() {

        setLayout(new MigLayout("", "[100%]", "[100%]"));

        table = new JTable();
//        table = new JTable(100, 15);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);


        add(new JScrollPane(table), "width 100%, height 100%");
    }

    public void setTableModel(TableModel tableModel) {
        table.setModel(tableModel);
    }
}
