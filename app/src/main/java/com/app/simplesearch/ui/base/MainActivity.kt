package com.app.simplesearch.ui.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.app.simplesearch.R
import com.app.simplesearch.ui.main.fragments.HomeFragment

class MainActivity :BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        defaultFragment(HomeFragment.newInstance())
    }

    private fun defaultFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_containerMain, fragment, fragment::class.java.name)
            .commit()
    }


}