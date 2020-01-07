package com.main

import android.app.Activity
import android.os.Bundle
import com.foobnix.pdf.info.BuildConfig

import com.foobnix.pdf.info.ExtUtils
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

class MainActivity : Activity() {

    private val DATABASE_NAME = "livro2.epub"
    private val DATABASE_PATH = "/data/data/" + BuildConfig.APPLICATION_ID + "/databases/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        copyDBFromAssetsFolder()
        val f = File("$DATABASE_PATH/$DATABASE_NAME")
        ExtUtils.showDocument(this, f)
    }

    private fun copyDBFromAssetsFolder() {
        var `is`: InputStream? = null
        try {
            //lê o arquivo da pasta assets
            `is` = assets.open(DATABASE_NAME)
            val size = `is`!!.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            val fileDir = File(DATABASE_PATH)
            fileDir.mkdirs()
            val f = File("$DATABASE_PATH/$DATABASE_NAME")
            f.createNewFile()
            val fos = FileOutputStream(f)
            fos.write(buffer)
            fos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }
}
