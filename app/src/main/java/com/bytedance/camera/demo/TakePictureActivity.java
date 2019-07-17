package com.bytedance.camera.demo;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bytedance.camera.demo.utils.Utils;

import java.io.File;

public class TakePictureActivity extends AppCompatActivity {

    private ImageView imageView;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_EXTERNAL_STORAGE = 101;
    private File imageFile;
    String[] permissions = new String[] {
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_picture);
        imageView = findViewById(R.id.img);
        findViewById(R.id.btn_picture).setOnClickListener(v -> {
            if (Utils.isPermissionsReady(TakePictureActivity.this,permissions)) {
                takePicture();
            } else {
                //todo 在这里申请相机、存储的权限
                Utils.reuqestPermissions(TakePictureActivity.this,permissions,REQUEST_EXTERNAL_STORAGE);
            }
        });

    }

    private void takePicture() {
        //todo 打开相机
        Intent cameraIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imageFile=Utils.getOutputMediaFile(Utils.MEDIA_TYPE_IMAGE);
        Uri uri;
        if (imageFile!=null){
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N)
                uri= FileProvider.getUriForFile(this, "com.bytedance.camera.demo",imageFile);
            else
                uri=Uri.fromFile(imageFile);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
        }
        startActivityForResult(cameraIntent,REQUEST_IMAGE_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
        //todo 处理返回数据

            setPic();

        }
    }

    private void setPic() {
        //todo 根据imageView裁剪
        //todo 根据缩放比例读取文件，生成Bitmap

        //todo 如果存在预览方向改变，进行图片旋转

        //todo 如果存在预览方向改变，进行图片旋转
        //todo 显示图片
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds=true;
        BitmapFactory.decodeFile(imageFile.getAbsolutePath(),bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;
        int scaleFactor = Math.min(photoH/targetH,photoW/targetW);
        bmOptions.inJustDecodeBounds=false;
        bmOptions.inSampleSize= scaleFactor;
        bmOptions.inPurgeable=true;
        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath(),bmOptions);
        bitmap=Utils.rotateImage(bitmap,imageFile.getAbsolutePath());
        imageView.setImageBitmap(bitmap);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                //todo 判断权限是否已经授予
                if (Utils.isPermissionsReady(TakePictureActivity.this,permissions)){
                    takePicture();
                }
                else{
                    Utils.reuqestPermissions(this,permissions,REQUEST_EXTERNAL_STORAGE);
                }
                break;
            }
        }
    }
}
