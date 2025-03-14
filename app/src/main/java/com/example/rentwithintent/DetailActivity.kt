package com.example.rentwithintent

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class DetailActivity : AppCompatActivity() {

    private lateinit var itemImage: ImageView
    private lateinit var itemName: TextView
    private lateinit var itemPrice: TextView
    private lateinit var rentalDuration: SeekBar
    private lateinit var confirmButton: Button
    private lateinit var cancelButton: Button
    private lateinit var multiChoiceGroup: ChipGroup

    private lateinit var selectedItem: RentalItem
    private var credits: Int = 0  // To store received credits
    private var selectedExtrasCost = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        itemImage = findViewById(R.id.detail_image)
        itemName = findViewById(R.id.detail_name)
        itemPrice = findViewById(R.id.detail_price)
        rentalDuration = findViewById(R.id.rental_period)
        confirmButton = findViewById(R.id.save_button)
        cancelButton = findViewById(R.id.cancel_button)
        multiChoiceGroup = findViewById(R.id.multi_choice_group)

        selectedItem = intent.getParcelableExtra("item") ?: run {
            Toast.makeText(this, "Error loading item", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        credits = intent.getIntExtra("credits", 1000)

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
            val totalCost = duration * (selectedItem.price + selectedExtrasCost)

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

        // Assign the correct multi-choice options based on item
        selectedItem = when (selectedItem.name) {
            "Drum" -> selectedItem.copy(multiChoiceOptions = listOf(
                "Acoustic Kits" to 10,
                "Cymbals" to 5,
                "Drum Hardware" to 15,
                "Percussion" to 10
            ))
            "Guitar" -> selectedItem.copy(multiChoiceOptions = listOf(
                "Guitar Amps" to 5,
                "Effects Pedals" to 10,
                "Accessories" to 10
            ))
            "Piano" -> selectedItem.copy(multiChoiceOptions = listOf(
                "Keyboards" to 5,
                "Synthesizers" to 15,
                "MIDI Controllers" to 20,
                "Keyboard Amps" to 10
            ))
            else -> selectedItem
        }

        setupMultiChoiceOptions()
    }


    private fun setupMultiChoiceOptions() {
        multiChoiceGroup.removeAllViews()
        selectedExtrasCost = 0

        for ((option, cost) in selectedItem.multiChoiceOptions) {
            val chip = Chip(this)
            chip.text = "$option (+$cost credits)"
            chip.isCheckable = true
            chip.textSize = 20f
            chip.setOnCheckedChangeListener { _, isChecked ->
                selectedExtrasCost += if (isChecked) cost else -cost
                updatePriceText()
            }
            multiChoiceGroup.addView(chip)
        }
    }

    private fun updatePriceText() {
        val days = rentalDuration.progress
        itemPrice.text = "Price: ${days * (selectedItem.price + selectedExtrasCost)} credits for $days ${if (days > 1) "days" else "day"}"
    }
}
