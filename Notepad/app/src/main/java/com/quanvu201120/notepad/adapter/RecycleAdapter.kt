package com.quanvu201120.notepad.adapter

import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.quanvu201120.notepad.R
import com.quanvu201120.notepad.model.NoteP

interface iItemRecycleClick{
    fun onItemClick(note : NoteP)
}

class RecycleAdapter(var list : ArrayList<NoteP>, var iItemRecycleClick: iItemRecycleClick) : RecyclerView.Adapter<RecycleAdapter.NotePHolder>() {

    inner class NotePHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnCreateContextMenuListener {
        lateinit var tv_title : TextView
        lateinit var tv_time : TextView
        lateinit var tv_content : TextView
        init {
            tv_title = itemView.findViewById(R.id.tv_title_item)
            tv_time = itemView.findViewById(R.id.tv_time_item)
            tv_content = itemView.findViewById(R.id.tv_content_item)

            itemView.setOnClickListener {

                iItemRecycleClick.onItemClick(list.get(adapterPosition))

            }

            itemView.setOnCreateContextMenuListener(this)


        }

        override fun onCreateContextMenu(
            p0: ContextMenu?,
            p1: View?,
            p2: ContextMenu.ContextMenuInfo?
        ) {
            p0?.add(adapterPosition,0,0,"Xóa ghi chú")
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotePHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_recycle_main,parent,false)
        return NotePHolder(view)
    }

    override fun onBindViewHolder(holder: NotePHolder, position: Int) {

        var notep : NoteP = list.get(position)

        holder.tv_content.text = notep.content
        holder.tv_title.text = notep.title
        holder.tv_time.text = notep.time


    }

    override fun getItemCount(): Int {
        return list.size
    }


}