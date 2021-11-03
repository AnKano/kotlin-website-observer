package com.example.websiteobserver

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.websiteobserver.adapters.WebsiteListAdapter
import com.example.websiteobserver.adapters.extra.SwipeToDelete
import com.example.websiteobserver.database.WebsiteViewModel
import com.example.websiteobserver.database.WebsiteViewModelFactory
import com.example.websiteobserver.database.entities.Website
import com.example.websiteobserver.receivers.ObserverRestartReceiver
import com.example.websiteobserver.validators.URIWatcher
import kotlinx.coroutines.launch


class InitialActivity : AppCompatActivity() {

    private val websiteViewModel: WebsiteViewModel by viewModels {
        WebsiteViewModelFactory((application as WebsiteObserverApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.initial_activity)

        val recyclerView = findViewById<RecyclerView>(R.id.websiteList)
        recyclerView.itemAnimator = null

        val adapter = WebsiteListAdapter()
        val touchHelper = ItemTouchHelper(SwipeToDelete(this, adapter))
        touchHelper.attachToRecyclerView(recyclerView)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val swipeLayout = findViewById<SwipeRefreshLayout>(R.id.swipe_to_refresh_layout)
        swipeLayout.setOnRefreshListener {
            val broadcastIntent = Intent()
            broadcastIntent.setClass(this, ObserverRestartReceiver::class.java)
            this.sendBroadcast(broadcastIntent)

            swipeLayout.isRefreshing = false
        }

        websiteViewModel.allWords.observe(this, { words ->
            words?.let { (recyclerView.adapter as WebsiteListAdapter).submitList(it) }
        })

        val button = findViewById<Button>(R.id.addWebsiteEntryBtn)
        button.setOnClickListener {
            val inflater = layoutInflater
            val view = inflater.inflate(R.layout.website_entry_creation_dialog, null)

            val builder = AlertDialog.Builder(this)
                .setView(view)
                .setNegativeButton("Close") { _, _ -> }
                .setPositiveButton("OK") { _, _ -> }
            val dialog = builder.create()
            dialog.setOnShowListener {
                val name = view.findViewById<EditText>(R.id.websiteCreationName)
                val url = view.findViewById<EditText>(R.id.websiteCreationUrl)

                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                    val application = application as WebsiteObserverApplication
                    application.applicationScope.launch {
                        application.database.dao().insertEntry(
                            Website(name.text.toString(), url.text.toString())
                        )
                    }
                    dialog.dismiss()
                }

                val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                positiveButton.isEnabled = false

                url.addTextChangedListener(URIWatcher(positiveButton))
            }

            dialog.show()
        }

        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, ObserverRestartReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext, 0, intent, 0
        )
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis(),
            AlarmManager.INTERVAL_FIFTEEN_MINUTES,
            pendingIntent
        )
    }

}
