package com.example.healthcarecompanion;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FolderAdapter
        extends RecyclerView.Adapter<FolderAdapter.FolderViewHolder> {

    public interface OnFolderClickListener {
        void onFolderClick(String folderName);
    }

    private final List<String> folders;
    private final OnFolderClickListener listener;

    public FolderAdapter(List<String> folders, OnFolderClickListener listener) {
        this.folders = folders;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FolderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_folder, parent, false);
        return new FolderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FolderViewHolder holder, int position) {
        String name = folders.get(position);
        holder.txtFolderName.setText(name);
        holder.itemView.setOnClickListener(v -> listener.onFolderClick(name));
    }

    @Override
    public int getItemCount() {
        return folders.size();
    }

    static class FolderViewHolder extends RecyclerView.ViewHolder {
        TextView txtFolderName;
        FolderViewHolder(View itemView) {
            super(itemView);
            txtFolderName = itemView.findViewById(R.id.txtFolderName);
        }
    }
}
