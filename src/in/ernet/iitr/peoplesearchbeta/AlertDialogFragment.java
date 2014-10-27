package in.ernet.iitr.peoplesearchbeta;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import networking_and_object_handling.ResultObject;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.Contacts.Intents;
import android.provider.ContactsContract;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class AlertDialogFragment extends DialogFragment{
	private ResultObject obj;
	public  void setArguments(ResultObject obj){
		this.obj=obj;
	}
	public Dialog onCreateDialog(Bundle savedInstanceState){
		LayoutInflater inflater  = getActivity().getLayoutInflater();
		View alertboxview = inflater.inflate(R.layout.alertbox_base, null);
		ImageView imageview = (ImageView) alertboxview.findViewById(R.id.imageView_alertbox);
		String imageurl = "http://people.iitr.ernet.in/photo/";
		StringBuilder sb = new StringBuilder();
		RelativeLayout rl =(RelativeLayout) alertboxview.findViewById(R.id.base_alertview);
		
		if(obj.sub_role.equals("stud")||obj.sub_role.equals("all_stud")){
			
			View stud_view = inflater.inflate(R.layout.alertbox_student, null, false);
			rl.addView(stud_view, 1);
			TextView name = (TextView) stud_view.findViewById(R.id.student_name);
			name.setText(obj.name);
			TextView branch = (TextView) stud_view.findViewById(R.id.student_branch);
			branch.setText(obj.branch);
			TextView year = (TextView) stud_view.findViewById(R.id.student_year);
			year.setText(obj.year);
			TextView enrollment = (TextView) stud_view.findViewById(R.id.student_enrollment_no);
			enrollment.setText(obj.enrollment_no);
			TextView bhawan = (TextView) stud_view.findViewById(R.id.student_bhawan);
			bhawan.setText(obj.bhawan);
			TextView room = (TextView) stud_view.findViewById(R.id.student_room);
			room.setText(obj.room);
			sb.append(imageurl+obj.enrollment_no+"/");
			
		}
		else if(obj.sub_role.equals("fac")||obj.sub_role.equals("all_fac")){
			
			View fac_view = inflater.inflate(R.layout.alertbox_faculty, null, false);
			rl.addView(fac_view, 1);
			TextView name = (TextView) fac_view.findViewById(R.id.faculty_name);
			name.setText(obj.name);
			TextView department = (TextView) fac_view.findViewById(R.id.faculty_department);
			department.setText(obj.department);
			TextView designation = (TextView) fac_view.findViewById(R.id.faculty_designation);
			designation.setText(obj.designation);
			TextView office_no = (TextView) fac_view.findViewById(R.id.faculty_office_no);
			office_no.setText(obj.office_no);
			sb.append(imageurl+obj.username+"/");
			
		}
		else if(obj.sub_role.equals("groups")||obj.sub_role.equals("all_groups")){
			
			View groups_view = inflater.inflate(R.layout.alertbox_group, null, false);
			rl.addView(groups_view, 1);
			TextView name = (TextView) groups_view.findViewById(R.id.group_name);
			name.setText(obj.name);
			TextView phone = (TextView) groups_view.findViewById(R.id.group_phone);
			phone.setText(obj.phone);
			TextView email = (TextView) groups_view.findViewById(R.id.group_email);
			email.setText(obj.email);
			sb.append(imageurl+obj.name+"/");
			
		}
		else{
			
			View serv_view = inflater.inflate(R.layout.alertbox_service, null, false);
			rl.addView(serv_view, 1);
			TextView name = (TextView) serv_view.findViewById(R.id.service_name);
			name.setText(obj.name);
			TextView service = (TextView) serv_view.findViewById(R.id.service);
			service.setText(obj.service);
			TextView office_no = (TextView) serv_view.findViewById(R.id.service_office_no);
			office_no.setText(obj.office_no);
			
		}
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
				intent.putExtra(Intents.Insert.NAME,obj.name);
				intent.putExtra(Intents.Insert.EMAIL, obj.email);
				intent.putExtra(Intents.Insert.PHONE, obj.phone);
				intent.putExtra(Intents.Insert.PHONE, obj.office_no);
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
