package com.jeetracker.app.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.Date

// API Response Models
data class ApiResponse<T>(
    val result: ApiResult<T>
)

data class ApiResult<T>(
    val data: T
)

// User Models
@Entity(tableName = "users")
data class User(
    @PrimaryKey
    val id: Int,
    val openId: String,
    val name: String?,
    val email: String?,
    val role: String = "user",
    val createdAt: Date = Date(),
    val updatedAt: Date = Date(),
    val lastSignedIn: Date = Date()
)

// Subject Models
@Entity(tableName = "subjects")
data class Subject(
    @PrimaryKey
    val id: Int,
    val name: String,
    val description: String? = null
)

// Category Models
@Entity(tableName = "categories")
data class Category(
    @PrimaryKey
    val id: Int,
    val subjectId: Int,
    val name: String
)

// Topic Models
@Entity(tableName = "topics")
data class Topic(
    @PrimaryKey
    val id: Int,
    val categoryId: Int,
    val name: String,
    val description: String? = null
)

// User Progress Models
@Entity(tableName = "user_progress")
data class UserProgress(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int,
    val topicId: Int,
    val completed: Boolean = false,
    val completedAt: Date? = null
)

// Subscription Models
@Entity(tableName = "subscriptions")
data class Subscription(
    @PrimaryKey
    val id: Int,
    val userId: Int,
    val plan: String, // "free", "trial", "monthly", "yearly"
    val status: String, // "active", "inactive", "cancelled"
    val stripeCustomerId: String? = null,
    val stripeSubscriptionId: String? = null,
    val startDate: Date = Date(),
    val endDate: Date? = null,
    val createdAt: Date = Date()
)

// Payment Models
@Entity(tableName = "payments")
data class Payment(
    @PrimaryKey
    val id: Int,
    val userId: Int,
    val amount: Double,
    val currency: String = "INR",
    val status: String, // "pending", "completed", "failed"
    val stripePaymentIntentId: String? = null,
    val planId: String,
    val createdAt: Date = Date()
)

// Notes Models
@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int,
    val topicId: Int,
    val content: String,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
)

// Recommendations Models
@Entity(tableName = "recommendations")
data class Recommendation(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int,
    val content: String,
    val isRead: Boolean = false,
    val createdAt: Date = Date()
)

// Progress Statistics
data class ProgressStats(
    val totalTopics: Int,
    val completedTopics: Int,
    val percentage: Int,
    val bySubject: List<SubjectStats> = emptyList(),
    val byCategory: List<CategoryStats> = emptyList()
)

data class SubjectStats(
    val id: Int,
    val name: String,
    val total: Int,
    val completed: Int
)

data class CategoryStats(
    val id: Int,
    val name: String,
    val total: Int,
    val completed: Int
)

// Combined Models for UI
data class TopicWithProgress(
    val topic: Topic,
    val completed: Boolean
)

data class CategoryWithTopics(
    val category: Category,
    val topics: List<TopicWithProgress>
)

data class SubjectWithCategories(
    val subject: Subject,
    val categories: List<CategoryWithTopics>
)
