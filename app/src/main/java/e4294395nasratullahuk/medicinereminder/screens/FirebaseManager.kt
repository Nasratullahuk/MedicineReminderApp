package e4294395nasratullahuk.medicinereminder.screens

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

// Data class to represent a user in the database
data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val passwordHash: String = "" // Storing plain text for simplicity, NOT RECOMMENDED FOR PRODUCTION
)

object FirebaseManager {
    private val database = FirebaseDatabase.getInstance()
    private val usersRef = database.getReference("users") // Root node for users

    private const val TAG = "FirebaseManager"

    // MutableStateFlow to hold the current logged-in user
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    // Function to register a new user
    suspend fun registerUser(name: String, email: String, passwordPlain: String): Result<User> =
        suspendCancellableCoroutine { continuation ->
            // First, check if a user with this email already exists
            usersRef.orderByChild("email").equalTo(email)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            // User with this email already exists
                            Log.d(TAG, "Registration failed: User with email $email already exists.")
                            continuation.resume(Result.failure(Exception("User with this email already exists.")))
                        } else {
                            // Email is unique, proceed with registration
                            val userId = usersRef.push().key ?: run {
                                continuation.resume(Result.failure(Exception("Failed to generate user ID.")))
                                return
                            }
                            val newUser = User(userId, name, email, passwordPlain) // Store plain password (INSECURE)

                            usersRef.child(userId).setValue(newUser)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Log.d(TAG, "User registered successfully: $userId")
                                        _currentUser.value = newUser // Set current user on successful registration
                                        continuation.resume(Result.success(newUser))
                                    } else {
                                        Log.e(TAG, "Registration failed: ${task.exception?.message}")
                                        continuation.resume(Result.failure(task.exception ?: Exception("Unknown registration error.")))
                                    }
                                }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e(TAG, "Database check for email cancelled: ${error.message}")
                        continuation.resumeWithException(error.toException())
                    }
                })
        }

    // Function to log in a user
    suspend fun loginUser(email: String, passwordPlain: String): Result<User> =
        suspendCancellableCoroutine { continuation ->
            usersRef.orderByChild("email").equalTo(email)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (!snapshot.exists()) {
                            // No user found with this email
                            Log.d(TAG, "Login failed: User not found for email $email.")
                            continuation.resume(Result.failure(Exception("Invalid email or password.")))
                            return
                        }

                        // Iterate through results (should be only one if email is unique)
                        for (childSnapshot in snapshot.children) {
                            val user = childSnapshot.getValue(User::class.java)
                            if (user != null && user.passwordHash == passwordPlain) { // Compare plain password (INSECURE)
                                Log.d(TAG, "Login successful for user: ${user.email}")
                                _currentUser.value = user // Set current user on successful login
                                continuation.resume(Result.success(user))
                                return
                            }
                        }
                        // If loop finishes without returning, password was incorrect
                        Log.d(TAG, "Login failed: Incorrect password for email $email.")
                        continuation.resume(Result.failure(Exception("Invalid email or password.")))
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e(TAG, "Database query for login cancelled: ${error.message}")
                        continuation.resumeWithException(error.toException())
                    }
                })
        }

    // Function to log out the current user
    fun logoutUser() {
        _currentUser.value = null
        Log.d(TAG, "User logged out.")
    }
}

