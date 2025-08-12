package com.dn.woofwell;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderViewHolder> {

    private Context context;
    private List<OrderData> dataList;

    public OrderAdapter(Context context, List<OrderData> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_view, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {

        holder.userName.setText(dataList.get(position).getEmail());
        holder.address.setText(dataList.get(position).getAddress());
        holder.itemCount.setText(dataList.get(position).getItemCount());
        holder.itemName.setText(dataList.get(position).getItemName());
        holder.phone.setText(dataList.get(position).getPhone());
        holder.itemPrice.setText(dataList.get(position).getItemPrice());
        Glide.with(holder.orderImage.getContext())
                .load(dataList.get(position).getImage())
                .placeholder(R.drawable.vector_one)
                .into(holder.orderImage);

        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, OrderDetail.class);

                intent.putExtra("Email", dataList.get(holder.getAdapterPosition()).getEmail());
                intent.putExtra("Title", dataList.get(holder.getAdapterPosition()).getItemName());
                intent.putExtra("Phone", dataList.get(holder.getAdapterPosition()).getPhone());
                intent.putExtra("Address",dataList.get(holder.getAdapterPosition()).getAddress());
                intent.putExtra("ItemCount", dataList.get(holder.getAdapterPosition()).getItemCount());
                intent.putExtra("ItemPrice", dataList.get(holder.getAdapterPosition()).getItemPrice());
                intent.putExtra("Image", dataList.get(holder.getAdapterPosition()).getImage());
                intent.putExtra("FullName", dataList.get(holder.getAdapterPosition()).getFullName());
                intent.putExtra("SellerId", dataList.get(holder.getAdapterPosition()).getSellerId());
                intent.putExtra("Key", dataList.get(holder.getAdapterPosition()).getKey());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void searchDataList(ArrayList<OrderData> searchList){
        dataList = searchList;
        notifyDataSetChanged();
    }
}

class OrderViewHolder extends RecyclerView.ViewHolder{


    ImageView orderImage;
    TextView userName, address,itemName,itemPrice,itemCount,phone,fullname;
    CardView recCard;

    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);

        recCard = itemView.findViewById(R.id.recCard);
        address = itemView.findViewById(R.id.address);
        userName = itemView.findViewById(R.id.userName);
        itemName = itemView.findViewById(R.id.itemName);
        itemPrice = itemView.findViewById(R.id.itemPrice);
        itemCount = itemView.findViewById(R.id.itemCount);
        phone = itemView.findViewById(R.id.phone);
        orderImage = itemView.findViewById(R.id.orderImage);

    }
}