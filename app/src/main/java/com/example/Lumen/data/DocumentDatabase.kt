package com.example.Lumen.data

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// 1. Entity
@Entity(tableName = "documents")
data class DocumentEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val uri: String,
    val sizeBytes: Long,
    val pageCount: Int,
    val createdAt: Long = System.currentTimeMillis()
)

// 2. DAO
@Dao
interface DocumentDao {
    @Query("SELECT * FROM documents ORDER BY createdAt DESC")
    fun getAllDocs(): Flow<List<DocumentEntity>>

    @Insert
    suspend fun insert(doc: DocumentEntity)

    @Delete
    suspend fun delete(doc: DocumentEntity)

    @Update
    suspend fun update(doc: DocumentEntity)

    @Query("SELECT SUM(sizeBytes) FROM documents")
    fun getTotalSize(): Flow<Long?>
}

// 3. Database
@Database(entities = [DocumentEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun documentDao(): DocumentDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(context, AppDatabase::class.java, "scanner_db")
                    .build().also { INSTANCE = it }
            }
        }
    }
}

// 4. ViewModel
class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getDatabase(application).documentDao()
    private val userPrefs = UserPreferences(application)

    // A. Live Database List
    val documents = dao.getAllDocs()

    // B. Storage Stats (Live from DB)
    val totalStorage = dao.getTotalSize()

    // C. Persistent Stats (From UserPreferences)

    // 1. Total Pages
    private val _lifetimePages = MutableStateFlow(userPrefs.lifetimePages)
    val totalPageCount = _lifetimePages.asStateFlow()

    // 2. Total Docs (FIXED: Added this missing variable)
    private val _totalDocs = MutableStateFlow(userPrefs.lifetimeDocs)
    val totalDocs = _totalDocs.asStateFlow()

    // 3. Average
    private val _averagePages = MutableStateFlow(calculateAverage())
    val averagePages = _averagePages.asStateFlow()

    // 4. Journey Start
    private val _journeyStartTime = MutableStateFlow(userPrefs.firstScanTime)
    val journeyStartTime = _journeyStartTime.asStateFlow()


    fun addDocument(name: String, uri: String, size: Long, pageCount: Int) {
        viewModelScope.launch {
            val newDoc = DocumentEntity(
                name = name,
                uri = uri,
                sizeBytes = size,
                pageCount = pageCount,
                createdAt = System.currentTimeMillis()
            )
            dao.insert(newDoc)
            updateLifetimeStats(pageCount)
        }
    }

    fun deleteDocument(doc: DocumentEntity) {
        viewModelScope.launch {
            dao.delete(doc)
        }
    }

    fun renameDocument(doc: DocumentEntity, newName: String) {
        viewModelScope.launch {
            dao.update(doc.copy(name = newName))
        }
    }

    private fun updateLifetimeStats(newPageCount: Int) {
        // Update Pages
        val currentPages = userPrefs.lifetimePages
        val newTotalPages = currentPages + newPageCount
        userPrefs.lifetimePages = newTotalPages
        _lifetimePages.value = newTotalPages

        // Update Docs (FIXED: Now updates the public flow too)
        val currentDocs = userPrefs.lifetimeDocs
        val newTotalDocs = currentDocs + 1
        userPrefs.lifetimeDocs = newTotalDocs
        _totalDocs.value = newTotalDocs

        // Update Time
        if (userPrefs.firstScanTime == 0L) {
            val now = System.currentTimeMillis()
            userPrefs.firstScanTime = now
            _journeyStartTime.value = now
        }

        // Update Average
        _averagePages.value = calculateAverage()
    }

    private fun calculateAverage(): Float {
        val pages = userPrefs.lifetimePages
        val docs = userPrefs.lifetimeDocs
        if (docs == 0) return 0f
        return pages.toFloat() / docs
    }
}