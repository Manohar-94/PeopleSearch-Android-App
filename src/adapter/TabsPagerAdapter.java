package adapter;

import in.ernet.iitr.peoplesearchbeta.Fragment_all;
import in.ernet.iitr.peoplesearchbeta.Fragment_faculties;
import in.ernet.iitr.peoplesearchbeta.Fragment_groups;
import in.ernet.iitr.peoplesearchbeta.Fragment_services;
import in.ernet.iitr.peoplesearchbeta.Fragment_students;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter{
	public TabsPagerAdapter(FragmentManager fm){
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {
		// TODO Auto-generated method stub
		switch(index){
		case 0:
			return new Fragment_all();
		case 1:
			return new Fragment_students();
		case 2:
			return new Fragment_faculties();
		case 3:
			return new Fragment_services();
		case 4:
			return new Fragment_groups();
		}
		return null;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 5;
	}

}
