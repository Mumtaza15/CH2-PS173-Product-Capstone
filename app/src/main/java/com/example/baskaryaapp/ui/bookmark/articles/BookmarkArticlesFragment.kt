package com.example.baskaryaapp.ui.bookmark.articles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.baskaryaapp.data.api.ApiConfig
import com.example.baskaryaapp.data.helper.FirebaseHelper
import com.example.baskaryaapp.data.repo.ArticlesRepository
import com.example.baskaryaapp.data.response.ArticlesItem
import com.example.baskaryaapp.databinding.FragmentBookmarkArticlesBinding
import com.example.baskaryaapp.ui.ArticlesViewModelFactory
import com.example.baskaryaapp.ui.articles.ArticlesAdapter
import com.example.baskaryaapp.ui.articles.ArticlesViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class BookmarkArticlesFragment : Fragment()  {

    private lateinit var binding: FragmentBookmarkArticlesBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookmarkArticlesBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val firebaseHelper = FirebaseHelper()
    private lateinit var articleList: MutableList<ArticlesItem>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repository = ArticlesRepository.getInstance(ApiConfig.apiService)
        val factory = ArticlesViewModelFactory.getInstance(repository)
        val articlesViewModel = ViewModelProvider(this, factory)[ArticlesViewModel::class.java]

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvBookmarkArticles.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding.rvBookmarkArticles.addItemDecoration(itemDecoration)

        articleList = mutableListOf()

        val auth: FirebaseAuth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid

//        articlesViewModel.listArticles.observe(requireActivity()) { listArticles ->
//            setArticlesData(listArticles)
//        }

        lifecycleScope.launch {
            while (lifecycleScope.coroutineContext.isActive) {
                delay(5000)
                if (isAdded() && userId != null) {
                    // Dapatkan data bookmark
                    firebaseHelper.getBookmarkedArticles(userId) { bookmarkedIds ->
                        if (isAdded()) {
                            // Set data article
                            articlesViewModel.listArticles.observe(requireActivity()) { listArticles ->
                                setArticlesData(listArticles, bookmarkedIds)
                            }
                        }
                    }
                }
            }
        }

        articlesViewModel.isLoading.observe(requireActivity()) { loading ->
            showLoading(loading)
        }
    }


    private fun setArticlesData(items: List<ArticlesItem>, bookmarkedIds: List<String?>) {
        val adapter = ArticlesAdapter()

        // Filter batik yang telah di-bookmark berdasarkan daftar bookmarkedIds
        val bookmarkedArticles = items.filter { batik ->
            bookmarkedIds.contains(batik.id)
        }

        // Set status bookmark pada semua batik
        val itemsWithBookmarkStatus = bookmarkedArticles.map { articles ->
            articles.copy(isBookmarked = bookmarkedIds.contains(articles.id))
        }
        adapter.submitList(itemsWithBookmarkStatus)

        binding.rvBookmarkArticles.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}