package acr.browser.lightning.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import acr.browser.lightning.R
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ReadingModeActivity : ComponentActivity() {

    companion object {
        private const val EXTRA_URL = "reading_url"
        private const val EXTRA_TITLE = "reading_title"

        fun launch(context: Context, url: String, title: String) {
            context.startActivity(
                Intent(context, ReadingModeActivity::class.java).apply {
                    putExtra(EXTRA_URL, url)
                    putExtra(EXTRA_TITLE, title)
                }
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val url = intent.getStringExtra(EXTRA_URL) ?: ""
        val pageTitle = intent.getStringExtra(EXTRA_TITLE) ?: "Article"

        setContent {
            ReadingModeScreen(
                pageTitle = pageTitle,
                url = url,
                onBack = { finish() },
                onSaveOffline = { title, content -> saveOffline(title, content) }
            )
        }
    }

    private fun saveOffline(title: String, content: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val dir = File(filesDir, "reading_mode").also { it.mkdirs() }
                val safe = title.replace(Regex("[^a-zA-Z0-9 ]"), "_").take(50)
                val stamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
                val file = File(dir, "${safe}_$stamp.html")
                file.writeText(buildOfflineHtml(title, content))
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@ReadingModeActivity,
                        "Article saved for offline reading",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@ReadingModeActivity,
                        "Save failed: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun buildOfflineHtml(title: String, content: String): String =
        """<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>$title</title>
  <style>
    body { font-family: Georgia, serif; max-width: 720px; margin: 40px auto;
           padding: 0 20px; font-size: 18px; line-height: 1.7; color: #222; background: #fff; }
    h1   { font-size: 28px; margin-bottom: 24px; }
  </style>
</head>
<body>
  <h1>$title</h1>
  $content
</body>
</html>"""
}

enum class ReadingTheme(val bg: Color, val text: Color, val label: String) {
    LIGHT(Color.White, Color(0xFF1A1A1A), "Light"),
    SEPIA(Color(0xFFF5ECD7), Color(0xFF3D2B1F), "Sepia"),
    DARK(Color(0xFF1C1C1E), Color(0xFFE5E5EA), "Dark")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadingModeScreen(
    pageTitle: String,
    url: String,
    onBack: () -> Unit,
    onSaveOffline: (title: String, content: String) -> Unit
) {
    var articleTitle by remember { mutableStateOf(pageTitle) }
    var articleContent by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }
    var errorMsg by remember { mutableStateOf("") }
    var fontSize by remember { mutableIntStateOf(18) }
    var theme by remember { mutableStateOf(ReadingTheme.LIGHT) }

    LaunchedEffect(url) {
        withContext(Dispatchers.IO) {
            try {
                val doc = Jsoup.connect(url)
                    .userAgent(
                        "Mozilla/5.0 (Linux; Android 12; Mobile) AppleWebKit/537.36 " +
                        "(KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36"
                    )
                    .timeout(15_000)
                    .get()

                doc.select(
                    "script, style, nav, header, footer, aside, iframe, " +
                    "[class*=sidebar], [class*=-ad-], [class*=banner], " +
                    "[id*=sidebar], [id*=cookie], [role=banner], [role=navigation]"
                ).remove()

                val title = doc.title().ifBlank { pageTitle }

                val body = doc.selectFirst("article")
                    ?: doc.selectFirst("[role=article]")
                    ?: doc.selectFirst("main")
                    ?: doc.selectFirst(
                        ".post-content, .entry-content, .article-body, " +
                        ".story-body, #article-body, #main-content"
                    )
                    ?: doc.body()

                val html = body?.html() ?: ""

                withContext(Dispatchers.Main) {
                    articleTitle = title
                    articleContent = html
                    isLoading = false
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    errorMsg = "Could not load article:\n${e.message}"
                    isLoading = false
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = articleTitle,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painter = painterResource(R.drawable.ic_action_back),
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            if (!isLoading && articleContent.isNotBlank()) {
                                onSaveOffline(articleTitle, articleContent)
                            }
                        }
                    ) {
                        Text("Save offline", color = theme.text)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = theme.bg,
                    titleContentColor = theme.text,
                    navigationIconContentColor = theme.text
                )
            )
        },
        bottomBar = {
            ReadingControls(
                fontSize = fontSize,
                currentTheme = theme,
                onFontDecrease = { if (fontSize > 12) fontSize-- },
                onFontIncrease = { if (fontSize < 32) fontSize++ },
                onThemeSelect = { theme = it }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(theme.bg)
                .padding(innerPadding)
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                errorMsg.isNotBlank() -> {
                    Text(
                        text = errorMsg,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(24.dp)
                    )
                }
                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 20.dp, vertical = 16.dp)
                    ) {
                        Text(
                            text = articleTitle,
                            fontSize = (fontSize + 6).sp,
                            fontFamily = FontFamily.Serif,
                            color = theme.text,
                            lineHeight = ((fontSize + 6) * 1.35f).sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        ArticleBodyText(
                            html = articleContent,
                            fontSize = fontSize,
                            textColor = theme.text
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ReadingControls(
    fontSize: Int,
    currentTheme: ReadingTheme,
    onFontDecrease: () -> Unit,
    onFontIncrease: () -> Unit,
    onThemeSelect: (ReadingTheme) -> Unit
) {
    Surface(
        color = currentTheme.bg,
        shadowElevation = 8.dp,
        tonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                TextButton(onClick = onFontDecrease) {
                    Text("A", fontSize = 14.sp, color = currentTheme.text)
                }
                Text(text = "${fontSize}sp", color = currentTheme.text, fontSize = 12.sp)
                TextButton(onClick = onFontIncrease) {
                    Text("A", fontSize = 22.sp, color = currentTheme.text)
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                ReadingTheme.entries.forEach { t ->
                    val selected = t == currentTheme
                    OutlinedButton(
                        onClick = { onThemeSelect(t) },
                        border = BorderStroke(
                            width = if (selected) 2.dp else 1.dp,
                            color = if (selected) t.text else t.text.copy(alpha = 0.35f)
                        ),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (selected) t.text.copy(alpha = 0.12f)
                                            else Color.Transparent,
                            contentColor = t.text
                        ),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Text(t.label, fontSize = 12.sp, color = t.text)
                    }
                }
            }
        }
    }
}

@Composable
private fun ArticleBodyText(html: String, fontSize: Int, textColor: Color) {
    val text = remember(html) {
        Jsoup.parse(html).wholeText()
            .lines()
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .joinToString("\n\n")
    }
    Text(
        text = text,
        fontSize = fontSize.sp,
        fontFamily = FontFamily.Serif,
        color = textColor,
        lineHeight = (fontSize * 1.65f).sp
    )
}
