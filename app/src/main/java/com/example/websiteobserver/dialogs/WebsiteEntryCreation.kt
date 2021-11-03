package com.example.websiteobserver.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.websiteobserver.R

class WebsiteEntryCreation : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val inflater = requireActivity().layoutInflater
            val builder = AlertDialog.Builder(it)
            builder
                .setView(inflater.inflate(R.layout.website_entry_creation_dialog, null))
                .setNegativeButton("Close") { _, _ -> }.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
