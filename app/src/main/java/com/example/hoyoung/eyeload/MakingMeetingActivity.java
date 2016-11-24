package com.example.hoyoung.eyeload;


/**
 * Created by Jin on 2016-10-8.
 */

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Jin on 2016-11-5.
 */

public class MakingMeetingActivity extends Activity {

    MeetingControl control = MeetingControl.getInstance();
    //private EditText editTextKey;
    private EditText editTextTitle;
    private EditText editTextPlaceName;
    private EditText editTextMeetingInfo;
    private EditText editTextPublisher;
    private EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_making_meeting);

        //editTextKey = (EditText) findViewById(R.id.meetingKey);
        editTextTitle = (EditText) findViewById(R.id.title);
        editTextPlaceName = (EditText) findViewById(R.id.placeName);
        editTextMeetingInfo = (EditText) findViewById(R.id.meetingInfo);
        editTextPublisher = (EditText) findViewById(R.id.publisher);
        editTextPassword = (EditText) findViewById(R.id.password);

    }

    public void insert(View view) {
        //String meetingKey = editTextKey.getText().toString();
        String title = editTextTitle.getText().toString();
        String placeName = editTextPlaceName.getText().toString();
        String meetingInfo = editTextMeetingInfo.getText().toString();
        String publisher = editTextPublisher.getText().toString();
        String password = editTextPassword.getText().toString();
        Toast.makeText(MakingMeetingActivity.this, title + " is opened.", Toast.LENGTH_SHORT).show();
        //insertToDatabase(meetingKey, title, placeName,meetingInfo,publisher,password);
        control.setInfo(title, placeName, meetingInfo, publisher, password);

    }

    //private void insertToDatabase(String meetingKey, String title,String placeName,String meetingInfo,String publisher, String password){

}
