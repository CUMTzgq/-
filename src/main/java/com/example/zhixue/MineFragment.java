package com.example.zhixue;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import static com.example.zhixue.Utils.maskEmail;


public class MineFragment extends Fragment {


    public MineFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.layout_minefragment, container, false);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        String email=maskEmail(sharedPreferences.getString("email",""));
        TextView name=view.findViewById(R.id.name);
        name.setText(email);
        ConstraintLayout zh1=view.findViewById(R.id.zh1);
        ConstraintLayout zh2=view.findViewById(R.id.zh2);
        View.OnClickListener onClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AccountActivity.class);
                startActivity(intent);
            }
        };
        zh1.setOnClickListener(onClickListener);
        zh2.setOnClickListener(onClickListener);
        // Inflate the layout for this fragment
        return view;


    }
}