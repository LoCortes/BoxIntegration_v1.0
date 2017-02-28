package net.locortes.box.java.unirest.user;

/**
 * Created by vicenc.cortesolea on 24/01/2017.
 */
public class User {
    String type;
    String id;
    String name;
    String login;

    public User(String type, String id, String name, String login) {
        this.type = type;
        this.id = id;
        this.name = name;
        this.login = login;
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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public String toString() {
        return "User{" +
                "type='" + type + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", login='" + login + '\'' +
                '}';
    }
}
