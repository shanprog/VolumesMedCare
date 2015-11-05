package model.exceptions;

import model.Constants.LoginExceptions;

public class UserException extends Exception {

    private String message;

    public UserException(LoginExceptions exc) {

        switch (exc) {
            case LOGIN_PAS_EXC:
                message = "Логин или пароль введен неправильно";
                break;
            case ACTUAL_EXC:
                message = "Пользователь не активен";
                break;
        }

    }

    @Override
    public String getMessage() {
        return message;
    }
}
