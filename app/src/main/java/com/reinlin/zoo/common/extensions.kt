package com.reinlin.zoo.common

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.reinlin.zoo.MainActivity
import com.reinlin.zoo.R
import kotlinx.android.synthetic.main.activity_main.*

internal fun AppCompatActivity.attachFragment(fragment: Fragment, tag: String) {
    val transaction = supportFragmentManager.beginTransaction()
        .replace(R.id.container, fragment, tag)
    if (tag == BRIEF_LIST)
        transaction.commit()
    else {
        transaction.addToBackStack(tag)
        transaction.commit()
    }
}


internal fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

internal fun MainActivity.toolbarTitle(title: String?) {
    title?.let {
        toolbar_title.text = it
    }
}

internal fun String.splitC(ch: String): String {
    return if (this.contains(ch)) this.split(ch)[0] else this
}