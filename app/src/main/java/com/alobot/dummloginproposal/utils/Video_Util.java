package com.alobot.dummloginproposal.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Matrix;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.Surface;
import android.view.TextureView;
import android.widget.RelativeLayout;

public class Video_Util {
    public static final String MEDIA_PLAYER_POSITION_KEY = "MEDIA_PLAYER_POSITION_KEY";

    public MediaPlayer prepareMediaPlayer(Context context, Surface surface, Uri uri) {
        MediaPlayer mediaPlayer = new MediaPlayer();

        try {
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(context, uri);
            mediaPlayer.setSurface(surface);
            mediaPlayer.prepare();
        } catch (Exception e) {
            e.printStackTrace();

            mediaPlayer = null;
        }

        return mediaPlayer;
    }

    public void setMediaPlayerListeners(MediaPlayer mediaPlayer,
                                        MediaPlayer.OnBufferingUpdateListener onBufferingUpdateListener,
                                        MediaPlayer.OnCompletionListener onCompletionListener,
                                        MediaPlayer.OnPreparedListener onPreparedListener,
                                        MediaPlayer.OnVideoSizeChangedListener onVideoSizeChangedListener) {
        mediaPlayer.setOnBufferingUpdateListener(onBufferingUpdateListener);
        mediaPlayer.setOnCompletionListener(onCompletionListener);
        mediaPlayer.setOnPreparedListener(onPreparedListener);
        mediaPlayer.setOnVideoSizeChangedListener(onVideoSizeChangedListener);
    }

    public void configureScreenOrientation(Activity activity, MediaPlayer mediaPlayer,
                                           TextureView textureView) {
        // Gets Screen Orientation
        if (activity.getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_PORTRAIT) {
            portraitVideo(activity, mediaPlayer, textureView);
        } else if (activity.getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE) {
            landscapeVideo(activity, mediaPlayer, textureView);
        }
    }

    public void portraitVideo(Activity activity, MediaPlayer mediaPlayer, TextureView textureView) {
        float scaleX = 0.5f;
        float scaleY = 0.5f;
        float videoWidth = mediaPlayer.getVideoWidth();
        float videoHeight = mediaPlayer.getVideoHeight();
        int viewWidth = activity.getWindowManager().getDefaultDisplay().getWidth();
        int viewHeight = activity.getWindowManager().getDefaultDisplay().getHeight();

        if (videoWidth > viewWidth && videoHeight > viewHeight) {
            scaleX = videoWidth / viewWidth;
            scaleY = videoHeight / viewHeight;
        } else if (videoWidth < viewWidth && videoHeight < viewHeight) {
            scaleY = viewWidth / videoWidth;
            scaleX = viewHeight / videoHeight;
        } else if (viewWidth > videoWidth) {
            scaleY = (viewWidth / videoWidth) / (viewHeight / videoHeight);
        } else if (viewHeight > videoHeight) {
            scaleX = (viewHeight / videoHeight) / (viewWidth / videoWidth);
        }

        // Calculate pivot points, in our case crop from center
        int pivotPointX = viewWidth / 2;
        int pivotPointY = viewHeight / 2;

        Matrix matrix = new Matrix();
        matrix.setScale(scaleX, scaleY, pivotPointX, pivotPointY);

        textureView.setTransform(matrix);
        textureView.setLayoutParams(new RelativeLayout.LayoutParams(viewWidth, viewHeight));
    }

    public void landscapeVideo(Activity activity, MediaPlayer mediaPlayer, TextureView textureView) {
        // Get the dimensions of the video
        int videoWidth = mediaPlayer.getVideoWidth();
        int videoHeight = mediaPlayer.getVideoHeight();

        // Get the width of the screen
        int screenWidth = activity.getWindowManager().getDefaultDisplay().getWidth();

        // Get the SurfaceView layout parameters
        android.view.ViewGroup.LayoutParams textureViewLayoutParams = textureView.getLayoutParams();

        // Set the width of the SurfaceView to the width of the screen
        textureViewLayoutParams.width = screenWidth;

        //Set the height of the SurfaceView to match the aspect ratio of the video
        //be sure to cast these as floats otherwise the calculation will likely be 0
        textureViewLayoutParams.height = (int) (((float) videoHeight / (float) videoWidth) *
                (float) screenWidth);

        //Commit the layout parameters
        textureView.setLayoutParams(textureViewLayoutParams);
    }
}