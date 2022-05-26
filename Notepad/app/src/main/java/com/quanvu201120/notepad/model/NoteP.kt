package com.quanvu201120.notepad.model

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

class NoteP( var title : String? = "",
             var content : String? = "",
             var time : String? = "",
             var id : Int = 0,
             var docId : String? = "null") : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString()) {
    }

    override fun toString(): String {
        return "NoteP(title='$title', content='$content', time='$time', id=$id, doc_id='$docId')"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(content)
        parcel.writeString(time)
        parcel.writeInt(id)
        parcel.writeString(docId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NoteP> {
        override fun createFromParcel(parcel: Parcel): NoteP {
            return NoteP(parcel)
        }

        override fun newArray(size: Int): Array<NoteP?> {
            return arrayOfNulls(size)
        }
    }
}