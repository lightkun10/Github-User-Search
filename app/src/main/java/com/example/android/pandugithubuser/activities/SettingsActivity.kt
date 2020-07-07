package com.example.android.pandugithubuser.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.android.pandugithubuser.R
import com.example.android.pandugithubuser.fragments.SettingsFragment
import com.example.android.pandugithubuser.utils.AlarmReceiver

private const val TAG = "SettingActivity"

class SettingsActivity : AppCompatActivity(), SettingsFragment.OnDataPass {

    private lateinit var alarmReceiver: AlarmReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        supportFragmentManager.beginTransaction().add(R.id.setting_holder,
            SettingsFragment()
        ).commit()
        alarmReceiver = AlarmReceiver()

        // actionbar
        val actionbar = supportActionBar
        // set actionbar title
        actionbar?.title = resources.getString(R.string.preferences_setting)
        // set back button
        actionbar?.setDisplayHomeAsUpEnabled(true)
        actionbar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onDataPass(data: String) {
        var reminderPrefs = data.toBoolean()

        when (reminderPrefs) {
            true -> alarmReceiver.setRepeatingAlarm(this, AlarmReceiver.TYPE_REPEATING)
            false -> alarmReceiver.cancelAlarm(this, AlarmReceiver.TYPE_REPEATING)
        }
    }


}