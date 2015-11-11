package model.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DBWorkerMO {

    private Statement statement = DataBaseConnect.getStatement();
    private ResultSet resultSet;

    public int getIdMo(int nomer) {
        try {
            resultSet = statement.executeQuery(String.format("SELECT id_mo FROM mo_list WHERE nomer_mo = '%d'", nomer));

            while (resultSet.next())
                return resultSet.getInt("id_mo");

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        return 0;
    }

    public int getIdMo(String name) {
        try {
            resultSet = statement.executeQuery(String.format("SELECT id_mo FROM mo_list WHERE name = '%s'", name));

            while (resultSet.next())
                return resultSet.getInt("id_mo");

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        return 0;
    }

    public ArrayList<Integer> getActiveMos() {

        ArrayList<Integer> result= new ArrayList<>();

        try {
            resultSet = statement.executeQuery(String.format("SELECT id_mo FROM mo_list WHERE actual = '1' ORDER BY sort"));

            while (resultSet.next())
                result.add(resultSet.getInt("id_mo"));

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        return result;
    }

    public String getNameMO(int idMo) {

        String result = "";

        try {
            resultSet = statement.executeQuery(String.format("SELECT name FROM mo_list WHERE id_mo = '%d'", idMo));

            while (resultSet.next())
                result = resultSet.getString("name");

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        return result;
    }

}
