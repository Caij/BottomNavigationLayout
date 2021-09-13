package com.caij.nav.behaviour;


import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.os.ParcelableCompat;
import androidx.core.os.ParcelableCompatCreatorCallbacks;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.core.view.ViewPropertyAnimatorUpdateListener;
import androidx.customview.view.AbsSavedState;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;


import com.caij.nav.BottomNavigationLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.snackbar.Snackbar;

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
    private Boolean restoreBottomNavigationLayoutIsHidden;
    private boolean autoHideEnabled = true;
    private int mSnackBarHeight = -1;

    private View snackBar;
    private boolean mIsHidden;
    private ViewPropertyAnimatorCompat mTranslationAnimator;
    private ViewPropertyAnimatorListener showViewPropertyAnimatorListener;
    private ViewPropertyAnimatorListener hideViewPropertyAnimatorListener;

    protected static final int ENTER_ANIMATION_DURATION = 225;

    public BottomVerticalScrollBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BottomVerticalScrollBehavior() {

    }

    ///////////////////////////////////////////////////////////////////////////
    // onBottomBar changes
    ///////////////////////////////////////////////////////////////////////////
    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, final BottomNavigationLayout child, int layoutDirection) {
        // First let the parent lay it out
        parent.onLayoutChild(child, layoutDirection);

        if (restoreBottomNavigationLayoutIsHidden != null) {
            if (restoreBottomNavigationLayoutIsHidden) {
                if (!isHidden()) hide(child, false);
            }else {
                if (isHidden()) show(child, false);
            }

            // on restore once
            restoreBottomNavigationLayoutIsHidden = null;
        }

        updateSnackBarPosition(child, getSnackBarInstance(parent, child));

        return super.onLayoutChild(parent, child, layoutDirection);
    }

    ///////////////////////////////////////////////////////////////////////////
    // SnackBar Handling
    ///////////////////////////////////////////////////////////////////////////
    @Override
    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull BottomNavigationLayout child, @NonNull View dependency) {
        return isDependent(dependency) || super.layoutDependsOn(parent, child, dependency);
    }

    private boolean isDependent(View dependency) {
        return dependency instanceof Snackbar.SnackbarLayout;
    }

    @Override
    public void onDependentViewRemoved(@NonNull CoordinatorLayout parent, @NonNull BottomNavigationLayout child, @NonNull View dependency) {
        if (dependency instanceof Snackbar.SnackbarLayout) {
            snackBar = null;
        }
        super.onDependentViewRemoved(parent, child, dependency);
    }

    @Override
    public boolean onDependentViewChanged(@NonNull CoordinatorLayout parent, @NonNull BottomNavigationLayout child, @NonNull View dependency) {
        if (dependency instanceof Snackbar.SnackbarLayout) {
            snackBar = dependency;
        } else {
            snackBar = null;
        }
        return super.onDependentViewChanged(parent, child, dependency);
    }

    private void updateSnackBarPosition(BottomNavigationLayout child, View dependency) {
        if (dependency instanceof Snackbar.SnackbarLayout) {
            if (mSnackBarHeight == -1) {
                mSnackBarHeight = dependency.getHeight();
            }
            int targetPadding = (int) (child.getHeight() - child.getTranslationY());
            dependency.setPadding(dependency.getPaddingLeft(),
                    dependency.getPaddingTop(), dependency.getPaddingRight(), targetPadding
            );
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
//        handleDirection(coordinatorLayout, child, scrollDirection);
    }

    @Override
    protected boolean onNestedDirectionFling(CoordinatorLayout coordinatorLayout, BottomNavigationLayout child, View target, float velocityX, float velocityY, boolean consumed, @ScrollDirection int scrollDirection) {
//        if (consumed) {
//            handleDirection(coordinatorLayout, child, scrollDirection);
//        }
        return consumed;
    }

    @Override
    public void onNestedVerticalScrollConsumed(CoordinatorLayout coordinatorLayout, BottomNavigationLayout child, @ScrollDirection int scrollDirection, int currentOverScroll) {
        handleDirection(coordinatorLayout, child, scrollDirection);
    }

    private void handleDirection(CoordinatorLayout parent, BottomNavigationLayout child, int scrollDirection) {
        if (child != null && isAutoHideEnabled()) {
            if (scrollDirection == ScrollDirection.SCROLL_DIRECTION_DOWN && isHidden()) {
                show(child);
            } else if (scrollDirection == ScrollDirection.SCROLL_DIRECTION_UP && !isHidden()) {
                hide(child);
            }
        }
    }

    public boolean isAutoHideEnabled() {
        return autoHideEnabled;
    }

    public void setAutoHideEnabled(boolean autoHideEnabled) {
        this.autoHideEnabled = autoHideEnabled;
    }

    public boolean isHidden() {
        return mIsHidden;
    }

    @Override
    public Parcelable onSaveInstanceState(CoordinatorLayout parent, BottomNavigationLayout child) {
        final Parcelable superState = super.onSaveInstanceState(parent, child);
        SavedState savedState = new SavedState(superState);
        savedState.isHidden = isHidden();
        return savedState;
    }

    @Override
    public void onRestoreInstanceState(CoordinatorLayout parent, BottomNavigationLayout child, Parcelable state) {
        super.onRestoreInstanceState(parent, child, state);
        if (state instanceof SavedState) {
            SavedState ss = (SavedState) state;
            this.restoreBottomNavigationLayoutIsHidden = ss.isHidden;
        }
    }

    /**
     * show with animation
     */
    public void show(View view) {
        show(view, true);
    }

    /**
     * @param animate is animation enabled for show
     */
    public void show(View view, boolean animate) {
        mIsHidden = false;
        setTranslationY(view, 0, animate, showViewPropertyAnimatorListener);
    }

    /**
     * hide with animation
     */
    public void hide(View view) {
        hide(view, true);
    }

    /**
     * @param animate is animation enabled for hide
     */
    public void hide(View view, boolean animate) {
        mIsHidden = true;
        setTranslationY(view, view.getHeight(), animate, hideViewPropertyAnimatorListener);
    }

    /**
     * @param offset  offset needs to be set
     * @param animate is animation enabled for translation
     */
    private void setTranslationY(View view, int offset, boolean animate, ViewPropertyAnimatorListener viewPropertyAnimatorListener) {
        if (animate) {
            animateOffset(view, offset, viewPropertyAnimatorListener);
        } else {
            if (mTranslationAnimator != null) {
                mTranslationAnimator.cancel();
            }
            if (viewPropertyAnimatorListener != null) {
                viewPropertyAnimatorListener.onAnimationStart(view);
            }
            view.setTranslationY(offset);
            if (viewPropertyAnimatorListener != null) {
                viewPropertyAnimatorListener.onAnimationEnd(view);
            }
        }
    }

    /**
     * Internal Method
     * <p/>
     * used to set animation and
     * takes care of cancelling current animation
     * and sets duration and interpolator for animation
     *
     * @param offset translation offset that needs to set with animation
     */
    private void animateOffset(View view, final int offset, ViewPropertyAnimatorListener viewPropertyAnimatorListener) {
        if (mTranslationAnimator == null) {
            mTranslationAnimator = ViewCompat.animate(view);
            mTranslationAnimator.setDuration(ENTER_ANIMATION_DURATION);
            mTranslationAnimator.setInterpolator(INTERPOLATOR);
        } else {
            mTranslationAnimator.cancel();
        }

        mTranslationAnimator.setUpdateListener(new ViewPropertyAnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(View view) {
                updateSnackBarPosition((BottomNavigationLayout) view, snackBar);
            }
        });

        if (viewPropertyAnimatorListener != null) {
            mTranslationAnimator.setListener(viewPropertyAnimatorListener);
        }

        mTranslationAnimator.translationY(offset).start();
    }

    public void setShowViewPropertyAnimatorListener(ViewPropertyAnimatorListener showViewPropertyAnimatorListener) {
        this.showViewPropertyAnimatorListener = showViewPropertyAnimatorListener;
    }

    public void setHideViewPropertyAnimatorListener(ViewPropertyAnimatorListener hideViewPropertyAnimatorListener) {
        this.hideViewPropertyAnimatorListener = hideViewPropertyAnimatorListener;
    }

    public static interface Listener {
        void onShow(View view);
        void onHide(View view);
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

    public static BottomVerticalScrollBehavior from(@NonNull View view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (!(params instanceof CoordinatorLayout.LayoutParams)) {
            throw new IllegalArgumentException("The view is not a child of CoordinatorLayout");
        }
        CoordinatorLayout.Behavior<?> behavior =
                ((CoordinatorLayout.LayoutParams) params).getBehavior();
        if (!(behavior instanceof BottomVerticalScrollBehavior)) {
            throw new IllegalArgumentException("The view is not associated with BottomSheetBehavior");
        }
        return (BottomVerticalScrollBehavior) behavior;
    }
}
