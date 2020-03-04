import java.util.ArrayList;

public class Series {
    private String name;
    private ArrayList<Integer> events = new ArrayList<>();

    private int id;
    private static int numSeries = 0;

    public Series(String name) {
        numSeries ++;
        id = numSeries;
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    /**
     * Add the id of an event to this memo's list of associated events
     * @param newEvent the event whose id is being added
     * @return -1 if the event id is already in its events list, 1 if successfully added
     */
    public int addEvent(Event newEvent) {
        if (events.contains(newEvent.getId())) {
            return -1; //FAILURE
        } else {
            events.add(newEvent.getId());
            return 1; //SUCCESS
        }
    }

    public ArrayList<Integer> getEvents() {
        return this.events;
    }

}