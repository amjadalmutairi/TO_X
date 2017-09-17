package me.amjadalmutairi.todo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AboutActivity extends AppCompatActivity {

    @BindView(R.id.email_button)
    ImageButton emailButton;
    @BindView(R.id.github_button)
    ImageButton githubButton;
    @BindView(R.id.share_button)
    ImageButton shareButton;
    @BindView(R.id.share_view)
    TextView shareTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("  " + getString(R.string.app_name));
        getSupportActionBar().setIcon(R.drawable.ic_icon);

        ButterKnife.bind(this);

        shareTextView.setText(getString(R.string.share));

        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","mutairi.amjad@gmail.com", null));
                startActivity(Intent.createChooser(emailIntent, getString(R.string.send_email)));
            }
        });

        githubButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://github.com/amjadalmutairi/TO_X.git";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String packageName = getApplicationContext().getPackageName();
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/html");
                String shareBody = getString(R.string.share_message) + "\n" + "https://play.google.com/store/apps/details?id=" + packageName ;
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.share));
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, getString(R.string.share)));
            }
        });
    }
}
