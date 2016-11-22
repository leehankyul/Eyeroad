package kr.soen.mypart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SearchActivity extends AppCompatActivity implements  View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        findViewById(R.id.gotoARviewButton).setOnClickListener(this);
    }

    public void onClick(View v){ // 메뉴의 버튼 선택 시 activity 이동
        switch(v.getId()){
            case R.id.gotoARviewButton:
                startActivity(new Intent(this, ArViewActivity.class));
                break;
        }
    }
}
