package in.ernet.iitr.peoplesearchbeta;

import java.util.ArrayList;

import networking_and_object_handling.ConnectAndParse;
import networking_and_object_handling.ResultObject;

import org.json.JSONArray;
import org.json.JSONObject;

import adapter.PeopleListAdapter;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;

public class Fragment_all extends SherlockFragment {

	private String course="",year="",srch_str="",role="all",faculty_department="",faculty_designation="", services_list="",groups_list="";
	JSONObject jobj ,json0, json;
	JSONArray json1,json2,json3,json4;
	private ListView lv;
	public View storeView;
	private int temp=0,counter=0,sum=0;
	
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
		ConnectAndParse cp = new ConnectAndParse();
		cp.setArguments(course,year,srch_str,role,faculty_department,
				faculty_designation,services_list,groups_list, counter);
		ArrayList<ResultObject> list = new ArrayList<ResultObject>();
		list = cp.parseData(cp.getData());
		PeopleListAdapter adapter = new PeopleListAdapter(this.getActivity(),list);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new MyClickListener(list));
		
	}
	
	class MyClickListener implements OnItemClickListener{
		ArrayList<ResultObject> list;
		public MyClickListener(ArrayList<ResultObject> list){
			this.list = list;
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
			// TODO Auto-generated method stub
			AlertDialogFragment ad = new AlertDialogFragment();
			ad.setArguments(list.get(position));
			ad.show(getFragmentManager(), "alertdialog");
		}

	}
		
	}