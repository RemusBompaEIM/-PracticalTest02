package ro.pub.cs.systems.eim.practicaltest02.model;

public class Alarm {

    public Alarm(String hour, String minute) {
        this.hour = hour;
        this.minute = minute;
    }


    private String hour;
    private String minute;

    @Override
    public String toString() {
        return "Alarm{" +
                "hour='" + hour + '\'' +
                ", minute='" + minute + '\'' +
                '}';
    }
}