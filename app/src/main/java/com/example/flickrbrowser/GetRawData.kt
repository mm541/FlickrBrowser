package com.example.flickrbrowser

import android.os.AsyncTask
import android.util.Log
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL

enum class DownloadStatus {
    OK,IDLE,NOT_INITIALISED,FAILED_OR_EMPTY,PERMISSIONS_ERROR,ERROR
}
class GetRawData(private val listener: OnDownloadComplete):AsyncTask<String,Void,String>() {
    private val TAG = "GetRawData"
    private var downloadStatus = DownloadStatus.IDLE

    interface OnDownloadComplete {
        fun onDownloadComplete(data:String,status:DownloadStatus)
    }
    override fun doInBackground(vararg params: String?): String {
        if(params[0] == null) {
            downloadStatus = DownloadStatus.NOT_INITIALISED
            return "No url provided"
        }
        try {
            downloadStatus = DownloadStatus.OK
            return URL(params[0]).readText()
        }catch (e:Exception) {
            val errorMsg = when(e) {
                is MalformedURLException -> {
                    downloadStatus = DownloadStatus.NOT_INITIALISED
                    Log.d(TAG,"Invalid url: ${e.message}")
                    "Invalid url: ${e.message}"
                }
                is IOException -> {
                    downloadStatus = DownloadStatus.FAILED_OR_EMPTY
                    Log.d(TAG,"Caught IO Exception while reading: ${e.message}")
                    "Caught IO Exception while reading: ${e.message}"
                }
                is SecurityException -> {
                    downloadStatus = DownloadStatus.PERMISSIONS_ERROR
                    Log.d(TAG,"Permission error: ${e.message}")
                    "Permission error: ${e.message}"
                }
                else -> {
                    downloadStatus = DownloadStatus.ERROR
                    Log.d(TAG,"Unknown error: ${e.message}")
                    "Unknown error: ${e.message}"
                }
            }
            return errorMsg
        }
    }

    override fun onPostExecute(result: String?) {
        Log.d(TAG,"onPostExecute() called")
        listener.onDownloadComplete(result!!,downloadStatus)
    }
}