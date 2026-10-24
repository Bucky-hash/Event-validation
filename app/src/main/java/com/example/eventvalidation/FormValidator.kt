package com.example.eventvalidation

import android.util.Patterns

data class ValidationResult(
    val isValid: Boolean,
    val errorMessage: String? = null
)

object FormValidator {

    // Ít nhất 2 từ, mỗi từ chỉ gồm chữ cái (có dấu tiếng Việt), cách nhau 1 dấu cách
    private val NAME_REGEX = Regex("^[\\p{L}\\p{M}]+( [\\p{L}\\p{M}]+)+$")

    // Di động Việt Nam: 10 số, đầu số 03 05 07 08 09
    private val PHONE_REGEX = Regex("^(03|05|07|08|09)\\d{8}$")

    // MSV: chỉ gồm chữ số, 8 đến 12 chữ số
    private val STUDENT_ID_REGEX = Regex("^\\d{8,12}$")

    // Lớp: chữ, số, dấu chấm, gạch dưới, gạch ngang, 3 đến 20 ký tự, không có dấu cách
    private val CLASS_REGEX = Regex("^[A-Za-z0-9._-]{3,20}$")

    fun validateName(raw: String): ValidationResult {
        val name = raw.trim().replace(Regex("\\s+"), " ")
        if (name.isBlank()) {
            return ValidationResult(false, "Họ tên không được để trống")
        }
        if (name.length > 50) {
            return ValidationResult(false, "Họ tên tối đa 50 ký tự")
        }
        if (!name.matches(NAME_REGEX)) {
            return ValidationResult(false, "Họ tên cần tối thiểu 2 từ và chỉ gồm chữ cái")
        }
        return ValidationResult(true)
    }

    fun validateEmail(raw: String): ValidationResult {
        val email = raw.trim()
        if (email.isBlank()) {
            return ValidationResult(false, "Email không được để trống")
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return ValidationResult(false, "Email không đúng định dạng")
        }
        return ValidationResult(true)
    }

    fun validatePhone(raw: String): ValidationResult {
        val phone = raw.trim()
        if (phone.isBlank()) {
            return ValidationResult(false, "Số điện thoại không được để trống")
        }
        if (!phone.matches(PHONE_REGEX)) {
            return ValidationResult(false, "SĐT phải có 10 số, đầu số 03, 05, 07, 08 hoặc 09")
        }
        return ValidationResult(true)
    }

    fun validateStudentId(raw: String): ValidationResult {
        val id = raw.trim()
        if (id.isBlank()) {
            return ValidationResult(false, "MSV không được để trống")
        }
        if (!id.matches(STUDENT_ID_REGEX)) {
            return ValidationResult(false, "MSV chỉ gồm chữ số, từ 8 đến 12 chữ số")
        }
        return ValidationResult(true)
    }

    fun validateClass(raw: String): ValidationResult {
        val cls = raw.trim()
        if (cls.isBlank()) {
            return ValidationResult(false, "Lớp không được để trống")
        }
        if (!cls.matches(CLASS_REGEX)) {
            return ValidationResult(false, "Lớp gồm 3 đến 20 ký tự chữ, số, . _ - và không có dấu cách")
        }
        return ValidationResult(true)
    }
}
