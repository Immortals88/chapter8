package com.bytedance.camera.demo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bytedance.camera.demo.utils.Utils;

import java.io.File;

public class PictureReview extends AppCompatActivity {
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        File imageFile = (File)getIntent().getSerializableExtra("file");
        setContentView(R.layout.activity_picture_review);
        imageView = findViewById(R.id.iv_pic);

        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
        bitmap= Utils.rotateImage(bitmap,imageFile.getAbsolutePath());
        imageView.setImageBitmap(bitmap);
        findViewById(R.id.btn_back).setOnClickListener(v -> {
            finish();
        });
    }
}
