package com.revolve44.windturbine1.adapters;

import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.revolve44.windturbine1.ui.console.LocationActivity;
import com.revolve44.windturbine1.R;
import com.revolve44.windturbine1.models.DashboardItem;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.ExampleViewHolder> {
    private ArrayList<DashboardItem> mExampleList;


    public DashboardAdapter(ArrayList<DashboardItem> exampleList) {
        mExampleList = exampleList;
    }
    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_info, parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v);
        return evh;
    }
    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        DashboardItem currentItem = mExampleList.get(position);
        holder.mImageView.setImageResource(currentItem.getImageResource());
        holder.mTextView1.setText(currentItem.getText1());

        //holder.mTextView2.setText(currentItem.getText2());
    }
    public static class ExampleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView mImageView;
        public TextView mTextView1;
        public TextView mTextView2;
        //for clicklistener
        CardView mCard;


        public ExampleViewHolder(final View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageView);
            mTextView1 = itemView.findViewById(R.id.textView);
            //mCard = itemView.findViewById(R.id.XCard);
            //mTextView2 = itemView.findViewById(R.id.textView2);
            //for clicklistener
            // Setup the click listener
            mCard = itemView.findViewById(R.id.XCard);
            mCard.setCardBackgroundColor(Color.TRANSPARENT);
            mCard.setOnClickListener(this);
            mTextView1.setTextSize(18);


        }
        //for clicklistener
        // TODO: now account starting from 0 to 1 , 2 , 3. we are count sections
        @Override
        public void onClick(View view) {
            int a = getAdapterPosition();// get for what a user clicked
            Log.d(TAG, ">>>>> " + a + " ");

            switch (a) {
                case 0:
                    System.out.println("Monday");
                    Intent myIntent = new Intent(view.getContext(), LocationActivity.class);
                    view.getContext().startActivity(myIntent);
                    break;
                case 1:
                    System.out.println("Tuesday");
                    break;
                case 2:
                    System.out.println("Wednesday");

                    break;
                case 3:
                    System.out.println("Thursday");
                    break;
            }

            if ( a==2){ // define which item we clicked
//                Intent myIntent = new Intent(view.getContext(), Unittt.class);
//                view.getContext().startActivity(myIntent);
                Toast.makeText(view.getContext(), "Hello Third Section!!!", Toast.LENGTH_SHORT).show(); // in view.getContext has been problem
            }
        }
    }
    @Override
    public int getItemCount() {
        return mExampleList.size();
    }
}
