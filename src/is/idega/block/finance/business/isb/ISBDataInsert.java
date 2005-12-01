/*
 * $Id: ISBDataInsert.java,v 1.3.4.1 2005/12/01 00:36:41 palli Exp $
 * Created on Dec 20, 2004
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.block.finance.business.isb;

import is.idega.block.finance.business.isb.ws.ArrayOfKrafa;
import is.idega.block.finance.business.isb.ws.ArrayOfUppreiknudKrafa;
import is.idega.block.finance.business.isb.ws.AstandKrofu;
import is.idega.block.finance.business.isb.ws.Krafa;
import is.idega.block.finance.business.isb.ws.KrofurWSSoapStub;
import is.idega.block.finance.business.isb.ws.UppreiknudKrafa;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Calendar;

import org.apache.axis.AxisFault;

import com.idega.block.finance.business.BankFileManager;
import com.idega.block.finance.business.BankInvoiceFileManager;
import com.idega.block.finance.business.InvoiceDataInsert;
import com.idega.block.finance.data.BankInfo;

/**
 * 
 *  Last modified: $Date: 2005/12/01 00:36:41 $ by $Author: palli $
 * 
 * @author <a href="mailto:birna@idega.com">birna</a>
 * @version $Revision: 1.3.4.1 $
 */
public class ISBDataInsert /*extends Window*/ implements InvoiceDataInsert{
	
	private static String ACTION_URL = "https://ws-test.isb.is/adgerdirv1/krofur.asmx";

	public void createClaimsInBank(int batchNumber, BankInfo info) {
		BankFileManager bfm = new BankInvoiceFileManager(info);
		
		createClaimsInBank(batchNumber, bfm);
	}
	
	public void createClaimsInBank(int batchNumber, int groupId) {
		BankFileManager bfm = new BankInvoiceFileManager(groupId);

		createClaimsInBank(batchNumber, bfm);
	}
	
	private void createClaimsInBank(int batchNumber, BankFileManager bfm) {
		
		ArrayOfKrafa arrayOfKrafa = new ArrayOfKrafa();
		Integer[] krofuNumer = bfm.getInvoiceNumbers(batchNumber);
		
		Krafa[] krofur = new Krafa[krofuNumer.length]; 
		for(int i=0; i<krofuNumer.length; i++) {
			Krafa krafa = new Krafa();
			
			int number = krofuNumer[i].intValue();
			krafa.setKrofunumer(number);
			krafa.setKennitalaKrofuhafa(bfm.getClaimantSSN());//"0904649069"
			krafa.setGjalddagi(bfm.getDueDate(number));//Calendar.getInstance()
			krafa.setFaerslugerd(bfm.getBookkeepingType(number));//"K"
			//add 4 years to the due date (gjalddagi) for nidurfellingardag - 4 years is the max
			Calendar nidurfellingardagur = bfm.getDueDate(number);//Calendar.getInstance();
			nidurfellingardagur.add(Calendar.YEAR,4);
			krafa.setNidurfellingardagur(nidurfellingardagur);
			krafa.setAudkenni(bfm.getClaimantsAccountId());//"IAS"
			krafa.setKennitalaGreidanda(bfm.getPayersSSN(number));//"0904649069"
			krafa.setBankanumer(Integer.valueOf(bfm.getBankBranchNumber()).intValue());//515
			krafa.setHofudbok(bfm.getAccountBook());//66
			krafa.setUpphaed(new BigDecimal(bfm.getAmount(number)));
			krafa.setTilvisun(String.valueOf(batchNumber));
			krafa.setSedilnumer("");
			krafa.setVidskiptanumer(bfm.getPayersSSN(number));//"0904649069"
			//the eindagi is the same as the due date (gjalddagi) if there are no overdue fee (vanskilagjald)!
			krafa.setEindagi(bfm.getFinalDueDate(number));//Calendar.getInstance()
			krafa.setDrattavaxtaprosenta(new BigDecimal(bfm.getPenalIntrestProsent(number)));
			//the values in drattavaxtaregla may be:
			//'' - drattavextir(penal intrest) taken after eindaga (final due date), but are calculated from gjalddaga (due date).
			//     if eindagi and gjalddagi are the same day and on a bank holiday then the invoice may be payed without
			//     drattavextir the next bank day.
			//'1' - no drattavextir - this is the default
			//'2' - if past the final due date then the drattavextir are calculated from eindagi (eindagi included) with
			//      the rule 360/360
			krafa.setDrattavaxtaregla("1");//bfm.getPenalIntrestRule(numer));
			//controls if drattavextir (penal intrest) is calculated from the amount of the invoice or
			//the amount + vanskilagjald (overdue fee)
			//'' - drattavextir calculated from the amount - the default
			//'1' - drattavextir calculated from the amount + vanskilagjald
			krafa.setDrattavaxtastofnkodi("");//bfm.getPenalIntrestCode(numer));
			//code that tells if the invoice may be payed with existing older invoices
			//' ' - the invoice may not be payed if there exists an older invoice
			//'1' - the invoice may be payed in any case - the default!
			krafa.setGreidslukodi("1");//bfm.getPaymentCode(numer));
			krafa.setTilkynningarOgGreidslugjald1(new BigDecimal(0));//new BigDecimal(bfm.getNotificationAndPaymentFee1(numer)));
			krafa.setTilkynningarOgGreidslugjald2(new BigDecimal(0));//new BigDecimal(bfm.getNotificationAndPaymentFee2(numer)));
			krafa.setVanskilagjald1(new BigDecimal(0));
			krafa.setVanskilagjald2(new BigDecimal(0));
			krafa.setDagafjoldiVanskilagjalds1(0);
			krafa.setDagafjoldiVanskilagjalds2(0);
			krafa.setVanskilagjaldsKodi("");
			krafa.setAnnarKostnadur(new BigDecimal(0));//new BigDecimal(bfm.getOtherCost(numer)));
			krafa.setAnnarVanskilakostnadur(new BigDecimal(0));
			krafa.setGengistegund("");
			krafa.setMynt("");
			krafa.setGengiskodi("");
			krafa.setAfslattur1(new BigDecimal(0));
			krafa.setAfslattur2(new BigDecimal(0));
			krafa.setDagafjoldiAfslattar1(0);
			krafa.setDagafjoldiAfslattar2(0);
			krafa.setAfslattarkodi("");
			krafa.setInnborgunarkodi("");
			krafa.setBirtingakodi("");
			krafa.setVefslod("");
			krafa.setNafnGreidanda1("");
			krafa.setNafnGreidanda2("");
			krafa.setHeimiliGreidanda("");
			krafa.setSveitarfelagGreidanda("");
			krafa.setAthugasemdalina1("");
			krafa.setAthugasemdalina2("");
			krafa.setHreyfingar(null);
			krofur[i] = krafa;
						
		}
		arrayOfKrafa.setKrafa(krofur);
		KrofurWSSoapStub ws = null;
		try {
			ws = new KrofurWSSoapStub(new URL(ACTION_URL),null);
			ws.setUsername(bfm.getUsername());
			ws.setPassword(bfm.getPassword());
			//sending the request to the bank
			BigDecimal bunkanumer = ws.stofnaKrofubunka(arrayOfKrafa);
//			System.out.println("bunkanumer: " + bunkanumer);
			//TODO: store the bunkanumer in BatchBMPBean
		}
		catch (AxisFault e) {
			e.printStackTrace();
		}
		catch (MalformedURLException e2) {
			e2.printStackTrace();
		}
		catch (RemoteException e1) {
			e1.printStackTrace();
		}
		
		
	}
	/*
	 * This method is not functional yet - deserializing problem...
	 *  (non-Javadoc)
	 * @see com.idega.block.finance.business.InvoiceDataInsert#getClaimStatusFromBank(int, int)
	 */
	public void getClaimStatusFromBank(int batchNumber, BankInfo info,
			java.util.Date from, java.util.Date to) {
		BankFileManager bfm = new BankInvoiceFileManager(info);

		getClaimStatusFromBank(batchNumber, bfm, from, to);
	}

