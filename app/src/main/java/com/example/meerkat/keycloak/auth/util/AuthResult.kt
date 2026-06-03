package com.example.meerkat.keycloak.auth.util



import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi

data class AuthResult(
    val data: User?,
    val errorMessage: String?)


data class User(
    val id : String = "",
    val name : String = "",
    val email : String  = "",
    val image : String ="",
) : Parcelable {

    constructor(parcel: Parcel):this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
    ){

    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest){
        writeString(id)
        writeString(name)
        writeString(email)
        writeString(image)
    }
    companion object CREATOR : Parcelable.Creator<User>{
        @RequiresApi(Build.VERSION_CODES.Q)
        override fun createFromParcel(source: Parcel): User? {
            return User(source)
        }

        override fun newArray(size: Int): Array<out User?>? {
            return arrayOfNulls(size)
        }
    }
}