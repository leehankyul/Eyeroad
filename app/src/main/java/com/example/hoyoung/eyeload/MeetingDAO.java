package kr.soen.mypart;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by Jin on 2016-11-17.
 */

public class MeetingDAO extends DAO{
    //싱글톤 클래스
    //private static MeetingDAO meetingDAO = new MeetingDAO();
    private ArrayList<MeetingDTO> arrayListMeetingDTO = new ArrayList<>();
    private MeetingDTO meetingDTOSelected = new MeetingDTO();

    public MeetingDTO getMeetingDTOSelected()
    {
        return meetingDTOSelected;
    }


    public boolean insert(MeetingDTO dto)
    {

        try{

            //String meetingKey = (String)params[0];
            String title = dto.getTitle();
            String placeName = dto.getPlaceName();
            String meetingInfo = dto.getMeetingInfo();
            String publisher = dto.getPublisher();
            String password = dto.getPassword();

            String link="http://210.94.194.201/insertMeeting.php";
            //String data  = URLEncoder.encode("meetingKey", "UTF-8") + "=" + URLEncoder.encode(meetingKey, "UTF-8");
            //meetingKey는 자동으로 설정됨
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
            return true;
        }
        catch(Exception e){
            return false;
        }

    }

    public boolean deleteInfo(int key){
        String meetingKey = String.valueOf(key);
        try{

            String link="http://210.94.194.201/deleteMeeting.php";
            String data  = URLEncoder.encode("meetingKey", "UTF-8") + "=" + URLEncoder.encode(meetingKey, "UTF-8");

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
            return true;
        }
        catch(Exception e){
           return false;
        }
    }

    /*public MeetingDTO select(int key)
    {
        class SelectData extends AsyncTask<String, Void, String>{

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //loading = ProgressDialog.show(MakingMeetingActivity.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {

                try{
                    JSONObject jsonObj = new JSONObject(s);
                    JSONArray jsonArrayMeetingDTO = null;
                    jsonArrayMeetingDTO = jsonObj.getJSONArray("result");

                    for(int i=0;i<jsonArrayMeetingDTO.length();i++) {

                        //MeetingDTO 객체를 생성
                        MeetingDTO meetingDTO = new MeetingDTO();

                        JSONObject c = jsonArrayMeetingDTO.getJSONObject(i);
                        //MeetingDTO 객체에 정보 삽입
                        //meetingDTO.setKey(Integer.parseInt(c.getString("meetingKey")));
                        meetingDTO.setTitle(c.getString("title"));
                        meetingDTO.setPlaceName(c.getString("placeName"));
                        meetingDTO.setMeetingInfo(c.getString("meetingInfo"));
                        meetingDTO.setPublisher(c.getString("publisher"));
                        meetingDTO.setPassword(c.getString("password"));

                        //MeetingDTO 객체를 ArrayList에 삽입
                        meetingDTOSelected = meetingDTO;

                        Log.d("TESTING","MeetingDAO onPostExcute end");
                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(String... params) {

                try{

                    //String meetingKey = (String)params[0];
                    String key = (String)params[0];


                    String link="http://210.94.194.201/selectMeeting.php";
                    //String data  = URLEncoder.encode("meetingKey", "UTF-8") + "=" + URLEncoder.encode(meetingKey, "UTF-8");
                    //meetingKey는 자동으로 설정됨
                    String data  = URLEncoder.encode("meetingKey", "UTF-8") + "=" + URLEncoder.encode(key, "UTF-8");


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
                    Log.d("TESTING","MeetingDAO doInBackground end");
                    return sb.toString();
                }


                catch(Exception e){
                    return new String("Exception: " + e.getMessage());
                }

            }

        }
        String meetingKey = String.valueOf(key);
        SelectData task = new SelectData();
        task.execute(meetingKey);

        Log.d("TESTING","MeetingDAO select end");
        return meetingDTOSelected;
    }*/

