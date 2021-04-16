package com.caij.nav;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import androidx.core.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


public class NavigationItemView extends FrameLayout {

    private ImageView ivIcon;
    private TextView tvLabel;
    private TextView tvBadge;
    private NavigationItem navigationItem;

    public NavigationItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public NavigationItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NavigationItemView(Context context) {
        this(context, null);
    }

    private void init() {
        inflate(getContext(), R.layout.item_navigation_layout, this);
        initView();
    }

    private void initView() {
        ivIcon = (ImageView) findViewById(R.id.tab_item_layout_icon);
        tvLabel = (TextView) findViewById(R.id.tab_item_layout_text);
        tvBadge = (TextView) findViewById(R.id.tv_badge);
    }

    public void setIcon(Drawable drawable) {
        ivIcon.setImageDrawable(drawable);
    }

    public void setText(String s) {
        tvLabel.setText(s);
    }

    public void setTextColor(int color){
        tvLabel.setTextColor(color);
    }

    public TextView getTvBadge() {
        return tvBadge;
    }

    public ImageView getIconImageView() {
        return ivIcon;
    }

    public TextView getLabelTextView() {
        return tvLabel;
    }

    public void select() {
        ivIcon.setSelected(true);
        tvLabel.setSelected(true);
        if (navigationItem != null && navigationItem.type == NavigationItem.TYPE_URL) {
            navigationItem.imageLoader.loadTabImage(ivIcon, navigationItem.selectUrl);
        }
    }

    public void unSelect() {
        ivIcon.setSelected(false);
        tvLabel.setSelected(false);
    }

    public void initialise(NavigationItem tabItem) {
        this.navigationItem = tabItem;
        ivIcon.setSelected(false);
        if (tabItem.type == NavigationItem.TYPE_DRAWABLE) {
            StateListDrawable states = new StateListDrawable();
            states.addState(new int[]{android.R.attr.state_selected},
                    tabItem.selectIcon);
            states.addState(new int[]{-android.R.attr.state_selected},
                    tabItem.icon);
            states.addState(new int[]{},
                    tabItem.icon);
            ivIcon.setImageDrawable(states);
        } else if (tabItem.type == NavigationItem.TYPE_RES) {
            Drawable drawable = DrawableCompat.wrap(tabItem.icon);
            DrawableCompat.setTintList(drawable, new ColorStateList(
                    new int[][]{
                            new int[]{android.R.attr.state_selected}, //1
                            new int[]{-android.R.attr.state_selected}, //2
                            new int[]{}
                    },
                    new int[]{
                            tabItem.selectDrawableTintColor, //1
                            tabItem.drawableTintColor, //2
                            tabItem.drawableTintColor //3
                    }
            ));
            ivIcon.setImageDrawable(drawable);
        } else {
            navigationItem.imageLoader.loadTabImage(ivIcon, tabItem.url);
        }
        setText(tabItem.title);
        tvLabel.setTextColor(new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_selected}, //1
                        new int[]{-android.R.attr.state_selected}, //2
                        new int[]{}
                },
                new int[]{
                        tabItem.selectTextColor, //1
                        tabItem.textColor, //2
                        tabItem.textColor //3
                }
        ));
    }
}
