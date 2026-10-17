package com.you.framenotes.exif

import android.content.Context
import android.provider.DocumentsContract
import android.provider.DocumentsContract.Document
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.you.framenotes.data.FolderPrefs
import com.you.framenotes.data.PhotoDao
import com.you.framenotes.data.PhotoEntity
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltWorker
class IndexWorker @AssistedInject constructor(
    @Assisted private val ctx: Context,
    @Assisted params: WorkerParameters,
    private val dao: PhotoDao,
    private val reader: ExifReader,
    private val prefs: FolderPrefs,
) : CoroutineWorker(ctx, params) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val root = prefs.treeUri() ?: return@withContext Result.failure()
        val known = dao.snapshot().associateBy { it.uri }
        val seen = HashSet<String>()
        val batch = ArrayList<PhotoEntity>(50)
        var done = 0

        for (entry in walk(root)) {
            if (isStopped) return@withContext Result.retry()
            val key = entry.uri.toString()
            seen += key
            val stamp = known[key]
            if (stamp != null && stamp.lastModified == entry.lastModified && !stamp.missing) continue

            reader.read(entry, existingId = stamp?.id ?: 0)?.let { batch += it }
            if (batch.size >= 50) { dao.upsert(batch.toList()); batch.clear() }
            if (++done % 25 == 0) setProgress(workDataOf("done" to done))
        }
        if (batch.isNotEmpty()) dao.upsert(batch.toList())

        known.values.filter { it.uri !in seen && !it.missing }
            .map { it.id }.chunked(500).forEach { dao.markMissing(it) }

        Result.success(workDataOf("done" to done))
    }

    private fun walk(root: android.net.Uri): Sequence<DocEntry> = sequence {
        val resolver = ctx.contentResolver
        val stack = java.util.ArrayDeque<String>().apply { add(DocumentsContract.getTreeDocumentId(root)) }
        val cols = arrayOf(
            Document.COLUMN_DOCUMENT_ID, Document.COLUMN_DISPLAY_NAME, Document.COLUMN_MIME_TYPE,
            Document.COLUMN_LAST_MODIFIED, Document.COLUMN_SIZE
        )
        while (stack.isNotEmpty()) {
            val parentId = stack.removeLast()
            val children = DocumentsContract.buildChildDocumentsUriUsingTree(root, parentId)
            resolver.query(children, cols, null, null, null)?.use { c ->
                while (c.moveToNext()) {
                    val id = c.getString(0)
                    val mime = c.getString(2) ?: continue
                    when {
                        mime == Document.MIME_TYPE_DIR -> stack.add(id)
                        mime.startsWith("image/") -> yield(
                            DocEntry(
                                DocumentsContract.buildDocumentUriUsingTree(root, id),
                                c.getString(1) ?: id, c.getLong(3), c.getLong(4)
                            )
                        )
                    }
                }
            }
        }
    }
}
