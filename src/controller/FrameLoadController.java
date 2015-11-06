package controller;

import model.database.DBWorkerOffers;
import view.FrameLoadOffers;
import view.FrameProgress;

import javax.swing.*;

import java.io.File;
import java.util.List;

public class FrameLoadController {

    private File[] files;
    private FrameProgress frameProgress;

    public FrameLoadController() {

        FrameLoadOffers frameLoadOffers = new FrameLoadOffers();

        frameLoadOffers.setLoadAction(e -> {
            if (frameLoadOffers.getFilePathText().equals(""))
                JOptionPane.showMessageDialog(frameLoadOffers, "Путь к папке не может быть пустым");
            else {

                try {
                    File myFolder = new File(frameLoadOffers.getFilePathText());
                    files = myFolder.listFiles();

                    if (files.length != 0) {

                        frameProgress = new FrameProgress(0, files.length);
                        frameProgress.setVisible(true);

                        new LoadOffersInBase().execute();
                    } else {
                        JOptionPane.showMessageDialog(frameLoadOffers, "В заданной папке нет файлов", "Внимание!", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NullPointerException npException) {
                    JOptionPane.showMessageDialog(frameLoadOffers, "Ошибка, неверный путь к папке", "Ошибка!", JOptionPane.ERROR_MESSAGE);
                }

            }
        });

    }

    private class LoadOffersInBase extends SwingWorker<String, Integer> {

        @Override
        protected String doInBackground() throws Exception {

            DBWorkerOffers dbWorkerOffers = new DBWorkerOffers();

            for (int i = 0; i < files.length; i++) {
                if (!dbWorkerOffers.processFile(files[i])) {
                    continue;
                }
                publish(i + 1);
            }

            return "";
        }

        @Override
        protected void process(List<Integer> chunks) {
            frameProgress.setValue(chunks.get(0));
        }

        @Override
        protected void done() {
            frameProgress.setVisible(false);
        }
    }


}
