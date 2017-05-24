package com.akatastroph.projectvelib.utils.events;

import com.akatastroph.projectvelib.model.Station;

import java.util.ArrayList;

/**
 * Created by genfinternet on 13/05/2017.
 */

public class Event {
    public static class StationListUpdatedEvent{
        private ArrayList<Station> mStations;

        public StationListUpdatedEvent(ArrayList<Station> stations) {
            this.mStations = stations;
        }

        public ArrayList<Station> getStations() {
            return mStations;
        }
    }

    public static class FilteredStationListUpdatedEvent{
        private ArrayList<Station> mStations;

        public FilteredStationListUpdatedEvent(ArrayList<Station> stations) {
            this.mStations = stations;
        }

        public ArrayList<Station> getStations() {
            return mStations;
        }
    }
}
