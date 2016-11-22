package kr.soen.mypart;

/**
 * Created by Jin on 2016-10-8.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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

public class MakingMeetingActivity extends Activity {

    //private EditText editTextKey;
    private EditText editTextTitle;
    private EditText editTextPlaceName;
    private EditText editTextMeetingInfo;
    private EditText editTextPublisher;
    private EditText editTextPassword;
    private Button placeSelect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_making_meeting);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        //editTextKey = (EditText) findViewById(R.id.meetingKey);
        editTextTitle = (EditText) findViewById(R.id.title);
        editTextPlaceName = (EditText) findViewById(R.id.placeName);
        editTextMeetingInfo = (EditText) findViewById(R.id.meetingInfo);
        editTextPublisher = (EditText) findViewById(R.id.publisher);
        editTextPassword = (EditText) findViewById(R.id.password);
        placeSelect=(Button)findViewById(R.id.select_place);
        placeSelect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                PlaceRadio();
            }

        });

    }

    public void insert(View view){
        //String meetingKey = editTextKey.getText().toString();
        String title = editTextTitle.getText().toString();
        String placeName = editTextPlaceName.getText().toString();
        String meetingInfo = editTextMeetingInfo.getText().toString();
        String publisher = editTextPublisher.getText().toString();
        String password = editTextPassword.getText().toString();

        //insertToDatabase(meetingKey, title, placeName,meetingInfo,publisher,password);
        insertToDatabase(title, placeName,meetingInfo,publisher,password);

    }

    //private void insertToDatabase(String meetingKey, String title,String placeName,String meetingInfo,String publisher, String password){
    private void insertToDatabase(String title,String placeName,String meetingInfo,String publisher, String password){
        class InsertData extends AsyncTask<String, Void, String>{
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MakingMeetingActivity.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... params) {

                try{
                    //String meetingKey = (String)params[0];
                    String title = (String)params[0];
                    String placeName = (String)params[1];
                    String meetingInfo = (String)params[2];
                    String publisher = (String)params[3];
                    String password = (String)params[4];

                    String link="http://210.94.194.201/insertMeeting.php";
                    //String data  = URLEncoder.encode("meetingKey", "UTF-8") + "=" + URLEncoder.encode(meetingKey, "UTF-8");
                    String data  = URLEncoder.encode("title", "UTF-8") + "=" + URLEncoder.encode(title, "UTF-8");
                    data += "&" + URLEncoder.encode("placeName", "UTF-8") + "=" + URLEncoder.encode(placeName, "UTF-8");
                    data += "&" + URLEncoder.encode("meetingInfo", "UTF-8") + "=" + URLEncoder.encode(meetingInfo, "UTF-8");
                    data += "&" + URLEncoder.encode("publisher", "UTF-8") + "=" + URLEncoder.encode(publisher, "UTF-8");
                    data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");

                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write( data );
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while((line = reader.readLine()) != null)
                    {
                        sb.append(line);
                        break;
                    }
                    return sb.toString();
                }
                catch(Exception e){
                    return new String("Exception: " + e.getMessage());
                }

            }
        }

        InsertData task = new InsertData();
        //task.execute(meetingKey, title,placeName, meetingInfo, publisher, password);
        task.execute(title,placeName, meetingInfo, publisher, password);
    }
    private void PlaceRadio(){
        final CharSequence[] Places = {"신공학관", "원흥관", "명진관","정보문화관P동","정보문화관Q동","학술문화관","사회과학관","경영관","다향관","본관"};
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        alt_bld.setTitle("모임 장소를 선택하세요");
        alt_bld.setSingleChoiceItems(Places, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                Toast.makeText(getApplicationContext(), "모임장소 = "+Places[item], Toast.LENGTH_SHORT).show();
                editTextPlaceName.setText(Places[item]);
                dialog.cancel();
            }
        });
        AlertDialog alert = alt_bld.create();
        alert.show();
    }
}
