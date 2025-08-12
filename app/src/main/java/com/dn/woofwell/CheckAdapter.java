package com.dn.woofwell;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class CheckAdapter extends RecyclerView.Adapter<CheckAdapter.CheckViewHolder> {
    private List<Cart> LastList;
    private Context context;

    public CheckAdapter(List<Cart> LastList, Context context) {
        this.LastList = LastList;
        this.context = context;
    }

    @NonNull
    @Override
    public CheckViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.check_rv_items, parent, false);
        return new CheckViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckViewHolder holder, int position) {

        Cart cart = LastList.get(position);
        holder.finalItemName.setText(cart.getItemName());
        holder.finalItemQun.setText(cart.getItemCount());
        holder.finalItemPrice.setText(cart.getItemTotal()+" LKR");
        holder.sellerName.setText(cart.getSellerName());

        String name = cart.getItemName();

        Glide.with(holder.finalCheckImg.getContext())
                .load(cart.getItemImage())
                .placeholder(R.drawable.vector_one)
                .into(holder.finalCheckImg);

    }

    @Override
    public int getItemCount() {
        return LastList.size();
    }

    public static class CheckViewHolder extends RecyclerView.ViewHolder {
        public TextView finalItemName, finalItemQun, finalItemPrice,sellerName;
        public ImageView finalCheckImg;

        public CheckViewHolder(@NonNull View view) {
            super(view);

            finalItemName = view.findViewById(R.id.finalItemName);
            finalItemQun = view.findViewById(R.id.finalItemQun);
            finalItemPrice = view.findViewById(R.id.finalItemPrice);
            finalCheckImg = view.findViewById(R.id.finalCheckImg);
            sellerName= view.findViewById(R.id.sellerName);

        }
    }
}
