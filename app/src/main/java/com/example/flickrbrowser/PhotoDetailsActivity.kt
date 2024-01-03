package com.example.flickrbrowser

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.flickrbrowser.databinding.ActivityPhotoDetailsBinding
import com.squareup.picasso.Picasso

class PhotoDetailsActivity : AppCompatActivity() {
    private lateinit var _binding:ActivityPhotoDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPhotoDetailsBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        val toolbarPhoto = _binding.toolbarPhoto
        setSupportActionBar(toolbarPhoto)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val photo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(PHOTO_TRANSFER,Photo::class.java)!!
        } else {
            intent.getParcelableExtra(PHOTO_TRANSFER)
        }

        _binding.photoAuthor.text = photo?.author
        _binding.photoTitle.text = photo?.title
        _binding.photoTags.text = photo?.tags
        Log.d("photoDetailActivity", photo!!.link)
        Log.d("photoDetailActivity", photo.image)
        Picasso.get().load(photo.link).error(R.drawable.placeholder)
            .placeholder(R.drawable.placeholder)
            .into(_binding.photoImage)

    }
}