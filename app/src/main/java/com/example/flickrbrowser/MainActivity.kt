package com.example.flickrbrowser

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flickrbrowser.databinding.ActivityMainBinding

private const val TAG = "MainActivity"
const val FLICKR_QUERY = "FLICKR_QUERY"
const val PHOTO_TRANSFER = "PHOTO_TRANSFER"
class MainActivity : AppCompatActivity(),
    GetRawData.OnDownloadComplete,
    GetFlickrJson.OnDataAvailable,
    RecyclerItemClickListener.OnRecyclerItemClick
{

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
        binding.recyclerView.addOnItemTouchListener(RecyclerItemClickListener(this,binding.recyclerView,this))
        binding.recyclerView.adapter = flickrRecyclerViewAdapter
        val getRawData = GetRawData(this)
        val url = createUri("https://www.flickr.com/services/feeds/photos_public.gne","android,oreo","en-us",true)

        getRawData.execute(url)
        Log.d(TAG,"onCreate end")
    }
    override fun onCLickItem(view: View, position: Int) {
        Log.d(TAG,"onCLickItem() called and position of child item is $position")
        Toast.makeText(this,"Normal tap at position $position",Toast.LENGTH_SHORT).show()
    }

    override fun onLongCLickItem(view: View, position: Int) {
        Log.d(TAG,"onLongCLickItem() called and position of child item is $position")
        val photo = flickrRecyclerViewAdapter.getPhotos(position)
        if(photo != null) {
            val intent = Intent(this,PhotoDetailsActivity::class.java)
            intent.putExtra(PHOTO_TRANSFER,photo)
            startActivity(intent)
        }
        Toast.makeText(this,"Long tap at position $position",Toast.LENGTH_SHORT).show()
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
            R.id.search_item->{startActivity(Intent(this,SearchActivity::class.java))
                                true
                        }
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

    override fun onResume() {
        super.onResume()
        Log.d(TAG,"onResume() called")

    }

}