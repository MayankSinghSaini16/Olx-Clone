package com.example.olxapplication.ui.sell.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.olxapplication.R
import com.example.olxapplication.model.CategoriesModel
import de.hdodenhof.circleimageview.CircleImageView

class SellAdapter(var categoriesList: MutableList<CategoriesModel>,
                  var itemClickListener: IemClickListener)
    :RecyclerView.Adapter<SellAdapter.ViewHolder>()
{
    private lateinit var context:Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context= parent.context

        val viewHolder= LayoutInflater.from(parent.context).inflate(R.layout.adapter_sell, parent, false)
        return ViewHolder(viewHolder)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textViewTitle.text = categoriesList.get(position).key
        Glide.with(context)
            .load(categoriesList.get(position).image_bw)
            .placeholder(R.drawable.ic_placeholder)
            .into(holder.imageView)

        holder.itemView.setOnClickListener(View.OnClickListener {
            itemClickListener.onItemClick(position)
        })
    }

    override fun getItemCount():Int {
        return categoriesList.size
    }

    fun updateList(temp: MutableList<CategoriesModel>) {
        categoriesList = temp
        notifyDataSetChanged()

    }

    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        val textViewTitle= itemView.findViewById<TextView>(R.id.tvTitle)!!
        val imageView= itemView.findViewById<ImageView>(R.id.ivIcon)!!
    }

    interface IemClickListener{
        fun onItemClick(position: Int)
    }
}