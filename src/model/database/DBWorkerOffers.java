package model.database;

import model.Constants;
import model.Constants.OffersTabs;
import model.ContentTableModel;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class DBWorkerOffers {

    private Statement statement = DataBaseConnect.getStatement();
    private ResultSet resultSet;

    private final static int YEAR = 2014;

    HSSFWorkbook wb;

    HSSFSheet sheet;
    HSSFRow row;
    HSSFCell cell;

    public DBWorkerOffers() {

    }

    public boolean processFile(File file) {

        ArrayList<String> queries;

        int idMo;

        try {
            POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(file.getAbsolutePath()));
            queries = new ArrayList<>();

            wb = new HSSFWorkbook(fs);


            idMo = getIdMo();

            process_24h(idMo, queries);
            process_8h(idMo, queries);
            process_ambul(idMo, queries);
            process_smp(idMo, queries);
            process_other(idMo, queries);

//            for (String q : queries) {
//                System.out.println(q);
//            }

            runQueries(queries);

        } catch (IOException ioException) {
            JOptionPane.showMessageDialog(null, "Файл не может быть прочитан. " + ioException.toString());
        } catch (Exception e) {
            System.out.println(file.getName());
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public int getIdMo() {
        sheet = wb.getSheetAt(0);
        row = sheet.getRow(6);
        cell = row.getCell(0);

        DBWorkerMO dbWorkerMO = new DBWorkerMO();
        return dbWorkerMO.getIdMo((int) cell.getNumericCellValue());
    }

    private void runQueries(ArrayList<String> queries) {

        try {
            for (String query : queries) {
                statement.execute(query);
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

    }

    private void process_24h(int idMo, ArrayList<String> queries) {

        sheet = wb.getSheetAt(0);
        queries.add(String.format("DELETE FROM offers_hours_24 WHERE id_mo = '%d' AND year='%d';", idMo, YEAR));

        for (int i = 14; i < 47; i++) {
            row = sheet.getRow(i);

            int bed = (int) row.getCell(1).getNumericCellValue();
            int hosp = (int) row.getCell(2).getNumericCellValue();
            int kdays = (int) row.getCell(3).getNumericCellValue();

            int idprofile = Constants.planPatHours24[i - 14];

            queries.add(String.format("INSERT INTO offers_hours_24 VALUES(NULL, '%d', '%d', '%d', '%d', '%d', '%d');",
                    idMo, idprofile, bed, hosp, kdays, YEAR));

        }

    }

    private void process_8h(int idMo, ArrayList<String> queries) {

        sheet = wb.getSheetAt(1);
        queries.add(String.format("DELETE FROM offers_hours_8 WHERE id_mo = '%d' AND year='%d';", idMo, YEAR));

        for (int i = 5; i < 21; i++) {
            row = sheet.getRow(i);

            int outP = (int) row.getCell(1).getNumericCellValue();
            int kdays = (int) row.getCell(2).getNumericCellValue();

            int idprofile = Constants.planPatHours8[i - 5];

            queries.add(String.format("INSERT INTO offers_hours_8 VALUES(NULL, '%d', '%d', '%d', '%d', '%d');",
                    idMo, idprofile, outP, kdays, YEAR));

        }

    }

    private void process_ambul(int idMo, ArrayList<String> queries) {

        sheet = wb.getSheetAt(2);

        queries.add(String.format("DELETE FROM offers_ambul_prof WHERE id_mo = '%d' AND year='%d';", idMo, YEAR));

        int numberOfProfile = 0;
        for (int i = 6; i < 37; i++) {

            if (i == 33)
                continue;

            row = sheet.getRow(i);

            int prof = (int) row.getCell(1).getNumericCellValue();

            int idprofile = Constants.planPatAmbulProf[numberOfProfile++];

            queries.add(String.format("INSERT INTO offers_ambul_prof VALUES(NULL, '%d', '%d', '%d', '%d');",
                    idMo, idprofile, prof, YEAR));
        }


        queries.add(String.format("DELETE FROM offers_ambul_neot WHERE id_mo = '%d' AND year='%d';", idMo, YEAR));

        numberOfProfile = 0;
        for (int i = 6; i < 37; i++) {

            if (i >= 31 && i <= 35)
                continue;

            row = sheet.getRow(i);

            int neot = (int) row.getCell(3).getNumericCellValue();
            int zab = (int) row.getCell(5).getNumericCellValue();

            int idprofile = Constants.planPatAmbulNeot[numberOfProfile++];

            queries.add(String.format("INSERT INTO offers_ambul_neot VALUES(NULL, '%d', '%d', '%d', '%d');",
                    idMo, idprofile, neot, YEAR));
            queries.add(String.format("INSERT INTO offers_ambul_zab VALUES(NULL, '%d', '%d', '%d', '%d');",
                    idMo, idprofile, zab, YEAR));
        }


        queries.add(String.format("DELETE FROM offers_ambul_uet WHERE id_mo = '%d' AND year='%d';", idMo, YEAR));

        row = sheet.getRow(36);

        double prof_uet = (double) row.getCell(2).getNumericCellValue();
        double neot_uet = (double) row.getCell(4).getNumericCellValue();
        double zab_uet = (double) row.getCell(6).getNumericCellValue();

        int idprofile = 43;

        queries.add(String.format("INSERT INTO offers_ambul_uet VALUES(NULL, '%d', '%d', '%f', '%f', '%f', '%d');",
                idMo, idprofile, prof_uet, neot_uet, zab_uet, YEAR));
    }

    private void process_smp(int idMo, ArrayList<String> queries) {

        sheet = wb.getSheetAt(3);
        queries.add(String.format("DELETE FROM offers_smp WHERE id_mo = '%d' AND year='%d';", idMo, YEAR));

        for (int i = 5; i < 8; i++) {
            row = sheet.getRow(i);

            int offer = (int) row.getCell(2).getNumericCellValue();


            int idprofile = Constants.planPatSmp[i - 5];

            queries.add(String.format("INSERT INTO offers_smp VALUES(NULL, '%d', '%d', '%d', '%d');",
                    idMo, idprofile, offer, YEAR));
        }

    }

    private void process_other(int idMo, ArrayList<String> queries) {

        sheet = wb.getSheetAt(4);
        queries.add(String.format("DELETE FROM offers_other WHERE id_mo = '%d' AND year='%d';", idMo, YEAR));

        int numberOfProfile = 0;
        for (int i = 6; i < 16; i++) {

            if (i == 8)
                continue;

            row = sheet.getRow(i);

            int offer = 0;
            if (i < 10) {
                offer = (int) row.getCell(2).getNumericCellValue();
            } else {
                offer = (int) row.getCell(1).getNumericCellValue();
            }


            int idprofile = Constants.planPatOther[numberOfProfile++];

            queries.add(String.format("INSERT INTO offers_other VALUES(NULL, '%d', '%d', '%d', '%d');",
                    idMo, idprofile, offer, YEAR));
        }

    }

    public ArrayList<String> getNamesProfile(OffersTabs offersTabs) {

        ArrayList<String> result = new ArrayList<>();
        int[] profilesId = new int[0];
        String table = "";

        switch (offersTabs) {
            case HOURS_24:
                profilesId = Constants.planPatHours24;
                table = "offers_hours_24";
                break;
            case HOURS_8:
                profilesId = Constants.planPatHours8;
                table = "offers_hours_8";
                break;
            case AMBUL_PROF:
                profilesId = Constants.planPatAmbulProf;
                table = "offers_ambul_prof";
                break;

            case AMBUL_NEOT:
            case AMBUL_ZAB:
                profilesId = Constants.planPatAmbulNeot;
                table = "offers_ambul_neot";
                break;
            case SMP:
                profilesId = Constants.planPatSmp;
                table = "offers_smp";
                break;
            case OTHER:
                profilesId = Constants.planPatOther;
                table = "offers_other";
                break;
        }

        for (int i : profilesId) {
            try {
                resultSet = statement.executeQuery(String.format("SELECT name FROM profiles WHERE id_profile = '%d'", i));

                while (resultSet.next())
                    result.add(resultSet.getString("name"));

            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }
        }

        return result;
    }


    public HashMap<Integer, Integer> getValuesByProfilesFromHours24(int idMo) {

        HashMap<Integer, Integer> result = new HashMap<>();
        int[] profiles = Constants.planPatHours24;

        for (int i : profiles) {
            result.put(i, 0);
        }


        try {
            resultSet = statement.executeQuery(String.format("SELECT id_profile, offer FROM offers_hours_24 WHERE id_mo = '%d' AND year = '%d'", idMo, YEAR));

            while (resultSet.next())
                result.replace(resultSet.getInt("id_profile"), resultSet.getInt("offer"));

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        return result;

    }

    public HashMap<Integer, Integer> getValuesByProfilesFromHours8(int idMo) {

        HashMap<Integer, Integer> result = new HashMap<>();
        int[] profiles = Constants.planPatHours8;


        for (int i : profiles) {
            result.put(i, 0);
        }


        try {
            resultSet = statement.executeQuery(String.format("SELECT id_profile, offer FROM offers_hours_8 WHERE id_mo = '%d' AND year = '%d'", idMo, YEAR));

            while (resultSet.next())
                result.replace(resultSet.getInt("id_profile"), resultSet.getInt("offer"));

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        return result;

    }

    public HashMap<Integer, Integer> getValuesByProfilesFromAmbul(int idMo, int[] profiles, String name) {

        HashMap<Integer, Integer> result = new HashMap<>();

        for (int i : profiles) {
            result.put(i, 0);
        }

        try {
            resultSet = statement.executeQuery(String.format("SELECT id_profile, offer FROM offers_ambul_%s WHERE id_mo = '%d' AND year = '%d'", name, idMo, YEAR));

            while (resultSet.next())
                result.replace(resultSet.getInt("id_profile"), resultSet.getInt("offer"));

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        return result;

    }

    public double getValuesByProfilesFromUet(int idMo, String name) {

        double result = 0;

        try {
            resultSet = statement.executeQuery(String.format("SELECT %s FROM offers_ambul_uet WHERE id_mo = '%d' AND year = '%d'", name, idMo, YEAR));

            while (resultSet.next())
                result = resultSet.getDouble(String.format("%s", name));

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        return result;

    }


    public HashMap<Integer, Integer> getValuesByProfilesFromSMP(int idMo) {

        HashMap<Integer, Integer> result = new HashMap<>();
        int[] profiles = Constants.planPatSmp;

        for (int i : profiles) {
            result.put(i, 0);
        }


        try {
            resultSet = statement.executeQuery(String.format("SELECT id_profile, offer FROM offers_smp WHERE id_mo = '%d' AND year = '%d'", idMo, YEAR));

            while (resultSet.next())
                result.replace(resultSet.getInt("id_profile"), resultSet.getInt("offer"));

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        return result;

    }

    public HashMap<Integer, Integer> getValuesByProfilesFromOther(int idMo) {

        HashMap<Integer, Integer> result = new HashMap<>();
        int[] profiles = Constants.planPatOther;

        for (int i : profiles) {
            result.put(i, 0);
        }


        try {
            resultSet = statement.executeQuery(String.format("SELECT id_profile, offer FROM offers_other WHERE id_mo = '%d' AND year = '%d'", idMo, YEAR));

            while (resultSet.next())
                result.replace(resultSet.getInt("id_profile"), resultSet.getInt("offer"));

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        return result;

    }


    public void saveTableValues(ContentTableModel tableModel) {

        ArrayList<String> queries = new ArrayList<>();

        switch (tableModel.getOffersTabs()) {
            case HOURS_24:
                queries = updateDataQueries(tableModel, Constants.planPatHours24);
                break;

            case HOURS_8:
                queries = updateDataQueries(tableModel, Constants.planPatHours8);
                break;



            case AMBUL_PROF:
                queries = updateDataQueries(tableModel, Constants.planPatAmbulProf);
                break;
            case AMBUL_NEOT:
            case AMBUL_ZAB:
                queries = updateDataQueries(tableModel, Constants.planPatAmbulNeot);
                break;
            case SMP:
                queries = updateDataQueries(tableModel, Constants.planPatSmp);
                break;
            case OTHER:
                System.out.println(tableModel.getOffersTabs());
                break;
        }


        for (String q : queries) {
            System.out.println(q);
        }
    }

    private ArrayList<String> updateDataQueries(ContentTableModel tableModel, int[] profiles) {

        ArrayList<String> queries = new ArrayList<>();

        String tableName = "";

        switch (tableModel.getOffersTabs()) {
            case HOURS_24:
                tableName += "hours_24";
                break;
            case HOURS_8:
                tableName += "hours_8";
                break;
            case AMBUL_PROF:
                tableName += "ambul_prof";
                break;
            case AMBUL_NEOT:
                tableName += "ambul_neot";
                break;
            case AMBUL_ZAB:
                tableName += "ambul_zab";
                break;
            case SMP:
                tableName += "smp";
                break;
            case OTHER:


        }


        for (int row = 0; row < tableModel.getRowCount() - 1; row++) {
            int idMo = new DBWorkerMO().getIdMo((String) tableModel.getValueAt(row, 0));

            for (int column = 1; column <= profiles.length; column++) {
                queries.add(String.format("UPDATE offers_'%s' SET offer='%d' WHERE id_mo = '%d' AND id_profile='%d'",
                        tableName, (Integer) tableModel.getValueAt(row, column), idMo, profiles[column - 1]));
            }

            if (tableModel.getOffersTabs() == OffersTabs.AMBUL_PROF) {
                queries.add(String.format("UPDATE offers_ambul_uet SET prof='%f' WHERE id_mo = '%d' AND id_profile='%d'",
                        (Double) tableModel.getValueAt(row, profiles.length+1), idMo, 43));
            }

            if (tableModel.getOffersTabs() == OffersTabs.AMBUL_NEOT) {
                queries.add(String.format("UPDATE offers_ambul_uet SET neot='%f' WHERE id_mo = '%d' AND id_profile='%d'",
                        (Double) tableModel.getValueAt(row, profiles.length+1), idMo, 43));
            }

            if (tableModel.getOffersTabs() == OffersTabs.AMBUL_ZAB) {
                queries.add(String.format("UPDATE offers_ambul_uet SET zab='%f' WHERE id_mo = '%d' AND id_profile='%d'",
                        (Double) tableModel.getValueAt(row, profiles.length+1), idMo, 43));
            }

        }

        return queries;
    }


}
