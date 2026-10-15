package com.example.creditcardexpensetracker;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.creditcardexpensetracker.ui.SetupViewModel;
import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    private SetupViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = new ViewModelProvider(this).get(SetupViewModel.class);

        TextInputEditText pinEditText = findViewById(R.id.pinEditText);
        Button setupButton = findViewById(R.id.setupButton);
        TextView statusTextView = findViewById(R.id.statusTextView);

        setupButton.setOnClickListener(v -> {
            String pin = pinEditText.getText() != null ? pinEditText.getText().toString() : "";
            if (pin.length() < 4) {
                Toast.makeText(MainActivity.this, "Please enter a 4-digit PIN", Toast.LENGTH_SHORT).show();
                return;
            }
            
            // In a real flow, check if it's already set up, but this is the first launch screen for Lab 1
            viewModel.setupAdmin(pin);
        });

        viewModel.getIsSetupComplete().observe(this, isComplete -> {
            if (isComplete) {
                statusTextView.setText("Setup complete! Admin user created.");
                pinEditText.setEnabled(false);
                setupButton.setEnabled(false);
                // In Lab 2, we would navigate to the next screen here
            }
        });
    }
}
