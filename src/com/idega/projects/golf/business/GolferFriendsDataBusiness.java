package com.idega.projects.golf.business;

import java.sql.Timestamp;
import com.idega.projects.golf.entity.GolferPageFriendsData;
import java.sql.Date;
import java.lang.String;
import com.idega.util.idegaTimestamp;
import com.idega.data.*;
import java.sql.SQLException;


/**
 * Title:        idegaWeb Classes
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author <a href="bjarni@idega.is">Bjarni Viljhalmsson</a>
 * @version 1.0
 */

public class GolferFriendsDataBusiness {

public static void insertFriendsData(String name, String sSNumber, String email, String adress,
  /* TimeStamp signingDate,*/ String cardType, String cardNumber, String cardExpDate,
   boolean nameAppearance, String paymentAmount, String paymentDuration, String bankAccount, String bankSSNumer,
   boolean giroPayment) throws SQLException{

   GolferPageFriendsData golferPageFriendsData = new GolferPageFriendsData();
     golferPageFriendsData.setName(name);
     golferPageFriendsData.setAdress(adress);
     golferPageFriendsData.setBankAccountSSNumber(bankSSNumer);
     golferPageFriendsData.setBankAccount(bankAccount);
     golferPageFriendsData.setCreditCardExpDate(cardExpDate);
     golferPageFriendsData.setCreditCardNumber(cardNumber);
     golferPageFriendsData.setCreditCardType(cardType);
     golferPageFriendsData.setEmail(email);
     golferPageFriendsData.setGiroPayment(giroPayment);
     golferPageFriendsData.setNameAppearance(nameAppearance);
     golferPageFriendsData.setPaymentAmount(paymentAmount);
     golferPageFriendsData.setPaymentDuration(paymentDuration);
     golferPageFriendsData.setSigningDate(new idegaTimestamp().getTimestampRightNow());
     golferPageFriendsData.setSocialSecurityNumber(sSNumber);
     golferPageFriendsData.insert();
   }

}