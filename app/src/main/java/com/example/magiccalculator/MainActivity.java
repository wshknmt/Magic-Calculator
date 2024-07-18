package com.example.magiccalculator;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.magiccalculator.databinding.ActivityMainBinding;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private static final int PICK_CONTACT = 1;
    private String selectedNumber;
    private char lastCharacter = 'E'; // N - number, O - operator, E - empty, C - comma
    private Boolean pickedNumber = false;
    private double firstNumber = 0, secondNumber = 0;
    private Boolean existSecondNumber = false;
    private char operator;
    private Boolean pickedOperator = false;
    private Boolean commaExistFirst = false;
    private Boolean commaExistSecond = false;
    private int exponentFirst = 1;
    private int exponentSecond = 1;
    double precision = Math.pow(10, 6);
//    DecimalFormat df = new DecimalFormat("#.######");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.button0.setOnClickListener(v -> numberClicked('0'));
        binding.button1.setOnClickListener(v -> numberClicked('1'));
        binding.button2.setOnClickListener(v -> numberClicked('2'));
        binding.button3.setOnClickListener(v -> numberClicked('3'));
        binding.button4.setOnClickListener(v -> numberClicked('4'));
        binding.button5.setOnClickListener(v -> numberClicked('5'));
        binding.button6.setOnClickListener(v -> numberClicked('6'));
        binding.button7.setOnClickListener(v -> numberClicked('7'));
        binding.button8.setOnClickListener(v -> numberClicked('8'));
        binding.button9.setOnClickListener(v -> numberClicked('9'));

        binding.buttonPlus.setOnClickListener(v -> operatorClicked('+'));
        binding.buttonMinus.setOnClickListener(v -> operatorClicked('-'));
        binding.buttonMultiply.setOnClickListener(v -> operatorClicked('*'));
        binding.buttonDivide.setOnClickListener(v -> operatorClicked('/'));
        binding.buttonPercent.setOnClickListener(v -> operatorClicked('%'));

        binding.buttonComma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lastCharacter == 'N') {
                    if (!existSecondNumber && !commaExistFirst) {
                        commaExistFirst = true;
                    } else if (existSecondNumber && !commaExistSecond) {
                        commaExistSecond = true;
                    } else return;
                    String currentText = binding.resultText.getText().toString();
                    String newText = getString(R.string.result_text_placeholder, currentText + '.');
                    binding.resultText.setText(newText);
                    lastCharacter = 'C';
//                    commaExist = true;
                }
            }
        });

        binding.buttonAllClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.resultText.setText("0");
                lastCharacter = 'E';
                pickedOperator = false;
                firstNumber = 0;
                secondNumber = 0;
                existSecondNumber = false;
                operator = ' ';
                pickedNumber = false;
                commaExistFirst = false;
                commaExistSecond = false;
                exponentFirst = 1;
                exponentSecond = 1;
            }
        });

        binding.buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lastCharacter != 'E' && binding.resultText.length() > 0) {
                    String text = binding.resultText.getText().toString();
                    binding.resultText.setText(text.substring(0, text.length() - 1));
//                    if ()
                }
                if (binding.resultText.length() == 0) {
                    lastCharacter = 'E';
                    binding.resultText.setText("0");
                }
            }
        });

        binding.buttonEquals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pickedNumber) {
//                    String text = binding.resultText.getText().toString();
                    binding.resultText.setText(selectedNumber);
                } else {
                    if (existSecondNumber) {
                        double countedResult = makeCalculations();
                        countedResult = Math.floor(countedResult * precision) / precision;
                        String strResult = String.valueOf(countedResult);
                        if (Math.floor(countedResult) == countedResult) {
                            strResult = strResult.split("\\.")[0];
                            commaExistFirst = false;
                            exponentFirst = 1;
                        } else {
                            String[] fractionalParts = strResult.split("\\.");
                            if (fractionalParts.length < 2) {
                                commaExistFirst = false;
                                exponentFirst = 1;
                            } else {
                                exponentFirst = fractionalParts[1].length();
                            }
                        }
                        binding.resultText.setText(strResult);
                        firstNumber = countedResult;
                        secondNumber = 0;
                        existSecondNumber = false;
                        commaExistSecond = false;
                        exponentSecond = 1;
                        pickedOperator = false;
                        lastCharacter = 'N';
                    }
                }
            }
        });

        binding.buttonEquals.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, 0);
                }
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, PICK_CONTACT);
                return false;
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_CONTACT && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            pickedNumber = true;

            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                String id = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                Uri uriPhone = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                String selection = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " =?";
                Cursor cursor1 = getContentResolver().query(uriPhone, null, selection, new String[]{id}, null);
                if (cursor1.moveToFirst()) {
                    selectedNumber = cursor1.getString(cursor1.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    if (selectedNumber.startsWith("+48 ")) {
                        selectedNumber = selectedNumber.substring(4); // Remove "+48" prefix
                    }
                }
                cursor1.close();
            }
            cursor.close();
        }
    }

    void numberClicked(char character) {
        if (lastCharacter == 'E') {
            binding.resultText.setText("");
        }
        String currentText = binding.resultText.getText().toString();
        String newText = getString(R.string.result_text_placeholder, currentText + character);
        binding.resultText.setText(newText);
        lastCharacter = 'N';
        if (!pickedOperator) {
            if (!commaExistFirst)
                firstNumber = firstNumber * 10 + character - '0';
            else {
                double quotient = Math.pow(10, exponentFirst);
                firstNumber = firstNumber + (character - '0') / quotient;
                exponentFirst++;
            }
        } else {
            existSecondNumber = true;
            if (!commaExistSecond)
                secondNumber = secondNumber * 10 + character - '0';
            else {
                double quotient = Math.pow(10, exponentSecond);
                secondNumber = secondNumber + (character - '0') / quotient;
                exponentSecond++;
            }
        }
    }

    void operatorClicked(char character) {
        if (lastCharacter == 'N') {
            if (!existSecondNumber) {
                String currentText = binding.resultText.getText().toString();
                String newText = getString(R.string.result_text_placeholder, currentText + character);
                binding.resultText.setText(newText);
            } else {
                double countedResult = makeCalculations();
                countedResult = Math.floor(countedResult * precision) / precision;
                String strResult = String.valueOf(countedResult);
                if (Math.floor(countedResult) == countedResult) {
                    strResult = strResult.split("\\.")[0];
                    commaExistFirst = false;
                    exponentFirst = 1;
                } else {
                    String[] fractionalParts = strResult.split("\\.");
                    if (fractionalParts.length < 2) {
                        commaExistFirst = false;
                        exponentFirst = 1;
                    } else {
                        exponentFirst = fractionalParts[1].length();
                    }
                }

                strResult += character;
                binding.resultText.setText(strResult);
                firstNumber = countedResult;
                secondNumber = 0;
                existSecondNumber = false;
                commaExistSecond = false;
                exponentSecond = 1;

            }

        } else if (lastCharacter == 'O') {
            String currentText = binding.resultText.getText().toString();
            String newText = currentText.substring(0, currentText.length() - 1) + character;
            binding.resultText.setText(newText);
        } else return;
        lastCharacter = 'O';
        pickedOperator = true;
        operator = character;
    }

    double makeCalculations() {
        double result = 0.0;
        switch(operator) {
            case '+':
                result = firstNumber + secondNumber;
                break;
            case '-':
                result = firstNumber - secondNumber;
                break;
            case '*':
                result = firstNumber * secondNumber;
                break;
            case '/':
                result = firstNumber / secondNumber;
                break;
            case '%':
                result = firstNumber * 100 / secondNumber;
                break;
        };
        return result;
    }
}