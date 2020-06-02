package com.revolve44.windturbine1.ui.console;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerTabStrip;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.revolve44.windturbine1.MainActivity;
import com.revolve44.windturbine1.R;

public class LocationActivity extends AppCompatActivity  {
    private GoogleMap mMap;
    public LatLng lol;
    Marker marker;
    double latitude;
    double longitude;
    public Float NominalPower = 0.0f;

    public String Latitude;
    public String Longitude;

    Boolean check = false;

    LatLng MYLOCATION =  new LatLng (latitude, longitude);

    LinearLayout Loader;

    TextView textView;

    EditText inputnominalpower;

    FragmentPagerAdapter adapterViewPager;


    boolean tempFahrenheit;
    CheckBox checkImperial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        setTitle("My Wind Turbine");

        ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);

        PagerTabStrip pagerTabStrip = (PagerTabStrip) findViewById(R.id.pager_header);
        pagerTabStrip.setDrawFullUnderline(true);
        pagerTabStrip.setTabIndicatorColor(Color.RED);
        pagerTabStrip.setBackgroundColor(Color.WHITE);

        final View touchView = findViewById(R.id.vpPager);
        touchView.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                return true;
            }
        });


    }

    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 2;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    return LocationFragment.newInstance(0, "Page # 1");
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return SettingNominalPowerFragment.newInstance(1, "Page # 2");
//                case 2: // Fragment # 1 - This will show SecondFragment
//                    return SecondFragment.newInstance(2, "Page # 3");
                default:
                    return null;
            }
        }
        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    return "1 step: Location";
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return "2 step: Characteristics";
//                case 2: // Fragment # 1 - This will show SecondFragment
//                    return "Map";
                default:
                    return null;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Lifecycle ->","Maps onResume launch ");
        //LoadData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("Lifecycle ->","Maps onPause launch ");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("Lifecycle ->","Maps onStop launch ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("Lifecycle ->","Maps onDestroy launch ");
    }
}
