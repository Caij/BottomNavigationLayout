package com.caij.bottomnavigationlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.caij.nav.BottomNavigationLayout;
import com.caij.nav.NavigationItem;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final BottomNavigationLayout bottomNavigationLayout = (BottomNavigationLayout) findViewById(R.id.bottom_nav_layout);
        int color = fetchContextColor(this, R.attr.colorPrimary);
        bottomNavigationLayout.addItem(new NavigationItem("首页", getResources().getDrawable(R.mipmap.icon_home)).setActiveColor(color))
                .addItem(new NavigationItem("首页", getResources().getDrawable(R.mipmap.icon_message)).setActiveColor(color))
                .addItem(new NavigationItem("首页", getResources().getDrawable(R.mipmap.icon_me)).setActiveColor(color))
                .initialise();

        TextView tvStatusBadge = bottomNavigationLayout.getTab(0).getTvBadge();
        tvStatusBadge.setVisibility(View.VISIBLE);
        tvStatusBadge.setText("99");

        bottomNavigationLayout.setTabSelectedListener(new BottomNavigationLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {

            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {
                Snackbar snackbar = Snackbar.make(bottomNavigationLayout, "Tips： 单击回到顶部， 双击可直接刷新", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    public static int fetchContextColor(Context context, int androidAttribute) {
        TypedValue typedValue = new TypedValue();

        TypedArray a = context.obtainStyledAttributes(typedValue.data, new int[]{androidAttribute});
        int color = a.getColor(0, 0);

        a.recycle();

        return color;
    }
}
