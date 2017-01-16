package com.caij.nav;

import android.graphics.drawable.Drawable;

/**
 * Created by Ca1j on 2017/1/13.
 */

public class NavigationItem {

    private Drawable mActiveIcon;
    private Drawable mInactiveIcon;
    private int mInactiveColor = -1;
    private int mActiveColor = -1;
    private String mTitle;

    private boolean inActiveIconAvailable = false;

    public NavigationItem(String title, Drawable icon) {
        this.mInactiveIcon = icon;
        this.mTitle = title;
    }

    public NavigationItem setActiveIcon(Drawable inactiveIcon) {
        this.mActiveIcon = inactiveIcon;
        inActiveIconAvailable = true;
        return this;
    }

    public NavigationItem setInactiveColor(int inactiveColor) {
        this.mInactiveColor = inactiveColor;
        return this;
    }

    public Drawable getActiveIcon() {
        return mActiveIcon;
    }

    public Drawable getInactiveIcon() {
        return mInactiveIcon;
    }


    public int getInactiveColor() {
        return mInactiveColor;
    }

    public String getTitle() {
        return mTitle;
    }

    public boolean isInActiveIconAvailable() {
        return inActiveIconAvailable;
    }

    public int getActiveColor() {
        return mActiveColor;
    }

    public NavigationItem setActiveColor(int mActiveColor) {
        this.mActiveColor = mActiveColor;
        return this;
    }
}
