package com.example.videolist.ui.main.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.videolist.R
import com.example.videolist.ViewModelProviderFactory
import com.example.videolist.adapters.RecyclerViewAdapter
import com.example.videolist.databinding.FragmentListBinding
import com.example.videolist.utils.Constants
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class ListFragment : DaggerFragment(), OnItemClickListener {

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    @Inject
    lateinit var adapter : RecyclerViewAdapter

    private lateinit var viewModel: ListViewModel

    private var _binding: FragmentListBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this, providerFactory).get(ListViewModel::class.java)
        initRecyclerView()
        subscribeObservers()
}

    private fun initRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.adapter = adapter
        //Applying listener to recyclerView adapter
        adapter.listener = this
    }

    private fun subscribeObservers() {
        viewModel.videosObservable.removeObservers(viewLifecycleOwner)
        //Observe for videos in db
        viewModel.videosObservable.observe(viewLifecycleOwner) {
            if(it.isNotEmpty()) {
                hideProgressBar()
            }
            adapter.setVideos(it)
        }
        //Load videos if they aren't
        viewModel.loadVideos(Constants.VIDEOS, requireActivity().filesDir)
        //Check for errors
        viewModel.errorMessages.observe(viewLifecycleOwner) {
            makeToast(it)
        }
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }

    private fun makeToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

    //Listening to recyclerView
    override fun onClick(adapterPosition: Int) {
        moveToVideo(adapterPosition)
    }

    //Move to video fragment. Pass clicked video
    private fun moveToVideo(videoId: Int) {
        val bundle = bundleOf("VideoId" to videoId)
        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(R.id.action_listFragment_to_videoFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
//Interface for recyclerViewAdapter
interface OnItemClickListener {
    fun onClick(adapterPosition: Int)
}