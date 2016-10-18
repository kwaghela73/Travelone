package com.demons.travelone;

import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.demons.travelone.Pojo.Trip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import ernestoyaquello.com.verticalstepperform.VerticalStepperFormLayout;
import ernestoyaquello.com.verticalstepperform.interfaces.VerticalStepperForm;
import mabbas007.tagsedittext.TagsEditText;

/**
 * Created by kinjal on 4/9/16.
 */
public class TripRegistration extends AppCompatActivity implements VerticalStepperForm, DatePickerDialog.OnDateSetListener, TagsEditText.TagsEditListener {

    private static final String TAG = "Hello There";
    private VerticalStepperFormLayout verticalStepperForm;
    private EditText name;
    private Toolbar toolbar;
    private ImageButton dateButton;
    private TextView datetv;
    private RadioButton medium, role;
    private RadioGroup rolegroup, mediumgroup;
    private int seleted;
    private TagsEditText mTagsEditText;
    private EditText desc;
    private MakeTrip m;
    private FirebaseAuth auth;
    private String uid;
    private String tos, froms, med;
    private Trip trip;
    private EditText to, from;
    private String uname;
    private String description;
    SharedPreferences sharedPreferences;
    String token;
    StringRequest stringRequest;
    String app_server_url = "http://travelearn.16mb.com/fcm_insert.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trip_registration);
        toolbar = (Toolbar) findViewById(R.id.toolbartrip);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavUtils.navigateUpFromSameTask(TripRegistration.this);
            }
        });
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        auth = FirebaseAuth.getInstance();
        uid = auth.getCurrentUser().getUid();
        uname = auth.getCurrentUser().getDisplayName();
        m=new MakeTrip();
        String[] mySteps = {"Select Role", "Traveling Medium", "Basic Info", "Subject", "Description"};
        int colorPrimary = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary);
        int colorPrimaryDark = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark);

        sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.FCM_PREF), Context.MODE_PRIVATE);
        token = sharedPreferences.getString(getString(R.string.FCM_TOKEN),"");
        stringRequest = new StringRequest(Request.Method.POST, app_server_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("fcm_token",token);

                return params;
            }
        };
        // Finding the view
        verticalStepperForm = (VerticalStepperFormLayout) findViewById(R.id.vertical_stepper_form);

        // Setting up and initializing the form
        VerticalStepperFormLayout.Builder.newInstance(verticalStepperForm, mySteps, this, this)
                .primaryColor(colorPrimary)
                .primaryDarkColor(colorPrimaryDark)
                .displayBottomNavigation(true) // It is true by default, so in this case this line is not necessary
                .init();


    }

    @Override
    public View createStepContentView(int stepNumber) {
        View view = null;
        switch (stepNumber) {
            case 0:
                view = selectRoleStep();
                break;
            case 1:
                view = selectMediumStep();
                break;
            case 2:
                view = basicInfoStep();
                break;
            case 3:
                view = subjectStep();
                break;
            case 4:
                view = descriptionStep();
                break;
        }
        return view;
    }

    private View selectRoleStep() {
        LayoutInflater inflater = LayoutInflater.from(getBaseContext());
        LinearLayout select_role = (LinearLayout) inflater.inflate(R.layout.select_role, null, false);
        rolegroup = (RadioGroup) select_role.findViewById(R.id.selectrole);
        rolegroup.check(R.id.tutor);
        seleted = rolegroup.getCheckedRadioButtonId();
        role = (RadioButton) findViewById(seleted);
        return select_role;
    }

    private View selectMediumStep() {
        LayoutInflater inflater = LayoutInflater.from(getBaseContext());
        LinearLayout select_medium = (LinearLayout) inflater.inflate(R.layout.select_medium, null, false);
        mediumgroup = (RadioGroup) select_medium.findViewById(R.id.selectmedium);
        mediumgroup.check(R.id.train);
        seleted = mediumgroup.getCheckedRadioButtonId();
        medium = (RadioButton) findViewById(seleted);
        return select_medium;
    }


    private View basicInfoStep() {
        LayoutInflater inflater = LayoutInflater.from(getBaseContext());
        LinearLayout basic_info = (LinearLayout) inflater.inflate(R.layout.asic_info, null, false);
        dateButton = (ImageButton) basic_info.findViewById(R.id.date);
        final Calendar c = Calendar.getInstance();
        final int year = c.get(Calendar.YEAR);
        final int month = c.get(Calendar.MONTH);
        final int day = c.get(Calendar.DATE);
        datetv = (TextView) basic_info.findViewById(R.id.date_text);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(TripRegistration.this, year, month, day);
                datePickerDialog.show(getFragmentManager(), "Select Date");
            }
        });
        from = (EditText) basic_info.findViewById(R.id.from);
        to = (EditText) basic_info.findViewById(R.id.to);
        return basic_info;
    }

    private View subjectStep() {
        LayoutInflater inflater = LayoutInflater.from(getBaseContext());
        LinearLayout select_subject = (LinearLayout) inflater.inflate(R.layout.select_subject, null, false);
        mTagsEditText = (TagsEditText) select_subject.findViewById(R.id.tagsEditText);
        mTagsEditText.setHint("Enter names of fruits");
        mTagsEditText.setTagsListener(this);
        mTagsEditText.setTagsWithSpacesEnabled(true);
        mTagsEditText.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.fruits)));
        mTagsEditText.setThreshold(1);
        return select_subject;
    }

    private View descriptionStep() {
        LayoutInflater inflater = LayoutInflater.from(getBaseContext());
        LinearLayout description = (LinearLayout) inflater.inflate(R.layout.travel_description, null, false);
        desc = (EditText) description.findViewById(R.id.description);
        return description;
    }

    @Override
    public void onStepOpening(int stepNumber) {
        LayoutInflater inflater = LayoutInflater.from(getBaseContext());
        switch (stepNumber) {
            case 0:
                checkRole();
                break;
            case 1:
                LinearLayout select_medium = (LinearLayout) inflater.inflate(R.layout.select_medium, null, false);
                mediumgroup = (RadioGroup) select_medium.findViewById(R.id.selectmedium);
                mediumgroup.check(R.id.train);
                seleted = mediumgroup.getCheckedRadioButtonId();
                medium = (RadioButton) findViewById(seleted);
                med = (String) medium.getText();
                checkMedium();
                break;
            case 2:
                checkInfo();
                // As soon as the phone number step is open, we mark it as completed in order to show the "Continue"
                // button (We do it because this field is optional, so the user can skip it without giving any info)
                //verticalStepperForm.setStepAsCompleted(2);
                // In this case, the instruction above is equivalent to:
                // verticalStepperForm.setActiveStepAsCompleted();
                break;
            case 3:
                checkSub();
                break;
            case 4:
                checkDescription();
                break;
        }
    }

    private void checkSub() {
        verticalStepperForm.setActiveStepAsCompleted();
    }

    private void checkDescription() {
        if(desc.getText().toString().equals(null))
        {
            String errorMessage = "enter mendetory text";
            verticalStepperForm.setActiveStepAsUncompleted(errorMessage);
        }
        else {
            verticalStepperForm.setActiveStepAsCompleted();
        }
    }

    private void checkRole() {

        if (seleted == -1) {
            String errorMessage = "Select atleast one role.";
            verticalStepperForm.setActiveStepAsUncompleted(errorMessage);
        } else {
            verticalStepperForm.setActiveStepAsCompleted();
        }
    }

    private void checkMedium() {
        if (seleted == -1) {
            String errorMessage = "Select atleast one medium.";
            verticalStepperForm.setActiveStepAsUncompleted(errorMessage);
        } else {
            verticalStepperForm.setActiveStepAsCompleted();
        }
    }

    private void checkInfo()
    {
    if(to.getText().toString().equals(null) && from.getText().toString().equals(null))
    {
        String errorMessage = "enter mendetory text";
        verticalStepperForm.setActiveStepAsUncompleted(errorMessage);
    }
        else {
        verticalStepperForm.setActiveStepAsCompleted();
    }
    }

    @Override
    public void sendData() {
        tos = to.getText().toString();
        froms=from.getText().toString();
        description=desc.getText().toString();


        MySingleton.getmInstance(TripRegistration.this).addToRequestque(stringRequest);

        m.makeanewtrip(uname, tos , froms, med, description);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);


    }

    @Override
    public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = "Travelling Date : " + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;

        datetv.setText(date);
    }

    @Override
    public void onTagsChanged(Collection<String> collection) {

    }

    @Override
    public void onEditingFinished() {

    }


}
