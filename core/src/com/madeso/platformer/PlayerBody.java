package com.madeso.platformer;

public class PlayerBody extends GenericBody {
    private final SmartSound sounddeath;

    public PlayerBody(PlatformGame game) {
        super(game);

        this.dying = game.assetManager.animation(this.destructor, "player.png").setAnimation(0.10f, new int[][]{ {5,0} });
        this.dead = game.assetManager.animation(this.destructor, "player.png").setAnimation(0.10f, new int[][]{ {5,1} });
        this.anim = this.createGroup(this.dying);
        this.sounddeath = game.assetManager.sound(this.destructor, "playerdies.wav");
    }

    public void setup(float x, float y, boolean right, boolean direction) {
        super.setup(x, y, right, direction);
        this.sounddeath.play();
    }

    @Override
    public void subupdate(float dt) {
        super.subupdate(dt);
        if( this.latestFlags.down) {
            GameState.dt = 1;
        }
    }
}
