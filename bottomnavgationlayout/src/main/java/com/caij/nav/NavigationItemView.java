package com.caij.nav;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


public class NavigationItemView extends FrameLayout {

    private ImageView ivIcon;
    private TextView tvLabel;
    private TextView tvBadge;
    private int activeColor;
    private int inActiveColor;

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
    }

    public void unSelect() {
        ivIcon.setSelected(false);
        tvLabel.setSelected(false);
    }

    public void initialise(NavigationItem tabItem) {
        ivIcon.setSelected(false);
        activeColor = tabItem.getActiveColor() != -1 ? tabItem.getActiveColor() : fetchContextColor(getContext(), R.attr.colorAccent);
        inActiveColor = tabItem.getInactiveColor() != -1 ? tabItem.getInactiveColor() : Color.LTGRAY;
        if (tabItem.isInActiveIconAvailable()) {
            StateListDrawable states = new StateListDrawable();
            states.addState(new int[]{android.R.attr.state_selected},
                    tabItem.getActiveIcon());
            states.addState(new int[]{-android.R.attr.state_selected},
                    tabItem.getInactiveIcon());
            states.addState(new int[]{},
                    tabItem.getInactiveIcon());
            ivIcon.setImageDrawable(states);
        } else {
            Drawable drawable = DrawableCompat.wrap(tabItem.getInactiveIcon());
            DrawableCompat.setTintList(drawable, new ColorStateList(
                    new int[][]{
                            new int[]{android.R.attr.state_selected}, //1
                            new int[]{-android.R.attr.state_selected}, //2
                            new int[]{}
                    },
                    new int[]{
                            activeColor, //1
                            inActiveColor, //2
                            inActiveColor //3
                    }
            ));
            ivIcon.setImageDrawable(drawable);
        }
        setText(tabItem.getTitle());
        tvLabel.setTextColor(new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_selected}, //1
                        new int[]{-android.R.attr.state_selected}, //2
                        new int[]{}
                },
                new int[]{
                        activeColor, //1
                        inActiveColor, //2
                        inActiveColor //3
                }
        ));
    }

    public static int fetchContextColor(Context context, int androidAttribute) {
        TypedValue typedValue = new TypedValue();

        TypedArray a = context.obtainStyledAttributes(typedValue.data, new int[]{androidAttribute});
        int color = a.getColor(0, 0);

        a.recycle();

        return color;
    }
}
