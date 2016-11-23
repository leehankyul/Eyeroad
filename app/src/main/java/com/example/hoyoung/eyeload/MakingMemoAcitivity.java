package kr.soen.mypart;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

public class MakingMemoAcitivity extends AppCompatActivity {
    private MemoControl control = MemoControl.getInstance();
    private EditText mTitleText;
    private EditText mBodyText;
    private ImageView mImage;
    private ImageView mIcon1;
    private ImageView mIcon2;
    private ImageView mIcon3;
    private ImageView selectedIcon;
    private String mImageString;
    private int mIconID;
    final static int ACT_EDIT = 0;
    private int mSelectedIndexSet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_making_memo_acitivity);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setTitle("메모 만들기");
        mTitleText = (EditText) findViewById(R.id.title);
        mBodyText = (EditText) findViewById(R.id.body);
        selectedIcon = (ImageView) findViewById(R.id.selected_icon_img);
        mImage = (ImageView) findViewById(R.id.imageresult);
        findViewById(R.id.imageupload).setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(MakingMemoAcitivity.this, CameraCropActivity.class);
                        startActivityForResult(intent, ACT_EDIT);
                    }
                }
        );
        mIcon1 = (ImageView) findViewById(R.id.icon1);
        mIcon1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedIcon.setImageResource(R.drawable.ic_action_name);
                mIconID=1;
            }
        });
        mIcon2 = (ImageView) findViewById(R.id.icon2);
        mIcon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedIcon.setImageResource(R.drawable.ic2_action_name);
                mIconID=2;
            }
        });
        mIcon3 = (ImageView) findViewById(R.id.icon3);
        mIcon3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedIcon.setImageResource(R.drawable.ic3_action_name);
                mIconID=3;
            }
        });
        final CheckedTextView ctv = (CheckedTextView) findViewById(R.id.public_or_private);
        ctv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ctv.isChecked()) {
                    ctv.setChecked(false);
                    mSelectedIndexSet=1;
                }
                else {
                    ctv.setChecked(true);
                    mSelectedIndexSet=0;
                }
            }
        });
        Button saveButton = (Button) findViewById(R.id.save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                setResult(RESULT_OK);
                makeMemo();
                finish();
            }

        });

    }
    public void makeMemo()
    {
        Log.d("TEST","MakingMemoActi : " + mImageString);
        control.setInfo(mTitleText.getText().toString(),(double)1,(double)1,(double)1,mBodyText.getText().toString(),"2010",mImageString,1,"deviceId",1);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ACT_EDIT:
                if(resultCode == RESULT_OK) {
                    mImage.setImageBitmap((Bitmap)data.getParcelableExtra("image"));
                    mImageString=bitmapToByteArray((Bitmap)data.getParcelableExtra("image"));
                }
                break;
        }
    }
    public String bitmapToByteArray(Bitmap bitmap){//가져온 이미지를 바이트 배열로 변환하고 문자열로 변환
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        String profileImageBase64 = Base64.encodeToString(byteArray, 0);
        return profileImageBase64;
    }

}
