package model;

public class User {

    private static User instance;

    private String login;
    private String name;
    private int role;

    private User() {

    }

    public static User getInstance() {

        if (instance == null) {
            instance = new User();
        }

        return instance;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
