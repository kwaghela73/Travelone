package com.demons.travelone;

import android.util.Log;
import android.widget.Toast;

import com.demons.travelone.Pojo.Post;
import com.demons.travelone.Pojo.Trip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kinjal on 30/8/16.
 */
public class MakeTrip {
    DatabaseReference mDatabase;
    private FirebaseAuth auth;

    public void makeanewtrip(String uname,String to,String from,String medium, String description){
        auth = FirebaseAuth.getInstance();
        String uid = auth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        String key = mDatabase.child("trips").push().getKey();
        Trip trip = new Trip(uname,to,from,medium,description);
        Map<String, Object> postValues = trip.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/trips/" + key, postValues);
        childUpdates.put("/user-trips/"+uid+"/" + key,postValues);

        mDatabase.updateChildren(childUpdates);

    }
    private void writeNewPost(String userId, String username, String title, String body) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        String key = mDatabase.child("posts").push().getKey();
        Post post = new Post(userId, username, title, body);
        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/posts/" + key, postValues);
        childUpdates.put("/user-posts/" + userId + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates);
    }

}
