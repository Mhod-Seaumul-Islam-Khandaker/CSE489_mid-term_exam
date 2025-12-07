package com.example.test

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // SYNTAX: Set the content view to your layout file
        // This inflates activity_main.xml and displays it
        setContentView(R.layout.activity_main)

        // CONTEXT: If you want to enable edge-to-edge display, use this instead:
        // enableEdgeToEdge()
        // But you need to handle window insets properly
    }
}