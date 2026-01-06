package com.example.healthcarecompanion;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private final List<ReminderRecord> tasks;

    public TaskAdapter(List<ReminderRecord> tasks) {
        this.tasks = tasks;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        ReminderRecord r = tasks.get(position);
        holder.txtTitle.setText(r.getMedicineName() + ", " + r.getDosage());
        holder.txtDetail.setText(r.getStatus() + " at " + r.getTime());

        // Show a check if taken, or pending icon otherwise
        int iconRes = r.getStatus().equals("Taken")
                ? R.drawable.ic_check_circle
                : R.drawable.ic_pending;
        holder.imgStatus.setImageResource(iconRes);
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle, txtDetail;
        ImageView imgStatus;

        TaskViewHolder(View v) {
            super(v);
            txtTitle = v.findViewById(R.id.txtTaskTitle);
            txtDetail = v.findViewById(R.id.txtTaskDetail);
            imgStatus = v.findViewById(R.id.imgStatus);  // new binding
        }
    }
}


