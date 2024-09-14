package com.example.snapsphere.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.snapsphere.data.Event
import com.example.snapsphere.data.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

private const val USERS = "users"

@HiltViewModel
class IgViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val storage: FirebaseStorage
) : ViewModel() {

    // flag to check if user is signed in or not
    private val _signedIn = mutableStateOf<Boolean?>(null)
    val signedIn = _signedIn

    // flag for checking if sign up is in progress or not
    private val _inProgress = mutableStateOf(false)
    val inProgress = _inProgress

    private val _userData = mutableStateOf<UserData?>(null)
    val userData = _userData

    private val _popupNotification = mutableStateOf<Event<String>?>(null)
    val popupNotification = _popupNotification

    init {
        //auth.signOut()
        val currentUser = auth.currentUser
        _signedIn.value = currentUser != null
        currentUser?.uid?.let {
            getUserData(it)
        }
    }

    // function to signup the user
    fun onSignUp(username: String, email: String, password: String) {
        _inProgress.value = true

        // checking if the firestore already have the username entered by the user or not
        db.collection(USERS).whereEqualTo("username", username).get()
            .addOnSuccessListener { documents ->
                /*
                    when the operation to search for the username is successful, we are checking if the documents returned by the function
                    contains something or not. If the documents is empty then it means that there is no user present with the username that the new
                    user is trying to signup with and if the documents contains something then the username already exists in the firestore database
                 */
                if (!documents.isEmpty) {
                    handleException(customMessage = "This username is already taken. Please try a different one.")
                    _inProgress.value = false
                } else {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                _signedIn.value = true
                                // Create Profile
                                createOrUpdateProfile(username = username)
                            } else {
                                handleException(task.exception, "Signup failed")
                            }
                            _inProgress.value = false
                        }
                }
            }
            .addOnFailureListener {
                handleException(it, "Signup failed")
            }
    }

    // function to login the user
    fun onLogin(email: String, password: String) {
        _inProgress.value = true

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {    task ->
                if(task.isSuccessful) {
                    _signedIn.value = true
                    auth.currentUser?.uid?.let {
                        getUserData(it)
                    }
                    //handleException(customMessage = "Login Successful")
                    _inProgress.value = false
                } else {
                    handleException(task.exception, "Login failed")
                    _inProgress.value = false
                }
            }
            .addOnFailureListener {
                handleException(it, "Login failed")
                _inProgress.value = false
            }
    }

    // function to create or update user profile in firebase database (firestore)
    private fun createOrUpdateProfile(
        name: String? = null,
        username: String? = null,
        bio: String? = null,
        imageUrl: String? = null
    ) {
        // getting userId for current user using firebase authentication
        val userId = auth.currentUser?.uid

        // creating user data
        val userData = UserData(
            userId = userId,
            name = name ?: _userData.value?.name,
            username = username ?: _userData.value?.username,
            imageUrl = imageUrl ?: _userData.value?.imageUrl,
            bio = bio ?: _userData.value?.bio,
            following = _userData.value?.following
        )

        userId?.let { uid ->
            _inProgress.value = true
            db.collection(USERS).document(uid).get()
                .addOnSuccessListener { user ->
                    if (user.exists()) {      // checking if the user already exists
                        user.reference.update(userData.toMap())
                            .addOnSuccessListener {
                                this._userData.value = userData
                                _inProgress.value = false
                            }
                            .addOnFailureListener {
                                handleException(it, customMessage = "Cannot update user")
                            }
                    } else {
                        db.collection(USERS).document(uid).set(userData)
                        getUserData(uid)
                        _inProgress.value = false
                    }
                }
                .addOnFailureListener {
                    handleException(it, "Cannot create user")
                    _inProgress.value = false
                }
        }
    }

    // function to get user data if the creation or updation of user is successful
    private fun getUserData(userId: String) {
        _inProgress.value = true
        db.collection(USERS).document(userId).get()
            .addOnSuccessListener {
                val userData = it.toObject<UserData>()
                _userData.value = userData
                _inProgress.value = false
            }
            .addOnFailureListener {
                handleException(it, "Cannot retrieve user data")
            }
    }

    // function to show toast when an exception occurs
    fun handleException(e: Exception? = null, customMessage: String = "") {
        e?.printStackTrace()
        val errorMsg = e?.localizedMessage ?: ""
        val message =
            if (customMessage.isEmpty()) errorMsg else if (errorMsg.isEmpty()) customMessage else "$customMessage: $errorMsg"
        _popupNotification.value = Event(message)
    }
}