package com.example.shutthemouth

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.json.JSONArray
import org.json.JSONException


public class PreferenceUtil(context: Context)
{
    private val prefs: SharedPreferences = context.getSharedPreferences("other2", 0)

    fun getPref() : SharedPreferences {
        return prefs
    }
    fun getString(key: String, defValue: String): String {
        return prefs.getString(key, defValue).toString()
    }

    fun setString(key: String, str: String) {
        prefs.edit().putString(key, str).apply()
    }

    fun getBoolean(key: String, defValue: Boolean): Boolean {
        return prefs.getBoolean(key, defValue)
    }

    fun setBoolean(key: String, str: Boolean) {
        prefs.edit().putBoolean(key, str).apply()
    }

    fun getInt(key: String, defValue: Int): Int {
        return prefs.getInt(key, defValue)
    }

    fun setInt(key: String, value: Int) {
        prefs.edit().putInt(key, value).apply()
    }

    fun getArrayListString(key: String): ArrayList<String> {
        val json = prefs.getString(key, null)
        val gson = GsonBuilder().create()
        val type = object : TypeToken<ArrayList<String>>() {}.type
        return gson.fromJson(json, type) ?: ArrayList()
    }

    fun setArrayListString(key: String, list: ArrayList<String>) {
        val gson = GsonBuilder().create()
        val json = gson.toJson(list)
        prefs.edit().putString(key, json).apply()
    }

//    fun getContact(): MutableList<ContactData> {
//        var json = prefs.getString("array",null)
//        val gson = GsonBuilder().create()
//        val arrayListType = object : TypeToken<ArrayList<ContactData>>() {}.type
//        val personList: MutableList<ContactData> = gson.fromJson(json, arrayListType)
//        return personList
//    }
//
//    fun setContact(contactList : MutableList<ContactData>) {
//        // Gson 객체 생성
//        val gson = GsonBuilder().create()
//        // ArrayList를 JSON 문자열로 변환
//        val jsonString = gson.toJson(contactList)
//        prefs.edit().putString("array", jsonString).apply()
//    }
}