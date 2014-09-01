package in.ernet.iitr.peoplesearchbeta;

import in.ernet.iitr.peoplesearchbeta.Fragment_students.AlertDialogFragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;

public class MainActivity extends SherlockFragmentActivity {
	
	public static int tag;
	public ActionBar actionbar;
	public static final String PREFS_NAME = "MyPrefsFile";
	String session_key="",name="",info="",enrollment_no="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		actionbar = getSupportActionBar();
		actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionbar.setTitle("Peoplesearch");
		
		ActionBar.Tab FragAll = actionbar.newTab().setText("All");
		ActionBar.Tab FragStud = actionbar.newTab().setText("Students");
		ActionBar.Tab FragFacu = actionbar.newTab().setText("Faculties");
		ActionBar.Tab FragServ = actionbar.newTab().setText("Services");
		ActionBar.Tab FragGrou = actionbar.newTab().setText("Groups");
		
		Fragment fragment_all = new Fragment_all();
		Fragment fragment_students = new Fragment_students();
		Fragment fragment_faculties = new Fragment_faculties();
		Fragment fragment_services = new Fragment_services();
		Fragment fragment_groups = new Fragment_groups();
		
		FragAll.setTabListener(new MyTabsListener(fragment_all));
		FragStud.setTabListener(new MyTabsListener(fragment_students));
		FragFacu.setTabListener(new MyTabsListener(fragment_faculties));
		FragServ.setTabListener(new MyTabsListener(fragment_services));
		FragGrou.setTabListener(new MyTabsListener(fragment_groups));
		
		actionbar.addTab(FragAll);
		actionbar.addTab(FragStud);
		actionbar.addTab(FragFacu);
		actionbar.addTab(FragServ);
		actionbar.addTab(FragGrou);
		
		actionbar.setBackgroundDrawable(new ColorDrawable(Color.RED));
				
	}
	
	class MyTabsListener implements ActionBar.TabListener{
		public Fragment fragment;
		
		public MyTabsListener(Fragment fragment){
			this.fragment = fragment;
		}
		
		@Override
		public void onTabSelected(Tab tab , FragmentTransaction ft){
			ft.replace(R.id.fragment_container, fragment , "current_tab" );
			tag = actionbar.getSelectedNavigationIndex();
			}
		
		@Override
		public void onTabUnselected(Tab tab , FragmentTransaction ft){
			
		}
		
		@Override
		public void onTabReselected(Tab tab , FragmentTransaction ft){
			
		}
		
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.menu, menu);
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        if (null != searchView )
        {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(false);   
        }

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() 
        {
            public boolean onQueryTextChange(String newText) 
            {
                
                return true;
            }

            public boolean onQueryTextSubmit(String query) 
            { 
            	shareData(query);
                           	
                return true;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);

        return super.onCreateOptionsMenu(menu);
				 
	}
	
	public boolean onOptionsItemSelected(MenuItem item){
		SharedPreferences settings = getSharedPreferences(PREFS_NAME,0);
		
		switch(item.getItemId()){
		case R.id.profile:
			
			Fragment_students ss = new Fragment_students();
			name = settings.getString("name", "");
			info = settings.getString("info", "");
			enrollment_no = settings.getString("enrollment_no", "");
			AlertDialogFragment a = ss.new AlertDialogFragment(info, enrollment_no, name);
			a.show(getSupportFragmentManager(), "alertdialog");
			
			return true;
			
		case R.id.logout:
			//HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost("http://192.168.121.5:8080/peoplesearch/logout_user/");
			session_key = settings.getString("session_key", "");
			List<NameValuePair> namevaluepair = new ArrayList<NameValuePair>(1);
			namevaluepair.add(new BasicNameValuePair("session_key",session_key));
			try{
				SplashScreen s = new SplashScreen();
				httpPost.setEntity(new UrlEncodedFormEntity(namevaluepair));
				String result = s.new ConnectTask().execute(httpPost).get();
				s.result = result;
				s.parseData();
				if(s.msg.equals("YES")){
					Toast toast = Toast.makeText(getApplicationContext(),"logged out successfully" , Toast.LENGTH_SHORT);
					toast.show();
					SharedPreferences.Editor editor = settings.edit();
					editor.putString("session_key", session_key);
					editor.commit();
					finish();
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
			
			return true;
			
		default:
			return super.onOptionsItemSelected(item);
		}
		
	}
	
	
public void shareData(String srch_str){
	FragmentManager fm = getSupportFragmentManager();
	if(tag==0){
		Fragment_all fragment = (Fragment_all) fm.findFragmentByTag("current_tab");
		fragment.update(srch_str);
	}
	else if(tag==1){
	Fragment_students fragment = (Fragment_students) fm.findFragmentByTag("current_tab");
	fragment.update(srch_str);
	}
	else if(tag==2){
		Fragment_faculties fragment = (Fragment_faculties) fm.findFragmentByTag("current_tab");
		fragment.update(srch_str);
	}
	else if(tag==3){
		Fragment_services fragment = (Fragment_services) fm.findFragmentByTag("current_tab");
		fragment.update(srch_str);
	}
	else{
		Fragment_groups fragment = (Fragment_groups) fm.findFragmentByTag("current_tab");
		fragment.update(srch_str);
	}
	
}

}
