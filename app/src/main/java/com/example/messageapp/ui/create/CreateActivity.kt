package com.example.messageapp.ui.create

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.messageapp.R
import com.example.messageapp.ui.login.LoginActivity
import com.example.messageapp.ui.login.afterTextChanged

class CreateActivity : AppCompatActivity() {

    private lateinit var createViewModel : CreateViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)


        val username = findViewById<EditText>(R.id.account)
        val password = findViewById<EditText>(R.id.password)
        val name = findViewById<EditText>(R.id.userName)
        val register = findViewById<Button>(R.id.login)
        val loading = findViewById<ProgressBar>(R.id.loading)


        createViewModel = ViewModelProvider(this, CreateViewModelFactory())
            .get(CreateViewModel::class.java)


        createViewModel.createFormState.observe(this@CreateActivity, Observer {
            val createState = it ?: return@Observer

            // disable login button unless both username / password is valid
            register.isEnabled = createState.isDataValid

            if (createState.usernameError != null) {
                username.error = getString(createState.usernameError)
            }
            if (createState.passwordError != null) {
                password.error = getString(createState.passwordError)
            }
            if (createState.nameError != null) {
                name.error = getString(createState.nameError)
            }
        })

        createViewModel.createResult.observe(this@CreateActivity, Observer {
            val createResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (createResult.error != null) {
                showLoginFailed(createResult.error)
            }
            if (createResult.success != null) {

                createAfter()
            }
            setResult(Activity.RESULT_OK)

            //Complete and destroy login activity once successful
            finish()
        })

        username.afterTextChanged {
            createViewModel.createDataChanged(
                username.text.toString(),
                password.text.toString(),
                name.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                createViewModel.createDataChanged(
                    username.text.toString(),
                    password.text.toString(),
                    name.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        createViewModel.create(username.text.toString(), password.text.toString(), name.text.toString())
                }
                false
            }

            register.setOnClickListener {
                loading.visibility = View.VISIBLE
                createViewModel.create(username.text.toString(), password.text.toString(), name.text.toString())
            }
        }
    }

    private fun createAfter() {
        val welcome = getString(R.string.register_finished)

        val intent = Intent(this, LoginActivity::class.java)

        startActivity(intent)
        Toast.makeText(
            applicationContext,
            welcome,
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })

}