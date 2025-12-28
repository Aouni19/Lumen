package com.example.Lumen.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.net.toUri
import java.io.File
import java.io.FileOutputStream

// 1. A Helper Class to return data regardless of where it was saved
data class GeneratedPdfResult(
    val name: String,
    val uriString: String, // Could be file:// or content://
    val size: Long
)

object PdfGenerator {

    fun generateCompressedPdf(
        context: Context,
        imageUris: List<Uri>,
        qualityLevel: String
    ): GeneratedPdfResult? {

        // --- 1. SET UP COMPRESSION ---
        val pdfDocument = PdfDocument()
        val paint = Paint()

        val (compressionQuality, targetWidth) = when (qualityLevel) {
            "High" -> Pair(50, 1024)
            "Low" -> Pair(80, 2048)
            else -> Pair(70, 1600)
        }

        try {
            // --- 2. DRAW IMAGES TO PDF ---
            imageUris.forEachIndexed { index, uri ->
                val inputStream = context.contentResolver.openInputStream(uri) ?: return@forEachIndexed

                // Decode Bounds
                val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
                BitmapFactory.decodeStream(inputStream, null, options)
                inputStream.close()

                // Calculate Scale
                options.inJustDecodeBounds = false
                options.inSampleSize = calculateInSampleSize(options, targetWidth)

                // Decode Actual Image
                val scaledStream = context.contentResolver.openInputStream(uri)
                val originalBitmap = BitmapFactory.decodeStream(scaledStream, null, options)
                scaledStream?.close()

                if (originalBitmap != null) {
                    // Compress
                    val compressedStream = java.io.ByteArrayOutputStream()
                    originalBitmap.compress(Bitmap.CompressFormat.JPEG, compressionQuality, compressedStream)
                    val compressedBytes = compressedStream.toByteArray()
                    val finalBitmap = BitmapFactory.decodeByteArray(compressedBytes, 0, compressedBytes.size)

                    // Draw to PDF
                    val pageInfo = PdfDocument.PageInfo.Builder(finalBitmap.width, finalBitmap.height, index + 1).create()
                    val page = pdfDocument.startPage(pageInfo)
                    page.canvas.drawBitmap(finalBitmap, 0f, 0f, paint)
                    pdfDocument.finishPage(page)

                    originalBitmap.recycle()
                    finalBitmap.recycle()
                }
            }

            // --- 3. SAVE THE FILE (THE NEW LOGIC) ---

            // A. Get User Preference
            val prefs = context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)
            val destination = prefs.getString("destination_type", "Downloads") ?: "Downloads"
            val fileName = "Scan_${System.currentTimeMillis()}.pdf"

            var resultUriString = ""
            var resultSize = 0L

            if (destination == "Downloads" || destination == "Documents") {
                // === PUBLIC STORAGE LOGIC (MediaStore) ===

                val relativePath = if (destination == "Downloads") Environment.DIRECTORY_DOWNLOADS else Environment.DIRECTORY_DOCUMENTS

                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                    put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        put(MediaStore.MediaColumns.RELATIVE_PATH, relativePath)
                    }
                }

                // Insert into the system database
                val contentUri = context.contentResolver.insert(MediaStore.Files.getContentUri("external"), contentValues)

                if (contentUri != null) {
                    // Open the stream and write the PDF
                    val outputStream = context.contentResolver.openOutputStream(contentUri)
                    outputStream?.use { pdfDocument.writeTo(it) }

                    resultUriString = contentUri.toString()
                    // Calculate size roughly or query it back. For speed, we estimate.
                    // Or better: open a file descriptor to get size.
                    // For simplicity in this step, we trust the write succeeded.

                    // To get exact size of the written file from ContentProvider:
                    context.contentResolver.openFileDescriptor(contentUri, "r")?.use {
                        resultSize = it.statSize
                    }
                }

            } else {
                // === INTERNAL/CUSTOM STORAGE LOGIC (App Sandbox) ===
                // Defaulting "Custom" to internal for now until folder picker is built

                val file = File(context.filesDir, fileName)
                val fileOutputStream = FileOutputStream(file)
                pdfDocument.writeTo(fileOutputStream)
                fileOutputStream.close()

                resultUriString = file.toUri().toString()
                resultSize = file.length()
            }

            pdfDocument.close()

            // Check if we succeeded
            if (resultUriString.isNotEmpty()) {
                return GeneratedPdfResult(fileName, resultUriString, resultSize)
            } else {
                return null
            }

        } catch (e: Exception) {
            e.printStackTrace()
            pdfDocument.close()
            return null
        }
    }

    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int): Int {
        val (height: Int, width: Int) = options.outHeight to options.outWidth
        var inSampleSize = 1
        if (width > reqWidth) {
            val halfWidth = width / 2
            while ((halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }
}