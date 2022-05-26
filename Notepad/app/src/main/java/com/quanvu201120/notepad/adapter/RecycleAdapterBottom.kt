package com.quanvu201120.notepad.adapter

import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.quanvu201120.notepad.R
import com.quanvu201120.notepad.model.NoteP

interface IitemBottomClick{
    fun itemBClick(note : NoteP)
}

class RecycleAdapterBottom(var arrayList: ArrayList<NoteP>,var iitemBottomClick: IitemBottomClick) : RecyclerView.Adapter<RecycleAdapterBottom.ItemHolder>() {

    inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnCreateContextMenuListener {

        var tv_title : TextView
        var tv_content : TextView
        var tv_time : TextView
        init {
            tv_title = itemView.findViewById(R.id.tv_title_item_bottom1)
            tv_content = itemView.findViewById(R.id.tv_content_item_bottom1)
            tv_time = itemView.findViewById(R.id.tv_time_item_bottom1)

            itemView.setOnClickListener {
                iitemBottomClick.itemBClick(arrayList.get(adapterPosition))
            }

            itemView.setOnCreateContextMenuListener(this)
        }

        override fun onCreateContextMenu(
            p0: ContextMenu?,
            p1: View?,
            p2: ContextMenu.ContextMenuInfo?,
        ) {
            p0?.add(adapterPosition,0,0,"Sửa")
            p0?.add(adapterPosition,1,1,"Xóa")

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_recycle_bottom1,parent,false)
        var itemHolder : ItemHolder = ItemHolder(view)
        return itemHolder
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        var note = arrayList.get(position)

        holder.tv_title.text = note.title
        holder.tv_content.text = note.content
        holder.tv_time.text = note.time

    }

    override fun getItemCount(): Int {
        return arrayList.size
    }


}