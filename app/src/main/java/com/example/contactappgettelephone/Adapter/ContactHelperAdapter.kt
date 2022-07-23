package com.example.contactappgettelephone.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.contactappgettelephone.CLASS.Contact
import com.example.contactappgettelephone.databinding.ItemRvBinding

class ContactHelperAdapter(
    var list: ArrayList<Contact>,
    var onItemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<ContactHelperAdapter.Vh>() {

    inner class Vh(var itemRvBinding: ItemRvBinding) : RecyclerView.ViewHolder(itemRvBinding.root) {

        fun onBind(contact: Contact, position: Int) {

            itemRvBinding.tv1.text = contact.number
            itemRvBinding.tv2.text = contact.name


            itemRvBinding.callId.setOnClickListener {
                onItemClickListener.onPhoneClick(contact)
            }

            itemRvBinding.messageId.setOnClickListener {
                onItemClickListener.onMessageClick(contact)
            }


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {

        return Vh(
            ItemRvBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: Vh, position: Int) {

        holder.onBind(list[position], position)


    }

    override fun getItemCount(): Int {

        return list.size

    }

    interface OnItemClickListener {
        fun onPhoneClick(contactHelper: Contact)
        fun onMessageClick(contactHelper: Contact)
    }

}