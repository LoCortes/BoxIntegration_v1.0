package net.locortes.box.java.sdk;

import org.apache.commons.lang.StringUtils;

/**
 * Created by vicenc.cortesolea on 28/10/2016.
 */
public class Document {
    private String id;
    private String name;

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

    public Document(String id) {

        this.id = id;
    }

    public Document(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return toString(0);
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
