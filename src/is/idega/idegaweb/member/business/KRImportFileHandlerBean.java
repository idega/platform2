package is.idega.idegaweb.member.business;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.transaction.UserTransaction;

import com.idega.block.importer.business.ImportFileHandler;
import com.idega.block.importer.data.ImportFile;
import com.idega.business.IBOServiceBean;
import com.idega.core.business.AddressBusiness;
import com.idega.core.data.Address;
import com.idega.core.data.AddressHome;
import com.idega.core.data.AddressType;
import com.idega.core.data.Country;
import com.idega.core.data.CountryHome;
import com.idega.core.data.PostalCode;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Gender;
import com.idega.user.data.GenderHome;
import com.idega.user.data.Group;
import com.idega.user.data.GroupHome;
import com.idega.user.data.User;
import com.idega.user.data.UserHome;
import com.idega.util.IWTimestamp;
import com.idega.util.Timer;
import com.idega.util.text.TextSoap;

/**
 * <p>Title: KRImportFileHandlerBean</p>
 * <p>Description: </p>
 * <p>Copyright (c) 2002</p>
 * <p>Company: Idega Software</p>
 * @author <a href="mailto:eiki@idega.is"> Eirikur Sveinn Hrafnsson</a>
 * @version 1.0
 */



public class KRImportFileHandlerBean extends IBOServiceBean implements KRImportFileHandler{


  private UserBusiness biz;
  private UserHome home;
  private AddressBusiness addressBiz;
  private MemberFamilyLogic relationBiz;
  private GroupHome groupHome;
  private Group rootGroup;
  private ImportFile file;
  private UserTransaction transaction;
  private UserTransaction transaction2;

  
  private ArrayList userValues;
  private ArrayList failedRecords = new ArrayList();


  private final int COLUMN_PERSONAL_ID = 0;
  private final int COLUMN_NAME = 1;  
  private final int COLUMN_ADDRESS = 2;
  private final int COLUMN_POSTAL_CODE = 3;
  private final int COLUMN_PHONE_NUMBER = 4;
  private final int COLUMN_EMAIL = 5;
  
  private Gender male;
  private Gender female;
	
	public KRImportFileHandlerBean(){}
  
  public boolean handleRecords() throws RemoteException{
    //transaction =  this.getSessionContext().getUserTransaction();
    //transaction2 =  this.getSessionContext().getUserTransaction();

    Timer clock = new Timer();
    clock.start();

    try {
      //initialize business beans and data homes
      biz = (UserBusiness) this.getServiceInstance(UserBusiness.class);
      relationBiz = (MemberFamilyLogic) this.getServiceInstance(MemberFamilyLogic.class);
      home = biz.getUserHome();
      addressBiz = (AddressBusiness) this.getServiceInstance(AddressBusiness.class);

      //if the transaction failes all the users and their relations are removed
      //transaction.begin();

      //iterate through the records and process them
      String item;

      int count = 0;
      while ( !(item=(String)file.getNextRecord()).equals("") ) {
        count++;

		
           if( ! processRecord(item) ) failedRecords.add(item);

        if( (count % 500) == 0 ){
          System.out.println("KRFileHandler processing RECORD ["+count+"] time: "+IWTimestamp.getTimestampRightNow().toString());
        }
        
        item = null;
      }
      
      printFailedRecords();

      clock.stop();
      System.out.println("Time to handleRecords: "+clock.getTime()+" ms  OR "+((int)(clock.getTime()/1000))+" s");

      // System.gc();
      //success commit changes
      //transaction.commit();


      //store family relations
      //if( importRelations){
      //  storeRelations();
      //}

      return true;
    }
    catch (Exception ex) {
     ex.printStackTrace();

     /*try {
      transaction.rollback();
     }
     catch (SystemException e) {
       e.printStackTrace();
     }*/

     return false;
    }

  }

  private boolean processRecord(String record) throws RemoteException{
    userValues = file.getValuesFromRecordString(record);
    

	boolean success = storeUserInfo();
    /**
    * family and other releation stuff
    */
//    if( importRelations ) addRelations();

    userValues = null;

    return success;
  }
  
  public void printFailedRecords(){
  	System.out.println("Import failed for these records, please fix and import again:");
  
  	Iterator iter = failedRecords.iterator();
  	while (iter.hasNext()) {
		System.out.println((String) iter.next());
	}
	
  	System.out.println("Import failed for these records, please fix and import again:");
  }

