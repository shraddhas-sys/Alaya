package com.example.alaya.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alaya.model.AlayaNotification
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Calendar
import java.util.concurrent.TimeUnit

class DashboardViewModel(private val auth: FirebaseAuth = FirebaseAuth.getInstance()) : ViewModel() {

    private val dbRef = FirebaseDatabase.getInstance().reference
    private val usersRef = dbRef.child("users")

    private val _userName = mutableStateOf("")
    val userName: State<String> = _userName
    private val _planned = mutableIntStateOf(0)
    val planned: State<Int> = _planned

    private val _streak = mutableIntStateOf(0)
    val streak: State<Int> = _streak

    private val _totalGoal = mutableIntStateOf(20)
    val totalGoal: State<Int> = _totalGoal

    private val _notifications = mutableStateListOf<AlayaNotification>()
    val notifications: List<AlayaNotification> get() = _notifications

    val unreadCount: Int get() = _notifications.count { !it.isRead }

    private var userListener: ValueEventListener? = null
    private var notificationListener: ValueEventListener? = null

    init {
        fetchUserData()
    }

    private fun getCurrentUserId(): String? = auth.currentUser?.uid

    fun fetchUserData() {
        val uid = getCurrentUserId()
        if (uid != null) {
            observeUserProgress(uid)
            observeNotifications(uid)
        }
    }

    private fun observeUserProgress(uid: String) {
        userListener?.let { usersRef.child(uid).removeEventListener(it) }
        userListener = usersRef.child(uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    _userName.value = snapshot.child("name").getValue(String::class.java) ?: ""

                    val cVal = snapshot.child("completedCount").value
                    _planned.intValue = when (cVal) {
                        is Long -> cVal.toInt()
                        is Int -> cVal
                        else -> 0
                    }

                    val sVal = snapshot.child("streak").value
                    _streak.intValue = when (sVal) {
                        is Long -> sVal.toInt()
                        is Int -> sVal
                        else -> 0
                    }

                    val gVal = snapshot.child("totalGoal").value
                    _totalGoal.intValue = (gVal as? Long)?.toInt() ?: (gVal as? Int) ?: 20
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun onPlanComplete(planTitle: String) {
        val uid = getCurrentUserId() ?: return
        val userPath = usersRef.child(uid)

        val updates = hashMapOf<String, Any>(
            "completedCount" to ServerValue.increment(1),
            "streak" to ServerValue.increment(1)
        )

        userPath.updateChildren(updates).addOnSuccessListener {
            sendNotification("Task Completed! ", "You've finished: '$planTitle'")
        }
    }

    fun deleteLastPlan() {
        val uid = getCurrentUserId() ?: return
        val userPath = usersRef.child(uid)

        userPath.runTransaction(object : Transaction.Handler {
            override fun doTransaction(currentData: MutableData): Transaction.Result {
                val c = (currentData.child("completedCount").value as? Long) ?: 0L
                val s = (currentData.child("streak").value as? Long) ?: 0L

                currentData.child("completedCount").value = if (c > 0) c - 1 else 0
                currentData.child("streak").value = if (s > 0) s - 1 else 0

                return Transaction.success(currentData)
            }

            override fun onComplete(e: DatabaseError?, committed: Boolean, snapshot: DataSnapshot?) {
                if (committed) sendNotification("Action Undone 🗑️", "Last progress removed.")
            }
        })
    }

    private fun observeNotifications(uid: String) {
        notificationListener?.let { dbRef.child("notifications").removeEventListener(it) }
        notificationListener = dbRef.child("notifications")
            .orderByChild("userId")
            .equalTo(uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val tempList = mutableListOf<AlayaNotification>()
                    for (data in snapshot.children) {
                        val timestamp = data.child("timestamp").getValue(Long::class.java) ?: 0L
                        tempList.add(AlayaNotification(
                            id = data.key ?: "",
                            title = data.child("title").getValue(String::class.java) ?: "",
                            message = data.child("message").getValue(String::class.java) ?: "",
                            userId = data.child("userId").getValue(String::class.java) ?: "",
                            timestamp = timestamp,
                            isRead = data.child("isRead").getValue(Boolean::class.java) ?: false,
                            type = getNotificationCategory(timestamp),
                            timeAgo = getTimeAgo(timestamp)
                        ))
                    }
                    _notifications.clear()
                    _notifications.addAll(tempList.sortedByDescending { it.timestamp })
                }
                override fun onCancelled(error: DatabaseError) {}
            })
    }

    fun markAsRead(notificationId: String) {
        if (notificationId.isEmpty()) return
        dbRef.child("notifications").child(notificationId).child("isRead").setValue(true)
    }

    fun sendNotification(title: String, message: String) {
        val uid = getCurrentUserId() ?: return
        viewModelScope.launch {
            try {
                val newNotifRef = dbRef.child("notifications").push()
                newNotifRef.setValue(mapOf(
                    "id" to (newNotifRef.key ?: ""),
                    "title" to title,
                    "message" to message,
                    "userId" to uid,
                    "timestamp" to System.currentTimeMillis(),
                    "isRead" to false
                )).await()
            } catch (e: Exception) { }
        }
    }

    private fun getTimeAgo(t: Long): String {
        val d = System.currentTimeMillis() - t
        val m = TimeUnit.MILLISECONDS.toMinutes(d)
        val h = TimeUnit.MILLISECONDS.toHours(d)
        return when {
            d < 60000 -> "Just now"
            m < 60 -> "${m}m ago"
            h < 24 -> "${h}h ago"
            else -> "Older"
        }
    }

    private fun getNotificationCategory(t: Long): String {
        val now = Calendar.getInstance()
        val nd = Calendar.getInstance().apply { timeInMillis = t }
        return if (now.get(Calendar.YEAR) == nd.get(Calendar.YEAR) &&
            now.get(Calendar.DAY_OF_YEAR) == nd.get(Calendar.DAY_OF_YEAR)) "Today" else "This Week"
    }

    fun logout(onLogout: () -> Unit) {
        val uid = getCurrentUserId()
        uid?.let {
            userListener?.let { l -> usersRef.child(it).removeEventListener(l) }
            notificationListener?.let { l -> dbRef.child("notifications").removeEventListener(l) }
        }
        auth.signOut()
        _userName.value = ""
        _notifications.clear()
        onLogout()
    }
}