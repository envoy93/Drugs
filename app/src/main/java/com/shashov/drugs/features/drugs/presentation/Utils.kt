package com.shashov.drugs.features.drugs.presentation

import android.graphics.Color
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.shashov.drugs.R
import java.util.*

object ColorGenerator {
    private val random: Random = Random(System.currentTimeMillis())
    private val colors = Arrays.asList(
            0xffe57373,
            0xfff06292,
            0xffba68c8,
            0xff9575cd,
            0xff7986cb,
            0xff64b5f6,
            0xff4fc3f7,
            0xff4dd0e1,
            0xff4db6ac,
            0xff81c784,
            0xffaed581,
            0xffff8a65,
            0xffd4e157,
            0xffffd54f,
            0xffffb74d,
            0xffa1887f,
            0xff90a4ae
    )

    fun getRandomColor(): Long {
        return colors.get(random.nextInt(colors.size))
    }
}

enum class SearchType {
    DRUG, SUBSTANCE
}

fun TextView.on() {
    setBackgroundColor(this.context.resources.getColor(R.color.filterBgOn))
    setTextColor(this.context.resources.getColor(R.color.filterBgOff))
}

fun TextView.off() {
    setBackgroundColor(Color.TRANSPARENT)
    setTextColor(this.context.resources.getColor(R.color.filterBgOn))
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

