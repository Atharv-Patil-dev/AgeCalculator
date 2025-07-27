package com.example.agecalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;

public class Mainpage extends AppCompatActivity {
    private TextView tvAge, tvNextBirthday, tvNumerology;
    private Button btnPickDate, btnShare;
    private int birthYear, birthMonth, birthDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);

                tvAge = findViewById(R.id.tv_age);
                tvNextBirthday = findViewById(R.id.tv_next_birthday);
                tvNumerology = findViewById(R.id.tv_numerology);
                btnPickDate = findViewById(R.id.btn_pick_date);
                btnShare = findViewById(R.id.btn_share);

                btnPickDate.setOnClickListener(v -> showDatePicker());
                btnShare.setOnClickListener(v -> shareAgeDetails());
            }

            private void showDatePicker() {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDay) -> {
                    birthYear = selectedYear;
                    birthMonth = selectedMonth;
                    birthDay = selectedDay;
                    calculateAge();
                }, year, month, day);
                datePickerDialog.show();
            }

            private void calculateAge() {
                Calendar today = Calendar.getInstance();
                int age = today.get(Calendar.YEAR) - birthYear;

                if (today.get(Calendar.MONTH) < birthMonth || (today.get(Calendar.MONTH) == birthMonth && today.get(Calendar.DAY_OF_MONTH) < birthDay)) {
                    age--;
                }

                tvAge.setText("Your Age: " + age + " Years");

                // Calculate Next Birthday Countdown
                Calendar nextBirthday = Calendar.getInstance();
                nextBirthday.set(today.get(Calendar.YEAR), birthMonth, birthDay);
                if (nextBirthday.before(today)) {
                    nextBirthday.add(Calendar.YEAR, 1);
                }
                long daysUntilBirthday = (nextBirthday.getTimeInMillis() - today.getTimeInMillis()) / (1000 * 60 * 60 * 24);
                tvNextBirthday.setText("Next Birthday in: " + daysUntilBirthday + " days");

                // Calculate Numerology (Life Path Number)
                int lifePath = calculateLifePathNumber(birthYear, birthMonth, birthDay);
                tvNumerology.setText("Life Path Number: " + lifePath);
            }

            private int calculateLifePathNumber(int year, int month, int day) {
                int sum = year + month + day;
                while (sum > 9) {
                    sum = sum / 10 + sum % 10;
                }
                return sum;
            }

            private void shareAgeDetails() {
                String shareText = tvAge.getText().toString() + "\n" + tvNextBirthday.getText().toString() + "\n" + tvNumerology.getText().toString();
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
                startActivity(Intent.createChooser(shareIntent, "Sharevia"));
            }
}

