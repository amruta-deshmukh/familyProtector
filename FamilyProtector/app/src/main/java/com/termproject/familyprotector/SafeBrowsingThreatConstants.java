package com.termproject.familyprotector;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mehul on 10/3/2016.
 */
public class SafeBrowsingThreatConstants {
    /**
     * This threat type identifies URLs of pages that are flagged as containing potentially
     * harmful applications.
     */
    public static final int TYPE_POTENTIALLY_HARMFUL_APPLICATION = 4;

    /**
     * This threat type identifies URLs of pages that are flagged as containing social
     * engineering threats.
     */
    public static final int TYPE_SOCIAL_ENGINEERING = 5;

    public static final List<Integer> threatTypes = new ArrayList<Integer>(){{
        add(4);
        add(5);
    }};


}
