package is.idega.idegaweb.member.business;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.ejb.FinderException;
import javax.transaction.UserTransaction;

import com.idega.block.importer.data.ImportFile;
import com.idega.business.IBOServiceBean;
import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.EmailHome;
import com.idega.core.contact.data.Phone;
import com.idega.core.contact.data.PhoneHome;
import com.idega.core.location.business.AddressBusiness;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.AddressHome;
import com.idega.core.location.data.AddressType;
import com.idega.core.location.data.Country;
import com.idega.core.location.data.CountryHome;
import com.idega.core.location.data.PostalCode;
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
 * <p>Title: KRClubImportFileHandlerBean</p>
 * <p>Description: </p>
 * <p>Copyright (c) 2002</p>
 * <p>Company: Idega Software</p>
 * @author <a href="mailto:eiki@idega.is">Eirikur Sveinn Hrafnsson</a>
 * @version 1.0
 */



public class KRClubImportFileHandlerBean extends IBOServiceBean implements KRClubImportFileHandler{

  private UserBusiness biz;
  private UserHome home;
  private AddressBusiness addressBiz;
  private PhoneHome phoneHome;
  private EmailHome eHome;
//  private MemberFamilyLogic relationBiz;
  private GroupHome groupHome;
  private Group rootGroup;
  private ImportFile file;
  private UserTransaction transaction;
  private UserTransaction transaction2;

  private ArrayList userValues;
  private ArrayList failedRecords = new ArrayList();

//ID;FEL-NR;NAFN;KT;HEIMILI;POSTNR;SV-FEL;GR-MATI;
//klubbsflokkur;BYRJA�I;H-SIMI;V-SIMI;F-SIMI;NETFANG

  private final int COLUMN_NAME = 2;  
  private final int COLUMN_PERSONAL_ID = 3;
  private final int COLUMN_ADDRESS = 4;
  private final int COLUMN_POSTAL_CODE = 5;
  private final int COLUMN_POSTAL_CODE_NAME = 6;
  
  private final int COLUMN_PAYMENT_TYPE = 7;
    
  private final int COLUMN_MEMBER_TYPE = 8; 
  
  private final int COLUMN_STARTED = 9;
     
  private final int COLUMN_HOME_PHONE_NUMBER = 10;
  private final int COLUMN_WORK_PHONE_NUMBER = 11;
  private final int COLUMN_MOBILE_PHONE_NUMBER = 12;
  private final int COLUMN_EMAIL = 13;
  
  private Gender male;
  private Gender female;
  
  private Group A;
  private Group B;
  private Group C;
	
  public KRClubImportFileHandlerBean(){}
  
