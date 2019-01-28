package com.bradleypmartinsandbox.chat_a_box_tutorial;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class FragmentAdapter extends FragmentPagerAdapter {

    HistoryFragment mHistory;

    public FragmentAdapter(FragmentManager manager) {
        super(manager);
        mHistory = HistoryFragment.newInstance(1);
    }

    public int getCount() { return 3; }

    public Fragment getItem(int position) {

        Fragment page = null;

        switch (position) {
            case 0:
                page = ChatMessageFragment.newInstance("One","Two");
                break;
            case 1:
                page = mHistory;
                break;
            case 2:
                page = MembersFragment.newInstance(2);
                break;
            default:
                page = ChatMessageFragment.newInstance("One","Two");
                break;
        }
        return page;
    }

    public CharSequence getPageTitle(int position) {
        CharSequence result = "";

        switch (position) {
            case 0:
                result = "Chat";
                break;
            case 1:
                result = "History";
                break;
            case 2:
                result = "Members";
                break;
            default:
                result = "Error";
        }
        return result;
    }

}
