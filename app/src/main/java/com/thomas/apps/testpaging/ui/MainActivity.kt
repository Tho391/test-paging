package com.thomas.apps.testpaging.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.thomas.apps.testpaging.adapter.LoadStateAdapter
import com.thomas.apps.testpaging.adapter.OfficeAdapter
import com.thomas.apps.testpaging.api.OfficeService
import com.thomas.apps.testpaging.data.OfficeRepository
import com.thomas.apps.testpaging.databinding.ActivityMainBinding
import com.thomas.apps.testpaging.db.AppDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    private var searchJob: Job? = null
    private val officeAdapter by lazy {
        OfficeAdapter().apply {
            withLoadStateFooter(LoadStateAdapter { this.retry() })
            addLoadStateListener { loadState ->
                // Only show the list if refresh succeeds.
                binding.recyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
                // Show loading spinner during initial load or refresh.
                binding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                // Show the retry state if initial load or refresh fails.
                binding.retryButton.isVisible = loadState.source.refresh is LoadState.Error

                // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
                val errorState = loadState.source.append as? LoadState.Error
                    ?: loadState.source.prepend as? LoadState.Error
                    ?: loadState.append as? LoadState.Error
                    ?: loadState.prepend as? LoadState.Error
                errorState?.let {
                    Toast.makeText(
                        this@MainActivity,
                        "\uD83D\uDE28 Wooops ${it.error}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = OfficeRepository(OfficeService.create(), AppDatabase.getInstance(this))
        viewModel =
            ViewModelProvider(this, MainViewModelFactory(repository)).get(MainViewModel::class.java)

        setUpRecyclerView()

        getOffices()

        // Scroll to top when the list is refreshed from network.
        lifecycleScope.launch {
            officeAdapter.loadStateFlow
                // Only emit when REFRESH LoadState for RemoteMediator changes.
                .distinctUntilChangedBy { it.refresh }
                // Only react to cases where Remote REFRESH completes i.e., NotLoading.
                .filter { it.refresh is LoadState.NotLoading }
                .collect { binding.recyclerView.smoothScrollToPosition(0) }
        }

        binding.retryButton.setOnClickListener { officeAdapter.retry() }
    }

    private fun getOffices() {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.getOffices().collectLatest {
                officeAdapter.submitData(it)
            }
        }
    }

    private fun setUpRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            addItemDecoration(
                DividerItemDecoration(
                    this@MainActivity,
                    DividerItemDecoration.VERTICAL
                )
            )
            adapter = officeAdapter
        }
    }
}