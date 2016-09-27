package id.co.lazystudio.watchIt_freemoviedatabase.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import id.co.lazystudio.watchIt_freemoviedatabase.R;
import id.co.lazystudio.watchIt_freemoviedatabase.entity.Genre;

/**
 * Created by faqiharifian on 28/09/16.
 */
public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.ViewHolder> {
    List<Genre> mGenreList;
    Context mContext;

    public GenreAdapter(Context context, List<Genre> genres){
        mContext = context;
        mGenreList = genres;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_genres, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Genre genre = mGenreList.get(position);
        holder.bind(genre);
        holder.genreTextView.setText(genre.getName());
    }

    @Override
    public int getItemCount() {
        return mGenreList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView genreTextView;
        public ViewHolder(View v){
            super(v);
            genreTextView = (TextView) v.findViewById(R.id.genre_text_view);
        }

        public void bind(final Genre genre){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("clicked", String.valueOf(genre.getId() +" - "+ genre.getName()));
                }
            });
        }
    }
}
