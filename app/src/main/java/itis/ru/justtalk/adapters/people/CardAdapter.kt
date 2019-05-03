package itis.ru.justtalk.adapters.people

import android.support.v7.widget.CardView

interface CardAdapter {

    val baseElevation: Float

    val count: Int

    fun getCardViewAt(position: Int): CardView

    companion object {

        val MAX_ELEVATION_FACTOR = 1
    }
}
