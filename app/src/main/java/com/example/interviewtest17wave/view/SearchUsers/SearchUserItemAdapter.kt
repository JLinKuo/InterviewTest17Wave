package com.example.interviewtest17wave.view.SearchUsers

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.interviewtest17wave.databinding.ViewListUserItemBinding
import com.example.interviewtest17wave.model.network.bean.GithubUser

class SearchUserItemAdapter: RecyclerView.Adapter<SearchUserItemAdapter.ViewHolder>() {

    private lateinit var context: Context
    private val listUsers by lazy { ArrayList<GithubUser>() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(ViewListUserItemBinding.inflate(
            LayoutInflater.from(context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context).load(listUsers[position].avatarUrl).into(holder.binding.avatar)
        holder.binding.username.text = listUsers[position].login
    }

    override fun getItemCount() = listUsers.size

    fun updateList(list: List<GithubUser>) {
        val start = list.size
        this.listUsers.addAll(list)
        this.notifyItemRangeChanged(start, listUsers.size)
    }

    fun getUserList() = listUsers

    inner class ViewHolder(val binding: ViewListUserItemBinding): RecyclerView.ViewHolder(binding.root)
}