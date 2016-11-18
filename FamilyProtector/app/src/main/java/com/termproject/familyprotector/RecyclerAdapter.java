package com.termproject.familyprotector;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseObject;

import java.util.List;

/**
 * Created by Mehul on 11/27/2015.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ChildListRowHolder>{
    private List<ParseObject> children;
    private Context context;
    private NotificationDBHelper dbHelper;

    public  RecyclerAdapter(Context context, List<ParseObject> children) {
        this.children = children;
        this.context = context;
        this.dbHelper = new NotificationDBHelper(context);

    }

    public class ChildListRowHolder extends RecyclerView.ViewHolder {

        protected ImageView childIcon;
        protected TextView childNameText;
        protected TextView childNotificationCount;

        public ChildListRowHolder(View view) {
            super(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("alert adapter", "Element " + getLayoutPosition() + " clicked.");

                    ParseObject child = children.get(getLayoutPosition());
                    String childName = child.getString("name");
                    Log.d("alert adapter", childName);
                    Log.v("Database", "updating child notification");
                    dbHelper.updateOpenedChild(childName);
                    Intent intent = new Intent(context,ChildDetailActivity.class).putExtra(Intent.EXTRA_TEXT, childName);
                    context.startActivity(intent);


                }
            });
            this.childIcon = (ImageView) view.findViewById(R.id.child_icon);
            this.childNameText = (TextView) view.findViewById(R.id.child_name_text);
            this.childNotificationCount = (TextView) view.findViewById(R.id.child_notification_value);
        }



    }

    @Override
    public ChildListRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.child_row, null);

        ChildListRowHolder rowView = new ChildListRowHolder(view);
        return rowView;
    }

    @Override
    public void onBindViewHolder(ChildListRowHolder rowViewHolder, int i) {
        ParseObject child = children.get(i);
        String parseChildName = child.getString("name");

        rowViewHolder.childNameText.setText(parseChildName);
        String notificationCount = String.valueOf(dbHelper.getNotificationCountForChild(parseChildName));

        Log.v("NotCount",notificationCount);
        rowViewHolder.childNotificationCount.setText(notificationCount);
        if(Integer.parseInt(notificationCount)>0) {
            rowViewHolder.childNotificationCount.setVisibility(View.VISIBLE);
        }
        if (child.getString("gender").equals("Female")){
            rowViewHolder.childIcon.setImageResource(R.drawable.child_girl_icon);
        }
        else{
            rowViewHolder.childIcon.setImageResource(R.drawable.child_boy_icon);

        }

        rowViewHolder.childNameText.setTag(rowViewHolder);
        rowViewHolder.childIcon.setTag(rowViewHolder);
//        rowViewHolder.childNotificationCount.setTag(rowViewHolder);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChildListRowHolder holder = (ChildListRowHolder) view.getTag();
                int position = holder.getPosition();

                ParseObject child = children.get(position);
                String childName = child.getString("name");
                Log.v("Database", "updating child notification");
                dbHelper.updateOpenedChild(childName);
                Intent intent = new Intent(context,ChildDetailActivity.class).putExtra(Intent.EXTRA_TEXT, childName);
                context.startActivity(intent);
            }
        };

        //Handle click event on both title and image click
        rowViewHolder.childNameText.setOnClickListener(clickListener);
        rowViewHolder.childIcon.setOnClickListener(clickListener);
//        rowViewHolder.childNotificationCount.setOnClickListener(clickListener);






    }

    @Override
    public int getItemCount() {
        return (null != children ? children.size() : 0);
    }


}
