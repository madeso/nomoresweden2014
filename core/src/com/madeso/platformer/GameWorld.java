package com.madeso.platformer;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;
import java.util.List;

public class GameWorld implements Disposable {
    List<WorldObject> objects = new ArrayList<WorldObject>();
    List<WorldObject> special = new ArrayList<WorldObject>();
    boolean looping = false;
    List<WorldObject> objectsToAdd = new ArrayList<WorldObject>();
    List<WorldObject> specialToRemove = new ArrayList<WorldObject>();

    List<WorldObject> objectsToRemove = new ArrayList<WorldObject>();

    public void update(float delta, OrthoMap map) {
        looping = true;

        for (WorldObject m : objects) {
            if( m.update(delta) ) {
                objectsToRemove.add(m);
            }
        }
        objects.removeAll(objectsToRemove);
        for (WorldObject m : objectsToRemove) {
            m.dispose();
        }
        objectsToRemove.clear();

        for (WorldObject m : special) {
            if( m.update(delta) ) {
                specialToRemove.add(m);
            }
        }
        special.removeAll(specialToRemove);
        for (WorldObject m : specialToRemove) {
            m.dispose();
        }
        specialToRemove.clear();

        for (Moveable m : objects) {
            m.applyMovement(map);
        }
        for (Moveable m : special) {
            m.applyMovement(map);
        }
        looping = false;
        objects.addAll(objectsToAdd);
        objectsToAdd.clear();
    }

    @Override
    public void dispose() {
        for (WorldObject m : objects) {
            m.dispose();
        }
    }

    public void spawn(WorldObject object) {
        List<WorldObject> target = objects;
        if( looping ) target = objectsToAdd;
        target.add(object);
    }

    public void render(SpriteBatch batch, OrthographicCamera worldCamera) {
        for (WorldObject m : special) {
            m.render(batch, worldCamera);
        }

        for (WorldObject m : objects) {
            m.render(batch, worldCamera);
        }
    }

    public void add(WorldObject body) {
       if( body == null ) throw new NullPointerException("Body is null");
        this.special.add(body);
    }

    public static interface Collision {
        void collide(WorldObject left, WorldObject right);
    }

    public void overlap(Class left, Class right, Collision handler) {
        for (WorldObject a : objects) {
            if( a.getClass() == left ) {
                for (WorldObject b : objects) {
                    if( b.getClass() == right ) {
                        if( overlap(a, b) ) {
                            handler.collide(a, b);
                        }
                    }
                }
            }
        }
    }

    private boolean overlap(WorldObject a, WorldObject b) {
        return a.getRect().overlaps(b.getRect());
    }
}
