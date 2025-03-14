package com.example.rentwithintent

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var itemImage: ImageView
    private lateinit var itemName: TextView
    private lateinit var itemPrice: TextView
    private lateinit var itemRating: RatingBar
    private lateinit var itemCondition: TextView
    private lateinit var nextButton: Button
    private lateinit var borrowButton: Button
    private lateinit var creditBalance: TextView

    private var credits: Int = 1000  // Initial balance

    private val rentalItems = mutableListOf(
        RentalItem("Drum", 15, 4.5f, "New"),
        RentalItem("Guitar", 10, 4.0f, "Good"),
        RentalItem("Piano", 25, 3.5f, "Fair")
    )

    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        itemImage = findViewById(R.id.item_image)
        itemName = findViewById(R.id.item_name)
        itemPrice = findViewById(R.id.item_price)
        itemRating = findViewById(R.id.item_rating)
        itemCondition = findViewById(R.id.item_condition)
        nextButton = findViewById(R.id.next_button)
        borrowButton = findViewById(R.id.borrow_button)
        creditBalance = findViewById(R.id.credit_balance)

        updateUI()

        nextButton.setOnClickListener {
            currentIndex = (currentIndex + 1) % rentalItems.size
            updateUI()
        }

        borrowButton.setOnClickListener {
            val intent = Intent(this, DetailActivity::class.java).apply {
                putExtra("item", rentalItems[currentIndex])
                putExtra("credits", credits)
            }
            startActivityForResult(intent, REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            credits = data?.getIntExtra("credits", credits) ?: credits
            updateUI()
        }
    }

    private fun updateUI() {
        val currentItem = rentalItems[currentIndex]
        itemName.text = currentItem.name
        itemPrice.text = "Price: ${currentItem.price} credits"
        itemRating.rating = currentItem.rating
        itemCondition.text = currentItem.condition
        creditBalance.text = "Credits: $credits"

        itemImage.setImageResource(
            when (currentItem.name) {
                "Drum" -> R.drawable.drum
                "Guitar" -> R.drawable.guitar
                "Piano" -> R.drawable.piano
                else -> R.drawable.drum
            }
        )

        borrowButton.isEnabled = credits >= currentItem.price
    }

    companion object {
        private const val REQUEST_CODE = 1
    }
}
