package com.example.flickrbrowser

import android.os.AsyncTask
import android.util.Log
import org.json.JSONException
import org.json.JSONObject

class GetFlickrJson(private val listener:OnDataAvailable):AsyncTask<String,Void,ArrayList<Photo>>() {
    private val TAG = "GetFlickrJson"
    interface OnDataAvailable{
        fun onDataAvailable(result:List<Photo>)
        fun onError(e:Exception)
    }


    override fun doInBackground(vararg params: String?): ArrayList<Photo> {
        val result:ArrayList<Photo> = ArrayList()
        try {
           val jsonObject = JSONObject(params[0]!!)
           val itemsArray = jsonObject.getJSONArray("items")
            for (i in 0 until itemsArray.length()) {
                val photoItem = itemsArray.getJSONObject(i)
                val title = photoItem.getString("title")
                val author = photoItem.getString("author")
                val authorId = photoItem.getString("author_id")
                val tags = photoItem.getString("tags")
                val jsonMedia = photoItem.getJSONObject("media")
                val imageUrl = jsonMedia.getString("m")
                val link = imageUrl.replace("_m.jpg","_b.jpg")
                val photo = Photo(title, author, authorId, link,tags,imageUrl)
                result.add(photo)
            }
       }catch (e:JSONException) {
           Log.d(TAG,"caught JSON Exception while parsing")
            cancel(true)
           listener.onError(e)
       }
        return result
    }

    override fun onPostExecute(result: ArrayList<Photo>?) {
        if(result!!.isNotEmpty()) {
            listener.onDataAvailable(result)
        }
    }
}