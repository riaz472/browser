package acr.browser.lightning.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import acr.browser.lightning.R

class ContactActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Contact Us"

        findViewById<Button>(R.id.contact_email_button).setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf("riazalishahani485@gmail.com"))
                putExtra(Intent.EXTRA_SUBJECT, "Nexus Browser - Contact")
            }
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
                startActivity(
                    Intent(Intent.ACTION_VIEW, Uri.parse("mailto:riazalishahani485@gmail.com"))
                )
            }
        }

        findViewById<Button>(R.id.contact_whatsapp_button).setOnClickListener {
            val phone = "923301458939"
            val whatsappIntent = Intent(Intent.ACTION_VIEW,
                Uri.parse("https://wa.me/$phone?text=Hi%2C%20I%20have%20a%20query%20about%20Nexus%20Browser"))
            if (whatsappIntent.resolveActivity(packageManager) != null) {
                startActivity(whatsappIntent)
            } else {
                Toast.makeText(this, "WhatsApp is not installed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
