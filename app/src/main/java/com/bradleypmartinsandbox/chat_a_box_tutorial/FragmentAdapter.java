package com.bradleypmartinsandbox.chat_a_box_tutorial;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class FragmentAdapter extends FragmentPagerAdapter {

    public FragmentAdapter(FragmentManager manager) { super(manager); }

    public int getCount() { return 3; }

    public Fragment getItem(int position) {

        Fragment f = new Fragment();
        return f;

    }

}
