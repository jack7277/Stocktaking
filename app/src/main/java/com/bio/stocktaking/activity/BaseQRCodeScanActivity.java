/**
 * Android Jungle framework project.
 *
 * Copyright 2016 Arno Zhang <zyfgood12@163.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bio.stocktaking.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;

import com.bio.stocktaking.QrCodeCaptureManager;
import com.bio.stocktaking.R;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CompoundBarcodeView;

public abstract class BaseQRCodeScanActivity extends AppCompatActivity {

    public static final String EXTRA_SCAN_CONTENT = "extra_scan_content";


    protected CompoundBarcodeView mBarcodeView;
    protected QrCodeCaptureManager mCapture;
    protected boolean mIsTorchOn = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        setContentView(getContentLayoutResId());
        findViewById(R.id.switch_torch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsTorchOn) {
                    mBarcodeView.setTorchOff();
                } else {
                    mBarcodeView.setTorchOn();
                }

                mIsTorchOn = !mIsTorchOn;
            }
        });

        mBarcodeView = findViewById(R.id.bar_code_view);
        mCapture = new QrCodeCaptureManager(this, mBarcodeView, mScanListener);
        mCapture.initializeFromIntent(getIntent(), savedInstanceState);
        mCapture.decode();

        mBarcodeView.setStatusText(null);
        setResult(Activity.RESULT_CANCELED);
    }

    @LayoutRes
    protected abstract int getContentLayoutResId();

    private QrCodeCaptureManager.OnScanListener mScanListener =
            new QrCodeCaptureManager.OnScanListener() {
                @Override
                public void onResult(BarcodeResult result) {
                    Intent data = new Intent();
                    data.putExtra(EXTRA_SCAN_CONTENT, result.getText());
                    setResult(Activity.RESULT_OK, data);
                    finish();
                }
            };

    @Override
    protected void onResume() {
        super.onResume();
        mCapture.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCapture.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCapture.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mCapture.onSaveInstanceState(outState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return mBarcodeView.onKeyDown(keyCode, event)
                || super.onKeyDown(keyCode, event);
    }
}
