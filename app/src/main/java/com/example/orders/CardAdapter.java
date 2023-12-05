package com.example.orders;
import static com.example.orders.R.string.cancel;
import static com.example.orders.R.string.error;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
            // Сохранить удаленный элемент
            Order order = cardItemList.get(position);
            new AlertDialog.Builder(viewHolder.itemView.getContext())
                    .setTitle(R.string.deletion)
                    .setMessage(viewHolder.itemView.getContext().getString(R.string.delete_message) + " №" + order.Number +" ?")
                    .setPositiveButton(R.string.delete, (dialog, which) -> {
                        MainActivity.dataBaseWorker.deleteOrders(false, String.valueOf(order.ID));
                        cardItemList.remove(position);
                        notifyItemRemoved(position);
                    })
                    .setNegativeButton(R.string.cancel, (dialog, which) -> {
                        Toast.makeText(viewHolder.itemView.getContext(), cancel,
                                Toast.LENGTH_SHORT).show();
                        Order temp = new Order(order.ID, order.Number, order.Date, order.IsSigned);
                        cardItemList.remove(position);
                        notifyItemRemoved(position);
                        cardItemList.add(position, temp);
                        notifyItemInserted(position);
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
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
