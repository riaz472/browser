package acr.browser.lightning.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import acr.browser.lightning.R

class SupportActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_support)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Support"
    }
}
