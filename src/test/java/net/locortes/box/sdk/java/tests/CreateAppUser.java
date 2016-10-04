package net.locortes.box.sdk.java.tests;

import com.box.sdk.*;
import net.locortes.box.sdk.java.BOXConnectionHelper;


import java.io.*;

/**
 * Created by VICENC.CORTESOLEA on 08/09/2016.
 */
public class CreateAppUser {

    /**
     * Method to create a user
     * @param api
     * @param name
     */
    public void createUser(BoxAPIConnection api, String name){
        /*
        DEFINING THE PARAMETERS THAT THE USERS WILL HAVE
        */
        CreateUserParams params = new CreateUserParams();
        params.setSpaceAmount(512 * 1024 * 1024); //512 MB

        System.out.println("Creating user:" + name);
        BoxUser.Info user = BoxUser.createAppUser(api, name, params);
        System.out.println("User created with name " + name + " and id " + user.getID() + " and login: " + user.getLogin());
        System.out.println("User created with name " + name);
    }

    /**
     * Main test to get a connection and crate a User named XXX
     * @param args
     */
    public void main(String[] args){
        BOXConnectionHelper boxConnectionHelper = new BOXConnectionHelper();
        BoxAPIConnection api = boxConnectionHelper.getConnection();

        CreateAppUser create = new CreateAppUser();
        create.createUser(api, "APP_USER_NAME");
    }
}
