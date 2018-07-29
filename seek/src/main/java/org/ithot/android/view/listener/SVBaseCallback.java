package org.ithot.android.view.listener;


public abstract class SVBaseCallback implements ISVCallback {

    protected abstract int calRange(int _progress);

    public void call(int _progress) {
        int $progress = calRange(_progress);
        step($progress);
    }
}
