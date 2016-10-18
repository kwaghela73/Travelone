package com.demons.travelone;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by kinjal on 23/9/16.
 */
public class MySingleton {
    private static MySingleton mInstance;
    private static Context mctx;
    private RequestQueue requestQueue;

    private MySingleton(Context context){
        mctx = context;
        requestQueue = getRequestQueue();

    }

    private RequestQueue getRequestQueue()
    {
        if(requestQueue ==null)
        {
            requestQueue = Volley.newRequestQueue(mctx.getApplicationContext());
        }
        return requestQueue;
    }

    public static synchronized MySingleton getmInstance(Context context)
    {
        if(mInstance==null)
        {
            mInstance = new MySingleton(context);
        }
        return mInstance;
    }

    public<T> void addToRequestque(Request<T> request)
    {
          getRequestQueue().add(request);
    }
}
