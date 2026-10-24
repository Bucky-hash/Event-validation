package com.example.eventvalidation

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.eventvalidation.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val student = intent.getSerializableExtra(EXTRA_STUDENT) as? Student

        if (student == null) {
            binding.groupInfo.visibility = View.GONE
            binding.tvEmpty.visibility = View.VISIBLE
        } else {
            binding.groupInfo.visibility = View.VISIBLE
            binding.tvEmpty.visibility = View.GONE
            binding.tvName.text = getString(R.string.detail_name, student.name)
            binding.tvEmail.text = getString(R.string.detail_email, student.email)
            binding.tvPhone.text = getString(R.string.detail_phone, student.phone)
            binding.tvStudentId.text = getString(R.string.detail_student_id, student.studentId)
            binding.tvClass.text = getString(R.string.detail_class, student.className)
        }

        binding.btnBack.setOnClickListener {
            val back = Intent(this, RegistrationActivity::class.java)
            back.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(back)
            finish()
        }
    }

    companion object {
        const val EXTRA_STUDENT = "extra_student"
    }
}
