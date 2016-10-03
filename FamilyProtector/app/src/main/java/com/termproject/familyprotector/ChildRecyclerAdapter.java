package com.termproject.familyprotector;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.ParseObject;

import java.util.List;

/**
 * Created by Mehul on 11/29/2015.
 */
public class ChildRecyclerAdapter extends RecyclerView.Adapter<ChildListRowHolder>{
    private List<ParseObject> children;
    private Context context;
    UserLocalStore userLocalStore;

    public ChildRecyclerAdapter(Context context, List<ParseObject> children) {
        this.children = children;
        this.context = context;
        userLocalStore = new UserLocalStore(context);
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

        rowViewHolder.childNameText.setText(child.getString("name"));

        if (child.getString("gender").equals("Female")){
            rowViewHolder.childIcon.setImageResource(R.drawable.child_girl_icon);
        }
        else{
            rowViewHolder.childIcon.setImageResource(R.drawable.child_boy_icon);

        }

        rowViewHolder.childNameText.setTag(rowViewHolder);
        rowViewHolder.childIcon.setTag(rowViewHolder);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChildListRowHolder holder = (ChildListRowHolder) view.getTag();
                int position = holder.getPosition();

                ParseObject child = children.get(position);
                String childName = child.getString("name");
                String gender = child.getString("gender");
                userLocalStore.setChildForThisPhone(childName);
                userLocalStore.setChildForThisPhoneGender(gender);

                //    Toast.makeText(getActivity(),forecast,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context,ChildHomeScreenAfterSetup.class);
                context.startActivity(intent);
            }
        };

        //Handle click event on both title and image click
        rowViewHolder.childNameText.setOnClickListener(clickListener);
        rowViewHolder.childIcon.setOnClickListener(clickListener);



    }

    @Override
    public int getItemCount() {
        return (null != children ? children.size() : 0);
    }

}
