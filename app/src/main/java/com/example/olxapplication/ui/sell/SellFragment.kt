package com.example.olxapplication.ui.sell

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.olxapplication.BaseFragment
import com.example.olxapplication.R
import com.example.olxapplication.model.CategoriesModel
import com.example.olxapplication.ui.home.adapter.CategoriesAdapter
import com.example.olxapplication.ui.sell.adapter.SellAdapter
import com.example.olxapplication.utilities.Constants
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_sell.*

class SellFragment : BaseFragment(), CategoriesAdapter.IemClickListener, SellAdapter.IemClickListener {

    val db= FirebaseFirestore.getInstance()
//    private lateinit var db: FirebaseFirestore
    private lateinit var categoriesModel: MutableList<CategoriesModel>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_sell, container, false )

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        getCategoryList()
    }
    private fun getCategoryList() {
        showProgressBar()
        db.collection("Categories").get().addOnSuccessListener {
            hideProgressBar()
            categoriesModel= it.toObjects(CategoriesModel::class.java)
            setAdapter()
        }
    }

    private fun setAdapter() {
        rv_offerings.layoutManager = GridLayoutManager(context, 3)!!
        val sellAdapter = SellAdapter(categoriesModel, this)
        rv_offerings.adapter = sellAdapter
    }

    override fun onItemClick(position: Int) {
       // Toast.makeText(context, "HEY"+position, Toast.LENGTH_SHORT).show()
        var bundle = Bundle()
        bundle.putString(Constants.KEY,categoriesModel.get(position).key)

        findNavController().navigate(R.id.action_sell_to_include_details,bundle)
    }

}