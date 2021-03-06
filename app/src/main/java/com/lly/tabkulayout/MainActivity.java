package com.lly.tabkulayout;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lly.mylibrary.TabKuLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    ViewPager vp_viewpager;
    List<View> views = new ArrayList<>();
    TabKuLayout tabKu_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vp_viewpager = findViewById(R.id.vp_viewpager);
        tabKu_layout = findViewById(R.id.tk_layout);
        addView();
        //和TabLayout绑定方式一样
        vp_viewpager.setAdapter(new MyPagerAdapter());
        tabKu_layout.setupWithViewPager(vp_viewpager);
    }


    private void addView() {
        for (int i = 0; i < 8; i++) {
            LinearLayout linearLayout = new LinearLayout(this);
            TextView textView = new TextView(this);
            textView.setText("我是Item" + i);
            textView.setTextSize(12);
            textView.setGravity(Gravity.CENTER);
            textView.setBackgroundColor(Color.GRAY);
            linearLayout.addView(textView);
            views.add(textView);
        }
    }


    private class MyPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ViewGroup parent = (ViewGroup) views.get(position).getParent();
            if (parent != null) {
                parent.removeAllViews();
            }
            container.addView(views.get(position));
            return views.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            int random = new Random().nextInt(10);
            if (position == 0) {
                return "颜色";
            } else if (position == 1) {
                return "颜色渐变1";
            } else if (position == 2) {
                return "颜色渐变2";
            } else if (position == 3) {
                return "颜色渐变3";
            } else if (position == 4) {
                return "颜色渐变4";
            } else {
                return "item" + position;
            }
        }
    }


}
