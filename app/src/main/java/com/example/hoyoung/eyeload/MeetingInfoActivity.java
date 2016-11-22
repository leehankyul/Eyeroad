package kr.soen.mypart;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by Jin on 2016-11-5.
 */

public class MeetingInfoActivity extends AppCompatActivity implements View.OnClickListener{

    private int key;
    MeetingControl control = MeetingControl.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_info);
        findViewById(R.id.meetingInfoDelete).setOnClickListener(this);

        Intent intent = new Intent(this.getIntent());
        String meetingKey = intent.getStringExtra("meetingKey");
        TextView textView=(TextView)findViewById(R.id.meetingInfoTextView);
        key = Integer.valueOf(meetingKey);
        textView.setText(meetingKey);
    }

    public void onClick(View v) { // 메뉴의 버튼 선택 시 activity 이동
        switch (v.getId()) {
            case R.id.meetingInfoDelete:
                Toast.makeText(MeetingInfoActivity.this, key + " is deleted.", Toast.LENGTH_SHORT).show();
                control.deleteInfo(key);
        }
    }

    public void navigateMeeting()
    {
        //showMapActivity를 호출하여 길을 안내하면 됨
    }
    public void deleteMeeting()
    {
        //Control의 delete()를 통해 Meeting을 삭제하면 됨
    }
}
