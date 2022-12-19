package uz.yusufbekibragimov.testapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.yusufbekibragimov.testapp.databinding.ItemDesignBinding

/**
 * Created by Ibragimov Yusufbek
 * Date: 12/17/2022
 * Project: Test App
 **/

class HomeAdapter : RecyclerView.Adapter<HomeAdapter.RecViewHolder>() {

    inner class RecViewHolder(var itemDesignBinding: ItemDesignBinding) :
        RecyclerView.ViewHolder(itemDesignBinding.root) {
        fun bindData() {
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecViewHolder {
        val binding = ItemDesignBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecViewHolder, position: Int) {
        holder.bindData()
    }

    override fun getItemCount(): Int {
        return 30
    }

    fun refresh() {
        notifyDataSetChanged()
    }

}