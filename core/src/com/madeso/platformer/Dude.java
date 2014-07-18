package com.madeso.platformer;

import com.badlogic.gdx.utils.Disposable;

public class Dude implements Disposable {
    private final PlatformGame game;

    public Dude(PlatformGame game) {
        this.game = game;
    }

    @Override
    public void dispose() {

    }
}
