package com.bio.stocktaking.FTPS;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Looper;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.realm.Realm;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;

import static com.bio.stocktaking.Stocktaking_MainActivity.BARCODES_CSV;
import static com.bio.stocktaking.Stocktaking_MainActivity.FTP_IP1;
import static com.bio.stocktaking.Stocktaking_MainActivity.FTP_IP2;


public class ConnectFtpDownloadXLSX extends AsyncTask<Void, Void, Void> {
    long retval;
    Context context=null;
//    TextView textLog = (TextView) findViewById(R.id.textLog);

    public ConnectFtpDownloadXLSX(Context context ) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
  //      textLog.setText("Start download");
    }

    @Override
    protected Void doInBackground(Void... voids) {
        File fileDownload = null;

        // certificate prepare
        TrustManager[] trustManager = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }};
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustManager, new SecureRandom());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        SSLSocketFactory sslSocketFactory = null;
        if (sslContext != null) {
            sslSocketFactory = sslContext.getSocketFactory();
        }
        // start connection
        final FTPClient client = new FTPClient();
        client.setSSLSocketFactory(sslSocketFactory);
        client.setSecurity(FTPClient.SECURITY_FTPS);
        // certificate prepare end
        try {
            client.connect(FTP_IP1, 990);
        } catch (IOException e) {
            boolean clientConnected = client.isConnected(); // ==false при ошибке
            if (!clientConnected) try {
                client.connect(FTP_IP2, 990);
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (FTPIllegalReplyException e1) {
                e1.printStackTrace();
            } catch (FTPException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } catch (FTPIllegalReplyException e) {
            e.printStackTrace();
        } catch (FTPException e) {
            e.printStackTrace();
        }

        client.setType(FTPClient.TYPE_BINARY);
//            client.setCompressionEnabled(true);
        String path = String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));

        path = String.valueOf(context.getFilesDir());

        if (client.isConnected()) {
            try {
                client.login("tj1", "tj1");
                client.setPassive(true); // Passive mode
                fileDownload = new File( path + "/" + BARCODES_CSV);
               // fileDownload.delete();
                fileDownload.createNewFile();
                retval = client.fileSize(BARCODES_CSV);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (Looper.myLooper() == null) {
                Looper.prepare();
            }

            try {
                final File finalFileDownload = fileDownload;
                client.download(BARCODES_CSV, fileDownload, new FTPDataTransferListener() {
                    public void transferred(int arg0) {
                        // arg0 - сколько байт передано, постоянно вызывается
                    }

                    public void started() {
                    }

                    public void failed() {

                        //showToast("Download failed");
                    }

                    // завершили передачу
                    public void completed() {
               //         showToast("Download successfully complete");
                        try {
                            client.disconnect(true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    public void aborted() {
//                        showToast("Transfer aborted, please try again...");
                    }
                });
            } catch (Exception e) {
                e.toString();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        Toast.makeText(context, String.valueOf(retval) + " bytes downloaded.", Toast.LENGTH_SHORT).show();
        Realm realm = Realm.getDefaultInstance();
        if (realm != null) {
            try {
                realm.beginTransaction();
//                realm.delete(AllGoods_DB.class);
//                realm.commitTransaction();
                realm.close();
            } catch (Exception e) {
            }
        }

    }
}
