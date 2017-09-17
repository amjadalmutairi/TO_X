package me.amjadalmutairi.todo;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import java.util.ArrayList;

import me.amjadalmutairi.todo.data.ItemContract;
import me.amjadalmutairi.todo.data.ItemHelper;

/**
 * Created by amjadalmutairi on 9/16/17.
 */

public class ItemAdapter extends ArrayAdapter<Item> {

    private Context context;
    private ArrayList<Item> items;

    public ItemAdapter(Context context, ArrayList<Item> items) {
        super(context, 0, items);
        this.context = context;
        this.items =items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        final Item item = getItem(position);
        CheckBox checkBox = listItemView.findViewById(R.id.completed_checkBox);
        checkBox.setText(item.getTitle());

        if (item.isCompleted() == ItemContract.ItemEntry.COMPLETED) {
            checkBox.setChecked(true);
        }

        LinearLayout linearLayoutView = listItemView.findViewById(R.id.item_view);
        switch (item.getCategory()) {
            case READ:
                linearLayoutView.setBackgroundColor(context.getResources().getColor(R.color.dark_blue));
                break;
            case WATCH:
                linearLayoutView.setBackgroundColor(context.getResources().getColor(R.color.light_blue));
                break;
            default:
                linearLayoutView.setBackgroundColor(context.getResources().getColor(R.color.light_green));
        }

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                ItemHelper mDbHelper = new ItemHelper(context);
                SQLiteDatabase db = mDbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                String selection = ItemContract.ItemEntry._ID + " LIKE ?";
                String[] selectionArgs = { item.getId() };

                if (item.isCompleted() == ItemContract.ItemEntry.COMPLETED) {
                    values.put(ItemContract.ItemEntry.COLUMN_COMPLETED, ItemContract.ItemEntry.NOT_COMPLETED);
                    item.setCompleted(ItemContract.ItemEntry.NOT_COMPLETED);
                }

                else {
                    values.put(ItemContract.ItemEntry.COLUMN_COMPLETED, ItemContract.ItemEntry.COMPLETED);
                    item.setCompleted(ItemContract.ItemEntry.COMPLETED);
                }

                int count = db.update(
                        ItemContract.ItemEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
            }
        });

        checkBox.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder confirmReset = new AlertDialog.Builder(context)
                        .setTitle("")
                        .setMessage(context.getString(R.string.delete_item_confirm_q))
                        .setPositiveButton(context.getString(R.string.yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                ItemHelper mDbHelper = new ItemHelper(context);
                                SQLiteDatabase db = mDbHelper.getWritableDatabase();
                                String selection = ItemContract.ItemEntry._ID + " LIKE ?";
                                String[] selectionArgs = { item.getId() };
                                db.delete(ItemContract.ItemEntry.TABLE_NAME, selection, selectionArgs);
                                items.remove(item);
                                notifyDataSetChanged();
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alertDialog = confirmReset.create();
                alertDialog.show();
                return true;
            }

        });
        return listItemView;
    }

    public ArrayList<Item> getData() {
        return items;
    }



}
