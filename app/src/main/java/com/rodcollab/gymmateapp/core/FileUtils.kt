package com.rodcollab.gymmateapp.core

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object FileUtils {
    fun saveBitmapToFile(context: Context, bitmap: Bitmap, fileName: String) : String {
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName)

        Log.d("save_uri_result", Uri.fromFile(file).toString())
        try {
            val stream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return Uri.fromFile(file).toString()
    }
}