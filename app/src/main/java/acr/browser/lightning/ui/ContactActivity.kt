package acr.browser.lightning.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import acr.browser.lightning.R

class ContactActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Contact Us"
    }
}
