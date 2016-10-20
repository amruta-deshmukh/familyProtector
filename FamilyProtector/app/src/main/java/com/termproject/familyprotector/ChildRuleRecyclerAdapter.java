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

import java.util.List;

/**
 * Created by Mehul on 12/17/2015.
 */
public class ChildRuleRecyclerAdapter extends RecyclerView.Adapter<ChildRuleRecyclerAdapter.ChildRuleViewHolder> {
    private List<ParseObject> mChildRules;
    private String mChildName;
    String[] array = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    private Context context;
    private String mRuleType;
    private List<String> mChildWebCategories;

    public static class ChildRuleViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        public ChildRuleViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("rule adapter", "Element " + getPosition() + " clicked.");
                }
            });
            textView = (TextView) v.findViewById(R.id.child_rule_textView);
        }

        public TextView getTextView() {
            return textView;
        }
    }
    public ChildRuleRecyclerAdapter(Context context, List<ParseObject> childRules, String childName, String ruleType,
                                    List<String> childWebCategories) {
        this.context = context;
        mChildRules = childRules;
        mChildName = childName;
        mRuleType = ruleType;
        mChildWebCategories = childWebCategories;

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
            holder.getTextView().setText("Rule for  :  " + rule.getString("locationName"));

            holder.getTextView().setTag(holder);

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
                    if (Integer.parseInt(timearr[1]) < 10) {
                        fromTime = timearr[0] + ":0" + timearr[1];
                    }
                    timearr = toTime.split(":");
                    if (Integer.parseInt(timearr[1]) < 10) {
                        toTime = timearr[0] + ":0" + timearr[1];
                    }
                    String days = "";
                    for (String day : array) {
                        if (rule.getString(day) != null) {
                            if (rule.getString(day).matches("Yes")) {
                                days = days + ", " + day;
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
                    context.startActivity(intent);
                }
            };

            //Handle click event on both title and image click
            holder.getTextView().setOnClickListener(clickListener);
        }


        if(mRuleType.equals("web")) {

            String webCategory = mChildWebCategories.get(position);
            holder.getTextView().setText("Category  :  " + webCategory);

            holder.getTextView().setTag(holder);

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
