package com.termproject.familyprotector;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;



public class ChildRuleFragment extends Fragment {
    FloatingActionButton addRuleFloatingActionButton;
    private static final int DATASET_COUNT = 10;

    UserLocalStore userLocalStore;
    String childName;
    private RecyclerView mRecyclerView;
    private ChildRuleRecyclerAdapter mAdapter;
    Context context;
    RecyclerView.LayoutManager mLayoutManager;
    protected String[] mDataset;
    private User user;
    Spinner childRuleSpinner;
    private ImageButton fab;
    private View fabaction1;
    private View fabaction2;
    private boolean expanded = false;
    private float offset1;
    private float offset2;
    private static final String TRANSLATION_Y = "translationY";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        userLocalStore = new UserLocalStore(getActivity());
        user = userLocalStore.getLoggedInUser();
        childName = userLocalStore.getChildDetails();
        initDataset();
//        getChildRulesFromParse();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_child_rule, container, false);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.child_rule_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        childRuleSpinner = (Spinner)view.findViewById(R.id.rule_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.child_rule_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        childRuleSpinner.setAdapter(adapter);
        childRuleSpinner.setOnItemSelectedListener(new SpinnerItemSelectedListener());

        buildFab(view);
//        addRuleFloatingActionButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), MapsActivity.class);
//                startActivity(intent);
//
//            }
//        });

        return view;
    }

    private void initDataset() {
        mDataset = new String[DATASET_COUNT];
        for (int i = 0; i < DATASET_COUNT; i++) {
            mDataset[i] = "This is element #" + i;
        }
    }


    private void getChildRulesFromParse() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ChildRuleLocation");
        query.whereEqualTo("userName", user.getUsername());
        query.whereEqualTo("childName", childName);
        query.orderByDescending("createdAt");

        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> childRulesFromParse, ParseException e) {
                if (e == null) {
                    if (childRulesFromParse.size() > 0) {
                        mAdapter = new ChildRuleRecyclerAdapter(getActivity(),childRulesFromParse, childName);
                        // Set CustomAdapter as the adapter for RecyclerView.
                        mRecyclerView.setAdapter(mAdapter);

                    }
                    else {

                    }
                }
                else {
                    Log.e("Child Rule Fragment", "error in fetching"+e.toString());
                }

            }
        });
    }

    public class SpinnerItemSelectedListener implements AdapterView.OnItemSelectedListener{
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            String selected = parent.getItemAtPosition(pos).toString();
            if(selected.equals("Location")){
                getChildRulesFromParse();

            }
            else if (selected.equals("Web Access")){

            }
        }

        public void onNothingSelected(AdapterView parent) {
            // Do nothing.
        }
    }


    private void buildFab(View view) {
        final ViewGroup fabContainer = (ViewGroup) view.findViewById(R.id.fab_container);
        fab = (ImageButton) view.findViewById(R.id.fab);
        fabaction1 = view.findViewById(R.id.fab_action_1);
        fabaction2 = view.findViewById(R.id.fab_action_2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expanded = !expanded;
                if (expanded) {
                    expandFab();
                } else {
                    collapseFab();
                }
            }
        });

        view.findViewById(R.id.fab_action_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChildWebsiteCategorySelection.class);
                startActivity(intent);
            }
        });

        view.findViewById(R.id.fab_action_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                startActivity(intent);
            }
        });

        fabContainer.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                fabContainer.getViewTreeObserver().removeOnPreDrawListener(this);
                offset1 = fab.getY() - fabaction1.getY();
                fabaction1.setTranslationY(offset1);
                offset2 = fab.getY() - fabaction2.getY();
                fabaction2.setTranslationY(offset2);
                return true;
            }
        });
    }

    private void expandFab() {
        fab.setImageResource(R.drawable.animated_plus);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(createExpandAnimator(fabaction1, offset1),
                createExpandAnimator(fabaction2, offset2));
        animatorSet.start();
        animateFab();
    }

    private void collapseFab() {
        fab.setImageResource(R.drawable.animated_minus);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(createCollapseAnimator(fabaction1, offset1),
                createCollapseAnimator(fabaction2, offset2));
        animatorSet.start();
        animateFab();


    }



    private Animator createExpandAnimator(View view, float offset) {
        return ObjectAnimator.ofFloat(view, TRANSLATION_Y, offset, 0)
                .setDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime));
    }

    private Animator createCollapseAnimator(View view, float offset) {
        return ObjectAnimator.ofFloat(view, TRANSLATION_Y, 0, offset)
                .setDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime));
    }


    private void animateFab() {
        Drawable drawable = fab.getDrawable();
        if (drawable instanceof Animatable) {
            ((Animatable) drawable).start();
        }
    }

}
