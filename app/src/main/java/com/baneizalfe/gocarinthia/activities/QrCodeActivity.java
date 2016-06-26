package com.baneizalfe.gocarinthia.activities;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.baneizalfe.gocarinthia.App;
import com.baneizalfe.gocarinthia.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QrCodeActivity extends BaseActivity {

    @BindView(R.id.imageView)
    ImageView imageView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private QRCodeWriter qrcw;
    private final int IMAGE_SIZE = 240;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (App.getApp().getAuthToken() != null && App.getApp().getBeacon() != null) {
            qrcw = new QRCodeWriter();
            createCode();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void createCode() {
        try {
            // generate QR code

            String code = String.format("uid:%s;bid:%s", App.getApp().getAuthToken().id, App.getApp().getBeacon().identifier);
            BitMatrix bm = qrcw.encode(code, BarcodeFormat.QR_CODE, IMAGE_SIZE, IMAGE_SIZE);
            Bitmap bmp = Bitmap.createBitmap(IMAGE_SIZE, IMAGE_SIZE, Bitmap.Config.RGB_565);

            for (int x = 0; x < IMAGE_SIZE; x++) {
                for (int y = 0; y < IMAGE_SIZE; y++) {
                    bmp.setPixel(x, y, bm.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }

            bmp = bmp.createScaledBitmap(bmp, 600, 600, false);

            if (bm != null) {
                imageView.setImageBitmap(bmp);
            }

        } catch (WriterException e) {

        }
    }
}
