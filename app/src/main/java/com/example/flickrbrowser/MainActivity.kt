package com.example.flickrbrowser

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flickrbrowser.databinding.ActivityMainBinding

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity(),GetRawData.OnDownloadComplete,GetFlickrJson.OnDataAvailable {

//    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val flickrRecyclerViewAdapter:FlickrRecyclerViewAdapter = FlickrRecyclerViewAdapter(
        ArrayList()
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        Log.d(TAG,"onCreate called")
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = flickrRecyclerViewAdapter
        val getRawData = GetRawData(this)
        val url = createUri("https://www.flickr.com/services/feeds/photos_public.gne","android,oreo","en-us",true)

        getRawData.execute(url)
        Log.d(TAG,"onCreate end")
    }

    private fun createUri(baseUrl: String, tags: String, lang: String, tagMode: Boolean): String {
       return Uri.parse(baseUrl).
                buildUpon().
                appendQueryParameter("tags",tags).
                appendQueryParameter("lang",lang).
                appendQueryParameter("tagmode",if(tagMode) "any" else "all").
                appendQueryParameter("format","json").
                appendQueryParameter("nojsoncallback","1").
                build().
                toString()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        Log.d(TAG,"onCreateOptionsMenu called")
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Log.d(TAG,"onOptionsItemSelected called")
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

   override fun onDownloadComplete(data: String,status:DownloadStatus) {
        if(status == DownloadStatus.OK) {
//            Log.d(TAG,"data downloaded successfully and data is $data")
            val getFlickrJson = GetFlickrJson(this)
            getFlickrJson.execute(data)
        }else {
            Log.d(TAG,"Error failed to download data with status: $status and error message is $data")
        }
    }

    override fun onDataAvailable(result: List<Photo>) {
//        Log.d(TAG,"Parsing successful, result: $result")
        flickrRecyclerViewAdapter.loadNewData(result)
    }

    override fun onError(e: Exception) {
        Log.d(TAG,"Parsing error: ${e.message}")
    }


}