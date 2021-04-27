package edu.byu.cs240.familymap.model;

/**
 * Result for whether the settings have changed
 */
public class SettingsResult {
    private boolean eventChanges;
    private boolean lineChanges;

    public SettingsResult() {
        eventChanges = false;
        lineChanges = false;
    }

    public SettingsResult(boolean eventChanges, boolean lineChanges) {
        this.eventChanges = eventChanges;
        this.lineChanges = lineChanges;
    }

    public boolean isEventChanges() {
        return eventChanges;
    }

    public void setEventChanges(boolean eventChanges) {
        this.eventChanges = eventChanges;
    }

    public boolean isLineChanges() {
        return lineChanges;
    }

    public void setLineChanges(boolean lineChanges) {
        this.lineChanges = lineChanges;
    }
}
