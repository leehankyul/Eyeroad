package com.example.hoyoung.eyeload;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by Jin on 2016-10-8.
 */

public class MeetingListActivity extends AppCompatActivity {

    private MeetingControl control = MeetingControl.getInstance();
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        control.getAllMeeting(); // DB에서 Meeting에 관한 모든 정보를 가져옴
        setContentView(R.layout.activity_meeting_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try {
            Thread.sleep(1);
        } catch (Exception e) {
        }
        setupListView();
        meetingClicked();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//Activity 안의 버튼이 눌린 경우
                Intent intent = new Intent(MeetingListActivity.this, MakingMeetingActivity.class);

                startActivity(intent);
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
            }
        });
    }

    /*@Override
    protected  void onRestart(){
        super.onRestart();
        control.getAllMeeting();
        control.notifyDataSetChanged();
        listView.setAdapter(control);
        Toast.makeText(MeetingListActivity.this, "restart", Toast.LENGTH_SHORT).show();
    }*/

    /*@Override
    protected  void onResume(){
        super.onResume();
        Toast.makeText(MeetingListActivity.this, "resume", Toast.LENGTH_SHORT).show();
    }*/

    //List내용을 xml에 추가하는 부분
    public void setupListView() {
        //control.getAllMeeting();

        listView = (ListView) findViewById(R.id.meetingListview1);
        listView.setAdapter(control);

    }

    public void meetingClicked() {
        // Meeting list 선택 작동하는 부분
        AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MeetingListActivity.this, control.getMeetingList().get(position).getTitle() + " is clicked.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MeetingListActivity.this, MeetingInfoActivity.class);
                intent.putExtra("meetingKey", String.valueOf(control.getMeetingList().get(position).getKey()));
                startActivity(intent);
            }
        };

        listView.setOnItemClickListener(listener);
    }
}
