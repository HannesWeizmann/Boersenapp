package com.example.boersenapp.ui.favoriten

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.boersenapp.databinding.FragmentFavoritenBinding

class FavoritenFragment : Fragment() {

    private var _binding: FragmentFavoritenBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val favoritenViewModel =
            ViewModelProvider(this).get(FavoritenViewModel::class.java)

        _binding = FragmentFavoritenBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textFavoriten
        favoritenViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}