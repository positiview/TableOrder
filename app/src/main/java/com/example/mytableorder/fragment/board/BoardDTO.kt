package com.example.mytableorder.fragment.board

import android.os.Parcel
import android.os.Parcelable

class BoardDTO(
  var id: Int,
  var name: String?,
  var title: String?,
  var content: String?
) : Parcelable {
  constructor(name: String?, title: String, content: String?) : this(0, name, title, content)

  companion object CREATOR : Parcelable.Creator<BoardDTO> {
    override fun createFromParcel(source: Parcel): BoardDTO {
      return BoardDTO(source)
    }

    override fun newArray(size: Int): Array<BoardDTO?> {
      return arrayOfNulls<BoardDTO>(size)
    }
  }

  constructor(parcel: Parcel) :
      this(parcel.readInt(), parcel.readString(), parcel.readString(), parcel.readString())

  override fun describeContents(): Int {
    return 0
  }

  override fun writeToParcel(dest: Parcel, flags: Int) {
    dest.writeInt(id)
    dest.writeString(name)
    dest.writeString(title)
    dest.writeString(content)
  }
}