    public MeetingDTO select(int key)
    {
        try {
            String meetingKey = String.valueOf(key);

            String link = "http://210.94.194.201/selectMeeting.php";
            String data = URLEncoder.encode("meetingKey", "UTF-8") + "=" + URLEncoder.encode(meetingKey, "UTF-8");


            URL url = new URL(link);
            URLConnection con = url.openConnection();
            con.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());

            wr.write(data);
            wr.flush();

            StringBuilder sb = new StringBuilder();

            BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(con.getInputStream()));

            String json;
            while((json = bufferedReader.readLine())!= null){
                sb.append(json+"\n");
            }

            String queryJson = sb.toString().trim();
            JSONObject jsonObj = new JSONObject(queryJson);
            JSONArray meetingInfo = jsonObj.getJSONArray("result");

            JSONObject c = meetingInfo.getJSONObject(0);

            /*for(int i=0;i<MeetingInfo.length();i++)
            {
                try {

                    url = new URL(temp.getString("url"));
                    bitmap[i] = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }*/
            MeetingDTO selectedMeetingDTO=new MeetingDTO();

            selectedMeetingDTO.setTitle(c.getString("title"));
            selectedMeetingDTO.setPlaceName(c.getString("placeName"));
            selectedMeetingDTO.setMeetingInfo(c.getString("meetingInfo"));
            selectedMeetingDTO.setPublisher("publisher");
            selectedMeetingDTO.setPassword("password");

            return selectedMeetingDTO;


        }catch (Exception e) {
            //return new String("Exception: " + e.getMessage());
            return null;
        }
    }


    public ArrayList<MeetingDTO> selectAll()
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
                arrayListMeetingDTO.clear();//업데이트를 위한 초기화부분

                try{
                    JSONObject jsonObj = new JSONObject(result);
                    JSONArray jsonArrayMeetingDTO = null;
                    jsonArrayMeetingDTO = jsonObj.getJSONArray("result");

                    for(int i=0;i<jsonArrayMeetingDTO.length();i++) {

                        //MeetingDTO 객체를 생성
                        MeetingDTO meetingDTO = new MeetingDTO();

                        JSONObject c = jsonArrayMeetingDTO.getJSONObject(i);
                        //MeetingDTO 객체에 정보 삽입
                        meetingDTO.setKey(Integer.parseInt(c.getString("meetingKey")));
                        meetingDTO.setTitle(c.getString("title"));
                        meetingDTO.setPlaceName(c.getString("placeName"));
                        meetingDTO.setMeetingInfo(c.getString("meetingInfo"));
                        meetingDTO.setPublisher(c.getString("publisher"));
                        meetingDTO.setPassword(c.getString("password"));

                        //MeetingDTO 객체를 ArrayList에 삽입
                        arrayListMeetingDTO.add(meetingDTO);

                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute("http://210.94.194.201/selectAllMeeting.php");

        return arrayListMeetingDTO;
    }
    /*public ArrayList<MeetingDTO> selectAll()
    {
        getAllData("http://210.94.194.201/selectAllMeeting.php");
        return arrayListMeetingDTO;
    }



    protected void showList(String myJSON) {

        arrayListMeetingDTO.clear();//업데이트를 위한 초기화부분

        try{
            JSONObject jsonObj = new JSONObject(myJSON);
            JSONArray jsonArrayMeetingDTO = null;
            jsonArrayMeetingDTO = jsonObj.getJSONArray("result");
            //Log.d("print","meetingListLength : "+ String.valueOf(jsonArrayMeetingDTO.length()) );
            for(int i=0;i<jsonArrayMeetingDTO.length();i++) {

                //MeetingDTO 객체를 생성
                MeetingDTO meetingDTO = new MeetingDTO();

                JSONObject c = jsonArrayMeetingDTO.getJSONObject(i);
                //MeetingDTO 객체에 정보 삽입
                meetingDTO.setKey(Integer.parseInt(c.getString("meetingKey")));
                meetingDTO.setTitle(c.getString("title"));
                meetingDTO.setPlaceName(c.getString("placeName"));
                meetingDTO.setMeetingInfo(c.getString("meetingInfo"));
                meetingDTO.setPublisher(c.getString("publisher"));
                meetingDTO.setPassword(c.getString("password"));

                //MeetingDTO 객체를 ArrayList에 삽입
                arrayListMeetingDTO.add(meetingDTO);

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

    }*/
}
