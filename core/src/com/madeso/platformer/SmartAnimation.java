package com.madeso.platformer;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

public class SmartAnimation extends SmartRegion {
    private float duration = 1;
    private int[][] frames = null;
    public Animation animation;
    public boolean looping=true;



    public SmartAnimation(AssetManager assetManager, String name) {
        super(assetManager, name);
    }

    public SmartAnimation setAnimation(float duration, int[][] frames) {
        this.duration = duration;
        this.frames = frames;
        return this;
    }

    public SmartAnimation setLooping(boolean loop ) {
        this.looping = loop;
        return this;
    }

    @Override
    public void postLoad() {
        super.postLoad();
        Array<TextureRegion> regions = new Array<TextureRegion>();

        if( frames == null ) {
            regions.add( this.regions[0][0]);
        }
        else {
            for (int i = 0; i < frames.length; ++i) {
                int x = frames[i][0];
                int y = frames[i][1];
                regions.add(this.regions[x][y]);
            }
        }

        this.animation = new Animation(this.duration, regions);
    }
}
