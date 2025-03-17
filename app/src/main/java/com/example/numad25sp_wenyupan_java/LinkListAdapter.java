package com.example.numad25sp_wenyupan_java;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class LinkListAdapter extends RecyclerView.Adapter<LinkListAdapter.ViewHolder> {

    private List<Link> linkList;
    private OnLinkClickListener listener;

    public interface OnLinkClickListener {
        void onLinkClick(Link link);
        void onLinkDelete(Link link);
        void onLinkEdit(Link link);
    }

    public LinkListAdapter(List<Link> linkList, OnLinkClickListener listener) {
        this.linkList = linkList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_link, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Link link = linkList.get(position);
        holder.nameTextView.setText(link.getName());
        holder.phoneTextView.setText(link.getPhoneNumber());

        holder.itemView.setOnClickListener(v -> listener.onLinkClick(link));
        holder.editButton.setOnClickListener(v -> listener.onLinkEdit(link));
        holder.deleteButton.setOnClickListener(v -> listener.onLinkDelete(link));
    }

    @Override
    public int getItemCount() {
        return linkList.size();
    }

    public void updateList(List<Link> newList) {
        this.linkList = newList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, phoneTextView;
        ImageView deleteButton, editButton;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.textViewTitle);
            phoneTextView = itemView.findViewById(R.id.textViewUrl);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            editButton = itemView.findViewById(R.id.editButton);
        }
    }
}
