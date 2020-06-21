package com.fit5046.m3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.MutableLiveData;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fit5046.m3.lib.NetworkConnection;
import com.fit5046.m3.lib.Utils;
import com.fit5046.m3.entity.Person;
import com.google.gson.Gson;

import java.security.NoSuchAlgorithmException;
import java.util.Calendar;

// This activity is the only one without viewmodel.
public class SignupActivity extends AppCompatActivity {

    // indicate whether the sign up succeed.
    // 0: init state, 1: success, 2: failure.
    private MutableLiveData<Integer> success;

    public SignupActivity() {
        success = new MutableLiveData<>(0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initSpinner();

        Button btn_pick_date = findViewById(R.id.btn_pick_dob);
        btn_pick_date.setOnClickListener(this::chooseDate);

        Button btn_cancle = findViewById(R.id.btn_cancel);
        btn_cancle.setOnClickListener(this::backToLogin);
        
        Button btn_submit = findViewById(R.id.btn_submit_signup);
        btn_submit.setOnClickListener(this::signup);

        success.observe(this, this::displayMsg);
    }

    // Used when user clicks Cancel button. Redirect to login activity.
    private void backToLogin(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    // Used when user clicks button to pick date.
    private void chooseDate(View view) {
        DialogFragment datePicker = new DatePickerFragment(this);
        datePicker.show(getSupportFragmentManager(), "datePicker");
    }

    // Used to inform user whether the sign up succeed.
    // And return to login activity if so.
    private void displayMsg(Integer success) {
        String msg = "";
        if (success == 1)
            msg = "Sign up completed!";
        else if (success == 2)
            msg = "Sign up failed: email already existed.";

        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();

        if (success == 1) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }

    /***
     * Used to retrieve the date from the dob text view.
     * @param s: content on the text view, like 'birthday: xxxx-xx-xx'.
     * @return the date part, like 'xxxx-xx-xx'.
     */
    private String getDob(String s) {
        String[] ss = s.split(":");
        if (ss.length <= 1) return "";
        else return ss[1].trim();
    }

    // Used to init the spinner to choose states.
    private void initSpinner() {
        Spinner spinner = findViewById(R.id.spinner_state);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.stats, android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);
    }

    // Get user info from interface and validate them.
    // Start another thread to send request if the info has no problem.
    private void signup(View view) {
        EditText et_email, et_pw1, et_pw2, et_address, et_fname, et_lname, et_postcode;
        Spinner spinner;
        TextView tv_dob;
        RadioGroup radioGroup;

        // Get all the inputs.
        et_email = findViewById(R.id.et_email1);
        String email = et_email.getText().toString();
        et_pw1 = findViewById(R.id.et_pw1);
        String pw1 = et_pw1.getText().toString();
        et_pw2 = findViewById(R.id.et_pw2);
        String pw2 = et_pw2.getText().toString();
        et_address = findViewById(R.id.et_address);
        String address = et_address.getText().toString();
        et_fname = findViewById(R.id.et_fname);
        String fname = et_fname.getText().toString();
        et_lname = findViewById(R.id.et_lname);
        String lname = et_lname.getText().toString();
        et_postcode = findViewById(R.id.et_postcode);
        String postcode = et_postcode.getText().toString();
        spinner = findViewById(R.id.spinner_state);
        String state = spinner.getSelectedItem().toString();
        tv_dob = findViewById(R.id.tv_birthday);
        String dob = getDob(tv_dob.getText().toString());
        radioGroup = findViewById(R.id.radio_gender);
        int gender = radioGroup.getCheckedRadioButtonId();
        if (gender < 0) gender = 2;

        // allow dob, address, stat and postcode empty.
        if (email.length() == 0 || pw1.length() == 0 || pw2.length() == 0
                || fname.length() == 0 || lname.length() == 0) {
            Toast.makeText(this, "Missing mandantory field.", Toast.LENGTH_LONG).show();
            return;
        }

        if (postcode.length() > 4) {
            Toast.makeText(this, "Postcode only allow 4 digits", Toast.LENGTH_LONG).show();
            return;
        }

        if (!pw1.equals(pw2)) {
            Toast.makeText(this, "Passwords not match.", Toast.LENGTH_LONG).show();
            return;
        }

        String password = "";

        try {
            password = Utils.hash(pw1);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return;
        }

        Person p = new Person(fname, lname, gender, state, address, postcode, dob, email, password,
                Utils.getCurrentDate());

        Thread t = new Thread(() -> signupRequest(p));
        t.start();
    }

    // Used to send sign up request and handle response.
    private void signupRequest(Person p) {
        Gson gson = new Gson();
        String jo = gson.toJson(p);

        NetworkConnection conn = new NetworkConnection();
        conn.buildUrl(4, "m3.person/signup");
        String output = conn.request("post", jo);
        if (output.contains("exist"))
            success.postValue(2);
        else if (output.contains("OK"))
            success.postValue(1);
    }

    // The DatePicker component for choosing birthday.
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        private SignupActivity root;

        DatePickerFragment(SignupActivity root) {
            this.root = root;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(requireActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            String monthStr = Utils.addZeroBeforeStr(month + 1);
            String dayStr = Utils.addZeroBeforeStr(dayOfMonth);
            TextView tv = root.findViewById(R.id.tv_birthday);
            tv.setText(String.format("Birthday: %s-%s-%s",year, monthStr, dayStr));
        }
    }

}
