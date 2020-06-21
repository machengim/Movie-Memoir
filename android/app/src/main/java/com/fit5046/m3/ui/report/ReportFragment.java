package com.fit5046.m3.ui.report;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.fit5046.m3.MainViewModel;
import com.fit5046.m3.R;
import com.fit5046.m3.entity.WatchedInMonth;
import com.fit5046.m3.entity.WathedInArea;
import com.fit5046.m3.lib.Utils;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

// Used for Report screen.
public class ReportFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private View root;
    private ReportViewModel model;
    PieChart pie;
    BarChart bar;
    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;
    private int uid;

    public ReportFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_report, container, false);

        // get user id from Main ViewModel and set up spinner and charts.
        MainViewModel mainModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        if (mainModel.getUserId().getValue() == null) return null;
        initSpinner();
        uid = mainModel.getUserId().getValue();
        pie = root.findViewById(R.id.chart_pie);
        bar = root.findViewById(R.id.chart_bar);

        // connect to viewmodel instance and start retrieving info.
        model = new ViewModelProvider(this).get(ReportViewModel.class);
        model.clickRetrieveAreaCounts(uid);
        model.clickRetrieveMonthCounts(uid);

        // start observation.
        model.getAreaCounts().observe(getViewLifecycleOwner(), this::setPieChart);
        model.getMonthCounts().observe(getViewLifecycleOwner(), this::setBarChart);
        model.getStartDate().observe(getViewLifecycleOwner(), this::setStartDate);
        model.getEndDate().observe(getViewLifecycleOwner(), this::setEndDate);

        Button btn_choose_start = root.findViewById(R.id.btn_start_date);
        btn_choose_start.setOnClickListener(v -> chooseDate(v, 1));

        Button btn_choose_end = root.findViewById(R.id.btn_end_date);
        btn_choose_end.setOnClickListener(v -> chooseDate(v, 2));

        Button btn_retrieve_area_counts = root.findViewById(R.id.btn_confirm_pie);
        btn_retrieve_area_counts.setOnClickListener(v -> model.clickRetrieveAreaCounts(uid));

        return root;
    }

    // when user clicks the Choose date button, pop up a dialog for date picker.
    private void chooseDate(View v, int option) {
        DialogFragment datePicker = new DatePickerFragment(model, option);
        datePicker.show(requireActivity().getSupportFragmentManager(), "datePicker");
    }

    // init spinner according to the years in arrays.xml.
    private void initSpinner() {
        spinner = root.findViewById(R.id.spinner_year);
        adapter = ArrayAdapter.createFromResource(requireActivity(),
                R.array.years, android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    // refresh the bar chart after new figures got.
    private void setBarChart(List<WatchedInMonth> results) {
        bar.setDrawBarShadow(false);
        bar.setDrawValueAboveBar(true);
        bar.getDescription().setEnabled(false);
        bar.setDrawGridBackground(false);
        bar.getAxisLeft().setGranularity(1f);
        bar.getAxisRight().setGranularity(1f);

        // collect counts into entries list to use as bar chart values.
        ArrayList<BarEntry> entries = new ArrayList<>();
        int i = 1;  // Use i to indicate the month index.
        for (WatchedInMonth w: results) {
            entries.add(new BarEntry(i, w.getCount()));
            i++;
        }

        BarDataSet dataSet = new BarDataSet(entries, null);
        BarData data = new BarData(dataSet);
        bar.setData(data);
        bar.getLegend().setEnabled(false);
        bar.invalidate();
    }


    // change the end date when user select a new date.
    private void setEndDate(String s) {
        TextView tv_end = root.findViewById(R.id.tv_end_date);
        tv_end.setText(s);
    }

    // change the start date when user select a new date.
    private void setStartDate(String s) {
        TextView tv_start = root.findViewById(R.id.tv_start_date);
        tv_start.setText(s);
    }

    // refresh pie chart when new data retrieved.
    private void setPieChart(List<WathedInArea> results) {

        ArrayList<PieEntry> entries = new ArrayList<>();
        ArrayList<LegendEntry> legends = new ArrayList<>();

        int i = 0;
        for (WathedInArea w: results) {
            entries.add(new PieEntry(w.getCount(), w.getCount() + " in area " + w.getPostcode()));
            LegendEntry legend = new LegendEntry();
            legend.label = w.getPostcode();
            legend.formColor = ColorTemplate.COLORFUL_COLORS[i];
            legends.add(legend);
            i++;
        }

        pie.setUsePercentValues(true);
        pie.getDescription().setEnabled(false);
        PieDataSet dataSet = new PieDataSet(entries, null);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter(pie));
        data.setValueTextSize(12f);

        pie.setData(data);
        pie.getLegend().setEntries(legends);
        pie.invalidate();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String year = spinner.getSelectedItem().toString();
        model.setYear(year);
        model.clickRetrieveMonthCounts(uid);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    // inner class for DatePicker.
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        private ReportViewModel model;
        private int option;

        DatePickerFragment(ReportViewModel model, int option) {
            this.model = model;
            this.option = option;
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
            if (option == 1)
                model.setStartDate(year + "-" + monthStr + "-" + dayStr);
            else
                model.setEndDate(year + "-" + monthStr + "-" + dayStr);
        }
    }

}
