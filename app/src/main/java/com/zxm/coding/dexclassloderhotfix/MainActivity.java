package com.zxm.coding.dexclassloderhotfix;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

import dalvik.system.DexClassLoader;

public class MainActivity extends AppCompatActivity implements HotFixProvider {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String[] PERMISSIONS = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private Context mContext;
    private TextView mTipsTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        mTipsTv = findViewById(R.id.tv_tips);
        findViewById(R.id.btn_tips).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!PermissionChecker.checkSeriesPermissions(mContext, PERMISSIONS)) {
                    PermissionChecker.requestPermissions(MainActivity.this, PERMISSIONS, 100);
                } else {
                    showTips();
                }
            }
        });
    }

    private void showTips() {
        final String path =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                        .getAbsolutePath();
        final File patchFile = new File(path + File.separator + "test_hotfix.jar");
        if (!patchFile.exists()) {
            mTipsTv.setText(provide());
        } else {

            //TODO:当parent为null时会报类型转换异常，为什么？？；
            final DexClassLoader classLoader = new DexClassLoader(patchFile.getAbsolutePath(),
                    getExternalCacheDir().getAbsolutePath(), null, mContext.getClassLoader());

            try {
                final Class clz = classLoader
                        .loadClass("com.zxm.coding.dexclassloderhotfix.HotFixProviderImpl");
                HotFixProvider provider = (HotFixProvider) clz.newInstance();
                mTipsTv.setText(provider.provide());

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    @Override
    public String provide() {
        return TAG;
    }
}
