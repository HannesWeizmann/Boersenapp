package com.example.boersenapp.ui.home

import android.os.Parcel
import android.os.Parcelable

data class TickersItemsViewModel(val ticker: String, val name: String) : Parcelable {

    constructor(parcel: Parcel) : this(ticker = parcel.readString()!!, name = parcel.readString()!!) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(ticker)
        parcel.writeString(name)
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


