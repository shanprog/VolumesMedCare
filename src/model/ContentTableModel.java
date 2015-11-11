package model;

import model.database.DBWorkerMO;
import model.database.DBWorkerOffers;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

import static model.Constants.*;

public class ContentTableModel extends AbstractTableModel {

    private ArrayList<ArrayList<Object>> data = new ArrayList<>();
    private ArrayList<String> columnNames = new ArrayList<>();
    private ArrayList<String> profiles;

    private OffersTabs offersTabs;
    private DBWorkerOffers dbWorkerOffers;
    private DBWorkerMO dbWorkerMO;

    public ContentTableModel(OffersTabs offersTabs) {

        this.offersTabs = offersTabs;

        dbWorkerOffers = new DBWorkerOffers();
        dbWorkerMO = new DBWorkerMO();

        profiles = dbWorkerOffers.getNamesProfile(offersTabs);

        switch (offersTabs) {

            case HOURS_24:
            case HOURS_8:

                orgColumnNames();
                columnNames.add("Итого");
                columnNames.add("Название МО");

                orgRows();
                break;

            case AMBUL_PROF:
            case AMBUL_NEOT:
            case AMBUL_ZAB:
                orgColumnNames();

                columnNames.add("УЕТ");
                columnNames.add("Итого");
                columnNames.add("Название МО");

                orgRows();
                break;

            case SMP:
                orgColumnNames();
                columnNames.add("Итого");
                orgRows();
                break;

            case OTHER:

                columnNames.add("Название МО");

                for (int i = 0; i < profiles.size(); i++) {

                    if (i == 2) {
                        columnNames.add("Итого");
                    }

                    columnNames.add(profiles.get(i));
                }
                columnNames.add("Итого");

                orgRows();


                break;


        }


    }

    public OffersTabs getOffersTabs() {
        return offersTabs;
    }

    private void orgColumnNames() {
        // Организация колонок
        columnNames.add("Название МО");

        columnNames.addAll(profiles.stream().collect(Collectors.toList()));
    }

