package com.example.todaysforecast.ui.help

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.todaysforecast.databinding.HelpFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HelpFragment : Fragment() {

    private lateinit var binding: HelpFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HelpFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.webview.loadUrl("file:///android_asset/tutorial.html")
    }
}