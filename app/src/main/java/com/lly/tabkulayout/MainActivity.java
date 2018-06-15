package com.lly.tabkulayout;

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

public class MainActivity extends AppCompatActivity {

    ViewPager vp_viewpager;
    List<View> views = new ArrayList<>();
    TabKuLayout tk_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vp_viewpager = findViewById(R.id.vp_viewpager);
        tk_layout = findViewById(R.id.tk_layout);
        for (int i = 0; i < 5; i++) {
            LinearLayout linearLayout = new LinearLayout(this, null, ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT);
            TextView textView = new TextView(this);
            textView.setText("Item" + i);
            textView.setTextSize(60);
            textView.setGravity(Gravity.CENTER);
            linearLayout.addView(textView);
            views.add(textView);
        }
        tk_layout.setViewpager(vp_viewpager);
        vp_viewpager.setAdapter(new PagerAdapter() {
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
        });
    }

}
