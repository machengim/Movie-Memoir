package com.fit5046.m3.ui.addmemoir;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.fit5046.m3.MainViewModel;
import com.fit5046.m3.R;
import com.fit5046.m3.entity.Cinema;
import com.fit5046.m3.entity.Movie;
import com.fit5046.m3.lib.Utils;
import com.fit5046.m3.ui.dialog.SendTweetFragment;

import java.util.Calendar;
import java.util.List;

// Fragment for Add Memoir page.
public class AddMemoirFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private View root;
    private AddMemoirViewModel model;
    private ArrayAdapter<String> adapter;
    private int cinemaid;

    public AddMemoirFragment() {
        cinemaid = 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_add_memoir, container, false);
        model = new ViewModelProvider(this).get(AddMemoirViewModel.class);

        // retrieve movie object from MainViewModel.
        MainViewModel mainModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        setMovieInfo(mainModel.getMovie());

        // init date, time, Spinner, and button click listeners.
        initDateTime();

        Button btn_choose_time = root.findViewById(R.id.btn_choose_time);
        btn_choose_time.setOnClickListener(this::chooseTime);

        Button btn_choose_date = root.findViewById(R.id.btn_choose_date);
        btn_choose_date.setOnClickListener(this::chooseDate);

        Button btn_add_cinema = root.findViewById(R.id.btn_add_cinema);
        btn_add_cinema.setOnClickListener(this::popupAddCinema);

        Button btn_add_memoir = root.findViewById(R.id.btn_submit_new_memoir);
        btn_add_memoir.setOnClickListener(this::addMemoir);

        Spinner spinner = root.findViewById(R.id.spinner_cinema);
        setupSpinner(spinner);
        spinner.setOnItemSelectedListener(this);

        // observe live data.
        model.getWatchTime().observe(getViewLifecycleOwner(), this::setWatchTime);
        model.getWatchDate().observe(getViewLifecycleOwner(), this::setWatchDate);
        model.getCinemas().observe(getViewLifecycleOwner(), this::setSpinner);
        model.getInfo().observe(getViewLifecycleOwner(), this::toastInfo);
        model.getSent().observe(getViewLifecycleOwner(), this::askTweet);
        model.getTweetSent().observe(getViewLifecycleOwner(), this::tweetSent);

        return root;
    }

    // Prepare to send memoir info when user clicks the submit button.
    private void addMemoir(View v) {
        System.out.println("add memoir in fragment");
        EditText et_watch_date = root.findViewById(R.id.et_watch_date);
        EditText et_watch_time = root.findViewById(R.id.et_watch_time);

        String time = et_watch_date.getText().toString().trim() + " "
                + et_watch_time.getText().toString().trim();

        RatingBar ratingBar = root.findViewById(R.id.rating2);
        float rating = ratingBar.getRating();

        EditText et_comment = root.findViewById(R.id.et_comment);
        String comment = et_comment.getText().toString();

        MainViewModel mainModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        if (mainModel.getUserId().getValue() == null) return;
        int uid = mainModel.getUserId().getValue();
        Movie movie = mainModel.getMovie();

        model.addMemoir(movie, time, cinemaid, uid, rating, comment);
    }

    // Ask user whether to send a tweet after the memoir being saved.
    private void askTweet(Integer sent) {
        if (sent == 2) {
            Toast.makeText(requireActivity(), "Failed", Toast.LENGTH_LONG).show();
        } else if (sent == 1) {
            SendTweetFragment fragment = new SendTweetFragment(model.getMessage());
            fragment.show(getActivity().getSupportFragmentManager(), "missiles");
            fragment.setModel(this.model);
        }
    }

    // Used when user clicks button to pick a date.
    private void chooseDate(View v) {
        DialogFragment datePicker = new DatePickerFragment(model);
        datePicker.show(requireActivity().getSupportFragmentManager(), "datePicker");
    }

    // Used when user clicks button to pick a time.
    private void chooseTime(View v) {
        DialogFragment timePicker = new TimePickerFragment(model);
        timePicker.show(requireActivity().getSupportFragmentManager(), "timePicker");
    }

    // set the init date and time to current clock.
    private void initDateTime() {
        EditText et_date = root.findViewById(R.id.et_watch_date);
        EditText et_time = root.findViewById(R.id.et_watch_time);
        et_date.setText(Utils.getCurrentDate());
        et_time.setText(Utils.getCurrentTime().substring(11, 16));
    }

    // Pop up a dialog to add new cinema when user clicks the button.
    private void popupAddCinema(View view) {
        Dialog dialog = new Dialog(requireActivity());
        dialog.setContentView(R.layout.popup_add_cinema);
        dialog.setCancelable(false);
        dialog.show();

        EditText et_name = dialog.findViewById(R.id.et_cinema_name);
        EditText et_postcode = dialog.findViewById(R.id.st_postcode);
        Button btn_close = dialog.findViewById(R.id.btn_close);
        Button btn_add_cinema = dialog.findViewById(R.id.btn_submit_new_memoir);

        btn_close.setOnClickListener(v -> dialog.dismiss());
        btn_add_cinema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.addCinema(et_name.getText().toString()
                        , et_postcode.getText().toString());
                dialog.dismiss();
            }
        });
    }

    // display movie info on interface.
    private void setMovieInfo(Movie movie) {
        if (movie == null) return;

        TextView tv_title = root.findViewById(R.id.tv_title_pop);
        tv_title.setText(movie.getTitle());
        TextView tv_release = root.findViewById(R.id.tv_release_pop);
        tv_release.setText(movie.getReleaseDate());
        ImageView iv_poster = root.findViewById(R.id.iv_poster);

        if (movie.getImage() != null) {
            iv_poster.setImageBitmap(movie.getImage());
        } else {
            // TODO: retrieve poster from internet.
        }
    }

    // Refresh the spinner contents when cinemas change.
    private void setSpinner(List<Cinema> cinemas) {
        adapter.clear();
        for (Cinema c: cinemas) {
            adapter.add(c.toString());
        }
        adapter.notifyDataSetChanged();
    }

    // init spinner for user to choose cinema.
    private void setupSpinner(Spinner spinner) {
        adapter = new ArrayAdapter<>(requireActivity(),
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    // used when user selects a new date and it is observed.
    private void setWatchDate(String date) {
        TextView et_watch_date = root.findViewById(R.id.et_watch_date);
        et_watch_date.setText(date);
    }

    // used when user selects a new time and it is observed.
    private void setWatchTime(String time) {
        TextView et_watch_time = root.findViewById(R.id.et_watch_time);
        et_watch_time.setText(time);
    }

    // helper method to display toast.
    private void toastInfo(String info) {
        Toast.makeText(getContext(), info, Toast.LENGTH_SHORT).show();
    }

    // Send a message to inform user the tweet has been sent successfully.
    private void tweetSent(Integer sent) {
        if (sent == 0) return;

        if (sent == 2)
            Toast.makeText(requireActivity(), "Tweet sent.", Toast.LENGTH_LONG).show();

        //Navigation.findNavController(root).popBackStack();
    }

    // Get the cinema id from the spinner.
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String s = (String) parent.getItemAtPosition(position);
        String t = s.split(",")[0];
        this.cinemaid = Integer.parseInt(t);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    // DataPicker and TimePicker fragments below.
    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        private AddMemoirViewModel model;

        TimePickerFragment(AddMemoirViewModel model) {
            this.model = model;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String hour = Utils.addZeroBeforeStr(hourOfDay);
            String min = Utils.addZeroBeforeStr(minute);
            model.setWatchTime(hour + ":" + min);
        }
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        private AddMemoirViewModel model;

        DatePickerFragment(AddMemoirViewModel model) {
            this.model = model;
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
            model.setWatchDate(year + "-" + monthStr + "-" + dayStr);
        }
    }

}
