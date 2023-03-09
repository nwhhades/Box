package com.github.nwhhades.base.view;

import android.app.Activity;
import android.content.DialogInterface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import java.util.List;

public class TvSpinner implements DialogInterface.OnClickListener {

    public interface OnSelectItemListener {
        void onSelectItem(String data, int index);
    }

    private final AlertDialog alertDialog;
    private final String[] strings;
    private OnSelectItemListener onSelectItemListener;

    public TvSpinner(@NonNull Activity activity, @NonNull String title, @NonNull List<String> list, int index) {
        strings = list.toArray(new String[0]);
        alertDialog = new AlertDialog.Builder(activity)
                .setTitle(title)
                .setSingleChoiceItems(strings, index, this)
                .create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
        if (onSelectItemListener != null) {
            onSelectItemListener.onSelectItem(strings[which], which);
        }
    }

    public void show() {
        alertDialog.show();
    }

    public void hide() {
        alertDialog.hide();
    }

    public void destroy() {
        alertDialog.cancel();
        onSelectItemListener = null;
    }

    public void setOnSelectItemListener(OnSelectItemListener onSelectItemListener) {
        this.onSelectItemListener = onSelectItemListener;
    }

}
