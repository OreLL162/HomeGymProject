package com.example.GymApp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.GymApp.Model.SelectedMuscleGroupActivity;
import com.example.GymApp.MuscleGroup;
import com.example.GymApp.R;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private final List<MuscleGroup> data;
    private final Context context;
    private String userId;

    public Adapter(List<MuscleGroup> data, Context context) {
        this.data = data;
        this.context = context;
        this.userId = userId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the current recipe from the data
        MuscleGroup exercise = data.get(position);

        //Set Image
        holder.img.setImageResource(exercise.getImagePath());

        // Set title
        holder.title.setText(exercise.getTitle());

        //Set description
        holder.description.setText(exercise.getDescription());

        // Set OnClickListener to the card
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start SelectedMuscleGroupActivity and pass userId as an extra
                Intent intent = new Intent(context, SelectedMuscleGroupActivity.class);
                intent.putExtra("userId", userId);
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    // When showing all the recipes in a category
    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView title, description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imageViewMuscleGroup);
            title = itemView.findViewById(R.id.muscleGroupTitle);
            description = itemView.findViewById(R.id.textViewDescription);
        }
    }
}
