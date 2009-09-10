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
        return this.folder;
    }

    /**
     * Returns the number of messages in the folder.
     */
    public int getMessageCount() throws MessagingException {
        return this.folder.getMessageCount();
    }

    /**
     * hostname getter method.
     */
    public String getHostname() {
        return this.hostname;
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
        return this.username;
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
        return this.password;
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
        return this.protocol;
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
        return this.session;
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
        return this.store;
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
        return this.url;
    }

    /**
     * Method for checking if the user is logged in.
     */
    public boolean isLoggedIn() {
        return this.store.isConnected();
    }

    /**
     * Method used to login to the mail host.
     */
    public void login() throws Exception {
        this.url = new URLName(this.protocol, getHostname(), -1, this.mbox,
                          getUsername(), getPassword());
        Properties props = System.getProperties();
        this.session = Session.getInstance(props, null);
        this.store = this.session.getStore(this.url);
        this.store.connect();
        this.folder = this.session.getFolder(this.url);

        this.folder.open(Folder.READ_WRITE);
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
        this.folder.close(false);
        this.store.close();
        this.store = null;
        this.session = null;
    }
}

