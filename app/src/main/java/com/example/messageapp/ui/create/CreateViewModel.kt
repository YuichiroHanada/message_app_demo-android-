package com.example.messageapp.ui.create

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.messageapp.CreateRepository
import com.example.messageapp.R
import com.example.messageapp.data.model.CreateReq
import kotlinx.coroutines.launch

class CreateViewModel(private val createRepository: CreateRepository) : ViewModel() {

    private val _createForm = MutableLiveData<CreateFormState>()
    val createFormState: LiveData<CreateFormState> = _createForm

    private val _createResult = MutableLiveData<CreateResult>()
    val createResult: LiveData<CreateResult> = _createResult




    fun create(account: String, password: String, name: String) {

        val createReq = CreateReq(account, password, name)

        viewModelScope.launch {
            try {
                val project = createRepository.create(createReq)
                if (project.isSuccessful) {

                    if (project.body()!!) {
                        _createResult.value = CreateResult(success = project.body())
                    } else {
                        _createResult.value = CreateResult(error = R.string.register_failed)
                    }

                } else {
                  _createResult.value = CreateResult(error = R.string.register_failed)
                }
            } catch (e: Exception) {
                Log.e("create:Failed", e.stackTrace.toString())
            }
        }
    }


    fun createDataChanged(username: String, password: String, name: String) {
        if (!isUserNameValid(username)) {
            _createForm.value = CreateFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _createForm.value = CreateFormState(passwordError = R.string.invalid_password)
        } else if (!isNameValid(name)) {
            _createForm.value = CreateFormState(nameError = R.string.invalid_name)

        } else {
            _createForm.value = CreateFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(account: String): Boolean {
        return if (account.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(account).matches()
        } else {
            account.isNotBlank()
        }
        return account.isNotBlank()

    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }

    private fun isNameValid(name: String): Boolean {
        return name.isNotBlank()
    }

}