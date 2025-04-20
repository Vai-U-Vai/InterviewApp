package com.example.interviewapp

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    // Объявляем все view-элементы
    private lateinit var fullNameEditText: EditText
    private lateinit var ageEditText: EditText
    private lateinit var salarySeekBar: SeekBar
    private lateinit var salaryValueTextView: TextView
    private lateinit var experienceCheckBox: CheckBox
    private lateinit var teamworkCheckBox: CheckBox
    private lateinit var travelCheckBox: CheckBox
    private lateinit var submitButton: Button
    private lateinit var resultTextView: TextView

    // Правильные ответы (индексы начинаются с 0)
    private val correctAnswers = listOf(1, 0, 1, 0, 1) // Номера правильных вариантов для вопросов 1-5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Инициализируем все view-элементы
        initViews()

        // Настраиваем SeekBar для зарплаты
        setupSalarySeekBar()

        // Обработчик кнопки "Сдать тест"
        submitButton.setOnClickListener {
            if (validateInputs()) {
                val score = calculateScore()
                showResult(score)
            }
        }
    }

    private fun initViews() {
        fullNameEditText = findViewById(R.id.fullNameEditText)
        ageEditText = findViewById(R.id.ageEditText)
        salarySeekBar = findViewById(R.id.salarySeekBar)
        salaryValueTextView = findViewById(R.id.salaryValueTextView)
        experienceCheckBox = findViewById(R.id.experienceCheckBox)
        teamworkCheckBox = findViewById(R.id.teamworkCheckBox)
        travelCheckBox = findViewById(R.id.travelCheckBox)
        submitButton = findViewById(R.id.submitButton)
        resultTextView = findViewById(R.id.resultTextView)
    }

    private fun setupSalarySeekBar() {
        salarySeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val salary = 800 + progress // Диапазон 800-1600 USD
                salaryValueTextView.text = getString(R.string.salary_value_prefix) + salary
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun validateInputs(): Boolean {
        val fullName = fullNameEditText.text.toString().trim()
        val ageText = ageEditText.text.toString().trim()

        return when {
            fullName.isEmpty() -> {
                fullNameEditText.error = getString(R.string.error_name_required)
                false
            }
            ageText.isEmpty() || ageText.toInt() !in 21..40 -> {
                ageEditText.error = getString(R.string.error_age_invalid)
                false
            }
            else -> true
        }
    }

    private fun calculateScore(): Int {
        var score = 0

        // Проверяем ответы на вопросы (1-5)
        val radioGroups = listOf(
            findViewById<RadioGroup>(R.id.question1RadioGroup),
            findViewById<RadioGroup>(R.id.question2RadioGroup),
            findViewById<RadioGroup>(R.id.question3RadioGroup),
            findViewById<RadioGroup>(R.id.question4RadioGroup),
            findViewById<RadioGroup>(R.id.question5RadioGroup)
        )

        radioGroups.forEachIndexed { index, group ->
            val checkedId = group.checkedRadioButtonId
            if (checkedId != -1) {
                val radioButton = findViewById<RadioButton>(checkedId)
                val answerIndex = radioButton.tag.toString().toInt()

                if (answerIndex == correctAnswers[index]) {
                    score += 2
                }
            }
        }

        // Дополнительные баллы
        if (experienceCheckBox.isChecked) score += 2
        if (teamworkCheckBox.isChecked) score += 1
        if (travelCheckBox.isChecked) score += 1

        return score
    }

    private fun showResult(score: Int) {
        val passed = score >= 10 // Минимальный проходной балл

        val message = if (passed) {
            getString(R.string.test_passed_message) + "\n\n${getString(R.string.company_contacts)}"
        } else {
            getString(R.string.test_failed_message)
        }

        AlertDialog.Builder(this)
            .setTitle("Результат теста")
            .setMessage("Ваш результат: $score/14 баллов\n\n$message")
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }
}