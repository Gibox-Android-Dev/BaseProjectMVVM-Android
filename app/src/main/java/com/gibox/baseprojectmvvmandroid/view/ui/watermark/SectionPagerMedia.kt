/*
 * Created by Muhammad Riza
 * Thursday, 3/2/2022
 * Copyright (c) 2022 by Gibox Digital Asia.
 * All Rights Reserve
 */

package com.gibox.baseprojectmvvmandroid.view.ui.watermark

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.gibox.baseprojectmvvmandroid.view.ui.watermark.fragment.AllFragment
import com.gibox.baseprojectmvvmandroid.view.ui.watermark.fragment.ImageFragment
import com.gibox.baseprojectmvvmandroid.view.ui.watermark.fragment.VideoFragment

class SectionPagerMedia (private val mContext: Activity, fm: FragmentManager) :
    FragmentStatePagerAdapter(
        fm,
        BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
    ) {

    companion object{
        private val TAB_TITLE = arrayOf("ALL","IMAGES","VIDEO")
    }

    override fun getCount(): Int = TAB_TITLE.size

    override fun getItem(position: Int): Fragment =
        when(position){
            0->AllFragment()
            1->ImageFragment()
            2->VideoFragment()
            else->Fragment()
        }

    override fun getPageTitle(position: Int): CharSequence = TAB_TITLE[position]

}