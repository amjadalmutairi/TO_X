package me.amjadalmutairi.todo;

import java.io.Serializable;

/**
 * Created by amjadalmutairi on 9/16/17.
 */

public class Item implements Serializable {

    private String id;
    private String title;
    private MainActivity.Category category;
    private int completed;

    public Item(String id, String title, MainActivity.Category category, int completed) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.completed = completed;
    }

    public String getId() {
        return id;
    }

    public int isCompleted() {
        return completed;
    }

    public String getTitle() {
        return title;

    }

    public MainActivity.Category getCategory() {
        return category;
    }

    public void setCompleted(int completed) {
        this.completed = completed;
    }
}
