package com.home.demomediaplayer;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener, Runnable, SeekBar.OnSeekBarChangeListener {
    private Handler handler;
    private static MediaPlayer mp;
    private static String previous = null;
    private TextView current, total;
    private Button play, stop, setSongImage;
    private String strpath, song;
    private int id = 1;
    private ImageView imageView;
    private byte[] image_array;
    private MediaMetadataRetriever retriever;
    private SeekBar seekBar;
    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Bundle bundle = getIntent().getExtras();
        song = bundle.getString("NAME");
        setTitle(song);
        strpath = bundle.getString("SONG");
        if(((previous != null)&&(!previous.equals(strpath)))||(previous == null)){
                if(mp!=null)
                    mp.release();
                mp= MediaPlayer.create(this, Uri.parse(strpath));
                mp.start();
                previous = strpath;
        }
        initialize();
        setImage();
        seekBar.setOnSeekBarChangeListener(this);
        play.setOnClickListener(this);
        setSongImage.setOnClickListener(this);
        stop.setOnClickListener(this);
    }

    private void setImage() {
        retriever = new MediaMetadataRetriever();
        retriever.setDataSource(this,Uri.parse(strpath));
        Song.setImage(imageView,retriever);
    }

    private void initialize() {
        handler = new Handler();
        current = (TextView)findViewById(R.id.currenttime);
        total = (TextView)findViewById(R.id.totaltime);
        play =  (Button)findViewById(R.id.play);
        stop =  (Button)findViewById(R.id.stop);
        setSongImage =(Button) findViewById(R.id.pic);
        imageView = (ImageView)findViewById(R.id.image);
        seekBar=(SeekBar)findViewById(R.id.seekbar);
        int millis = mp.getDuration();
        int sec=millis/1000;
        int min=sec/60;
        int rem=sec%60;

        play.setText("Pause");
        current.setText("0:0");
        total.setText(min+":"+rem);
        seekBar.setMax(millis);
        handler.postDelayed(this, 1);

        Intent notifyIntent =
                new Intent(Intent.makeMainActivity(new ComponentName(this, Main2Activity.class)));
        Bundle bundle = new Bundle();
        bundle.putString("SONG", strpath);
        bundle.putString("NAME", song);
        notifyIntent.putExtras(bundle);
// Sets the Activity to start in a new, empty task
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);
// Creates the PendingIntent
        PendingIntent pendingIntent=
                PendingIntent.getActivity(
                        this,
                        0,
                        notifyIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mNotifyManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle("Playing Song")
                .setContentText(song)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent);

    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.play: {
                    if(mp==null)
                    {

                    }
                    if (mp.isPlaying()) {
                        mp.pause();
                        play.setText("Play");
                    } else {
                        handler.postDelayed(this,1);
                        mp.start();
                        play.setText("Pause");
                    }
                    break;
                }case R.id.stop: {
                    mp.stop();
                    mp=null;
                    seekBar.setProgress(0);
                    current.setText("0:0");
                    play.setText("Play");
                    mp= MediaPlayer.create(this, Uri.parse(strpath));
                    int millis = mp.getDuration();
                    int sec=millis/1000;
                    int min=sec/60;
                    int rem=sec%60;
                    total.setText(min+":"+rem);
                    seekBar.setMax(millis);
                    break;
                }
            }
        }
        catch (Exception e){e.printStackTrace();}
    }
    @Override
    public void run() {
        if(mp!=null)
        {
            if(mp.getDuration()>mp.getCurrentPosition())
            {
                int millis = mp.getCurrentPosition();
                int sec=millis/1000;
                int min=sec/60;
                int rem=sec%60;
                current.setText(min+":"+rem);
                seekBar.setProgress(millis);
                handler.postDelayed(this, 1000);
                mBuilder.setProgress(mp.getDuration(), millis, false);
                mNotifyManager.notify(id, mBuilder.build());
            }
            else
            {
                mBuilder.setContentText("Song complete")
                        .setProgress(0,0,false);
                mNotifyManager.notify(id, mBuilder.build());
            }
        }
    }
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    }
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int millis=seekBar.getProgress();
        mp.seekTo(millis);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==2)
        {
            if(resultCode==RESULT_OK)
            {/*
                Bitmap bitmap=(Bitmap)data.getExtras().get("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                ImageData img = new ImageData(byteArray,"JPEG/bitmap","EmbededPicture",0);

                File src = new File(strpath);
                MusicMetadataSet src_set = null;
                try {
                    src_set = new MyID3().read(src);
                } catch (IOException e1) { } // read metadata

                if (!(src_set == null)){ // perhaps no metadata
                    File dst = new File(strpath);
                    MusicMetadata meta = new MusicMetadata("name");
                    meta.addPicture(img);
                    try {
                        new MyID3().write(src, dst, src_set, meta);
                    }catch (Exception e) {}  // write updated metadata
                }*/
            }
        }
    }
}


