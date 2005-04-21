package se.idega.idegaweb.commune.account.citizen.data;

import se.idega.idegaweb.commune.account.data.AccountApplication;

import com.idega.block.process.data.Case;
import com.idega.data.IDOEntity;

public interface CitizenAccount extends IDOEntity, Case, AccountApplication {
	final static String PUT_CHILDREN_IN_NACKA_SCHOOL_KEY = "caa_put_children_in_nacka";
	final static String PUT_CHILDREN_IN_NACKA_CHILDCARE_KEY = "caa_put_children_in_nacka_childcare";
	final static String MOVING_TO_NACKA_KEY = "caa_moving_to_nacka";
	final static String MAKE_KOMVUX_APPLICATION_KEY = "caa_make_komvux_application";

    String getCaseCodeDescription ();
    String getCaseCodeKey ();

    String getApplicantName ();
    String getSsn ();
    String getEmail ();
    String getPhoneHome ();
    String getPhoneWork ();
    String getCareOf ();
    String getStreet ();
    String getZipCode ();
    String getCity ();
    String getCivilStatus ();
    boolean hasCohabitant ();
    int getChildrenCount ();
    String getApplicationReason ();

    void setApplicantName (String name);
    void setSsn (String ssn);
    void setEmail (String email);
    void setPhoneHome (String phoneHome);
    void setPhoneWork (String phoneWork);
    void setCareOf (String careOf);
    void setStreet (String street);
    void setZipCode (String zipCode);
    void setCity (String city);
    void setCivilStatus (String civilStatus);
    void setHasCohabitant (boolean hasCohabitant);
    void setChildrenCount (int childrenCount);
    void setApplicationReason (String applicationReason);
}
