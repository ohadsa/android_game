package com.example.android_game;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;

public class Fragment_List extends Fragment {


    private AppCompatActivity activity;
    private CallBack_List callBackList;
    private CallBack_Map callBack_map ;
    private ArrayList<Player> top_10;
    private RecyclerView main_LST_records;
    private TextView lbl_Line_map ;


    private TextView frame1_LBL_details;


    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }

    public Fragment_List setCallBackList(CallBack_List callBackList) {
        this.callBackList = callBackList;
        return this;
    }

    public Fragment_List setCallBack_map(CallBack_Map callBack_map){
        this.callBack_map = callBack_map;
        return this;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        findViews(view);
        initViews();


        return view;
    }


    private void initViews() {

        top_10 = new ArrayList<>();
        MSPV3 instance = MSPV3.getMe();
        Gson g = new Gson();
        int numOfRecords = instance.getInt("NUMBER_OF_RECORDS", 0);

        for (int i = 0; i < numOfRecords; i++) {
            String str = instance.getString("player" + i, "");
            Player p;
            p = g.fromJson(str, Player.class);
            top_10.add(p);
        }

        ArrayList<View.OnClickListener> listeners = new ArrayList<>();


        if (numOfRecords == 0) {
            frame1_LBL_details.setText("not records yet");
        }
        else
            frame1_LBL_details.setText("touch name to see location on map");

        Adapter_Player adapter_player = new Adapter_Player(activity, top_10,callBackList,callBack_map);

        main_LST_records.setLayoutManager(new GridLayoutManager(activity, 1));
        main_LST_records.setHasFixedSize(true);
        main_LST_records.setItemAnimator(new DefaultItemAnimator());
        main_LST_records.setAdapter(adapter_player);

    }


    private void findViews(View view) {

        frame1_LBL_details = view.findViewById(R.id.frame1_LBL_details);
        main_LST_records = view.findViewById(R.id.main_LST_records);
        lbl_Line_map = view.findViewById(R.id.data);

    }

    public void updateText(String txt) {
        frame1_LBL_details.setText(txt);
    }
}