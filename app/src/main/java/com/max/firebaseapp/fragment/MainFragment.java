package com.max.firebaseapp.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.max.firebaseapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_main, container, false);

        TextView textView = layout.findViewById(R.id.frag_main_text);
        textView.setOnClickListener(v ->{
            Toast.makeText(getActivity(),"Click",Toast.LENGTH_SHORT).show();
        });
        return layout;
    }
}
