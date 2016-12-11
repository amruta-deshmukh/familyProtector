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
 * Created by Mehul on 12/17/2015.
 */
public class ChildRuleRecyclerAdapter extends RecyclerView.Adapter<ChildRuleRecyclerAdapter.ChildRuleViewHolder> {
    private List<ParseObject> mChildRules;
    private String mChildName;
    String[] array = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    private Context context;
    private String mRuleType, mwebUpdatedAt;
    private List<String> mChildWebCategories;

    public static class ChildRuleViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewHeader, textViewDate ;

        public ChildRuleViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("rule adapter", "Element " + getPosition() + " clicked.");
                }
            });
            textViewHeader = (TextView) v.findViewById(R.id.child_rule_text_header);
            textViewDate = (TextView)v.findViewById(R.id.child_rule_text_created_on);
        }

        public TextView getTextViewHeader() {
            return textViewHeader;
        }
        public TextView getTextViewDate() {
            return textViewDate;
        }
    }
    public ChildRuleRecyclerAdapter(Context context, List<ParseObject> childRules, String childName, String ruleType,
                                    List<String> childWebCategories, String webUpdatedAt) {
        this.context = context;
        mChildRules = childRules;
        mChildName = childName;
        mRuleType = ruleType;
        mChildWebCategories = childWebCategories;
        mwebUpdatedAt = webUpdatedAt;

    }

    @Override
    public ChildRuleViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.child_rule_row_item, viewGroup, false);

        return new ChildRuleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ChildRuleViewHolder holder, int position) {

        if(mRuleType.equals("loc")) {


            ParseObject rule = mChildRules.get(position);
            Date date = rule.getCreatedAt();
            SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
            final String dateStr = formatter.format(date);
            holder.getTextViewHeader().setText("Rule location :  " + rule.getString("locationName"));
            holder.getTextViewDate().setText  ("Created on     : " + dateStr);

            holder.getTextViewHeader().setTag(holder);
            holder.getTextViewDate().setTag(holder);

            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ChildRuleViewHolder rowHolder = (ChildRuleViewHolder) view.getTag();
                    int position = rowHolder.getPosition();

                    ParseObject rule = mChildRules.get(position);
                    String location = rule.getString("locationName");
                    String locationAddress = rule.getString("locationAddress");
                    Float perimeter = rule.getNumber("locationRadius").floatValue();
                    String perimeterStr = Float.toString(perimeter);
                    String fromTime = rule.getString("ruleFromTime");
                    String toTime = rule.getString("ruleToTime");
                    String[] timearr = fromTime.split(":");
                    boolean fromTimePM =false, toTimePM =false;
//                    if (Integer.parseInt(timearr[1]) < 10) {
//                        timearr[1] = "0" + timearr[1];
////                        fromTime = timearr[0] + ":0" + timearr[1];
//                    }
                    if(Integer.parseInt(timearr[0]) == 12){
                        fromTimePM = true;
                    }else if(Integer.parseInt(timearr[0]) > 12){
                        timearr[0] = String.valueOf(Integer.parseInt(timearr[0]) - 12);
                        fromTimePM = true;
                    } else if(Integer.parseInt(timearr[0]) <=0){
                        timearr[0] = String.valueOf(12);
                    }
                    if(fromTimePM){
                        fromTime = timearr[0]+":"+timearr[1]+ " PM";
                    } else{
                        fromTime = timearr[0]+":"+timearr[1]+ " AM";
                    }

                    //checking for to Time string
                    timearr = toTime.split(":");
//                    if (Integer.parseInt(timearr[1]) < 10) {
//                        timearr[1] = "0" + timearr[1];
//                    }
                    if(Integer.parseInt(timearr[0])==12){
                        toTimePM = true;
                    } else if(Integer.parseInt(timearr[0]) > 12){
                        timearr[0] = String.valueOf(Integer.parseInt(timearr[0]) - 12);
                        toTimePM = true;
                    } else if(Integer.parseInt(timearr[0]) <=0){
                        timearr[0] = String.valueOf(12);
                    }
                    if(toTimePM){
                        toTime = timearr[0]+":"+timearr[1]+ " PM";
                    } else{
                        toTime = timearr[0]+":"+timearr[1]+ " AM";
                    }

                    String days = "";
                    for (String day : array) {
                        if (rule.getString(day) != null) {
                            if (rule.getString(day).matches("Yes")) {
                                if(days.matches("")){
                                    days = day;
                                } else {
                                    days = days + ", " + day;
                                }
                            }

                        }
                    }
                    String timeStr = fromTime + "  to  " + toTime;

                    Intent intent = new Intent(context, ChildRuleDetailActivity.class);
                    intent.putExtra("location", location);
                    intent.putExtra("ruleAddress", locationAddress);
                    intent.putExtra("days", days);
                    intent.putExtra("time", timeStr);
                    intent.putExtra("perimeter", perimeterStr);
                    intent.putExtra("objectId", rule.getObjectId());
                    context.startActivity(intent);
                }
            };

            //Handle click event on both title and image click
            holder.getTextViewHeader().setOnClickListener(clickListener);
            holder.getTextViewDate().setOnClickListener(clickListener);

        }


        if(mRuleType.equals("web")) {

            final String webCategory = mChildWebCategories.get(position);
            holder.getTextViewHeader().setText("Category         :  " + webCategory);
            holder.getTextViewDate().setText  ("Last Updated : "+ mwebUpdatedAt);

            holder.getTextViewHeader().setTag(holder);
            holder.getTextViewDate().setTag(holder);

            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ChildRuleViewHolder rowHolder = (ChildRuleViewHolder) view.getTag();
                    int position = rowHolder.getPosition();


                    Intent intent = new Intent(context, ChildWebRuleDetailActivity.class);
                    intent.putExtra("category", webCategory);
                    intent.putExtra("lastUpdated", mwebUpdatedAt);
                    context.startActivity(intent);
                }
            };

            //Handle click event on both title and image click
            holder.getTextViewHeader().setOnClickListener(clickListener);
            holder.getTextViewDate().setOnClickListener(clickListener);

        }

    }

    @Override
    public int getItemCount() {
        if(mRuleType.equals("loc")) {
            return mChildRules.size();
        }
        else if (mRuleType.equals("web")&& mChildWebCategories!=null){
            return mChildWebCategories.size();
        }
        return -1;
    }
}
