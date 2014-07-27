package com.fatsoon.googleplayactionbar;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends Activity {

    @InjectView(R.id.scrollview)
    ScrollView scrollView;
    @InjectView(R.id.layout_bottom)
    RelativeLayout layoutBottom;

    Drawable actionBarBgDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);

        //The default background alpha.
        actionBarBgDrawable = getResources().getDrawable(R.drawable.action_bar_color);
        actionBarBgDrawable.setAlpha(0);
        getActionBar().setBackgroundDrawable(actionBarBgDrawable);

        // The default title textColor alpha.
        int actionBarTitleId = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
        if (actionBarTitleId > 0) {
            TextView title = (TextView) findViewById(actionBarTitleId);
            if (title != null) {
                int color = Color.argb(0, 255, 255, 255);
                title.setTextColor(color);
            }
        }


        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        initScrollView();
    }

    private void initScrollView() {
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                changeActionBarBackgroundAlpha();
                changeActionBarTitleAlpha();
            }
        });
    }

    /**
     * Get the current slide in multiples.
     */
    private float getScrollMultiple() {
        int scrollY = scrollView.getScrollY();
        layoutBottom.setY(-scrollY / 2);
        float layoutBottomHeight = getResources().getDimension(R.dimen.bottom_layout_height);
        float multiple = (float) scrollY / layoutBottomHeight;
        multiple = multiple > 1 ? 1 : multiple;
        multiple = multiple < 0 ? 0 : multiple;
        return multiple;
    }


    private void changeActionBarTitleAlpha() {
        int actionBarTitleId = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
        if (actionBarTitleId > 0) {
            TextView title = (TextView) findViewById(actionBarTitleId);
            if (title != null) {
                int color = Color.argb((int) (255 * getScrollMultiple()), 255, 255, 255);
                title.setTextColor(color);
            }
        }
    }

    private void changeActionBarBackgroundAlpha() {
        actionBarBgDrawable.setAlpha((int) (255 * getScrollMultiple()));
        getActionBar().setBackgroundDrawable(actionBarBgDrawable);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
