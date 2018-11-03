package com.quikzon.ad.model;

public class Ad_timer {
    boolean is_show;
    String timer,timer_time,timer_server_time;

    public boolean isIs_show() {
        return is_show;
    }

    public void setIs_show(boolean is_show) {
        this.is_show = is_show;
    }

    public String getTimer() {
        return timer;
    }

    public void setTimer(String timer) {
        this.timer = timer;
    }

    public String getTimer_time() {
        return timer_time;
    }

    public void setTimer_time(String timer_time) {
        this.timer_time = timer_time;
    }

    public String getTimer_server_time() {
        return timer_server_time;
    }

    public void setTimer_server_time(String timer_server_time) {
        this.timer_server_time = timer_server_time;
    }
}
