package com.example.flickrbrowser

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class FlickrImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val thumbnail:ImageView = view.findViewById(R.id.placeholder)
        val title:TextView = view.findViewById(R.id.title)
}
class FlickrRecyclerViewAdapter(private var photoList:ArrayList<Photo>):RecyclerView.Adapter<FlickrImageViewHolder>() {
    private val TAG = "RecyclerAdapter"
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlickrImageViewHolder {
        Log.d(TAG,"onCreateViewHolder() called")
        val view = LayoutInflater.from(parent.context).inflate(R.layout.browse,parent,false)
        return FlickrImageViewHolder(view)
    }

    fun loadNewData(photos: List<Photo>) {
         photoList = photos as ArrayList<Photo>
        notifyDataSetChanged()
    }
    fun getPhotos(position:Int):Photo? {
        return if(photoList.isNotEmpty()) return photoList[position] else null
    }
    override fun getItemCount(): Int {
       return if(photoList.isNotEmpty()) photoList.size else 0
    }

    override fun onBindViewHolder(holder: FlickrImageViewHolder, position: Int) {
        Log.d(TAG,"onBindViewHolder() called")
        val photoItem = photoList[position]
        Picasso.get()
            .load(photoItem.image)
            .error(R.drawable.placeholder)
            .placeholder(R.drawable.placeholder)
            .into(holder.thumbnail)

        holder.title.text = photoItem.title

    }


}