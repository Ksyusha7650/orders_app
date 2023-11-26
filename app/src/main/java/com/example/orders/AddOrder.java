package com.example.orders;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class AddOrder extends AppCompatActivity {

    public static Order order;
    EditText date, editTextNumber;
    CheckBox checkBoxIsSigned;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order);
        date = findViewById(R.id.editTextDate);
        editTextNumber = findViewById(R.id.editTextNumber);
        checkBoxIsSigned = findViewById(R.id.checkBoxIsSigned);
        date.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    AddOrder.this,
                    (view, year1, monthOfYear, dayOfMonth) -> date.setText(dayOfMonth + "." + (monthOfYear + 1) + "." + year1),
                    year, month, day);
            datePickerDialog.show();
        });
        if (order != null){
            date.setText(order.Date);
            editTextNumber.setText(order.Number);
            checkBoxIsSigned.setChecked(order.IsSigned);
            Button buttonEdit = findViewById(R.id.buttonAction);
            buttonEdit.setText(R.string.change);
            buttonEdit.setOnClickListener(v -> {
                MainActivity.dataBaseWorker.updateOrder(
                        editTextNumber.getText().toString(),
                        date.getText().toString(),
                        checkBoxIsSigned.isChecked(),
                        String.valueOf(order.ID)
                );
                order = null;
                MainActivity.orders = MainActivity.dataBaseWorker.loadData();
                Intent myIntent = new Intent(buttonEdit.getContext(), MainActivity.class);
                startActivity(myIntent);
            });

        }
    }

    public void addOrder(View view) {
        DataBaseWorker dataBaseWorker = MainActivity.dataBaseWorker;
        try {
            dataBaseWorker.insertOrder(
                    editTextNumber.getText().toString(),
                    date.getText().toString(),
                    checkBoxIsSigned.isChecked()
            );
            MainActivity.orders = dataBaseWorker.loadData();
            Intent myIntent = new Intent(view.getContext(), MainActivity.class);
            startActivity(myIntent);
        }
        catch (Exception e){
            e.getMessage();
        }
    }
}