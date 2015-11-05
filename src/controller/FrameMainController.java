package controller;

import model.User;
import view.FrameMain;

public class FrameMainController {

    public FrameMainController(User user) {

        final FrameMain frameMain = new FrameMain();

        frameMain.getMainMenuBar().setItemExitAction(e -> frameMain.getFrame().dispose());

        frameMain.getFrame().setVisible(true);
    }
}
