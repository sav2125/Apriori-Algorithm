package com.project3;

import java.util.TreeSet;

/**
 * Created by shivani on 11/25/2015.
 */
public class Rule {
    public TreeSet<String> left;
    public TreeSet<String> right;

    public Rule()
    {
        left = new TreeSet<String>();
        right = new TreeSet<String>();
    }
}
