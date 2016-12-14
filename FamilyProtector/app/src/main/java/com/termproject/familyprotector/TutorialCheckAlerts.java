package com.termproject.familyprotector;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class TutorialCheckAlerts extends AppCompatActivity implements
        ViewPager.OnPageChangeListener, View.OnClickListener{

    protected View view;
    private ViewPager intro_images;
    private ImageButton btnNext;
    private LinearLayout pager_indicator;
    private WelcomeTutorialViewPagerAdapter mAdapter;
    private int dotsCount;
    private ImageView[] dots;

    private int[] mImageResources = {
            R.mipmap.tut_check_alert_1,
            R.mipmap.tut_check_alert_2,
            R.mipmap.tut_check_alert_3

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_tutorial_check_alerts);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
        Toast.makeText(this, "Swipe Left or click on icon to proceed", Toast.LENGTH_SHORT).show();

        setReference();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setReference() {
        ViewGroup container = (ViewGroup) findViewById(android.R.id.content);
        view = LayoutInflater.from(this).inflate(R.layout.activity_tutorial_check_alerts, container);

        intro_images = (ViewPager) view.findViewById(R.id.tut_check_alerts_pager_intro);
        btnNext = (ImageButton) view.findViewById(R.id.tut_check_alerts_btn_next);

        pager_indicator = (LinearLayout) view.findViewById(R.id.tut_check_alerts_viewPagerCountDots);

        btnNext.setOnClickListener(this);

        mAdapter = new WelcomeTutorialViewPagerAdapter(TutorialCheckAlerts.this, mImageResources);
        intro_images.setAdapter(mAdapter);

        intro_images.setCurrentItem(0);
        intro_images.setOnPageChangeListener(this);
        setUiPageViewController();
    }

    private void setUiPageViewController() {

        dotsCount = mAdapter.getCount();
        dots = new ImageView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(4, 0, 4, 0);

            pager_indicator.addView(dots[i], params);
        }

        dots[0].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tut_check_alerts_btn_next:
                intro_images.setCurrentItem((intro_images.getCurrentItem() < dotsCount)
                        ? intro_images.getCurrentItem() + 1 : 0);
                break;

        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < dotsCount; i++) {
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
        }

        dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));

        if (position + 1 == dotsCount) {
            btnNext.setVisibility(View.GONE);
        } else {
            btnNext.setVisibility(View.VISIBLE);
            pager_indicator.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

}
