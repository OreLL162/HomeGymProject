package com.example.GymApp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.GymApp.Exercise;
import com.example.GymApp.R;

import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {

    private Context context;
    private List<Exercise> exercises;

    public ExerciseAdapter(Context context, List<Exercise> exercises) {
        this.context = context;
        this.exercises = exercises;
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.selected_muscle_group_layout, parent, false);
        return new ExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        Exercise exercise = exercises.get(position);
        holder.titleTextView.setText(exercise.getName());
        holder.imageView.setImageResource(exercise.getPhoto());

        // Set the tag for the imageView to the resource ID of the exercise photo
        holder.imageView.setTag(exercise.getPhoto());
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    public static class ExerciseViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleTextView;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.exercisePhoto);
            titleTextView = itemView.findViewById(R.id.ExerciseName);
        }
    }
}
