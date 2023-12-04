package app.user;

import app.utils.Enums;

import java.util.ArrayList;

public class Host extends User {
    private ArrayList<String> hostedEvents; // Example additional property for Hosts

    public Host(String username, int age, String city) {
        super(username, age, city);
        this.hostedEvents = new ArrayList<>();
        setUserType(Enums.UserType.HOST);
    }


    public void addHostedEvent(String event) {
        hostedEvents.add(event);
    }


    public ArrayList<String> getHostedEvents() {
        return hostedEvents;
    }


}
