package net.davidgouveia.futebolnatv.app.classes;

/**
 * Created by davidgouveia on 23/02/14.
 */
public class MyEvent {
    public String date, time, home, away, channel;

    public MyEvent( String date, String time, String home, String away, String channel)
    {
        this.date = date;
        this.time = time;
        this.home = home;
        this.away = away;
        this.channel = channel;
    }

}
