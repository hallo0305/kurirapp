package com.example.kurirapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kurirapp.R
import com.example.kurirapp.model.UserResponse

class UserAdapter(private var daftarUser: List<UserResponse>) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewName: TextView = itemView.findViewById(R.id.textViewName)
        val textViewCompany: TextView = itemView.findViewById(R.id.textViewCompany)
        val textViewEmail: TextView = itemView.findViewById(R.id.textViewEmail)
        val textViewPhone: TextView = itemView.findViewById(R.id.textViewPhone)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = daftarUser[position]
        holder.textViewName.text = user.name
        holder.textViewCompany.text = user.company.companyName
        holder.textViewEmail.text = user.email
        holder.textViewPhone.text = user.phone
    }

    override fun getItemCount(): Int = daftarUser.size

    fun perbaruiDaftar(newDaftar: List<UserResponse>) {
        daftarUser = newDaftar
        notifyDataSetChanged()
    }
}
