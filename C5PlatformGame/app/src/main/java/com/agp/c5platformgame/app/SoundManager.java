package com.agp.c5platformgame.app;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;

import java.io.IOException;

/**
 * To comment out a line = ctrl + (keypad /)
 * To block comment = ctrl +  shift + (keypad /)
 * To refactor = shift f6
 * To remove unused imports = ctrl + alt + o
 * To format code = ctrl + alt + L
 * To run class = ctrl +  shift + F10
 * Finds the next occurrence of the currently selected text = ctrl + F3
 *
 * The class handles all the sounds in the game.
 */
public class SoundManager
{
    private SoundPool mSoundPool;
    private int mShoot = -1;
    private int mJump = -1;
    private int mTeleport = -1;
    private int mCoin_pickup = -1;
    private int mGun_upgrade = -1;
    private int mPlayer_burn = -1;
    private int mRicochet = -1;
    private int mHit_guard = -1;
    private int mExplode = -1;
    private int mExtra_life = -1;

    /**
     * Loads all the sounds into memory
     * @param context
     */
    public void loadSound(Context context)
    {
        //Use the new soundPool or the deprecated version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            final AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();

            mSoundPool = new SoundPool.Builder()
                    .setMaxStreams(10)
                    .setAudioAttributes(audioAttributes)
                    .build();
        }
        else
        {
            mSoundPool = new SoundPool(10, AudioManager.STREAM_MUSIC,0);
        }

        try{
            //create objects of the 2 required classes
            AssetManager assetManager = context.getAssets();
            AssetFileDescriptor descriptor;

            //create fx
            descriptor = assetManager.openFd("shoot.ogg");
            mShoot = mSoundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("jump.ogg");
            mJump = mSoundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("teleport.ogg");
            mTeleport = mSoundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("coin_pickup.ogg");
            mCoin_pickup = mSoundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("gun_upgrade.ogg");
            mGun_upgrade = mSoundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("player_burn.ogg");
            mPlayer_burn = mSoundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("ricochet.ogg");
            mRicochet = mSoundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("hit_guard.ogg");
            mHit_guard = mSoundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("explode.ogg");
            mExplode = mSoundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("extra_life.ogg");
            mExtra_life = mSoundPool.load(descriptor, 0);
        } catch (IOException e)
        {
            //print an error message to the console
            Log.e("error", "failed to load sound files");
            //e.printStackTrace();
        }
    }

    public void playSound(String sound)
    {
        switch (sound)
        {
            case "shoot":
                //play(left vol, right vol, priority, rate)
                mSoundPool.play(mShoot,1,1,0,0,1);
                break;
            case "jump":
                mSoundPool.play(mJump,1,1,0,0,1);
                break;
            case "teleport":
                mSoundPool.play(mTeleport,1,1,0,0,1);
                break;
            case "coin_pickup":
                mSoundPool.play(mCoin_pickup,1,1,0,0,1);
                break;
            case "gun_upgrade":
                mSoundPool.play(mGun_upgrade,1,1,0,0,1);
                break;
            case "player_burn":
                mSoundPool.play(mPlayer_burn,1,1,0,0,1);
                break;
            case "ricochet":
                mSoundPool.play(mRicochet,1,1,0,0,1);
                break;
            case "hit_guard":
                mSoundPool.play(mHit_guard,1,1,0,0,1);
                break;
            case "explode":
                mSoundPool.play(mExplode,1,1,0,0,1);
                break;
            case "extra_life":
                mSoundPool.play(mExtra_life,1,1,0,0,1);
                break;
        }
    }
}