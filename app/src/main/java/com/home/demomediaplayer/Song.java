package com.home.demomediaplayer;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

/**
 * Created by Sagar Pahwa on 13-05-2016.
 */
public class Song extends AsyncTask<MediaMetadataRetriever,Integer,Bitmap> {

    private String song;
    private MediaMetadataRetriever retriever = new MediaMetadataRetriever();
    private String path;
    private Activity listActivity;
    private boolean selected;

    public MediaMetadataRetriever getRetriever() {
        return retriever;
    }

    public Song(){}

    public String getPath() {
        return path;
    }

    public Song(String path, String song, Activity listActivity) {
        this.path = path;
        this.selected = false;
        this.retriever.setDataSource(listActivity, Uri.parse(path));
        this.song = song;
        this.listActivity = listActivity;
    }

    public String getSong() {
        return song;
    }

    public static String getMcontentType(MediaMetadataRetriever retriever) {
        return retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE);
    }

    public static String getMdur(MediaMetadataRetriever retriever) {
        return retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
    }

    public static String getMalbum(MediaMetadataRetriever retriever) {
        return retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
    }

    public static String getMalbumArtist(MediaMetadataRetriever retriever) {
        return retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST);
    }

    public static String getMauthor(MediaMetadataRetriever retriever) {
        return retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_AUTHOR);
    }

    public static String getMcomposer(MediaMetadataRetriever retriever) {
        return retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_COMPOSER);
    }

    public static String getMgenre(MediaMetadataRetriever retriever) {
        return retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE);
    }

    public static void setImage(ImageView imageView, MediaMetadataRetriever retriever) {
        byte[] bytes = retriever.getEmbeddedPicture();
        if (bytes != null)
            imageView.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
        else
            imageView.setImageResource(R.mipmap.ic_launcher);
    }

    @Override
    public Bitmap doInBackground(MediaMetadataRetriever... params) {
        byte[] image_array = retriever.getEmbeddedPicture();
        if (image_array != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(image_array, 0, image_array.length);
            return bitmap;
        }
        else return null;
    }

    public void detailsDialog() {
        final Song song = this;
        int duration = 0;
        MediaMetadataRetriever retriever = this.getRetriever();
        CharSequence message = null;
        if(Song.getMcontentType(retriever)!= null) {
            message = "Content Type: "+Song.getMcontentType(retriever)+"\n\n";
        }if(Song.getMalbum(retriever) != null) {
            message = message+"Album: "+Song.getMalbum(retriever)+"\n\n";
        }if(Song.getMalbumArtist(retriever) != null) {
            message = message+"Album Artist: "+Song.getMalbumArtist(retriever)+"\n\n";
        }if(Song.getMauthor(retriever) != null) {
            message = message+"Author: "+Song.getMauthor(retriever)+"\n\n";
        }if(Song.getMcomposer(retriever) != null) {
            message = message+"Composer: "+Song.getMcomposer(retriever)+"\n\n";
        }if(Song.getMgenre(retriever) != null) {
            message = message+"Genre: "+Song.getMgenre(retriever)+"\n\n";
        }if(Song.getMdur(retriever) != null) {
            duration = Integer.parseInt(Song.getMdur(retriever));
            int sec=duration/1000;
            int min=sec/60;
            int rem=sec%60;
            message = message+"Duration: "+min+" : "+rem+"";
        }
        final AlertDialog dialog =
                new AlertDialog.Builder(listActivity)
                        .setTitle("Details")
                        .setMessage(message)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(listActivity, "cool dialog..", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();}})
                        .setNeutralButton("Delete Song",new  DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                File file = new File(song.getPath());
                                boolean deleted = file.delete();
                                String confirm = null;
                                if(deleted) {
                                    confirm = "File Deleted.";
                                    /*songPath.remove(position);
                                        //songlist.remove(songName.get(position));
                                    songName.remove(position);*/
                                    //listView.setAdapter(songlist);
                                }else
                                    confirm = "Delete fail.";
                                Toast.makeText(listActivity,confirm,Toast.LENGTH_SHORT).show();}
                        })
                        .create();
        dialog.show();
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
