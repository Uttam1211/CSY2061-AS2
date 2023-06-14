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
import com.example.kewis.models.User;

import java.util.List;

public class ManageUsersAdapter extends RecyclerView.Adapter<ManageUsersAdapter.UserViewHolder> {
    private Context context;
    private OnUserClickListener onUserClickListener;
    private List<User> users;

    public ManageUsersAdapter(Context context, List<User> users, OnUserClickListener onUserClickListener) {
        this.context = context;
        this.users = users;
        this.onUserClickListener = onUserClickListener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.manage_user_list, parent, false);
        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = users.get(position);
        holder.textViewUserName.setText(user.getUsername());
        holder.textViewUserType.setText(user.getUsertype());

        holder.buttonEditUser.setOnClickListener(v -> {
            onUserClickListener.onEditClick(user);
        });

        holder.buttonDeleteUser.setOnClickListener(v -> {
            onUserClickListener.onDeleteClick(user);
        });
    }

    @Override
    public int getItemCount() {
        return users != null ? users.size() : 0;
    }


    public class UserViewHolder extends RecyclerView.ViewHolder {
        TextView textViewUserName;
        TextView textViewUserType;
        ImageView buttonEditUser;
        ImageView buttonDeleteUser;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewUserName = itemView.findViewById(R.id.textView_cart_product);
            textViewUserType = itemView.findViewById(R.id.date_added);
            buttonEditUser = itemView.findViewById(R.id.button_edit_user);
            buttonDeleteUser = itemView.findViewById(R.id.button_delete_cart);


        }


    }

    public interface OnUserClickListener {
        void onEditClick(User user);

        void onDeleteClick(User user);
    }


}
