/**
 * Android Jungle framework project.
 * <p>
 * Copyright 2016 Arno Zhang <zyfgood12@163.com>
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bio.stocktaking;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bio.stocktaking.Databases.AllGoods_DB;
import com.bio.stocktaking.FTPS.ConnectFtpDownloadXLSX;
import com.bio.stocktaking.Parsers.CSVparver;
import com.bio.stocktaking.activity.QRCodeScanActivity;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

import static xdroid.toaster.Toaster.toast;

public class Stocktaking_MainActivity extends AppCompatActivity {

    private static final int REQUEST_FOR_SCAN = 1;

    // ftp ip
    public static String FTP_IP1 = "213.177.x.x";
    final public static String FTP_IP2 = "95.79.x.x";
    final public static String BARCODES_CSV = "barcodes.csv";
    private static final String TAG = "DEBUG";

    public final static String BARCODE_FIELD = "barcode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        //
        Realm.init(this);
        // одна открытая база realm на одно андроид приложение
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name(getString(R.string.stocktaking))
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                .compactOnLaunch()
                .build();

        Realm.setDefaultConfiguration(realmConfiguration);
        //

        long maxmem = Runtime.getRuntime().maxMemory();
        ((TextView) findViewById(R.id.textViewMaxMerory)).setText(String.valueOf(maxmem));

        EditText editTextManualBarcode = findViewById(R.id.editTextManualBarcode);
        InputMethodManager keyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.showSoftInput((View) editTextManualBarcode.getWindowToken(), 0);


        editTextManualBarcode.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    onButtonFindBarcodeClick();
                    return true;
                }
                return false;
            }
        });
    }

    @OnClick(R.id.buttonFindBarcode)
    void onButtonFindBarcodeClick() {
        String barcode = null;
        try {
            barcode = String.valueOf(((EditText) findViewById(R.id.editTextManualBarcode)).getText()).trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (barcode.trim().isEmpty()) return;

        long bcode = Long.parseLong(barcode);

        Realm realm = Realm.getDefaultInstance();

        RealmResults<AllGoods_DB> items = null;
        try {
            items = realm.where(AllGoods_DB.class)
                    .equalTo(BARCODE_FIELD, bcode)
                    .findAll();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (items.size() == 0) {
            toast(getString(R.string.nothing_found));
            return;
        }

        String result = "";

        for (int i = 0; i < items.size(); i++) {
            try {
                long id = items.get(i).getID();
                long bc = items.get(i).getBarcode();
                String name = items.get(i).getStockname();
                long artikul = items.get(i).getArtikul();
                int kolvo = items.get(i).getAmmount();
                double price = items.get(i).getPrice();
                String boxnum = items.get(i).getBoxNumber();

                result = getString(R.string.id) + id + "\n"
                        + getString(R.string.barcode) + bc + "\n"
                        + getString(R.string.name) + name + "\n"
                        + getString(R.string.vcode) + artikul + "\n"
                        + getString(R.string.amount) + kolvo + "\n"
                        + getString(R.string.price) + price + "\n"
                        + getString(R.string.box_number) + boxnum + "\n\n";

                ((TextView) findViewById(R.id.textViewResult)).setText(result);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

//        private RealmPTStoListAdapter adapter = new RealmPTStoListAdapter(ptsItems);
//        final ListView listView = (ListView) findViewById(R.id.listView);
//        listView.setVisibility(View.VISIBLE);
//        listView.setAdapter(adapter);

        //((TextView) findViewById(R.id.textViewResult)).setText(barcode);
    }

    @OnClick(R.id.buttonScan)
    void buttonScanClick() {
        QRCodeScanActivity.start(this, REQUEST_FOR_SCAN);
    }

    @OnClick(R.id.buttonDownloadBarcodes)
    void buttonDownloadBarcodesClick() {
        FTP_IP1 = ((EditText) findViewById(R.id.editTextManualBarcode)).getText().toString().trim();

        if (isOnline()) {
            ConnectFtpDownloadXLSX downloadXLSX = new ConnectFtpDownloadXLSX(getApplicationContext());
            downloadXLSX.execute();
        } else {
            showToast(getString(R.string.no_internet));
        }
    }

    void showToast(final String err) {
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(), err, Toast.LENGTH_LONG).show();
            }
        });
    }


    // проверяем есть ли интернеты эти ваши
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = null;
        try {
            netInfo = cm.getActiveNetworkInfo();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        if (netInfo != null && netInfo.isConnected()) {
            return true;
        }

        return false;
    }


    @OnClick(R.id.buttonParseXLSX)
    void buttonParseXLSXClick() {
//        XLSXparser.parseXLSX(getApplicationContext());

//        XLSXparser parserXLSX = new XLSXparser(getApplicationContext());
//        parserXLSX.execute();
        String path;// = String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));

        path = String.valueOf(getApplicationContext().getFilesDir());

        try {
            FileInputStream myInputStream = new FileInputStream(path + "/" + BARCODES_CSV);
            CSVparver reader = new CSVparver(new InputStreamReader(myInputStream, "windows-1251"), getApplicationContext());//Specify asset file name
            List<String[]> arrayList = reader.read();
            reader.saveToDB(arrayList);

//            Log.d(TAG, arrayList.toString());

//            String [] nextLine;
//            while ((nextLine = reader.readNext()) != null) {
//                // nextLine[] is an array of values from the line
//                System.out.println(nextLine[0] + nextLine[1] + "etc...");
//                Log.d("VariableTag", nextLine[0]);
//            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, R.string.input_file_was_not_found, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_FOR_SCAN) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    String content = data.getStringExtra(QRCodeScanActivity.EXTRA_SCAN_CONTENT);
                    ((EditText) findViewById(R.id.editTextManualBarcode)).setText(content);
                    ((TextView) findViewById(R.id.textViewResult)).setText("");
                    //Toast.makeText(this, content, Toast.LENGTH_LONG).show();
                    onButtonFindBarcodeClick();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, R.string.scan_canceled, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public boolean onEditorAction(TextView exampleView, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_NULL
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            onButtonFindBarcodeClick();
        }
        return true;
    }

}
