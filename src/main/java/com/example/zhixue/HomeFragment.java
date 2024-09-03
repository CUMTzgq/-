package com.example.zhixue;

import android.content.Intent;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.layout_homefragment, container, false);
        ConstraintLayout coursebtn=view.findViewById(R.id.coursebtn);
        coursebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CourseActivity.class);
                startActivity(intent);
            }
        });
        ConstraintLayout xinghuobtn=view.findViewById(R.id.xunfei);
        xinghuobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), XunfeiActivity.class);
                startActivity(intent);
            }
        });
        ConstraintLayout xfyunbtn=view.findViewById(R.id.xfyun);
        xfyunbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), XfyunActivity.class);
                startActivity(intent);
            }
        });
        ConstraintLayout imagebtn=view.findViewById(R.id.image);
        imagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ImageActivity.class);
                startActivity(intent);
            }
        });
        ConstraintLayout generationbtn=view.findViewById(R.id.generation);
        generationbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), GenerationActivity.class);
                startActivity(intent);
            }
        });
        ConstraintLayout correctionbtn=view.findViewById(R.id.correction_btn);
        correctionbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CorrectionActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
}