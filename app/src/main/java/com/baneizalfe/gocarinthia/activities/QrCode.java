package com.baneizalfe.gocarinthia.activities;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.baneizalfe.gocarinthia.App;
import com.baneizalfe.gocarinthia.R;
import com.baneizalfe.gocarinthia.user.AuthToken;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class QrCode extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);
        ImageView image_view = (ImageView) findViewById(R.id.imageView);

        QRCodeWriter qrcw = new QRCodeWriter();

        try {
            // generate a 150x150 QR code
            AuthToken t = App.getApp().getAuthToken();
            String info = "uid:"+t.id+";bid:"+App.getApp().getBeaconId();

            BitMatrix bm = qrcw.encode(info, BarcodeFormat.QR_CODE, 150, 150);
            Bitmap bmp = Bitmap.createBitmap(150, 150, Bitmap.Config.RGB_565);

            for (int x = 0; x < 150; x++) {
                for (int y = 0; y < 150; y++) {
                    bmp.setPixel(x, y, bm.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }

            bmp = bmp.createScaledBitmap(bmp, 300, 300, false);

            if (bm != null) {
                image_view.setImageBitmap(bmp);
            }

        } catch (WriterException e) {

        }
    }
}
