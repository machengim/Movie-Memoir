package com.fit5046.m3.ui.search;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fit5046.m3.R;
import com.fit5046.m3.entity.Movie;
import com.fit5046.m3.lib.ItemClickListener;
import com.fit5046.m3.lib.Utils;
import com.fit5046.m3.lib.VerticleItemDecoration;

import java.util.List;

public class SearchFragment extends Fragment {

    private View root;
    private SearchViewModel model;
    private SearchRvAdapter adapter;

    public SearchFragment() {
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_search, container, false);

        model = new ViewModelProvider(this).get(SearchViewModel.class);
        model.getMovies().observe(getViewLifecycleOwner(), this::setRecyclerView);
        model.getNewImage().observe(getViewLifecycleOwner(), this::gotNewImage);

        Button btnSearch = root.findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(this::clickSearch);

        return root;
    }

    // Ask view model to retrieve movies according to the user input when user clicks search button.
    private void clickSearch(View view) {
        EditText etSearch = root.findViewById(R.id.et_movie_name);
        model.searchMovie(etSearch.getText().toString());
        TextView tv_title = root.findViewById(R.id.results_title);
        tv_title.setVisibility(View.VISIBLE);
        Utils.hideKeyboard(requireActivity());
    }

    // refresh the recycler view when new image has been downloaded.
    private void gotNewImage(Boolean b) {
        if (!b || adapter == null) return;
        adapter.notifyDataSetChanged();
    }

    // set up recycler view after getting movies.
    private void setRecyclerView(List<Movie> movies) {
        RecyclerView rv = root.findViewById(R.id.rv_search_results);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(manager);
        adapter = new SearchRvAdapter(getActivity(), movies);
        adapter.setRoot(this);
        rv.addItemDecoration(new VerticleItemDecoration(50));
        rv.setAdapter(adapter);
    }

}
