package com.example.midterm_tanupreet_kaur;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.content.Intent;
import android.app.AlertDialog;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText num;
    Button btnGenerate, btnHistory;
    ListView listTable;
    ArrayAdapter<String> adapter;
    ArrayList<String> tableList = new ArrayList<>();

    // list for history
    public static ArrayList<Integer> historyList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        num = findViewById(R.id.numberTxt);
        btnGenerate = findViewById(R.id.btnGenerate);
        btnHistory = findViewById(R.id.btnHistory);
        listTable = findViewById(R.id.listTable);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tableList);
        listTable.setAdapter(adapter);
        
        btnGenerate.setOnClickListener(v -> {
            String input = num.getText().toString().trim();

            if (input.isEmpty()) {
                Toast.makeText(this, "Please enter a number", Toast.LENGTH_SHORT).show();
                return;
            }

            int num = Integer.parseInt(input);
            generateTable(num);

            // Add to history if not already there
            if (!historyList.contains(num)) {
                historyList.add(num);
            }
        });

        listTable.setOnItemClickListener((parent, view, position, id) -> {
            String selectedItem = tableList.get(position);
            showDeleteDialog(position, selectedItem);
        });

        btnHistory.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
        });

    }

    private void showDeleteDialog(int position, String selectedItem) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Entry")
                .setMessage("Do you want to delete: " + selectedItem + "?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    tableList.remove(position);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(this, "Deleted: " + selectedItem, Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void generateTable(int num) {
        tableList.clear();
        for(int i = 1; i <= 10; i++){
            tableList.add(num + " * " + i + " = " + (num * i));
        }
        adapter.notifyDataSetChanged();
    }

    // Inflate the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    // Handle menu actions
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.clear_all) {
            if (tableList.isEmpty()) {
                Toast.makeText(this, "No items to clear", Toast.LENGTH_SHORT).show();
                return true;
            }

            new AlertDialog.Builder(this)
                    .setTitle("Clear All")
                    .setMessage("Are you sure to delete all items?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        tableList.clear();
                        adapter.notifyDataSetChanged();
                        Toast.makeText(this, "All rows got cleared!", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("No", null)
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}