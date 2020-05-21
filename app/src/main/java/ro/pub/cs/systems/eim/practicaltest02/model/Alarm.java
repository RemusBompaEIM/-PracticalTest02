package ro.pub.cs.systems.eim.practicaltest02.model;

public class Alarm {

    public Alarm(String hour, String minute) {
        this.hour = hour;
        this.minute = minute;
        this.activated = false;
    }


    private String hour;
    private String minute;

    public Boolean getActivated() {
        return activated;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    private Boolean activated;

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    @Override
    public String toString() {
        return "Alarm{" +
                "hour='" + hour + '\'' +
                ", minute='" + minute + '\'' +
                '}';
    }
}