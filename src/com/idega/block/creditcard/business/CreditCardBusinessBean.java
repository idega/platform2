package com.idega.block.creditcard.business;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.transaction.TransactionManager;

import com.idega.block.creditcard.data.CreditCardAuthorizationEntry;
import com.idega.block.creditcard.data.CreditCardMerchant;
import com.idega.block.creditcard.data.KortathjonustanAuthorisationEntries;
import com.idega.block.creditcard.data.KortathjonustanAuthorisationEntriesHome;
import com.idega.block.creditcard.data.KortathjonustanMerchant;
import com.idega.block.creditcard.data.KortathjonustanMerchantHome;
import com.idega.block.creditcard.data.TPosAuthorisationEntriesBean;
import com.idega.block.creditcard.data.TPosAuthorisationEntriesBeanHome;
import com.idega.block.creditcard.data.TPosMerchant;
import com.idega.block.creditcard.data.TPosMerchantHome;
import com.idega.block.trade.data.CreditCardInformation;
import com.idega.block.trade.data.CreditCardInformationHome;
import com.idega.block.trade.stockroom.data.Supplier;
import com.idega.business.IBOLookup;
import com.idega.business.IBOServiceBean;
import com.idega.data.IDOFinderException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDORelationshipException;
import com.idega.idegaweb.IWBundle;
import com.idega.transaction.IdegaTransactionManager;
import com.idega.util.Encrypter;
import com.idega.util.IWTimestamp;

/**
 * @author gimmi
 */
public class CreditCardBusinessBean extends IBOServiceBean implements CreditCardBusiness{
	
  public final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.creditcard";
  
  private final static String PROPERTY_KORTATHJONUSTAN_HOST_NAME = "kortathjonustan_host_name";
  private final static String PROPERTY_KORTATHJONUSTAN_HOST_PORT = "kortathjonustan_host_port";
  private final static String PROPERTY_KORTATHJONUSTAN_KEYSTORE = "kortathjonustan_keystore";
  private final static String PROPERTY_KORTATHJONUSTAN_KEYSTORE_PASS = "kortathjonustan_keystore_pass";

  public String getBundleIdentifier() {
  		return IW_BUNDLE_IDENTIFIER;
  }
  
  public Collection getCreditCardTypeImages(CreditCardClient client) {
  		Collection types = client.getValidCardTypes();
  		Vector images = new Vector();
  		if (types != null  && !types.isEmpty()) {
  			Iterator iter = types.iterator();
  			IWBundle bundle = this.getBundle();
  			String type;
  			while (iter.hasNext()) {
  				type = (String) iter.next();
  				if (CreditCardBusiness.CARD_TYPE_DANKORT.equals(type)) {
  					images.add(bundle.getImage("logos/dankort.gif"));
  				} else if (CreditCardBusiness.CARD_TYPE_DINERS.equals(type)) {
  					images.add(bundle.getImage("logos/diners.gif"));
  				} else if (CreditCardBusiness.CARD_TYPE_ELECTRON.equals(type)) {
  					images.add(bundle.getImage("logos/electron.gif"));
  				} else if (CreditCardBusiness.CARD_TYPE_JCB.equals(type)) {
  					images.add(bundle.getImage("logos/jcb.gif"));
  				} else if (CreditCardBusiness.CARD_TYPE_MASTERCARD.equals(type)) {
  					images.add(bundle.getImage("logos/mastercard.gif"));
  				} else if (CreditCardBusiness.CARD_TYPE_VISA.equals(type)) {
  					images.add(bundle.getImage("logos/visa.gif"));
  				} else if (CreditCardBusiness.CARD_TYPE_AMERICAN_EXPRESS.equals(type)) {
  					images.add(bundle.getImage("logos/ae.gif"));
  				}
  			}
  		}
  		
  		return images;
  }
  
	public CreditCardClient getCreditCardClient(Supplier supplier, IWTimestamp stamp) throws Exception {

		CreditCardMerchant merchant = getCreditCardMerchant(supplier, stamp);
		CreditCardClient client = getCreditCardClient(merchant);

		return client;
	}
	
	public CreditCardClient getCreditCardClient(CreditCardMerchant merchant) throws Exception {
		if (merchant != null && merchant.getType() != null) {
			if (CreditCardMerchant.MERCHANT_TYPE_TPOS.equals(merchant.getType())) {
				return new TPosClient(getIWApplicationContext(), merchant);
			} else if (CreditCardMerchant.MERCHANT_TYPE_KORTHATHJONUSTAN.equals(merchant.getType())) {
				String hostName = getBundle().getProperty(PROPERTY_KORTATHJONUSTAN_HOST_NAME);
				String hostPort =  getBundle().getProperty(PROPERTY_KORTATHJONUSTAN_HOST_PORT);
				String keystore = getBundle().getProperty(PROPERTY_KORTATHJONUSTAN_KEYSTORE);
				String keystorePass =  getBundle().getProperty(PROPERTY_KORTATHJONUSTAN_KEYSTORE_PASS);
				
				return new KortathjonustanCreditCardClient(getIWApplicationContext(), hostName, Integer.parseInt(hostPort), keystore, keystorePass, merchant);
			}
		}
		
		// Default client
		return  new TPosClient(getIWApplicationContext());
	}

