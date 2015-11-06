package controller;

import model.User;
import view.FrameLoadOffers;
import view.FrameMain;

public class FrameMainController {

    public FrameMainController(User user) {

        final FrameMain frameMain = new FrameMain();

        frameMain.getMainMenuBar().setItemExitAction(e -> frameMain.getFrame().dispose());
        frameMain.getMainMenuBar().setItemLoadAction(e -> new FrameLoadController());

        frameMain.getFrame().setVisible(true);
    }
}
