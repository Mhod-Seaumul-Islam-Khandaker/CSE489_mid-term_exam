package com.example.test.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.test.R
import com.example.test.model.Landmark

class LandmarkAdapter : RecyclerView.Adapter<LandmarkAdapter.LandmarkViewHolder>() {

    private var landmarks: List<Landmark> = emptyList()

    fun setLandmarks(landmarks: List<Landmark>) {
        this.landmarks = landmarks
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LandmarkViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_landmark, parent, false)
        return LandmarkViewHolder(view)
    }

    override fun onBindViewHolder(holder: LandmarkViewHolder, position: Int) {
        val landmark = landmarks[position]
        holder.bind(landmark)
    }

    override fun getItemCount(): Int = landmarks.size

    class LandmarkViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.landmarkTitle)
        private val coordinatesTextView: TextView = itemView.findViewById(R.id.landmarkCoordinates)
        private val imageView: ImageView = itemView.findViewById(R.id.landmarkImage)
        private val idTextView: TextView = itemView.findViewById(R.id.landmarkId)

        fun bind(landmark: Landmark) {
            titleTextView.text = landmark.title
            coordinatesTextView.text = landmark.getFormattedCoordinates()
            idTextView.text = "ID: ${landmark.id}"

            // Load image with Coil
            imageView.load(landmark.getFullImageUrl()) {
                placeholder(R.drawable.placeholder)
                error(android.R.drawable.ic_menu_report_image)
                crossfade(true)
            }
        }
    }
}