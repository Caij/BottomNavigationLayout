package com.caij.bottomnavigationlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.caij.nav.BottomNavigationLayout;
import com.caij.nav.NavigationItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationLayout bottomNavigationLayout = (BottomNavigationLayout) findViewById(R.id.bottom_nav_layout);
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

            }
        });
    }

    public static int fetchContextColor(Context context, int androidAttribute) {
        TypedValue typedValue = new TypedValue();

        TypedArray a = context.obtainStyledAttributes(typedValue.data, new int[]{androidAttribute});
        int color = a.getColor(0, 0);

        a.recycle();

        return color;
    }
}
