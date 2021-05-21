package com.trackier.sdk

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri


class FacebookHelper(context: Context) {

    val ATTRIBUTION_ID_CONTENT_URI = Uri.parse("content://com.facebook.katana.provider.AttributionIdProvider")
    val ATTRIBUTION_ID_COLUMN_NAME = "aid"
    val logger = Factory.logger

    init {
        val contentResolver = context.contentResolver
        getAttributionId(contentResolver)
    }

    fun getAttributionId(contentResolver: ContentResolver): String? {

        if(contentResolver == null) return null

        val projection = arrayOf(ATTRIBUTION_ID_COLUMN_NAME)
        val c: Cursor? = contentResolver.query(ATTRIBUTION_ID_CONTENT_URI, projection, null, null, null)
        if (c == null || !c.moveToFirst()) {
            return null
        }
        val attributionId: String = c.getString(c.getColumnIndex(ATTRIBUTION_ID_COLUMN_NAME))
        c.close()

        logger.info("Facebook Attribution getAttributionId: "+ attributionId)
        return attributionId
    }
}