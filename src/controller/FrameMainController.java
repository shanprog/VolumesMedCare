package controller;

import model.Constants;
import model.ContentTableModel;
import model.User;
import view.ContentTablePanel;
import view.FrameMain;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FrameMainController {

    final FrameMain frameMain;

    public FrameMainController(User user) {

        frameMain = new FrameMain();

        frameMain.getMainMenuBar().setItemExitAction(e -> frameMain.getFrame().dispose());
        frameMain.getMainMenuBar().setItemLoadAction(e -> new FrameLoadController());

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

                        ContentTablePanel contentTablePanel = new ContentTablePanel();
                        ContentTableModel tableModel = new ContentTableModel((Constants.OffersTabs) node.getUserObject());

                        contentTablePanel.setTableModel(tableModel);

                        frameMain.getjTabbedPane().add(node.toString(), contentTablePanel);

                    }

                }

            }
        }

    }
}
