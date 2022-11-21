package com.example.cce;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
public class myAdapter extends RecyclerView.Adapter<myAdapter.ViewHolder>{
    ArrayList<Model> models;
    Context context;

    public myAdapter(ArrayList<Model> models, Context context) {
        this.models = models;
        this.context = context;
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle;
        TextView txtDate;
        TextView txtTime;


        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            txtTitle= view.findViewById(R.id.txtTitle);
            txtDate= view.findViewById(R.id.txtDate);
            txtTime= view.findViewById(R.id.txtTime);
        }


    }




    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.singlereminder, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.txtTitle.setText(models.get(position).getTitle());
        viewHolder.txtDate.setText(models.get(position).getDate());
        viewHolder.txtTime.setText(models.get(position).getTime());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return models.size();
    }
}
