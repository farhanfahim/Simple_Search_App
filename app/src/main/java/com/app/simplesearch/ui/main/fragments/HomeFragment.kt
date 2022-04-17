package com.app.simplesearch.ui.main.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.EditorInfo
import android.widget.AbsListView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.app.simplesearch.ResultModel
import com.app.simplesearch.R
import com.app.simplesearch.adapter.ListingAdapter
import com.app.simplesearch.callback.ActionCallback
import com.app.simplesearch.data.model.Items
import com.app.simplesearch.databinding.FragmentHomeBinding
import com.app.simplesearch.ui.base.BaseFragment
import com.app.simplesearch.ui.events.BaseEvent
import com.app.simplesearch.ui.events.ResultEvent
import com.app.simplesearch.utils.AppConstants
import com.app.simplesearch.utils.onSNACK
import com.app.simplesearch.viewmodel.HomeViewModel
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class HomeFragment : BaseFragment(), ActionCallback {

    private lateinit var binding: FragmentHomeBinding
    lateinit var viewModel: HomeViewModel
    var currentPage = 1
    var totalItem = 0
    lateinit var adapterListing: ListingAdapter
    val params: HashMap<String, Any> = HashMap()
    var arrResult: ArrayList<Items> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val window: Window = requireActivity().window
        window.statusBarColor = ContextCompat.getColor(requireActivity(), R.color.colorRedShade1)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        viewModel.navEvent.observe(this, eventObserver)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.tvSearchData.visibility = View.VISIBLE
        binding.edtSearch.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                if (!binding.edtSearch.text.isNullOrEmpty()) {
                    totalItem = 0
                    currentPage = 1
                    hideKeyboard(requireActivity())
                    arrResult.clear()
                    adapterListing.clearData()
                    adapterListing.notifyDataSetChanged()
                    binding.shimmerLayout.visibility = View.VISIBLE
                    binding.rvResults.visibility = View.GONE
                    binding.tvNoData.visibility = View.GONE
                    binding.tvSearchData.visibility = View.GONE
                    binding.shimmerLayout.startShimmer()
                    params[AppConstants.PARAM_QUERY] = binding.edtSearch.text
                    params[AppConstants.PARAM_PER_PAGE] = 9
                    params[AppConstants.PARAM_CURRENT_PAGE] = currentPage
                    print(params)
                    viewModel.searchApi(params)


                }
            }
            false
        }

        binding.btnclear.setOnClickListener {
            hideKeyboard(requireActivity())
            binding.btnclear.visibility = View.GONE
            binding.edtSearch.setText("")
        }

        binding.edtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if (s!!.length > 0) {
                    binding.btnclear.visibility = View.VISIBLE
                } else {
                    binding.btnclear.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding.rvResults.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                when (newState) {
                    AbsListView.OnScrollListener.SCROLL_STATE_IDLE -> {

                        if (!binding.rvResults.canScrollVertically(1) && !binding.loader.isVisible) {
                            if(totalItem == adapterListing.itemCount){
                                onSNACK(requireView(), requireContext(), "This is the last page of pagination")
                            }else{
                                binding.loader.visibility = View.VISIBLE
                                Handler(Looper.getMainLooper()).postDelayed({
                                    currentPage++
                                    params[AppConstants.PARAM_CURRENT_PAGE] = currentPage
                                    print(params)
                                    viewModel.searchApi(params)
                                }, 3000)
                            }


                        }

                    }
                }
            }
        })
    }

    companion object {
        private const val TAG = "HomeFragment"

        @JvmStatic
        fun newInstance() = HomeFragment()
    }


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        Log.e(TAG, "onCreateView: ")

        adapterListing = ListingAdapter()
        binding = binding<FragmentHomeBinding>(inflater, R.layout.fragment_home, container).apply {
            lifecycleOwner = viewLifecycleOwner
            listingAdapter = adapterListing
            callback = this@HomeFragment
        }



        return binding.root

    }


    override fun onClick(view: View) {
        when (view.id) {

            R.id.btnTryAgain -> {
                binding.layoutNoInternet.visibility = View.GONE

            }
        }
    }


    private val eventObserver = Observer<BaseEvent<ResultEvent>> {
        when (val event = it.getEventIfNotHandled()) {

            is ResultEvent.OnResultData -> {
                if (event.model != null) {
                    totalItem = event.model.totalCount!!
                    binding.tvResultCount.text = "Total Results ${event.model.totalCount}"
                    binding.loader.visibility = View.GONE
                    binding.shimmerLayout.visibility = View.GONE
                    binding.rvResults.visibility = View.VISIBLE
                    binding.shimmerLayout.stopShimmer()
                    if (event.model.totalCount == 0) {
                        binding.tvNoData.visibility = View.VISIBLE
                    } else {
                        binding.tvNoData.visibility = View.GONE
                        arrResult.addAll(event.model.items)
                        adapterListing.setData(arrResult)
                        adapterListing.notifyDataSetChanged()
                    }

                } else {

                    binding.tvNoData.visibility = View.VISIBLE
                    onSNACK(requireView(), requireContext(), "No Record Found")
                }
            }

            is ResultEvent.OnNoInternetAvailable -> {
                binding.shimmerLayout.stopShimmer()
                binding.shimmerLayout.visibility = View.GONE
                binding.loader.visibility = View.GONE
                Log.d("TAG", "${event.noInternetAvailable}")
                binding.layoutNoInternet.visibility = View.VISIBLE
                onSNACK(requireView(), requireContext(), "No Internet")
            }

            is ResultEvent.Error -> {
                binding.shimmerLayout.stopShimmer()
                binding.loader.visibility = View.GONE
                binding.shimmerLayout.visibility = View.GONE
                Log.d("TAG", "${event.error}")
                onSNACK(requireView(), requireContext(), event.error.toString())
            }

            is ResultEvent.Exception -> {
                binding.shimmerLayout.stopShimmer()
                binding.loader.visibility = View.GONE
                binding.shimmerLayout.visibility = View.GONE
                Log.d("TAG", event.exception?.message.toString())
                onSNACK(requireView(), requireContext(), event.exception?.message.toString())
            }
        }
    }


}