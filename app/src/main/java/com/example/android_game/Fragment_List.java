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
    private TextView[] frame1_LBL_top;
    private ArrayList<Player> top_10;
    private RecyclerView main_LST_records;


    private TextView frame1_LBL_details;


    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void setCallBackList(CallBack_List callBackList) {
        this.callBackList = callBackList;
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


        if (numOfRecords == 0)
            frame1_LBL_details.setText("no records yet");
        else
            frame1_LBL_details.setText("touch name to see location on map");

        Adapter_Player adapter_player = new Adapter_Player(activity, top_10)
                .setCallBackList(callBackList);

        main_LST_records.setLayoutManager(new GridLayoutManager(activity, 1));
        main_LST_records.setHasFixedSize(true);
        main_LST_records.setItemAnimator(new DefaultItemAnimator());
        main_LST_records.setAdapter(adapter_player);

    }


    private void findViews(View view) {

        frame1_LBL_details = view.findViewById(R.id.frame1_LBL_details);
        main_LST_records = view.findViewById(R.id.main_LST_records);
        frame1_LBL_top = new TextView[10];
        frame1_LBL_top[0] = view.findViewById(R.id.frame1_LBL_top_1);
        frame1_LBL_top[1] = view.findViewById(R.id.frame1_LBL_top_2);
        frame1_LBL_top[2] = view.findViewById(R.id.frame1_LBL_top_3);
        frame1_LBL_top[3] = view.findViewById(R.id.frame1_LBL_top_4);
        frame1_LBL_top[4] = view.findViewById(R.id.frame1_LBL_top_5);
        frame1_LBL_top[5] = view.findViewById(R.id.frame1_LBL_top_6);
        frame1_LBL_top[6] = view.findViewById(R.id.frame1_LBL_top_7);
        frame1_LBL_top[7] = view.findViewById(R.id.frame1_LBL_top_8);
        frame1_LBL_top[8] = view.findViewById(R.id.frame1_LBL_top_9);
        frame1_LBL_top[9] = view.findViewById(R.id.frame1_LBL_top_10);

    }

    public void updateText(String txt) {
        frame1_LBL_details.setText(txt);
    }
}