	public void getClaimStatusFromBank(int batchNumber, int groupId,
			java.util.Date from, java.util.Date to) {
		BankFileManager bfm = new BankInvoiceFileManager(groupId);

		getClaimStatusFromBank(batchNumber, bfm, from, to);
	}

	private void getClaimStatusFromBank(int batchNumber, BankFileManager bfm,
			java.util.Date from, java.util.Date to) {
		//BankFileManager bfm = new BankInvoiceFileManager();
		ArrayOfUppreiknudKrafa arrayOfKrofur = new ArrayOfUppreiknudKrafa();
		

		int bunkanumer = 865369; //TODO: get the bunkanumer from the batchNumber (BatchBMPBean)
		AstandKrofu astandKrofu = AstandKrofu.ALLAR_KROFUR;
		try {
			KrofurWSSoapStub ws = new KrofurWSSoapStub(new URL(ACTION_URL),null);
			arrayOfKrofur = ws.saekjaKrofubunkasvar(new BigDecimal(bunkanumer));
			Calendar fromCal = Calendar.getInstance();
			fromCal.set(2005,1,17);
			Calendar toCal = Calendar.getInstance();
			toCal.set(2005,2,18);
//			arrayOfKrofur = ws.saekjaKrofur("0904649069","IAS",from,to,astandKrofu,1,5000);
			Calendar gjalddagi = Calendar.getInstance();
			
			UppreiknudKrafa uppreiknudKrafa = ws.saekjaKrofu("0904649069",515,66,12,gjalddagi);
//			System.out.println("audkenni uppreiknudu krofunnar: " + uppreiknudKrafa.getAudkenni());
		}
		catch (AxisFault e) {
			e.printStackTrace();
		}
		catch (MalformedURLException e) {
			e.printStackTrace();
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		
		UppreiknudKrafa[] krofur = arrayOfKrofur.getUppreiknudKrafa();
		for(int i=0; i<krofur.length; i++) {
			UppreiknudKrafa krafa = arrayOfKrofur.getUppreiknudKrafa(i);
			System.out.println( "audkenni krofu: " + krafa.getAudkenni());
			//bfm.setInvoiceStatus(krafa.getStada().getValue(), krafa.getKrofunumer());
		}
		
	}
	public void deleteClaim(BankInfo info, int claimNumber,
			java.util.Date dueDate, String payersSSN) {
		BankFileManager bfm = new BankInvoiceFileManager(info);
		deleteClaim(bfm, claimNumber, dueDate, payersSSN);
	}

	public void deleteClaim(int groupId, int claimNumber,
			java.util.Date dueDate, String payersSSN) {
		BankFileManager bfm = new BankInvoiceFileManager(groupId);
		deleteClaim(bfm, claimNumber, dueDate, payersSSN);
	}

	private void deleteClaim(BankFileManager bfm, int claimNumber,
			java.util.Date dueDate, String payersSSN) {
		
	}

}
