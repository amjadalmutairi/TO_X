package me.amjadalmutairi.todo;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.amjadalmutairi.todo.MainActivity.Category;
import me.amjadalmutairi.todo.data.ItemContract;
import me.amjadalmutairi.todo.data.ItemHelper;

/**
 * Created by amjadalmutairi on 9/16/17.
 */

public class ListActivity extends AppCompatActivity {

    private Category category;
    private ArrayList<Item> items;
    private ItemAdapter itemAdapter;
    @BindView(R.id.empty_list_message_view)
    TextView empty_list ;
    @BindView(R.id.delete_all_button)
    ImageButton deleteAll;
    @BindView(R.id.add_new_item)
    ImageButton addNewItem;

    public static final int ADDED_CODE = 100;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        category = (Category) intent.getSerializableExtra("Category");
        items = getItems(category);

        setTitle(" TO " + category.name());

        ColorDrawable colorDrawable;
        switch (category) {
            case READ:
                colorDrawable = (new ColorDrawable(getResources().getColor(R.color.dark_blue)));
                empty_list.setTextColor(getResources().getColor(R.color.dark_blue));
                break;
            case WATCH:
                colorDrawable = (new ColorDrawable(getResources().getColor(R.color.light_blue)));
                empty_list.setTextColor(getResources().getColor(R.color.light_blue));
                break;
            default:
                colorDrawable = (new ColorDrawable(getResources().getColor(R.color.light_green)));
                empty_list.setTextColor(getResources().getColor(R.color.light_green));
        }

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_icon);
        getSupportActionBar().setBackgroundDrawable(colorDrawable);

        TextView empty_list = (TextView) findViewById(R.id.empty_list_message_view);
        empty_list.setText(getString(R.string.empty_message) + " " + category.name() + ".");

        final ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setEmptyView(empty_list);


        itemAdapter = new ItemAdapter(this, items);
        listView.setAdapter(itemAdapter);

        deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder confirmReset = new AlertDialog.Builder(ListActivity.this)
                        .setTitle("")
                        .setMessage(getString(R.string.delete_all_items_confirm_q))
                        .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                ItemHelper mDbHelper = new ItemHelper(getApplicationContext());
                                SQLiteDatabase db = mDbHelper.getWritableDatabase();
                                String selection =  ItemContract.ItemEntry.COLUMN_CATEGORY + " LIKE ?";
                                String[] selectionArgs = { category.name() };
                                db.delete( ItemContract.ItemEntry.TABLE_NAME, selection, selectionArgs);

                                items = new ArrayList<>();
                                itemAdapter.getData().clear();
                                itemAdapter.getData().addAll(items);
                                itemAdapter.notifyDataSetChanged();
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alertDialog = confirmReset.create();
                alertDialog.show();
            }
        });


        addNewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListActivity.this, AddNewItemActivity.class);
                intent.putExtra("Category", category);
                startActivityForResult(intent , ADDED_CODE);
            }
        });
    }

    private ArrayList<Item> getItems(Category category){

        ArrayList<Item> items = new ArrayList<>();

        ItemHelper mDbHelper = new ItemHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = {
                ItemContract.ItemEntry._ID,
                ItemContract.ItemEntry.COLUMN_TITLE,
                ItemContract.ItemEntry.COLUMN_CATEGORY,
                ItemContract.ItemEntry.COLUMN_COMPLETED
        };

        String selection =  ItemContract.ItemEntry.COLUMN_CATEGORY + " = ?";
        String[] selectionArgs = { category.name()};

        String sortOrder = ItemContract.ItemEntry.COLUMN_COMPLETED + " DESC";

        Cursor cursor = db.query(
                ItemContract.ItemEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );

        if (cursor.moveToFirst()) {
            do {
                int idIndex = cursor.getColumnIndex(ItemContract.ItemEntry._ID);
                int titleIndex = cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_TITLE);
                int categoryIndex = cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_CATEGORY);
                int completedIndex = cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_COMPLETED);

                String id = cursor.getString(idIndex);
                String title = cursor.getString(titleIndex);
                String category_ = cursor.getString(categoryIndex);
                int completed = cursor.getInt(completedIndex);

                items.add(new Item(id,title,Category.getCategory(category_),completed));

            } while (cursor.moveToNext());
        }

        return items;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == ADDED_CODE) {
            Item item = (Item) data.getExtras().getSerializable("newItem");
            itemAdapter.add(item);
            itemAdapter.notifyDataSetChanged();
        }
    }
}