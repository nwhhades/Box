package com.github.nwhhades.player.inf;

import android.content.Context;

public interface IPlayerFactory<T extends IMediaPlayer> {

    T getPlayer(final Context context);

}
