package com.bio.stocktaking.Parsers;

import android.content.Context;
import android.util.Log;

import com.bio.stocktaking.Databases.AllGoods_DB;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

import static android.content.ContentValues.TAG;
import static xdroid.toaster.Toaster.toast;

public class CSVparver {
    InputStreamReader inputStream;
    Context context;

    public CSVparver(InputStreamReader inputStream, Context context) {
        this.inputStream = inputStream;
        this.context = context;
    }

    public List<String[]> read() {
        List resultList = new ArrayList();
        BufferedReader reader = new BufferedReader(inputStream);
        try {
            String csvLine;
            while ((csvLine = reader.readLine()) != null) {
                String[] row = csvLine.split(";");
                resultList.add(row);
                Log.d("VariableTag", row[0].toString());
            }
        } catch (IOException ex) {
            throw new RuntimeException("Error in reading CSV file: " + ex);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                throw new RuntimeException("Error while closing input stream: " + e);
            }
        }
        return resultList;
    }

    public void saveToDB(List<String[]> s) {
        int index = 0;
        int size = s.size();

        AllGoods_DB stocktaking_db = new AllGoods_DB();
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();

        // индекс 0
        stocktaking_db.setID(0);
        stocktaking_db.setBarcode(0);
        stocktaking_db.setStockname("");
        stocktaking_db.setArtikul(0);
        stocktaking_db.setAmmount(0);
        stocktaking_db.setPrice(0.0);
        stocktaking_db.setBoxNumber("");
        realm.copyToRealmOrUpdate(stocktaking_db);

        for (int i = 1; i < size; i++) {
            String[] aS = null;
            try {
                aS = s.get(i);

                stocktaking_db.setID(i - 1);
                stocktaking_db.setBarcode(Long.parseLong(aS[0]));
                stocktaking_db.setStockname(aS[1]);
                stocktaking_db.setArtikul(Long.parseLong(aS[2]));
                stocktaking_db.setAmmount(Integer.parseInt(aS[3]));
                stocktaking_db.setPrice(Double.parseDouble(aS[4]));
                stocktaking_db.setBoxNumber("0");

                realm.copyToRealmOrUpdate(stocktaking_db);

            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d(TAG, "saveToDB: " + aS.toString() + size);
        }


        try {
            realm.commitTransaction();
            realm.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        toast("parse_successfull");

        Realm orgRealm = Realm.getDefaultInstance();


        try {
            // создаем в каталоге программы копию realm.backup
            String fname = "backupRealm.bak";
            String path2file = context.getFilesDir() + "/" + fname;
            orgRealm.writeCopyTo(new File(path2file));
            orgRealm.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}




