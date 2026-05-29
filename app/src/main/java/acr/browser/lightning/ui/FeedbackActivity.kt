package acr.browser.lightning.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import acr.browser.lightning.R

class FeedbackActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Feedback"

        val nameInput = findViewById<EditText>(R.id.feedback_name)
        val emailInput = findViewById<EditText>(R.id.feedback_email)
        val msgInput = findViewById<EditText>(R.id.feedback_message)
        val submitBtn = findViewById<Button>(R.id.feedback_submit)

        submitBtn.setOnClickListener {
            val name = nameInput.text.toString()
            val email = emailInput.text.toString()
            val msg = msgInput.text.toString()
            if (name.isNotBlank() && email.isNotBlank() && msg.isNotBlank()) {
                Toast.makeText(this, "Thank you! Your feedback has been sent.", Toast.LENGTH_LONG).show()
                finish()
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
