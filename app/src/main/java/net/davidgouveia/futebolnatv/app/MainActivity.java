package net.davidgouveia.futebolnatv.app;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import net.davidgouveia.futebolnatv.app.classes.EventsAdapter;
import net.davidgouveia.futebolnatv.app.classes.MyEvent;
import net.davidgouveia.futebolnatv.app.lib.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MainActivity extends ActionBarActivity {

    private static final String TAG = "Main";
    Utils utils;
    Button btnRefresh;
    TextView txtTitle;
    ListView lstEvents;
    ArrayList eventList = new ArrayList<String>();

    private void langSelector()
    {
        Locale.getDefault().getDisplayLanguage();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        utils = new Utils(this);
        btnRefresh = (Button) findViewById(R.id.btnRefresh);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        lstEvents = (ListView) findViewById(R.id.lstEvents);

        btnRefresh.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       if ( utils.checkInternetConnection() )
                          new DownloadWebPageTask().execute(new String[] {"http://davidgouveia.net/goodies/getgames.php"});
                       else
                          Toast.makeText(MainActivity.this,
                                  getResources().getString(R.string.noConnection),
                                  Toast.LENGTH_LONG).show();
                    }
                }
        );
        //Toast.makeText(this, "Ready!", Toast.LENGTH_SHORT).show();
    }


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
        if (id == R.id.action_about) {
            AlertDialog ad = new AlertDialog.Builder(this).create();
            ad.setCancelable(false); // This blocks the 'BACK' button
            ad.setTitle(getResources().getString(R.string.action_about) + " Futebol na TV...");
            ad.setMessage("Developed by:\n\n" +
                    "David Gouveia:\n" +
                    "david.gouveia@gmail.com\n" +
                    "twitter.com/davidsgouveia\n" +
                    "http://www.davidgouveia.net");
            ad.setButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            ad.show();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class DownloadWebPageTask extends AsyncTask<String, Void, String> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(MainActivity.this);
            pd.setTitle(R.string.processing);
            pd.setMessage(getResources().getString(R.string.main_processing));
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            for (String url : urls) {
                try{
                    response = utils.download(url);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, e.getMessage());
                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            /*
                Split all the content by lines (each line represents an event)
            */

            String[] events;
            if ( result != null && (events = result.split("\n")).length  > 0 )
            {
                final EventsAdapter adapter = new EventsAdapter(MainActivity.this);
                adapter.clear();
                for( String event : events)
                {
                    String[] eventDetails = event.split(";;");
                    //Only try to add valid events
                    if ( eventDetails.length >= 5)
                    adapter.add(new MyEvent(eventDetails[0],
                                            eventDetails[1],
                                            eventDetails[2],
                                            eventDetails[3],
                            eventDetails[4]) );
                }
                lstEvents.setAdapter(adapter);

            }else
                Toast.makeText(MainActivity.this, "No events found...", Toast.LENGTH_LONG).show();
            pd.dismiss();
        }

    }

}
