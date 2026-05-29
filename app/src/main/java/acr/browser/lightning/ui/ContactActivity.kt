package acr.browser.lightning.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
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
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
