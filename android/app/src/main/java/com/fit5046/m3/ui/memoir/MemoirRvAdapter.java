package com.fit5046.m3.ui.memoir;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.fit5046.m3.R;
import com.fit5046.m3.entity.Memoir;
import com.fit5046.m3.lib.Utils;

import java.util.List;

// recycler view adapter for memoir.
public class MemoirRvAdapter extends RecyclerView.Adapter<MemoirRvAdapter.VH> {

    private MemoirFragment root;
    private List<Memoir> memoirs;
    private LayoutInflater inflater;

    MemoirRvAdapter(Context context, List<Memoir> memoirs) {
        this.inflater = LayoutInflater.from(context);
        this.memoirs = memoirs;
    }

    public void setRoot(MemoirFragment root) {
        this.root = root;
    }

    // refresh memoirs when getting new response.
    void setMemoirs(List<Memoir> memoirs) {
        this.memoirs.clear();
        for (Memoir m: memoirs) {
            this.memoirs.add(m);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.rv_memoir_list, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Memoir memoir = memoirs.get(position);
        holder.tv_title.setText(memoir.getMovieName());
        holder.tv_release.setText(Utils.trimDate(memoir.getReleaseDate(), 10));
        holder.tv_watch_time.setText(Utils.trimDate(memoir.getWatchTime(), 16));
        holder.ratingBar.setRating(memoir.getScore());
        holder.tv_comment.setText(memoir.getComment());
        holder.tv_cinema.setText(memoir.getCinema().getName());
        if (memoir.getPoster() != null) {
            holder.iv_poster.setImageBitmap(memoir.getPoster());
        }
        holder.layout.setOnClickListener(v -> clickLayout(position));
    }

    @Override
    public int getItemCount() {
        return memoirs.size();
    }

    // Used when user select a row. the movie detail dialog will display.
    private void clickLayout(int pos) {
        int mid = memoirs.get(pos).getMid();
        root.popupMovieDetail(mid, memoirs.get(pos).getMovieName(),
                memoirs.get(pos).getReleaseDate());
    }

    class VH extends RecyclerView.ViewHolder {
        ConstraintLayout layout;
        TextView tv_title, tv_release, tv_watch_time, tv_cinema, tv_comment;
        RatingBar ratingBar;
        ImageView iv_poster;

        VH(@NonNull View itemView) {
            super(itemView);
            tv_cinema = itemView.findViewById(R.id.tv_cinema);
            tv_comment = itemView.findViewById(R.id.tv_comment);
            tv_release = itemView.findViewById(R.id.tv_release_pop);
            tv_title = itemView.findViewById(R.id.tv_title_pop);
            tv_watch_time = itemView.findViewById(R.id.tv_watch_time);
            ratingBar = itemView.findViewById(R.id.ratingBar2);
            iv_poster = itemView.findViewById(R.id.iv_poster2);
            layout = itemView.findViewById(R.id.rv_memoir_layout);
        }

    }
}
