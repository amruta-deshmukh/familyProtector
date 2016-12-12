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
        private final TextView textViewHeader, textViewDate, textViewTime;

        public ChildAlertViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("alert adapter", "Element " + getPosition() + " clicked.");
                }
            });
            textViewHeader = (TextView) v.findViewById(R.id.child_alert_text_header);
            textViewDate = (TextView) v.findViewById(R.id.child_alert_text_date);
            textViewTime = (TextView) v.findViewById(R.id.child_alert_text_time);
        }

        public TextView getTextViewHeader() {
            return textViewHeader;
        }

        public TextView getTextViewDate() {
            return textViewDate;
        }

        public TextView getTextViewTime() {
            return textViewTime;
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

        if (alertType.equals(FamilyProtectorConstants.ALERT_TYPE_GEOFENCE)) {

            ParseObject alert = mChildAlerts.get(position);
            Date date = alert.getCreatedAt();
            SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
            SimpleDateFormat formatTime = new SimpleDateFormat("hh:mm a");
            final String dateStr = formatter.format(date);
            final String timeStr = formatTime.format(date);
            holder.getTextViewHeader().setText("Alert: " + mChildAlerts.get(position).getString("alert"));
            holder.getTextViewDate().setText("Date: " + dateStr);
            holder.getTextViewTime().setText("Time: " + timeStr);
            holder.getTextViewHeader().setTag(holder);
            holder.getTextViewDate().setTag(holder);
            holder.getTextViewTime().setTag(holder);


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
                    Intent intent = new Intent(context, ChildAlertDetailActivity.class)
                            .putExtra("dateStr", dateStr);
                    intent.putExtra("alertAddress", alert.getString("alertAddress"));
                    intent.putExtra("timeStr", timeStr);
                    intent.putExtra("location", alertLocation);
                    intent.putExtra("objectId", alert.getObjectId());
                    context.startActivity(intent);
                }
            };
            holder.getTextViewHeader().setOnClickListener(clickListener);
            holder.getTextViewDate().setOnClickListener(clickListener);
            holder.getTextViewTime().setOnClickListener(clickListener);
        } else if (alertType.equals(FamilyProtectorConstants.ALERT_TYPE_WEB_HISTORY)) {

            ParseObject alert = mChildAlerts.get(position);
            holder.getTextViewHeader().setText("URL: " + alert.getString("urlName"));
            holder.getTextViewDate().setText("Date: " + alert.getString("visitedDate"));
            holder.getTextViewTime().setText("Time: " + alert.getString("visitedTime"));

            holder.getTextViewHeader().setTag(holder);
            holder.getTextViewDate().setTag(holder);
            holder.getTextViewTime().setTag(holder);


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
                    intent.putExtra("objectId", alert.getObjectId());
                    context.startActivity(intent);
                }
            };

            //Handle click event on both title and image click
            holder.getTextViewHeader().setOnClickListener(clickListener);
            holder.getTextViewDate().setOnClickListener(clickListener);
            holder.getTextViewTime().setOnClickListener(clickListener);
        } else if (alertType.equals(FamilyProtectorConstants.ALERT_TYPE_DEVICE_ADMIN)) {

            ParseObject alert = mChildAlerts.get(position);
            holder.getTextViewHeader().setText("Alert: " + "Tried to uninstall app");
            holder.getTextViewDate().setText("Date: " + alert.getString("alertDate"));
            holder.getTextViewTime().setText("Time: " + alert.getString("alertTime"));

            holder.getTextViewHeader().setTag(holder);
            holder.getTextViewDate().setTag(holder);
            holder.getTextViewTime().setTag(holder);


            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ChildAlertViewHolder rowHolder = (ChildAlertViewHolder) view.getTag();
                    int position = rowHolder.getPosition();

                    ParseObject alert = mChildAlerts.get(position);
                    Intent intent = new Intent(context, ChildDeviceAdminAlertDetailActivity.class);
                    intent.putExtra("alertDate", alert.getString("alertDate"));
                    intent.putExtra("alertTime", alert.getString("alertTime"));
                    intent.putExtra("objectId", alert.getObjectId());
                    context.startActivity(intent);
                }
            };

            //Handle click event on both title and image click
            holder.getTextViewHeader().setOnClickListener(clickListener);
            holder.getTextViewDate().setOnClickListener(clickListener);
            holder.getTextViewTime().setOnClickListener(clickListener);
        } else if (alertType.equals(FamilyProtectorConstants.ALERT_TYPE_CURRENT_LOC)) {

            ParseObject alert = mChildAlerts.get(position);
            holder.getTextViewHeader().setText("Alert: " + "Current Loc unavailable");
            holder.getTextViewDate().setText("Date since: " + alert.getString("dateSinceLastOnline"));
            holder.getTextViewTime().setText("Time since: " + alert.getString("timeSinceLastOnline"));

            holder.getTextViewHeader().setTag(holder);
            holder.getTextViewDate().setTag(holder);
            holder.getTextViewTime().setTag(holder);
            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ChildAlertViewHolder rowHolder = (ChildAlertViewHolder) view.getTag();
                    int position = rowHolder.getPosition();

                    ParseObject alert = mChildAlerts.get(position);
                    Intent intent = new Intent(context, ChildCurrentLocationAlertDetailActivity.class);
                    intent.putExtra("alertDate", alert.getString("dateSinceLastOnline"));
                    intent.putExtra("alertTime", alert.getString("timeSinceLastOnline"));
                    intent.putExtra("objectId", alert.getObjectId());
                    context.startActivity(intent);
                }
            };

            //Handle click event on both title and image click
            holder.getTextViewHeader().setOnClickListener(clickListener);
            holder.getTextViewDate().setOnClickListener(clickListener);
            holder.getTextViewTime().setOnClickListener(clickListener);
        }

    }


    @Override
    public int getItemCount() {
        return mChildAlerts.size();
    }

}
