package edu.byu.cs240.familymap.model;

/**
 * Settings for the user's map, including which events to show on the map and
 * person view and what lines to show
 */
public class Settings {
    private final MapType mapType;
    private boolean lifeStoryLines;
    private boolean familyTreeLines;
    private boolean spouseLines;
    private boolean fatherSide;
    private boolean motherSide;
    private boolean maleEvents;
    private boolean femaleEvents;

    public Settings() {
        mapType = MapType.ROADMAP;
        lifeStoryLines = true;
        familyTreeLines = true;
        spouseLines = true;
        fatherSide = true;
        motherSide = true;
        maleEvents = true;
        femaleEvents = true;
    }

    public MapType getMapType() {
        return mapType;
    }

    public boolean isFatherSide() {
        return fatherSide;
    }

    public void setFatherSide(boolean fatherSide) {
        this.fatherSide = fatherSide;
    }

    public boolean isMotherSide() {
        return motherSide;
    }

    public void setMotherSide(boolean motherSide) {
        this.motherSide = motherSide;
    }

    public boolean isMaleEvents() {
        return maleEvents;
    }

    public void setMaleEvents(boolean maleEvents) {
        this.maleEvents = maleEvents;
    }

    public boolean isFemaleEvents() {
        return femaleEvents;
    }

    public void setFemaleEvents(boolean femaleEvents) {
        this.femaleEvents = femaleEvents;
    }

    public boolean isLifeStoryLines() {
        return lifeStoryLines;
    }

    public void setLifeStoryLines(boolean lifeStoryLines) {
        this.lifeStoryLines = lifeStoryLines;
    }

    public boolean isFamilyTreeLines() {
        return familyTreeLines;
    }

    public void setFamilyTreeLines(boolean familyTreeLines) {
        this.familyTreeLines = familyTreeLines;
    }

    public boolean isSpouseLines() {
        return spouseLines;
    }

    public void setSpouseLines(boolean spouseLines) {
        this.spouseLines = spouseLines;
    }
}