  protected boolean storeUserInfo() throws RemoteException{

    User user = null;

    //variables
    
    String PIN = getUserProperty(this.COLUMN_PERSONAL_ID);
    
    if(PIN==null) return false;
    else{
    	PIN = TextSoap.findAndCut(PIN,"-");
    	if( PIN.length()!=10 ) return false;
    }
    
    
    String name = getUserProperty(this.COLUMN_NAME);
    if(name == null ) return false;

    Gender gender = guessGenderFromName(name);
    IWTimestamp dateOfBirth = getBirthDateFromPin(PIN);

    /**
    * basic user info
    */
    try{
      //System.err.println(firstName);
      user = biz.createUserByPersonalIDIfDoesNotExist("","","",PIN, gender, dateOfBirth);
    }
    catch(Exception e){
      e.printStackTrace();
      return false;
    }

    /**
     * addresses
     */
    //main address

      String addressLine =  getUserProperty(this.COLUMN_ADDRESS);
      if( (addressLine!=null) ){
        try{

        String streetName = addressBiz.getStreetNameFromAddressString(addressLine);
        String streetNumber = addressBiz.getStreetNumberFromAddressString(addressLine);
        
        String postalCode = getUserProperty(this.COLUMN_POSTAL_CODE);
        String postalName = "Reykjavik";//temporary need to create the correct postalcode table for Iceland

        Address address = biz.getUsersMainAddress(user);
        Country iceland = ((CountryHome)getIDOHome(Country.class)).findByIsoAbbreviation("IS");
        PostalCode code = addressBiz.getPostalCodeAndCreateIfDoesNotExist(postalCode,postalName,iceland);

        boolean addAddress = false;/**@todo is this necessary?**/

        if( address == null ){
          AddressHome addressHome = addressBiz.getAddressHome();
          address = addressHome.create();
          AddressType mainAddressType = addressHome.getAddressType1();
          address.setAddressType(mainAddressType);
          addAddress = true;
        }

        address.setCountry(iceland);
        address.setPostalCode(code);
        //address.setProvince("Nacka");
        address.setCity("Reykjavik");
        address.setStreetName(streetName);
        address.setStreetNumber(streetNumber);

        address.store();

        if(addAddress){
          user.addAddress(address);
        }
        
        /*
         * Need to add a cashier which should be the admin of the club. and a categoryid
         * com.idega.block.finance.business.AccountManager
        public static Account makeNewFinanceAccount(int iUserId, String sName,String sExtra, int iCashierId,int iCategoryId)throws Exception{       
        */
        if( rootGroup!=null)
        	rootGroup.addGroup(user);

    }
     catch(Exception e){
      e.printStackTrace();
      return false;
    }

    }

    /**
     * Save the user to the database
     */
    //user.store();

    /**
    * Main group relation
    */
	//adda i retta gruppu
	
    //nackaGroup.addUser(user);

    //finished with this user
    user = null;
    return true;
  }

  public void setImportFile(ImportFile file){
    this.file = file;
  }


  private Gender guessGenderFromName(String name){
    try {
      GenderHome home = (GenderHome) this.getIDOHome(Gender.class);
      if( name.indexOf("son",name.length()-4)!=-1 ){//check if this name ends with the "son" string
        if( male == null ){
          male = home.getMaleGender();
        }
        return male;
        
      }
      else{
		if( female == null ){
          female = home.getFemaleGender();
        }
        return female;
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return null;//if something happened
    }
  }

  private IWTimestamp getBirthDateFromPin(String pin){
    //pin format = 2502785279 yyyymmddxxxx
    int dd = Integer.parseInt(pin.substring(0,2));
    int mm = Integer.parseInt(pin.substring(2,4));
    int yyyy = Integer.parseInt(pin.substring(4,6));
    
    if(pin.endsWith("9")) yyyy += 1900;
    else yyyy += 2000;
    
    IWTimestamp dob = new IWTimestamp(dd,mm,yyyy);
    return dob;
  }
  
	private String getUserProperty(int columnIndex){
		String value = null;
		
		if( userValues!=null ){
		
			try {
				value = (String)userValues.get(columnIndex);
			} catch (RuntimeException e) {
				return null;
			}
	 			//System.out.println("Index: "+columnIndex+" Value: "+value);
	 		if( file.getEmptyValueString().equals( value ) ) return null;
		 	else return value;
  		}
  		else return null;
  }

/**
 * Returns the rootGroup.
 * @return Group
 */
public Group getRootGroup() {
	return rootGroup;
}

/**
 * Sets the rootGroup.
 * @param rootGroup The rootGroup to set
 */
public void setRootGroup(Group rootGroup) {
	this.rootGroup = rootGroup;
}

  }