package com.adrcotfas.minibrowser.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.adrcotfas.minibrowser.cache.UrlEntity
import com.adrcotfas.minibrowser.databinding.DialogHistoryBinding
import com.adrcotfas.minibrowser.ui.UrlViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.Executors

@AndroidEntryPoint
class HistoryDialog : BottomSheetDialogFragment() {
    interface Listener {
        fun onClicked(url: String)
    }

    private lateinit var viewModel: UrlViewModel
    private lateinit var listener: Listener

    private val adapter = HistoryAdapter(object : HistoryAdapter.Listener {
        override fun onClick(url: String) {
            listener.onClicked(url)
            dismiss()
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(UrlViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DialogHistoryBinding.inflate(
            layoutInflater
        )
        binding.buttonClear.setOnClickListener {
            Executors.newSingleThreadExecutor().execute { viewModel.clear() }
        }
        binding.recycler.adapter = adapter
        viewModel.urls.observe(viewLifecycleOwner) { urlEntities: List<UrlEntity> ->
            val urls: MutableList<String> = ArrayList()
            for (e in urlEntities) {
                urls.add(e.url)
            }
            adapter.setData(urls)
            adapter.notifyDataSetChanged()
        }
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(listener: Listener): HistoryDialog {
            val dialog = HistoryDialog()
            dialog.listener = listener
            return dialog
        }
    }
}