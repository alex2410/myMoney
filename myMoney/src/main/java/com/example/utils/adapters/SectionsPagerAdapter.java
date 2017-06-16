package com.example.utils.adapters;

import java.util.List;
import java.util.Map.Entry;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;

public class SectionsPagerAdapter extends FragmentPagerAdapter implements OnPageChangeListener{

	private Activity activity;
	private List<Entry<Fragment, Integer>> fragments;

	public SectionsPagerAdapter(FragmentManager fm, Activity activity, List<Entry<Fragment, Integer>> fragments) {
		super(fm);
		this.activity = activity;
		this.fragments = fragments;
	}
	
	@Override
	public Fragment getItem(int position) {
		if(position < fragments.size()){
			Entry<Fragment, Integer> entry = fragments.get(position);
			return entry.getKey();
		}
		return null;
	}
	
	@Override
	public int getCount() {
		return fragments.size();
	}
	
	public List<Entry<Fragment, Integer>> getFragments() {
		return fragments;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		if(position < fragments.size()){
			Entry<Fragment, Integer> entry = fragments.get(position);
			return activity.getString(entry.getValue());
		}
		return null;
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int position) {
		if(position < fragments.size()){
			Entry<Fragment, Integer> entry = fragments.get(position);
			activity.setTitle(activity.getString(entry.getValue()));
		}
	}
}
