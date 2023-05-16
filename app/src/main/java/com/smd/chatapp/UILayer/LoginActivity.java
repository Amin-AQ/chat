package com.smd.chatapp.UILayer;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.smd.chatapp.R;
import com.smd.chatapp.SessionManager;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    private static final int OTP_LENGTH = 6;
    private static final long OTP_EXPIRY_TIME = 30000; // 30 seconds in milliseconds
    SessionManager sessionManager;
    private EditText editTextPhoneNumber;
    private Button loginBtn;
    //private TextView textViewCountdown;

/*    private String sentOTP;
    private CountDownTimer otpCountDownTimer;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager=new SessionManager(getApplicationContext());
        setContentView(R.layout.activity_login);
        loginBtn=findViewById(R.id.loginBtn);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pno = editTextPhoneNumber.getText().toString();
                if (pno.startsWith("0")) {
                    pno = "+92" + pno.substring(1);
                    editTextPhoneNumber.setText(pno);
                }
                // Check if phone number starts with +92 and has 13 digits
                if (!pno.matches("^\\+92\\d{10}$")) {
                    Toast.makeText(LoginActivity.this, "Invalid phone number", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                    sessionManager.createLoginSession(pno);
                    Intent intent=new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("user_num",pno);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

   /* private void generateOTP() {
        Random random = new Random();
        StringBuilder otpBuilder = new StringBuilder();

        for (int i = 0; i < OTP_LENGTH; i++) {
            otpBuilder.append(random.nextInt(10));
        }

        sentOTP = otpBuilder.toString();
        // Display the sent OTP or perform any other necessary actions
    }

    private void startOTPCountdown() {
        if (otpCountDownTimer != null) {
            otpCountDownTimer.cancel();
        }

        otpCountDownTimer = new CountDownTimer(OTP_EXPIRY_TIME, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long secondsRemaining = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished);
                textViewCountdown.setText(String.valueOf(secondsRemaining));
            }

            @Override
            public void onFinish() {
                // Enable the "Get OTP" button again after the countdown finishes
                buttonGetOTP.setEnabled(true);
                textViewCountdown.setText("");
            }
        };

        otpCountDownTimer.start();
        buttonGetOTP.setEnabled(false); // Disable the "Get OTP" button during the countdown
    }

    private void verifyOTP() {
        String enteredOTP = editTextOTP.getText().toString().trim();
        if (enteredOTP.equals(sentOTP)) {
            // Correct OTP entered, proceed with login
            Toast.makeText(this, "OTP is correct. Login successful!", Toast.LENGTH_SHORT).show();
            // Implement your login logic here
        } else {
            // Incorrect OTP entered
            Toast.makeText(this, "Incorrect OTP. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }
*/
    @Override
    protected void onDestroy() {
        super.onDestroy();
/*        if (otpCountDownTimer != null) {
            otpCountDownTimer.cancel();
        }*/
    }
}


