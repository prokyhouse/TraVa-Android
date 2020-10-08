package ru.myitschool.travamd.models;

/**
 * Created by kirillprokofev on 29.05.17.
 */

public class Actor {
    String actorName, actorCharacter, actorProfilePath, actorID;

    public Actor(String actorID, String actorName, String actorCharacter, String actorProfilePath) {

        this.actorName = actorName;
        this.actorCharacter = actorCharacter;
        this.actorProfilePath = actorProfilePath;
        this.actorID = actorID;

    }

    public String getActorID() {
        return actorID;
    }

    public String getActorName() {
        return actorName;
    }

    public String getActorCharacter() {
        return actorCharacter;
    }

    public String getActorProfilePath() {
        return actorProfilePath;
    }

}
