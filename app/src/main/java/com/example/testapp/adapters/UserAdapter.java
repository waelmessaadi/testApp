package com.example.testapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapp.R;
import com.example.testapp.models.UserModel;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    List<UserModel> listUser = new ArrayList<>();
    OnItemClickListener mClickListener;
    Context context;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mClickListener = listener;
    }

    public UserAdapter(Context context, List<UserModel> listUser) {
        this.listUser = listUser;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.users_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem, mClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Picasso.get().load(listUser.get(position).getImage()).into(holder.imageUser);

        holder.firstname.setText(listUser.get(position).getFirstname());
        holder.lastname.setText(listUser.get(position).getLastname());

    }

    @Override
    public int getItemCount() {
        return listUser == null ? 0 : Math.min(listUser.size(), 10);
    }

    public  void updateList(List<UserModel> updatedList){
        listUser = updatedList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageUser;
        public TextView firstname, lastname;

        public ViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            imageUser = itemView.findViewById(R.id.imageUser);

            firstname = itemView.findViewById(R.id.tv_firstname);
            lastname = itemView.findViewById(R.id.tv_lastname);

            itemView.setOnClickListener(view -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION)
                        listener.onItemClick(position);
                }
            });

        }
    }
}