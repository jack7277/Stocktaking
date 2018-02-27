package com.bio.stocktaking.Parsers;

import android.content.Context;
import android.os.AsyncTask;

import io.realm.Realm;

public class XLSXparser extends AsyncTask<Void, Void, Void> {
    long retval;
    Context context = null;
//    TextView textLog = (TextView) findViewById(R.id.textLog);

    public XLSXparser(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //      textLog.setText("Start download");
    }

    @Override
    protected Void doInBackground(Void... voids) {
//        // Открываем эксель файл
//        XSSFWorkbook workbook = null;
//        FileInputStream inputStream = null;
//        XSSFSheet sheet, sh = null;
//        OPCPackage pkg;
//
//        String path;// = String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
//
//        path = String.valueOf(context.getFilesDir());
//
//
//        try {
//            BufferedInputStream bis = null;
//            FileInputStream myInputStream = new FileInputStream(path + "/" + BARCODES_CSV);
//            bis = new BufferedInputStream(myInputStream);
//            pkg = OPCPackage.open(myInputStream);
//            // XSSFWorkbook, File
////            pkg = OPCPackage.open(new File(path + "/" + BARCODES_CSV));
//            workbook = new XSSFWorkbook(pkg);
//
////            SXSSFWorkbook wb = new SXSSFWorkbook(workbook); // keep 100 rows in memory, exceeding rows will be flushed to disk
////            wb.setCompressTempFiles(true); // temp files will be gzipped
//
//
//            // Открываем первую вкладку в файле эксель, индекс 0
//            if (workbook != null) {
//                 sheet = workbook.getSheetAt(0);
////                sh = wb.getXSSFWorkbook().getSheetAt(0);// .createSheet();
//            }
//        } catch (Exception e) {
//            // нечего нам дальше делать, если файла нет, уходим отсюда
//            e.printStackTrace();
//            if (Looper.myLooper() == null) Looper.prepare();
//
//            Toast.makeText(context, "no input task file, error", Toast.LENGTH_SHORT).show();
//            return null;
//        }
//
//
//        AllGoods_DB stocktaking_db = new AllGoods_DB();
//        LinkedList<AllGoods_DB> listObj = new LinkedList<>();
//
//        deleteRealmDB();
//
//        Realm realm = Realm.getDefaultInstance();
//
//        realm.beginTransaction();
//
//        for (int i = 1; i < 57103; i++) {
//            XSSFRow row = sh.getRow(i); // перебираем строки сверху - вниз
//            stocktaking_db.setID(i - 1);
//
//            // колонка Штрихкод
//            try {
//                if (row.getCell(0).getCellTypeEnum() == CellType.STRING) {
//                    String text = row.getCell(0).getStringCellValue();
//                    stocktaking_db.setBarcode(Long.parseLong(text));
//                }
//
//                if (row.getCell(0).getCellTypeEnum() == CellType.NUMERIC) {
//                    Number number = row.getCell(0).getNumericCellValue();
//                    long num = Double.valueOf(String.valueOf(number)).longValue();
//                    stocktaking_db.setBarcode(num);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            // колонка Название
//            try {
//                String text = row.getCell(1).getStringCellValue();
//                stocktaking_db.setStockname(text);
//            } catch (Exception e) {
//                e.printStackTrace();
//                //  при ошибке чтения ячейки с названием, ставим пустую строку
//                stocktaking_db.setStockname("");
//            }
//
//            // колонка Артикул
//            try {
//                if (row.getCell(2).getCellTypeEnum() == CellType.STRING) {
//                    String text = row.getCell(2).getStringCellValue();
//                    stocktaking_db.setArtikul(Long.parseLong(text));
//                }
//
//                if (row.getCell(2).getCellTypeEnum() == CellType.NUMERIC) {
//                    Number number = row.getCell(2).getNumericCellValue();
//                    long num = Double.valueOf(String.valueOf(number)).longValue();
//                    stocktaking_db.setArtikul(num);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            // Количество
//            try {
//                if (row.getCell(3).getCellTypeEnum() == CellType.STRING) {
//                    String text = row.getCell(3).getStringCellValue();
//                    stocktaking_db.setAmmount(Integer.parseInt(text));
//                }
//
//                if (row.getCell(3).getCellTypeEnum() == CellType.NUMERIC) {
//                    Number number = row.getCell(3).getNumericCellValue();
//                    stocktaking_db.setAmmount((int) Math.round((double) number));
//                }
//            } catch (NumberFormatException e) {
//                e.printStackTrace();
//                //  при ошибке чтения ячейки с количеством, ставим = 0
//                stocktaking_db.setAmmount(0);
//            }
//
//            // цена
//            try {
//                if (row.getCell(4).getCellTypeEnum() == CellType.STRING) {
//                    String text = row.getCell(4).getStringCellValue();
//                    stocktaking_db.setPrice(Double.parseDouble(text));
//
//                }
//
//                if (row.getCell(4).getCellTypeEnum() == CellType.NUMERIC) {
//                    Number number = row.getCell(4).getNumericCellValue();
//                    stocktaking_db.setPrice((Double) number);
//                }
//            } catch (Exception e) {
//                //  при ошибке чтения ячейки с ценой, ставим = 0.0
//                e.printStackTrace();
//                stocktaking_db.setPrice(0.0);
//            }
//
//
//            // колонка Номер коробки
//            try {
//                stocktaking_db.setBoxNumber("");
//            } catch (Exception e) {
//            }
//
////             realm.copyToRealmOrUpdate(stocktaking_db);
////            listObj.add(stocktaking_db);
//
////            try {
////                realm.copyToRealmOrUpdate(stocktaking_db);
////            } catch (Exception e) {
////                realm.cancelTransaction();
////            }
//
////
////            if (Looper.myLooper() == null) {
////                Looper.prepare();
////            }
//
//            if ((i % 5000) == 0) {
////                toast("lines read: " + i);
////                realm.copyToRealm(listObj);
////                realm.commitTransaction();
////                realm.beginTransaction();
//                listObj.clear();
////                System.gc();
//            }
//
//        }
//
////        try {
////            realm.commitTransaction();
////            realm.close();
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
//
//
//        try {
//            realm.commitTransaction();
//            pkg.close();
//            realm.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        toast("parse_successfull");

        return null;
    }


    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);

    }


    void deleteRealmDB() {
        Realm realm = Realm.getDefaultInstance();
        if (realm != null) {
            try {
                realm.beginTransaction();
                //realm.delete(PTS_DB.class);
                realm.deleteAll();
                realm.commitTransaction();
                realm.close();
            } catch (Exception e) {
            }
        }
    }
}





