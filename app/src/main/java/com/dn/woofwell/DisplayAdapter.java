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

public class DisplayAdapter extends RecyclerView.Adapter<DisplayViewHolder> {
    private Context context;
    private List<DataClass> dataList;

    public DisplayAdapter(Context context, List<DataClass> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public DisplayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_display, parent, false);
        return new DisplayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DisplayViewHolder holder, int position) {
        DataClass data = dataList.get(position);

        Glide.with(context).load(data.getImage()).into(holder.proImage);
        holder.proTitle.setText(data.getTitle());


        holder.proPrice.setText(data.getPrice());
        holder.sellerName.setText(data.getSellerName());

        holder.proCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Detail.class);
                intent.putExtra("Image", dataList.get(holder.getAdapterPosition()).getImage());
                intent.putExtra("Description", dataList.get(holder.getAdapterPosition()).getDescription());
                intent.putExtra("Title", dataList.get(holder.getAdapterPosition()).getTitle());
                intent.putExtra("Youtube", dataList.get(holder.getAdapterPosition()).getYoutube());
                intent.putExtra("Age", dataList.get(holder.getAdapterPosition()).getAge());
                intent.putExtra("SellerName", dataList.get(holder.getAdapterPosition()).getSellerName());
                intent.putExtra("SellerId", dataList.get(holder.getAdapterPosition()).getSellerId());
                intent.putExtra("Brand", dataList.get(holder.getAdapterPosition()).getBrand());
                intent.putExtra("Type", dataList.get(holder.getAdapterPosition()).getType());
                intent.putExtra("Key",dataList.get(holder.getAdapterPosition()).getKey());
                intent.putExtra("Price", dataList.get(holder.getAdapterPosition()).getPrice());
                intent.putExtra("Key", dataList.get(holder.getAdapterPosition()).getKey());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void searchDataList(ArrayList<DataClass> searchList) {
        dataList = searchList;
        notifyDataSetChanged();
    }
}

class DisplayViewHolder extends RecyclerView.ViewHolder {
    CardView proCard;
    ImageView proImage;
    TextView proTitle, proRates, sellerName, proPrice;

    public DisplayViewHolder(@NonNull View itemView) {
        super(itemView);

        proCard = itemView.findViewById(R.id.proCard);
        proImage = itemView.findViewById(R.id.proImage);
        proTitle = itemView.findViewById(R.id.proTitle);
        proRates = itemView.findViewById(R.id.proRates);
        sellerName = itemView.findViewById(R.id.sellerName);
        proPrice = itemView.findViewById(R.id.proPrice);
    }
}
