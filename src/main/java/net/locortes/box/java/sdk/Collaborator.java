package net.locortes.box.java.sdk;

import org.apache.commons.lang.StringUtils;

/**
 * Created by vicenc.cortesolea on 28/10/2016.
 */
public class Collaborator {
    private String name;
    private String role;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Collaborator(String name, String role) {
        this.name = name;
        this.role = role;
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
                .append(Character.toString((char) 240))
                .append(" User: ").append(this.name)
                .append(" - Role: ").append(this.role).append("\n")
                .toString();
    }
}

