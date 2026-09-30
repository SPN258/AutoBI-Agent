package com.spn258.siyue

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.json.JSONArray
import org.json.JSONObject

private val Paper = Color(0xFFF4EFE4)
private val Ink = Color(0xFF151311)
private val Saffron = Color(0xFFC77B30)
private val Pine = Color(0xFF284B3E)
private val Sand = Color(0xFFE7DDC9)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { SiyueTheme { SiyueApp() } }
    }
}

private enum class Tab(val label: String, val icon: ImageVector) {
    Library("书架", Icons.Default.LibraryBooks),
    Search("搜书", Icons.Default.Search),
    Sources("书源", Icons.Default.Source),
}

@Composable
private fun SiyueTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Ink,
            onPrimary = Paper,
            secondary = Saffron,
            tertiary = Pine,
            background = Paper,
            surface = Color(0xFFFBF8F1),
            onSurface = Ink,
        ),
        content = content,
    )
}

@Composable
private fun SiyueApp() {
    var tab by rememberSaveable { mutableStateOf(Tab.Library) }
    var readerOpen by rememberSaveable { mutableStateOf(false) }
    var settingsOpen by rememberSaveable { mutableStateOf(false) }

    when {
        readerOpen -> ReaderScreen(onBack = { readerOpen = false })
        settingsOpen -> SettingsScreen(onBack = { settingsOpen = false })
        else -> Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            topBar = { EditorialHeader(onSettings = { settingsOpen = true }) },
            bottomBar = { InkDock(selected = tab, onSelect = { tab = it }) },
        ) { padding ->
            Box(Modifier.fillMaxSize().padding(padding)) {
                when (tab) {
                    Tab.Library -> LibraryScreen(onRead = { readerOpen = true })
                    Tab.Search -> SearchScreen(onOpenSources = { tab = Tab.Sources })
                    Tab.Sources -> SourcesScreen()
                }
            }
        }
    }
}

@Composable
private fun EditorialHeader(onSettings: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 22.dp, vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(Modifier.weight(1f)) {
            Text("私阅", fontFamily = FontFamily.Serif, fontWeight = FontWeight.Black, fontSize = 30.sp)
            Text("SIYUE · PRIVATE READING", letterSpacing = 2.sp, fontSize = 10.sp, color = Saffron)
        }
        IconButton(onClick = onSettings) { Icon(Icons.Default.Settings, contentDescription = "设置") }
    }
}

@Composable
private fun InkDock(selected: Tab, onSelect: (Tab) -> Unit) {
    Row(
        Modifier.fillMaxWidth().background(Ink).navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        Tab.entries.forEach { tab ->
            val active = tab == selected
            Column(
                Modifier.clip(RoundedCornerShape(22.dp)).clickable { onSelect(tab) }
                    .background(if (active) Paper else Color.Transparent)
                    .padding(horizontal = 22.dp, vertical = 9.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(tab.icon, contentDescription = tab.label, tint = if (active) Ink else Paper, modifier = Modifier.size(20.dp))
                Text(tab.label, color = if (active) Ink else Paper, fontSize = 11.sp, modifier = Modifier.padding(top = 3.dp))
            }
        }
    }
}

@Composable
private fun LibraryScreen(onRead: () -> Unit) {
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        Text("今天读什么？", fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, fontSize = 26.sp)

        Card(
            modifier = Modifier.fillMaxWidth().clickable(onClick = onRead),
            shape = RoundedCornerShape(30.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        ) {
            Column(
                Modifier.background(Brush.linearGradient(listOf(Pine, Color(0xFF17332A)))).padding(24.dp)
            ) {
                Text("正在读", color = Color(0xFFBFD8CB), fontSize = 12.sp)
                Spacer(Modifier.height(34.dp))
                Text("风停在旧站台", color = Paper, fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, fontSize = 28.sp)
                Text("原创演示短篇 · 18%", color = Paper.copy(alpha = .72f))
                Spacer(Modifier.height(18.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.height(4.dp).weight(1f).clip(CircleShape).background(Paper.copy(alpha = .18f))) {
                        Box(Modifier.fillMaxWidth(.18f).height(4.dp).background(Saffron))
                    }
                    Spacer(Modifier.width(12.dp))
                    Icon(Icons.Default.Book, null, tint = Paper)
                }
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard("1", "本地书籍", Modifier.weight(1f))
            StatCard("0", "已接书源", Modifier.weight(1f))
            StatCard("18%", "本周进度", Modifier.weight(1f))
        }
        Text("书架不是仓库，是你愿意继续读的东西。", color = Ink.copy(alpha = .62f))
        Spacer(Modifier.height(20.dp))
    }
}

@Composable
private fun StatCard(value: String, label: String, modifier: Modifier = Modifier) {
    Card(modifier, shape = RoundedCornerShape(22.dp), colors = CardDefaults.cardColors(containerColor = Sand.copy(alpha = .65f))) {
        Column(Modifier.padding(16.dp)) {
            Text(value, fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, fontSize = 22.sp)
            Text(label, fontSize = 11.sp, color = Ink.copy(alpha = .62f))
        }
    }
}

@Composable
private fun SearchScreen(onOpenSources: () -> Unit) {
    var query by rememberSaveable { mutableStateOf("") }
    Column(
        Modifier.fillMaxSize().padding(horizontal = 20.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text("搜书", fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, fontSize = 28.sp)
        Text("只搜索你自己导入的书源。产品层与书源执行层彼此独立。", color = Ink.copy(alpha = .62f))
        OutlinedTextField(
            value = query, onValueChange = { query = it }, modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp), leadingIcon = { Icon(Icons.Default.Search, null) },
            placeholder = { Text("书名、作者或关键词") }, singleLine = true,
        )
        Card(shape = RoundedCornerShape(28.dp), colors = CardDefaults.cardColors(containerColor = Ink), modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(22.dp)) {
                Text("书源执行层尚未接入", color = Saffron, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Text("这一版故意不做“伪 Legado 兼容”。下一阶段会把成熟规则解析能力接到这个全新 UI，而不是把旧 UI 搬回来。", color = Paper.copy(alpha = .82f))
                Spacer(Modifier.height(16.dp))
                OutlinedButton(onClick = onOpenSources) { Text("先导入 / 管理书源") }
            }
        }
    }
}

@Composable
private fun SourcesScreen() {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("sources", 0) }
    val sources = remember {
        mutableStateListOf<ImportedSource>().apply {
            val raw = prefs.getString("items", null)
            if (!raw.isNullOrBlank()) {
                val array = JSONArray(raw)
                repeat(array.length()) { index ->
                    val item = array.getJSONObject(index)
                    add(ImportedSource(item.getString("name"), item.getString("url"), item.getString("raw")))
                }
            }
        }
    }
    var input by rememberSaveable { mutableStateOf("") }
    var message by rememberSaveable { mutableStateOf<String?>(null) }

    fun persist() {
        val array = JSONArray()
        sources.forEach { source ->
            array.put(JSONObject().put("name", source.name).put("url", source.url).put("raw", source.rawJson))
        }
        prefs.edit().putString("items", array.toString()).apply()
    }

    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text("我的书源", fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, fontSize = 28.sp)
        Text("这里保存的是你自己导入的配置。当前只做格式验证和本地保存，不冒充完整规则兼容。", color = Ink.copy(alpha = .62f))
        OutlinedTextField(
            value = input, onValueChange = { input = it }, modifier = Modifier.fillMaxWidth().height(180.dp),
            shape = RoundedCornerShape(24.dp), placeholder = { Text("粘贴 Legado 书源 JSON…") },
        )
        Button(
            onClick = {
                SourceDraftParser.parse(input).fold(
                    onSuccess = { source ->
                        sources.removeAll { old -> old.url == source.url }
                        sources.add(source)
                        persist()
                        message = "已保存：${source.name}"
                        input = ""
                    },
                    onFailure = { error -> message = error.message ?: "格式错误" }
                )
            },
            modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp),
        ) {
            Icon(Icons.Default.Add, null)
            Spacer(Modifier.width(8.dp))
            Text("验证并保存")
        }
        message?.let { text ->
            Text(text, color = if (text.startsWith("已保存")) Pine else Color(0xFF9A3324))
        }
        sources.forEach { source ->
            Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFFFBF8F1))) {
                Row(Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(42.dp).clip(CircleShape).background(Pine), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Check, null, tint = Paper)
                    }
                    Column(Modifier.weight(1f).padding(start = 14.dp)) {
                        Text(source.name, fontWeight = FontWeight.Bold)
                        Text(source.url, fontSize = 11.sp, color = Ink.copy(alpha = .55f))
                    }
                    TextButton(onClick = { sources.remove(source); persist() }) { Text("移除") }
                }
            }
        }
        Spacer(Modifier.height(20.dp))
    }
}

