package com.madeso.platformer;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public class BasicCollision {
    private static void getTiles (TiledMap map, int startX, int startY, int endX, int endY, Array<Rectangle> tiles) {
        TiledMapTileLayer layer = (TiledMapTileLayer)map.getLayers().get("col");
        rectPool.freeAll(tiles);
        tiles.clear();
        for (int y = startY; y <= endY; y++) {
            for (int x = startX; x <= endX; x++) {
                TiledMapTileLayer.Cell cell = layer.getCell(x, y);
                if (cell != null) {
                    Rectangle rect = rectPool.obtain();
                    rect.set(x, y, 1, 1);
                    tiles.add(rect);
                }
            }
        }
    }


    private static Pool<Rectangle> rectPool = new Pool<Rectangle>() {
        @Override
        protected Rectangle newObject () {
            return new Rectangle();
        }
    };

    private static Array<Rectangle> tiles = new Array<Rectangle>();

    private static void SubSimple(TiledMap map, Vector2 position, Vector2 velocity, float width, float height) {
        boolean grounded = false;
        // perform collision detection & response, on each axis, separately
        // if the koala is moving right, check the tiles to the right of it's
        // right bounding box edge, otherwise check the ones to the left
        Rectangle koalaRect = rectPool.obtain();
        koalaRect.set(position.x, position.y, width, height);
        int startX, startY, endX, endY;
        if (velocity.x > 0) {
            startX = endX = (int)(position.x + width + velocity.x);
        } else {
            startX = endX = (int)(position.x + velocity.x);
        }
        startY = (int)(position.y);
        endY = (int)(position.y + height);
        getTiles(map, startX, startY, endX, endY, tiles);
        koalaRect.x += velocity.x;
        for (Rectangle tile : tiles) {
            if (koalaRect.overlaps(tile)) {
                velocity.x = 0;
                break;
            }
        }
        koalaRect.x = position.x;

        // if the koala is moving upwards, check the tiles to the top of it's
        // top bounding box edge, otherwise check the ones to the bottom
        if (velocity.y > 0) {
            startY = endY = (int)(position.y + height + velocity.y);
        } else {
            startY = endY = (int)(position.y + velocity.y);
        }
        startX = (int)(position.x);
        endX = (int)(position.x + width);
        getTiles(map, startX, startY, endX, endY, tiles);
        koalaRect.y += velocity.y;
        for (Rectangle tile : tiles) {
            if (koalaRect.overlaps(tile)) {
                // we actually reset the koala y-position here
                // so it is just below/above the tile we collided with
                // this removes bouncing :)
                if (velocity.y > 0) {
                    position.y = tile.y - height;
                    // we hit a block jumping upwards, let's destroy it!
                    // TiledMapTileLayer layer = (TiledMapTileLayer)map.getLayers().get(1);
                    // layer.setCell((int)tile.x, (int)tile.y, null);
                } else {
                    position.y = tile.y + tile.height;
                    // if we hit the ground, mark us as grounded so we can jump
                    grounded = true;
                }
                velocity.y = 0;
                break;
            }
        }
        rectPool.free(koalaRect);
    }

    public static void Simple(TiledMap map, Vector2 position, Vector2 velocity, float width, float height) {
        float tw = 64;
        float th = 64;
        position.x /= tw;
        position.y /= th;
        velocity.x /= tw;
        velocity.y /= th;

        SubSimple(map, position, velocity, width/tw, height/th);

        position.x *= tw;
        position.y *= th;
        velocity.x *= tw;
        velocity.y *= th;
    }
}
