package com.example.magiccalculator;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import com.example.magiccalculator.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.button0.setOnClickListener(v -> addCharacterToTextbox('0'));
        binding.button1.setOnClickListener(v -> addCharacterToTextbox('1'));
        binding.button2.setOnClickListener(v -> addCharacterToTextbox('2'));
        binding.button3.setOnClickListener(v -> addCharacterToTextbox('3'));
        binding.button4.setOnClickListener(v -> addCharacterToTextbox('4'));
        binding.button5.setOnClickListener(v -> addCharacterToTextbox('5'));
        binding.button6.setOnClickListener(v -> addCharacterToTextbox('6'));
        binding.button7.setOnClickListener(v -> addCharacterToTextbox('7'));
        binding.button8.setOnClickListener(v -> addCharacterToTextbox('8'));
        binding.button9.setOnClickListener(v -> addCharacterToTextbox('9'));

        binding.buttonPlus.setOnClickListener(v -> addCharacterToTextbox('+'));
        binding.buttonMinus.setOnClickListener(v -> addCharacterToTextbox('-'));
        binding.buttonMultiply.setOnClickListener(v -> addCharacterToTextbox('*'));
        binding.buttonDivide.setOnClickListener(v -> addCharacterToTextbox('/'));
        binding.buttonPercent.setOnClickListener(v -> addCharacterToTextbox('%'));
        binding.buttonComma.setOnClickListener(v -> addCharacterToTextbox(','));

        binding.buttonAllClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.resultText.setText("0");
            }
        });

        binding.buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = binding.resultText.getText().toString();
                binding.resultText.setText(text.substring(0, text.length() - 1));
            }
        });
    }

    void addCharacterToTextbox(char character) {
        String currentText = binding.resultText.getText().toString();
        String newText = getString(R.string.result_text_placeholder, currentText + character);
        binding.resultText.setText(newText);
    }
}