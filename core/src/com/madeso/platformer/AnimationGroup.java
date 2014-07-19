package com.madeso.platformer;

import com.badlogic.gdx.graphics.g2d.Animation;

import java.util.Random;

public class AnimationGroup {
    float state = 0;
    private SmartAnimation animation;

    private static Random random = new Random();
    public AnimationGroup(SmartAnimation animation) {
        this.animation = animation;
        this.state = random.nextFloat();
        if( animation == null ) throw new NullPointerException("supplied anim is null");
    }

    public void update(float dt) {
        state += dt;
    }

    public void changeAnimation(SmartAnimation animation) {
        if( animation == null ) throw new NullPointerException("supplied anim is null");
        if( animation != this.animation ) {
            state = 0;
            this.animation = animation;
        }
    }

    public Animation getAnimation() {
        return this.animation.animation;
    }

    public float getTime() {
        return state;
    }

    public boolean isLooping() {
        return this.animation.looping;
    }
}
