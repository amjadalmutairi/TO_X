package me.amjadalmutairi.todo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

   public enum Category {
        READ , WATCH , DO;


       public static Category getCategory(String value) {

           switch (value){
               case "READ" : return READ;
               case "WATCH" : return WATCH;
               default: return DO;
           }
       }
   }

    @BindView(R.id.about_button)
    ImageButton aboutButton;
    @BindView(R.id.share_button)
    ImageButton shareButton;
    @BindView(R.id.to_read)
    TextView toReadTextView;
    @BindView(R.id.to_watch)
    TextView toWatchTextView;
    @BindView(R.id.to_do)
    TextView toDoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("  " + getString(R.string.app_name));
        getSupportActionBar().setIcon(R.drawable.ic_icon);

        ButterKnife.bind(this);

        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
                startActivity(intent);
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String packageName = getApplicationContext().getPackageName();
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/html");
                String shareBody = getString(R.string.share_message) + "\n" + "https://play.google.com/store/apps/details?id=" + packageName ;
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.share) + " "  + getString(R.string.app_name));
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, getString(R.string.share)));
            }
        });

        toReadTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                intent.putExtra("Category", Category.READ);
                startActivity(intent);

            }
        });

        toWatchTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                intent.putExtra("Category", Category.WATCH);
                startActivity(intent);
            }
        });

        toDoTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                intent.putExtra("Category", Category.DO);
                startActivity(intent);
            }
        });
    }


}