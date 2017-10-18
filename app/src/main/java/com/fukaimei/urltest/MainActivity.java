package com.fukaimei.urltest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    ImageView show;
    // 代表从网络下载得到的图片
    Bitmap bitmap;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x123) {
                // 使用ImageView显示该图片
                show.setImageBitmap(bitmap);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        show = (ImageView) findViewById(R.id.show);
        new Thread() {
            @Override
            public void run() {
                try {
                    // 定义一个URL对象
                    URL url = new URL("http://172.xx.xxx.xxx:8080/fukaimei/android_vs_ios.jpg");
                    // 打开该URL对应的资源的输入流
                    InputStream is = url.openStream();
                    // 从InputStream中解析出图片
                    bitmap = BitmapFactory.decodeStream(is);
                    // 发送消息，通知UI组件的资源的输入流
                    handler.sendEmptyMessage(0x123);
                    is.close();
                    // 再次打开URL对应的资源的输入流
                    is = url.openStream();
                    // 打开手机文件对应的输入流
                    OutputStream os = openFileOutput("android_vs_ios.jpg", MODE_PRIVATE);
                    byte[] buff = new byte[1024];
                    int hasRead = 0;
                    // 将URL对应的资源下载到本地
                    while ((hasRead = is.read(buff)) > 0) {
                        os.write(buff, 0, hasRead);
                    }
                    is.close();
                    os.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}












