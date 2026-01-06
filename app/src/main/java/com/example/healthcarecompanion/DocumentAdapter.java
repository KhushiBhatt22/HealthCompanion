package com.example.healthcarecompanion;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

public class DocumentAdapter
        extends RecyclerView.Adapter<DocumentAdapter.DocViewHolder> {

    private final List<MedicineRecord> documents;
    private final Context context;

    public DocumentAdapter(Context ctx, List<MedicineRecord> docs) {
        this.context   = ctx;
        this.documents = docs;
    }

    @NonNull
    @Override
    public DocViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_document, parent, false);
        return new DocViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DocViewHolder holder, int pos) {
        MedicineRecord rec = documents.get(pos);
        String path = rec.getFilePath();
        holder.txtName.setText(rec.getFileName());

        File file = new File(path);
        // simple image check by extension
        if (path.matches(".*\\.(?i)(png|jpe?g|gif|bmp)")) {
            // load thumbnail
            Bitmap thumb = BitmapFactory.decodeFile(path);
            holder.imgThumb.setImageBitmap(
                    Bitmap.createScaledBitmap(thumb, 48, 48, true)
            );

            // click → in-app full-screen preview
            holder.itemView.setOnClickListener(v -> {
                Intent i = new Intent(context, DocumentViewActivity.class);
                i.putExtra("filePath", path);
                context.startActivity(i);
            });
        } else {
            // generic icon for non-images
            holder.imgThumb.setImageResource(R.drawable.ic_document);
            // click → external viewer
            holder.itemView.setOnClickListener(v -> {
                Uri uri = Uri.fromFile(file);
                Intent i = new Intent(Intent.ACTION_VIEW)
                        .setDataAndType(uri, context.getContentResolver().getType(uri))
                        .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                context.startActivity(i);
            });
        }
    }

    @Override
    public int getItemCount() {
        return documents.size();
    }

    static class DocViewHolder extends RecyclerView.ViewHolder {
        ImageView imgThumb;
        TextView  txtName;
        DocViewHolder(View itemView) {
            super(itemView);
            imgThumb = itemView.findViewById(R.id.imgThumbnail);
            txtName  = itemView.findViewById(R.id.txtDocName);
        }
    }
}
