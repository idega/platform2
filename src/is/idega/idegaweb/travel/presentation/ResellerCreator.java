package is.idega.idegaweb.travel.presentation;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.ejb.FinderException;
import com.idega.block.trade.stockroom.data.Reseller;
import com.idega.block.trade.stockroom.data.ResellerHome;
import com.idega.block.trade.stockroom.data.Supplier;
import com.idega.block.trade.stockroom.data.SupplierBMPBean;
import com.idega.block.trade.stockroom.data.SupplierHome;
import com.idega.core.accesscontrol.business.LoginDBHandler;
import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.Phone;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.PostalCode;
import com.idega.core.location.data.PostalCodeHome;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.BackButton;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.PasswordInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;

/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class ResellerCreator extends TravelManager {

  private IWResourceBundle iwrb;
  private String parameterSaveNewReseller = "contractSaveNewReseller";
  private String parameterUpdateReseller = "contractUpdateReseller";
  private String parameterNewReseller = "contractNewReseller";
  private String parameterEditReseller = "contractEditReseller";
  private String parameterDeleteReseller = "contractDeleteReseller";
  private String parameterDelYes = "contractDelYes";
  private String parameterSelectReseller = "contractSelectReseller";
  private String parameterResellerId = "contractResellerId";
  private String parameterCheckBox = "parameterCheckBox_";
  private String parameterSupplierCheckBox = "par_supp_checkBox_";
  private String parameterUpdateSuppliers = "par_upd_sup";
  private String parameterAddSuppliers = "par_add_sup";
  private String sAction = "res_cr_action";

  private String tableWidth = "75%";

  public ResellerCreator() {
  }

  public void main(IWContext iwc) throws Exception{
    super.main(iwc);
    init(iwc);

    String action = iwc.getParameter(this.sAction);
    if (super.isSupplierManager()) {
      if (action == null) {
        selectReseller(iwc);
//        mainMenu(iwc);
      }else if (action.equals(this.parameterNewReseller)) {
        resellerCreation(iwc, -1);
      }else if (action.equals(this.parameterSelectReseller)) {
        selectReseller(iwc);
      }else if (action.equals(this.parameterEditReseller)) {
        String sResellerId = iwc.getParameter(this.parameterResellerId);
        resellerCreation(iwc, Integer.parseInt(sResellerId));
      }else if (action.equals(this.parameterSaveNewReseller)) {
        int resellerId = saveReseller(iwc,-1);
        if (resellerId != -1) {
          resellerCreation(iwc, resellerId);
        }
      }else if (action.equals(this.parameterUpdateReseller)) {
        String sResellerId = iwc.getParameter(this.parameterResellerId);
        int resellerId = saveReseller(iwc,Integer.parseInt(sResellerId));
        if (resellerId != -1) {
          resellerCreation(iwc, resellerId);
        }
      }else if (action.equals(this.parameterDeleteReseller)) {
        comfirmDelete(iwc);
      }else if (action.equals(this.parameterDelYes)) {
        deleteReseller(iwc);
        selectReseller(iwc);
      }else if (action.equals(this.parameterUpdateSuppliers)) {
        String sResellerId = iwc.getParameter(this.parameterResellerId);
        updateSuppliers(iwc);
        addSuppliers(iwc, Integer.parseInt(sResellerId));
      }else if (action.equals(this.parameterAddSuppliers)) {
        String sResellerId = iwc.getParameter(this.parameterResellerId);
        addSuppliers(iwc, Integer.parseInt(sResellerId));
      }
    }else {
      iwrb.getLocalizedString("travel.log_in","Please log in.");
    }
  }

  private void init(IWContext iwc) throws RemoteException {
    iwrb = super.getResourceBundle();
  }
/*
  private void mainMenu(IWContext iwc) {
    Table table = new Table();

    Link newReseller = new Link(iwrb.getLocalizedImageButton("travel.new_reseller","New Reseller"));
      newReseller.addParameter(this.sAction, this.parameterNewReseller);

    Link editReseller = new Link(iwrb.getLocalizedImageButton("travel.edit_reseller","Edit Reseller"));
      editReseller.addParameter(this.sAction, this.parameterSelectReseller);

      table.add(newReseller,1, 1);
      table.add(editReseller,1, 1);


    add(Text.BREAK);
    add(table);

//      table.add(iwrb.getLocalizedImageButton("travel.new_reseller","New Reseller"),1, 1);
  }*/

  private void resellerCreation(IWContext iwc, int resellerId) throws SQLException, RemoteException, FinderException{
      Form form = new Form();
      Table table = new Table();
        form.add(table);
        table.setColor(super.WHITE);
        table.setCellspacing(1);
        table.setAlignment("center");
        table.setColumnAlignment(1,"right");
        table.setBorder(0);
        table.setWidth(this.tableWidth);

      boolean isUpdate = false;
      if (resellerId != -1) {
        isUpdate = true;
      }
      int row = 0;

      Text newSupplierText = (Text) theBoldText.clone();
        if (isUpdate) newSupplierText.setText(iwrb.getLocalizedString("travel.update_reseller_information","Update reseller information"));
        else newSupplierText.setText(iwrb.getLocalizedString("travel.new_reseller","New Reseller"));

      Text nameText = (Text) theBoldText.clone();
          nameText.setText(iwrb.getLocalizedString("travel.name","Name"));
          nameText.addToText(":");
          nameText.setFontColor(super.BLACK);

      Text descText = (Text) theBoldText.clone();
          descText.setText(iwrb.getLocalizedString("travel.Description","Description"));
          descText.addToText(":");
          descText.setFontColor(super.BLACK);

      Text addressText = (Text) theBoldText.clone();
          addressText.setText(iwrb.getLocalizedString("travel.address_long","Address"));
          addressText.addToText(":");
          addressText.setFontColor(super.BLACK);

			Text postalText = (Text) theBoldText.clone();
			postalText.setFontColor(super.BLACK);
			postalText.setText(iwrb.getLocalizedString("travel.postal_code_long","Postal code"));
			postalText.addToText(":");
	
      Text phoneText = (Text) theBoldText.clone();
          phoneText.setText(iwrb.getLocalizedString("travel.telephone_number_lg","Telephone number"));
          phoneText.addToText(":");
          phoneText.setFontColor(super.BLACK);

      Text faxText = (Text) theBoldText.clone();
          faxText.setText(iwrb.getLocalizedString("travel.fax","Fax number"));
          faxText.addToText(":");
          faxText.setFontColor(super.BLACK);

      Text emailText = (Text) theBoldText.clone();
          emailText.setText(iwrb.getLocalizedString("travel.email_lg","E-mail"));
          emailText.addToText(":");
          emailText.setFontColor(super.BLACK);

      Text loginText = (Text) theBoldText.clone();
          loginText.setText(iwrb.getLocalizedString("travel.user_name","User name"));
          loginText.addToText(":");
          loginText.setFontColor(super.BLACK);

      Text passwordText = (Text) theBoldText.clone();
          passwordText.setText(iwrb.getLocalizedString("travel.password","Password"));
          passwordText.addToText(":");
          passwordText.setFontColor(super.BLACK);

      int inputSize = 40;

      TextInput name = new TextInput("reseller_name");
        name.setSize(inputSize);
      TextArea description = new TextArea("reseller_description");
      description.setWidth("260");
      description.setHeight("80");
    	TextInput address = new TextInput("reseller_address");
        address.setSize(inputSize);
		  DropdownMenu postalCode = new DropdownMenu("reseller_postal_code");
			PostalCodeHome pch = (PostalCodeHome) IDOLookup.getHome(PostalCode.class);
			Collection allPostalCodes = pch.findAllOrdererByCode();
			Iterator iter = allPostalCodes.iterator();
			PostalCode pc;
			while (iter.hasNext()) {
			  pc = (PostalCode) iter.next();
			  postalCode.addMenuElement(pc.getPrimaryKey().toString(), pc.getPostalCode()+" "+pc.getName());
			}
      TextInput phone = new TextInput("reseller_phone");
        phone.setSize(inputSize);
      TextInput fax = new TextInput("reseller_fax");
        fax.setSize(inputSize);
      TextInput email = new TextInput("reseller_email");
        email.setSize(inputSize);
      TextInput userName = new TextInput("reseller_user_name");
        userName.setAsNotEmpty(iwrb.getLocalizedString("travel.a_username_must_be_selected","Verður að velja notendanafn"));
      PasswordInput passOne = new PasswordInput("reseller_password_one");
      PasswordInput passTwo = new PasswordInput("reseller_password_two");


      SubmitButton submit = new SubmitButton(iwrb.getImage("buttons/save.gif"),this.sAction,this.parameterSaveNewReseller);
      SubmitButton delete = new SubmitButton(iwrb.getImage("buttons/delete.gif"),this.sAction,this.parameterDeleteReseller);
      SubmitButton back = new SubmitButton(iwrb.getImage("buttons/back.gif"), this.sAction, this.parameterSelectReseller);

      if (resellerId != -1) {
        table.add(new HiddenInput(this.parameterResellerId,Integer.toString(resellerId)));

        Reseller reseller = ((com.idega.block.trade.stockroom.data.ResellerHome)com.idega.data.IDOLookup.getHomeLegacy(Reseller.class)).findByPrimaryKeyLegacy(resellerId);
          name.setContent(reseller.getName());
          description.setContent(reseller.getDescription());

          Address addr = reseller.getAddress();
          if (addr != null) {
            String namer = addr.getStreetName();
            String number = addr.getStreetNumber();
            if (number == null) {
                address.setContent(namer);
            }else {
                address.setContent(namer+" "+number);
            }
						int iPostalCodeId = addr.getPostalCodeID();
						if (iPostalCodeId != -1){
							postalCode.setSelectedElement(iPostalCodeId);
						}
          }

          List phones = reseller.getHomePhone();
          if (phones != null) {
            if (phones.size() > 0) {
              Phone phone1 = (Phone) phones.get(0);
              if (phone1 != null && phone1.getNumber() != null )
              phone.setContent(phone1.getNumber());
            }
          }

          phones = reseller.getFaxPhone();
          if (phones != null) {
            if (phones.size() > 0) {
              Phone phone2 = (Phone) phones.get(0);
              if (phone2 != null && phone2.getNumber() != null )
              fax.setContent(phone2.getNumber());
            }
          }

          Email eEmail = reseller.getEmail();
          if (eEmail != null) {
            email.setContent(eEmail.getEmailAddress());
          }
          submit = new SubmitButton(iwrb.getImage("buttons/update.gif"),this.sAction,this.parameterUpdateReseller);
      }


      ++row;
      table.mergeCells(1,row,2,row);
      table.setAlignment(1,row,"center");
      table.add(newSupplierText,1,row);
      table.setRowColor(row,super.backgroundColor);

      ++row;
      table.add(nameText,1,row);
      table.add(name,2,row);
      table.setRowColor(row,super.GRAY);

      ++row;
      table.add(descText,1,row);
      table.setVerticalAlignment(1,row,"top");
      table.add(description,2,row);
      table.setRowColor(row,super.GRAY);

      ++row;
      table.add(addressText,1,row);
      table.add(address,2,row);
      table.setRowColor(row,super.GRAY);

			++row;
			table.add(postalText,1,row);
			table.add(postalCode,2,row);
			table.setRowColor(row,super.GRAY);

      ++row;
      table.add(phoneText,1,row);
      table.add(phone,2,row);
      table.setRowColor(row,super.GRAY);

      ++row;
      table.add(faxText,1,row);
      table.add(fax,2,row);
      table.setRowColor(row,super.GRAY);

      ++row;
      table.add(emailText,1,row);
      table.add(email,2,row);
      table.setRowColor(row,super.GRAY);

      if (!isUpdate) {
        ++row;
        table.add(loginText,1,row);
        table.add(userName,2,row);
        table.setRowColor(row,super.GRAY);

        ++row;
        table.add(passwordText,1,row);
        table.setVerticalAlignment(1,row,"top");
        table.add(passOne,2,row);
        table.addBreak(2,row);
        table.add(passTwo,2,row);
        table.setRowColor(row,super.GRAY);
      }


      table.setColumnAlignment(2,"left");
      ++row;
      table.setAlignment(1,row,"left");
      table.add(back,1,row);
      table.setAlignment(2,row,"right");
      table.add(delete,2,row);
      table.add(Text.NON_BREAKING_SPACE, 2, row);
      table.add(submit,2,row);
      table.setRowColor(row,super.GRAY);



      if (resellerId != -1) {
        form.add(Text.getBreak());
        Table list = getSupplierList(iwc, resellerId);
//          form.addParameter(this.parameterResellerId, resellerId);
				//++row;
				//table.mergeCells(1, row, 2, row);
        form.add(list);
      }

      add(Text.getBreak());
      add(form);

  }

  private int saveReseller(IWContext iwc) {
    return saveReseller(iwc, -1);
  }


  private int saveReseller(IWContext iwc, int resellerId)  {
      add(Text.getBreak());
      int returner = -1;

      javax.transaction.TransactionManager tm = com.idega.transaction.IdegaTransactionManager.getInstance();

      try {
          tm.begin();
          String name = iwc.getParameter("reseller_name");
          String description = iwc.getParameter("reseltler_description");
          String address = iwc.getParameter("reseller_address");
					String postalCode = iwc.getParameter("reseller_postal_code");
          String phone = iwc.getParameter("reseller_phone");
          String fax = iwc.getParameter("reseller_fax");
          String email = iwc.getParameter("reseller_email");

          String userName = iwc.getParameter("reseller_user_name");
          String passOne = iwc.getParameter("reseller_password_one");
          String passTwo = iwc.getParameter("reseller_password_two");
//                  tm.begin();

					int iPostalCode = -1;
					try {
						if (postalCode != null){
							iPostalCode = Integer.parseInt(postalCode);
						}
					}catch (NumberFormatException e) {
					}

          boolean isUpdate = false;
          if (resellerId != -1) isUpdate = true;


          if (isUpdate) {
              Vector phoneIDS = new Vector();
              Reseller reseller = ((com.idega.block.trade.stockroom.data.ResellerHome)com.idega.data.IDOLookup.getHomeLegacy(Reseller.class)).findByPrimaryKeyLegacy(resellerId);

              Phone ph;
              List phones = reseller.getPhones(com.idega.core.contact.data.PhoneBMPBean.getHomeNumberID());
              if (phones != null) {
                if (phones.size() > 0) {
                  for (int i = 0; i < phones.size(); i++) {
                    ph = (Phone) phones.get(i);
                      ph.setNumber(phone);
                    ph.update();
                    phoneIDS.add(new Integer(ph.getID()));
                  }
                }else {
                  ph = ((com.idega.core.contact.data.PhoneHome)com.idega.data.IDOLookup.getHomeLegacy(Phone.class)).createLegacy();
                    ph.setNumber(phone);
                    ph.setPhoneTypeId(com.idega.core.contact.data.PhoneBMPBean.getHomeNumberID());
                  ph.insert();
                  phoneIDS.add(new Integer(ph.getID()));
                }
              }

              phones = reseller.getPhones(com.idega.core.contact.data.PhoneBMPBean.getFaxNumberID());
              if (phones != null) {
                if (phones.size() > 0 ) {
                  for (int i = 0; i < phones.size(); i++) {
                    ph = (Phone) phones.get(i);
                      ph.setNumber(fax);
                    ph.update();
                    phoneIDS.add(new Integer(ph.getID()));
                  }
                }else {
                  ph = ((com.idega.core.contact.data.PhoneHome)com.idega.data.IDOLookup.getHomeLegacy(Phone.class)).createLegacy();
                    ph.setNumber(fax);
                    ph.setPhoneTypeId(com.idega.core.contact.data.PhoneBMPBean.getFaxNumberID());
                  ph.insert();
                  phoneIDS.add(new Integer(ph.getID()));
                }
              }

              int[] phoneIds = new int[phoneIDS.size()];
              for (int i = 0; i < phoneIDS.size(); i++) {
                  phoneIds[i] = ((Integer) phoneIDS.get(i)).intValue() ;
              }


              Address addr = reseller.getAddress();
                addr.setStreetName(address);
								if (iPostalCode != -1) {
									addr.setPostalCodeID(iPostalCode);
								}
              addr.update();

              int[] addressIds = new int[1];
              addressIds[0] = addr.getID();


              Email eml = reseller.getEmail();
                eml.setEmailAddress(email);
              eml.update();

              int[] emailIds = new int[1];
              emailIds[0] = eml.getID();

              reseller = getResellerManager(iwc).updateReseller(resellerId,  name, description, addressIds, phoneIds, emailIds);


              add(iwrb.getLocalizedString("travel.information_updated","Information updated"));
              returner = reseller.getID();
   //           resellerCreation(resellerId);

          }else {
            if (passOne.equals(passTwo) && !LoginDBHandler.isLoginInUse(userName)) {

                Vector phoneIDS = new Vector();
                if (phone.length() > 0) {
                  Phone phonePhone = ((com.idega.core.contact.data.PhoneHome)com.idega.data.IDOLookup.getHomeLegacy(Phone.class)).createLegacy();
                    phonePhone.setNumber(phone);
                    phonePhone.setPhoneTypeId(com.idega.core.contact.data.PhoneBMPBean.getHomeNumberID());
                  phonePhone.insert();
                  phoneIDS.add(new Integer(phonePhone.getID()));
                }
                if (fax.length() > 0) {
                  Phone faxPhone = ((com.idega.core.contact.data.PhoneHome)com.idega.data.IDOLookup.getHomeLegacy(Phone.class)).createLegacy();
                    faxPhone.setNumber(fax);
                    faxPhone.setPhoneTypeId(com.idega.core.contact.data.PhoneBMPBean.getFaxNumberID());
                  faxPhone.insert();
                  phoneIDS.add(new Integer(faxPhone.getID()));
                }


                int[] phoneIds = new int[phoneIDS.size()];
                for (int i = 0; i < phoneIDS.size(); i++) {
                    phoneIds[i] = ((Integer) phoneIDS.get(i)).intValue() ;
                }

                int[] addressIds = new int[1];
                Address addressAddress = ((com.idega.core.location.data.AddressHome)com.idega.data.IDOLookup.getHomeLegacy(Address.class)).createLegacy();
                    addressAddress.setStreetName(address);
										if (iPostalCode != -1) {
											addressAddress.setPostalCodeID(iPostalCode);
										}
                    addressAddress.insert();
                addressIds[0] = addressAddress.getID();

                int[] emailIds = new int[1];
                Email eEmail = ((com.idega.core.contact.data.EmailHome)com.idega.data.IDOLookup.getHomeLegacy(Email.class)).createLegacy();
                  eEmail.setEmailAddress(email);
                  eEmail.insert();
                emailIds[0] = eEmail.getID();

                Reseller tempReseller = getResellerManager(iwc).createReseller(null, name, userName, passOne, description, addressIds, phoneIds, emailIds);
                tempReseller.setSupplierManager(getSupplierManager());
                tempReseller.store();
                /*if (supplier != null) {
                  tempReseller.addTo(supplier);
                }*/

                //add(iwrb.getLocalizedString("travel.reseller_created","Reseller was created"));
                //resellers = getResellers();
                //this.selectReseller(iwc);
                returner = tempReseller.getID();
            }else {
              if (LoginDBHandler.isLoginInUse(userName)) {
                add(iwrb.getLocalizedString("username_in_use","Username in use"));
                add(Text.BREAK);
              }
              if (!passOne.equals(passTwo)) {
                add(iwrb.getLocalizedString("passwords_not_the_same","Passwords not the same"));
                add(Text.BREAK);
              }
              add(Text.BREAK);
              add(new BackButton(iwrb.getImage("buttons/back.gif")));
            }
          }
        tm.commit();
        return returner;
      }
      catch (Exception sql) {
          add(iwrb.getLocalizedString("travel.reseller_not_created","Reseller was not created"));
        sql.printStackTrace(System.err);
        try {
          tm.rollback();
        }catch (javax.transaction.SystemException se) {
          se.printStackTrace(System.err);
        }

      }
    return returner;
  }

  private void comfirmDelete(IWContext iwc) throws SQLException, RemoteException{
    String resellerId = iwc.getParameter(this.parameterResellerId);
    if (resellerId != null) {
      ResellerHome rHome = (ResellerHome) IDOLookup.getHomeLegacy(Reseller.class);
      Reseller res = rHome.findByPrimaryKeyLegacy(Integer.parseInt(resellerId));

      Text areSure = getText(iwrb.getLocalizedString("travel.delete_reseller_question","Are you sure you want to delete this reseller"));
        areSure.addToText(" : ");
        areSure.setFontColor(super.WHITE);


      Form form = new Form();
      Table table = getTable();
      table.setWidth(this.tableWidth);
      form.add(table);
      table.add(areSure, 1, 1);
      table.add(getHeaderText(res.getName()+" ? "), 1, 1);
      table.mergeCells(1, 1, 2, 1);

      table.mergeCells(1, 2, 2, 2);
      Supplier[] supps = getResellerManager(iwc).getSuppliers(res.getID(), SupplierBMPBean.getColumnNameName());
      if (supps.length == 0) {
        table.add(getText(iwrb.getLocalizedString("travel.reseller_not_connected","This reseller is not connected to any suppliers")+"."), 1, 2);
      }else {
        table.add(getText(iwrb.getLocalizedString("travel.reseller_connected_to_following","This reseller is connected to the following suppliers")+" :"), 1, 2);
        for (int i = 0; i < supps.length; i++) {
          table.add(Text.BREAK+Text.NON_BREAKING_SPACE+Text.NON_BREAKING_SPACE+getText(supps[i].getName()),1, 2);
        }
      }

      table.setAlignment(2, 3, "right");
      table.add(new SubmitButton(iwrb.getImage("buttons/back.gif"), this.sAction, this.parameterEditReseller), 1, 3);

      table.add(new SubmitButton(iwrb.getImage("buttons/yes.gif"), this.sAction, this.parameterDelYes), 2, 3);
      table.add(new HiddenInput(this.parameterResellerId, resellerId), 2, 3);
      table.setRowColor(1, super.backgroundColor);
      table.setRowColor(2, super.GRAY);
      table.setRowColor(3, super.GRAY);

      add(Text.BREAK);
      add(form);
    }
  }

  private void deleteReseller(IWContext iwc) throws SQLException {
    String resellerId = iwc.getParameter(this.parameterResellerId);
    if (resellerId != null) {
      ResellerHome rHome = (ResellerHome) IDOLookup.getHomeLegacy(Reseller.class);
      Reseller res = rHome.findByPrimaryKeyLegacy(Integer.parseInt(resellerId));
      res.delete();
    }
  }

  private void selectReseller(IWContext iwc) throws RemoteException, FinderException {
    ResellerHome rHome = (ResellerHome) IDOLookup.getHomeLegacy(Reseller.class);
    Collection coll = rHome.findAllBySupplierManager(super.getSupplierManager());
    //Reseller[] resellers = (Reseller[]) rHome.createLegacy().findAll("select * from "+ResellerBMPBean.getResellerTableName()+" where "+ResellerBMPBean.getColumnNameIsValid()+" = 'Y' order by "+ResellerBMPBean.getColumnNameName());

    Table table = super.getTable();
    table.setWidth(this.tableWidth);
    int row = 1;
    table.add(super.getHeaderText(iwrb.getLocalizedString("travel.name","Name")), 1, row);
    table.add(Text.NON_BREAKING_SPACE, 2, row);
    table.setRowColor(row, super.backgroundColor);


    if (coll != null) {
	    Link editLink;
	    Iterator iter = coll.iterator();
	    Reseller reseller;
	    while (iter.hasNext()) {
	    //for (int i = 0; i < resellers.length; i++) {
	    	reseller = (Reseller) iter.next();
	      ++row;
	      table.setRowColor(row, super.GRAY);
	      table.add(super.getText(reseller.getName()), 1, row);
	
	      editLink = new Link(iwrb.getLocalizedImageButton("travel.more","More"));
	        editLink.addParameter(this.sAction, this.parameterEditReseller);
	        editLink.addParameter(this.parameterResellerId, reseller.getID());
	
	      table.add(editLink, 2, row);
	    }
    }

    Link newReseller = new Link(iwrb.getLocalizedImageButton("travel.new_reseller","New Reseller"));
      newReseller.addParameter(this.sAction, this.parameterNewReseller);
    ++row;
    table.add(newReseller, 1, row);
    table.setRowColor(row, super.backgroundColor);
    table.setWidth(2, "2");

    add(Text.BREAK);
    add(table);

  }

  private void updateSuppliers(IWContext iwc) throws SQLException{
    String sResellerId = iwc.getParameter(this.parameterResellerId);
    ResellerHome rHome = (ResellerHome) IDOLookup.getHomeLegacy(Reseller.class);
    Reseller reseller = rHome.findByPrimaryKeyLegacy(Integer.parseInt(sResellerId));

    String[] suppIds = iwc.getParameterValues(this.parameterSupplierCheckBox);

    reseller.removeFrom(Supplier.class);

    if (suppIds != null) {
      for (int i = 0; i < suppIds.length; i++) {
        reseller.addTo(Supplier.class, Integer.parseInt(suppIds[i]));
      }
    }
  }

  private void addSuppliers(IWContext iwc, int resellerId) throws SQLException, RemoteException {
    SupplierHome sHome = (SupplierHome) IDOLookup.getHome(Supplier.class);
    Collection collAllSupps = null;
		try {
			collAllSupps = sHome.findAll(getSupplierManager());
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		Supplier[] allSuppsArr = new Supplier[]{};
    if (collAllSupps != null) {
    	allSuppsArr = (Supplier[]) collAllSupps.toArray(new Supplier[]{});
    }
    //Supplier[] allSuppsArr = sHome.findAll();//create().findAll("select * from "+SupplierBMPBean.getSupplierTableName()+" where "+SupplierBMPBean.getColumnNameIsValid()+" = 'Y' order by "+SupplierBMPBean.getColumnNameName());
    Supplier[] resSuppsArr = getResellerManager(iwc).getSuppliers(resellerId, SupplierBMPBean.getColumnNameName());
    Supplier supp;
    
    List allSupps = getList(allSuppsArr);
    List resSupps = getList(resSuppsArr);
    allSupps.removeAll(resSupps);

    Form form = getForm(true);
    Table table = getTable();
    table.setWidth(this.tableWidth);
    form.add(table);
    int row = 1;
    table.add(getHeaderText(iwrb.getLocalizedString("travel.supplier","Supplier")), 1, row);
    table.add(getHeaderText(iwrb.getLocalizedString("travel.use","Use")), 2, row);
    table.setRowColor(row, super.backgroundColor);

    CheckBox checkBox;

    Iterator iter = resSupps.iterator();
    while (iter.hasNext()) {
      ++row;
      supp = (Supplier) iter.next();
      checkBox = new CheckBox(this.parameterSupplierCheckBox, Integer.toString(supp.getID()));
      checkBox.setChecked(true);
      table.add(getText(supp.getName()), 1, row);
      table.add(checkBox, 2, row);
      table.setRowColor(row, super.GRAY);
    }

    ++row;
    table.setRowColor(row, super.backgroundColor);

    iter = allSupps.iterator();
    while (iter.hasNext()) {
      ++row;
      supp = (Supplier) iter.next();
      checkBox = new CheckBox(this.parameterSupplierCheckBox, Integer.toString(supp.getID()));
      checkBox.setChecked(false);
      table.add(getText(supp.getName()), 1, row);
      table.add(checkBox, 2, row);
      table.setRowColor(row, super.GRAY);
    }
    table.setColumnAlignment(2, "center");

    ++row;
    table.setAlignment(2, row, "right");
    table.add(new SubmitButton(iwrb.getImage("buttons/back.gif"), this.sAction, this.parameterEditReseller), 1, row);
    table.add(new SubmitButton(iwrb.getImage("buttons/update.gif"), this.sAction, this.parameterUpdateSuppliers), 2, row);
    table.setRowColor(row, super.GRAY);
    table.setWidth(2, "2");

    add(form);
//    add("adding ton");
  }

  private List getList(Object[] objects) {
    List list = new Vector();
    for (int i = 0; i < objects.length; i++) {
      list.add(objects[i]);
    }
    return list;
  }


  private Table getSupplierList(IWContext iwc, int resellerId) throws SQLException, RemoteException{
    Supplier[] supps = getResellerManager(iwc).getSuppliers(resellerId);

//    Form form = new Form();
//      form.addParameter(this.parameterResellerId, resellerId);
    Table table = getTable();
    table.setWidth(this.tableWidth);
    int row = 1;
//    form.add(table);


    table.add(getHeaderText(iwrb.getLocalizedString("travel.suppliers","Suppliers")), 1, row);
//    table.add(getHeaderText(iwrb.getLocalizedString("travel.remove","Remove")), 2, row);
    table.setRowColor(row, super.backgroundColor);
    CheckBox cBox;
    for (int i = 0; i < supps.length; i++) {
      ++row;
      table.add(supps[i].getName(),1, row);
      table.setRowColor(row, super.GRAY);
      //cBox = new CheckBox(this.parameterCheckBox+supps[i].getID());
        //cBox.setChecked(false);
      //table.add(cBox, 2, row);
    }
    //table.setColumnAlignment(2, "center");
    ++row;
    SubmitButton addNew = new SubmitButton(iwrb.getImage("buttons/add.gif"), sAction, parameterAddSuppliers);
//    SubmitButton update = new SubmitButton(iwrb.getImage("buttons/update.gif"), sAction, parameterUpdateSuppliers);
    table.setAlignment(1, row, "left");
    table.add(addNew, 1, row);
    table.setRowColor(row, super.GRAY);
//    table.setAlignment(2, row, "right");
//   table.add(update, 2, row);

//    return form;
		return table;
  }


  private Form getForm(boolean maintainResellerId) {
    Form form = new Form();
    if (maintainResellerId) {
      form.maintainParameter(this.parameterResellerId);
    }
    return form;
  }
}