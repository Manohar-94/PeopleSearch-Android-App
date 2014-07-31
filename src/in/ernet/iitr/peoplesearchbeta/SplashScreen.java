package in.ernet.iitr.peoplesearchbeta;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class SplashScreen extends Activity {

	public static final String PREFS_NAME = "MyPrefsFile";
	public String result, url, urlbase = "";
	public String username="", password="", session_key="";


	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);		
		
		SharedPreferences settings = getSharedPreferences(PREFS_NAME,0);
		session_key = settings.getString("session_key","");
		
		if(session_key == ""){
			processData();
		}
		if(session_key != ""){
			
		}
		
		Intent intent = new Intent(this,MainActivity.class);
		startActivity(intent);
	}
	
	private void processData(){
		Button submit = (Button) findViewById(R.id.submit);
		submit.setOnClickListener(new MyClickListener());
	}
	class MyClickListener implements OnClickListener{
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			EditText usertext = (EditText) findViewById(R.id.username);
			EditText passtext = (EditText) findViewById(R.id.password);
			username = usertext.getText().toString();
			password = passtext.getText().toString();
			StringBuilder sb = new StringBuilder();
			sb.append(urlbase+"/"+username+"/"+password);
			url=sb.toString();
			result = connect();
			//parseData();
			SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
			SharedPreferences.Editor editor = settings.edit();	
			editor.putString("username", username);
			editor.putString("password", password);
			editor.putString("session_key",session_key);
			editor.commit();
		}
		

	}
	public String connect(){
		InputStream isr = null;
		String result="";
		try{
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet httppost = new HttpGet(url);
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			isr = entity.getContent();
		}
		catch(Exception e){
			Log.e("log_tag", "Error in http connection "+e.toString());

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
