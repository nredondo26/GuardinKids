package com.nestnetgroup.guardinkids.adapter_frame;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.nestnetgroup.guardinkids.frame.ajustes;
import com.nestnetgroup.guardinkids.frame.dispositivos;
import com.nestnetgroup.guardinkids.frame.reportes;

/**
 * Created by NestNet on 2/05/2018.
 */

public class Pager_Adapter extends FragmentStatePagerAdapter {
    int Number_tab;

    public Pager_Adapter(FragmentManager fm, int Number_tab) {
        super(fm);
        this.Number_tab=Number_tab;
    }

    @Override
    public Fragment getItem(int position) {
         switch (position){
             case 0:
                 return  new dispositivos();
             case 1:
                 return  new reportes ();
             case 2:
                 return  new ajustes();
         }

        return null;
    }

    @Override
    public int getCount() {
        return Number_tab;
    }
}
