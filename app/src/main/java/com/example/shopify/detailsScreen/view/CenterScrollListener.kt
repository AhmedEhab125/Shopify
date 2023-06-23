package com.example.shopify.detailsScreen.view

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.shopify.R

class ZoomOutPageTransformer : ViewPager2.PageTransformer {
    private val MIN_SCALE = 0.85f
    private val MIN_ALPHA = 0.5f

    override fun transformPage(page: View, position: Float) {
        val pageWidth = page.width
        val pageHeight = page.height

        when {
            position < -1 -> { // This page is way off-screen to the left.
                page.alpha = 0f
            }
            position <= 1 -> { // Page is visible within the screen.
                val scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position))
                val vertMargin = pageHeight * (1 - scaleFactor) / 2
                val horzMargin = pageWidth * (1 - scaleFactor) / 2
                page.translationX = if (position < 0) horzMargin - vertMargin / 2 else -horzMargin + vertMargin / 2

                // Scale the page down (between MIN_SCALE and 1)
                page.scaleX = scaleFactor
                page.scaleY = scaleFactor

                // Fade the page relative to its distance from the center
                page.alpha = MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA)
            }
            else -> { // This page is way off-screen to the right.
                page.alpha = 0f
            }
        }
    }
}
