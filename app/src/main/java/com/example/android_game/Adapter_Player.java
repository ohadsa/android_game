package com.example.android_game;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class Adapter_Player extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity activity;
    private ArrayList<Player> players = new ArrayList<>();
    private CallBack_List callBackList ;
    private CallBack_Map callBack_map;
    private int[] numbers_image = new int[10] ;

    public Adapter_Player(Activity activity, ArrayList<Player> movies , CallBack_List callBackList , CallBack_Map callBack_map ) {

        this.callBack_map = callBack_map;
        this.callBackList = callBackList;
        this.activity = activity;
        this.players = movies;

        numbers_image[0] = R.drawable.one;
        numbers_image[1] = R.drawable.two;
        numbers_image[2] = R.drawable.three;
        numbers_image[3] = R.drawable.four;
        numbers_image[4] = R.drawable.five;
        numbers_image[5] = R.drawable.six;
        numbers_image[6] = R.drawable.seven;
        numbers_image[7] = R.drawable.eight;
        numbers_image[8] = R.drawable.nine;
        numbers_image[9] = R.drawable.ten ;

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_player, parent, false );
        return new PlayerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Player player = getItem(position);
        PlayerViewHolder playerViewHolder = (PlayerViewHolder)  holder;
        playerViewHolder.setPlayer(player);
        playerViewHolder.setImage(numbers_image[position]);
        playerViewHolder.player_LBL_Name.setText(player.getName());
        playerViewHolder.player_LBL_score.setText(""+player.getScore());



    }

    public Adapter_Player setCallBackList(CallBack_List callBackList){
        this.callBackList = callBackList ;
        return this;
    }

    public Adapter_Player setCallBackMap(CallBack_Map callBackMap){
        this.callBack_map = callBackMap ;
        return this;
    }

    private Player getItem(int position) {
        return players.get(position);
    }

    @Override
    public int getItemCount() {
        return players.size();
    }



    private class PlayerViewHolder extends RecyclerView.ViewHolder {
        public MaterialTextView player_LBL_Name;
        public MaterialTextView player_LBL_score;
        public AppCompatImageView player_IMG_image;
        private Player p;

        public PlayerViewHolder(View view  ) {
            super(view);
            this.player_IMG_image = view.findViewById(R.id.player_IMG_image);
            this.player_LBL_Name = view.findViewById(R.id.player_LBL_name);
            this.player_LBL_score = view.findViewById(R.id.player_LBL_score);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    callBackList.placeOnMap(p.getLat() , p.getLon() , p.getName() );
                    if(p.getLat() == Fragment_Map.THAILAND_LANTITUDE && p.getLon() == Fragment_Map.THAILAND_LONDTITUDE )
                        callBack_map.rename_lbl("was not GPS connection so that is thailand" , false );
                }
            });
        }


        public void setPlayer(Player player) {
            this.p = player ;
        }

        public void setImage(int l) {
            this.player_IMG_image.setImageResource(l);
        }
    }


}
