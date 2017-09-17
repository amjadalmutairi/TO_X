package me.amjadalmutairi.todo;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.amjadalmutairi.todo.data.ItemContract;
import me.amjadalmutairi.todo.data.ItemHelper;

import static me.amjadalmutairi.todo.ListActivity.ADDED_CODE;

public class AddNewItemActivity extends AppCompatActivity {

    private MainActivity.Category category;

    @BindView(R.id.save)
    ImageButton saveButton;
    @BindView(R.id.cancel)
    ImageButton cancelButton;
    @BindView(R.id.title)
    EditText titleEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_item);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        category = (MainActivity.Category) intent.getSerializableExtra("Category");

        setTitle(Html.fromHtml("  Add new "  + "TO<sup><small>2</small></sup>" + " " +category.name()));

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_icon);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(! titleEditText.getText().toString().trim().equals("")) {

                    ItemHelper mDbHelper = new ItemHelper(getApplicationContext());
                    SQLiteDatabase db = mDbHelper.getWritableDatabase();
                    String title = titleEditText.getText().toString().trim();
                    ContentValues values = new ContentValues();
                    values.put(ItemContract.ItemEntry.COLUMN_TITLE,title);
                    values.put(ItemContract.ItemEntry.COLUMN_COMPLETED, ItemContract.ItemEntry.NOT_COMPLETED);
                    values.put(ItemContract.ItemEntry.COLUMN_CATEGORY,category.name());

                    long newRowId = db.insert(ItemContract.ItemEntry.TABLE_NAME,null,values);

                    if (newRowId == -1) {
                        Toast.makeText(getApplicationContext(), getString(R.string.cannot_save_item_error_msg), Toast.LENGTH_SHORT).show();
                    } else {

                        Item item = new Item(String.valueOf(newRowId) , title,category, ItemContract.ItemEntry.NOT_COMPLETED);
                        Intent intent = new Intent();
                        intent.putExtra("newItem", item);
                        setResult(ADDED_CODE, intent);
                        finish();
                    }
                } else {
                    Toast.makeText(AddNewItemActivity.this, getString(R.string.missing_title_error_msg), Toast.LENGTH_LONG).show();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(-1);
                finish();
            }
        });

    }
}
