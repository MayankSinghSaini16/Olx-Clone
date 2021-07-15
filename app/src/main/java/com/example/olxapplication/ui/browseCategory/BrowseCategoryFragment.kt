package com.example.olxapplication.ui.browseCategory

import android.os.Bundle
import android.provider.SyncStateContract
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.olxapplication.BaseFragment
import com.example.olxapplication.R
import com.example.olxapplication.model.CategoriesModel
import com.example.olxapplication.model.DataItemModel
import com.example.olxapplication.ui.browseCategory.adapter.BrowseCategoryAdapter
import com.example.olxapplication.utilities.Constants
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import io.grpc.internal.JsonUtil.getList
import kotlinx.android.synthetic.main.fragment_home.*

class BrowseCategoryFragment:BaseFragment(), BrowseCategoryAdapter.ItemClickListener {

    private var categoriesAdapter: BrowseCategoryAdapter?=null
    private lateinit var dataItemModel: MutableList<DataItemModel>
    val db= FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView= inflater.inflate(R.layout.fragment_browse,container,false)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getList()

        textListener()

        rv_categories.layoutManager= LinearLayoutManager(context)
    }

    private fun getList() {
        showProgressBar()
        db.collection(arguments?.getString(Constants.KEY)!!)
            .get().addOnSuccessListener {
                hideProgressBar()
                dataItemModel= it.toObjects(DataItemModel::class.java)
                setAdapter()
            }
    }

    private fun setAdapter() {
        categoriesAdapter= BrowseCategoryAdapter(dataItemModel, this)
        rv_categories.adapter= categoriesAdapter
    }

    override fun onItemClick(position: Int) {
        var bundle= Bundle()
        bundle.putString(Constants.DOCUMENT_ID, dataItemModel.get(position).id)
        bundle.putString(Constants.KEY, dataItemModel.get(position).type)
        findNavController().navigate(R.id.action_browse_to_details,bundle)
    }

    private fun textListener() {
        edSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
                filterList(s.toString())
            }
        })
    }

    private fun filterList(toString: String) {
        var temp:MutableList<DataItemModel> = ArrayList()
        for (data in dataItemModel){
            if(data.brand.contains(toString.capitalize())||data.brand.contains(toString)
                ||data.adTitle.contains(toString.capitalize())||data.adTitle.contains(toString)
            ){
                temp.add(data)
            }
        }
        categoriesAdapter?.updateList(temp)
    }

}

