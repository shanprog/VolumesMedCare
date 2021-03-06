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

                columnNames.add("Итого -стоматология");
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
            int sumWithoutStom = 0;

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

                    for (int i = 0; i < Constants.planPatAmbulProf.length - 1; i++) {
                        sumWithoutStom += profilesValues.get(Constants.planPatAmbulProf[i]);
                    }

                    row.add(sumWithoutStom);
                    row.add(dbWorkerOffers.getValuesByProfilesFromUet(idMo, "prof"));
                    row.add(sum);

                    break;
                case AMBUL_NEOT:

                    profilesValues = dbWorkerOffers.getValuesByProfilesFromAmbul(idMo, Constants.planPatAmbulNeot, "neot");

                    for (int i : Constants.planPatAmbulNeot) {
                        row.add(profilesValues.get(i));
                        sum += profilesValues.get(i);
                    }

                    for (int i = 0; i < Constants.planPatAmbulNeot.length - 1; i++) {
                        sumWithoutStom += profilesValues.get(Constants.planPatAmbulNeot[i]);
                    }

                    row.add(sumWithoutStom);
                    row.add(dbWorkerOffers.getValuesByProfilesFromUet(idMo, "neot"));
                    row.add(sum);

                    break;
                case AMBUL_ZAB:

                    profilesValues = dbWorkerOffers.getValuesByProfilesFromAmbul(idMo, Constants.planPatAmbulNeot, "zab");

                    for (int i : Constants.planPatAmbulNeot) {
                        row.add(profilesValues.get(i));
                        sum += profilesValues.get(i);
                    }

                    for (int i = 0; i < Constants.planPatAmbulNeot.length - 1; i++) {
                        sumWithoutStom += profilesValues.get(Constants.planPatAmbulNeot[i]);
                    }

                    row.add(sumWithoutStom);
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

                    break;
            }


            row.add(nameMo);

            data.add(row);
        }


        // Добавление строки сумм снизу
        ArrayList<Object> sumRow = new ArrayList<>();

        sumRow.add("Итого");
        int to = 0;
        switch (offersTabs) {

            case HOURS_24:
            case HOURS_8:
            case AMBUL_PROF:
            case AMBUL_NEOT:
            case AMBUL_ZAB:
                to = getColumnCount() - 1;
                break;
            case SMP:
            case OTHER:
                to = getColumnCount();
                break;
        }


        for (int i = 1; i < to; i++) {

            if (
                    (offersTabs == OffersTabs.AMBUL_PROF ||
                            offersTabs == OffersTabs.AMBUL_NEOT ||
                            offersTabs == OffersTabs.AMBUL_ZAB)
                            && i == getColumnCount() - 3
                    ) {

                double sum = 0;
                for (int j = 0; j < getRowCount(); j++) {
                    sum += (Double) getValueAt(j, i);
                }

                sumRow.add(sum);

            } else {
                int sum = 0;
                for (int j = 0; j < getRowCount(); j++) {
                    sum += (Integer) getValueAt(j, i);
                }

                sumRow.add(sum);
            }

        }

        if (offersTabs != OffersTabs.OTHER || offersTabs != OffersTabs.SMP) {
            sumRow.add("Итого");
        }

        data.add(sumRow);


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
        int oldSum = 0;
        int oldSumWithoutStom = 0;

        if (offersTabs == OffersTabs.SMP || offersTabs == OffersTabs.OTHER) {
            if ((data.get(rowIndex)).get(colCount - 1) != null)
                oldSum = (Integer) (data.get(rowIndex)).get(colCount - 1);
        } else if (offersTabs == OffersTabs.HOURS_24 || offersTabs == OffersTabs.HOURS_8) {
            if ((data.get(rowIndex)).get(colCount - 2) != null)
                oldSum = (Integer) (data.get(rowIndex)).get(colCount - 2);
        } else {

            if ((data.get(rowIndex)).get(colCount - 2) != null)
                oldSum = (Integer) (data.get(rowIndex)).get(colCount - 2);

            if ((data.get(rowIndex)).get(colCount - 4) != null)
                oldSumWithoutStom = (Integer) (data.get(rowIndex)).get(colCount - 4);
        }


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

                if (columnIndex > 0 && columnIndex < colCount - 4) {

                    int sum = 0;
                    int sumWithoutStom = 0;

                    for (int i = 0; i < planPatAmbulProf.length; i++) {
                        sum += (Integer) (data.get(rowIndex)).get(i + 1);
                    }

                    for (int i = 0; i < planPatAmbulProf.length - 1; i++) {
                        sumWithoutStom += (Integer) (data.get(rowIndex)).get(i + 1);
                    }

                    data.get(rowIndex).set(colCount - 2, sum);
                    data.get(rowIndex).set(colCount - 4, sumWithoutStom);
                }


                if (columnIndex == colCount - 4) {

                    double k = ((Integer) (data.get(rowIndex)).get(columnIndex)) / ((double) oldSumWithoutStom);

                    int sum = 0;
                    int sumWithoutStom = 0;

                    for (int i = 0; i < planPatAmbulProf.length - 1; i++) {
                        int newVal = (int) ((Integer) (data.get(rowIndex)).get(i + 1) * k);
                        data.get(rowIndex).set(i + 1, newVal);

                        sumWithoutStom += newVal;
                        sum += newVal;
                    }

                    sum += ((Integer) (data.get(rowIndex)).get(planPatAmbulProf.length));

                    data.get(rowIndex).set(columnIndex, sumWithoutStom);
                    data.get(rowIndex).set(colCount - 2, sum);
                }


                if (columnIndex == colCount - 2) {

                    double k = ((Integer) (data.get(rowIndex)).get(columnIndex)) / ((double) oldSum);

                    int sum = 0;
                    int sumWithoutStom = 0;

                    for (int i = 0; i < planPatAmbulProf.length; i++) {
                        int newVal = (int) ((Integer) (data.get(rowIndex)).get(i + 1) * k);
                        data.get(rowIndex).set(i + 1, newVal);

                        sum += newVal;
                        sumWithoutStom += newVal;
                    }

                    sumWithoutStom -= ((Integer) (data.get(rowIndex)).get(planPatAmbulProf.length));

                    data.get(rowIndex).set(columnIndex, sum);
                    data.get(rowIndex).set(colCount - 4, sumWithoutStom);
                }


                fireTableRowsUpdated(rowIndex, rowIndex);
                break;

            case AMBUL_NEOT:
            case AMBUL_ZAB:

                if (columnIndex > 0 && columnIndex < colCount - 4) {

                    int sum = 0;
                    int sumWithoutStom = 0;

                    for (int i = 0; i < planPatAmbulNeot.length; i++) {
                        sum += (Integer) (data.get(rowIndex)).get(i + 1);
                    }

                    for (int i = 0; i < planPatAmbulNeot.length - 1; i++) {
                        sumWithoutStom += (Integer) (data.get(rowIndex)).get(i + 1);
                    }

                    data.get(rowIndex).set(colCount - 2, sum);
                    data.get(rowIndex).set(colCount - 4, sumWithoutStom);
                }


                if (columnIndex == colCount - 4) {

                    double k = ((Integer) (data.get(rowIndex)).get(columnIndex)) / ((double) oldSumWithoutStom);

                    int sum = 0;
                    int sumWithoutStom = 0;

                    for (int i = 0; i < planPatAmbulNeot.length - 1; i++) {
                        int newVal = (int) ((Integer) (data.get(rowIndex)).get(i + 1) * k);
                        data.get(rowIndex).set(i + 1, newVal);

                        sumWithoutStom += newVal;
                        sum += newVal;
                    }

                    sum += ((Integer) (data.get(rowIndex)).get(planPatAmbulNeot.length));

                    data.get(rowIndex).set(columnIndex, sumWithoutStom);
                    data.get(rowIndex).set(colCount - 2, sum);
                }


                if (columnIndex == colCount - 2) {

                    double k = ((Integer) (data.get(rowIndex)).get(columnIndex)) / ((double) oldSum);

                    int sum = 0;
                    int sumWithoutStom = 0;

                    for (int i = 0; i < planPatAmbulNeot.length; i++) {
                        int newVal = (int) ((Integer) (data.get(rowIndex)).get(i + 1) * k);
                        data.get(rowIndex).set(i + 1, newVal);

                        sum += newVal;
                        sumWithoutStom += newVal;
                    }

                    sumWithoutStom -= ((Integer) (data.get(rowIndex)).get(planPatAmbulNeot.length));

                    data.get(rowIndex).set(columnIndex, sum);
                    data.get(rowIndex).set(colCount - 4, sumWithoutStom);
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

        if (getColumnClass(columnIndex).equals(Double.class)) {
            data.get(getRowCount() - 1).set(columnIndex, getColumnSum(columnIndex));
        } else if (getColumnClass(columnIndex).equals(Integer.class)) {
            data.get(getRowCount() - 1).set(columnIndex, (int) getColumnSum(columnIndex));
        }

        switch (offersTabs) {
            case HOURS_24:

                if (columnIndex == getColumnCount() - 2) {
                    for (int i = 0; i < planPatHours24.length; i++) {
                        data.get(getRowCount() - 1).set(i + 1, (int) getColumnSum(i + 1));
                    }
                }

                data.get(getRowCount() - 1).set(getColumnCount() - 2, (int) getColumnSum(getColumnCount() - 2));
                break;
            case HOURS_8:
                if (columnIndex == getColumnCount() - 2) {
                    for (int i = 0; i < planPatHours8.length; i++) {
                        data.get(getRowCount() - 1).set(i + 1, (int) getColumnSum(i + 1));
                    }
                }

                data.get(getRowCount() - 1).set(getColumnCount() - 2, (int) getColumnSum(getColumnCount() - 2));
                break;
            case AMBUL_PROF:

                if (columnIndex == getColumnCount() - 2 || columnIndex == getColumnCount() - 4) {
                    for (int i = 0; i < planPatAmbulProf.length; i++) {
                        data.get(getRowCount() - 1).set(i + 1, (int) getColumnSum(i + 1));
                    }
                }

                data.get(getRowCount() - 1).set(getColumnCount() - 2, (int) getColumnSum(getColumnCount() - 2));
                data.get(getRowCount() - 1).set(getColumnCount() - 4, (int) getColumnSum(getColumnCount() - 4));
                break;
            case AMBUL_NEOT:
            case AMBUL_ZAB:

                if (columnIndex == getColumnCount() - 2 || columnIndex == getColumnCount() - 4) {
                    for (int i = 0; i < planPatAmbulNeot.length; i++) {
                        data.get(getRowCount() - 1).set(i + 1, (int) getColumnSum(i + 1));
                    }
                }

                data.get(getRowCount() - 1).set(getColumnCount() - 2, (int) getColumnSum(getColumnCount() - 2));
                data.get(getRowCount() - 1).set(getColumnCount() - 4, (int) getColumnSum(getColumnCount() - 4));
                break;
            case SMP:
                if (columnIndex == getColumnCount() - 1) {
                    for (int i = 0; i < planPatSmp.length; i++) {
                        data.get(getRowCount() - 1).set(i + 1, (int) getColumnSum(i + 1));
                    }
                }

                data.get(getRowCount() - 1).set(getColumnCount() - 1, (int) getColumnSum(getColumnCount() - 1));
                break;
            case OTHER:
                if (columnIndex == getColumnCount() - 1) {
                    for (int i = 0; i < planPatOther.length; i++) {
                        data.get(getRowCount() - 1).set(i + 1, (int) getColumnSum(i + 1));
                    }
                }
                data.get(getRowCount() - 1).set(getColumnCount() - 1, (int) getColumnSum(getColumnCount() - 1));
                break;
        }


        fireTableRowsUpdated(getRowCount() - 1, getRowCount() - 1);

    }

    private double getColumnSum(int col) {

        double res = 0;

        for (int i = 0; i < getRowCount() - 1; i++) {

            if (getColumnClass(col).equals(Integer.class)) {
                res += (Integer) getValueAt(i, col);
            } else if (getColumnClass(col).equals(Double.class)) {
                res += (Double) getValueAt(i, col);
            }

        }

        return res;
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
