package in.ernet.iitr.peoplesearchbeta;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Intents;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

public class Fragment_all extends SherlockFragment {

	private String course="",year="",srch_str="",role="All",faculty_department="",faculty_designation="", services_list="",groups_list="";
	JSONObject jobj ,json0, json;
	JSONArray json1,json2,json3,json4;
	private ListView lv;
	public View storeView;
	private int temp=0,counter=0,sum=0;
	private String[] Fordisplay,Forimage,s2,phone,email,name;

	public View onCreateView(LayoutInflater inflater , ViewGroup container , Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.all, container , false);	
		storeView = view;
		lv = (ListView) view.findViewById(R.id.listView);
		update(srch_str);

		return view;
	}

	@SuppressLint("NewApi")
	public void update(String name){
		StrictMode.enableDefaults();
		srch_str = name;
		counter = 0;
		parseData(getData());
	}

	public String getData(){
		String result="";
		InputStream isr = null;
		String url = "http://192.168.121.5:8080/peoplesearch/";
		StringBuilder sbuilder = new StringBuilder();
		sbuilder.append(url+"?name="+srch_str+"&role="+role+"&course="+course+
				"&year="+year+"&faculty_department="+faculty_department+"&faculty_designation="+faculty_designation+
				"&services_list="+services_list+"&groups_list="+groups_list+"&counter="+counter);
		url = sbuilder.toString();

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
	public void parseData(String result){

		try {
			jobj = new JSONObject(result);
			json0 = jobj.getJSONObject("data");
			temp = jobj.getInt("temp");

			json1 = json0.getJSONArray("Students");
			json2 = json0.getJSONArray("Faculties");
			json3 = json0.getJSONArray("Services");
			json4 = json0.getJSONArray("Groups");
			int total = json1.length() + json2.length() + json3.length() + json4.length();

			Fordisplay = new String[total];
			Forimage = new String[total];
			s2 = new String[total];
			name = new String[total];
			phone = new String[total];
			email = new String[total];
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		sum=0;
		try{
			for(int i=0;i<json1.length();i++){
				json = json1.getJSONObject(i);
				Fordisplay[sum]=json.getString("name")+"\n"+
						json.getString("branch")+"\t"+
						json.getString("year")+" "+
						"year"+"\n";        	          
				Forimage[sum] = json.getString("enrollment_no");
				s2[sum] = 
						json.getString("name")+"\n"+
								json.getString("enrollment_no")+"\n"+
								json.getString("branch")+"\t"+
								json.getString("year")+" "+
								"year"+"\n"+
								json.getString("room")+"\t"+
								json.getString("bhawan")+"\n";
				name[sum] = json.getString("name");
				phone[sum] = "";
				email[sum] = "";
				sum++;
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			Log.e("log_tag", "Error Parsing Data All-Students "+e.toString());
		}

		try{
			for(int i=0;i<json2.length();i++){
				json = json2.getJSONObject(i);
				Fordisplay[sum]=json.getString("name")+"\n"+
						json.getString("department")+"\n";        	          
				Forimage[sum] = json.getString("username");

				s2[sum] = json.getString("name")+"\n"+
						json.getString("designation")+"\n"+
						json.getString("department")+"\t"+
						"department"+"\n"+
						json.getString("office-no");

				phone[sum] = json.getString("office-no");
				name[sum] = json.getString("name");
				email[sum] = "";
				sum++;
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			Log.e("log_tag", "Error Parsing Data All-Faculties "+e.toString());
		}
		try{
			for(int i=0;i<json3.length();i++){
				json = json3.getJSONObject(i);
				Fordisplay[sum]=json.getString("name")+"\t"+
						json.getString("service")+"\n";       

				Forimage[sum] = "";

				s2[sum] = json.getString("name")+"\n"+
						json.getString("service")+"\n"+
						json.getString("office_no")+"\n";

				phone[sum] = json.getString("office_no");

				email[sum] = "";
				name[sum] = json.getString("name");
				sum++;
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			Log.e("log_tag", "Error Parsing Data All-Services "+e.toString());
		}
		try{
			for(int i=0;i<json4.length();i++){
				json = json4.getJSONObject(i);
				Fordisplay[sum]=json.getString("name")+"\n"+
						json.getString("phone-no")+"\n";     

				Forimage[sum] = json.getString("name");

				s2[sum] = json.getString("name")+"\n"+
						json.getString("phone-no")+"\n"+
						json.getString("email")+"\n";

				phone[sum] = json.getString("phone-no");

				email[sum] = json.getString("email");
				sum++;
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			Log.e("log_tag", "Error Parsing Data All-Groups "+e.toString());
		}

		MyCustomAdapter customadapter = new MyCustomAdapter(this.getActivity(),Fordisplay,Forimage);
		lv.setAdapter(customadapter);
		lv.setOnItemClickListener(new MyOnClickListener(s2,Forimage,phone,email,Forimage));


		TextView textview5 = (TextView) storeView.findViewById(R.id.textView5);
		String text = "";
		if(temp == 0){
			text = "Showing 0-0 out of 0 results";
		}
		else if(temp/20 > counter){
			text = "Showing "+(20*counter+1)+" - "+(20*(counter+1))+" out of "+temp+" results";
		}
		else{
			text = "Showing "+(20*counter+1)+" - "+(temp)+" out of "+temp+" results";
		}
		textview5.setText(text);

		next();
		previous();
	}

	private void next(){
		ImageButton next = (ImageButton) storeView.findViewById(R.id.next_all);
		next.setOnClickListener(new NextListener());
	}

	class NextListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if(temp%20 == 0){
				if((temp/20-1) > counter){
					counter++;
				}
			}
			else if(temp/20 > counter){
				counter++;
			}
			parseData(getData());
		}

	}

	private void previous(){
		ImageButton previous = (ImageButton) storeView.findViewById(R.id.previous_all);
		previous.setOnClickListener(new PreviousListener());
	}

	class PreviousListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if(counter != 0){
				counter--;
			}
			parseData(getData());
		}
	}


	public class MyCustomAdapter extends ArrayAdapter<String>{
		private final Context context;
		private final String[] values;
		private final String[] Forimage;

		public  MyCustomAdapter(Context context , String[] values , String[] Forimage){
			super(context,R.layout.view_for_listview,values);
			this.context=context;
			this.values=values;
			this.Forimage = Forimage;
		}

		public View getView(int position,View convertView , ViewGroup parent){
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View students_listview = inflater.inflate(R.layout.view_for_listview, parent , false);
			TextView textview = (TextView) students_listview.findViewById(R.id.textForListview);
			
			if(Forimage[position] != ""){
			ImageView imageview = (ImageView) students_listview.findViewById(R.id.imageForListview);
			String imageurl = "http://people.iitr.ernet.in/photo/";
			StringBuilder sb = new StringBuilder();
			sb.append(imageurl+Forimage[position]+"/");
			imageurl=sb.toString();
			
				try{

					Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(imageurl).getContent());
					imageview.setImageBitmap(bitmap);

				}
				catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			String string = 
					values[position];
			textview.setText(string);

			return students_listview;

		}
	}


	class MyOnClickListener implements OnItemClickListener{
		String[] s2;
		String[] forimage;
		String[] office_no;
		String[] email;
		String[] name;
		public MyOnClickListener(String[] s2,String[] forimage,String[] office_no,String[] email,String[] name){
			this.s2 = s2;
			this.forimage = forimage;
			this.office_no = office_no;
			this.email = email;
			this.name = name;
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
			// TODO Auto-generated method stub
			AlertDialogFragment ad = new AlertDialogFragment(s2[position],forimage[position],name[position],email[position],office_no[position]);
			ad.show(getFragmentManager(), "alertdialog");
		}
	}

	@SuppressLint("ValidFragment")
	public class AlertDialogFragment extends DialogFragment{
		private String s2;
		private String forimage;
		private String name;
		private String email;
		private String office_no;
		public AlertDialogFragment(String s2 , String forimage , String name , String email , String office_no){
			this.s2 = s2;
			this.forimage = forimage;
			this.name = name;
			this.name = name;
			this.office_no = office_no;
		}
		public Dialog onCreateDialog(Bundle savedInstanceState){
			LayoutInflater inflater  = getActivity().getLayoutInflater();
			View alertboxview = inflater.inflate(R.layout.alertboxdialog, null);
			TextView textview = (TextView) alertboxview.findViewById(R.id.textView_alertbox);
			textview.setText(s2);
			
			if(forimage != ""){
			ImageView imageview = (ImageView) alertboxview.findViewById(R.id.imageView_alertbox);
			String imageurl = "http://people.iitr.ernet.in/photo/";
			StringBuilder sb = new StringBuilder();
			sb.append(imageurl+forimage+"/");
			imageurl=sb.toString();
			try{

				Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(imageurl).getContent());
				imageview.setImageBitmap(bitmap);
			}
			catch (MalformedURLException e) {
				e.printStackTrace();
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
			}
			
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setView(alertboxview)
			.setPositiveButton(R.string.add_to_contacts, new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog,int id){
					Intent intent = new Intent(Intents.Insert.ACTION);
					intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
					intent.putExtra(Intents.Insert.NAME,name)
					.putExtra(Intents.Insert.PHONE_TYPE, Phone.TYPE_WORK)
					.putExtra(Intents.Insert.PHONE, office_no)
					.putExtra(Intents.Insert.EMAIL, email);
					startActivity(intent);
				}
			})
			.setNegativeButton(R.string.back, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int id) {
					// TODO Auto-generated method stub
					AlertDialogFragment.this.getDialog().cancel();
				}
			});

			return builder.create();
		}
	}
}
