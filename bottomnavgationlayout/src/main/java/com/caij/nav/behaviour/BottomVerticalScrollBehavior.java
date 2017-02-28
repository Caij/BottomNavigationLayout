package com.caij.nav.behaviour;


import android.os.Parcel;
import android.os.Parcelable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.os.ParcelableCompat;
import android.support.v4.os.ParcelableCompatCreatorCallbacks;
import android.support.v4.view.AbsSavedState;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;


import com.caij.nav.BottomNavigationLayout;

import java.util.List;

/**
 * Class description
 *
 * @author ashokvarma
 * @version 1.0
 * @see VerticalScrollingBehavior
 * @since 25 Mar 2016
 */
public class BottomVerticalScrollBehavior extends VerticalScrollingBehavior<BottomNavigationLayout> {
    private static final Interpolator INTERPOLATOR = new FastOutSlowInInterpolator();
    private static final String TAG = "BottomBehavior";
    private Boolean bottomNavigationLayoutIsHidden;

    ///////////////////////////////////////////////////////////////////////////
    // onBottomBar changes
    ///////////////////////////////////////////////////////////////////////////
    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, final BottomNavigationLayout child, int layoutDirection) {
        // First let the parent lay it out
        parent.onLayoutChild(child, layoutDirection);

        if (bottomNavigationLayoutIsHidden != null) {
            if (bottomNavigationLayoutIsHidden) {
                if (!child.isHidden()) child.hide(false);
            }else {
                if (child.isHidden()) child.show(false);
            }

            // on restore once
            bottomNavigationLayoutIsHidden = null;
        }

        updateSnackBarPosition(parent, child, getSnackBarInstance(parent, child));

        return super.onLayoutChild(parent, child, layoutDirection);
    }

    ///////////////////////////////////////////////////////////////////////////
    // SnackBar Handling
    ///////////////////////////////////////////////////////////////////////////
    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, BottomNavigationLayout child, View dependency) {
        return isDependent(dependency) || super.layoutDependsOn(parent, child, dependency);
    }

    private boolean isDependent(View dependency) {
        return dependency instanceof Snackbar.SnackbarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, BottomNavigationLayout child, View dependency) {
        if (isDependent(dependency)) {
            updateSnackBarPosition(parent, child, dependency);
            return false;
        }

        return super.onDependentViewChanged(parent, child, dependency);
    }

    private void updateSnackBarPosition(CoordinatorLayout parent, BottomNavigationLayout child, View dependency) {
        updateSnackBarPosition(parent, child, dependency, ViewCompat.getTranslationY(child) - child.getHeight());
    }

    private void updateSnackBarPosition(CoordinatorLayout parent, BottomNavigationLayout child, View dependency, float translationY) {
        if (dependency != null && dependency instanceof Snackbar.SnackbarLayout) {
            ViewCompat.animate(dependency).setInterpolator(INTERPOLATOR).setDuration(80).setStartDelay(0).translationY(translationY).start();
        }
    }

    private Snackbar.SnackbarLayout getSnackBarInstance(CoordinatorLayout parent, BottomNavigationLayout child) {
        final List<View> dependencies = parent.getDependencies(child);
        for (int i = 0, z = dependencies.size(); i < z; i++) {
            final View view = dependencies.get(i);
            if (view instanceof Snackbar.SnackbarLayout) {
                return (Snackbar.SnackbarLayout) view;
            }
        }
        return null;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Auto Hide Handling
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public void onNestedVerticalScrollUnconsumed(CoordinatorLayout coordinatorLayout, BottomNavigationLayout child, @ScrollDirection int scrollDirection, int currentOverScroll) {
        // Empty body
    }

    @Override
    public void onNestedVerticalPreScroll(CoordinatorLayout coordinatorLayout, BottomNavigationLayout child, View target, int dx, int dy, int[] consumed, @ScrollDirection int scrollDirection) {
//        handleDirection(child, scrollDirection);
    }

    @Override
    protected boolean onNestedDirectionFling(CoordinatorLayout coordinatorLayout, BottomNavigationLayout child, View target, float velocityX, float velocityY, boolean consumed, @ScrollDirection int scrollDirection) {
//        if (consumed) {
//            handleDirection(child, scrollDirection);
//        }
        return consumed;
    }

    @Override
    public void onNestedVerticalScrollConsumed(CoordinatorLayout coordinatorLayout, BottomNavigationLayout child, @ScrollDirection int scrollDirection, int currentOverScroll) {
        handleDirection(coordinatorLayout, child, scrollDirection);
    }

    private void handleDirection(CoordinatorLayout parent, BottomNavigationLayout child, int scrollDirection) {
        if (child != null && child.isAutoHideEnabled()) {
            if (scrollDirection == ScrollDirection.SCROLL_DIRECTION_DOWN && child.isHidden()) {
                updateSnackBarPosition(parent, child, getSnackBarInstance(parent, child), - child.getHeight());
                child.show();
            } else if (scrollDirection == ScrollDirection.SCROLL_DIRECTION_UP && !child.isHidden()) {
                updateSnackBarPosition(parent, child, getSnackBarInstance(parent, child), 0);
                child.hide();
            }
        }
    }

    @Override
    public Parcelable onSaveInstanceState(CoordinatorLayout parent, BottomNavigationLayout child) {
        final Parcelable superState = super.onSaveInstanceState(parent, child);
        SavedState savedState = new SavedState(superState);
        savedState.isHidden = child.isHidden();
        return savedState;
    }

    @Override
    public void onRestoreInstanceState(CoordinatorLayout parent, BottomNavigationLayout child, Parcelable state) {
        super.onRestoreInstanceState(parent, child, state);
        if (state instanceof SavedState) {
            SavedState ss = (SavedState) state;
            this.bottomNavigationLayoutIsHidden = ss.isHidden;
        }
    }

    protected static class SavedState extends AbsSavedState {

        boolean isHidden;

        public SavedState(Parcel source, ClassLoader loader) {
            super(source, loader);
            isHidden = source.readByte() != 0;
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeByte((byte) (isHidden ? 1 : 0));
        }

        public static final Parcelable.Creator<SavedState> CREATOR =
                ParcelableCompat.newCreator(new ParcelableCompatCreatorCallbacks<SavedState>() {
                    @Override
                    public SavedState createFromParcel(Parcel source, ClassLoader loader) {
                        return new SavedState(source, loader);
                    }

                    @Override
                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                });
    }
}
