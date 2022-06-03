package com.dicoding.diva.pimpledetectku.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.diva.pimpledetectku.api.AcneItems
import com.dicoding.diva.pimpledetectku.databinding.ItemRowBinding

class ListAcneAdapter(private val listAcnes: ArrayList<AcneItems>) : RecyclerView.Adapter<ListAcneAdapter.ViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRowBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val acne = listAcnes[position]
        holder.bind(acne)
    }

    override fun getItemCount(): Int = listAcnes.size

    inner class ViewHolder(val binding: ItemRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AcneItems){
            binding.apply {
                nameTv.text = item.name
                descriptionTv.text = item.name
                causeTv.text = item.cause
                solutionTv.text = item.solution
            }
        }

//        var imgAcne: ImageView = itemView.findViewById(R.id.imageView)
//        var nameTv: TextView = itemView.findViewById(R.id.name_tv)
//        var desctiptionTv: TextView = itemView.findViewById(R.id.description_tv)
//        var causeTv: TextView = itemView.findViewById(R.id.cause_tv)
//        var solutionTv: TextView = itemView.findViewById(R.id.solution_tv)
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: AcneItems)
    }
}