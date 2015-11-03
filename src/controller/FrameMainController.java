package controller;

import view.FrameMain;

public class FrameMainController {

    public FrameMainController() {

        final FrameMain frameMain = new FrameMain();

        frameMain.getMainMenuBar().setItemExitAction(e -> frameMain.getFrame().dispose());

        frameMain.getFrame().setVisible(true);
    }
}