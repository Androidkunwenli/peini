package com.jsz.peini.utils;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * 基于Handler的定时器
 *
 * @author huizhe.ju
 **/
public class Ticker extends Handler {

    private final int MSG_TICK = 1;

    private WeakReference<OnTickListener> host;
    private int duration = 3000;
    private boolean isRunning;

    public Ticker(OnTickListener onTickListener) {
        super();
        this.host = new WeakReference<OnTickListener>(onTickListener);
    }

    public Ticker(OnTickListener onTickListener, int duration) {
        this(onTickListener);
        if (duration <= 0) {
            throw new IllegalArgumentException("duration should be more than zero");
        }
        this.duration = duration;
    }

    @Override
    public void handleMessage(Message msg) {
        if (msg.what == MSG_TICK) {
            if (isRunning) {
                sendEmptyMessageDelayed(MSG_TICK, duration);
                OnTickListener listener = host.get();
                if (listener != null) {
                    listener.onTick();
                }
            }
        }
        super.handleMessage(msg);
    }

    public void start() {
        if (!isRunning) {
            isRunning = true;
            sendEmptyMessageDelayed(MSG_TICK, duration);
        }
    }

    public void stop() {
        if (isRunning) {
            isRunning = false;
            removeMessages(MSG_TICK);
        }
    }

    public interface OnTickListener {
        void onTick();
    }

}
