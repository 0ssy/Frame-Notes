package com.you.framenotes.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.you.framenotes.data.PhotoDao
import com.you.framenotes.data.PhotoNoteEntity
import com.you.framenotes.data.NoteDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedState: SavedStateHandle,
    dao: PhotoDao,
    private val notes: NoteDao
) : ViewModel() {
    private val id: Long = checkNotNull(savedState["photoId"]) { "Missing photoId" }
    val photo = dao.photo(id).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
    val note = notes.note(id).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
    val tags = notes.tagsFor(id).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun saveNote(text: String) = viewModelScope.launch {
        notes.saveNote(PhotoNoteEntity(id, text, System.currentTimeMillis()))
    }
    fun addTag(name: String) = viewModelScope.launch { notes.addTag(id, name) }
    fun removeTag(tagId: Long) = viewModelScope.launch { notes.unlink(id, tagId) }
}
