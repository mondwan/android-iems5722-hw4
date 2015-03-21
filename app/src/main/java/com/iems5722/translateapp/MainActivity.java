package com.iems5722.translateapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.instance_translation);

    }

    private void showTranslateRecords() {
        // Delegate the workload for showing records to ListTranslateRecord

        Intent intent = new Intent(MainActivity.this, ListTranslateRecord.class);
        startActivity(intent);
    }

    private void shareText() {
        // Share translated text to other application by using INTENT

        // Get translated text
        TextView translateTxtView = (TextView) this.findViewById(R.id.translated_txt_view);
        String translatedTxt = translateTxtView.getText().toString();

        // Create a send intent
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Translated text");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, translatedTxt);
        this.startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    private void translateText(String protocol) {
        // Delegate workloads for looking up dictionary to Class::OnlineWordDictionary with
        // given protocol
        //
        // @param protocol String

        // get user input
        EditText translateEdt = (EditText) this.findViewById(R.id.translate_edt);
        String inputTxt = translateEdt.getText().toString();
        String outputTxt;

        if (BuildConfig.DEBUG) {
            Log.d(TAG, String.format("User input %s", inputTxt));
        }

        // show some feedback to user: translated text, error message, dialog etc
        if (inputTxt.equals("")) {
            // Empty input
            outputTxt = "Input is empty";
            this.showTranslateEmptyToast(outputTxt);
        } else {
            // Send request to the online word dictionary
            OnlineWordDictionary myDictionary = new OnlineWordDictionary(this, protocol, inputTxt);
            myDictionary.execute();
        }
    }

    protected void showTranslateEmptyToast(String err) {
        // Pop up a toast with message err
        //
        // @param err String

        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(this.getApplicationContext(), err, duration);
        toast.show();
    }

    public void showTranslateErrorDialog(String err) {
        // Show error box with given message if there are any errors
        //
        // @param err String


        Bundle args = new Bundle();
        args.putString("errorMsg", err);
        TranslateErrorDialog dialog = new TranslateErrorDialog();
        dialog.setArguments(args);
        dialog.show(this.getFragmentManager(), "translationErrors");
    }


    // Options menu - not needed for this app
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
