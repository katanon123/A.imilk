package com.example.user.restuarant;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by USER on 9/1/2559.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {
    int numoftabs;

    public PagerAdapter(FragmentManager fm, int tabs){
        super(fm);
        this.numoftabs = tabs;
    }

    @Override
    public Fragment getItem(int position){

        switch (position){
            case 0:
                FragmentService tab1 = new FragmentService();//เมนู
                return tab1;
            case 1:
                FragmentProfile tab2 = new FragmentProfile();//สเตตัส
                return tab2;
            case 2:
                FragmentHome tab3 = new FragmentHome();
                return tab3;
            case 3:
                Fragmentetc tab4 = new Fragmentetc();
                return tab4;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {return numoftabs;}
}
