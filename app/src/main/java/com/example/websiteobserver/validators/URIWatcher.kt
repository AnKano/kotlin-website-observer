package com.example.websiteobserver.validators

import android.text.Editable
import android.text.TextWatcher
import android.webkit.URLUtil
import android.widget.Button

class URIWatcher(private val button: Button) : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable?) {
        val value = s.toString().trim()
        val status = URLUtil.isValidUrl(value)
        button.isEnabled = status
    }
}