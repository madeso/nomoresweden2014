package com.madeso.platformer;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;

public class Trigger extends WorldObject {
    public int nextLevel = -1;

    public Trigger(PlatformGame game, MapObject object, float x, float y) {
        super(game);
        this.teleport(x,y);

        String name = object.getName();
        if( name != null ) {
            this.nextLevel = Integer.parseInt(name);
        }
    }

    @Override
    protected void subupdate(float dt) {
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera cam) {
    }

    public void trigger() {
        this.removeMe = true;
        System.out.println("Triggered");
    }
}
