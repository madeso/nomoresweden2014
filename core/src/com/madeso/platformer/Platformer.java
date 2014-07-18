package com.madeso.platformer;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class Platformer extends ApplicationAdapter {
    static final int WIDTH  = 1280;
    static final int HEIGHT = 768;
    static final int PIXELS_PER_METER = 10;
    static final float TILEWIDTH = 10;
    private OrthographicCamera cam;

	SpriteBatch batch;
	Texture img;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");

        cam = new OrthographicCamera();
        // cam.position.set(WIDTH / 2, HEIGHT / 2, 0);
	}

	@Override
	public void render () {
        Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        cam.update();
        batch.setProjectionMatrix(cam.combined);
		batch.begin();
		batch.draw(img, 0, 0, TILEWIDTH * 4, TILEWIDTH * 4);
		batch.end();
	}

    @Override
    public void resize(int width, int height)
    {
        Vector3 oldpos = new Vector3(cam.position);
        cam.setToOrtho(false,
                width/PIXELS_PER_METER,
                height/PIXELS_PER_METER);
        cam.translate(oldpos.x-cam.position.x,oldpos.y-cam.position.y);
    }
}
