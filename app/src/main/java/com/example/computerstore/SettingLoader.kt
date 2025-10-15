package com.example.computerstore

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson

object SettingLoader {

    private const val PREFS_NAME = "computer_store_settings"

    // --- Lấy SharedPreferences instance
    private fun prefs(context: Context): SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    // --- Lưu String
    fun saveString(context: Context, key: String, value: String) {
        prefs(context).edit().putString(key, value).apply()
    }

    // --- Lấy String
    fun getString(context: Context, key: String): String? {
        return prefs(context).getString(key, null)
    }

    // --- Lưu Boolean
    fun saveBoolean(context: Context, key: String, value: Boolean) {
        prefs(context).edit().putBoolean(key, value).apply()
    }

    fun getBoolean(context: Context, key: String, default: Boolean = false): Boolean {
        return prefs(context).getBoolean(key, default)
    }

    // --- Lưu Int
    fun saveInt(context: Context, key: String, value: Int) {
        prefs(context).edit().putInt(key, value).apply()
    }

    fun getInt(context: Context, key: String, default: Int = 0): Int {
        return prefs(context).getInt(key, default)
    }

    // --- Xoá key
    fun remove(context: Context, key: String) {
        prefs(context).edit().remove(key).apply()
    }

    // --- Xoá toàn bộ dữ liệu
    fun clearAll(context: Context) {
        prefs(context).edit().clear().apply()
    }

    // --- Generic: Lưu object bất kỳ (dùng Gson để serialize)
    inline fun <reified T> saveObject(context: Context, key: String, obj: T) {
        val json = Gson().toJson(obj)
        saveString(context, key, json)
    }

    // --- Generic: Lấy object bất kỳ
    inline fun <reified T> getObject(context: Context, key: String): T? {
        val json = getString(context, key) ?: return null
        return try {
            Gson().fromJson(json, T::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
