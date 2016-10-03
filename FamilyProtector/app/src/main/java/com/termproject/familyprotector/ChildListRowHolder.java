package com.termproject.familyprotector;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Mehul on 11/27/2015.
 */
public class ChildListRowHolder extends RecyclerView.ViewHolder{

    protected ImageView childIcon;
    protected TextView childNameText;

    public ChildListRowHolder(View view) {
        super(view);
        this.childIcon = (ImageView) view.findViewById(R.id.child_icon);
        this.childNameText = (TextView) view.findViewById(R.id.child_name_text);
    }


}
