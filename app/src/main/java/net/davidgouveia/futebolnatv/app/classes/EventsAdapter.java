package net.davidgouveia.futebolnatv.app.classes;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import net.davidgouveia.futebolnatv.app.R;

import java.util.ArrayList;

/**
 * Created by davidgouveia on 23/02/14.
 */
public class EventsAdapter extends ArrayAdapter<MyEvent> {
    private final Context context;
    //private final ArrayList<MyEvent> values;

    public EventsAdapter(Context context) {
        super(context, R.layout.list_entry);
        this.context = context;


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_entry, parent, false);

        TextView txtChannel = (TextView) rowView.findViewById(R.id.txtChannel);
        TextView txtDescription = (TextView) rowView.findViewById(R.id.txtDescription);
        TextView txtSchedule = (TextView) rowView.findViewById(R.id.txtSchedule);

        txtChannel.setText(this.getItem(position).channel);
        txtDescription.setText(this.getItem(position).home + " - " + this.getItem(position).away);
        txtSchedule.setText(this.getItem(position).date + " " + this.getItem(position).time);

        return rowView;
    }

}

