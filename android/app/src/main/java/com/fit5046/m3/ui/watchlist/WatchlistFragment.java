package com.fit5046.m3.ui.watchlist;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fit5046.m3.R;
import com.fit5046.m3.entity.Watchlist;
import com.fit5046.m3.lib.VerticleItemDecoration;

import java.util.ArrayList;
import java.util.List;

// used for watchlist fragment.
public class WatchlistFragment extends Fragment {

    private View root;
    private WatchlistViewModel model;
    private WatchlistRvAdapter adapter;
    private RecyclerView rv;

    public WatchlistFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_watchlist, container, false);

        model = new ViewModelProvider(this).get(WatchlistViewModel.class);
        model.init(getActivity().getApplication());
        setupRv();
        model.getMovies().observe(getViewLifecycleOwner(), this::changeRv);
        model.findAllList();

        return root;
    }

    // init the recycler view using an empty list.
    private void setupRv() {
        rv = root.findViewById(R.id.rv_watch_list);
        adapter = new WatchlistRvAdapter(getActivity(), new ArrayList<>());
        adapter.setRoot(this);
        rv.setAdapter(adapter);

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(manager);
        rv.addItemDecoration(new VerticleItemDecoration(50));
    }

    // refresh the recycler view when change happens.
    private void changeRv(List<Watchlist> movies){
        adapter.setMovies(movies);
    }

}
