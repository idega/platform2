package com.idega.block.email.client.business;

import java.util.*;
import javax.mail.*;

/**
 * This JavaBean is used to store mail user information.
 */
public class MailUserBean {
    private Folder folder;
    private String hostname;
    private String username;
    private String password;
    private Session session;
    private Store store;
    private URLName url;
    private String protocol = "imap";
    private String mbox = "INBOX";

    public MailUserBean(){}

    /**
     * Returns the javax.mail.Folder object.
     */
    public Folder getFolder() {
        return folder;
    }

    /**
     * Returns the number of messages in the folder.
     */
    public int getMessageCount() throws MessagingException {
        return folder.getMessageCount();
    }

    /**
     * hostname getter method.
     */
    public String getHostname() {
        return hostname;
    }

    /**
     * hostname setter method.
     */
    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    /**
     * username getter method.
     */
    public String getUsername() {
        return username;
    }

    /**
     * username setter method.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * password getter method.
     */
    public String getPassword() {
        return password;
    }

    /**
     * password setter method.
     */
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

     /**
     * password getter method.
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     * password setter method.
     */
    public void setPassword(String password) {
        this.password = password;
    }


    /**
     * session getter method.
     */
    public Session getSession() {
        return session;
    }

    /**
     * session setter method.
     */
    public void setSession(Session s) {
        this.session = s;
    }

    /**
     * store getter method.
     */
    public Store getStore() {
        return store;
    }

    /**
     * store setter method.
     */
    public void setStore(Store store) {
        this.store = store;
    }

    /**
     * url getter method.
     */
    public URLName getUrl() {
        return url;
    }

    /**
     * Method for checking if the user is logged in.
     */
    public boolean isLoggedIn() {
        return store.isConnected();
    }

    /**
     * Method used to login to the mail host.
     */
    public void login() throws Exception {
        url = new URLName(protocol, getHostname(), -1, mbox,
                          getUsername(), getPassword());
        Properties props = System.getProperties();
        session = Session.getInstance(props, null);
        store = session.getStore(url);
        store.connect();
        folder = session.getFolder(url);

        folder.open(Folder.READ_WRITE);
    }

    /**
     * Method used to login to the mail host.
     */
    public void login(String hostname, String username, String password,String protocol)
        throws Exception {

        this.protocol = protocol;
        this.hostname = hostname;
        this.username = username;
        this.password = password;

        login();
    }

    /**
     * Method used to logout from the mail host.
     */
    public void logout() throws MessagingException {
        folder.close(false);
        store.close();
        store = null;
        session = null;
    }
}