@Composable
private fun ReaderScreen(onBack: () -> Unit) {
    var fontSize by rememberSaveable { mutableFloatStateOf(20f) }
    val paragraph = "风停在旧站台。天色还没有完全暗下来，铁轨尽头留着一条很薄的橙色。她把书翻到夹着票根的那一页，没有急着继续，只是听远处的风穿过空棚。这里是一段原创演示文字，用来验证阅读器自己的排版、留白和阅读节奏。"
    Scaffold(
        containerColor = Color(0xFFF1E8D6),
        topBar = {
            Row(Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "返回") }
                Text("风停在旧站台", fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold)
            }
        },
        bottomBar = {
            Row(
                Modifier.fillMaxWidth().background(Ink).navigationBarsPadding().padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically,
            ) {
                TextButton(onClick = { fontSize = (fontSize - 1).coerceAtLeast(16f) }) { Text("A−", color = Paper) }
                Text("${fontSize.toInt()} pt", color = Paper.copy(alpha = .7f))
                TextButton(onClick = { fontSize = (fontSize + 1).coerceAtMost(30f) }) { Text("A+", color = Paper) }
            }
        }
    ) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(horizontal = 28.dp, vertical = 30.dp)
        ) {
            Text("第一章", color = Saffron, fontSize = 12.sp, letterSpacing = 2.sp)
            Text("晚风之前", fontFamily = FontFamily.Serif, fontWeight = FontWeight.Black, fontSize = 32.sp, modifier = Modifier.padding(top = 10.dp, bottom = 32.dp))
            repeat(6) {
                Text(paragraph, fontFamily = FontFamily.Serif, fontSize = fontSize.sp, lineHeight = (fontSize * 1.72f).sp, modifier = Modifier.padding(bottom = 24.dp))
            }
        }
    }
}

@Composable
private fun SettingsScreen(onBack: () -> Unit) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            Row(Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "返回") }
                Text("设置", fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, fontSize = 22.sp)
            }
        }
    ) { padding ->
        Column(Modifier.padding(padding).padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            SettingCard("阅读外观", "字号、行距、纸张与夜间模式将在这里统一管理")
            SettingCard("书源与隐私", "不内置公共书源，不上传你的书源配置")
            SettingCard("关于私阅", "全新产品层 · 0.1.0")
        }
    }
}

@Composable
private fun SettingCard(title: String, body: String) {
    Card(shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFFFBF8F1)), modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(18.dp)) {
            Text(title, fontWeight = FontWeight.Bold)
            Text(body, color = Ink.copy(alpha = .58f), fontSize = 12.sp, modifier = Modifier.padding(top = 4.dp))
        }
    }
}
