package com.termproject.familyprotector;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class ChildDetailPagerAdapter extends FragmentStatePagerAdapter{
    private int numOfTabs;

    public ChildDetailPagerAdapter(FragmentManager fm, int numOfTabs){
        super(fm);
        this.numOfTabs = numOfTabs;
    }
    @Override
    public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    ChildAlertFragment childAlertFragment = new ChildAlertFragment();
                    return childAlertFragment;
                case 1:
                    ChildRuleFragment childRuleFragment = new ChildRuleFragment();
                    return childRuleFragment;
                default:
                    return null;
            }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
