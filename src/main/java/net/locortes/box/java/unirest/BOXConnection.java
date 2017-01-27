package net.locortes.box.java.unirest;

/**
 * Created by VICENC.CORTESOLEA on 10/10/2016.
 */
public class BOXConnection {

    String access_token;
    int expires_in;
    String token_type;
    String refresh_token;
    Object restricted_to;

    public BOXConnection(String access_token, int expires_in, String token_type, String refresh_token, Object restricted_to) {
        this.access_token = access_token;
        this.expires_in = expires_in;
        this.token_type = token_type;
        this.refresh_token = refresh_token;
        this.restricted_to = restricted_to;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public Object getRestricted_to() {
        return restricted_to;
    }

    public void setRestricted_to(Object restricted_to) {
        this.restricted_to = restricted_to;
    }

    @Override
    public String toString() {
        return "Access Token: " + getAccess_token()
                + "\nExpires in: " + getExpires_in()
                + "\nToken type: " + getToken_type()
                + "\nRefresh Token: " + getRefresh_token()
                + "\nRestricted to: " + getRestricted_to()
        ;
    }
}
