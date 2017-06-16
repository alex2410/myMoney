package com.example.activities;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.example.R;
import com.example.fragments.AddSpendingFragment;
import com.example.fragments.ReportsFragment;
import com.example.fragments.SpendingTableFragment;
import com.example.utils.adapters.SectionsPagerAdapter;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

public class SpendingTableActivity extends BaseActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spending_table);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager(), this, getFragments());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(mSectionsPagerAdapter);
    }

    private List<Entry<Fragment, Integer>> getFragments() {
        List<Entry<Fragment, Integer>> fragments = new ArrayList<Entry<Fragment, Integer>>();
        fragments.add(new SimpleEntry<Fragment, Integer>(new SpendingTableFragment(), R.string.spendingSectionTable));
        fragments.add(new SimpleEntry<Fragment, Integer>(new AddSpendingFragment(), R.string.spendingSectionAdding));
        fragments.add(new SimpleEntry<Fragment, Integer>(new ReportsFragment(), R.string.spendingSectionReport));

        return fragments;
    }

    @Override
    public void onBackPressed() {
        int currentItem = mViewPager.getCurrentItem();
        if (currentItem > 0) {
            mViewPager.setCurrentItem(--currentItem);
        }
        // nothing to do
        //super.onBackPressed();
    }

    public ViewPager getPager() {
        return mViewPager;
    }

    public SectionsPagerAdapter getPageAdapter() {
        return mSectionsPagerAdapter;
    }

    public void refreshTable() {//Fag thing
        Object instantiateItem = mSectionsPagerAdapter.instantiateItem(mViewPager, 0);
        ((SpendingTableFragment) instantiateItem).refreshTable();
    }
}
