package networking_and_object_handling;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;

import com.actionbarsherlock.app.SherlockFragment;

public class ConnectAndParse extends SherlockFragment{

	private String course,year,srch_str,role,faculty_department,
				faculty_designation, services_list, groups_list;
	JSONObject jobj , json, json1;
	JSONArray json2;
	public View storeView;
	private int temp=0,counter=0;
	
	public ConnectAndParse(){
		course=""; year="";srch_str=""; role=""; faculty_department="";
		faculty_designation=""; services_list=""; groups_list="";
	}
	public void setArguments(String course, String year, String srch_str, String role,
	          String faculty_department, String faculty_designation, 
	      String services_list, String groups_list, int counter){
		
		this.course=course;this.year=year;this.srch_str=srch_str;
		this.role=role;this.faculty_department=faculty_department;
		this.faculty_designation=faculty_designation;this.services_list=services_list;
		this.groups_list=groups_list;this.counter=counter;
	}
	
	@SuppressLint("NewApi")
	public String getData(){
		String result="";
		InputStream isr = null;
		String url = "http://172.25.55.5:8000/peoplesearch/index/";
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
	
	public ArrayList<ResultObject> parseData(String result){
		try{
			
			jobj = new JSONObject(result);
			json1 = jobj.getJSONObject("data");
			temp = jobj.getInt("temp");
			role = jobj.getString("role");
			String sub_role = role;
			ArrayList<ResultObject> list= new ArrayList<ResultObject>();
			
			if(role.equals("stud") || role.equals("all")){
			json2 = json1.getJSONArray("stud");
			for(int i=0;i<json2.length();i++){
				json = json2.getJSONObject(i);
				ResultObject obj = new ResultObject();
				if(role.equals("all")){sub_role=role+"_stud";}
				obj.studentObject(json,sub_role);
				list.add(obj);
			}
			}

			if(role.equals("fac") || role.equals("all")){
			json2 = json1.getJSONArray("fac");
			for(int i=0;i<json2.length();i++){
				json = json2.getJSONObject(i);
				ResultObject obj = new ResultObject();
				if(role.equals("all")){sub_role=role+"_fac";}
				obj.facultyObject(json,sub_role);
				list.add(obj);
			}
			}
			
			if(role.equals("serv") || role.equals("all")){
			json2 = json1.getJSONArray("serv");
			for(int i=0;i<json2.length();i++){
				json = json2.getJSONObject(i);
				ResultObject obj = new ResultObject();
				if(role.equals("all")){sub_role=role+"_serv";}
				obj.serviceObject(json,sub_role);
				list.add(obj);
			}
			}
			
			if(role.equals("groups") || role.equals("all")){
			json2 = json1.getJSONArray("groups");
			for(int i=0;i<json2.length();i++){
				json = json2.getJSONObject(i);
				ResultObject obj = new ResultObject();
				if(role.equals("all")){sub_role=role+"_stud";}
				obj.groupObject(json,sub_role);
				list.add(obj);
			}
			}
			
			return list;
		}
		catch (Exception e) {
			// TODO: handle exception
			Log.e("log_tag", "Error Parsing Data"+e.toString());
			return null;
		}
	}
}
