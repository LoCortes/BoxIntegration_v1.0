package net.locortes.box.unirest.folder;

/**
 * Created by VICENC.CORTESOLEA on 11/10/2016.
 */
public class Folder {

    private String type;
    private String id;
    private String name;

    public Folder(String type, String id, String name) {
        this.type = type;
        this.id = id;
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isFolder() {
        if (type.equals("folder"))
            return true;
        return false;
    }

    @Override
    public String toString() {
        return "Type: " + type + " - ID:" + id + " - Name: " + name;
    }
}
