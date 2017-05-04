package com.alobot.dummloginproposal;

import android.app.Activity;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Toast;

import com.alobot.dummloginproposal.utils.Base_Util;
import com.alobot.dummloginproposal.utils.File_Util;
import com.alobot.dummloginproposal.utils.Video_Util;

public class MainActivity extends AppCompatActivity implements TextureView.SurfaceTextureListener,
        MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnVideoSizeChangedListener {
    private int mMediaPlayerPosition;

    private Activity mActivity;
    private Context mContext;
    private Base_Util mBase_Util;
    private File_Util mFileUtil;
    private Video_Util mVideo_Util;
    private MediaPlayer mMediaPlayer;
    private TextureView mTextureView;
    // private RelativeLayout mRelativeLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hideActionBar();

        setContentView(R.layout.activity_main);

        initializeMembers();

        if (savedInstanceState != null) {
            mMediaPlayerPosition = savedInstanceState.getInt(Video_Util.MEDIA_PLAYER_POSITION_KEY);
        }
    }

    private void hideActionBar() {
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.hide();
            // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            //  WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    private void initializeMembers() {
        mActivity = MainActivity.this;
        mContext = getApplicationContext();
        // mRelativeLayout = (RelativeLayout) findViewById(R.id.relative_layout_main);

        mTextureView = (TextureView) findViewById(R.id.surface);
        mTextureView.setSurfaceTextureListener(this);
        mTextureView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "On Video Tapped", Toast.LENGTH_SHORT).show();
                // Snackbar.make(mRelativeLayout, "Video Tapped", Snackbar.LENGTH_SHORT).show();
            }
        });

        mFileUtil = new File_Util();
        mVideo_Util = new Video_Util();
        mBase_Util = new Base_Util();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mMediaPlayer != null) {
            mMediaPlayer.seekTo(mMediaPlayerPosition);
            mMediaPlayer.start();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mMediaPlayer != null) {
            mMediaPlayer.pause();
            mMediaPlayerPosition = mMediaPlayer.getCurrentPosition();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(Video_Util.MEDIA_PLAYER_POSITION_KEY, mMediaPlayerPosition);
    }

    public void onClickTextViewMoreInformation(View view) {
        Toast.makeText(mContext, "More Information Tapped", Toast.LENGTH_SHORT).show();
    }

    public void onClickTextViewForgotPassword(View view) {
        Toast.makeText(mContext, "Forgot Password Tapped", Toast.LENGTH_SHORT).show();
    }

    public void onClickButtonLogin(View view) {
        Toast.makeText(mContext, "Log In Tapped", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
        Surface surface = new Surface(surfaceTexture);
        Uri uri = mFileUtil.getRawUri(mContext, R.raw.advertising);

        mMediaPlayer = mVideo_Util.prepareMediaPlayer(mContext, surface, uri);

        if (mMediaPlayer != null) {
            mVideo_Util.setMediaPlayerListeners(mMediaPlayer, this, this, this, this);
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int width, int height) {
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int percent) {
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        if (mMediaPlayer != null) {
            mVideo_Util.configureScreenOrientation(mActivity, mMediaPlayer, mTextureView);

            if (mMediaPlayerPosition != 0) {
                mMediaPlayer.seekTo(mMediaPlayerPosition);
            }

            mMediaPlayer.start();
        }
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mediaPlayer, int width, int height) {
    }
}