	public CreditCardMerchant getCreditCardMerchant(Supplier supplier, IWTimestamp stamp) {
		CreditCardInformation ccInfo = getCreditCardInformation(supplier, stamp);
		return getCreditCardMerchant(ccInfo);
	}
	
	private CreditCardMerchant getCreditCardMerchant(CreditCardInformation ccInfo) {
		if (ccInfo != null) {
			try {
				String type = ccInfo.getType();
				if (CreditCardMerchant.MERCHANT_TYPE_TPOS.equals(type)) {
					TPosMerchantHome tposHome = (TPosMerchantHome) IDOLookup.getHome(TPosMerchant.class);
					return tposHome.findByPrimaryKey(new Integer(ccInfo.getMerchantPKString()));
				} else if (CreditCardMerchant.MERCHANT_TYPE_KORTHATHJONUSTAN.equals(type)) {
					KortathjonustanMerchantHome kortHome = (KortathjonustanMerchantHome) IDOLookup.getHome(KortathjonustanMerchant.class);
					return kortHome.findByPrimaryKey(new Integer(ccInfo.getMerchantPKString()));
				}
			} catch (Exception e) {
				e.printStackTrace(System.err);
			}
		}
		return null;
	}
	
	public CreditCardInformation getCreditCardInformation(Supplier supplier,IWTimestamp stamp) {
		try {
			Collection coll = this.getCreditCardInformations(supplier); 
			if (coll != null) {
				Iterator iter = coll.iterator();
				Timestamp toCheck = null;
				if (stamp != null) {
					stamp.getTimestamp();
				} else {
					toCheck = IWTimestamp.getTimestampRightNow();
				}
				Timestamp starts;
				Timestamp ends;
				CreditCardInformation info;
				CreditCardMerchant merchant;
				while (iter.hasNext()) {
					info = (CreditCardInformation) iter.next();
					merchant = getCreditCardMerchant(info);
					if (merchant != null && !merchant.getIsDeleted()) {
						starts = merchant.getStartDate();
						ends = merchant.getEndDate();
						
						if (ends == null) {
							return info;
						} else if (starts != null && starts.before(toCheck) && ends.after(toCheck)) {
							return info;
						} else if (starts == null) {
							return info;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
		return null;
	}
	
	public CreditCardMerchant getCreditCardMerchant(Supplier supplier, Object PK) {
		try {
			Collection coll = supplier.getCreditCardInformation();
			if (coll != null) {
				Iterator iter = coll.iterator();
				CreditCardInformation info;
				CreditCardMerchant merchant;
				while (iter.hasNext()) {
					info = (CreditCardInformation) iter.next();
					merchant = getCreditCardMerchant(info);
					if (merchant != null && merchant.getPrimaryKey().toString().equals(PK.toString())) {
						return merchant;
					}
				}
			}
		} catch (Exception e) {
			
		}
		return null;
	}

	public CreditCardMerchant createCreditCardMerchant(String type) throws CreateException {
		try {
			if (CreditCardMerchant.MERCHANT_TYPE_TPOS.equals(type)) {
				TPosMerchantHome tposHome = (TPosMerchantHome) IDOLookup.getHome(TPosMerchant.class);
				return tposHome.create();
			} else if (CreditCardMerchant.MERCHANT_TYPE_KORTHATHJONUSTAN.equals(type)) {
				KortathjonustanMerchantHome kortHome = (KortathjonustanMerchantHome) IDOLookup.getHome(KortathjonustanMerchant.class);
				return kortHome.create();
			}
			return null;
		} catch (IDOLookupException e) {
			throw new CreateException(e.getMessage());
		}
	}
	
	public void addCreditCardMerchant(Supplier supplier, CreditCardMerchant merchant) throws CreateException {
		TransactionManager t = IdegaTransactionManager.getInstance();
		try {
			t.begin();

			// Setting other merchants to deleted
			Collection coll = supplier.getCreditCardInformation();
			if (coll != null) {
				Iterator iter = coll.iterator();
				CreditCardInformation info;
				CreditCardMerchant tmpMerchant;
				while (iter.hasNext()) {
					info = (CreditCardInformation) iter.next();
					tmpMerchant = getCreditCardMerchant(supplier, new Integer(info.getMerchantPKString()));
					if (tmpMerchant != null && !tmpMerchant.getIsDeleted()) {
						tmpMerchant.remove();
					}
				}
			}
			
			// Creating a new one
			CreditCardInformationHome infoHome = (CreditCardInformationHome) IDOLookup.getHome(CreditCardInformation.class);
			CreditCardInformation info = infoHome.create();
			info.setType(merchant.getType());
			info.setMerchantPK(merchant.getPrimaryKey());
			info.store();
			
			supplier.addCreditCardInformation(info);

			t.commit();
		} catch (Exception e) {
			try {
				t.rollback();
			} catch (Exception e1) {
				e1.printStackTrace();
				throw new CreateException(e.getMessage());
			}
			throw new CreateException(e.getMessage());
		}
	}
	
	public Collection getCreditCardInformations(Supplier supplier) throws IDORelationshipException {
		Collection coll = supplier.getCreditCardInformation();
	  	if (coll == null || coll.isEmpty()) {
	  		int TPosID = supplier.getTPosMerchantId();
	  		if (TPosID > 0) {
	  			try {
	  				System.out.println("---- Starting backwards.... -----");
	  				System.out.println("---- ... TPosID = "+TPosID+" -----");
	  				TPosMerchantHome tposHome = (TPosMerchantHome) IDOLookup.getHome(TPosMerchant.class);
					TPosMerchant merchant = tposHome.findByPrimaryKey(new Integer(TPosID));
					addCreditCardMerchant(supplier, merchant);
					log("CreditCardBusiness : backwards compatability fix for CreditCard merchant");
					return getCreditCardInformations(supplier);
	  			} catch (Exception e) {
	  				e.printStackTrace(System.err);
	  			}
	  		}
	  	}
	  	return coll;
	}
	
	public static String encodeCreditCardNumber(String originalNumber) throws IllegalArgumentException {
		if (originalNumber != null && originalNumber.length() >= 10) {
			int length = originalNumber.length();
			String enc = Encrypter.encryptOneWay(originalNumber.substring(length - 10, length));
			
      return hexEncode(enc);

		} 
		throw new IllegalArgumentException("Number must be at least 10 characters long");
	}
		
	private static String hexEncode(String enc) {
		try {
		    String str = "";
		    char[] pass = enc.toCharArray();
		    for (int i = 0; i < pass.length; i++) {
		      String hex = Integer.toHexString((int)pass[i]);
		      while (hex.length() < 2) {
		        String s = "0";
		        s += hex;
		        hex = s;
		      }
		      str += hex;
		    }
		    if(str.equals("") && !enc.equals("")){
		      str = null;
		    }
		    
		    return str;
		  }
		  catch (Exception ex) {
		  		ex.printStackTrace(System.err);
		  		return null;
		  }
	}

	public boolean verifyCreditCardNumber(String numberToCheck, CreditCardAuthorizationEntry entry)  throws IllegalArgumentException {
		if (numberToCheck != null && numberToCheck.length() >= 10) {
			int length = numberToCheck.length();
			numberToCheck = numberToCheck.substring(length - 10, length);

			String hex = hexEncode(Encrypter.encryptOneWay(numberToCheck));
			
			return hex.equals(entry.getCardNumber());
		} 
		throw new IllegalArgumentException("Number must be at least 10 characters long");
	}
	
	public CreditCardAuthorizationEntry getAuthorizationEntry(Supplier supplier, String authorizationCode, IWTimestamp stamp) {
		CreditCardInformation info = getCreditCardInformation(supplier, stamp);
		if (info != null) {
			try {
				if ( CreditCardMerchant.MERCHANT_TYPE_TPOS.equals(info.getType()) ){
					TPosAuthorisationEntriesBeanHome authEntHome = (TPosAuthorisationEntriesBeanHome) IDOLookup.getHome(TPosAuthorisationEntriesBean.class);
					TPosAuthorisationEntriesBean entry = authEntHome.findByAuthorisationIdRsp(authorizationCode);
					if (entry != null) {
						return entry;
					}
				} else if ( CreditCardMerchant.MERCHANT_TYPE_KORTHATHJONUSTAN.equals(info.getType()) ) {
					KortathjonustanAuthorisationEntriesHome authEntHome = (KortathjonustanAuthorisationEntriesHome) IDOLookup.getHome(KortathjonustanAuthorisationEntries.class);
					KortathjonustanAuthorisationEntries entry = authEntHome.findByAuthorizationCode(authorizationCode);
					if (entry != null) {
						return entry;
					}
				}
			} catch (IDOFinderException ignore) {
			} catch (Exception e) {
				e.printStackTrace(System.err);
			}
		} else {
			try {
				log("Cannot find creditCardInformation for supplier = "+ supplier.getName()+", looking up authEntry in TPOS...authCode = "+authorizationCode);
				TPosAuthorisationEntriesBeanHome authEntHome = (TPosAuthorisationEntriesBeanHome) IDOLookup.getHome(TPosAuthorisationEntriesBean.class);
				TPosAuthorisationEntriesBean entry = authEntHome.findByAuthorisationIdRsp(authorizationCode);
				if (entry != null) {
					return entry;
				}
			} catch (IDOFinderException ignore) {
			} catch (Exception e) {
				e.printStackTrace(System.err);
			}
		}
		return null;
	}
	
	public boolean getUseCVC(CreditCardClient client) {
		return !(client instanceof TPosClient);
	}
	
	public boolean getUseCVC(CreditCardMerchant merchant) {
		if (merchant != null) {
			return !CreditCardMerchant.MERCHANT_TYPE_TPOS.equals(merchant.getType());
		}
		return false;
	}
		
	public boolean getUseCVC(Supplier supplier, IWTimestamp stamp) {
		return getUseCVC(getCreditCardMerchant(supplier, stamp));
	}


}
