package com.example.rentwithintent

import android.os.Parcel
import android.os.Parcelable

data class RentalItem(
    val name: String,
    val price: Int,
    val rating: Float,
    val condition: String,
    var expirationDate: Long? = null
) : Parcelable {

    private constructor(parcel: Parcel) : this(
        name = parcel.readString().orEmpty(),
        price = parcel.readInt(),
        rating = parcel.readFloat(),
        condition = parcel.readString().orEmpty(),
        expirationDate = parcel.readValue(Long::class.java.classLoader) as? Long
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeInt(price)
        parcel.writeFloat(rating)
        parcel.writeString(condition)
        parcel.writeValue(expirationDate)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<RentalItem> {
        override fun createFromParcel(parcel: Parcel) = RentalItem(parcel)
        override fun newArray(size: Int): Array<RentalItem?> = arrayOfNulls(size)
    }
}
