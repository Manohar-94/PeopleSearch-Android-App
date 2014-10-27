package adapter;

import in.ernet.iitr.peoplesearchbeta.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import networking_and_object_handling.ResultObject;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PeopleListAdapter extends ArrayAdapter<ResultObject>{
		private final Context context;
		private final ArrayList<ResultObject> obj;
		
		public  PeopleListAdapter(Context context , ArrayList<ResultObject> obj){
			super(context,R.layout.list_base,obj);
			this.context=context;
			this.obj=obj;
		}

		public View getView(int position, View convertView , ViewGroup parent){
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View people_listview = inflater.inflate(R.layout.list_base, null , false);
			RelativeLayout rl = (RelativeLayout) people_listview.findViewById(R.id.base_view);
			ImageView imageview = (ImageView) people_listview.findViewById(R.id.imageForListview);
			String imageurl = "http://people.iitr.ernet.in/photo/";
			StringBuilder sb = new StringBuilder();
			Log.e("peopleListAdapter", imageurl);
			
			if(obj.get(position).sub_role.equals("stud")||obj.get(position).sub_role.equals("all_stud")){
				
				View stud_view = inflater.inflate(R.layout.list_student, null, true);
				rl.addView(stud_view, 1);
				TextView name = (TextView) stud_view.findViewById(R.id.student_name);
				name.setText(obj.get(position).name);
				TextView branch = (TextView) stud_view.findViewById(R.id.student_branch);
				branch.setText(obj.get(position).branch);
				TextView year = (TextView) stud_view.findViewById(R.id.student_year);
				year.setText(obj.get(position).year);
				sb.append(imageurl+obj.get(position).enrollment_no+"/");
								
			}
			else if(obj.get(position).sub_role.equals("fac")||obj.get(position).sub_role.equals("all_fac")){
				
				View fac_view = inflater.inflate(R.layout.list_faculty, null, true);
				rl.addView(fac_view, 1);
				TextView name = (TextView) fac_view.findViewById(R.id.faculty_name);
				name.setText(obj.get(position).name);
				TextView department = (TextView) fac_view.findViewById(R.id.faculty_department);
				department.setText(obj.get(position).department);
				sb.append(imageurl+obj.get(position).username+"/");
				
			}
			else if(obj.get(position).sub_role.equals("groups")||obj.get(position).sub_role.equals("all_groups")){
				
				View groups_view = inflater.inflate(R.layout.list_group, null, true);
				rl.addView(groups_view, 1);
				TextView name = (TextView) groups_view.findViewById(R.id.group_name);
				name.setText(obj.get(position).name);
				TextView phone = (TextView) groups_view.findViewById(R.id.group_phone);
				phone.setText(obj.get(position).phone);
				sb.append(imageurl+obj.get(position).name+"/");
				
			}
			else{
				
				View serv_view = inflater.inflate(R.layout.list_service, null, true);
				rl.addView(serv_view, 1);
				TextView name = (TextView) serv_view.findViewById(R.id.service_name);
				name.setText(obj.get(position).name);
				TextView service = (TextView) serv_view.findViewById(R.id.service);
				service.setText(obj.get(position).service);
				
			}
							
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
			
			return people_listview;

		}
	}