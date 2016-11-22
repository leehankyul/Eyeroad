package kr.soen.mypart;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by Jin on 2016-10-8.
 */

public class MemoManagementActivity extends AppCompatActivity {

    private ListView listView;
    private MemoControl control = MemoControl.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_management);

        setupListView();
        memoClicked();
    }

    //List내용을 xml에 추가하는 부분
    public void setupListView()
    {
        control.getAllMemo();

        listView = (ListView) findViewById(R.id.memoManagementListview1);
        listView.setAdapter(control);

        Toast.makeText(MemoManagementActivity.this,"cal", Toast.LENGTH_SHORT).show();
    }

    public void memoClicked() {

        // Memo list 선택 작동하는 부분
        AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MemoManagementActivity.this, control.getMemoList().get(position).getTitle()+" is clicked.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MemoManagementActivity.this,MemoInfoActivity.class);
                intent.putExtra("memoKey",String.valueOf(control.getMemoList().get(position).getKey()));
                startActivity(intent);

            }
        };
        listView.setOnItemClickListener(listener);
    }
}
