package com.github.nwhhades.box.ijk;

import android.content.Context;

import com.github.nwhhades.player.inf.IPlayerFactory;

public class IjkPlayerFactory implements IPlayerFactory<IjkPlayer> {

    @Override
    public IjkPlayer getPlayer(Context context) {
        return new IjkPlayer();
    }

}
