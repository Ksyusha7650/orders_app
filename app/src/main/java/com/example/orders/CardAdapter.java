package com.example.orders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {
    private final List<Order> cardItemList;
    public CardAdapter(List<Order> cardItemList) {
        this.cardItemList = cardItemList;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        Order order = cardItemList.get(position);
        if (Objects.equals(order.Number, "")) {
            holder.imageView.setVisibility(View.INVISIBLE);
            holder.cbCardBool.setVisibility(View.INVISIBLE);
        }
        holder.tvCardTitle.setText(order.Number);
        holder.tvCardDate.setText(order.Date);
        holder.cbCardBool.setChecked(order.IsSigned);
    }

    @Override
    public int getItemCount() {
        return cardItemList.size();
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvCardTitle, tvCardDate;
        private final CheckBox cbCardBool;
        private final ImageView imageView;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCardTitle = itemView.findViewById(R.id.textViewNumberOrder);
            tvCardDate = itemView.findViewById(R.id.textViewDateOrder);
            cbCardBool = itemView.findViewById(R.id.checkBoxIsSigned);
            imageView = itemView.findViewById(R.id.imageViewDoc);
        }
    }
}
