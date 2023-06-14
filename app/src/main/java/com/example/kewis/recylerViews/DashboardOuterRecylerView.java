package com.example.kewis.recylerViews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kewis.R;
import com.example.kewis.databases.DatabaseHandler;
import com.example.kewis.models.Category;
import com.example.kewis.models.InnerData;

import java.util.List;

public class DashboardOuterRecylerView extends RecyclerView.Adapter<DashboardOuterRecylerView.ViewHolder> {
    private List<String> titles;
    private List<List<InnerData>> innerData;
    DatabaseHandler db;

    public DashboardOuterRecylerView(List<String> titles, List<List<InnerData>> innerData, DatabaseHandler db) {
        this.titles = titles;
        this.innerData = innerData;
        this.db = db;
    }

    @NonNull
    @Override
    public DashboardOuterRecylerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_body_recycler_title_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DashboardOuterRecylerView.ViewHolder holder, int position) {
        holder.title.setText(titles.get(position));

        GridLayoutManager layoutManager = new GridLayoutManager(holder.itemView.getContext(), 4, GridLayoutManager.VERTICAL, false);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int innerPosition) {
                return 1; // Each item takes up 1 span
            }
        });

        boolean isShopByCategory = titles.get(position).equals("Shop by Category");
        DashboardInnerRecylerView innerAdapter = new DashboardInnerRecylerView(innerData.get(position), isShopByCategory,db);
        holder.innerRecyclerView.setLayoutManager(layoutManager);
        holder.innerRecyclerView.setAdapter(innerAdapter);

        // Wrap the RecyclerView inside a HorizontalScrollView if there are more than 2 rows
        if (innerAdapter.getItemCount() > 5) {
            holder.innerRecyclerView.post(new Runnable() {
                @Override
                public void run() {
                    ViewGroup.LayoutParams layoutParams = holder.innerRecyclerView.getLayoutParams();
                    layoutParams.width = holder.itemView.getContext().getResources().getDisplayMetrics().widthPixels;
                    holder.innerRecyclerView.setLayoutParams(layoutParams);
                    holder.innerRecyclerView.setNestedScrollingEnabled(false);
                    holder.innerRecyclerView.setLayoutManager(new GridLayoutManager(holder.itemView.getContext(),4, GridLayoutManager.VERTICAL, false));
                }
            });
        } else {
            holder.innerRecyclerView.setNestedScrollingEnabled(false); // Disable nested scrolling
        }



    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        RecyclerView innerRecyclerView;
        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            title=itemView.findViewById(R.id.title_text_view);
            innerRecyclerView = itemView.findViewById(R.id.recycler_view_inner);
        }
    }
}
