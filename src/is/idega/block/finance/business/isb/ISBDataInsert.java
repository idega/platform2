/*
 * $Id: ISBDataInsert.java,v 1.2 2005/03/02 13:13:09 birna Exp $
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

/**
 * 
 *  Last modified: $Date: 2005/03/02 13:13:09 $ by $Author: birna $
 * 
 * @author <a href="mailto:birna@idega.com">birna</a>
 * @version $Revision: 1.2 $
 */
public class ISBDataInsert /*extends Window*/ implements InvoiceDataInsert{
	
	private static String ACTION_URL = "https://ws-test.isb.is/adgerdirv1/krofur.asmx";
	
/*	public ISBDataInsert() {
		setWidth(500);
		setHeight(500);
	}
	public void main(IWContext iwc)throws Exception{
		Form f = new Form();
		SubmitButton sb = new SubmitButton("submit","submit","submit");
		SubmitButton bs = new SubmitButton("getdata", "getdata", "getdata");
		f.add(sb);
		f.add(bs);
		if(iwc.getParameter("submit") != null && !iwc.getParameter("submit").equals("") && iwc.getParameter("submit").equals("submit")) {
			createClaimsInBank(0,170498);
		}
		if(iwc.getParameter("getdata") != null && !iwc.getParameter("getdata").equals("") && iwc.getParameter("getdata").equals("getdata")) {
			getClaimStatusFromBank(0, 170498);
		}
		add(f);
	}*/
	public void createClaimsInBank(int batchNumber, int groupId) {
		ArrayOfKrafa arrayOfKrafa = new ArrayOfKrafa();
		BankFileManager bfm = new BankInvoiceFileManager();
		
		Integer[] krofuNumer = bfm.getInvoiceNumbers(batchNumber);
		
/*		Integer[] krofuNumer = new Integer[2];
		krofuNumer[0] = new Integer(1);
		krofuNumer[1] = new Integer(2);
*/		
		Krafa[] krofur = new Krafa[krofuNumer.length]; 
		for(int i=0; i<krofuNumer.length; i++) {
			Krafa krafa = new Krafa();
			
			int number = krofuNumer[i].intValue();
			krafa.setKrofunumer(number);
			krafa.setKennitalaKrofuhafa(bfm.getClaimantSSN(groupId));//"0904649069"
			krafa.setGjalddagi(bfm.getDueDate(number));//Calendar.getInstance()
			krafa.setFaerslugerd(bfm.getBookkeepingType(number));//"K"
			//add 4 years to the due date (gjalddagi) for nidurfellingardag - 4 years is the max
			Calendar nidurfellingardagur = bfm.getDueDate(number);//Calendar.getInstance();
			nidurfellingardagur.add(Calendar.YEAR,4);
			krafa.setNidurfellingardagur(nidurfellingardagur);
			krafa.setAudkenni(bfm.getClaimantsAccountId(groupId));//"IAS"
			krafa.setKennitalaGreidanda(bfm.getPayersSSN(number));//"0904649069"
			krafa.setBankanumer(Integer.valueOf(bfm.getBankBranchNumber(groupId)).intValue());//515
			krafa.setHofudbok(bfm.getAccountBook(groupId));//66
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
			ws.setUsername(bfm.getUsername(groupId));
			ws.setPassword(bfm.getPassword(groupId));
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
	public void getClaimStatusFromBank(int batchNumber, int groupId) {
		BankFileManager bfm = new BankInvoiceFileManager();
		ArrayOfUppreiknudKrafa arrayOfKrofur = new ArrayOfUppreiknudKrafa();
		

		int bunkanumer = 865369; //TODO: get the bunkanumer from the batchNumber (BatchBMPBean)
		AstandKrofu astandKrofu = AstandKrofu.ALLAR_KROFUR;
		try {
			KrofurWSSoapStub ws = new KrofurWSSoapStub(new URL(ACTION_URL),null);
			arrayOfKrofur = ws.saekjaKrofubunkasvar(new BigDecimal(bunkanumer));
			Calendar from = Calendar.getInstance();
			from.set(2005,1,17);
			Calendar to = Calendar.getInstance();
			to.set(2005,2,18);
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

}
