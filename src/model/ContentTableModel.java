package model;

import model.database.DBWorkerMO;
import model.database.DBWorkerOffers;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

import static model.Constants.*;

public class ContentTableModel extends AbstractTableModel {

    private ArrayList<ArrayList<Object>> data = new ArrayList<>();
    private ArrayList<String> columnNames = new ArrayList<>();

    public ContentTableModel(OffersTabs offersTabs) {

        ArrayList<String> profiles = new DBWorkerOffers().getNamesProfile(offersTabs);

        columnNames.add("Название МО");

        for (String name : profiles) {
            columnNames.add(name);
        }

        columnNames.add("Название МО");

        DBWorkerMO dbWorkerMO = new DBWorkerMO();

        ArrayList<Integer> mos = dbWorkerMO.getActiveMos();

        for (Integer idMo : mos) {

            ArrayList<Object> row = new ArrayList<>();

            String nameMo = dbWorkerMO.getNameMO(idMo);
            row.add(nameMo);

            for (int k = 0; k < profiles.size(); k++) {

                // Добавление данных по профилям в таблицу
                row.add(0);
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
}
