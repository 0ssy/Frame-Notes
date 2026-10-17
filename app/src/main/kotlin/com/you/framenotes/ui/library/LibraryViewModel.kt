package com.you.framenotes.ui.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.you.framenotes.data.FolderPrefs
import com.you.framenotes.data.PhotoDao
import com.you.framenotes.data.PhotoEntity
import com.you.framenotes.exif.IndexWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.SharingStarted
import javax.inject.Inject

enum class SortOrder(val code: Int, val label: String) {
    DATE_DESC(0, "Newest"), DATE_ASC(1, "Oldest"), FOCAL_ASC(2, "Focal length"), ISO_DESC(3, "ISO")
}

data class LibraryFilter(
    val camera: String? = null, val minFocal: Int? = null, val maxFocal: Int? = null,
    val tagId: Long? = null, val query: String = "", val sort: SortOrder = SortOrder.DATE_DESC,
)

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val dao: PhotoDao,
    noteDao: com.you.framenotes.data.NoteDao,
    private val prefs: FolderPrefs,
    private val wm: WorkManager,
) : ViewModel() {

    private val _filter = MutableStateFlow(LibraryFilter())
    val filter = _filter.asStateFlow()

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val photos: Flow<PagingData<PhotoEntity>> = _filter.debounce(200).flatMapLatest { f ->
        Pager(PagingConfig(pageSize = 60, enablePlaceholders = true)) {
            dao.paged(f.camera, f.minFocal, f.maxFocal, f.tagId, f.query, f.sort.code)
        }.flow
    }.cachedIn(viewModelScope)

    val cameras = dao.cameras().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val tags = noteDao.allTags().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun update(block: (LibraryFilter) -> LibraryFilter) = _filter.update(block)
    fun hasFolder() = prefs.treeUri() != null
    fun onFolderChosen(uri: android.net.Uri) {
        prefs.save(uri)
        wm.enqueueUniqueWork("index", ExistingWorkPolicy.REPLACE, OneTimeWorkRequestBuilder<IndexWorker>().build())
    }
    fun rescan() { wm.enqueueUniqueWork("index", ExistingWorkPolicy.KEEP, OneTimeWorkRequestBuilder<IndexWorker>().build()) }
}
