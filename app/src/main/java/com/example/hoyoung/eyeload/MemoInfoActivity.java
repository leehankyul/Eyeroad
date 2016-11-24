package com.example.hoyoung.eyeload;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Jin on 2016-11-5.
 */

public class MemoInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private int key;
    MemoControl control = MemoControl.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_info);
        findViewById(R.id.memoInfoDelete).setOnClickListener(this);

        Intent intent = new Intent(this.getIntent());
        String memoKey = intent.getStringExtra("memoKey");
        TextView textView = (TextView) findViewById(R.id.memoInfoTextView);
        key = Integer.valueOf(memoKey);
        textView.setText(memoKey);
        control.getMemo(key);
    }

    public void onClick(View v) { // 메뉴의 버튼 선택 시 activity 이동
        switch (v.getId()) {
            case R.id.memoInfoDelete:
                deleteMemo();
        }
    }

    public void showMemoInfo() {
        //Control의 DTO를 가져와 Activity에 반환하거나 xml에 표시해줘야함
    }

    public void deleteMemo() {
        Toast.makeText(MemoInfoActivity.this, key + " is deleted.", Toast.LENGTH_SHORT).show();
        control.deleteInfo(key);
    }


}
