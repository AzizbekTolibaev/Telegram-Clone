package com.example.chatapp.presentation

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.flow.flow

class PhoneAuthViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private var verificationId = ""

    fun getVerificationId(): String {
        return this.verificationId
    }

    private val _verificationCodeSent = MutableLiveData<Boolean>()
    val verificationCodeSent: LiveData<Boolean> get() = _verificationCodeSent

    private val _signInSuccess = MutableLiveData<Boolean>()
    val signInSuccess: LiveData<Boolean> get() = _signInSuccess

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun sendVerificationCode(activity: Activity, phoneNumber: String) {

        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                _error.value = p0.message
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                this@PhoneAuthViewModel.verificationId = verificationId
                _verificationCodeSent.value = true
            }
        }

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber,
            60,
            java.util.concurrent.TimeUnit.SECONDS,
            activity,
            callbacks
        )
    }

    fun signInWithVerificationCode(verificationId: String, code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _signInSuccess.value = true
                } else {
                    _error.value = task.exception?.message
                }
            }
    }
}
