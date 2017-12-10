package comp433.calebpipkin.hobjobtest2;

import android.graphics.Path;

/**
 * Created by calebyp on 12/9/17.
 */

public class DataProvider {
    private String type;
    private String contact;
    private String event;
    private String description;
    private String imageresource;

    public DataProvider(String type, String contact, String event, String description, String imageresource) {
        this.type = type;
        this.contact = contact;
        this.event = event;
        this.description = description;
        this.imageresource = imageresource;
    }

    public String getContact() {
        return contact;
    }

    public String getDescription() {
        return description;
    }

    public String getEvent() {
        return event;
    }

    public String getImageresource() {
        return imageresource;
    }

    public String getType() {
        return type;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public void setImageresource(String imageresource) {
        this.imageresource = imageresource;
    }

    public void setType(String type) {
        this.type = type;
    }
}
