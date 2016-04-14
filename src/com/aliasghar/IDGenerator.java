package com.aliasghar;

/**
 * Created by Taghizadeh on 14/04/2016.
 */
public class IDGenerator {
    static int current = 0;
    public static int gen(){
        return current++;
    }
}
