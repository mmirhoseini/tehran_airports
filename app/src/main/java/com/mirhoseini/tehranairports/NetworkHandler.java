package com.mirhoseini.tehranairports;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * Created by Mohsen on 5/23/15.
 */
public class NetworkHandler {
    private static final String URL1 = "http://79.127.122.134/daily.asp";
    static NetworkHandler instance = null;
    static Context mContext;

    public static NetworkHandler getInstance(Context context) {
        if (instance == null)
            instance = new NetworkHandler();

        return instance;
    }

    public static void loadData(final NetworkListener listener) {
        if (listener != null)
            listener.onStart();

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.get(mContext, URL1, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                String result = new String(responseBody);

                //Convert String to model
                ArrayList<Flight> flights = null;
                try {
                    flights = convertFlights(result);
                } catch (Exception e) {
                    if (listener != null)
                        listener.onDataError(e);
                }

                if (flights != null) {
                    DataHandler.setFlights(flights);
                    if (listener != null)
                        listener.onSuccess();
                } else {
                    if (listener != null)
                        listener.onNullData(new NullPointerException());
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (listener != null)
                    listener.onNetworkError(statusCode, responseBody, error);
            }
        });
    }

    private static ArrayList<Flight> convertFlights(String html) {
        Document doc = Jsoup.parse(html);


        Elements tables = doc.getElementsByTag("table");//.getElementById("content");
        Element dataTable = tables.get(tables.size() - 1);//.getElementsByTag("a");
        Elements rows = dataTable.getElementsByTag("tr");

        ArrayList<Flight> flights = new ArrayList<Flight>();

        for (int i = 1; i < rows.size(); i++) {

            Element columns = rows.get(i);
            Elements pureColumns = columns.getElementsByTag("tr");

            Flight flight = new Flight();

            flight.setAirLine(pureColumns.get(0).toString());

            flights.add(flight);
        }

        return flights;
    }

    void NetworkHandler(Context context) {
        mContext = context;

    }

    interface NetworkListener {
        void onStart();

        void onSuccess();

        void onDataError(Exception error);

        void onNullData(Exception error);

        void onNetworkError(int statusCode, byte[] responseBody, Throwable error);
    }


}
