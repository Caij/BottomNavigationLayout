package com.caij.nav;

import android.graphics.drawable.Drawable;

/**
 * Created by Ca1j on 2017/1/13.
 */

public class NavigationItem {

    public static int TYPE_DRAWABLE = 1;
    public static int TYPE_RES = 2;
    public static int TYPE_URL = 3;

    public Drawable selectIcon;
    public Drawable icon;

    public int drawableTintColor = -1;
    public int selectDrawableTintColor = -1;

    public String selectUrl;
    public String url;

    public String title;

    public final int type;

    public int textColor = -1;
    public int selectTextColor = -1;

    public ImageLoader imageLoader;

    public NavigationItem(String title, Drawable icon, Drawable selectIcon) {
        this.icon = icon;
        this.title = title;
        this.selectIcon = selectIcon;
        type = TYPE_DRAWABLE;
    }

    public NavigationItem(String title, Drawable icon, int activeDrawableColor, int inactiveDrawableColor) {
        this.icon = icon;
        this.title = title;
        this.selectDrawableTintColor = activeDrawableColor;
        this.drawableTintColor = inactiveDrawableColor;
        type = TYPE_RES;
    }

    public NavigationItem(String title, String url, String selectUrl, ImageLoader imageLoader) {
        this.title = title;
        this.selectUrl = url;
        this.url = selectUrl;
        this.imageLoader = imageLoader;
        type = TYPE_URL;
    }

    public NavigationItem setInactiveColor(int inactiveColor) {
        this.drawableTintColor = inactiveColor;
        return this;
    }

    public NavigationItem setTextColor(int textColor) {
        this.textColor = textColor;
        return this;
    }

    public NavigationItem setSelectTextColor(int selectTextColor) {
        this.selectTextColor = selectTextColor;
        return this;
    }
}
