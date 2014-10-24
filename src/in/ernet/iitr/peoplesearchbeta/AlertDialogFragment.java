package in.ernet.iitr.peoplesearchbeta;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import network_connections.ResultObject;
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
import android.widget.TextView;

public class AlertDialogFragment extends DialogFragment{
	private ResultObject obj;
	public  void setArguments(ResultObject obj){
		this.obj=obj;
	}
	public Dialog onCreateDialog(Bundle savedInstanceState){
		LayoutInflater inflater  = getActivity().getLayoutInflater();
		View alertboxview = inflater.inflate(R.layout.alertboxdialog, null);
		TextView textview = (TextView) alertboxview.findViewById(R.id.textView_alertbox);
		//textview.setText(s2);
		ImageView imageview = (ImageView) alertboxview.findViewById(R.id.imageView_alertbox);
		String imageurl = "http://people.iitr.ernet.in/photo/";
		StringBuilder sb = new StringBuilder();
		sb.append(imageurl+obj.enrollment_no+"/");
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
