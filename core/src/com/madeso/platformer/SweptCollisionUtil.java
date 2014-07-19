package com.madeso.platformer;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

// stole from http://www.gamedev.net/page/resources/_/technical/game-programming/swept-aabb-collision-detection-and-response-r3084
public class SweptCollisionUtil {
    public static class Box
    {
        public Box(float x, float y, float tx, float ty, float w, float h) {
            this.x = x;
            this.y = y;
            this.vx = tx-x;
            this.vy = ty-y;
            this.w = w;
            this.h = h;
        }

        public Box(float x, float y, float w, float h) {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
            this.vx = 0;
            this.vy = 0;
        }
        // position of top-left corner
        public float x, y;

        // dimensions
        public float w, h;

        // velocity
        public float vx, vy;
    }

    public static ColResult Simple(Box box, Box block) {
        ColResult r = SweptAABB(box, block);
        float collisiontime = r.ret;
        r.x += box.vx * collisiontime;
        r.y += box.vy * collisiontime;

        r.vx = box.vx;
        r.vy = box.vy;
        return r;
    }

    public static class ColResult {
        public float normalx;
        public float normaly;
        public float ret;

        public float x;
        public float y;

        public float vx = 0.0f;
        public float vy = 0.0f;

        public ColResult(float normalx, float normaly, float ret) {
            this.normalx = normalx;
            this.normaly = normaly;
            this.ret = ret;
        }

        public ColResult(float x, float y) {
            this.normalx = 0;
            this.normaly = 0;
            this.x = x;
            this.y = y;
            this.ret = 1;
        }
    }

    public static ColResult SweptAABB(Box b1, Box b2) {
        final float INF = 1000000000000.0f;

        float xInvEntry, yInvEntry;
        float xInvExit, yInvExit;

        // find the distance between the objects on the near and far sides for both x and y
        if (b1.vx > 0.0f)
        {
            xInvEntry = b2.x - (b1.x + b1.w);
            xInvExit = (b2.x + b2.w) - b1.x;
        }
        else
        {
            xInvEntry = (b2.x + b2.w) - b1.x;
            xInvExit = b2.x - (b1.x + b1.w);
        }

        if (b1.vy > 0.0f)
        {
            yInvEntry = b2.y - (b1.y + b1.h);
            yInvExit = (b2.y + b2.h) - b1.y;
        }
        else
        {
            yInvEntry = (b2.y + b2.h) - b1.y;
            yInvExit = b2.y - (b1.y + b1.h);
        }

        // find time of collision and time of leaving for each axis (if statement is to prevent divide by zero)
        float xEntry, yEntry;
        float xExit, yExit;

        if (b1.vx == 0.0f)
        {
            xEntry = -INF;
            xExit = INF;
        }
        else
        {
            xEntry = xInvEntry / b1.vx;
            xExit = xInvExit / b1.vx;
        }

        if (b1.vy == 0.0f)
        {
            yEntry = -INF;
            yExit = INF;
        }
        else
        {
            yEntry = yInvEntry / b1.vy;
            yExit = yInvExit / b1.vy;
        }

        // find the earliest/latest times of collision
        float entryTime = Math.max(xEntry, yEntry);
        float exitTime = Math.min(xExit, yExit);

        // if there was no collision
        if (entryTime > exitTime || xEntry < 0.0f && yEntry < 0.0f || xEntry > 1.0f || yEntry > 1.0f)
        {
            return new ColResult(0.0f, 0.0f, 1.0f);
        }
        else // if there was a collision
        {
            float normalx;
            float normaly;
            // calculate normal of collided surface
            if (xEntry > yEntry)
            {
                if (xInvEntry < 0.0f)
                {
                    normalx = 1.0f;
                    normaly = 0.0f;
                }
                else
                {
                    normalx = -1.0f;
                    normaly = 0.0f;
                }
            }
            else
            {
                if (yInvEntry < 0.0f)
                {
                    normalx = 0.0f;
                    normaly = 1.0f;
                }
                else
                {
                    normalx = 0.0f;
                    normaly = -1.0f;
                }
            }

            // return the time of collision
            return new ColResult(normalx, normaly, entryTime);
        }
    }
}