  public boolean handleRecords() throws RemoteException{
    //transaction =  this.getSessionContext().getUserTransaction();
    //transaction2 =  this.getSessionContext().getUserTransaction();

    Timer clock = new Timer();
    clock.start();

    try {
      //initialize business beans and data homes
      this.biz = (UserBusiness) this.getServiceInstance(UserBusiness.class);
      this.home = this.biz.getUserHome();
      this.addressBiz = (AddressBusiness) this.getServiceInstance(AddressBusiness.class);
      this.phoneHome = this.biz.getPhoneHome();
      this.eHome = this.biz.getEmailHome();
      
      this.A = this.biz.getGroupBusiness().getGroupByGroupID(466);//hacks
      this.B = this.biz.getGroupBusiness().getGroupByGroupID(467);
      this.C = this.biz.getGroupBusiness().getGroupByGroupID(468);

      //if the transaction failes all the users and their relations are removed
      //transaction.begin();

      //iterate through the records and process them
      String item;

      int count = 0;
      while ( !(item=(String)this.file.getNextRecord()).equals("") ) {
        count++;

		
        if( ! processRecord(item) ) {
			this.failedRecords.add(item);
		}

        if( (count % 100) == 0 ){
          System.out.println("KRClubFileHandler processing RECORD ["+count+"] time: "+IWTimestamp.getTimestampRightNow().toString());
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
    this.userValues = this.file.getValuesFromRecordString(record);
    
	boolean success = storeUserInfo();

    this.userValues = null;

    return success;
  }
  
  public void printFailedRecords(){
  	System.out.println("Import failed for these records, please fix and import again:");
  
  	Iterator iter = this.failedRecords.iterator();
  	while (iter.hasNext()) {
		System.out.println((String) iter.next());
	}
	
  	System.out.println("Please fix and import again");
  }

  protected boolean storeUserInfo() throws RemoteException{

    User user = null;

    //variables
    
    String PIN = getUserProperty(this.COLUMN_PERSONAL_ID);
    
    if(PIN==null) {
		return false;
	}
	else{
    	PIN = TextSoap.findAndCut(PIN,"-");
    	if( PIN.length()!=10 ) {
			return false;
		}
    }
    
    
    String name = getUserProperty(this.COLUMN_NAME);
    if(name == null ) {
		return false;
	}

    Gender gender = guessGenderFromName(name);
    IWTimestamp dateOfBirth = getBirthDateFromPin(PIN);

    /**
    * basic user info
    */
    try{
      //System.err.println(firstName);
      user = this.biz.createUserByPersonalIDIfDoesNotExist(name,PIN, gender, dateOfBirth);
    }
    catch(Exception e){
      e.printStackTrace();
      return false;
    }


try{
    /**
     * addresses
     */
    //main address

      String addressLine =  getUserProperty(this.COLUMN_ADDRESS);
      if( (addressLine!=null) ){
   

        String streetName = this.addressBiz.getStreetNameFromAddressString(addressLine);
        String streetNumber = this.addressBiz.getStreetNumberFromAddressString(addressLine);
        
        
        String postalCode = getUserProperty(this.COLUMN_POSTAL_CODE);
        String postalName = getUserProperty(this.COLUMN_POSTAL_CODE_NAME);

        Address address = this.biz.getUsersMainAddress(user);
        Country iceland = ((CountryHome)getIDOHome(Country.class)).findByIsoAbbreviation("IS");
        
        PostalCode code = null;
        try{
        	code = this.addressBiz.getPostalCodeHome().findByPostalCodeAndCountryId(postalCode,((Integer)iceland.getPrimaryKey()).intValue());
        }
        catch(FinderException ex){
        	code = this.addressBiz.getPostalCodeAndCreateIfDoesNotExist(postalCode,postalName,iceland);
        }
        
        boolean addAddress = false;/**@todo is this necessary?**/

        if( address == null ){
          AddressHome addressHome = this.addressBiz.getAddressHome();
          address = addressHome.create();
          AddressType mainAddressType = addressHome.getAddressType1();
          address.setAddressType(mainAddressType);
          addAddress = true;
        }

        address.setCountry(iceland);
        if(code!=null) {
			address.setPostalCode(code);
		}
        
        //address.setCity("Reykjavik");
        address.setStreetName(streetName);
        if( streetNumber!=null ) {
			address.setStreetNumber(streetNumber);
		}

        address.store();

        if(addAddress){
          user.addAddress(address);
        }
        
       


   }//if ends

        
        //phone
		//@todo look for the phone first to avoid duplicated
		String phone = getUserProperty(this.COLUMN_HOME_PHONE_NUMBER);
		String work = getUserProperty(this.COLUMN_WORK_PHONE_NUMBER);
		String mobile = getUserProperty(this.COLUMN_MOBILE_PHONE_NUMBER);
		
		
		if( phone!=null || work!=null || mobile!=null){
			Collection phones = user.getPhones();
						
			boolean addPhone1 = true;
			boolean addPhone2 = true;
			boolean addPhone3 = true;
					
			Iterator iter = phones.iterator();
			//@todo do this with an equals method in a comparator?
			while (iter.hasNext()) {
				Phone tempPhone = (Phone) iter.next();
				String temp = tempPhone.getNumber();
				
				if( temp.equals(phone) ) {
					addPhone1 = false;
				}	
						
				if( temp.equals(work) ) {
					addPhone2 = false;
				}	
				
				if( temp.equals(mobile) ) {
					addPhone3 = false;
				}
				
			}
  
			if( addPhone1 && phone != null){
				Phone uPhone = this.phoneHome.create();
				uPhone.setNumber(phone);
				uPhone.setPhoneTypeId(1);//weeeeee...svindl
				uPhone.store();
				user.addPhone(uPhone);
			}
			
			if( addPhone2 && work != null){
				Phone uPhone = this.phoneHome.create();
				uPhone.setNumber(work);
				uPhone.setPhoneTypeId(2);//weeeeee...svindl
				uPhone.store();
				user.addPhone(uPhone);
			}
			
			if( addPhone3 && mobile != null){
				Phone uPhone = this.phoneHome.create();
				uPhone.setNumber(mobile);
				uPhone.setPhoneTypeId(3);//weeeeee...svindl
				uPhone.store();
				user.addPhone(uPhone);
			}
			
			
			
		}
        
        
        
        
        //email
        String email = getUserProperty(this.COLUMN_EMAIL);
        
		//both this and phones is very much a stupid hack in my part. I should have used findMethods etc. or make a useful getOrCreateIfNonExisting...bleh! -Eiki
		if( email!=null){
			Collection emails = user.getEmails();		
			boolean addEmail1 = true;
	
					
			Iterator iter = emails.iterator();
			//@todo do this with an equals method in a comparator?
			while (iter.hasNext()) {
				Email mail = (Email) iter.next();
				String tempAddress = mail.getEmailAddress();
				
				if( tempAddress.equals(email) ) {
					addEmail1 = false;
				}	
					
				
			}
			
			if( addEmail1 && email != null){
				Email uEmail = this.eHome.create();
				uEmail.setEmailAddress(email);
				uEmail.store();
				user.addEmail(uEmail);
			}
			

			
		}
        
        //adda i rettan flokk herna
        String type = getUserProperty(this.COLUMN_MEMBER_TYPE);
        if( type!=null ){//A and AC goto A
        	if("A".equals(type) || "AC".equals(type) ){
        		this.A.addGroup(user);
        	}
        	else {//B membership
        		this.B.addGroup(user);
        	}	
        	
        	
        }    
        
        
    }
     catch(Exception e){
      e.printStackTrace();
      return false;
    }

    /**
     * Save the user to the database
     */
    //user.store();


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
        if( this.male == null ){
          this.male = home.getMaleGender();
        }
        return this.male;
        
      }
      else{
		if( this.female == null ){
          this.female = home.getFemaleGender();
        }
        return this.female;
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
    
    if(pin.endsWith("9")) {
		yyyy += 1900;
	}
	else {
		yyyy += 2000;
	}
    
    IWTimestamp dob = new IWTimestamp(dd,mm,yyyy);
    return dob;
  }
  
	private String getUserProperty(int columnIndex){
		String value = null;
		
		if( this.userValues!=null ){
		
			try {
				value = (String)this.userValues.get(columnIndex);
			} catch (RuntimeException e) {
				return null;
			}
	 			//System.out.println("Index: "+columnIndex+" Value: "+value);
	 		if( this.file.getEmptyValueString().equals( value ) ) {
				return null;
			}
			else {
				return value;
			}
  		}
		else {
			return null;
		}
  }

/**
 * Returns the rootGroup.
 * @return Group
 */
public Group getRootGroup() {
	return this.rootGroup;
}

/**
 * Sets the rootGroup.
 * @param rootGroup The rootGroup to set
 */
public void setRootGroup(Group rootGroup) {
	this.rootGroup = rootGroup;
}

/**
 * @see com.idega.block.importer.business.ImportFileHandler#getFailedRecords()
 */
public List getFailedRecords(){
	return this.failedRecords;	
}

  }