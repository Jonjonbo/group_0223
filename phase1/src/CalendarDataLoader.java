import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class CalendarDataLoader {
    /**
     * @author Danial
     *
     * Converts the JSONObject, time, into a LocalDateTime object
     * @param  time JSONObject to be converted
     * @return
     * LocalDateTime object
     */
    private static LocalDateTime makeLocalDateTime(JSONObject time){
        int year = ((Long)time.get("year")).intValue();
        int month = ((Long)time.get("month")).intValue();
        int day = ((Long)time.get("day")).intValue();
        int hour = ((Long)time.get("hour")).intValue();
        int min = ((Long)time.get("min")).intValue();
        return (LocalDateTime.of(year, month, day, hour, min));
    }
    /**
     * @author Danial
     *
     * Loads the Arraylists in variables with Event, Memo,
     * Alert, and Series objects using JSONArray filled with
     * JSONOBjects from toBeLoaded
     * @param  toBeLoaded ArrayList containing JSONArrays
     *                   to be converted
     * @param variables ArrayList of ArrayLists to be filled with Event, Memo,
     *      * Alert, and Series objects
     */
    public static void loadData(ArrayList<JSONArray> toBeLoaded, ArrayList<ArrayList> variables) {
        loadMemos(toBeLoaded.get(0), variables.get(0));
        loadAlerts(toBeLoaded.get(1), variables.get(1));
        loadEvents(toBeLoaded.get(2), variables.get(2));
        loadSeries(toBeLoaded.get(3), variables.get(3));
    }
    /**
     * @author Danial
     *
     * Turns the JSONObjects from jsonmemos into Memo
     * objects, and adds them to memos
     * @param  jsonmemos JSONArray containing JSONObjects
     *                   to be converted
     * @param memos An Array to be filled with Memo objects
     */
    private static void loadMemos(JSONArray jsonmemos, ArrayList<Memo> memos){
        for(Object jmemo: jsonmemos){
            JSONObject jsonmemo = (JSONObject) jmemo;
            String content = (String)jsonmemo.get("content");
            Memo createdMemo = new Memo(content);
            for(Object id: (JSONArray)jsonmemo.get("event ids")){
//                Long  intval = (Long)id;
                createdMemo.addEvent(((Long) id).intValue());
            }
            memos.add(createdMemo);
        }
    }
    /**
     * @author Danial
     *
     * Turns the JSONObjects from jsonevents into Event
     * objects, and adds them to events
     * @param  jsonevents JSONArray containing JSONObjects
     *                   to be converted
     * @param events An Array to be filled with Event objects
     */
    private static void loadEvents(JSONArray jsonevents, ArrayList<Event> events){
        for(Object jevent: jsonevents){
            JSONObject jsonevent = (JSONObject) jevent;
            String name = (String)jsonevent.get("name");
            JSONObject stime = (JSONObject) jsonevent.get("start time");
            JSONObject etime = (JSONObject) jsonevent.get("end time");
            JSONArray tags = (JSONArray) jsonevent.get("tags");
            JSONArray alertids = (JSONArray) jsonevent.get("alert ids");
            LocalDateTime eventStartTime = makeLocalDateTime(stime);
            LocalDateTime eventEndTime = makeLocalDateTime(etime);
            Event createdEvent = new Event(name, eventStartTime, eventEndTime);
            if(jsonevent.get("memo id") != null) {
                for (Memo m : CalendarDataFacade.getMemos()) {
                    if (m.getId() == ((Long) jsonevent.get("memo id")).intValue()){
                        createdEvent.addMemo(m);
                    }
                }
            }
//            for(Object t: tags){
//                System.out.println((String) t);
//                createdEvent.addTag((String) t);
//                System.out.println(t);
//            }
            for(Object id: alertids){
                for(Alert alert: CalendarDataFacade.getAlerts()){
                    if((Integer)id == alert.getId()){
                        createdEvent.addAlert(alert);
                    }
                }
            }
            events.add(createdEvent);
        }
    }
    /**
     * @author Danial
     *
     * Turns the JSONObjects from jsonseries into Series
     * objects, and adds them to series
     * @param  jsonseries JSONArray containing JSONObjects
     *                   to be converted
     * @param series An Array to be filled with Series objects
     */
    private static void loadSeries(JSONArray jsonseries, ArrayList<Series> series){
        for(Object js: jsonseries){
            JSONObject jsonse = (JSONObject) js;
            String name = (String)jsonse.get("name");
            Series createdSerie = new Series(name);
            for(Object id: (JSONArray)jsonse.get("event ids")){
                for(Event event: CalendarDataFacade.getEvents()){
                    if(((Long)id).intValue() == event.getId()){
                        createdSerie.addEvent(event);
                    }
                }
            }
            series.add(createdSerie);
        }
    }
    /**
     * @author Danial
     *
     * Turns the JSONObjects from jsonseries into Alert
     * objects, and adds them to alerts
     * @param  jsonalerts JSONArray containing JSONObjects
     *                   to be converted
     * @param alerts An Array to be filled with Alert objects
     */
    private static void loadAlerts(JSONArray jsonalerts, ArrayList<Alert> alerts){
        for(Object jalert: jsonalerts){
            JSONObject jsonalert = (JSONObject) jalert;
            String name = (String)jsonalert.get("name");
            JSONObject time = (JSONObject) jsonalert.get("time");
            LocalDateTime alertTime = makeLocalDateTime(time);
            alerts.add(new Alert(name, alertTime));
        }
    }
}