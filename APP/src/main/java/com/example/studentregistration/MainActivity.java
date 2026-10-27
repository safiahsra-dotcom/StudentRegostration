package com.example.studentregistration;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText etName, etStudentId;
    CheckBox cbEnrolled;
    Button btnAdd, btnEdit, btnDelete, btnView;

    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // CONNECT XML TO JAVA
        etName = findViewById(R.id.etName);
        etStudentId = findViewById(R.id.etStudentId);
        cbEnrolled = findViewById(R.id.cbEnrolled);

        btnAdd = findViewById(R.id.btnAdd);
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);
        btnView = findViewById(R.id.btnView);

        db = new DatabaseHelper(this);

        // ADD BUTTON
        btnAdd.setOnClickListener(v -> {

            String name = etName.getText().toString().trim();
            String id = etStudentId.getText().toString().trim();
            boolean enrolled = cbEnrolled.isChecked();

            if (name.isEmpty() || id.isEmpty()) {
                showToast("Please fill all fields");
                return;
            }

            boolean result = db.addStudent(id, name, enrolled);

            if (result) {
                showToast("Student Added Successfully");
                clearFields();
            } else {
                showToast("Failed! Student ID may already exist");
            }
        });

        // EDIT BUTTON
        btnEdit.setOnClickListener(v -> {

            String name = etName.getText().toString().trim();
            String id = etStudentId.getText().toString().trim();
            boolean enrolled = cbEnrolled.isChecked();

            if (name.isEmpty() || id.isEmpty()) {
                showToast("Please fill all fields");
                return;
            }

            boolean result = db.updateStudent(id, name, enrolled);

            if (result) {
                showToast("Student Updated Successfully");
                clearFields();
            } else {
                showToast("Student ID Not Found");
            }
        });

        // DELETE BUTTON
        btnDelete.setOnClickListener(v -> {

            String id = etStudentId.getText().toString().trim();

            if (id.isEmpty()) {
                showToast("Please enter Student ID");
                return;
            }

            new AlertDialog.Builder(this)
                    .setTitle("Delete Student")
                    .setMessage("Are you sure you want to delete this student?")
                    .setPositiveButton("DELETE", (dialog, which) -> {

                        boolean result = db.deleteStudent(id);

                        if (result) {
                            showToast("Student Deleted Successfully");
                            clearFields();
                        } else {
                            showToast("Student ID Not Found");
                        }
                    })
                    .setNegativeButton("CANCEL", null)
                    .show();
        });

        // VIEW BUTTON
        btnView.setOnClickListener(v -> {

            Cursor cursor = db.getAllStudents();

            if (cursor.getCount() == 0) {
                showMessage("Students", "No Data Found");
                cursor.close();
                return;
            }

            StringBuilder data = new StringBuilder();

            int idIndex = cursor.getColumnIndexOrThrow("student_id");
            int nameIndex = cursor.getColumnIndexOrThrow("name");
            int enrolledIndex = cursor.getColumnIndexOrThrow("enrolled");

            while (cursor.moveToNext()) {

                data.append("Student ID: ")
                        .append(cursor.getString(idIndex))
                        .append("\n");

                data.append("Name: ")
                        .append(cursor.getString(nameIndex))
                        .append("\n");

                data.append("Enrolled: ")
                        .append(cursor.getInt(enrolledIndex) == 1
                                ? "Yes" : "No")
                        .append("\n\n");
            }

            cursor.close();

            showMessage("All Students", data.toString());
        });
    }

    private void showToast(String message) {
        Toast.makeText(
                this,
                message,
                Toast.LENGTH_SHORT
        ).show();
    }

    private void showMessage(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

    private void clearFields() {
        etName.setText("");
        etStudentId.setText("");
        cbEnrolled.setChecked(false);
    }
}
