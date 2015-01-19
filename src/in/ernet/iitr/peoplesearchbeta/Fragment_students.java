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
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
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
public class Fragment_students extends SherlockFragment implements OnItemSelectedListener{

	private String course="",year="",srch_str="",role="stud",faculty_department="",faculty_designation="", services_list="",groups_list="";
	private Spinner spinner1 , spinner2;
	JSONObject jobj , json;
	JSONArray json1;
	private ListView lv;
	public View storeView;
	private int temp=0,counter=0;
	SlidingDrawer sd;
	ArrayList<ResultObject> list;
	PeopleListAdapter adapter;

	public View onCreateView(LayoutInflater inflater , ViewGroup container , Bundle savedInstanceState){

		final View view = inflater.inflate(R.layout.students, container, false);
		storeView = view;

		spinner1 = (Spinner) view.findViewById(R.id.spinner_course);
		ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this.getActivity(), R.array.course, android.R.layout.simple_spinner_item);
		adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner1.setAdapter(adapter1);

		spinner2 = (Spinner) view.findViewById(R.id.spinner_year);
		ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this.getActivity(), R.array.year, android.R.layout.simple_spinner_item);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner2.setAdapter(adapter2);

		sd = (SlidingDrawer) view.findViewById(R.id.slidingDrawer1);
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
			LinearLayout v= (LinearLayout) storeView.findViewById(R.id.linearsliding1);
			RelativeLayout.LayoutParams p = (RelativeLayout.LayoutParams) v.getLayoutParams();
			p.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
			v.setLayoutParams(p);
			sd.open();
		}
	}

	class MyDrawerCloseListener implements SlidingDrawer.OnDrawerCloseListener{
		public void onDrawerClosed(){
			LinearLayout v= (LinearLayout) storeView.findViewById(R.id.linearsliding1);
			RelativeLayout.LayoutParams p = (RelativeLayout.LayoutParams) v.getLayoutParams();
			p.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
			v.setLayoutParams(p);
		}
	}

	public void onItemSelected(AdapterView<?> parent, View v, int position, long id ){

		if (parent == spinner1){
			if(position != 0){
				course = spinner1.getSelectedItem().toString();
			}
			else{
				course = "";
			}

		}
		else if (parent == spinner2){
			if(position != 0){
				year =spinner2.getSelectedItem().toString();
			}
			else{
				year = "";
			}
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
		list = new ArrayList<ResultObject>();
		list = cp.parseData(cp.getData());
		
		lv = (ListView) storeView.findViewById(R.id.listView1);
		adapter = new PeopleListAdapter(this.getActivity(),list);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new MyClickListener(list));
		lv.setOnScrollListener(new MyScrollListener(cp,counter));
		
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
	
	class MyScrollListener implements OnScrollListener{

		ConnectAndParse cp;
		int counter;
		public MyScrollListener(ConnectAndParse cp, int counter){
			this.cp=cp;
			this.counter=counter;
		}
		@Override
		public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
			// TODO Auto-generated method stub
			
		}

		@SuppressLint("NewApi")
		@Override
		public void onScrollStateChanged(AbsListView lv, int scrollstate) {
			// TODO Auto-generated method stub
			if(scrollstate==SCROLL_STATE_IDLE){
				if(lv.getLastVisiblePosition() >= lv.getCount()-1-2){
					//request to be sent
					ArrayList<ResultObject> new_list = new ArrayList<ResultObject>();
					new_list = cp.parseData(cp.getData());
					list.addAll(new_list);
					adapter.addAll(list);
					adapter.notifyDataSetChanged();
/*					PeopleListAdapter adapter = new PeopleListAdapter(getActivity(), new_list);
					lv.setAdapter(adapter);
					lv.setOnItemClickListener(new MyClickListener(list));
					lv.setOnScrollListener(new MyScrollListener(cp,counter));					
*/				}
			}
		}
		
	}
	}

