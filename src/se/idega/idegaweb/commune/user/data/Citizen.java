package se.idega.idegaweb.commune.user.data;

import com.idega.user.data.User;


public interface Citizen extends User
{
 public void store()throws com.idega.data.IDOStoreException;
}
