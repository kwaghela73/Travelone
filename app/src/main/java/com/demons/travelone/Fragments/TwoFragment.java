package com.demons.travelone.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demons.travelone.Pojo.Trip;
import com.demons.travelone.Pojo.User;
import com.demons.travelone.R;
import com.demons.travelone.TripRegistration;
import com.firebase.client.Firebase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;


public class TwoFragment extends Fragment {


    private FloatingActionButton addTrip;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager recyclerViewLayoutManager;
    private DatabaseReference mRef,listTravel;
    FirebaseAuth auth;
    private String uid;
    private String uname;

    public TwoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
auth = FirebaseAuth.getInstance();
        uid = auth.getCurrentUser().getUid().toString();
        uname = auth.getCurrentUser().getDisplayName().toString();

    }

    @Override
    public void onStart() {
        super.onStart();
        Trip model;

        final FirebaseRecyclerAdapter<Trip,MessageViewHolder> adapter = new
                FirebaseRecyclerAdapter<Trip, MessageViewHolder>(
                        Trip.class,
                        R.layout.lets_travel,
                        MessageViewHolder.class,
                        listTravel
                ) {
                    @Override
                    protected void populateViewHolder(MessageViewHolder viewHolder, Trip model, int position) {
                if(!(model.getUname().equals(uname))) {
                    viewHolder.setUsername(model.getUname());
                    viewHolder.setTo(model.getTo());
                    viewHolder.setFrom(model.getFrom());
                    viewHolder.setmedium(model.getMedium());
                    viewHolder.setDescription(model.getDescription());
                }
                        else{
                    viewHolder.deleteView();
                }
                    }

        };
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mRef = FirebaseDatabase.getInstance().getReference();
        listTravel = mRef.child("trips");
        View travelView = inflater.inflate(R.layout.fragment_two, container, false);

        mRecyclerView = (RecyclerView) travelView.findViewById(R.id.recyclerView);
        recyclerViewLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(recyclerViewLayoutManager);
        addTrip = (FloatingActionButton) travelView.findViewById(R.id.addTrip);

        addTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( getActivity().getApplication() , TripRegistration.class);
                startActivity(intent);
            }
        });

        return travelView;
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder{

        public MessageViewHolder(View itemView) {
            super(itemView);
            Button b = (Button) itemView.findViewById(R.id.request);
            TextView where2where = (TextView) itemView.findViewById(R.id.where2where);
            where2where.setText("to");
        }

        public void setUsername(String name) {
            TextView field = (TextView) itemView.findViewById(R.id.uname);
            field.setText(name);
        }

        public void setTo(String to) {
            TextView field = (TextView) itemView.findViewById(R.id.to);
            field.setText(to);
        }

        public void setFrom(String from) {
            TextView field = (TextView) itemView.findViewById(R.id.from);
            field.setText(from);
        }

        public void setmedium(String medium)
        {
            TextView field = (TextView) itemView.findViewById(R.id.medium);
            field.setText(medium);
        }

        public void setDescription(String desc)
        {
            TextView field = (TextView) itemView.findViewById(R.id.describe);
            field.setText(desc);
        }
        public void deleteView()
        {
            itemView.setVisibility(itemView.INVISIBLE);
        }
    }
}
