package com.example.todoappgame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.todoappgame.Models.TodoItem;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private List<TodoItem> todoList;
    private ArrayAdapter<TodoItem> adapter;

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

        listView = findViewById(R.id.listView);
        todoList = new ArrayList<>();

        // Initialize custom adapter
        adapter = new TodoItemAdapter(this, todoList);
        listView.setAdapter(adapter);

        // Check if the Intent has the TodoItem extra
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("todoItem")) {
            TodoItem todoItem = (TodoItem) intent.getSerializableExtra("todoItem");
            if (todoItem != null) {
                todoList.add(todoItem);
                adapter.notifyDataSetChanged();
            }
        }











        // Find the FloatingActionButton by its ID
        FloatingActionButton fabAddTask = findViewById(R.id.fab_add_task);

        // Set an OnClickListener to open CreateTodoActivity
        fabAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the CreateTodoActivity
                Intent intent = new Intent(MainActivity.this, CreateTodoActivity.class);
                startActivity(intent);
            }
        });
    }
}