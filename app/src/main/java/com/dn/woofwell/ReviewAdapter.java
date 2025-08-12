package com.dn.woofwell;

import static java.lang.Math.log;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private Context context;
    private List<DataClass> dataList;

    public ReviewAdapter(Context context, List<DataClass> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        DataClass data = dataList.get(position);
        holder.name.setText(data.getName());
        holder.comment.setText(data.getReview());

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView name, comment;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            // Ensure that the correct IDs are used for the TextViews
            name = itemView.findViewById(R.id.name);
            comment = itemView.findViewById(R.id.comment);
        }
    }
}
