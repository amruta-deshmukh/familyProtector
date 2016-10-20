package com.termproject.familyprotector;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Mehul on 12/3/2015.
 */
public class ChildAlertRecylerAdapter extends RecyclerView.Adapter<ChildAlertRecylerAdapter.ChildAlertViewHolder> {

    private List<ParseObject> mChildAlerts;
    private String mChildName;
    private Context context;
    private String alertType;

    public static class ChildAlertViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        public ChildAlertViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("alert adapter", "Element " + getPosition() + " clicked.");
                }
            });
            textView = (TextView) v.findViewById(R.id.child_alert_textView);
        }

        public TextView getTextView() {
            return textView;
        }
    }

    public ChildAlertRecylerAdapter(Context context, List<ParseObject> childAlerts, String childName, String alertType) {

        this.context = context;
        this.mChildAlerts = childAlerts;
        this.mChildName = childName;
        this.alertType = alertType;

    }


    @Override
    public ChildAlertViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.child_alert_row_item, viewGroup, false);

        return new ChildAlertViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ChildAlertViewHolder holder, int position) {

        if(alertType.equals("loc")) {

            ParseObject alert = mChildAlerts.get(position);
            Date date = alert.getCreatedAt();
            SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
            SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm");
            final String dateStr = formatter.format(date);
            final String timeStr = formatTime.format(date);


            // Get element from your dataset at this position and replace the contents of the view
            // with that element
            holder.getTextView().setText(mChildName + " " + mChildAlerts.get(position).getString("alert") +
                    " on " + dateStr + " at " + timeStr + " hrs.");
            holder.getTextView().setTag(holder);


            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ChildAlertViewHolder rowHolder = (ChildAlertViewHolder) view.getTag();
                    int position = rowHolder.getPosition();

                    ParseObject alert = mChildAlerts.get(position);
                    String alertId = alert.getString("ruleIdStr");
                    String[] alertLocationArr = alert.getString("alert").split(" ");
                    String alertLocation = "";
                    for (int i = 1; i < alertLocationArr.length; i++) {
                        alertLocation = alertLocation + alertLocationArr[i];

                    }
                    Intent intent = new Intent(context, ChildAlertDetailActivity.class).putExtra("dateStr", dateStr);
                    intent.putExtra("alertAddress", alert.getString("alertAddress"));
                    intent.putExtra("timeStr", timeStr);
                    intent.putExtra("location", alertLocation);
                    context.startActivity(intent);
                }
            };

            //Handle click event on both title and image click
            holder.getTextView().setOnClickListener(clickListener);
        }

        else if(alertType.equals("web")) {

            ParseObject alert = mChildAlerts.get(position);


            // Get element from your dataset at this position and replace the contents of the view
            // with that element
            holder.getTextView().setText(mChildName + " visited \"" + alert.getString("urlName") +
                    "\" on " + alert.getString("visitedDate") + " at " + alert.getString("visitedTime"));
            holder.getTextView().setTag(holder);


            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ChildAlertViewHolder rowHolder = (ChildAlertViewHolder) view.getTag();
                    int position = rowHolder.getPosition();

                    ParseObject alert = mChildAlerts.get(position);
                    Intent intent = new Intent(context, ChildWebAlertDetailActivity.class);
                    intent.putExtra("urlName", alert.getString("urlName"));
                    intent.putExtra("categoriesList", alert.getString("categoriesList"));
                    intent.putExtra("visitedDate", alert.getString("visitedDate"));
                    intent.putExtra("visitedTime", alert.getString("visitedTime"));
                    context.startActivity(intent);
                }
            };

            //Handle click event on both title and image click
            holder.getTextView().setOnClickListener(clickListener);
        }

    }


    @Override
    public int getItemCount() {
        return mChildAlerts.size();
    }

}
