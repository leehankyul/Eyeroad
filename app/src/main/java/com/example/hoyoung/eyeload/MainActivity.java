package kr.soen.mypart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/**
 * Created by Jin on 2016-10-8.
 */

public class MainActivity extends AppCompatActivity implements  View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.searchButton).setOnClickListener(this);
        findViewById(R.id.memoManagementButton).setOnClickListener(this);
        findViewById(R.id.arViewButton).setOnClickListener(this);
        findViewById(R.id.meetingButton).setOnClickListener(this);

    }

    public void onClick(View v){ // 메뉴의 버튼 선택 시 activity 이동
        switch(v.getId()){
            case R.id.searchButton:
                startActivity(new Intent(this, SearchActivity.class));
                break;
            case R.id.memoManagementButton:
                startActivity(new Intent(this, MemoManagementActivity.class));
                break;
            case R.id.arViewButton:
                startActivity(new Intent(this, ArViewActivity.class));
                break;
            case R.id.meetingButton:
                startActivity(new Intent(this, MeetingListActivity.class));
                break;
        }
    }
}
