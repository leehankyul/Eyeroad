package com.example.hoyoung.eyeload;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivityHoYoung extends Activity implements OnClickListener {

    Button btn_intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT>=23) {  //버전확인
            String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
            int i = 0;
            int permissionCode = 0;
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {

                } else {//권한 없음
                    ActivityCompat.requestPermissions(this, permissions, permissionCode);
                }
            }
        }
        setContentView(R.layout.activity_mainHoYoung);
        btn_intent = (Button)findViewById(R.id.btn_intent);
        btn_intent.setOnClickListener(this);


    }
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        Intent intent = new Intent(MainActivityHoYoung.this, ARActivity.class );
        startActivity(intent);
      //  finish();
    }
}
