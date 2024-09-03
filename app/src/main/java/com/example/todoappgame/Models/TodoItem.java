package com.example.todoappgame.Models;

import android.location.Location;
import android.widget.TimePicker;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;
import java.util.List;
import java.util.UUID;

enum typeTask {
        Cleaning,
        MealPrep,
        YardWork,
        VacationPlanning,
        Television
    }

enum priority {
    High,
    Normal,
    Low,
}

public class TodoItem implements Serializable {
    UUID id;
    String name;
    String description;
    String PlannedStartTime;
    String PlannedEndTime;
    Boolean isCompleted = false;
    Boolean isRepeatable = false;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPlannedStartTime() {
        return PlannedStartTime;
    }

    public void setPlannedStartTime(String plannedStartTime) {
        PlannedStartTime = plannedStartTime;
    }

    public String getPlannedEndTime() {
        return PlannedEndTime;
    }

    public void setPlannedEndTime(String plannedEndTime) {
        PlannedEndTime = plannedEndTime;
    }

    public Boolean getCompleted() {
        return isCompleted;
    }

    public void setCompleted(Boolean completed) {
        isCompleted = completed;
    }

    public Boolean getRepeatable() {
        return isRepeatable;
    }

    public void setRepeatable(Boolean repeatable) {
        isRepeatable = repeatable;
    }
}

