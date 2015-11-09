package view;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.event.MouseListener;

import static model.Constants.OffersTabs;

public class PanelTreeMenu extends JPanel {

    private JTree tree;

    public PanelTreeMenu() {
        setLayout(new MigLayout());

        DefaultMutableTreeNode year2015 = new DefaultMutableTreeNode("2015");
        DefaultMutableTreeNode offers = new DefaultMutableTreeNode("Предложения");

        for (OffersTabs ot : OffersTabs.values()) {
            offers.add(new DefaultMutableTreeNode(ot));
        }

        year2015.add(offers);

        DefaultTreeModel treeModel = new DefaultTreeModel(year2015);
        tree = new JTree(treeModel);


        add(new JScrollPane(tree), "width 100%, height 100%");
    }

    public void setMouseListener(MouseListener mouseListener) {
        tree.addMouseListener(mouseListener);
    }

    public JTree getTree() {
        return tree;
    }
}
