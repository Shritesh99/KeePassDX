/*
 * Copyright 2019 Jeremy Jamet / Kunzisoft.
 *     
 * This file is part of KeePassDX.
 *
 *  KeePassDX is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  KeePassDX is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with KeePassDX.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.kunzisoft.keepass.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.RelativeLayout
import androidx.annotation.StringRes
import com.google.android.material.textfield.TextInputLayout
import com.kunzisoft.keepass.R
import com.kunzisoft.keepass.database.element.security.ProtectedString

class EntryEditCustomField @JvmOverloads constructor(context: Context,
                                                     attrs: AttributeSet? = null,
                                                     defStyle: Int = 0)
    : RelativeLayout(context, attrs, defStyle) {

    private val valueLayoutView: TextInputLayout
    private val valueView: EditText
    private val protectionCheckView: CompoundButton

    val label: String
        get() = valueLayoutView.hint.toString()

    val value: String
        get() = valueView.text.toString()

    val isProtected: Boolean
        get() = protectionCheckView.isChecked

    init {

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater?
        inflater?.inflate(R.layout.view_entry_custom_field, this)

        val deleteView = findViewById<View>(R.id.entry_custom_field_delete)
        deleteView.setOnClickListener { deleteViewFromParent() }

        valueLayoutView = findViewById(R.id.entry_custom_field_value_container)
        valueView = findViewById(R.id.entry_custom_field_value)
        protectionCheckView = findViewById(R.id.entry_custom_field_protection)
    }

    fun setData(label: String?, value: ProtectedString?, fontInVisibility: Boolean) {
        if (label != null)
            valueLayoutView.hint = label
        if (value != null) {
            valueView.setText(value.toString())
            protectionCheckView.isChecked = value.isProtected
        }
        setFontVisibility(fontInVisibility)
    }

    /**
     * Validate or not the entry form
     *
     * @return ErrorValidation An error with a message or a validation without message
     */
    fun isValid(): Boolean {
        // Validate extra field
        if (label.isEmpty()) {
            setError(R.string.error_string_key)
            return false
        } else {
            setError(null)
        }
        return true
    }

    fun setError(@StringRes errorId: Int?) {
        valueLayoutView.error = if (errorId == null) null else {
            context.getString(errorId)
        }
    }

    fun setFontVisibility(applyFontVisibility: Boolean) {
        if (applyFontVisibility)
            valueView.applyFontVisibility()
    }

    private fun deleteViewFromParent() {
        try {
            val parent = parent as ViewGroup
            parent.removeView(this)
            parent.invalidate()
        } catch (e: ClassCastException) {
            Log.e(javaClass.name, "Unable to delete view", e)
        }
    }
}
