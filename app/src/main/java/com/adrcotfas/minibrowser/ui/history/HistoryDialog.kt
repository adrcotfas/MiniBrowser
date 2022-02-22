package com.adrcotfas.minibrowser.ui.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProvider;

import com.adrcotfas.minibrowser.cache.UrlEntity;
import com.adrcotfas.minibrowser.databinding.DialogHistoryBinding;
import com.adrcotfas.minibrowser.ui.UrlViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HistoryDialog extends BottomSheetDialogFragment {

    public interface Listener {
        void onClicked(String url);
    }

    private UrlViewModel viewModel;

    private final HistoryAdapter adapter = new HistoryAdapter(new HistoryAdapter.Listener() {
        @Override
        public void onClick(String url) {
            listener.onClicked(url);
            dismiss();
        }
    });

    private Listener listener;

    public static HistoryDialog newInstance(Listener listener) {
        HistoryDialog dialog = new HistoryDialog();
        dialog.listener = listener;
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(UrlViewModel.class);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        com.adrcotfas.minibrowser.databinding.DialogHistoryBinding binding = DialogHistoryBinding.inflate(getLayoutInflater());

        binding.buttonClear.setOnClickListener(
                v -> Executors.newSingleThreadExecutor().execute(() -> viewModel.clear()));

        binding.recycler.setAdapter(adapter);
        viewModel.getUrls().observe(getViewLifecycleOwner(), urlEntities -> {
            List<String> urls = new ArrayList<>();
            for (UrlEntity e : urlEntities) {
                urls.add(e.url);
            }
            adapter.setData(urls);
            adapter.notifyDataSetChanged();
        });

        return binding.getRoot();
    }
}
