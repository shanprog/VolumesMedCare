package model.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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

}
