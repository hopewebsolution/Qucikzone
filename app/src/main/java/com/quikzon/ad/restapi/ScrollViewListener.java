package com.quikzon.ad.restapi;

import com.quikzon.ad.helper.ScrollViewExt;

public interface ScrollViewListener {
    void onScrollChanged(ScrollViewExt scrollView,
                         int x, int y, int oldx, int oldy);
}
