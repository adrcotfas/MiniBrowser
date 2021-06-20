package com.adrcotfas.minibrowser.ui.history;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adrcotfas.minibrowser.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    interface Listener {
        void onClick(String url);
    }

    private List<String> data = new ArrayList<>();
    private final Listener listener;

    HistoryAdapter(Listener listener) {
        this.listener = listener;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    @NotNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull HistoryViewHolder holder, int position) {
        holder.bind(data.get(position));
        holder.root.setOnClickListener(v -> listener.onClick(data.get(position)));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {
        public final LinearLayout root;
        private final TextView initial;
        private final TextView url;

        public HistoryViewHolder(@NotNull View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.root);
            initial = itemView.findViewById(R.id.initial);
            url = itemView.findViewById(R.id.url);
        }

        void bind(String data) {
            initial.setText(String.valueOf(data.charAt(0)));
            url.setText(data);
        }
    }
}
