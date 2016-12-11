package com.termproject.familyprotector;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WelcomePageTutorial extends AppCompatActivity
        implements ViewPager.OnPageChangeListener, View.OnClickListener {

    protected View view;
    private ImageButton btnNext;
    private Button btnFinish;
    private TextView textTutorial,textOr;
    private ViewPager intro_images;
    private LinearLayout pager_indicator;
    private int dotsCount;
    private ImageView[] dots;
    private WelcomeTutorialViewPagerAdapter mAdapter;
    UserLocalStore userLocalStore;

    private int[] mImageResources = {
            R.mipmap.tutorial_first_page,
            R.mipmap.tutorial_second_page,
            R.mipmap.tutorial_third_page,
            R.mipmap.tutorial_fourth_page,
            R.mipmap.tutorial_fifth_page
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        userLocalStore = new UserLocalStore(this);
        setReference();
//        toolbar.setVisibility(View.GONE);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        setContentView(R.layout.activity_welcome_page_tutorial);
    }

    public void setReference() {
        ViewGroup container = (ViewGroup) findViewById(android.R.id.content);
        view = LayoutInflater.from(this).inflate(R.layout.activity_welcome_page_tutorial, container);

        intro_images = (ViewPager) view.findViewById(R.id.welcome_pager_introduction);
        btnNext = (ImageButton) view.findViewById(R.id.btn_next);
        //btnSkip = (Button) view.findViewById(R.id.btn_skip);
        btnFinish = (Button) view.findViewById(R.id.btn_finish);
        textTutorial = (TextView)view.findViewById(R.id.text_view_tutorial);
        textOr = (TextView)view.findViewById(R.id.text_view_or);

        pager_indicator = (LinearLayout) view.findViewById(R.id.viewPagerCountDots);

        btnNext.setOnClickListener(this);
        btnFinish.setOnClickListener(this);
        textTutorial.setOnClickListener(this);

        mAdapter = new WelcomeTutorialViewPagerAdapter(WelcomePageTutorial.this, mImageResources);
        intro_images.setAdapter(mAdapter);
        boolean tutorialViewed = userLocalStore.getTutorialViewed();
        if(tutorialViewed){
            intro_images.setCurrentItem(4);
            btnNext.setVisibility(View.GONE);
            pager_indicator.setVisibility(View.GONE);
            btnFinish.setVisibility(View.VISIBLE);
            textTutorial.setVisibility(View.VISIBLE);
            textOr.setVisibility(View.VISIBLE);

        }
        else{
            intro_images.setCurrentItem(0);
        }
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
            case R.id.btn_next:
                intro_images.setCurrentItem((intro_images.getCurrentItem() < dotsCount)
                        ? intro_images.getCurrentItem() + 1 : 0);
                break;

            case R.id.btn_finish:
                userLocalStore.setTutorialViewed(true);
                boolean userRegistered = userLocalStore.getUserRegistered();
                boolean userLoggedIn = userLocalStore.getUserLoggedIn();
                if (userLoggedIn){
                    Intent intent = new Intent(this, ParentHomeScreen.class);
                    startActivity(intent);
                }
                else if(userRegistered){
                    Intent intent = new Intent(this, Login.class);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(this, Register.class);
                    startActivity(intent);
                }
                break;
            case R.id.text_view_tutorial:
//                Toast.makeText(this, "The tutorial link", Toast.LENGTH_LONG).show();
                startActivity(new Intent(this,FamilyProtectorTutorial.class));
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
            pager_indicator.setVisibility(View.GONE);
            btnFinish.setVisibility(View.VISIBLE);
            textTutorial.setVisibility(View.VISIBLE);
            textOr.setVisibility(View.VISIBLE);
        } else {
            btnNext.setVisibility(View.VISIBLE);
            pager_indicator.setVisibility(View.VISIBLE);
            btnFinish.setVisibility(View.GONE);
            textTutorial.setVisibility(View.GONE);
            textOr.setVisibility(View.GONE);
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
