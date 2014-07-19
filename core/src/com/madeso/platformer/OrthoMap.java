package com.madeso.platformer;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

public class OrthoMap implements SuperAsset {
    private final AssetManager assetManager;
    private final String path;
    private OrthogonalTiledMapRenderer renderer;
    private TiledMap map;

    public OrthoMap(AssetManager assetManager, String path) {
        this.assetManager = assetManager;
        this.path = path;

        this.assetManager.load(path, TiledMap.class);
    }

    @Override
    public void postLoad() {
        this.map = this.assetManager.get(path);
        float unitScale = 1;
        this.renderer = new OrthogonalTiledMapRenderer(map, unitScale);
    }

    @Override
    public void dispose() {
        this.renderer.dispose();
        this.assetManager.unload(path);
    }

    public void render(OrthographicCamera camera) {
        this.renderer.setView(camera);
        this.renderer.render();
    }

    public void move(List<Moveable> moveables) {
        for (Moveable m : moveables) {
            m.applyMovement(this);
        }
    }

    private static float ONE = 0.99f;

    public SweptCollisionUtil.ColResult sweptAABB(float px, float py, float tx, float ty, int w, int h) {

        TiledMapTileLayer col = (TiledMapTileLayer)this.map.getLayers().get("col");
        for(int x=0; x<col.getWidth(); ++x) {
            for(int y=0; y<col.getHeight(); ++y) {
                TiledMapTileLayer.Cell cell = col.getCell(x,y);
                if( cell != null ) {
                    if( cell.getTile() != null ) {
                        SweptCollisionUtil.Box box = new SweptCollisionUtil.Box(px,py, tx, ty, w, h);
                        SweptCollisionUtil.ColResult res  = SweptCollisionUtil.Simple(box, new SweptCollisionUtil.Box(x * col.getTileWidth(), y * col.getTileHeight(), col.getTileWidth(), col.getTileHeight()));
                        if( res.ret < ONE ) {
                            return res;
                        }
                    }
                }
            }
        }

        return new SweptCollisionUtil.ColResult(tx, ty);
    }

    public CollisionData basic(float x, float y, float tx, float ty, float w, float h) {
        Vector2 p = new Vector2(x,y);
        Vector2 d = new Vector2(tx-x,ty-y);
        BasicCollision.Simple(this.map, p, d, w, h);
        return new CollisionData(p.x, p.y);
    }

    public CollisionData stop(float px, float py, float ptx, float pty, int w, int h) {
        SweptCollisionUtil.ColResult res = sweptAABB(px, py, ptx, pty, w, h);
        return new CollisionData(res.x, res.y, res.ret < ONE);
    }

    public CollisionData slide(float px, float py, float ptx, float pty, int w, int h) {
        float fx = px;
        float fy = py;
        float tx = ptx;
        float ty = pty;
        for(int i=0; i<1; ++i) {
            SweptCollisionUtil.ColResult res = sweptAABB(fx,fy, tx, ty, w, h);
            fx = res.x;
            fy = res.y;
            if( res.ret >= ONE ) break;
            else {
                float remainingtime = 1 - res.ret;
                float dotprod = (res.vx * res.normaly + res.vy * res.normalx) * remainingtime;
                float vx = dotprod * res.normaly;
                float vy = dotprod * res.normalx;
                //tx = fx + vx;
                //ty = fy + vy;
            }
        }

        return new CollisionData(fx, fy);
    }
}