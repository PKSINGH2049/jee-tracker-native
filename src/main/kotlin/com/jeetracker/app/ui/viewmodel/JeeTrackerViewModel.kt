package com.jeetracker.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeetracker.app.data.api.ApiService
import com.jeetracker.app.data.database.AppDatabase
import com.jeetracker.app.data.models.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class JeeTrackerViewModel(
    private val apiService: ApiService,
    private val database: AppDatabase
) : ViewModel() {

    // UI State
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    // User
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    // Progress
    private val _progressStats = MutableStateFlow<ProgressStats?>(null)
    val progressStats: StateFlow<ProgressStats?> = _progressStats.asStateFlow()

    // Subjects
    private val _subjects = MutableStateFlow<List<Subject>>(emptyList())
    val subjects: StateFlow<List<Subject>> = _subjects.asStateFlow()

    // Topics
    private val _topics = MutableStateFlow<List<Topic>>(emptyList())
    val topics: StateFlow<List<Topic>> = _topics.asStateFlow()

    // Subscription
    private val _subscription = MutableStateFlow<Subscription?>(null)
    val subscription: StateFlow<Subscription?> = _subscription.asStateFlow()

    // Notes
    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes.asStateFlow()

    // Recommendations
    private val _recommendations = MutableStateFlow<List<Recommendation>>(emptyList())
    val recommendations: StateFlow<List<Recommendation>> = _recommendations.asStateFlow()

    // Initialize
    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            try {
                _uiState.value = UiState.Loading
                
                // Load user
                val user = apiService.getMe().result.data
                _currentUser.value = user
                database.userDao().insertUser(user)

                // Load subjects
                val subjects = apiService.getSubjects().result.data
                _subjects.value = subjects
                database.subjectDao().insertSubjects(subjects)

                // Load progress
                val stats = apiService.getProgress().result.data
                _progressStats.value = stats

                // Load subscription
                val subscription = apiService.getSubscription().result.data
                _subscription.value = subscription

                _uiState.value = UiState.Success
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun loadTopicsBySubject(subjectId: Int) {
        viewModelScope.launch {
            try {
                val request = com.jeetracker.app.data.api.SubjectRequest(subjectId)
                val topics = apiService.getTopicsBySubject(request).result.data
                _topics.value = topics
                database.topicDao().insertTopics(topics)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Failed to load topics")
            }
        }
    }

    fun toggleTopic(topicId: Int) {
        viewModelScope.launch {
            try {
                val request = com.jeetracker.app.data.api.TopicRequest(topicId)
                apiService.toggleTopic(request)
                
                // Refresh progress
                val stats = apiService.getProgress().result.data
                _progressStats.value = stats
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Failed to toggle topic")
            }
        }
    }

    fun resetProgress(subjectId: Int? = null) {
        viewModelScope.launch {
            try {
                val request = com.jeetracker.app.data.api.ResetProgressRequest(subjectId)
                apiService.resetProgress(request)
                
                // Refresh progress
                val stats = apiService.getProgress().result.data
                _progressStats.value = stats
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Failed to reset progress")
            }
        }
    }

    fun loadNotes() {
        viewModelScope.launch {
            try {
                val notes = apiService.getUserNotes().result.data
                _notes.value = notes
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Failed to load notes")
            }
        }
    }

    fun createNote(topicId: Int, content: String) {
        viewModelScope.launch {
            try {
                val request = com.jeetracker.app.data.api.NoteRequest(topicId, content)
                apiService.createNote(request)
                loadNotes()
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Failed to create note")
            }
        }
    }

    fun deleteNote(topicId: Int) {
        viewModelScope.launch {
            try {
                val request = com.jeetracker.app.data.api.DeleteNoteRequest(topicId)
                apiService.deleteNote(request)
                loadNotes()
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Failed to delete note")
            }
        }
    }

    fun loadRecommendations() {
        viewModelScope.launch {
            try {
                val request = com.jeetracker.app.data.api.RecommendationsRequest()
                val recommendations = apiService.getRecommendations(request).result.data
                _recommendations.value = recommendations
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Failed to load recommendations")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                apiService.logout()
                _currentUser.value = null
                _uiState.value = UiState.LoggedOut
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Failed to logout")
            }
        }
    }
}

sealed class UiState {
    object Loading : UiState()
    object Success : UiState()
    object LoggedOut : UiState()
    data class Error(val message: String) : UiState()
}
