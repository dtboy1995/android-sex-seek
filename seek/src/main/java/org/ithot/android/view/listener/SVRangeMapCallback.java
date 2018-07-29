package org.ithot.android.view.listener;


import org.ithot.android.view.SeekView;

public abstract class SVRangeMapCallback extends SVBaseCallback {

    private int max;
    private int min;

    protected SVRangeMapCallback(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public int calRange(int _progress) {
        float unit = (max - min) / SeekView.MAX_PROGRESS;
        return (int) (unit * _progress + min);
    }
}
