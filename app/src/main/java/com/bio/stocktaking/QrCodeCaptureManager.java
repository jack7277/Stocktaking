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

package com.bio.stocktaking;

import android.app.Activity;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.CompoundBarcodeView;

public class QrCodeCaptureManager extends CaptureManager {

    public static interface OnScanListener {
        void onResult(BarcodeResult result);
    }


    private OnScanListener mScanListener;


    public QrCodeCaptureManager(
            Activity activity, CompoundBarcodeView barcodeView,
            OnScanListener listener) {

        super(activity, barcodeView);
        setScanListener(listener);
    }

    public void setScanListener(OnScanListener listener) {
        mScanListener = listener;
    }

    @Override
    protected void returnResult(BarcodeResult rawResult) {
        mScanListener.onResult(rawResult);
    }
}
