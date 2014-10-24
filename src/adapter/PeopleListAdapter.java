package adapter;

import in.ernet.iitr.peoplesearchbeta.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import network_connections.ResultObject;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PeopleListAdapter extends ArrayAdapter<String>{
		private final Context context;
		private final ArrayList<ResultObject> obj;
		
		public  PeopleListAdapter(Context context , ArrayList<ResultObject> obj){
			super(context,R.layout.view_for_listview);
			this.context=context;
			this.obj=obj;
		}

		public View getView(int position, View convertView , ViewGroup parent){
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View students_listview = inflater.inflate(R.layout.view_for_listview, parent , false);
			TextView textview = (TextView) students_listview.findViewById(R.id.textForListview);
			ImageView imageview = (ImageView) students_listview.findViewById(R.id.imageForListview);
			String imageurl = "http://people.iitr.ernet.in/photo/";
			StringBuilder sb = new StringBuilder();
			sb.append(imageurl+obj.get(position).enrollment_no+"/");
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
			/*String string = 
					values[position];
			textview.setText(string);
*/
			return students_listview;

		}
	}