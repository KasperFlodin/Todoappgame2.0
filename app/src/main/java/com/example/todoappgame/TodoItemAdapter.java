package com.example.todoappgame;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.todoappgame.Models.TodoItem;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TodoItemAdapter extends ArrayAdapter<TodoItem> {

    private final Context context;
    private final List<TodoItem> todoItems;

    public TodoItemAdapter(Context context, List<TodoItem> items) {
        super(context, R.layout.list_item_todo, items);
        this.context = context;
        this.todoItems = items;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_todo, parent, false);
        }

        TodoItem todoItem = todoItems.get(position);

        TextView nameTextView = convertView.findViewById(R.id.todo_name);
        TextView descriptionTextView = convertView.findViewById(R.id.todo_description);
        TextView startTimeTextView = convertView.findViewById(R.id.todo_start_time);
        TextView endTimeTextView = convertView.findViewById(R.id.todo_end_time);
        TextView completedTextView = convertView.findViewById(R.id.todo_completed);
        TextView repeatableTextView = convertView.findViewById(R.id.todo_repeatable);

        if (nameTextView != null) {
            nameTextView.setText(todoItem.getName());
        }
        if (descriptionTextView != null) {
            descriptionTextView.setText(todoItem.getDescription());
        }
        if (startTimeTextView != null) {
            startTimeTextView.setText("Start Time: " + todoItem.getPlannedStartTime());
        }
        if (endTimeTextView != null) {
            endTimeTextView.setText("End Time: " + todoItem.getPlannedEndTime());
        }
        if (completedTextView != null) {
            completedTextView.setText("Completed: " + (todoItem.getCompleted() ? "Yes" : "No"));
        }
        if (repeatableTextView != null) {
            repeatableTextView.setText("Repeatable: " + (todoItem.getRepeatable() ? "Yes" : "No"));
        }

        return convertView;
    }
}
