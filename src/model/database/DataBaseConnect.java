package model.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DataBaseConnect {

    private static DataBaseConnect instance;
    private static Statement statement;

    private DataBaseConnect() {

        try {
            String userName = "AdminMedCare";
            String url = "jdbc:mysql://192.168.1.11:3307/vol_med_care";
            String password = "4E8edI1E";
//            String url = "jdbc:mysql://localhost:3306/vol_med_care";
//            String password = "Nof7LE";
            Class.forName("com.mysql.jdbc.Driver").newInstance();

            Connection connection = DriverManager.getConnection(url, userName, password);
            statement = connection.createStatement();

        } catch (ClassNotFoundException cnfe) {
            System.err.println("ClassNotFoundException error");
            cnfe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static Statement getStatement() {
        if (instance == null)
            instance = new DataBaseConnect();

        return statement;
    }
}
