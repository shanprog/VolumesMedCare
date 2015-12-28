package controller;

import model.Constants;
import model.ContentTableModel;
import model.User;
import model.database.DBWorkerOffers;
import view.ContentTablePanel;
import view.FrameMain;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class FrameMainController {

    private final FrameMain frameMain;
    private ArrayList<TreePath> openTabs;
    private ArrayList<Constants.OffersTabs> openTabsNums;

    public FrameMainController(User user) {

        frameMain = new FrameMain();
        openTabs = new ArrayList<>();
        openTabsNums = new ArrayList<>();

        frameMain.getMainMenuBar().setItemExitAction(e -> frameMain.getFrame().dispose());
        frameMain.getMainMenuBar().setItemLoadAction(e -> new FrameLoadController());
        frameMain.getMainMenuBar().setItemOffersOutXlsAction(e -> new FrameOffersOutXlsController());

        frameMain.getMenuTreePanel().setMouseListener(new NodeMouseListener(frameMain.getMenuTreePanel().getTree()));


        frameMain.getFrame().setVisible(true);
    }

    private class NodeMouseListener extends MouseAdapter {

        private JTree tree;

        public NodeMouseListener(JTree tree) {
            this.tree = tree;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();

            TreePath path = tree.getPathForRow(tree.getRowForLocation(x, y));

            if (e.getClickCount() == 2) {


                DefaultMutableTreeNode node;

                if (path != null) {
                    node = (DefaultMutableTreeNode) path.getLastPathComponent();

                    if (node != null && node.isLeaf()) {


                        if (!openTabs.contains(path)) {
                            ContentTablePanel contentTablePanel = new ContentTablePanel();
                            ContentTableModel tableModel = new ContentTableModel((Constants.OffersTabs) node.getUserObject());

                            contentTablePanel.setTableModel(tableModel);

                            setColumnColors(contentTablePanel.getTable(), (Constants.OffersTabs) node.getUserObject());


                            frameMain.getjTabbedPane().add(node.toString(), contentTablePanel);

                            openTabs.add(path);
                            openTabsNums.add((Constants.OffersTabs) node.getUserObject());


                            contentTablePanel.setSaveButtonActionListener(lambda -> {

                                DBWorkerOffers dbWorkerOffers = new DBWorkerOffers();
                                dbWorkerOffers.saveTableValues((ContentTableModel) contentTablePanel.getTable().getModel());
                            });
                        }


                        int index = openTabsNums.indexOf(node.getUserObject());
                        frameMain.getjTabbedPane().setSelectedIndex(index);


                    }

                }

            }
        }

        private void setColumnColors(JTable table, Constants.OffersTabs offersTabs) {


            DefaultTableCellRenderer defaultTableCellRenderer = new DefaultTableCellRenderer();
            defaultTableCellRenderer.setBackground(Color.LIGHT_GRAY);

            table.getColumnModel().getColumn(0).setCellRenderer(defaultTableCellRenderer);

            switch (offersTabs) {
                case HOURS_24:
                case HOURS_8:
                    table.getColumnModel().getColumn(table.getColumnCount() - 1).setCellRenderer(defaultTableCellRenderer);
                    table.getColumnModel().getColumn(table.getColumnCount() - 2).setCellRenderer(defaultTableCellRenderer);
                    break;
                case AMBUL_NEOT:
                case AMBUL_PROF:
                case AMBUL_ZAB:
                    table.getColumnModel().getColumn(table.getColumnCount() - 1).setCellRenderer(defaultTableCellRenderer);
                    table.getColumnModel().getColumn(table.getColumnCount() - 2).setCellRenderer(defaultTableCellRenderer);
                    table.getColumnModel().getColumn(table.getColumnCount() - 4).setCellRenderer(defaultTableCellRenderer);
                    break;
                case SMP:
                case OTHER:
                    table.getColumnModel().getColumn(table.getColumnCount() - 1).setCellRenderer(defaultTableCellRenderer);
                    break;
            }

        }

    }

}
