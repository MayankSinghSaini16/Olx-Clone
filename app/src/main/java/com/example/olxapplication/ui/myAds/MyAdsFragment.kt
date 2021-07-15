package com.example.olxapplication.ui.myAds

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.olxapplication.BaseFragment
import com.example.olxapplication.R
import com.example.olxapplication.databinding.FragmentMyAdsBinding
import com.example.olxapplication.model.DataItemModel
import com.example.olxapplication.ui.myAds.adapter.MyAdsAdapter
import com.example.olxapplication.utilities.Constants
import com.example.olxapplication.utilities.SharedPref
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.adapter_myads.*
import kotlinx.android.synthetic.main.fragment_my_ads.*
import kotlinx.android.synthetic.main.fragment_upload_photo.*

class MyAdsFragment: BaseFragment(), MyAdsAdapter.ItemClickListener, View.OnClickListener {

    private lateinit var myAdsAdapter: MyAdsAdapter
    private var documentIdList: MutableList<DataItemModel> = ArrayList()
    private lateinit var dataItemModel: MutableList<DataItemModel>
    private lateinit var binding: FragmentMyAdsBinding
    val db = FirebaseFirestore.getInstance()
    var documentCount=0
    var count=0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_my_ads, container, false)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getMyAdsList()
        rv_ads.layoutManager = LinearLayoutManager(context)

    }

    private fun getMyAdsList() {
        showProgressBar()
        db.collection(Constants.CATEGORIES)
            .get().addOnSuccessListener {result->
                documentIdList= ArrayList()
                count=0;
                documentCount =result.documents.size
                for (i in result.documents) {
                    getDataFromKeys(i.getString("image")!!)

                }
            }
    }

    private fun getDataFromKeys(key: String) {
        db.collection(key)
            .whereEqualTo("userId", SharedPref(requireActivity()).getString(Constants.USER_ID))
            .get().addOnSuccessListener { result ->
                hideProgressBar()
                count++
                dataItemModel = result.toObjects(DataItemModel::class.java)
                documentIdList.addAll(dataItemModel)
                setAdapter()
                if (count==documentCount&&documentIdList.size>0){
                    ll_no_data.visibility=View.GONE
                    setAdapter()
                }else{
                    ll_no_data.visibility=View.VISIBLE
                }//is it working? it crashed


            }
            .addOnFailureListener {
                Toast.makeText(requireActivity(), "Error:$it",Toast.LENGTH_LONG).show()

            }


    }


    private fun setAdapter() {
        myAdsAdapter =
            MyAdsAdapter(documentIdList, this)
        if (rv_ads != null)
            rv_ads.adapter = myAdsAdapter

    }


    private fun listener() {
        buttonUpload.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {

            R.id.buttonUpload -> {
                findNavController().navigate(R.id.action_sell_to_include_details)

            }
        }
    }

    override fun onItemClick(position: Int) {
        var bundle = Bundle()
        bundle.putString(Constants.DOCUMENT_ID, documentIdList.get(position).id)
        bundle.putString(Constants.KEY, documentIdList.get(position).type)
        findNavController().navigate(R.id.action_my_ads_to_detail, bundle)
    }

}