package com.home.demomediaplayer;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Sagar Pahwa on 13-05-2016.
 */
public class SongAdapter extends BaseAdapter {
        private Activity listActivity;
    private ArrayList<Song> list = null;
    private int viewId;

    public SongAdapter(Activity listActivity, int viewId, ArrayList<Song> list) {
        this.listActivity = listActivity;
        this.viewId = viewId;
        this.list = list;
    }
/*
    @Override
    public int getCount() {
        return list.size();
    }*/

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Song getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        PlaceHolder holder = null;
        if (convertView == null) {
            convertView = listActivity.getLayoutInflater().inflate(viewId, parent, false);
            holder = new PlaceHolder();
            holder.name = (TextView) convertView.findViewById(R.id.song_name);
            holder.pic = (ImageView) convertView.findViewById(R.id.mimage);
            holder.author = (TextView) convertView.findViewById(R.id.author);
            holder.menu = (ImageView) convertView.findViewById(R.id.mmenu);
            /*if(new MainActivity().checkItemCkicked(position)) {
                convertView.setSelected(false);
                Log.i("msg","found");
                //convertView.setBackgroundColor(R.color.colorAccent);
            }
            else {
                convertView.setSelected(true);
                Log.i("msg2","miss");
               /// convertView.setBackgroundColor(R.color.itemSelected);
            }*/
            /*if(new MainActivity().checkItemCkicked(position)){
                convertView.setSelected(false);
                convertView.setPressed(false);
            }
            else
            {
                convertView.setSelected(true);
                convertView.setPressed(true);
            }*/
            convertView.setTag(holder);
        }
        else {
            holder = (PlaceHolder)convertView.getTag();
            holder.name.setText(list.get(position).getSong());
        }
        try {
            holder.menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Log.i("msg",position+"");
                    // Toast.makeText(listActivity,"Heelo "+position, Toast.LENGTH_LONG).show();
                    list.get(position).detailsDialog();
                }
            });
        }
        catch (Exception e){}
        holder.name.setText(list.get(position).getSong());
        if(Song.getMauthor(list.get(position).getRetriever())!=null)
            holder.author.setText(Song.getMauthor(list.get(position).getRetriever()));
        else
            holder.author.setText("Unknown Author");
        Song.setImage(holder.pic,list.get(position).getRetriever());
            //holder.pic.setImageBitmap(list.get(position).doInBackground(list.get(position).getRetriever()));
        //Song.setImage(holder.pic, list.get(position).getRetriever());
        return convertView;
        }

    class PlaceHolder{
            TextView name;
            TextView author;
            ImageView pic;
            ImageView menu;
        }
    }
