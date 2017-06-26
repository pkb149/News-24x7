package com.pkb149.news24x7;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


public class WelcomeActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    private Button btnSkip, btnNext;
    private PrefManager prefManager;
    private SQLiteDatabase mDb;

    public void queryExec(TextView tv, int status){
        Cursor c = mDb.rawQuery("SELECT * FROM sources WHERE source = '"+tv.getText().toString().trim()+"'", null);
        if(c==null) {
            ContentValues cv = new ContentValues();
            cv.put("source", tv.getText().toString().trim());
            cv.put("status", 1);
            mDb.insert("sources", null, cv);
        }
        else{
            String strSQL = "UPDATE sources SET status = 1 WHERE source = '"+ tv.getText().toString().trim()+"'";
            mDb.execSQL(strSQL);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Checking for first time launch - before calling setContentView()
        prefManager = new PrefManager(this);
        if (!prefManager.isFirstTimeLaunch()) {
            launchHomeScreen();
            finish();
        }

        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        setContentView(R.layout.activity_welcome);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        btnSkip = (Button) findViewById(R.id.btn_skip);
        btnNext = (Button) findViewById(R.id.btn_next);

        NewsDBHelper dbHelper = new NewsDBHelper(this);
        mDb = dbHelper.getWritableDatabase();



        // layouts of all welcome sliders
        // add few more layouts if you want
        layouts = new int[]{
                R.layout.welcome_slide0,
                R.layout.welcome_slide1,
                R.layout.welcome_slide2,
                R.layout.welcome_slide3,
                R.layout.welcome_slide4,
                R.layout.welcome_slide5,
                R.layout.welcome_slide6,
                R.layout.welcome_slide7,
                R.layout.welcome_slide8,
                R.layout.welcome_slide9};

        // adding bottom dots
        addBottomDots(0);

        // making notification bar transparent
        changeStatusBarColor();

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchHomeScreen();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking for last page
                // if last page home screen will be launched
                int current = getItem(+1);
                if (current < layouts.length) {
                    // move to next screen
                    viewPager.setCurrentItem(current);
                } else {
                    launchHomeScreen();
                }
            }
        });
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    private void launchHomeScreen() {
        prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
        finish();
    }

    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.length - 1) {
                // last page. make button text to GOT IT
                btnNext.setText(getString(R.string.start));
                btnSkip.setVisibility(View.GONE);
            } else {
                // still pages are left
                btnNext.setText(getString(R.string.next));
                btnSkip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    /**
     * Making notification bar transparent
     */
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * View pager adapter
     */
    public class MyViewPagerAdapter extends PagerAdapter implements View.OnClickListener{
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(final ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);
            if(position==1) {
                final TextView bloomberg = (TextView) view.findViewById(R.id.bloomberg);
                bloomberg.setOnClickListener(this);
                final TextView businessInsider = (TextView) view.findViewById(R.id.business_insider);
                businessInsider.setOnClickListener(this);
                final TextView cnbc = (TextView) view.findViewById(R.id.cnbc);
                cnbc.setOnClickListener(this);
                final TextView businessInsiderUK = (TextView) view.findViewById(R.id.business_insider_uk);
                businessInsiderUK.setOnClickListener(this);
                final TextView financialTimes = (TextView) view.findViewById(R.id.financial_times);
                financialTimes.setOnClickListener(this);
                final TextView fortune = (TextView) view.findViewById(R.id.fortune);
                fortune.setOnClickListener(this);
                final TextView economist = (TextView) view.findViewById(R.id.economist);
                economist.setOnClickListener(this);
                final TextView theWallStreetJournal = (TextView) view.findViewById(R.id.the_wall_street_journal);
                theWallStreetJournal.setOnClickListener(this);
            }
            else if(position==2){
                final TextView buzzfeed = (TextView) view.findViewById(R.id.buzzfeed);
                buzzfeed.setOnClickListener(this);
                final TextView dailyMail = (TextView) view.findViewById(R.id.daily_mail);
                dailyMail.setOnClickListener(this);
                final TextView entertainmentWeekly = (TextView) view.findViewById(R.id.entertainment_weekly);
                entertainmentWeekly.setOnClickListener(this);
                final TextView mashable = (TextView) view.findViewById(R.id.mashable);
                mashable.setOnClickListener(this);
                final TextView theLadBible = (TextView) view.findViewById(R.id.the_lad_bible);
                theLadBible.setOnClickListener(this);
            }

            else if(position==3){
                final TextView ign = (TextView) view.findViewById(R.id.ign);
                ign.setOnClickListener(this);
                final TextView polygon = (TextView) view.findViewById(R.id.polygon);
                polygon.setOnClickListener(this);
            }

            else if(position==4){
                final TextView abcNewsAu = (TextView) view.findViewById(R.id.abc_news_au);
                abcNewsAu.setOnClickListener(this);
                final TextView alJazeeraEnglish = (TextView) view.findViewById(R.id.al_jazeera_english);
                alJazeeraEnglish.setOnClickListener(this);
                final TextView associatedPress = (TextView) view.findViewById(R.id.associated_press);
                associatedPress.setOnClickListener(this);
                final TextView bbcNews = (TextView) view.findViewById(R.id.bbc_news);
                bbcNews.setOnClickListener(this);
                final TextView cnn = (TextView) view.findViewById(R.id.cnn);
                cnn.setOnClickListener(this);
                final TextView googleNews = (TextView) view.findViewById(R.id.google_news);
                googleNews.setOnClickListener(this);
                final TextView independent = (TextView) view.findViewById(R.id.independent);
                independent.setOnClickListener(this);
                final TextView metro = (TextView) view.findViewById(R.id.metro);
                metro.setOnClickListener(this);
                final TextView mirror = (TextView) view.findViewById(R.id.mirror);
                mirror.setOnClickListener(this);
                final TextView newsweek = (TextView) view.findViewById(R.id.newsweek);
                newsweek.setOnClickListener(this);
                final TextView newyork_magazine = (TextView) view.findViewById(R.id.newyork_magazine);
                newyork_magazine.setOnClickListener(this);
                final TextView reddit_r_all = (TextView) view.findViewById(R.id.reddit_r_all);
                reddit_r_all.setOnClickListener(this);
                final TextView reuters = (TextView) view.findViewById(R.id.reuters);
                reuters.setOnClickListener(this);
                final TextView the_guardian_au = (TextView) view.findViewById(R.id.the_guardian_au);
                the_guardian_au.setOnClickListener(this);
                final TextView the_guardian_uk = (TextView) view.findViewById(R.id.the_guardian_uk);
                the_guardian_uk.setOnClickListener(this);
                final TextView the_hindu = (TextView) view.findViewById(R.id.the_hindu);
                the_hindu.setOnClickListener(this);
                final TextView the_newyork_times = (TextView) view.findViewById(R.id.the_newyork_times);
                the_newyork_times.setOnClickListener(this);
                final TextView the_huffington_post = (TextView) view.findViewById(R.id.the_huffington_post);
                the_huffington_post.setOnClickListener(this);
                final TextView the_telegraph = (TextView) view.findViewById(R.id.the_telegraph);
                the_telegraph.setOnClickListener(this);
                final TextView the_times_of_india = (TextView) view.findViewById(R.id.the_times_of_india);
                the_times_of_india.setOnClickListener(this);
                final TextView time = (TextView) view.findViewById(R.id.time);
                time.setOnClickListener(this);
                final TextView the_washington_post = (TextView) view.findViewById(R.id.the_washington_post);
                the_washington_post.setOnClickListener(this);
                final TextView usa_today = (TextView) view.findViewById(R.id.usa_today);
                usa_today.setOnClickListener(this);
            }
            else if(position==4){
                final TextView mtv_news = (TextView) view.findViewById(R.id.mtv_news);
                mtv_news.setOnClickListener(this);
                final TextView mtv_news_uk = (TextView) view.findViewById(R.id.mtv_news_uk);
                mtv_news_uk.setOnClickListener(this);
            }
            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }

        @Override
        public void onClick(View v) {
            TextView tv=(TextView)v;
            if(v.isSelected()){
                tv.setTypeface(null, Typeface.NORMAL);
                tv.setTextSize(15);
                v.setSelected(false);
                Cursor c = mDb.rawQuery("SELECT * FROM sources WHERE source = '"+tv.getText().toString().trim()+"'", null);
                if(c==null) {
                    ContentValues cv = new ContentValues();
                    cv.put("source", tv.getText().toString().trim());
                    cv.put("status", 0);
                    mDb.insert("sources", null, cv);
                }
                else{
                    String strSQL = "UPDATE sources SET status = 0 WHERE source = '"+ tv.getText().toString().trim()+"'";
                    mDb.execSQL(strSQL);
                }
            }
            else
            {
                tv.setTypeface(null, Typeface.BOLD);
                tv.setTextSize(18);
                v.setSelected(true);
                Cursor c = mDb.rawQuery("SELECT * FROM sources WHERE source = '"+tv.getText().toString().trim()+"'", null);
                if(c==null) {
                    ContentValues cv = new ContentValues();
                    cv.put("source", tv.getText().toString().trim());
                    cv.put("status", 1);
                    mDb.insert("sources", null, cv);
                }
                else{
                    String strSQL = "UPDATE sources SET status = 1 WHERE source = '"+ tv.getText().toString().trim()+"'";
                    mDb.execSQL(strSQL);
                }

            }
        }
    }
}