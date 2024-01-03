package com.example.virtualhaus.models;

import android.content.Context

private const val HOME_ID_KEY = "homeId"
private const val USER_ID_KEY = "userId"
private const val DEFAULT_PREF = "DEFAULT_PREF"

class UserManager {
    companion object {
        val shared = UserManager()
    }

    var userId: String? = null
        private set
    var homeId: String? = null
        private set

    fun loadUserInfo(context: Context) {
        userId = getSharedString(context, USER_ID_KEY)
        homeId = getSharedString(context, HOME_ID_KEY)
    }

    fun setUserId(context: Context, value: String?) {
        setSharedString(context, USER_ID_KEY, value)
        userId = value
    }

    fun leaveRoom(context: Context) {
        setHomeId(context, null)
    }

    fun setHomeId(context: Context, value: String?) {
        setSharedString(context, HOME_ID_KEY, value)
        homeId = value
    }

    private fun setSharedString(context: Context, key: String, value: String?) {
        context.getSharedPreferences(DEFAULT_PREF, Context.MODE_PRIVATE)
            .edit()
            .putString(key, value)
            .apply()
    }

    private fun getSharedString(context: Context, key: String): String? {
        return context.getSharedPreferences(DEFAULT_PREF, Context.MODE_PRIVATE)
            ?.getString(key, null)
    }
}
