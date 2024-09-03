package com.example.zhixue;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FragmentStateVPAdapter extends FragmentStatePagerAdapter {
    private List<Fragment>fragmentList;

    public FragmentStateVPAdapter(@NonNull FragmentManager fm, List<Fragment> fragmentList){
        super(fm);
        this.fragmentList=fragmentList;
    }

    @NotNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList == null?null:fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList==null?0:fragmentList.size();
    }
}
