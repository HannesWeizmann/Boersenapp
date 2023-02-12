package com.example.boersenapp.ui.home

import android.os.Parcel
import android.os.Parcelable

data class TickersItemsViewModel(val text: String, ) : Parcelable {

    constructor(parcel: Parcel) : this(parcel.readString()!!) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(text)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TickersItemsViewModel> {
        override fun createFromParcel(parcel: Parcel): TickersItemsViewModel {
            return TickersItemsViewModel(parcel)
        }

        override fun newArray(size: Int): Array<TickersItemsViewModel?> {
            return arrayOfNulls(size)
        }
    }
}


