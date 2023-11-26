package com.example.orders;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
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

    private final ItemTouchHelper.SimpleCallback swipeToDelete = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            Order order = cardItemList.get(position);
            MainActivity.dataBaseWorker.deleteOrders(false, String.valueOf(order.ID));
            cardItemList.remove(position);
            notifyItemRemoved(position);
        }

        @Override
        public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
            super.onSelectedChanged(viewHolder, actionState);
            if (viewHolder != null) {
                ConstraintLayout layout = viewHolder.itemView.findViewById(R.id.cardLayout);
                layout.setBackgroundColor(Color.rgb(245,54,38));
            }
        }

        @Override
        public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            ConstraintLayout layout = viewHolder.itemView.findViewById(R.id.cardLayout);
            int releasedBackgroundColor = ContextCompat.getColor(viewHolder.itemView.getContext(), R.color.bg);
            layout.setBackgroundColor(releasedBackgroundColor);
        }
    };

    private final ItemTouchHelper.SimpleCallback swipeToEdit = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return true;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            AddOrder.order = cardItemList.get(position);
            Intent intent = new Intent(viewHolder.itemView.getContext(), AddOrder.class);
            viewHolder.itemView.getContext().startActivity(intent);
            notifyItemChanged(position);
        }

        @Override
        public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
            super.onSelectedChanged(viewHolder, actionState);
            if (viewHolder != null) {
                ConstraintLayout layout = viewHolder.itemView.findViewById(R.id.cardLayout);
                layout.setBackgroundColor(Color.rgb(33,190,219));
            }
        }

        @Override
        public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            ConstraintLayout layout = viewHolder.itemView.findViewById(R.id.cardLayout);
            int releasedBackgroundColor = ContextCompat.getColor(viewHolder.itemView.getContext(), R.color.bg);
            layout.setBackgroundColor(releasedBackgroundColor);
        }
    };

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        ItemTouchHelper itemTouchHelperLeft = new ItemTouchHelper(swipeToDelete);
        ItemTouchHelper itemTouchHelperRight = new ItemTouchHelper(swipeToEdit);
        itemTouchHelperLeft.attachToRecyclerView(recyclerView);
        itemTouchHelperRight.attachToRecyclerView(recyclerView);
    }

}
