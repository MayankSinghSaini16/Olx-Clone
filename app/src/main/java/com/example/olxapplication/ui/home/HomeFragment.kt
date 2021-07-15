package com.example.olxapplication.ui.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.olxapplication.BaseFragment
import com.example.olxapplication.R
import com.example.olxapplication.databinding.FragmentHomeBinding
import com.example.olxapplication.model.CategoriesModel
import com.example.olxapplication.ui.home.adapter.CategoriesAdapter
import com.example.olxapplication.utilities.Constants
import com.example.olxapplication.utilities.SharedPref
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment(), CategoriesAdapter.IemClickListener {

    private lateinit var categoriesAdapter: CategoriesAdapter
    private lateinit var db:FirebaseFirestore
    private lateinit var categoriesModel: MutableList<CategoriesModel>


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

       val root= inflater.inflate(R.layout.fragment_home, container, false)
        return root
     }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        db= FirebaseFirestore.getInstance();
        tvCityName.text= SharedPref(requireActivity()).getString(Constants.CITY_NAME)

        getCategoryList()

        textListener()

    }

    private fun textListener() {
        edSearch.addTextChangedListener(object :TextWatcher{
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
        var temp:MutableList<CategoriesModel> = ArrayList()
        for (data in categoriesModel){
            if(data.key.contains(toString.capitalize())||data.key.contains(toString)){
                temp.add(data)
            }
        }
        categoriesAdapter.updateList(temp)
    }

    private fun getCategoryList() {
        showProgressBar()
        db.collection("Categories").get().addOnSuccessListener {
            hideProgressBar()
            categoriesModel= it.toObjects(CategoriesModel::class.java)
            setAdapter()
        }
//        db.collection("Categories").get().addOnFailureListener(OnFailureListener {
//            exception ->  Log.d("errror",exception.localizedMessage)
//        })

    }
    private fun setAdapter() {
        rv_categories.layoutManager = GridLayoutManager(context, 3)!!
        categoriesAdapter = CategoriesAdapter(categoriesModel, this)
        rv_categories.adapter = categoriesAdapter
    }

    override fun onItemClick(position: Int) {
        var bundle= Bundle()
        bundle.putString(Constants.KEY, categoriesModel.get(position).key)
        findNavController().navigate(R.id.action_home_to_browse,bundle)
    }

}