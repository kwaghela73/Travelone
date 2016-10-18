package com.demons.travelone;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.demons.travelone.Pojo.User;
import com.demons.travelone.Pojo.UserBasicInfo;
import com.demons.travelone.Pojo.UserLocation;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kinjal on 27/8/16.
 */



public class BuildProfile extends AppCompatActivity {
    @BindView(R.id.tvuname) TextView username;
    @BindView(R.id.tvemail) TextView email;
    @BindView(R.id.username) EditText e_name;
    @BindView(R.id.email) EditText e_mail;
    @BindView(R.id.contact) EditText contact;
    @BindView(R.id.city) EditText city;
    @BindView(R.id.state) EditText state;
    @BindView(R.id.country) EditText country;
    @BindView(R.id.selectgender) RadioGroup gender;
    @BindView(R.id.main_toolbar) Toolbar toolbar;
    @BindView(R.id.fab) FloatingActionButton done;
    @BindView(R.id.coordinatorLayout) CoordinatorLayout cl;
    private FirebaseAuth auth;
    private String sname;
    private String semail;
    private String scontact;
    private String sgender;
    private String scity;
    private String sstate;
    private String scountry;
    private DatabaseReference mBasicRef;
    private DatabaseReference mLocRef;
    private String uid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.build_profile);
        ButterKnife.bind(this);

        UserBasicInfo ubi = new UserBasicInfo();
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavUtils.navigateUpFromSameTask(BuildProfile.this);
            }
        });
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        auth = FirebaseAuth.getInstance();
        sname   =   auth.getCurrentUser().getDisplayName();
        semail  =   auth.getCurrentUser().getEmail();
        uid     =   auth.getCurrentUser().getUid();
        username.setText(sname);
        email.setText(semail);
        e_name.setText(sname);
        e_name.setEnabled(false);
        e_mail.setText(semail);
        e_mail.setEnabled(false);
        toolbar.setTitle(sname);


        mBasicRef = FirebaseDatabase.getInstance().getReference().child("userbasicinfo").child(uid);
        mLocRef = FirebaseDatabase.getInstance().getReference().child("userlocation").child(uid);



        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get selected radio button from radioGroup
                int selectedId = gender.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                RadioButton male_female = (RadioButton) findViewById(selectedId);
                sgender = male_female.getText().toString();
                scontact = contact.getText().toString();
                scity = city.getText().toString();
                sstate = state.getText().toString();
                scountry = country.getText().toString();
                setUserBasicInfo(uid);
                Snackbar snackbar = Snackbar
                        .make(cl, "Your Data is Updated!", Snackbar.LENGTH_SHORT);

                snackbar.show();

            }
        });

    }

    private void setUserBasicInfo(final String uid) {
        FirebaseDatabase.getInstance().getReference().child("users").child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Create new comment object
                        UserBasicInfo ubi = new UserBasicInfo(sgender,scontact);
                        UserLocation ul = new UserLocation(scity,sstate,scountry);
                        // Push the comment, it will appear in the list
                        mBasicRef.setValue(ubi);
                        mLocRef.setValue(ul);
                        // Clear the field
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();

        mLocRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
try{
    UserLocation userLoc = dataSnapshot.getValue(UserLocation.class);
    city.setText(userLoc.getCity());
    country.setText(userLoc.getCountry());
    state.setText(userLoc.getState());

}catch (NullPointerException e){
    Snackbar snackbar = Snackbar
            .make(cl, e.toString(), Snackbar.LENGTH_SHORT);

    snackbar.show();
}
                mBasicRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        UserBasicInfo userbasic =dataSnapshot.getValue(UserBasicInfo.class);
                        contact.setText(userbasic.getContact());
                        sgender = userbasic.getGender();
                        if(sgender.equals("Female")){
                            gender.check(R.id.female);
                        }
                        else {
                            gender.check(R.id.male);
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}




