package com.example.listadodecryptomonedas.helper

import android.content.res.Resources
import android.util.TypedValue
import kotlin.math.roundToInt

fun Int.toDp() = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    toFloat(),
    Resources.getSystem().displayMetrics
).roundToInt()
