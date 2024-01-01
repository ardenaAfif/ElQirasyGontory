package com.gontory.elqirasygontory.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gontory.elqirasygontory.R
import com.gontory.elqirasygontory.data.Mutholaah
import com.gontory.elqirasygontory.databinding.ItemMutholaahBinding
import com.gontory.elqirasygontory.ui.mutholaah.MutholaahFragmentDirections

class MutholaahAdapter(private val context: Context) :
    RecyclerView.Adapter<MutholaahAdapter.MutholaahViewHolder>() {

    inner class MutholaahViewHolder(private val binding: ItemMutholaahBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(mutholaah: Mutholaah) {
            binding.apply {
                if (mutholaah.img_url.isNotEmpty()) {
                    Glide.with(itemView)
                        .load(mutholaah.img_url)
                        .into(ivMutholaah)
                } else {
                    ivMutholaah.setImageResource(R.drawable.al_hariqu)
                }

                tvTitleMutholaah.text = mutholaah.title
            }
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<Mutholaah>() {
        override fun areItemsTheSame(oldItem: Mutholaah, newItem: Mutholaah): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Mutholaah, newItem: Mutholaah): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MutholaahViewHolder {
        return MutholaahViewHolder(
            ItemMutholaahBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: MutholaahViewHolder, position: Int) {
        val mutholaah = differ.currentList[position]
        holder.bind(mutholaah)

        holder.itemView.setOnClickListener {
            val action = MutholaahFragmentDirections.actionMutholaahFragmentToMateriActivity(mutholaah)
            holder.itemView.findNavController().navigate(action)
        }
    }
}