package com.example.Lumen.screens

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraEnhance
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import com.google.android.datatransport.BuildConfig
import com.example.Lumen.components.DocumentItem
import com.example.Lumen.components.StatCard
import com.example.Lumen.ui.theme.AppTheme
import com.example.Lumen.utils.PdfGenerator
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.*
import com.google.mlkit.vision.documentscanner.GmsDocumentScanning
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult
import kotlinx.coroutines.launch
import java.io.File
import java.text.DecimalFormat

@Composable
fun HomeScreen(
    viewModel: com.example.Lumen.data.HomeViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var isProcessing by remember { mutableStateOf(false) }

    val documents by viewModel.documents.collectAsState(initial = emptyList())
    val docCount by viewModel.totalDocs.collectAsState()
    val storageSizeBytes by viewModel.totalStorage.collectAsState(initial = 0L)

    val storageUsedFormatted = remember(storageSizeBytes) {
        val size = storageSizeBytes ?: 0L
        val kb = size / 1024.0
        val mb = kb / 1024.0
        val gb = mb / 1024.0
        when {
            gb >= 1 -> "%.2f GB".format(gb)
            mb >= 1 -> "%.2f MB".format(mb)
            kb >= 1 -> "%.2f KB".format(kb)
            else -> "$size B"
        }
    }

    val options = GmsDocumentScannerOptions.Builder()
        .setGalleryImportAllowed(true)
        .setResultFormats(RESULT_FORMAT_PDF, RESULT_FORMAT_JPEG)
        .setScannerMode(SCANNER_MODE_FULL)
        .build()

    val scanner = remember { GmsDocumentScanning.getClient(options) }

    val scannerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val scanningResult = GmsDocumentScanningResult.fromActivityResultIntent(result.data)
            val pages = scanningResult?.pages

            if (!pages.isNullOrEmpty()) {
                isProcessing = true
                scope.launch {
                    val prefs = context.getSharedPreferences("app_settings", android.content.Context.MODE_PRIVATE)
                    val quality = prefs.getString("compression_level", "Medium") ?: "Medium"

                    val resultData = PdfGenerator.generateCompressedPdf(
                        context,
                        pages.map { it.imageUri },
                        quality
                    )

                    if (resultData != null) {
                        viewModel.addDocument(
                            resultData.name,
                            resultData.uriString,
                            resultData.size,
                            pages.size
                        )
                        Toast.makeText(context, "Saved successfully!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Error saving document", Toast.LENGTH_SHORT).show()
                    }
                    isProcessing = false
                }
            }
        }
    }

    Scaffold(
        containerColor = AppTheme.colors.background,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    scanner.getStartScanIntent(context as android.app.Activity)
                        .addOnSuccessListener { intentSender ->
                            scannerLauncher.launch(
                                IntentSenderRequest.Builder(intentSender).build()
                            )
                        }
                },
                containerColor = AppTheme.colors.floating,
                contentColor = Color.White,
                shape = RoundedCornerShape(22.dp),
                text = { Text("Scan Document", fontWeight = FontWeight.Bold) },
                icon = { Icon(Icons.Default.CameraEnhance, contentDescription = null) }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier.padding(padding).padding(horizontal = 20.dp)
            ) {
                item {
                    Text("Stats", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = AppTheme.colors.fontLogo, modifier = Modifier.padding(vertical = 16.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        StatCard(
                            title = "Documents\ncreated",
                            value = docCount.toString(),
                            containerColor = AppTheme.colors.docsCreated,
                            contentColor = AppTheme.colors.docFont,
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            title = "Storage\noccupied",
                            value = storageUsedFormatted,
                            containerColor = AppTheme.colors.storageName,
                            contentColor = AppTheme.colors.sFont,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Text("Documents", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = AppTheme.colors.fontLogo, modifier = Modifier.padding(top = 24.dp, bottom = 12.dp))
                }

                if (documents.isEmpty()) {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().padding(40.dp), contentAlignment = Alignment.Center) {
                            Text("No documents yet. Tap + New!", color = AppTheme.colors.fontLogo.copy(alpha = 0.5f))
                        }
                    }
                } else {
                    items(items = documents, key = { it.id }) { doc ->
                        val sizeMb = DecimalFormat("#.##").format(doc.sizeBytes.toDouble() / (1024 * 1024))
                        DocumentItem(
                            name = doc.name,
                            size = "${sizeMb}mb",
                            pages = doc.pageCount,
                            uri = doc.uri,
                            onDelete = { viewModel.deleteDocument(doc) },
                            onRename = { newName -> viewModel.renameDocument(doc, newName) },
                            onClick = { openDocument(context, doc.uri) },
                            onShare = { shareDocument(context, doc.uri) }
                        )
                    }
                }
            }

            if (isProcessing) {
                Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.5f)), contentAlignment = Alignment.Center) {
                    Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = AppTheme.colors.defaultCard)) {
                        Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(color = AppTheme.colors.fontLogo)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("Processing...", color = AppTheme.colors.fontLogo, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

fun openDocument(context: android.content.Context, uriString: String) {
    try {
        val uri = Uri.parse(uriString)
        val intent = Intent(Intent.ACTION_VIEW)

        if (uri.scheme == "file") {
            val file = File(uri.path!!)
            if (!file.exists()) {
                Toast.makeText(context, "File not found! It may have been deleted.", Toast.LENGTH_LONG).show()
                return
            }
            val authority = "${BuildConfig.APPLICATION_ID}.provider"

            val contentUri = FileProvider.getUriForFile(context, authority, file)
            intent.setDataAndType(contentUri, "application/pdf")
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        else {
            intent.setDataAndType(uri, "application/pdf")
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        context.startActivity(intent)

    } catch (e: Exception) {
        e.printStackTrace()
        if (e is android.content.ActivityNotFoundException) {
            Toast.makeText(context, "No PDF Viewer app found!", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}

fun shareDocument(context: android.content.Context, uriString: String) {
    try {
        val uri = Uri.parse(uriString)
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        if (uri.scheme == "file") {
            val file = java.io.File(uri.path!!)
            if (!file.exists()) {
                Toast.makeText(context, "File not found", Toast.LENGTH_SHORT).show()
                return
            }
            val authority = "${BuildConfig.APPLICATION_ID}.provider"
            val contentUri = androidx.core.content.FileProvider.getUriForFile(context, authority, file)

            intent.putExtra(Intent.EXTRA_STREAM, contentUri)
        } else {
            intent.putExtra(Intent.EXTRA_STREAM, uri)
        }

        context.startActivity(Intent.createChooser(intent, "Share PDF"))

    } catch (e: Exception) {
        Toast.makeText(context, "Error sharing file", Toast.LENGTH_SHORT).show()
    }
}