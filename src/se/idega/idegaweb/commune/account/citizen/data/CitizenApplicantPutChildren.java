package se.idega.idegaweb.commune.account.citizen.data;

public interface CitizenApplicantPutChildren extends com.idega.data.IDOEntity
{
 public int getApplicationId();
 public int getCurrentCommuneId();
 public void initializeAttributes();
 public void setApplicationId(int p0);
 public void setCurrentCommuneID(int p0);

}
