package com.example.eventvalidation

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.example.eventvalidation.databinding.ActivityRegistrationBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText

class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding
    private var lastStudent: Student? = null

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lastStudent = savedInstanceState?.getSerializable(STATE_STUDENT) as? Student

        setupRealtimeValidation()

        binding.btnRegister.setOnClickListener { handleRegister() }
        binding.btnGoDetail.setOnClickListener { openDetail(lastStudent) }

        // Bấm Done trên bàn phím ở ô cuối cùng thì đăng ký luôn
        binding.etClass.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                handleRegister()
                true
            } else {
                false
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(STATE_STUDENT, lastStudent)
    }

    // Người dùng gõ lại vào ô nào thì xóa lỗi của ô đó
    private fun setupRealtimeValidation() {
        binding.etName.doAfterTextChanged { binding.tilName.error = null }
        binding.etEmail.doAfterTextChanged { binding.tilEmail.error = null }
        binding.etPhone.doAfterTextChanged { binding.tilPhone.error = null }
        binding.etStudentId.doAfterTextChanged { binding.tilStudentId.error = null }
        binding.etClass.doAfterTextChanged { binding.tilClass.error = null }
    }

    private fun textOf(editText: TextInputEditText): String =
        editText.text?.toString().orEmpty().trim()

    private fun handleRegister() {
        val name = textOf(binding.etName).replace(Regex("\\s+"), " ")
        val email = textOf(binding.etEmail)
        val phone = textOf(binding.etPhone)
        val studentId = textOf(binding.etStudentId)
        val className = textOf(binding.etClass)

        // Báo lỗi đồng loạt trên tất cả các ô sai
        val checks = listOf(
            Triple(binding.tilName, binding.etName, FormValidator.validateName(name)),
            Triple(binding.tilEmail, binding.etEmail, FormValidator.validateEmail(email)),
            Triple(binding.tilPhone, binding.etPhone, FormValidator.validatePhone(phone)),
            Triple(binding.tilStudentId, binding.etStudentId, FormValidator.validateStudentId(studentId)),
            Triple(binding.tilClass, binding.etClass, FormValidator.validateClass(className))
        )

        var firstInvalid: View? = null
        for ((layout, editText, result) in checks) {
            layout.error = result.errorMessage
            if (!result.isValid && firstInvalid == null) {
                firstInvalid = editText
            }
        }

        // Có lỗi thì đưa focus vào ô sai đầu tiên
        if (firstInvalid != null) {
            firstInvalid.requestFocus()
            return
        }

        hideKeyboard()

        val student = Student(name, email, phone, studentId, className)
        lastStudent = student

        Snackbar.make(
            binding.root,
            getString(R.string.snackbar_success, name),
            Snackbar.LENGTH_LONG
        )
            .setDuration(10000)
            .setAction(R.string.snackbar_action_detail) { openDetail(student) }
            .show()
    }

    private fun openDetail(student: Student?) {
        val intent = Intent(this, DetailActivity::class.java)
        if (student != null) {
            intent.putExtra(DetailActivity.EXTRA_STUDENT, student)
        }
        startActivity(intent)
    }

    private fun hideKeyboard() {
        val focused = currentFocus ?: return
        val imm = getSystemService(InputMethodManager::class.java)
        imm?.hideSoftInputFromWindow(focused.windowToken, 0)
        focused.clearFocus()
    }

    companion object {
        private const val STATE_STUDENT = "state_student"
    }
}
