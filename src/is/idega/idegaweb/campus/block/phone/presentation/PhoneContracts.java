package is.idega.idegaweb.campus.block.phone.presentation;

import is.idega.idegaweb.campus.block.allocation.data.Contract;
import is.idega.idegaweb.campus.block.phone.business.PhoneFinder;
import is.idega.idegaweb.campus.presentation.CampusBlock;

import java.rmi.RemoteException;
import java.text.DateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.ejb.FinderException;

import com.idega.block.building.data.ApartmentView;
import com.idega.block.building.data.ApartmentViewHome;
import com.idega.business.IBOLookup;

import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;

import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.util.TextFormat;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.User;



/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved
 * Company:      idega
  *@author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.1
 */
public class PhoneContracts extends CampusBlock {

  private DateFormat df;
  private UserBusiness ub;
  private TextFormat tf;

  protected boolean isAdmin = false;

  public String getLocalizedNameKey(){
    return "phone_contracts";
  }

  public String getLocalizedNameValue(){
    return "Phone Contracts";
  }

  protected void control(IWContext iwc)throws RemoteException{
    //debugParameters(iwc);
    if(isAdmin){
       add(getSeachForm(iwc));
       if (iwc.isParameterSet("numbers")){
       		add(getPhoneTable(iwc,parseNumbers(iwc.getParameter("numbers"))));
       }
    }
    else
      add(getNoAccessObject(iwc));
    //add(String.valueOf(iSubjectId));
  }

  public String[] parseNumbers(String numbers){
  	StringTokenizer tokener = new StringTokenizer(numbers,",;: ");
  	String[] nums = new String[tokener.countTokens()];
   	for (int i = 0; i < nums.length; i++) {
		nums[i] = tokener.nextToken();
	}
	return nums;
  }

  public PresentationObject makeLinkTable(int menuNr){
    Table LinkTable = new Table(6,1);

    return LinkTable;
  }
  
  private PresentationObject getSeachForm(IWContext iwc){
  	Form F = new Form();
  	Table T = new Table();
  	TextInput numberInput = new TextInput("numbers");
  	if(iwc.isParameterSet("numbers"))
  		numberInput.setContent(iwc.getParameter("numbers"));
  	SubmitButton search = new SubmitButton("Search");
  	T.add(numberInput,1,1);
  	T.add(search,2,1);
  	F.add(T);
  	return F;
  }

  private PresentationObject getPhoneTable(IWContext iwc,String[] phoneNumbers)throws RemoteException{
  	Table T = new Table();
  	int row = 1;
  	if(phoneNumbers!=null){
  		ApartmentViewHome avh =(ApartmentViewHome)IDOLookup.getHome(ApartmentView.class);
  		for (int i = 0; i < phoneNumbers.length; i++) {
			List contracts = PhoneFinder.listOfPhoneContracts(phoneNumbers[i]);
			
		  	if(contracts!=null){
		  		Contract contract;
		  		User user;
		  		Iterator iter = contracts.iterator();
		  		T.add(tf.format( phoneNumbers[i],tf.HEADER),1,row++);
		  		while(iter.hasNext()){
		  			try {
						contract = (Contract) iter.next();
						user = (User) ub.getUser(contract.getUserId().intValue());
						T.add(tf.format(user.getName()),2,row);
						T.add(tf.format(df.format(contract.getValidFrom())+" - "+df.format(contract.getValidTo())),3,row);
						T.add(tf.format(avh.findByPrimaryKey(contract.getApartmentId()).getApartmentString(" ")),4,row);
						row++;
					}
					catch (FinderException e) {
						e.printStackTrace();
					}
		  		}
		  	
		  	}
		  row++;row++;
		}
  	}
  	return T;
  }

  public void main(IWContext iwc)throws RemoteException{
  
    isAdmin = iwc.hasEditPermission(this);
    df = DateFormat.getDateInstance(DateFormat.SHORT,iwc.getCurrentLocale());
    ub = (UserBusiness)IBOLookup.getServiceInstance(iwc,UserBusiness.class);
    tf = TextFormat.getInstance();
    control(iwc);
  }


}
