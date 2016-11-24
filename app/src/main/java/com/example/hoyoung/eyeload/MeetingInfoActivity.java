package com.example.hoyoung.eyeload;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Jin on 2016-11-5.
 */

public class MeetingInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private int key;
    MeetingControl control = MeetingControl.getInstance();

    public MeetingInfoActivity() {


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this.getIntent());
        String meetingKey = intent.getStringExtra("meetingKey");
        key = Integer.valueOf(meetingKey);
        control.getMeeting(key);
        String ti = control.getMeetingTest().getTitle();

        setContentView(R.layout.activity_meeting_info);
        findViewById(R.id.meetingInfoDelete).setOnClickListener(this);
        TextView textView = (TextView) findViewById(R.id.meetingInfoTextView);

        try {
            Thread.sleep(2);
        } catch (Exception e) {
        }
        Log.d("TESTING", "MeetingInfoActivity getMeeting before");
        textView.setText(ti);

    }

    public void onClick(View v) { // 메뉴의 버튼 선택 시 activity 이동
        switch (v.getId()) {
            case R.id.meetingInfoDelete:
                deleteMeeting();
        }
    }

    public void navigateMeeting() {
        //showMapActivity를 호출하여 길을 안내하면 됨
    }

    public void deleteMeeting() {
        Toast.makeText(MeetingInfoActivity.this, key + " is deleted.", Toast.LENGTH_SHORT).show();
        control.deleteInfo(key);
    }
}
