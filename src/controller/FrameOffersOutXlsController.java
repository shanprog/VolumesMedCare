package controller;

import model.Constants.OffersTabs;
import model.ContentTableModel;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import view.FrameOffersOutXls;

import javax.swing.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


public class FrameOffersOutXlsController {

    private Set<OffersTabs> checkeds = new HashSet<>();

    public FrameOffersOutXlsController() {

        FrameOffersOutXls frameOut = new FrameOffersOutXls();

        frameOut.setOurActionListener(e -> {

            for (JCheckBox ch : frameOut.getCheckboxes()) {
                if (ch.isSelected())
                    checkeds.add(OffersTabs.toOffersTabsElement(ch.getText()));
                else
                    checkeds.remove(OffersTabs.toOffersTabsElement(ch.getText()));
            }

            try {
                Workbook wb = new HSSFWorkbook();


                for (OffersTabs ot : checkeds) {
                    Sheet sh = wb.createSheet(ot.toString());

                    ContentTableModel tableModel = new ContentTableModel(ot);

                    for (int rownum = 0; rownum < tableModel.getRowCount() + 1; rownum++) {

                        Row row = sh.createRow(rownum);

                        for (int cellnum = 0; cellnum < tableModel.getColumnCount(); cellnum++) {

                            Cell cell = row.createCell(cellnum);

                            if (rownum == 0) {
                                String address = tableModel.getColumnName(cellnum);
                                cell.setCellValue(address);
                            } else {
                                String address = String.valueOf(tableModel.getValueAt(rownum - 1, cellnum));

                                if (ot != OffersTabs.SMP && ot != OffersTabs.OTHER) {
                                    if (cellnum == 0 || cellnum == tableModel.getColumnCount() - 1)
                                        cell.setCellValue(address);
                                    else
                                        cell.setCellValue(Double.valueOf(address));
                                } else {
                                    if (cellnum == 0)
                                        cell.setCellValue(address);
                                    else
                                        cell.setCellValue(Double.valueOf(address));
                                }
                            }

                        }

                    }

                }



                FileOutputStream out = new FileOutputStream(frameOut.getFilePath() + "\\Предложения_" + LocalDate.now() + ".xls");
                wb.write(out);
                out.close();

            } catch (IOException e1) {
                e1.printStackTrace();
            }


        });
    }
}
