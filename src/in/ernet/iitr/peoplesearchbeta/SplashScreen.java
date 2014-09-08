package in.ernet.iitr.peoplesearchbeta;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SplashScreen extends Activity{

	public static final String PREFS_NAME = "MyPrefsFile";
	public String result;
	public int check;
	URL urlbase = null;
	public String username="", password="", session_key="", msg="", name, info;
	JSONObject jsonobj;
	HttpClient httpClient;
	HttpPost httpPost;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);	

		SharedPreferences settings = getSharedPreferences(PREFS_NAME,0);
		SharedPreferences.Editor editor = settings.edit();
		session_key = settings.getString("session_key","");

		//do{
		if(session_key == ""){
			Button submit = (Button) findViewById(R.id.submit);
			submit.setOnClickListener(new Button.OnClickListener(){
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					try{
						processData();
					}
					catch(Exception e){
						e.printStackTrace();
						Log.e("log_tag", "error in processData");
					}
				}
			});
		}
		else if(session_key != ""){
			try{

				//httpClient = new DefaultHttpClient();
				httpPost = new HttpPost("http://192.168.121.5:8080/peoplesearch/check_session/");

				List<NameValuePair> namevaluepair = new ArrayList<NameValuePair>(1);
				namevaluepair.add(new BasicNameValuePair("session_key",session_key));
				try{
					httpPost.setEntity(new UrlEncodedFormEntity(namevaluepair));
				}
				catch(Exception e){
					e.printStackTrace();
				}
				result = new ConnectTask().execute(httpPost).get();
				parseData();
				Toast toast = Toast.makeText(getApplicationContext(),result, Toast.LENGTH_LONG);
				toast.show();

				/*editor.putString("session_key","");
				editor.commit();*/
				if(msg.equals("YES")){
					
					editor.commit();
					Intent intent = new Intent(this,MainActivity.class);
					startActivity(intent);
				}
			}
			catch(Exception e){
				Log.e("log_tag", e.toString());
			}
		}
		//}while(msg=="NO");

	}

	public void processData() throws UnsupportedEncodingException{

		EditText usertext = (EditText) findViewById(R.id.username);
		EditText passtext = (EditText) findViewById(R.id.password);
		username = usertext.getText().toString();
		password = passtext.getText().toString();

		//httpClient = new DefaultHttpClient();
		httpPost = new HttpPost("http://192.168.121.5:8080/peoplesearch/channeli_login/");

		List<NameValuePair> namevaluepair = new ArrayList<NameValuePair>(2);
		namevaluepair.add(new BasicNameValuePair("username",username));
		namevaluepair.add(new BasicNameValuePair("password",password));

		try{
			httpPost.setEntity(new UrlEncodedFormEntity(namevaluepair));
			result = new ConnectTask().execute(httpPost).get();
		}

		catch(Exception e){
			e.printStackTrace();
		}

		parseData();
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();	
		editor.putString("session_key",session_key);
		editor.commit();
		Toast toast = Toast.makeText(getApplicationContext(),result, Toast.LENGTH_LONG);
		toast.show();
		if(msg.equals("YES")){
			editor.putString("name", name);
			editor.putString("info", info);
			editor.putString("enrollment_no", username);
			editor.commit();
			Intent intent = new Intent(this,MainActivity.class);
			startActivity(intent);
		}
	}

	public void parseData(){
		try {
			JSONObject json = new JSONObject(result);
			msg = json.getString("msg");
			name = json.getString("_name");
			info = json.getString("info");
			session_key = json.getString("session_variable");
			if(msg=="NO"){
				Toast toast = Toast.makeText(getApplicationContext(),"could not login" , Toast.LENGTH_SHORT);
				toast.show();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	class ConnectTask extends AsyncTask<HttpPost, Void, String>{

		@Override
		protected String doInBackground(HttpPost... httpPosts) {
			// TODO Auto-generated method stub
			InputStream isr = null;
			String result="";
			try{
				httpClient = new DefaultHttpClient();
				HttpResponse response = httpClient.execute(httpPosts[0]);
				HttpEntity entity = response.getEntity();
				isr = entity.getContent();

			}
			catch(Exception e){
				e.printStackTrace();
			}

			//convert response to string
			try{
				BufferedReader reader = new BufferedReader(new InputStreamReader(isr,"iso-8859-1"),8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while((line = reader.readLine()) != null){
					sb.append(line+"\n");
				}
				isr.close();
				result = sb.toString();
			}
			catch(Exception e){
				Log.e("log_tag", "Error  converting result "+e.toString());
			}
			return result;

		}

	}
	public void onResume(){
		super.onResume();
		Log.e("log_tag", "inside resume");
		SharedPreferences settings = getSharedPreferences(PREFS_NAME,0);
		SharedPreferences.Editor editor = settings.edit();
		check = settings.getInt("check", 0);
		if(check==0){

			editor.putInt("check", 1);
			editor.commit();
		}
		else{

			editor.putInt("check", 0);
			editor.commit();
			finish();
		}

	}
	public void onBackPressed(){
		SharedPreferences settings = getSharedPreferences(PREFS_NAME,0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("check", 0);
		editor.commit();
		super.onBackPressed();
	}

}

