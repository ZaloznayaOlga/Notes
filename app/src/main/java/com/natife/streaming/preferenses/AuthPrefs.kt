package com.natife.streaming.preferenses

import android.annotation.SuppressLint
import android.content.SharedPreferences
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

interface AuthPrefs : BasePrefs {
    fun getAuthToken(): String?
    fun saveAuthToken(token: String?): Boolean
    fun getRefreshAuthToken(): String?
    fun saveRefreshAuthToken(refToken: String?): Boolean
    fun isLoggedIn(): Boolean
//    fun saveProfile(profile: Profile?): Boolean
//    fun getProfile(): Profile?
//    fun getProfileFlow(): Flow<Profile?>
}

private const val AUTH_TOKEN = "AUTH_TOKEN"
private const val REFRESH_TOKEN = "REFRESH_TOKEN"

@SuppressLint("ApplySharedPref")
class AuthPrefsImpl(private val pref: SharedPreferences) : AuthPrefs {
    override fun getAuthToken(): String? = pref.getString(AUTH_TOKEN, null)
    override fun saveAuthToken(token: String?): Boolean = pref.edit().putString(AUTH_TOKEN, token).commit()

    //maybe will useful for future
    override fun getRefreshAuthToken(): String? = pref.getString(REFRESH_TOKEN, null)
    override fun saveRefreshAuthToken(refToken: String?): Boolean = pref.edit().putString(REFRESH_TOKEN, refToken).commit()

    override fun isLoggedIn() = pref.getString(AUTH_TOKEN, null) != null


    override fun clear() = pref.edit().clear().commit()
}