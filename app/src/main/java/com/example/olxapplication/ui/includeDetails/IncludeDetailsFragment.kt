package com.example.olxapplication.ui.includeDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.olxapplication.BaseFragment
import com.example.olxapplication.R
import com.example.olxapplication.utilities.Constants
import kotlinx.android.synthetic.main.fragment_include_details.*

class IncludeDetailsFragment:BaseFragment(), View.OnClickListener {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView= inflater.inflate(R.layout.fragment_include_details, container, false)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        listener()

        if(arguments?.getString(Constants.KEY)!!.equals(Constants.CAR))
            edUsage.visibility= View.VISIBLE

        if(arguments?.getString(Constants.KEY)!!.equals(Constants.BIKE))
            edUsage.visibility= View.VISIBLE
    }

    private fun listener() {
        textViewNext.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id)
        {
            R.id.textViewNext->{
                sendData()
            }
        }
    }

    private fun sendData() {
        if(edBrand.text?.isEmpty()!!)
        {
            edBrand.setError(getString(R.string.enter_brand_name))
        }
        else if(edPhone.text?.isEmpty()!!)
        {
            edBrand.setError(getString(R.string.enter_phone_number))
        }
        else if(edPrice.text?.isEmpty()!!)
        {
            edBrand.setError(getString(R.string.enter_price))
        }
        else if(edDescription.text?.isEmpty()!!)
        {
            edBrand.setError(getString(R.string.enter_description))
        }
        else if(edYear.text?.isEmpty()!!)
        {
            edBrand.setError(getString(R.string.enter_year))
        }
        else
        {
            val bundle= Bundle()
            bundle.putString(Constants.BRAND,edBrand.text.toString())
            bundle.putString(Constants.YEAR,edYear.text.toString())
            bundle.putString(Constants.AD_TITLE,edAdTitle.text.toString())
            bundle.putString(Constants.AD_DESCRIPTION,edDescription.text.toString())
            bundle.putString(Constants.ADDRESS,edAddress.text.toString())
            bundle.putString(Constants.PHONE,edPhone.text.toString())
            bundle.putString(Constants.PRICE,edPrice.text.toString())
            bundle.putString(Constants.KM_DRIVEN,edUsage.text.toString())
            bundle.putString(Constants.KEY,arguments?.getString(Constants.KEY))
            findNavController().navigate(R.id.action_include_details_to_uploadPhoto,bundle)
        }
    }
}