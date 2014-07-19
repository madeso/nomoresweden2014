package com.madeso.platformer;

import com.badlogic.gdx.graphics.g2d.Animation;

public class AnimationGroup {
    float state = 0;
    private SmartAnimation animation;

    public AnimationGroup(SmartAnimation animation) {
        this.animation = animation;
    }

    public void update(float dt) {
        state += dt;
    }

    public void changeAnimation(SmartAnimation animation) {
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
}
