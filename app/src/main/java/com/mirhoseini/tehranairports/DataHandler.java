package com.mirhoseini.tehranairports;

import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Mohsen on 5/23/15.
 */
public class DataHandler {

    static ArrayList<Flight> mFlights = null;

    public static ArrayList<Flight> getFlights() {
        return mFlights;
    }

    public static void setFlights(ArrayList<Flight> flights) {
        mFlights = flights;
    }

    public static void loadList(ListView list) {
    }
}
