package com.cloudtech.antony;


import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.cloudtech.antony.spt.CTContentManager;
import com.cloudtech.antony.spt.CTContentRes;
import com.cloudtech.antony.sqlite.CreativeSqliteDao;
import com.cloudtech.antony.sqlite.CreativeVO;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Context context;
    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

    public static boolean isUSBConnected = false;
    public static final String SERVER_NAME = "com.cloudtech.antony/com.cloudtech.antony.access.ABService";

    public static String mainAmazon = "https://www.amazon.in";
    public static String amazon = "https://www.amazon.in/gp/product/B077PWBC7J/ref=as_li_tl?ie=UTF8&tag=cloudmobi20-21&camp=3638&creative=24630&linkCode=as2&creativeASIN=B077PWBC7J&linkId=db4ce0d4471b8a342fc93c4b7b221ebf";

    public static Handler handler = new Handler(Looper.myLooper());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this.getApplicationContext();
        checkPermission();

        final List<CreativeVO> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            CreativeVO creativeVO = new CreativeVO();
            creativeVO.cid = String.valueOf(i);
            creativeVO.setUrl("==://" + i);
            creativeVO.setStatus(1);
            list.add(creativeVO);
        }

        for (int i = 3; i < 5; i++) {
            CreativeVO creativeVO = new CreativeVO();
            creativeVO.cid = String.valueOf(i);
            creativeVO.setUrl("==://" + i);
            creativeVO.setStatus(0);
            list.add(creativeVO);
        }


        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CreativeSqliteDao.getInstance(context).insert(list);

            }
        });

        findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<CreativeVO> loaded = CreativeSqliteDao.getInstance(context).queryAllLoaded();

                Log.i(TAG, "onClick: >>qurey>" + loaded);
            }
        });

        findViewById(R.id.check).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<CreativeVO> loaded = CreativeSqliteDao.getInstance(context).deleteByType();

                Log.i(TAG, "onClick: >>delete>" + loaded);

            }
        });

        findViewById(R.id.open).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

    }

    //跳转系统自带界面 辅助功能界面
    private void open() {
        try {
            Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(intent);
            Toast.makeText(this, "打开辅助功能", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * DeepLink方式打开
     */
    private void openDeepLink(Context context, String deeplink) {
        Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(deeplink));
        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ComponentName componentName = it.resolveActivity(context.getPackageManager());
        if (componentName != null) {    //已经安装该应用
            context.startActivity(it);
        }
    }


    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : permissions) {
                if (context.checkCallingOrSelfPermission(permission) == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(this, permissions, 111);
                }
            }
        }
    }


    private String inputStreamToString(InputStream inputStream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] bytes = new byte[1024];
        int length;
        while ((length = inputStream.read(bytes)) != -1) {
            baos.write(bytes, 0, length);
        }
        baos.flush();
        return baos.toString();
    }


    String keyString = "ASIN:B07FJY24WL";
    String partern = "(ASIN|ISBN):\\s*([^\\s]+)";

    private void patten() {

        Pattern p = Pattern.compile(partern);
        Matcher m = p.matcher(keyString);
        boolean bo = m.find();
        CTLog.d(">>>" + bo);
        if (bo) {
            CTLog.d("===" + m.group());
        }

    }

}