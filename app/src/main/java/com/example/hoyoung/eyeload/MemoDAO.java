package kr.soen.mypart;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InterfaceAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.sql.Date;
import java.util.ArrayList;

/**
 * Created by Jin on 2016-11-17.
 */

public class MemoDAO extends DAO{

    private ArrayList<MemoDTO> arrayListMemoDTO = new ArrayList<>();
    public boolean insert(MemoDTO dto)
    {
        class InsertData extends AsyncTask<String, Void, String>{

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //loading = ProgressDialog.show(MakingMeetingActivity.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //loading.dismiss();
                //Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... params) {

                try{

                    //String memoKey = (String)params[0];
                    String title = (String)params[0];
                    String x = (String)params[1];
                    String y = (String)params[2];
                    String z = (String)params[3];
                    String content = (String)params[4];
                    String date = (String)params[5];
                    String image = (String)params[6];
                    String iconId = (String)params[7];
                    String deviceID = (String)params[8];
                    String visibility = (String)params[9];


                    String link="http://210.94.194.201/insertMemo.php";
                    //String data  = URLEncoder.encode("meetingKey", "UTF-8") + "=" + URLEncoder.encode(meetingKey, "UTF-8");
                    //meetingKey는 자동으로 설정됨
                    String data  = URLEncoder.encode("title", "UTF-8") + "=" + URLEncoder.encode(title, "UTF-8");
                    data += "&" + URLEncoder.encode("x", "UTF-8") + "=" + URLEncoder.encode(x, "UTF-8");
                    data += "&" + URLEncoder.encode("y", "UTF-8") + "=" + URLEncoder.encode(y, "UTF-8");
                    data += "&" + URLEncoder.encode("z", "UTF-8") + "=" + URLEncoder.encode(z, "UTF-8");
                    data += "&" + URLEncoder.encode("content", "UTF-8") + "=" + URLEncoder.encode(content, "UTF-8");
                    data += "&" + URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(date, "UTF-8");
                    data += "&" + URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(image, "UTF-8");
                    data += "&" + URLEncoder.encode("iconId", "UTF-8") + "=" + URLEncoder.encode(iconId, "UTF-8");
                    data += "&" + URLEncoder.encode("deviceID", "UTF-8") + "=" + URLEncoder.encode(deviceID, "UTF-8");
                    data += "&" + URLEncoder.encode("visibility", "UTF-8") + "=" + URLEncoder.encode(visibility, "UTF-8");


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
        //Json을 이용하기 때문에 형변환이 필요
        String title = dto.getTitle();
        String x = String.valueOf(dto.getX());
        String y = String.valueOf(dto.getY());
        String z = String.valueOf(dto.getZ());
        String content = dto.getContent();
        String date = dto.getDate();
        String image = dto.getImage();
        String iconId = String.valueOf(dto.getIconId());
        String deviceID = dto.getDeviceID();
        String visibility = String.valueOf(dto.getVisibility());

        InsertData task = new InsertData();
        //task.execute(meetingKey, title,placeName, meetingInfo, publisher, password);
        task.execute(title,x,y,z,content,date,image,iconId,deviceID,visibility);

        return true;
    }
    public boolean delete(int key){
        String memoKey = String.valueOf(key);
        class DeleteInfo extends AsyncTask<String, Void, String> {
            //ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //loading = ProgressDialog.show(MeetingInfoActivity, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                //loading.dismiss();
                //Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... params) {

                try{
                    String key = (String)params[0];

                    String link="http://210.94.194.201/deleteMemo.php";
                    String data  = URLEncoder.encode("memoKey", "UTF-8") + "=" + URLEncoder.encode(key, "UTF-8");

                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write( data );
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line = null;

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

        DeleteInfo task = new DeleteInfo();
        task.execute(memoKey);

        return true;
    }
    public MemoDTO select(int key)
    {
        MemoDTO dto = new MemoDTO();
        return dto;
    }
    public ArrayList<MemoDTO> selectAll()
    {
        getAllData("http://210.94.194.201/selectAllMemo.php");
        Log.d("print","memoDAO arrayListMemoDTO's size : "+ String.valueOf(arrayListMemoDTO.size()) );
        return arrayListMemoDTO;
    }

    protected void showList(String myJSON) {

        arrayListMemoDTO.clear();//업데이트를 위한 초기화부분
        int tmp;
        try{
            JSONObject jsonObj = new JSONObject(myJSON);
            JSONArray jsonArrayMemoDTO = null;
            jsonArrayMemoDTO = jsonObj.getJSONArray("result");
            Log.d("print","memoListLength : "+ String.valueOf(jsonArrayMemoDTO.length()) );
            for(int i=0;i<jsonArrayMemoDTO.length();i++) {

                //MeetingDTO 객체를 생성
                MemoDTO memoDTO = new MemoDTO();

                JSONObject c = jsonArrayMemoDTO.getJSONObject(i);

                /*Log.d("print","memoKey : "+ c.getString("memoKey") );
                Log.d("print","title : "+ c.getString("title") );
                Log.d("print","x : "+ c.getString("x") );
                Log.d("print","y : "+ c.getString("y") );
                Log.d("print","z : "+ c.getString("z") );
                Log.d("print","content : "+ c.getString("content") );
                Log.d("print","date : "+ c.getString("date") );
                Log.d("print","image : "+ c.getString("image") );
                Log.d("print","iconId : "+ c.getString("iconld") );
                Log.d("print","deviceID : "+ c.getString("deviceID") );
                Log.d("print","visibility : "+ c.getString("visibility") );*/
                memoDTO.setKey(Integer.parseInt(c.getString("memoKey")));
                memoDTO.setTitle(c.getString("title"));
                memoDTO.setX(Double.parseDouble(c.getString("x")));
                memoDTO.setY(Double.parseDouble(c.getString("y")));
                memoDTO.setZ(Double.parseDouble(c.getString("z")));
                memoDTO.setContent(c.getString("content"));
                memoDTO.setDate(c.getString("date"));
                memoDTO.setImage(c.getString("image"));
                memoDTO.setIconId(Integer.parseInt(c.getString("iconId")));
                memoDTO.setDeviceID(c.getString("deviceID"));
                memoDTO.setVisibility(Integer.parseInt(c.getString("visibility")));
                arrayListMemoDTO.add(memoDTO);
            }

        }catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void getAllData(String url)
    {
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];

                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while((json = bufferedReader.readLine())!= null){
                        sb.append(json+"\n");
                    }

                    return sb.toString().trim();

                }catch(Exception e){
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String result){
                showList(result);
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);

    }
}
