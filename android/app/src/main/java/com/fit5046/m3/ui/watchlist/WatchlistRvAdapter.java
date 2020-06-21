package com.fit5046.m3.ui.watchlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.fit5046.m3.MainViewModel;
import com.fit5046.m3.R;
import com.fit5046.m3.entity.Movie;
import com.fit5046.m3.entity.Watchlist;
import com.fit5046.m3.lib.ItemClickListener;
import com.fit5046.m3.lib.Utils;

import java.util.List;

// recycler view adapter for watch list screen.
public class WatchlistRvAdapter extends RecyclerView.Adapter<WatchlistRvAdapter.VH>
    implements ItemClickListener {

    private WatchlistFragment root;
    private List<Watchlist> movies;
    private LayoutInflater inflater;

    WatchlistRvAdapter(Context ctx, List<Watchlist> movies) {
        this.inflater = LayoutInflater.from(ctx);
        this.movies = movies;
    }

    public WatchlistFragment getRoot() {
        return root;
    }

    void setRoot(WatchlistFragment root) {
        this.root = root;
    }

    public List<Watchlist> getMovies() {
        return movies;
    }

    void setMovies(List<Watchlist> movies) {
        this.movies.clear();
        this.movies.addAll(movies);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.rv_watch_list, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Watchlist watchlist = movies.get(position);
        holder.movieName.setText(Utils.trimDate(watchlist.movieName, 28));
        holder.release.setText(Utils.trimDate(watchlist.release, 10));
        holder.addTime.setText(Utils.trimDate(watchlist.addTime, 19));
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    // respond to the click action of deleting a watch list from the local database.
    private void deleteItem(View view, int postion) {
        WatchlistViewModel model = new ViewModelProvider(root).get(WatchlistViewModel.class);
        model.remove(movies.get(postion));
    }

    // respond to the click action of showing detail of a watch list.
    private void showDetail(View view, int position) {
        if (root.getActivity() == null) return;

        // store the movie into Main ViewModel and redirecto to MovieView fragment.
        Watchlist watch = movies.get(position);
        Movie target = new Movie(watch.id, watch.movieName, watch.release);
        MainViewModel mainModel = new ViewModelProvider(root.getActivity()).get(MainViewModel.class);
        mainModel.setMovie(target);

        Navigation.findNavController(view).navigate(R.id.nav_movieview);
    }

    class VH extends RecyclerView.ViewHolder{

        TextView movieName, release, addTime;

        VH(@NonNull View itemView) {
            super(itemView);
            movieName = itemView.findViewById(R.id.tv_title_pop);
            release = itemView.findViewById(R.id.tv_release_pop);
            addTime = itemView.findViewById(R.id.tv_add_time);
            Button btn_delete = itemView.findViewById(R.id.btn_delete);
            btn_delete.setOnClickListener(this::deleteWatchList);
            Button btn_view = itemView.findViewById(R.id.btn_view);
            btn_view.setOnClickListener(this::showMovieDetai);
        }

        void deleteWatchList(View v) {
            deleteItem(v, getAdapterPosition());
        }

        void showMovieDetai(View v) {
            showDetail(v, getAdapterPosition());
        }
    }
}
