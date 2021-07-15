package com.example.olxapplication

import android.annotation.SuppressLint
import android.app.Dialog
import android.view.Window
import androidx.fragment.app.Fragment

open class BaseFragment: Fragment() {

    lateinit var mDialg:Dialog

    @SuppressLint("UseRequireInsteadOfGet")
    open fun showProgressBar()
    {
        mDialg= Dialog(activity!!)
//        course used activity!! here
//        exclamation marks indicate not null
        mDialg.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mDialg.setContentView(R.layout.dialogue_progressbar)
        mDialg.setCancelable(true)
        mDialg.show()
    }

    open fun hideProgressBar()
    {
        mDialg.dismiss()
    }

}