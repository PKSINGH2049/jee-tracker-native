package com.jeetracker.app.data.api

import com.jeetracker.app.data.models.*
import retrofit2.http.*

interface ApiService {
    // Auth endpoints
    @GET("auth.me")
    suspend fun getMe(): ApiResponse<User>

    @POST("auth.logout")
    suspend fun logout(): ApiResponse<Map<String, Boolean>>

    // Topics endpoints
    @GET("topics.getSubjects")
    suspend fun getSubjects(): ApiResponse<List<Subject>>

    @POST("topics.getBySubject")
    suspend fun getTopicsBySubject(@Body request: SubjectRequest): ApiResponse<List<Topic>>

    @POST("topics.getCategoriesBySubject")
    suspend fun getCategoriesBySubject(@Body request: SubjectRequest): ApiResponse<List<Category>>

    // Progress endpoints
    @GET("progress.getStats")
    suspend fun getProgress(): ApiResponse<ProgressStats>

    @POST("progress.toggleTopic")
    suspend fun toggleTopic(@Body request: TopicRequest): ApiResponse<Map<String, Boolean>>

    @POST("progress.resetProgress")
    suspend fun resetProgress(@Body request: ResetProgressRequest? = null): ApiResponse<Map<String, Boolean>>

    // Payment endpoints
    @GET("payment.getSubscription")
    suspend fun getSubscription(): ApiResponse<Subscription>

    @POST("payment.createCheckoutSession")
    suspend fun createCheckoutSession(@Body request: CheckoutRequest): ApiResponse<CheckoutResponse>

    @GET("payment.getPaymentHistory")
    suspend fun getPaymentHistory(): ApiResponse<List<Payment>>

    // Analytics endpoints
    @GET("payment.getAnalytics")
    suspend fun getAnalytics(): ApiResponse<ProgressStats>

    // Recommendations endpoints
    @POST("payment.getRecommendations")
    suspend fun getRecommendations(@Body request: RecommendationsRequest): ApiResponse<List<Recommendation>>

    // Notes endpoints
    @GET("payment.getUserNotes")
    suspend fun getUserNotes(): ApiResponse<List<Note>>

    @POST("payment.createNote")
    suspend fun createNote(@Body request: NoteRequest): ApiResponse<Note>

    @POST("payment.updateNote")
    suspend fun updateNote(@Body request: NoteRequest): ApiResponse<Note>

    @POST("payment.deleteNote")
    suspend fun deleteNote(@Body request: DeleteNoteRequest): ApiResponse<Map<String, Boolean>>

    // PDF Export endpoints
    @POST("payment.exportProgressPDF")
    suspend fun exportProgressPDF(): ApiResponse<PdfExportResponse>

    @POST("payment.exportAnalyticsPDF")
    suspend fun exportAnalyticsPDF(): ApiResponse<PdfExportResponse>
}

// Request models
data class SubjectRequest(
    val subjectId: Int
)

data class TopicRequest(
    val topicId: Int
)

data class ResetProgressRequest(
    val subjectId: Int? = null
)

data class CheckoutRequest(
    val planId: String
)

data class CheckoutResponse(
    val checkoutUrl: String?,
    val sessionId: String?
)

data class RecommendationsRequest(
    val unreadOnly: Boolean = false
)

data class NoteRequest(
    val topicId: Int,
    val content: String
)

data class DeleteNoteRequest(
    val topicId: Int
)

data class PdfExportResponse(
    val success: Boolean,
    val filename: String?,
    val size: Long?,
    val url: String?
)
