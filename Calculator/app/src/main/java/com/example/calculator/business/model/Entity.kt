package com.example.calculator.business.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Entity(@SerializedName("value") var value: String, @SerializedName("order") var order: Int, @SerializedName("type") var type: Type) :
    Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readInt(),
        Type.values()[parcel.readInt()]
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(value)
        parcel.writeInt(order)
        parcel.writeInt(type.ordinal)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Entity> {
        override fun createFromParcel(parcel: Parcel): Entity {
            return Entity(parcel)
        }

        override fun newArray(size: Int): Array<Entity?> {
            return arrayOfNulls(size)
        }
    }

    enum class Type(val id: String) {
        NORMAL("normal"), OPERATOR("operator"), EQUAL("equal"), CLEAR("CLEAR"), TRIGO("after"), MULTIPLE("before"),
        PI("pi"), E("e"), POW("pow"), FACT("fact"), PARENTHESES("parenthesis")
    }
}