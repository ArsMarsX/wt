package com.revolve44.windturbine1.ui.dashboard;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.revolve44.windturbine1.R;
import com.revolve44.windturbine1.adapters.DashboardAdapter;
import com.revolve44.windturbine1.models.DashboardItem;


import java.util.ArrayList;

import io.feeeei.circleseekbar.CircleSeekBar;

public class DashboardFragment extends Fragment {
    CircleSeekBar cr;
    TextView vvv;
    private ArrayList<DashboardItem> mExampleList;
    private RecyclerView mRecyclerView;
    private DashboardAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
//    private RecyclerView mRecyclerView;
//    private DashboardAdapter mAdapter;
//    private RecyclerView.LayoutManager mLayoutManager;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        //final TextView textView = root.findViewById(R.id.text_dashboard);
        Log.d("Lifecycle Launch ->>>"," Dashboard Fragment ");


        // get min and max text view
        final TextView tvMin = (TextView) root.findViewById(R.id.textMin1);
        final TextView tvMax = (TextView) root.findViewById(R.id.textMax1);
        vvv = root.findViewById(R.id.count);
        //final CrystalRangeSeekbar rangeSeekbar = (CrystalRangeSeekbar) root.findViewById(R.id.rangeSeekbar1);

        mExampleList = new ArrayList<>();
        mExampleList.add(new DashboardItem(R.drawable.ic_keyboard_arrow_right_black, "My Wind Turbine"));
        mExampleList.add(new DashboardItem(R.drawable.ic_keyboard_arrow_right_black, "Calibration of Power Output"));
        mExampleList.add(new DashboardItem(R.drawable.ic_keyboard_arrow_right_black, "Settings"));
        mExampleList.add(new DashboardItem(R.drawable.ic_keyboard_arrow_right_black, "About App"));

        mRecyclerView = root.findViewById(R.id.recycler);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new DashboardAdapter(mExampleList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);



        //int maxValue=simpleSeekBar.getMax(); // get maximum value of the Seek bar

        return root;
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.d("Lifecycle resume ->>>"," Home Fragment ");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("Lifecycle pause ->>>"," Home Fragment ");
    }
}
