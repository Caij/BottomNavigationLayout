package com.caij.nav;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.view.ViewPropertyAnimatorUpdateListener;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;

import com.caij.nav.behaviour.BottomVerticalScrollBehavior;

import java.util.ArrayList;

/**
 * Created by Ca1j on 2017/1/12.
 */

@CoordinatorLayout.DefaultBehavior(BottomVerticalScrollBehavior.class)
public class BottomNavigationLayout extends LinearLayout {

    private static final Interpolator INTERPOLATOR = new LinearOutSlowInInterpolator();

    private ArrayList<NavigationItem> mBottomNavigationItems;
    private int mSelectPosition;
    private OnTabSelectedListener mTabSelectedListener;

    private int tabItemBadgeTextSize;
    private int tabItemTextSize;
    private int tabItemBadgeColor;
    private boolean isShowText;

    public BottomNavigationLayout(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public BottomNavigationLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public BottomNavigationLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BottomNavigationLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes){
//        ViewCompat.setElevation(this, getResources().getDimension(R.dimen.bottom_navigation_elevation));
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BottomNavigationLayout, defStyleAttr, defStyleRes);

        tabItemBadgeTextSize = typedArray.getDimensionPixelOffset(R.styleable.BottomNavigationLayout_tabItemBadgeTextSize, getResources().getDimensionPixelOffset(R.dimen.nav_item_badge_text_size));
        tabItemTextSize = typedArray.getDimensionPixelOffset(R.styleable.BottomNavigationLayout_tabItemTextSize, getResources().getDimensionPixelOffset(R.dimen.nav_item_label_text_size));
        tabItemBadgeColor = typedArray.getColor(R.styleable.BottomNavigationLayout_tabItemBadgeColor, getResources().getColor(R.color.default_badge_color));
        isShowText = typedArray.getBoolean(R.styleable.BottomNavigationLayout_isShowText, true);

        typedArray.recycle();
    }

    public BottomNavigationLayout addItem(NavigationItem item) {
        if (mBottomNavigationItems == null) mBottomNavigationItems = new ArrayList<>();
        mBottomNavigationItems.add(item);
        return this;
    }

    public void initialise() {
        for (NavigationItem tabItem : mBottomNavigationItems){
            NavigationItemView navigationItemView = new NavigationItemView(getContext());

            navigationItemView.getLabelTextView().setTextSize(TypedValue.COMPLEX_UNIT_PX, tabItemTextSize);
            navigationItemView.getTvBadge().setTextSize(TypedValue.COMPLEX_UNIT_PX, tabItemBadgeTextSize);

            navigationItemView.getTvBadge().setBackgroundColor(tabItemBadgeColor);

            if (isShowText) {
                navigationItemView.getLabelTextView().setVisibility(VISIBLE);
            }else {
                navigationItemView.getLabelTextView().setVisibility(GONE);
            }

            setUp(navigationItemView, tabItem);
            selectTab(0, false);
        }
    }

    private void setUp(final NavigationItemView navigationItemView, final NavigationItem tabItem) {
        navigationItemView.initialise(tabItem);
        LinearLayout.LayoutParams layoutParams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.weight = 1;
        addView(navigationItemView, layoutParams);

        navigationItemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTabInternal(mBottomNavigationItems.indexOf(tabItem), true);
            }
        });
    }

    private void selectTabInternal(int newPosition, boolean callListener) {
        int oldPosition = mSelectPosition;

        NavigationItemView navigationItemView;
        if (oldPosition != -1) {
            navigationItemView = (NavigationItemView) getChildAt(oldPosition);
            navigationItemView.unSelect();
        }

        navigationItemView = (NavigationItemView) getChildAt(newPosition);
        navigationItemView.select();

        mSelectPosition = newPosition;

        if (callListener) {
            sendListenerCall(oldPosition, newPosition);
        }
    }

    private void sendListenerCall(int oldPosition, int newPosition) {
        if (mTabSelectedListener != null) {
            if (oldPosition == newPosition) {
                mTabSelectedListener.onTabReselected(newPosition);
            } else {
                mTabSelectedListener.onTabSelected(newPosition);
                if (oldPosition != -1) {
                    mTabSelectedListener.onTabUnselected(oldPosition);
                }
            }
        }
    }

    /**
     * Should be called only after initialization of BottomBar(i.e after calling initialize method)
     *
     * @param newPosition to select a tab after bottom navigation bar is initialised
     */
    public void selectTab(int newPosition) {
        selectTab(newPosition, true);
    }

    /**
     * Should be called only after initialization of BottomBar(i.e after calling initialize method)
     *
     * @param newPosition  to select a tab after bottom navigation bar is initialised
     * @param callListener should this change call listener callbacks
     */
    public void selectTab(int newPosition, boolean callListener) {
        selectTabInternal(newPosition, callListener);
    }

    public void setTabSelectedListener(OnTabSelectedListener onTabSelectedListener) {
        mTabSelectedListener = onTabSelectedListener;
    }

    public NavigationItemView getTab(int position) {
        return (NavigationItemView) getChildAt(position);
    }

    public interface OnTabSelectedListener {

        /**
         * Called when a tab enters the selected state.
         *
         * @param position The position of the tab that was selected
         */
        void onTabSelected(int position);

        /**
         * Called when a tab exits the selected state.
         *
         * @param position The position of the tab that was unselected
         */
        void onTabUnselected(int position);

        /**
         * Called when a tab that is already selected is chosen again by the user. Some applications
         * may use this action to return to the top level of a category.
         *
         * @param position The position of the tab that was reselected.
         */
        void onTabReselected(int position);
    }

}
