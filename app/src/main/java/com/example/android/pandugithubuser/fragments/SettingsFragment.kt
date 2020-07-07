package com.example.android.pandugithubuser.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings.ACTION_LOCALE_SETTINGS
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.android.pandugithubuser.R

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var dataPasser: OnDataPass

    override fun onCreatePreferences(bundle: Bundle?, s: String?) {

        addPreferencesFromResource(R.xml.preferences)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /**
         * Source: https://stackoverflow.com/questions/5298370/how-to-add-a-button-to-a-preferencescreen
         */
        val langSetting: Preference? = findPreference("language_setting")
        langSetting?.onPreferenceClickListener =
            Preference.OnPreferenceClickListener { //code for what you want it to do
                val mIntent = Intent(ACTION_LOCALE_SETTINGS)
                startActivity(mIntent)

                true
            }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dataPasser = context as OnDataPass
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        /**
         * Source:
         * https://v4all123.blogspot.com/2017/10/simple-switchpreference-example-in.html*/
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (key == "notifications") {
            setReminderSwitch()
        }
    }

    private fun setReminderSwitch() {
        val sh = preferenceManager.sharedPreferences
        val isChecked = sh.getBoolean("notifications", false)
        passData(isChecked.toString())
    }

    fun passData(data: String) {
        dataPasser.onDataPass(data)
    }

    interface OnDataPass {
        fun onDataPass(data: String)
    }

}