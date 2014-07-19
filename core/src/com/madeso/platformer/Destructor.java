package com.madeso.platformer;


import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;
import java.util.List;

public class Destructor implements Disposable {
    List<Disposable> disposables = new ArrayList<Disposable>();
    public Destructor add(Disposable d) {
        disposables.add(d);
        return this;
    }

    @Override
    public void dispose() {
        for(Disposable d : disposables) {
            d.dispose();
        }
    }
}
