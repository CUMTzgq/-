package com.example.zhixue;

import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ViewPager vp;
    private BottomNavigationView btmnavview;
    private FragmentStateVPAdapter svpadapter;
    private List<Fragment> fragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this)
                .init();
        setContentView(R.layout.layout_main);
        vp=findViewById(R.id.vp);
        btmnavview=findViewById(R.id.btmnavview);
        initData();
        svpadapter= new FragmentStateVPAdapter(getSupportFragmentManager(),fragmentList);
        vp.setAdapter(svpadapter);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                onpagerSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        btmnavview.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull  MenuItem item) {
                if(item.getItemId()==R.id.menuHome){
                    vp.setCurrentItem(0);
                }else{
                    vp.setCurrentItem(1);
                }
                return true;
            }
        });

    }
    private void onpagerSelected(int position) {
        switch(position){
            case 0:
                btmnavview.setSelectedItemId(R.id.menuHome);
                break;
            case 1:
                btmnavview.setSelectedItemId(R.id.menuMine);
                break;
        }
    }


    private void initData() {
        fragmentList=new ArrayList<>();
        HomeFragment homepage=new HomeFragment();
        MineFragment minepage=new MineFragment();
        fragmentList.add(homepage);
        fragmentList.add(minepage);
    }
}