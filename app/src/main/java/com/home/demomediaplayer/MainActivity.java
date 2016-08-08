package com.home.demomediaplayer;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AbsListView.MultiChoiceModeListener, AdapterView.OnItemClickListener  //, AdapterView.OnItemLongClickListener
{
    public ArrayList<Song> songName = new ArrayList<Song>();
    public ArrayList<String> songPath = new ArrayList<String>();
    public ArrayList<String> name = new ArrayList<String>();
    private ListView listView;
    private SongAdapter songlistadapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            File f = Environment.getExternalStorageDirectory();
            getSongs(f);
            f = new File("/storage/sdcard0");
            if (f.exists())
                getSongs(f);
            f = new File("/storage/sdcard1");
            if (f.exists())
                getSongs(f);
        }catch (Exception e) {
            Log.e("asdf","asdkfnaldskf lasdknfladsk f"+e);
        }
        //listViewSetup
        listView = (ListView) findViewById(R.id.mylist);
        songlistadapter = new SongAdapter(this, R.layout.song_item, songName);
        if (listView != null) {
            listView.setAdapter(songlistadapter);
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            listView.setOnItemClickListener(this);//  listView.setOnItemLongClickListener(this);
            listView.setMultiChoiceModeListener(this);
        }
    }
    public void getSongs(File f) {
        try {
            File list[] = f.listFiles();
            for (int i = 0; i < list.length; i++) {
                if (list[i].isFile()) {
                    if (list[i].getName().toLowerCase().endsWith(".mp3")) {
                        songPath.add(list[i].getAbsolutePath());
                        songName.add(new Song(list[i].getAbsolutePath(), list[i].getName(), this));
                    }
                } else if (list[i].isDirectory()) {
                    getSongs(list[i]);
                }
            }
        }
        catch (Exception e)
        {}
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
/*        if(songName.get(position).isSelected())
            view.setBackgroundColor(Color.YELLOW);
        else
            view.setBackgroundColor(Color.GREEN);*/
        Intent resultIntent = new Intent(this,Main2Activity.class);
        /*int notificatonId = 1;
        Intent resultIntent = new Intent(this, Main2Activity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(Main2Activity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.notify(notificatonId, builder.build()); */
        Bundle bundle = new Bundle();
        bundle.putString("SONG", songPath.get(position));
        bundle.putString("NAME", songName.get(position).getSong());
        resultIntent.putExtras(bundle);
        startActivity(resultIntent);
    }
   /* @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        songName.get(position).detailsDialog();
        return true;
    }*/
    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
        songName.get(position).setSelected(checked);

    }
    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.multi_item_select_menu, menu);
        return true;
    }
    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }
    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        return false;
    }
    @Override
    public void onDestroyActionMode(ActionMode mode) {
    }
}