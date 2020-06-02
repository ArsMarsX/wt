package com.revolve44.windturbine1.ui.console;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.revolve44.windturbine1.R;

public class SettingNominalPowerFragment extends Fragment {
    // Store instance variables
    private String title;
    private int page;

    // newInstance constructor for creating fragment with arguments
    public static SettingNominalPowerFragment newInstance(int page, String title) {
        SettingNominalPowerFragment fragmentFirst = new SettingNominalPowerFragment();
        Bundle args = new Bundle();
        args.putInt("someInt2", page);
        args.putString("someTitle2", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt2", 0);
        title = getArguments().getString("someTitle2");
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmentlocation_nominalpower, container, false);
        //TextView tvLabel = (TextView) view.findViewById(R.id.tvLabel2);
        //tvLabel.setText(page + " -- " + title);

        // get seekbar from view
        final CrystalRangeSeekbar rangeSeekbar = (CrystalRangeSeekbar) view.findViewById(R.id.rangeSeekbar1);

        // get min and max text view
        final TextView tvMin = (TextView) view.findViewById(R.id.textMin1);
        final TextView tvMax = (TextView) view.findViewById(R.id.textMax1);
        rangeSeekbar.setMaxValue(30);
        rangeSeekbar.setMinValue(1);

        // set listener
        rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                tvMin.setText("Cut-in wind speed \n"+minValue + " m/s");
                tvMax.setText("Rated power at \n"+maxValue + " m/s");
            }
        });

         // set final value listener
        rangeSeekbar.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {
                Log.d("CRS=>", String.valueOf(minValue) + " : " + String.valueOf(maxValue));
            }
        });
        rangeSeekbar.setMaxStartValue(10);
        rangeSeekbar.setMinStartValue(1);

        return view;
    }

}