    private void orgRows() {
        // Организация строк

        ArrayList<Integer> mos = dbWorkerMO.getActiveMos();

        for (Integer idMo : mos) {

            ArrayList<Object> row = new ArrayList<>();

            String nameMo = dbWorkerMO.getNameMO(idMo);
            row.add(nameMo);

            HashMap<Integer, Integer> profilesValues;

            int sum = 0;
            switch (offersTabs) {
                case HOURS_24:
                    profilesValues = dbWorkerOffers.getValuesByProfilesFromHours24(idMo);
                    for (int i : Constants.planPatHours24) {
                        row.add(profilesValues.get(i));
                        sum += profilesValues.get(i);
                    }
                    row.add(sum);
                    break;

                case HOURS_8:
                    profilesValues = dbWorkerOffers.getValuesByProfilesFromHours8(idMo);
                    for (int i : Constants.planPatHours8) {
                        row.add(profilesValues.get(i));
                        sum += profilesValues.get(i);
                    }
                    row.add(sum);
                    break;

                case AMBUL_PROF:
                    profilesValues = dbWorkerOffers.getValuesByProfilesFromAmbul(idMo, Constants.planPatAmbulProf, "prof");

                    for (int i : Constants.planPatAmbulProf) {
                        row.add(profilesValues.get(i));
                        sum += profilesValues.get(i);
                    }

                    row.add(dbWorkerOffers.getValuesByProfilesFromUet(idMo, "prof"));
                    row.add(sum);

                    break;
                case AMBUL_NEOT:

                    profilesValues = dbWorkerOffers.getValuesByProfilesFromAmbul(idMo, Constants.planPatAmbulNeot, "neot");

                    for (int i : Constants.planPatAmbulNeot) {
                        row.add(profilesValues.get(i));
                        sum += profilesValues.get(i);
                    }

                    row.add(dbWorkerOffers.getValuesByProfilesFromUet(idMo, "neot"));
                    row.add(sum);

                    break;
                case AMBUL_ZAB:

                    profilesValues = dbWorkerOffers.getValuesByProfilesFromAmbul(idMo, Constants.planPatAmbulNeot, "zab");

                    for (int i : Constants.planPatAmbulNeot) {
                        row.add(profilesValues.get(i));
                        sum += profilesValues.get(i);
                    }

                    row.add(dbWorkerOffers.getValuesByProfilesFromUet(idMo, "zab"));
                    row.add(sum);

                    break;
                case SMP:
                    profilesValues = dbWorkerOffers.getValuesByProfilesFromSMP(idMo);
                    for (int i : Constants.planPatSmp) {
                        row.add(profilesValues.get(i));
                        sum += profilesValues.get(i);
                    }
                    row.add(sum);
                    break;
                case OTHER:
                    profilesValues = dbWorkerOffers.getValuesByProfilesFromOther(idMo);

                    for (int i = 0; i < planPatOther.length; i++) {

                        row.add(profilesValues.get(planPatOther[i]));
                        sum += profilesValues.get(planPatOther[i]);


                        if (i == 1) {
                            row.add(sum);

                        }
                        if (i == 2) {
                            sum = 0;
                        }


                    }

                    row.add(sum);

//                    for (int i : Constants.planPatOther) {
//                        row.add(profilesValues.get(i));
//                        sum += profilesValues.get(i);
//                    }
//                    row.add(sum);
//                    row.add(sum);
                    break;
            }


            row.add(nameMo);

            data.add(row);
        }


    }


    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return (data.get(rowIndex)).get(columnIndex);
    }

    @Override
    public String getColumnName(int column) {
        return columnNames.get(column);
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

        int colCount = getColumnCount();
        int oldSum;

        if (offersTabs == OffersTabs.SMP || offersTabs == OffersTabs.OTHER)
            oldSum = (Integer) (data.get(rowIndex)).get(colCount - 1);
        else
            oldSum = (Integer) (data.get(rowIndex)).get(colCount - 2);


        data.get(rowIndex).set(columnIndex, aValue);

        switch (offersTabs) {
            case HOURS_24:

                if (columnIndex > 0 && columnIndex < colCount - 2) {
                    int sum = 0;
                    for (int i = 0; i < planPatHours24.length; i++) {
                        sum += (Integer) (data.get(rowIndex)).get(i + 1);
                    }

                    data.get(rowIndex).set(colCount - 2, sum);
                }

                if (columnIndex == colCount - 2) {

                    double k = ((Integer) (data.get(rowIndex)).get(colCount - 2)) / ((double) oldSum);

                    int sum = 0;
                    for (int i = 0; i < planPatHours24.length; i++) {
                        int newVal = (int) ((Integer) (data.get(rowIndex)).get(i + 1) * k);
                        data.get(rowIndex).set(i + 1, newVal);

                        sum += newVal;
                    }

                    data.get(rowIndex).set(columnIndex, sum);
                }

                fireTableRowsUpdated(rowIndex, rowIndex);

                break;
            case HOURS_8:

                if (columnIndex > 0 && columnIndex < colCount - 2) {
                    int sum = 0;
                    for (int i = 0; i < planPatHours8.length; i++) {
                        sum += (Integer) (data.get(rowIndex)).get(i + 1);
                    }

                    data.get(rowIndex).set(colCount - 2, sum);
                }

                if (columnIndex == colCount - 2) {

                    double k = ((Integer) (data.get(rowIndex)).get(colCount - 2)) / ((double) oldSum);

                    int sum = 0;
                    for (int i = 0; i < planPatHours8.length; i++) {
                        int newVal = (int) ((Integer) (data.get(rowIndex)).get(i + 1) * k);
                        data.get(rowIndex).set(i + 1, newVal);

                        sum += newVal;
                    }

                    data.get(rowIndex).set(columnIndex, sum);
                }

                fireTableRowsUpdated(rowIndex, rowIndex);

                break;
            case AMBUL_PROF:

                if (columnIndex > 0 && columnIndex < colCount - 3) {
                    int sum = 0;
                    for (int i = 0; i < planPatAmbulProf.length; i++) {
                        sum += (Integer) (data.get(rowIndex)).get(i + 1);
                    }

                    data.get(rowIndex).set(colCount - 2, sum);
                }


                if (columnIndex == colCount - 2) {

                    double k = ((Integer) (data.get(rowIndex)).get(colCount - 2)) / ((double) oldSum);

                    int sum = 0;
                    for (int i = 0; i < planPatAmbulProf.length; i++) {
                        int newVal = (int) ((Integer) (data.get(rowIndex)).get(i + 1) * k);
                        data.get(rowIndex).set(i + 1, newVal);

                        sum += newVal;
                    }

                    data.get(rowIndex).set(columnIndex, sum);
                }

                fireTableRowsUpdated(rowIndex, rowIndex);
                break;

            case AMBUL_NEOT:
            case AMBUL_ZAB:

                if (columnIndex > 0 && columnIndex < colCount - 3) {
                    int sum = 0;
                    for (int i = 0; i < planPatAmbulNeot.length; i++) {
                        sum += (Integer) (data.get(rowIndex)).get(i + 1);
                    }

                    data.get(rowIndex).set(colCount - 2, sum);
                }

                if (columnIndex == colCount - 2) {

                    double k = ((Integer) (data.get(rowIndex)).get(colCount - 2)) / ((double) oldSum);

                    int sum = 0;
                    for (int i = 0; i < planPatAmbulNeot.length; i++) {
                        int newVal = (int) ((Integer) (data.get(rowIndex)).get(i + 1) * k);
                        data.get(rowIndex).set(i + 1, newVal);

                        sum += newVal;
                    }

                    data.get(rowIndex).set(columnIndex, sum);
                }

                fireTableRowsUpdated(rowIndex, rowIndex);
                break;
            case SMP:

                if (columnIndex > 0 && columnIndex < colCount - 1) {
                    int sum = 0;
                    for (int i = 0; i < planPatSmp.length; i++) {
                        sum += (Integer) (data.get(rowIndex)).get(i + 1);
                    }

                    data.get(rowIndex).set(colCount - 1, sum);
                }

                if (columnIndex == colCount - 1) {

                    double k = ((Integer) (data.get(rowIndex)).get(colCount - 1)) / ((double) oldSum);

                    int sum = 0;
                    for (int i = 0; i < planPatSmp.length; i++) {
                        int newVal = (int) ((Integer) (data.get(rowIndex)).get(i + 1) * k);
                        data.get(rowIndex).set(i + 1, newVal);

                        sum += newVal;
                    }

                    data.get(rowIndex).set(columnIndex, sum);
                }

                fireTableRowsUpdated(rowIndex, rowIndex);

                break;

            case OTHER:

                if (columnIndex > 0 && columnIndex < 3) {
                    int sum = (Integer) (data.get(rowIndex)).get(1) + (Integer) (data.get(rowIndex)).get(2);

                    data.get(rowIndex).set(3, sum);
                }

                if (columnIndex > 4 && columnIndex < colCount - 1) {
                    int sum = 0;
                    for (int i = 5; i < colCount - 1; i++) {
                        sum += (Integer) (data.get(rowIndex)).get(i);
                    }

                    data.get(rowIndex).set(colCount - 1, sum);
                }


                if (columnIndex == colCount - 1) {

                    double k = ((Integer) (data.get(rowIndex)).get(colCount - 1)) / ((double) oldSum);

                    int sum = 0;
                    for (int i = 0; i < planPatOther.length; i++) {
                        int newVal = (int) ((Integer) (data.get(rowIndex)).get(i + 1) * k);
                        data.get(rowIndex).set(i + 1, newVal);

                        sum += newVal;
                    }

                    data.get(rowIndex).set(columnIndex, sum);
                }

                fireTableRowsUpdated(rowIndex, rowIndex);

                break;
        }

    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {


        if (offersTabs == OffersTabs.SMP || offersTabs == OffersTabs.OTHER) {
            if (columnIndex == 0) return false;
        } else {
            if (columnIndex == 0 || columnIndex == getColumnCount() - 1) return false;
        }

        return true;


    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {

        int colCount = getColumnCount();

        switch (offersTabs) {
            case HOURS_24:
            case HOURS_8:

                for (int i = 1; i < profiles.size() + 1; i++) {
                    if (columnIndex == i)
                        return Integer.class;
                }

                if (columnIndex == (colCount - 2)) {
                    return Integer.class;
                }

                break;

            case AMBUL_PROF:
            case AMBUL_NEOT:
            case AMBUL_ZAB:

                if (columnIndex == colCount - 3)
                    return Double.class;

                for (int i = 1; i < colCount - 1; i++) {
                    return Integer.class;
                }

                break;

            case SMP:

                for (int i = 1; i < profiles.size() + 1; i++) {
                    if (columnIndex == i)
                        return Integer.class;
                }

                if (columnIndex == (colCount - 1)) {
                    return Integer.class;
                }

                break;

            case OTHER:

                if (columnIndex > 0) {
                    return Integer.class;
                }

        }

        return Object.class;

    }
}
