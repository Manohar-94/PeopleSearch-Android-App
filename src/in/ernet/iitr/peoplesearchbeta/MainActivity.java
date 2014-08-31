package in.ernet.iitr.peoplesearchbeta;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;

public class MainActivity extends SherlockFragmentActivity {
	
	public static int tag;
	public ActionBar actionbar;
	
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
		switch(item.getItemId()){
		case R.id.profile:
			return true;
		case R.id.logout:
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
