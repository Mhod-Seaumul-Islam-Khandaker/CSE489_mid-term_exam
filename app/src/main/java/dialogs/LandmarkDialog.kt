package com.example.test.dialogs

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import com.example.test.R
import com.example.test.model.Landmark

class LandmarkDialog {

    interface LandmarkDialogListener {
        fun onLandmarkSaved(landmark: Landmark, isEditMode: Boolean)
    }

    companion object {

        // Show dialog for adding a new landmark
        fun showAddDialog(
            context: Context,
            listener: LandmarkDialogListener
        ) {
            showDialog(context, null, listener)
        }

        // Show dialog for editing an existing landmark
        fun showEditDialog(
            context: Context,
            landmark: Landmark,
            listener: LandmarkDialogListener
        ) {
            showDialog(context, landmark, listener)
        }

        private fun showDialog(
            context: Context,
            existingLandmark: Landmark?,
            listener: LandmarkDialogListener
        ) {
            val isEditMode = existingLandmark != null
            val dialogTitle = if (isEditMode) "Edit Landmark" else "Add New Landmark"

            // Inflate the dialog layout
            val inflater = LayoutInflater.from(context)
            val dialogView = inflater.inflate(R.layout.dialog_landmark, null)

            // Get references to EditText fields
            val titleEditText = dialogView.findViewById<EditText>(R.id.editTextTitle)
            val latEditText = dialogView.findViewById<EditText>(R.id.editTextLatitude)
            val lonEditText = dialogView.findViewById<EditText>(R.id.editTextLongitude)

            // If editing, populate with existing data
            if (isEditMode) {
                titleEditText.setText(existingLandmark?.title)
                latEditText.setText(existingLandmark?.latitude.toString())
                lonEditText.setText(existingLandmark?.longitude.toString())
            }

            // Create and show the dialog
            val dialog = AlertDialog.Builder(context)
                .setTitle(dialogTitle)
                .setView(dialogView)
                .setPositiveButton("Save") { dialogInterface, which ->
                    // Get input values
                    val title = titleEditText.text.toString().trim()
                    val latStr = latEditText.text.toString().trim()
                    val lonStr = lonEditText.text.toString().trim()

                    // Validate inputs
                    if (title.isEmpty() || latStr.isEmpty() || lonStr.isEmpty()) {
                        Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                        return@setPositiveButton
                    }

                    try {
                        val latitude = latStr.toDouble()
                        val longitude = lonStr.toDouble()

                        // Validate coordinate ranges (roughly for Bangladesh)
                        if (latitude < 20.0 || latitude > 27.0 ||
                            longitude < 88.0 || longitude > 93.0) {
                            Toast.makeText(context,
                                "Coordinates should be within Bangladesh range\n" +
                                        "Lat: 20-27, Lon: 88-93",
                                Toast.LENGTH_LONG).show()
                            return@setPositiveButton
                        }

                        // Create landmark object
                        val landmark = if (isEditMode) {
                            Landmark(
                                id = existingLandmark!!.id,
                                title = title,
                                latitude = latitude,
                                longitude = longitude,
                                imagePath = existingLandmark.imagePath // Keep existing image
                            )
                        } else {
                            // For new landmarks, use temporary ID (-1)
                            // Real ID will come from API response
                            Landmark(
                                id = -1,
                                title = title,
                                latitude = latitude,
                                longitude = longitude,
                                imagePath = "" // No image for now
                            )
                        }

                        // Call listener with the landmark
                        listener.onLandmarkSaved(landmark, isEditMode)

                    } catch (e: NumberFormatException) {
                        Toast.makeText(context, "Invalid coordinates format", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Cancel", null)
                .create()

            dialog.show()
        }
    }
}