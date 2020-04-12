package com.example.go.githubcontributors.ui.main

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.go.githubcontributors.R
import com.example.go.githubcontributors.data.model.Contributor
import com.example.go.githubcontributors.databinding.MainFragmentBinding
import com.example.go.githubcontributors.di.ViewModelFactory
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class MainFragment : Fragment(), MainEpoxyController.OnClickContributorListener {

    @Inject
    lateinit var factory: ViewModelFactory<MainViewModel>
    private val viewModel: MainViewModel by activityViewModels { factory }

    @Inject
    lateinit var epoxyController: MainEpoxyController

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = MainFragmentBinding.inflate(inflater, container, false)
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.fetchContributors()
        }
        binding.listContributors.apply {
            setController(epoxyController)
        }

        viewModel.contributors.observe(viewLifecycleOwner, Observer {
            epoxyController.setData(it)
            binding.swipeRefreshLayout.isRefreshing = false
        })

        viewModel.onFailureFetchContributors.observe(viewLifecycleOwner, Observer {
            epoxyController.setData(emptyList())
            Toast.makeText(
                requireContext(),
                R.string.fetch_contributors_fail_message,
                Toast.LENGTH_SHORT
            ).show()
            binding.swipeRefreshLayout.isRefreshing = false
        })

        return binding.root
    }

    override fun onClickContributor(contributor: Contributor) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(contributor.htmlUrl))
        startActivity(intent)
    }
}
