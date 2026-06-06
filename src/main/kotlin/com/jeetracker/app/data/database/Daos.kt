package com.jeetracker.app.data.database

import androidx.room.*
import com.jeetracker.app.data.models.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: Int): User?

    @Query("SELECT * FROM users WHERE openId = :openId")
    suspend fun getUserByOpenId(openId: String): User?

    @Delete
    suspend fun deleteUser(user: User)
}

@Dao
interface SubjectDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubjects(subjects: List<Subject>)

    @Query("SELECT * FROM subjects")
    fun getAllSubjects(): Flow<List<Subject>>

    @Query("SELECT * FROM subjects WHERE id = :id")
    suspend fun getSubjectById(id: Int): Subject?
}

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(categories: List<Category>)

    @Query("SELECT * FROM categories WHERE subjectId = :subjectId")
    fun getCategoriesBySubject(subjectId: Int): Flow<List<Category>>

    @Query("SELECT * FROM categories WHERE id = :id")
    suspend fun getCategoryById(id: Int): Category?
}

@Dao
interface TopicDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTopics(topics: List<Topic>)

    @Query("SELECT * FROM topics WHERE categoryId = :categoryId")
    fun getTopicsByCategory(categoryId: Int): Flow<List<Topic>>

    @Query("SELECT * FROM topics WHERE id = :id")
    suspend fun getTopicById(id: Int): Topic?

    @Query("SELECT * FROM topics")
    suspend fun getAllTopics(): List<Topic>
}

@Dao
interface ProgressDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgress(progress: UserProgress)

    @Query("SELECT * FROM user_progress WHERE userId = :userId AND topicId = :topicId")
    suspend fun getProgress(userId: Int, topicId: Int): UserProgress?

    @Query("SELECT * FROM user_progress WHERE userId = :userId")
    fun getUserProgress(userId: Int): Flow<List<UserProgress>>

    @Query("DELETE FROM user_progress WHERE userId = :userId")
    suspend fun deleteAllProgress(userId: Int)

    @Query("DELETE FROM user_progress WHERE userId = :userId AND topicId IN (SELECT id FROM topics WHERE categoryId IN (SELECT id FROM categories WHERE subjectId = :subjectId))")
    suspend fun deleteProgressBySubject(userId: Int, subjectId: Int)

    @Query("""
        SELECT COUNT(*) FROM user_progress 
        WHERE userId = :userId AND completed = 1
    """)
    suspend fun getCompletedCount(userId: Int): Int

    @Query("SELECT COUNT(*) FROM topics")
    suspend fun getTotalTopicsCount(): Int
}

@Dao
interface SubscriptionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubscription(subscription: Subscription)

    @Query("SELECT * FROM subscriptions WHERE userId = :userId ORDER BY createdAt DESC LIMIT 1")
    suspend fun getLatestSubscription(userId: Int): Subscription?

    @Query("SELECT * FROM subscriptions WHERE userId = :userId AND status = 'active'")
    suspend fun getActiveSubscription(userId: Int): Subscription?

    @Update
    suspend fun updateSubscription(subscription: Subscription)
}

@Dao
interface PaymentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayment(payment: Payment)

    @Query("SELECT * FROM payments WHERE userId = :userId ORDER BY createdAt DESC")
    fun getPaymentHistory(userId: Int): Flow<List<Payment>>

    @Query("SELECT * FROM payments WHERE stripePaymentIntentId = :intentId")
    suspend fun getPaymentByIntentId(intentId: String): Payment?

    @Update
    suspend fun updatePayment(payment: Payment)
}

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Query("SELECT * FROM notes WHERE userId = :userId AND topicId = :topicId")
    suspend fun getNote(userId: Int, topicId: Int): Note?

    @Query("SELECT * FROM notes WHERE userId = :userId ORDER BY updatedAt DESC")
    fun getUserNotes(userId: Int): Flow<List<Note>>

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)
}

@Dao
interface RecommendationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecommendation(recommendation: Recommendation)

    @Query("SELECT * FROM recommendations WHERE userId = :userId ORDER BY createdAt DESC")
    fun getUserRecommendations(userId: Int): Flow<List<Recommendation>>

    @Query("SELECT * FROM recommendations WHERE userId = :userId AND isRead = 0 ORDER BY createdAt DESC")
    fun getUnreadRecommendations(userId: Int): Flow<List<Recommendation>>

    @Update
    suspend fun updateRecommendation(recommendation: Recommendation)

    @Delete
    suspend fun deleteRecommendation(recommendation: Recommendation)
}
