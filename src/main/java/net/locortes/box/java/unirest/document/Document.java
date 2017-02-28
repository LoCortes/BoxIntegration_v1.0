package net.locortes.box.java.unirest.document;

import org.apache.commons.lang.StringUtils;

/**
 * Created by vicenc.cortesolea on 28/10/2016.
 */
public class Document {
    private String id;
    private String name;
    private String description;
    private String item_status;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getItem_status() {
        return item_status;
    }

    public void setItem_status(String item_status) {
        this.item_status = item_status;
    }

    public Document(String id) {

        this.id = id;
    }

    public Document(String id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * @return
     */
    @Override
    public String toString() {
        return "Document{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", item_status='" + item_status + '\'' +
                '}';
    }

    /**
     *
     * @param level
     * @return
     */
    public String toString(int level) {
        return new StringBuilder(StringUtils.repeat("\t", level))
                .append(" · ")
                .append(" Document Name: ").append(this.name)
                .append(" - Id: ").append(this.id).append("\n")
                .toString();
    }
}
