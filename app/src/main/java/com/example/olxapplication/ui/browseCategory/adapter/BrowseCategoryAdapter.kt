package com.example.olxapplication.ui.browseCategory.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.olxapplication.R
import com.example.olxapplication.model.DataItemModel
import com.example.olxapplication.utilities.Constants.CURRENCY_SYMBOL
import java.text.SimpleDateFormat

class BrowseCategoryAdapter (
    var dataItemModel: MutableList<DataItemModel>,
    var mClickListener: ItemClickListener
)
    :RecyclerView.Adapter<BrowseCategoryAdapter.ViewHolder>()
{
    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context

        val viewHolder =
            LayoutInflater.from(parent.context).inflate(R.layout.adapter_myads, parent, false)
        return ViewHolder(viewHolder)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textViewPrice.setText(CURRENCY_SYMBOL + dataItemModel.get(position).price)
        holder.textViewBrand.setText(dataItemModel.get(position).brand)
        holder.textViewAddress.setText(dataItemModel.get(position).address)
        Glide.with(context)
            .load(dataItemModel.get(position).images.get(0))
            .placeholder(R.drawable.ic_placeholder)
            .into(holder.imageView)

        val sdf = SimpleDateFormat("dd/mm/yyyy")
        val formattedDate = sdf.format(dataItemModel[position].createdDate?.time)
        holder.textViewDate.setText(formattedDate)

        holder.itemView.setOnClickListener(View.OnClickListener {
            mClickListener.onItemClick(position)
        })
    }

        override fun getItemCount():Int {
            return dataItemModel.size
        }

    fun updateList(temp: MutableList<DataItemModel>) {
        dataItemModel = temp
        notifyDataSetChanged()

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewPrice = itemView.findViewById<TextView>(R.id.my_tvPrice)!!
        val textViewBrand = itemView.findViewById<TextView>(R.id.my_ad_tvBrand)!!
        val textViewAddress = itemView.findViewById<TextView>(R.id.my_ad_tvAddress)!!
        val textViewDate = itemView.findViewById<TextView>(R.id.my_ad_tvDate)!!
        val imageView = itemView.findViewById<ImageView>(R.id.my_ad_imageView)!!
    }

    interface ItemClickListener {
        fun onItemClick(position: Int)
    }
}


