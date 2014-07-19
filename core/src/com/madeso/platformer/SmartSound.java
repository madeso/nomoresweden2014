package com.madeso.platformer;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;

public class SmartSound  implements SuperAsset {
    private final AssetManager assetManager;
    private final String name;
    public Sound sound;

    public SmartSound(AssetManager assetManager, String name) {
        this.assetManager = assetManager;
        this.name = name;
        this.assetManager.load(name, Sound.class);
    }

    @Override
    public void postLoad() {
        this.sound = assetManager.get(name);
    }

    @Override
    public void dispose() {
        assetManager.unload(name);
    }

    public void play() {
        this.sound.stop();
        this.sound.play();
    }
}

