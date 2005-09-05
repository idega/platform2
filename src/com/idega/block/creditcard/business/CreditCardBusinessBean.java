package com.idega.block.creditcard.business;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.transaction.TransactionManager;
import com.idega.block.creditcard.data.CreditCardAuthorizationEntry;
import com.idega.block.creditcard.data.CreditCardMerchant;
import com.idega.block.creditcard.data.KortathjonustanAuthorisationEntries;
import com.idega.block.creditcard.data.KortathjonustanAuthorisationEntriesBMPBean;
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
import com.idega.business.IBOServiceBean;
import com.idega.data.IDOFinderException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDORelationshipException;
import com.idega.idegaweb.IWBundle;
import com.idega.transaction.IdegaTransactionManager;
import com.idega.user.data.Group;
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
  
  public final static int CLIENT_TYPE_TPOS = 1;
  public final static int CLIENT_TYPE_KORTATHJONUSTAN = 2;

  public String getBundleIdentifier() {
  		return IW_BUNDLE_IDENTIFIER;
  }
  
  public Collection getAuthorizationEntries(int clientType, String merchantID, IWTimestamp from, IWTimestamp to) throws IDOLookupException, FinderException {
	  if (clientType > 0) {
		  if (clientType == CLIENT_TYPE_KORTATHJONUSTAN) {
			  KortathjonustanAuthorisationEntriesHome home = (KortathjonustanAuthorisationEntriesHome) IDOLookup.getHome(KortathjonustanAuthorisationEntries.class);
			  Collection coll =  home.findByDates(from, to);
			  
			  return coll;
		  } else if (clientType == CLIENT_TYPE_TPOS) {
			  TPosAuthorisationEntriesBeanHome home = (TPosAuthorisationEntriesBeanHome) IDOLookup.getHome(TPosAuthorisationEntriesBean.class);
			  Collection coll =  home.findByDates(from, to);
			  
			  return coll;
		  } 
	  }
	  return null;
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
	
	public CreditCardClient getCreditCardClient(Group supplierManager, IWTimestamp stamp) throws Exception {
		CreditCardMerchant m = getCreditCardMerchant(supplierManager, stamp);
		return getCreditCardClient(m);
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
	
	public CreditCardMerchant getCreditCardMerchant(Group supplierManager, IWTimestamp stamp) {
		CreditCardInformation ccInfo = getCreditCardInformations(supplierManager, stamp);
		return getCreditCardMerchant(ccInfo);
	}
	
	private CreditCardMerchant getCreditCardMerchant(CreditCardInformation ccInfo) {
		if (ccInfo != null) {
			try {
				String type = ccInfo.getType();
				if (CreditCardMerchant.MERCHANT_TYPE_TPOS.equals(type)) {
					TPosMerchantHome tposHome = (TPosMerchantHome) IDOLookup.getHome(TPosMerchant.class, ccInfo.getDatasource());
					return tposHome.findByPrimaryKey(new Integer(ccInfo.getMerchantPKString()));
				} else if (CreditCardMerchant.MERCHANT_TYPE_KORTHATHJONUSTAN.equals(type)) {
					KortathjonustanMerchantHome kortHome = (KortathjonustanMerchantHome) IDOLookup.getHome(KortathjonustanMerchant.class, ccInfo.getDatasource());
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
			Timestamp toCheck = null;
			if (stamp != null) {
				toCheck = stamp.getTimestamp();
			} else {
				toCheck = IWTimestamp.getTimestampRightNow();
			}

			CreditCardInformation ccInfo = null;
			
			// Checking for merchants configured to this supplier
			Collection coll = this.getCreditCardInformations(supplier); 
			if (coll != null) {
				Iterator iter = coll.iterator();
				ccInfo = getCreditCardInformationInUse(iter, toCheck);
			}
			
			// Checking for merchants configured to this supplier's supplierManager
			if (ccInfo == null) {
				ccInfo = getCreditCardInformations(supplier.getSupplierManager(), stamp);
			}
			
			return ccInfo;
			
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
		return null;
	}

	/**
	 * @param supplier
	 * @param toCheck
	 * @param ccInfo
	 * @return
	 * @throws FinderException
	 * @throws IDOLookupException
	 */
	private CreditCardInformation getCreditCardInformations(Group supplierManager, IWTimestamp stamp) {
		Timestamp toCheck = null;
		if (stamp != null) {
			toCheck = stamp.getTimestamp();
		} else {
			toCheck = IWTimestamp.getTimestampRightNow();
		}

		CreditCardInformation ccInfo = null;
		try {
			Collection coll;
			coll = getCreditCardInformations(supplierManager);
			if (ccInfo == null) {
				Iterator iter = coll.iterator();
				ccInfo = getCreditCardInformationInUse(iter, toCheck);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ccInfo;
	}
	
	private CreditCardInformation getCreditCardInformationInUse(Iterator iter, Timestamp toCheck) {
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
		return null;
	}

	public CreditCardMerchant getCreditCardMerchant(Supplier supplier, Object PK) {
		try {
			Collection coll = supplier.getCreditCardInformation();
			CreditCardMerchant returner = getCreditCardMerchant(PK, coll);
			return returner;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public CreditCardMerchant getCreditCardMerchant(Group supplierManager, Object PK) {
		try {
			Collection coll = getCreditCardInformations(supplierManager);
			CreditCardMerchant returner = getCreditCardMerchant(PK, coll);
			return returner;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param PK
	 * @param creditcardInformations
	 * @param returner
	 * @return
	 */
	private CreditCardMerchant getCreditCardMerchant(Object PK, Collection creditcardInformations) {
		if (creditcardInformations != null) {
			Iterator iter = creditcardInformations.iterator();
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
	
	public void addCreditCardMerchant(Group supplierManager, CreditCardMerchant merchant) throws CreateException {
		addCreditCardMerchant( (Object) supplierManager, merchant);
	}

	public void addCreditCardMerchant(Supplier supplier, CreditCardMerchant merchant) throws CreateException {
		addCreditCardMerchant((Object) supplier, merchant);
	}

	private void addCreditCardMerchant(Object merchantType, CreditCardMerchant merchant) throws CreateException {
		TransactionManager t = IdegaTransactionManager.getInstance();
		try {
			t.begin();
			boolean isSupplier = (merchantType instanceof Supplier);
			boolean isSupplierManager = (merchantType instanceof Group);

			// Setting other merchants to deleted
			Collection coll = null;
			if (isSupplier) {
				coll = ((Supplier) merchantType).getCreditCardInformation();
			} else if (isSupplierManager) {
				coll = getCreditCardInformations((Group) merchantType);
			}
			
			if (coll != null) {
				Iterator iter = coll.iterator();
				CreditCardInformation info;
				CreditCardMerchant tmpMerchant = null;
				while (iter.hasNext()) {
					info = (CreditCardInformation) iter.next();
					
					if (isSupplier) {
						tmpMerchant = getCreditCardMerchant((Supplier)merchantType, new Integer(info.getMerchantPKString()));
					} else if (isSupplierManager) {
						tmpMerchant = getCreditCardMerchant((Group) merchantType, new Integer(info.getMerchantPKString()));
					}
					if (tmpMerchant != null && !tmpMerchant.getIsDeleted()) {
						tmpMerchant.remove();
					}
				}
			}
			
			// Creating a new one
			CreditCardInformationHome infoHome = (CreditCardInformationHome) IDOLookup.getHome(CreditCardInformation.class, merchant.getDatasource());
			CreditCardInformation info = infoHome.create();
			info.setType(merchant.getType());
			info.setMerchantPK(merchant.getPrimaryKey());
			if (isSupplierManager) {
				info.setSupplierManager((Group) merchantType);
			}
			info.store();
			
			if (isSupplier) {
				((Supplier) merchantType).addCreditCardInformation(info);
			}

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
	  				TPosMerchantHome tposHome = (TPosMerchantHome) IDOLookup.getHome(TPosMerchant.class, supplier.getDatasource());
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
	
	public Collection getCreditCardInformations(Group supplierManager) throws FinderException, IDOLookupException {
		CreditCardInformationHome ccInfoHome = (CreditCardInformationHome) IDOLookup.getHome(CreditCardInformation.class, supplierManager.getDatasource());
		return ccInfoHome.findBySupplierManager(supplierManager);
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
	
	public CreditCardAuthorizationEntry getAuthorizationEntry(Group supplierManager, String authorizationCode, IWTimestamp stamp) {
		CreditCardInformation info = getCreditCardInformations(supplierManager, stamp);
		return getAuthorizationEntry(info, authorizationCode, stamp);
	}	
	
	public CreditCardAuthorizationEntry getAuthorizationEntry(Supplier supplier, String authorizationCode, IWTimestamp stamp) {
		CreditCardInformation info = getCreditCardInformation(supplier, stamp);
		CreditCardAuthorizationEntry entry =  getAuthorizationEntry(info, authorizationCode, stamp);
		if (entry == null) {
			entry = getAuthorizationEntry(supplier.getSupplierManager(), authorizationCode, stamp);
		}
		return entry;
	}
	private CreditCardAuthorizationEntry getAuthorizationEntry(CreditCardInformation info, String authorizationCode, IWTimestamp stamp) {
		if (info != null) {
			try {
				if ( CreditCardMerchant.MERCHANT_TYPE_TPOS.equals(info.getType()) ){
					TPosAuthorisationEntriesBeanHome authEntHome = (TPosAuthorisationEntriesBeanHome) IDOLookup.getHome(TPosAuthorisationEntriesBean.class, info.getDatasource());
					TPosAuthorisationEntriesBean entry = authEntHome.findByAuthorisationIdRsp(authorizationCode, stamp);
					if (entry != null) {
						return entry;
					}
				} else if ( CreditCardMerchant.MERCHANT_TYPE_KORTHATHJONUSTAN.equals(info.getType()) ) {
					KortathjonustanAuthorisationEntriesHome authEntHome = (KortathjonustanAuthorisationEntriesHome) IDOLookup.getHome(KortathjonustanAuthorisationEntries.class, info.getDatasource());
					KortathjonustanAuthorisationEntries entry = authEntHome.findByAuthorizationCode(authorizationCode, stamp);
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
				log("Cannot find creditCardInformation for = "+ info.getType()+", "+info.getMerchantPKString()+" looking up authEntry in TPOS...authCode = "+authorizationCode);
				TPosAuthorisationEntriesBeanHome authEntHome = (TPosAuthorisationEntriesBeanHome) IDOLookup.getHome(TPosAuthorisationEntriesBean.class, info.getDatasource());
				TPosAuthorisationEntriesBean entry = authEntHome.findByAuthorisationIdRsp(authorizationCode, stamp);
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

	public Collection getAllRefunds(IWTimestamp from, IWTimestamp to, int clientType) throws IDOLookupException, FinderException {
		Collection coll = new Vector();
		if (clientType == CLIENT_TYPE_TPOS) {
			TPosAuthorisationEntriesBeanHome home = (TPosAuthorisationEntriesBeanHome) IDOLookup.getHome(TPosAuthorisationEntriesBean.class);
			coll = home.findRefunds(from, to);
		}
		else if (clientType == CLIENT_TYPE_KORTATHJONUSTAN) {
			KortathjonustanAuthorisationEntriesHome home = (KortathjonustanAuthorisationEntriesHome) IDOLookup.getHome(KortathjonustanAuthorisationEntriesBMPBean.class);
			coll = home.findRefunds(from, to);
		}
		return coll;
	}

}
