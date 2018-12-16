package com.example.podar.todoapp2.model

import com.beust.klaxon.Json

/**
 * Created by cpodariu on 17-Mar-18.
 * For any questions please contact me at podariucatalin97@gmail.com
 */

data class TodoItem(val id: Long = 0, val serverId: Long = 0, @Json(name = "value") val value: String, val synced: Boolean = true) {
}