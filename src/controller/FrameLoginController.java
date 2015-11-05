package controller;

import model.Constants;
import model.database.DBWorkerUser;
import model.exceptions.UserException;
import org.apache.commons.codec.digest.DigestUtils;
import view.FrameLogin;

import javax.swing.*;
import java.util.Arrays;

public class FrameLoginController {

    private FrameLogin frameLogin;
    private DBWorkerUser dbWorkerUser;

    public FrameLoginController() {

        frameLogin = new FrameLogin();
        dbWorkerUser = new DBWorkerUser();

        frameLogin.setOkButtonAction(e -> {

            String login = frameLogin.getLogin();

            try {
                if (dbWorkerUser.userIsExist(login)) {

                    if (dbWorkerUser.userIsActual(login)) {
                        if (checkPassword()) {
                            new FrameMainController(dbWorkerUser.getUser(login));
                            frameLogin.setVisible(false);
                        } else
                            throw new UserException(Constants.LoginExceptions.LOGIN_PAS_EXC);
                    } else {
                        throw new UserException(Constants.LoginExceptions.ACTUAL_EXC);
                    }

                } else {
                    throw new UserException(Constants.LoginExceptions.LOGIN_PAS_EXC);
                }
            } catch (UserException ue) {
                JOptionPane.showMessageDialog(frameLogin, ue.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
            }

        });

    }

    private boolean checkPassword() {
        char[] passFromWindow = DigestUtils.md5Hex(Arrays.toString(frameLogin.getPassword())).toCharArray();
        char[] passFromDB = dbWorkerUser.getPassword(frameLogin.getLogin());

        return Arrays.equals(passFromDB, passFromWindow);
    }
}
