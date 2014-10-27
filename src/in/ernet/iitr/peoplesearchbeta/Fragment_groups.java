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
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.Spinner;

import com.actionbarsherlock.app.SherlockFragment;

@SuppressWarnings("deprecation")
public class Fragment_groups extends SherlockFragment implements OnItemSelectedListener{
	
	private String course="",year="",srch_str="",role="groups",faculty_department="",faculty_designation="", services_list="",groups_list="";
	private Spinner spinner1;
	JSONObject jobj , json;
	JSONArray json1;
	private ListView lv;
	public View storeView;
	private int temp=0,counter=0;
	SlidingDrawer sd;

	public View onCreateView(LayoutInflater inflater , ViewGroup container , Bundle savedInstanceState){
	      View view = inflater.inflate(R.layout.groups, container , false);	
	      storeView = view;
	     	      
	      spinner1 = (Spinner) view.findViewById(R.id.spinner_groups_list);
	      ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this.getActivity(), R.array.grou_list, android.R.layout.simple_spinner_item);
	      adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	      spinner1.setAdapter(adapter1);
	      spinner1.setOnItemSelectedListener(this);
	      
	      sd = (SlidingDrawer) view.findViewById(R.id.slidingDrawer4);
	      sd.setOnDrawerOpenListener(new MyDrawerOpenListener());
	      sd.setOnDrawerCloseListener(new MyDrawerCloseListener());
	      
	      update(srch_str);
	      						      
	      return view;
}
	

	class MyDrawerOpenListener implements SlidingDrawer.OnDrawerOpenListener{
		public MyDrawerOpenListener(){
			
		}
		
		@Override
		public void onDrawerOpened() {
			// TODO Auto-generated method stub
			LinearLayout v= (LinearLayout) storeView.findViewById(R.id.linearsliding4);
			RelativeLayout.LayoutParams p = (RelativeLayout.LayoutParams) v.getLayoutParams();
			p.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
			v.setLayoutParams(p);
			sd.open();
		}
	}
	
	class MyDrawerCloseListener implements SlidingDrawer.OnDrawerCloseListener{
		public void onDrawerClosed(){
			LinearLayout v= (LinearLayout) storeView.findViewById(R.id.linearsliding4);
			RelativeLayout.LayoutParams p = (RelativeLayout.LayoutParams) v.getLayoutParams();
			p.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
			v.setLayoutParams(p);
		}
	}
	
		
	public void onItemSelected(AdapterView<?> parent, View v, int position, long id ){
		
		if(position !=0){
        groups_list = spinner1.getSelectedItem().toString();
		}
		else{
			groups_list = "";
		}
        }

public void onNothingSelected(AdapterView<?> parent){
    
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
	
	lv = (ListView) storeView.findViewById(R.id.listView4);
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