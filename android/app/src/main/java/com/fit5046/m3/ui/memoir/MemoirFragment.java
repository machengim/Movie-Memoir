package com.fit5046.m3.ui.memoir;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.fit5046.m3.MainViewModel;
import com.fit5046.m3.R;
import com.fit5046.m3.entity.Cast;
import com.fit5046.m3.entity.Employee;
import com.fit5046.m3.entity.Genre;
import com.fit5046.m3.entity.MovieDetail;
import com.fit5046.m3.lib.Utils;
import com.fit5046.m3.lib.VerticleItemDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// Used for Memoir screen. The filter feature is not implemented.
public class MemoirFragment extends Fragment
        implements AdapterView.OnItemSelectedListener {

    private View root;
    private MemoirViewModel model;
    // adapter for recycler view.
    private MemoirRvAdapter adapter;
    // adapters for sortBy spinner and filter spinner.
    private ArrayAdapter<CharSequence> adapterSort, adapterFilter;
    private Spinner sp_sort, sp_filter;

    public MemoirFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_memoir, container, false);
        model = new ViewModelProvider(this).get(MemoirViewModel.class);
        // set up recycler view and spinners at first.
        setupRv();
        setupSpinners();

        // get user id from Main View Model and then retrieve memoirs from server.
        MainViewModel mainModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        model.retrieveMemoirs(mainModel.getUserId().getValue());

        // observation starts.
        model.getMemoirs().observe(getViewLifecycleOwner(),
                c -> { model.retrievePosters(); adapter.setMemoirs(c);});
        model.getCinemas().observe(getViewLifecycleOwner(), this::changeFilter);
        model.getChange().observe(getViewLifecycleOwner(), c -> adapter.notifyDataSetChanged());

        return root;
    }

    // Used when new Cast has been retrieved, the dialog contents will be changed.
    private void changeCast(Cast cast, Dialog dialog) {
        if (cast == null || cast.getCast() == null) return;

        List<String> actors = cast.getCast().stream().filter(e -> e.getJob() == null)
                .limit(5).map(Employee::getName).collect(Collectors.toList());
        if (actors.size() > 0) {
            String actorNames = String.join(", ", actors);
            TextView tv_actors = dialog.findViewById(R.id.tv_cast);
            tv_actors.setText(actorNames);
        }

        List<String> directors = cast.getCrew().stream().filter(e -> e.getJob() != null)
                .filter(e -> e.getJob().equals("Director"))
                .map(Employee::getName).collect(Collectors.toList());
        if(directors.size() > 0) {
            String directorNames = String.join(",", directors);
            TextView tv_director = dialog.findViewById(R.id.tv_director);
            tv_director.setText(directorNames);
        }
    }

    // Used when new MovieDetail has been retrieved, the dialog contents will be changed.
    private void changeDetail(MovieDetail detail, Dialog dialog) {
        if (detail.getRating() > 0) {
            RatingBar rb = dialog.findViewById(R.id.ratingBar3);
            rb.setRating(detail.getRating() / 2);
        }

        if (detail.getCountries() != null && detail.getCountries().size() > 0) {
            TextView country = dialog.findViewById(R.id.tv_country);
            country.setText(detail.getCountries().get(0).getName());
        }

        if (detail.getGenres() != null && detail.getGenres().size() > 0) {
            List<String> aList = detail.getGenres().stream()
                    .map(Genre::getName).collect(Collectors.toList());
            String genres = String.join(", ", aList);
            TextView genre = dialog.findViewById(R.id.tv_genre);
            genre.setText(genres);
        }

        if (detail.getOverview() != null) {
            TextView overview = dialog.findViewById(R.id.tv_plot);
            overview.setText(detail.getOverview());
        }
    }

    // Used when user selects a new filter.
    private void changeFilter(Set<String> cinemas) {
        adapterFilter.clear();
        adapterFilter.addAll(cinemas);
        adapterFilter.notifyDataSetChanged();
    }

    // set up spinners at first. sort by spinner contents are read from arrays.xml,
    // filter spinner waits for the response of server and use cinema name as filter keys.
    private void setupSpinners() {
        sp_sort = root.findViewById(R.id.spinner_sort_condition);
        sp_filter = root.findViewById(R.id.spinner_filter);

        adapterSort = ArrayAdapter.createFromResource(requireActivity(),
                R.array.sort_options, android.R.layout.simple_spinner_item);
        adapterSort.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_sort.setAdapter(adapterSort);
        sp_sort.setOnItemSelectedListener(this);

        adapterFilter = new ArrayAdapter<>(requireActivity(),
                android.R.layout.simple_spinner_item);
        adapterFilter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_filter.setAdapter(adapterFilter);
    }

    // set up recycler view according to memoir list retrieved.
    private void setupRv() {
        RecyclerView rv = root.findViewById(R.id.rv_memoir_list);
        adapter = new MemoirRvAdapter(getActivity(), new ArrayList<>());
        adapter.setRoot(this);
        rv.setAdapter(adapter);

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(manager);
        rv.addItemDecoration(new VerticleItemDecoration(50));
    }

    // Pop up a dialog to display movie detail when user selects one row on recycler view.
    void popupMovieDetail(int mid, String title, String release) {

        System.out.println("pop up movie detail");
        Dialog dialog = new Dialog(requireActivity());
        dialog.setContentView(R.layout.popup_movie_detail);
        dialog.setCancelable(false);
        dialog.show();
        model.retrieveMovieInfo(mid);

        TextView tv_title = dialog.findViewById(R.id.tv_title_pop);
        TextView tv_release = dialog.findViewById(R.id.tv_release_pop);
        Button btn_close = dialog.findViewById(R.id.btn_close);

        tv_title.setText(title);
        tv_release.setText(Utils.trimDate(release, 10));
        btn_close.setOnClickListener(v -> dialog.dismiss());

        // observe Detail and Cast respectively.
        model.getDetail().observe(getViewLifecycleOwner(), d -> changeDetail(d, dialog));
        model.getCast().observe(getViewLifecycleOwner(), c -> changeCast(c, dialog));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //System.out.println(parent + ", " + view);
        model.sortMemoirs(position);
        adapterSort.notifyDataSetChanged();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
