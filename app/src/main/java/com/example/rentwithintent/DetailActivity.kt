package com.example.rentwithintent

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class DetailActivity : AppCompatActivity() {

    private lateinit var itemImage: ImageView
    private lateinit var itemName: TextView
    private lateinit var itemPrice: TextView
    private lateinit var rentalDuration: SeekBar
    private lateinit var confirmButton: Button
    private lateinit var cancelButton: Button

    private lateinit var selectedItem: RentalItem
    private var credits: Int = 0  // To store received credits

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        itemImage = findViewById(R.id.detail_image)
        itemName = findViewById(R.id.detail_name)
        itemPrice = findViewById(R.id.detail_price)
        rentalDuration = findViewById(R.id.rental_period)
        confirmButton = findViewById(R.id.save_button)
        cancelButton = findViewById(R.id.cancel_button)

        selectedItem = intent.getParcelableExtra("item") ?: run {
            Toast.makeText(this, "Error loading item", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        credits = intent.getIntExtra("credits", 100)

        displayItemDetails()

        rentalDuration.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                updatePriceText()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        confirmButton.setOnClickListener {
            val duration = rentalDuration.progress
            val totalCost = duration * selectedItem.price

            if (duration > 0) {
                if (credits >= totalCost) {
                    credits -= totalCost

                    val currentTime = System.currentTimeMillis()
                    val returnDate = currentTime + duration * 24 * 60 * 60 * 1000L
                    selectedItem.expirationDate = returnDate

                    val resultIntent = Intent().apply {
                        putExtra("item", selectedItem)
                        putExtra("credits", credits)
                    }
                    setResult(RESULT_OK, resultIntent)
                    Toast.makeText(this, "Rental successful!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Not enough credits!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Must select at least 1 day!", Toast.LENGTH_SHORT).show()
            }
        }

        cancelButton.setOnClickListener {
            Toast.makeText(this, "Booking cancelled", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Toast.makeText(this, "Booking cancelled", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun displayItemDetails() {
        rentalDuration.progress = 1
        updatePriceText()
        itemName.text = selectedItem.name
        itemImage.setImageResource(
            when (selectedItem.name) {
                "Drum" -> R.drawable.drum
                "Guitar" -> R.drawable.guitar
                "Piano" -> R.drawable.piano
                else -> R.drawable.drum
            }
        )
    }

    private fun updatePriceText() {
        val days = rentalDuration.progress
        itemPrice.text = "Price: ${days * selectedItem.price} credits for $days ${if (days > 1) "days" else "day"}"
    }
}
