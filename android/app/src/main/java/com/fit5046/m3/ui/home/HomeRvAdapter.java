package com.fit5046.m3.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fit5046.m3.R;
import com.fit5046.m3.entity.Memoir;
import com.fit5046.m3.lib.Utils;

import java.util.List;

// Recycler view adapter used in home fragment.
public class HomeRvAdapter extends RecyclerView.Adapter<HomeRvAdapter.VH> {

    private List<Memoir> memoirs;
    private LayoutInflater inflater;

    HomeRvAdapter(Context ctx, List<Memoir> memoirs) {
        this.inflater = LayoutInflater.from(ctx);
        this.memoirs = memoirs;
    }

    // inner ViewHolder class to control the row contents.
    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.rv_memoir_on_home, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Memoir memoir = memoirs.get(position);
        holder.movieName.setText(memoir.getMovieName());
        holder.release.setText(Utils.trimDate(memoir.getReleaseDate(), 10));
        holder.score.setText(String.valueOf(memoir.getScore()));
    }

    @Override
    public int getItemCount() {
        return memoirs.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView movieName, release, score;

        VH(@NonNull View itemView) {
            super(itemView);
            movieName = itemView.findViewById(R.id.movie_name);
            release = itemView.findViewById(R.id.release);
            score = itemView.findViewById(R.id.score);
        }
    }
}
