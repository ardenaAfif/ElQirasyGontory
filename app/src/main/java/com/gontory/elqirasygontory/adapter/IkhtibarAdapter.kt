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
import com.gontory.elqirasygontory.ui.quiz.IkhtibarFragmentDirections

class IkhtibarAdapter(private val context: Context):
    RecyclerView.Adapter<IkhtibarAdapter.IkhtibarViewHolder>() {

    inner class IkhtibarViewHolder(private val binding: ItemMutholaahBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(ikhtibar: Mutholaah) {
                binding.apply {
                    if (ikhtibar.img_url.isNotEmpty()) {
                        Glide.with(itemView)
                            .load(ikhtibar.img_url)
                            .into(ivMutholaah)
                    } else {
                        ivMutholaah.setImageResource(R.drawable.al_hariqu)
                    }

                    tvTitleMutholaah.text = ikhtibar.title
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IkhtibarViewHolder {
        return IkhtibarViewHolder(
            ItemMutholaahBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: IkhtibarViewHolder, position: Int) {
        val mutholaah = differ.currentList[position]
        holder.bind(mutholaah)

        holder.itemView.setOnClickListener {
            val action = IkhtibarFragmentDirections.actionIkhtibarFragmentToQuizFragment(mutholaah)
            holder.itemView.findNavController().navigate(action)
//            Toast.makeText(holder.itemView.context, "Fitur ini akan segera hadir!", Toast.LENGTH_SHORT).show()
        }
    }
}