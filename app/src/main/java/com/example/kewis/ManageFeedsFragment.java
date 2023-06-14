package com.example.kewis;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.kewis.databases.DatabaseHandler;
import com.example.kewis.databases.DatabaseHelper;
import com.example.kewis.models.Feed;
import com.example.kewis.recylerViews.ManageFeedsAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ManageFeedsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ManageFeedsFragment extends Fragment implements ManageFeedsAdapter.OnFeedClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ManageFeedsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ManageFeedsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ManageFeedsFragment newInstance(String param1, String param2) {
        ManageFeedsFragment fragment = new ManageFeedsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    private DatabaseHandler db;
    private RecyclerView recyclerView;
    private ManageFeedsAdapter manageFeedAdapter;
    private List<Feed> feedList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_manage_feeds, container, false);
        recyclerView = view.findViewById(R.id.recyclerView_display_feeds);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        db = new DatabaseHandler(getActivity(), new DatabaseHelper(getActivity()));
        feedList = new ArrayList<>();
        feedList = db.getAllFeeds();

        manageFeedAdapter = new ManageFeedsAdapter(getActivity(), feedList,this);
        recyclerView.setAdapter(manageFeedAdapter);


        FloatingActionButton fabAddFeedTitle = view.findViewById(R.id.fab_add_feeds);
        fabAddFeedTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddFeedTitle();
            }
        });
        return view;
    }

    private void showAddFeedTitle() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_feed_titles, null);
        builder.setView(view);

        EditText editTextFeedTitle = view.findViewById(R.id.text_feed_title);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String feedTitle = editTextFeedTitle.getText().toString().trim();
            if (feedTitle.isEmpty()) {
                editTextFeedTitle.setError("Feed title required");
                editTextFeedTitle.requestFocus();
                return;
            }
            Feed feed = new Feed(feedTitle);
            long id = db.addFeed(feed);
        });
builder.setNegativeButton("Cancel", null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    @Override
    public void onEditClick(Feed feed) {

    }

    @Override
    public void onDeleteClick(Feed feed) {

    }
}