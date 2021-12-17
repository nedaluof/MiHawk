package com.nedaluof.mihawk.miserializer

import androidx.annotation.WorkerThread

/**
 * Created by NedaluOf on 11/10/2021.
 */
interface MiSerializer {
    @WorkerThread
    fun <T> toString(t: T): String

    @WorkerThread
    fun <T> fromString(value: String?, aClass: Class<T>): T?
}