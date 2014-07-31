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
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.Spinner;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

@SuppressWarnings("deprecation")
public class Fragment_faculties extends SherlockFragment implements OnItemSelectedListener{

	private String course="",year="",srch_str="",role="Faculties",faculty_department="",faculty_designation="", services_list="",groups_list="";
	private Spinner spinner1 , spinner2;
	JSONObject jobj , json;
	JSONArray json1;
	private ListView lv;
	public View storeView;
	private int temp=0,counter=0;
	SlidingDrawer sd;

	public View onCreateView(LayoutInflater inflater , ViewGroup container , Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.faculties, container , false);	
		storeView = view;

		spinner1 = (Spinner) view.findViewById(R.id.spinner_department);
		ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this.getActivity(), R.array.fac_dep, android.R.layout.simple_spinner_item);
		adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner1.setAdapter(adapter1);

		spinner2 = (Spinner) view.findViewById(R.id.spinner_designation);
		ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this.getActivity(), R.array.fac_des, android.R.layout.simple_spinner_item);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner2.setAdapter(adapter2);

		sd = (SlidingDrawer) view.findViewById(R.id.slidingDrawer2);
		sd.setOnDrawerOpenListener(new MyDrawerOpenListener());
		sd.setOnDrawerCloseListener(new MyDrawerCloseListener());


		spinner1.setOnItemSelectedListener(this);
		spinner2.setOnItemSelectedListener(this);

		update(srch_str);

		return view;
	}

	class MyDrawerOpenListener implements SlidingDrawer.OnDrawerOpenListener{
		public MyDrawerOpenListener(){

		}

		@Override
		public void onDrawerOpened() {
			// TODO Auto-generated method stub
			LinearLayout v= (LinearLayout) storeView.findViewById(R.id.linearsliding2);
			RelativeLayout.LayoutParams p = (RelativeLayout.LayoutParams) v.getLayoutParams();
			p.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
			v.setLayoutParams(p);
			sd.open();
		}
	}

	class MyDrawerCloseListener implements SlidingDrawer.OnDrawerCloseListener{
		public void onDrawerClosed(){
			LinearLayout v= (LinearLayout) storeView.findViewById(R.id.linearsliding2);
			RelativeLayout.LayoutParams p = (RelativeLayout.LayoutParams) v.getLayoutParams();
			p.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
			v.setLayoutParams(p);
		}
	}



	public void onItemSelected(AdapterView<?> parent, View v, int position, long id ){

		if (parent == spinner1){
			if(position !=0){
				faculty_department = spinner1.getSelectedItem().toString();
			}
			else{
				faculty_department = "";
			}
		}
		else if (parent == spinner2){
			if(position != 0){
				faculty_designation = spinner2.getSelectedItem().toString();
			}
			else{
				faculty_designation = "";
			}
		}
	}

	public void onNothingSelected(AdapterView<?> parent){

	}

	@SuppressLint("NewApi")
	public void update(String name){
		StrictMode.enableDefaults();
		srch_str = name;
		counter =0;
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
		try{
			lv = (ListView) storeView.findViewById(R.id.listView2);

			jobj = new JSONObject(result);
			json1 = jobj.getJSONArray("data");
			temp = jobj.getInt("temp");
			String[] s1 = new String[json1.length()];
			String[] s2 = new String[json1.length()];
			String[] username = new String[json1.length()];
			String[] office_no = new String[json1.length()];
			String[] name = new String[json1.length()];
			for(int i=0;i<json1.length();i++){
				json = json1.getJSONObject(i);

				s1[i] = 
						json.getString("name")+"\n"+
								json.getString("department")+"\n";

				username[i] = json.getString("username");

				s2[i] = json.getString("name")+"\n"+
						json.getString("designation")+"\n"+
						json.getString("department")+"\t"+
						"department"+"\n"+
						json.getString("office-no");

				office_no[i] = json.getString("office-no");

				name[i] = json.getString("name");
			}
			MyCustomAdapter customadapter = new MyCustomAdapter(this.getActivity(),s1,username);
			lv.setAdapter(customadapter);
			lv.setOnItemClickListener(new MyClickListener(s2,username,office_no,name));

			TextView textview2 = (TextView) storeView.findViewById(R.id.textView2);
			String text = "";
			if(temp == 0){
				text = "Showing 0-0 out of 0 results";
			}
			else if(temp/20 > counter){
				text = "Showing "+(20*counter+1)+"-"+(20*(counter+1))+" out of "+temp+" results";
			}
			else{
				text = "Showing "+(20*counter+1)+"-"+(temp)+" out of "+temp+" results";
			}
			textview2.setText(text);

			next();
			previous();

		}
		catch (Exception e) {
			// TODO: handle exception
			Log.e("log_tag", "Error Parsing Data Faculties"+e.toString());
		}
	}

	private void next(){
		ImageButton next = (ImageButton) storeView.findViewById(R.id.next_faculties);
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
		ImageButton previous = (ImageButton) storeView.findViewById(R.id.previous_faculties);
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
		private final String[] username;

		public  MyCustomAdapter(Context context , String[] values , String[] username){
			super(context,R.layout.view_for_listview,values);
			this.context=context;
			this.values=values;
			this.username = username;
		}

		public View getView(int position,View convertView , ViewGroup parent){
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View faculties_listview = inflater.inflate(R.layout.view_for_listview, parent , false);
			TextView textview = (TextView) faculties_listview.findViewById(R.id.textForListview);
			ImageView imageview = (ImageView) faculties_listview.findViewById(R.id.imageForListview);
			String imageurl = "http://people.iitr.ernet.in/photo/";
			StringBuilder sb = new StringBuilder();
			sb.append(imageurl+username[position]+"/");
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
			String string = 
					values[position];
			textview.setText(string);

			return faculties_listview;

		}
	}
	class MyClickListener implements OnItemClickListener{
		String[] s2;
		String[] username;
		String[] office_no;
		String[] name;
		public MyClickListener(String[] s2,String[] username,String[] office_no,String[] name){
			this.s2 = s2;
			this.username = username;
			this.office_no = office_no;
			this.name = name;
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
			// TODO Auto-generated method stub

			AlertDialogFragment ad = new AlertDialogFragment(s2[position],username[position],name[position],office_no[position]);
			ad.show(getFragmentManager(), "alertdialog");
		}
	}

	@SuppressLint("ValidFragment")
	public class AlertDialogFragment extends DialogFragment{
		private String s2;
		private String username;
		private String office_no;
		private String name;
		public AlertDialogFragment(String s2 , String username , String name , String office_no){
			this.s2 = s2;
			this.username = username;
			this.name = name;
			this.office_no = office_no;
		}
		public Dialog onCreateDialog(Bundle savedInstanceState){
			LayoutInflater inflater  = getActivity().getLayoutInflater();
			View alertboxview = inflater.inflate(R.layout.alertboxdialog, null);
			TextView textview = (TextView) alertboxview.findViewById(R.id.textView_alertbox);
			textview.setText(s2);
			ImageView imageview = (ImageView) alertboxview.findViewById(R.id.imageView_alertbox);
			String imageurl = "http://people.iitr.ernet.in/photo/";
			StringBuilder sb = new StringBuilder();
			sb.append(imageurl+username+"/");
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
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setView(alertboxview)
			.setPositiveButton(R.string.add_to_contacts, new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog,int id){
					Intent intent = new Intent(Intents.Insert.ACTION);
					intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
					intent.putExtra(Intents.Insert.NAME,name)
					.putExtra(Intents.Insert.PHONE, office_no)
					.putExtra(Intents.Insert.PHONE_TYPE, Phone.TYPE_WORK);
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

