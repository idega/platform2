package com.idega.block.creditcard.business;

import com.idega.block.creditcard.data.CreditCardMerchant;
import com.idega.block.trade.stockroom.data.Supplier;
import com.idega.util.IWTimestamp;


public interface CreditCardBusiness extends com.idega.business.IBOService
{
	
  public final static String CARD_TYPE_VISA = "VISA";
  public final static String CARD_TYPE_ELECTRON = "ELECTRON";
  public final static String CARD_TYPE_DINERS = "DINERS";
  public final static String CARD_TYPE_DANKORT = "DANKORT";
  public final static String CARD_TYPE_MASTERCARD = "MASTERCARD";
  public final static String CARD_TYPE_JCB = "JCB";
  public final static String CARD_TYPE_AMERICAN_EXPRESS = "AMERICAN_EXRESS";
  
 public void addCreditCardMerchant(com.idega.block.trade.stockroom.data.Supplier p0,com.idega.block.creditcard.data.CreditCardMerchant p1)throws javax.ejb.CreateException, java.rmi.RemoteException;
 public com.idega.block.creditcard.data.CreditCardMerchant createCreditCardMerchant(java.lang.String p0)throws javax.ejb.CreateException, java.rmi.RemoteException;
 //public java.lang.String encodeCreditCardNumber(java.lang.String p0)throws java.lang.IllegalArgumentException, java.rmi.RemoteException;
 public com.idega.block.creditcard.data.CreditCardAuthorizationEntry getAuthorizationEntry(com.idega.block.trade.stockroom.data.Supplier p0,java.lang.String p1, IWTimestamp stamp) throws java.rmi.RemoteException;
 public CreditCardClient getCreditCardClient(CreditCardMerchant merchant) throws Exception;
 public com.idega.block.creditcard.business.CreditCardClient getCreditCardClient(com.idega.block.trade.stockroom.data.Supplier p0, IWTimestamp stamp)throws java.lang.Exception, java.rmi.RemoteException;
 public com.idega.block.trade.data.CreditCardInformation getCreditCardInformation(com.idega.block.trade.stockroom.data.Supplier p0, IWTimestamp p1) throws java.rmi.RemoteException;
 public java.util.Collection getCreditCardInformations(com.idega.block.trade.stockroom.data.Supplier p0)throws com.idega.data.IDORelationshipException, java.rmi.RemoteException;
 public com.idega.block.creditcard.data.CreditCardMerchant getCreditCardMerchant(com.idega.block.trade.stockroom.data.Supplier p0, IWTimestamp p1) throws java.rmi.RemoteException;
 public com.idega.block.creditcard.data.CreditCardMerchant getCreditCardMerchant(com.idega.block.trade.stockroom.data.Supplier p0,java.lang.Object p1) throws java.rmi.RemoteException;
 public java.util.Collection getCreditCardTypeImages(com.idega.block.creditcard.business.CreditCardClient p0) throws java.rmi.RemoteException;
 public boolean verifyCreditCardNumber(java.lang.String p0,com.idega.block.creditcard.data.CreditCardAuthorizationEntry p1)throws java.lang.IllegalArgumentException, java.rmi.RemoteException;
 public boolean getUseCVC(CreditCardClient client);
 public boolean getUseCVC(CreditCardMerchant merchant);
 public boolean getUseCVC(Supplier supplier, IWTimestamp stamp);

}
