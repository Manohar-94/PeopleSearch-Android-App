package network_connections;

import org.json.JSONException;
import org.json.JSONObject;

public class ResultObject {
public String name, branch, year, enrollment_no, room, bhawan, department, 
                          username, designation, office_no, phone, email, service, role;
public ResultObject(){
	name="";branch="";year="";enrollment_no="";room="";bhawan="";department="";
	username="";designation="";office_no="";phone="";email="";service="";role="";
}
public void studentObject(JSONObject json, String role) throws JSONException{
	name=json.getString("name");
	branch=json.getString("branch");
	year=json.getString("year");
	enrollment_no=json.getString("enrollment_no");
	room=json.getString("room");
	bhawan=json.getString("bhawan");
	this.role=role;
}
public void facultyObject(JSONObject json, String role) throws JSONException{
	name=json.getString("name");
	username=json.getString("username");
	department=json.getString("department");
	designation=json.getString("designation");
	office_no=json.getString("office-no");
	this.role=role;
}
public void groupObject(JSONObject json, String role) throws JSONException{
	name=json.getString("name");
	phone=json.getString("phone-no");
	email=json.getString("email");
	this.role=role;
}
public void serviceObject(JSONObject json, String role) throws JSONException{
	name=json.getString("name");
	service=json.getString("service");
    office_no=json.getString("office_no");
    this.role=role;
}
}
