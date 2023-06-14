package com.example.kewis.recylerViews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kewis.R;
import com.example.kewis.models.Feed;
import com.example.kewis.models.Product;

import java.util.List;

public class ManageFeedsAdapter extends RecyclerView.Adapter<ManageFeedsAdapter.FeedViewHolder> {

    private Context context;
    private OnFeedClickListener onFeedClickListener;
    private List<Feed> feeds;

    public ManageFeedsAdapter(Context context, List<Feed> feeds, OnFeedClickListener onFeedClickListener) {
        this.context = context;
        this.feeds = feeds;
        this.onFeedClickListener = onFeedClickListener;
    }

    @NonNull
    @Override
    public ManageFeedsAdapter.FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.manage_feed_item, parent, false);
        return new FeedViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedViewHolder holder, int position) {
        Feed feed = feeds.get(position);
        holder.textViewFeedName.setText(feed.getFeedName());
        holder.textViewTotalProducts.setText("10");

        holder.buttonEditFeed.setOnClickListener(v -> {
            onFeedClickListener.onEditClick(feed);
        });

        holder.buttonDeleteFeed.setOnClickListener(v -> {
            onFeedClickListener.onDeleteClick(feed);
        });

    }

    @Override
    public int getItemCount() {
        return feeds != null ? feeds.size() : 0;
    }

    public class FeedViewHolder extends RecyclerView.ViewHolder {
        TextView textViewFeedName;
        TextView textViewTotalProducts;
        ImageView buttonEditFeed;
        ImageView buttonDeleteFeed;



        public FeedViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewFeedName = itemView.findViewById(R.id.textView_titles_feed);
            textViewTotalProducts = itemView.findViewById(R.id.textView_total_products);
            buttonEditFeed = itemView.findViewById(R.id.button_edit_feeds);
            buttonDeleteFeed = itemView.findViewById(R.id.button_delete_feeds);
        }
    }

    public interface OnFeedClickListener {
        void onEditClick(Feed feed);
        void onDeleteClick(Feed feed);
    }
}
