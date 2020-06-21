package com.fit5046.m3.ui.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.fit5046.m3.MainViewModel;
import com.fit5046.m3.R;
import com.fit5046.m3.entity.Movie;
import com.fit5046.m3.lib.ItemClickListener;
import com.fit5046.m3.lib.Utils;

import java.util.List;

// Reccyler view adapter for search screen.
public class SearchRvAdapter extends RecyclerView.Adapter<SearchRvAdapter.VH>
    implements ItemClickListener {

    private SearchFragment root;
    private List<Movie> movies;
    private LayoutInflater inflater;
    // used to respond to the click action on recycler view.
    private ItemClickListener rvClickListener;

    SearchRvAdapter(Context ctx, List<Movie> movies) {
        this.inflater = LayoutInflater.from(ctx);
        this.movies = movies;
        this.rvClickListener = this;
    }

    public SearchFragment getRoot() {
        return root;
    }

    void setRoot(SearchFragment root) {
        this.root = root;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.rv_search_results, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Movie movie = movies.get(position);
        holder.movieName.setText(Utils.trimDate(movie.getTitle(), 28));
        holder.release.setText(Utils.trimDate(movie.getReleaseDate(), 4));
        // TODO: poster url.
        if (movie.getImage() != null) {
            holder.poster.setImageBitmap(movie.getImage());
        }
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    // TODO: navigate to the movie detail page.
    @Override
    public void onItemClick(View view, int position) {
        if (root.getActivity() == null) return;
        MainViewModel mainModel = new ViewModelProvider(root.getActivity()).get(MainViewModel.class);
        mainModel.setMovie(movies.get(position));
        Navigation.findNavController(view).navigate(R.id.nav_movieview);
    }


    class VH extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView movieName, release;
        ImageView poster;

        VH(@NonNull View itemView) {
            super(itemView);
            movieName = itemView.findViewById(R.id.tv_movie_name);
            release = itemView.findViewById(R.id.tv_release_year);
            poster = itemView.findViewById(R.id.iv_poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (rvClickListener != null)
                rvClickListener.onItemClick(v, getAdapterPosition());
        }
    }
}
