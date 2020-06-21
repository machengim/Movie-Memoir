package com.fit5046.m3.ui.movieview;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fit5046.m3.MainViewModel;
import com.fit5046.m3.R;
import com.fit5046.m3.entity.Cast;
import com.fit5046.m3.entity.Employee;
import com.fit5046.m3.entity.Genre;
import com.fit5046.m3.entity.Movie;
import com.fit5046.m3.entity.MovieDetail;
import com.fit5046.m3.entity.Watchlist;
import com.fit5046.m3.lib.AsyncDbTask;
import com.fit5046.m3.lib.AsyncResponse;
import com.fit5046.m3.lib.Utils;

import java.util.List;
import java.util.stream.Collectors;

// Fragment for Movieview fragment.
public class MovieviewFragment extends Fragment implements AsyncResponse {

    private View root;
    private MovieviewViewModel model;
    private int mode = 0;
    private Watchlist watchlist;

    public MovieviewFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_movieview, container, false);

        model = new ViewModelProvider(this).get(MovieviewViewModel.class);
        MainViewModel mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        model.init(getActivity().getApplication(), mainViewModel.getMovie());
        model.getMovieDetail();

        model.getMovie().observe(getViewLifecycleOwner(), this::setView);
        model.getDetail().observe(getViewLifecycleOwner(), this::setDetailView);
        model.getCast().observe(getViewLifecycleOwner(), this::setCastView);
        model.getExistInWatchList().observe(getViewLifecycleOwner(), this::setAddToListButton);

        Button btn_watchlist = root.findViewById(R.id.btn_add_watchlist);
        btn_watchlist.setOnClickListener(v -> prepareToInsertWatchList());

        Button btn_memoir = root.findViewById(R.id.btn_add_memoir);
        btn_memoir.setOnClickListener(v -> addToMemoir());

        return root;
    }

    // When user clicks Add to memoir button, redirect to that fragment.
    private void addToMemoir() {
        Navigation.findNavController(root).navigate(R.id.nav_addmemoir);
    }

    // Used to set the movie's meta info on view.
    private void setView(Movie movie) {
        TextView title = root.findViewById(R.id.tv_title_pop);
        title.setText(movie.getTitle());
        TextView release = root.findViewById(R.id.tv_release_pop);
        release.setText(movie.getReleaseDate());
    }

    // Used to change movie detail info on view.
    private void setDetailView(MovieDetail detail) {
        if (detail.getRating() > 0) {
            TextView rating = root.findViewById(R.id.tv_rating);
            rating.setText(String.format("%s", detail.getRating()));
            RatingBar rb = root.findViewById(R.id.ratingBar);
            rb.setRating(detail.getRating() / 2);
        }

        if (detail.getCountries() != null && detail.getCountries().size() > 0) {
            TextView country = root.findViewById(R.id.tv_country);
            country.setText(detail.getCountries().get(0).getName());
        }

        if (detail.getGenres() != null && detail.getGenres().size() > 0) {
            List<String> aList = detail.getGenres().stream()
                    .map(Genre::getName).collect(Collectors.toList());
            String genres = String.join(", ", aList);
            TextView genre = root.findViewById(R.id.tv_genre);
            genre.setText(genres);
        }

        if (detail.getOverview() != null) {
            TextView overview = root.findViewById(R.id.tv_plot);
            overview.setText(detail.getOverview());
        }
    }

    // used to change the cast contents after retrieving.
    private void setCastView(Cast cast) {
        if (cast == null || cast.getCast().size() == 0) return;

        List<String> actors = cast.getCast().stream().filter(e -> e.getJob() == null)
                .limit(5).map(Employee::getName).collect(Collectors.toList());
        if (actors.size() > 0) {
            String actorNames = String.join(", ", actors);
            TextView tv_actors = root.findViewById(R.id.tv_cast);
            tv_actors.setText(actorNames);
        }

        List<String> directors = cast.getCrew().stream().filter(e -> e.getJob() != null)
                .filter(e -> e.getJob().equals("Director"))
                .map(Employee::getName).collect(Collectors.toList());
        if(directors.size() > 0) {
            String directorNames = String.join(",", directors);
            TextView tv_director = root.findViewById(R.id.tv_directors);
            tv_director.setText(directorNames);
        }
    }

    // Disable the "Add to WatchLList" button if this movie already existed in database.
    private void setAddToListButton(Boolean b) {
        Button btn = root.findViewById(R.id.btn_add_watchlist);
        btn.setEnabled(!b);
        if (b) {
            btn.setText(R.string.added);
        }
    }

    // Used when user clicks "Add to watchlist" button. Start an async database operation.
    private void prepareToInsertWatchList() {
        if (model.getMovie().getValue() == null) return;

        // mode 1 means prepare to insert data to watchlist, so check database first.
        mode = 1;
        watchlist = new Watchlist(model.getMovie().getValue(), Utils.getCurrentTime());
        AsyncDbTask task = new AsyncDbTask(1, watchlist, this, getContext());
        task.execute();

        Button btn = root.findViewById(R.id.btn_add_watchlist);
        btn.setEnabled(false);
    }

    private void insertWatchList() {
        AsyncDbTask task = new AsyncDbTask(2, watchlist, this, getContext());
        task.execute();
    }

    @Override
    public void processFinish(String output) {
        String info = "Success.";

        if (mode == 1 && output != null) {
            info = "Watchlist already exists.";
        } else if (mode == 1) {
            mode++;
            insertWatchList();
        } else {
            mode = 0;
        }

        Toast.makeText(getActivity(), info, Toast.LENGTH_SHORT).show();
    }
}
