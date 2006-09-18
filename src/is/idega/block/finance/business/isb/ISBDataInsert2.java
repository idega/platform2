package is.idega.block.finance.business.isb;

import is.idega.block.finance.business.isb.ws2.Krafa;
import is.idega.block.finance.business.isb.ws2.KrofurWSLocator;
import is.idega.block.finance.business.isb.ws2.KrofurWSSoap_PortType;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Date;

import javax.ejb.FinderException;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.xml.rpc.ServiceException;

import org.apache.axis.EngineConfiguration;
import org.apache.axis.client.Stub;
import org.apache.axis.configuration.FileProvider;
import org.apache.ws.security.WSConstants;
import org.apache.ws.security.WSPasswordCallback;
import org.apache.ws.security.handler.WSHandlerConstants;

import com.idega.block.finance.business.BankFileManager;
import com.idega.block.finance.business.BankInvoiceFileManager;
import com.idega.block.finance.business.InvoiceDataInsert;
import com.idega.block.finance.data.BankInfo;
import com.idega.block.finance.data.BankInfoHome;
import com.idega.block.finance.data.Batch;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWMainApplication;
import com.idega.util.FileUtil;

public class ISBDataInsert2 implements InvoiceDataInsert, CallbackHandler {

	private static String ACTION_URL = "https://ws.isb.is/adgerdirv1/krofur.asmx";

	// create claim
	public void createClaimsInBank(int batchNumber, int groupId) {
		BankFileManager bfm = new BankInvoiceFileManager(groupId);

		createClaimsInBank(batchNumber, bfm);
	}

	public void createClaimsInBank(int batchNumber, BankInfo info) {
		BankFileManager bfm = new BankInvoiceFileManager(info);

		createClaimsInBank(batchNumber, bfm);
	}

	private void createClaimsInBank(int batchNumber, BankFileManager bfm) {
		Integer[] krofuNumer = bfm.getInvoiceNumbers(batchNumber);

		Krafa[] krofur = new Krafa[krofuNumer.length];
		for (int i = 0; i < krofuNumer.length; i++) {
			Krafa krafa = new Krafa();

			int number = krofuNumer[i].intValue();
			krafa.setKrofunumer(number);
			krafa.setKennitalaKrofuhafa(bfm.getClaimantSSN());// "0904649069"
			krafa.setGjalddagi(bfm.getDueDate(number));// Calendar.getInstance()
			krafa.setFaerslugerd(bfm.getBookkeepingType(number));// "K"
			// add 4 years to the due date (gjalddagi) for nidurfellingardag - 4
			// years is the max
			Calendar nidurfellingardagur = bfm.getDueDate(number);// Calendar.getInstance();
			nidurfellingardagur.add(Calendar.YEAR, 4);
			krafa.setNidurfellingardagur(nidurfellingardagur);
			krafa.setAudkenni(bfm.getClaimantsAccountId());// "IAS"
			krafa.setKennitalaGreidanda(bfm.getPayersSSN(number));// "0904649069"
			krafa.setBankanumer(Integer.valueOf(bfm.getBankBranchNumber())
					.intValue());// 515
			krafa.setHofudbok(bfm.getAccountBook());// 66
			krafa.setUpphaed(new BigDecimal(bfm.getAmount(number)));
			krafa.setTilvisun(String.valueOf(batchNumber));
			krafa.setSedilnumer("");
			krafa.setVidskiptanumer(bfm.getPayersSSN(number));// "0904649069"
			// the eindagi is the same as the due date (gjalddagi) if there are
			// no overdue fee (vanskilagjald)!
			krafa.setEindagi(bfm.getFinalDueDate(number));// Calendar.getInstance()
			krafa.setDrattavaxtaprosenta(new BigDecimal(bfm
					.getPenalIntrestProsent(number)));
			// the values in drattavaxtaregla may be:
			// '' - drattavextir(penal intrest) taken after eindaga (final due
			// date), but are calculated from gjalddaga (due date).
			// if eindagi and gjalddagi are the same day and on a bank holiday
			// then the invoice may be payed without
			// drattavextir the next bank day.
			// '1' - no drattavextir - this is the default
			// '2' - if past the final due date then the drattavextir are
			// calculated from eindagi (eindagi included) with
			// the rule 360/360
			krafa.setDrattavaxtaregla("1");// bfm.getPenalIntrestRule(numer));
			// controls if drattavextir (penal intrest) is calculated from the
			// amount of the invoice or
			// the amount + vanskilagjald (overdue fee)
			// '' - drattavextir calculated from the amount - the default
			// '1' - drattavextir calculated from the amount + vanskilagjald
			krafa.setDrattavaxtastofnkodi("");// bfm.getPenalIntrestCode(numer));
			// code that tells if the invoice may be payed with existing older
			// invoices
			// ' ' - the invoice may not be payed if there exists an older
			// invoice
			// '1' - the invoice may be payed in any case - the default!
			krafa.setGreidslukodi("1");// bfm.getPaymentCode(numer));
			krafa.setTilkynningarOgGreidslugjald1(new BigDecimal(0));// new
			// BigDecimal(bfm.getNotificationAndPaymentFee1(numer)));
			krafa.setTilkynningarOgGreidslugjald2(new BigDecimal(0));// new
			// BigDecimal(bfm.getNotificationAndPaymentFee2(numer)));
			krafa.setVanskilagjald1(new BigDecimal(0));
			krafa.setVanskilagjald2(new BigDecimal(0));
			krafa.setDagafjoldiVanskilagjalds1(0);
			krafa.setDagafjoldiVanskilagjalds2(0);
			krafa.setVanskilagjaldsKodi("");
			krafa.setAnnarKostnadur(new BigDecimal(0));// new
			// BigDecimal(bfm.getOtherCost(numer)));
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

		try {
			
			StringBuffer file = new StringBuffer(IWMainApplication.getDefaultIWMainApplication().getBundle("is.idega.block.finance").getResourcesRealPath());
			file.append(FileUtil.getFileSeparator());
			file.append("client_deploy.wsdd");

			System.out.println("path = " + file.toString());
			
			EngineConfiguration config = new FileProvider(file.toString());
			KrofurWSLocator locator = new KrofurWSLocator(config);
			KrofurWSSoap_PortType port = locator.getKrofurWSSoap(new URL(
					ACTION_URL));

			Stub stub = (Stub) port;
			stub._setProperty(WSHandlerConstants.ACTION,
					WSHandlerConstants.USERNAME_TOKEN);
			stub._setProperty(WSHandlerConstants.PASSWORD_TYPE,
					WSConstants.PW_TEXT);
			stub._setProperty(WSHandlerConstants.USER, bfm.getUsername());
			stub._setProperty(WSHandlerConstants.PW_CALLBACK_CLASS, this
					.getClass().getName());

			BigDecimal batchNr = port.stofnaKrofubunka(krofur);
			Batch b = bfm.getBatch(batchNumber);
			if (b != null) {
				b.setExternalBatchNumber(batchNr.toString());
				b.store();
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

	public void handle(Callback[] callbacks)
			throws UnsupportedCallbackException {
		for (int i = 0; i < callbacks.length; i++) {
			if (callbacks[i] instanceof WSPasswordCallback) {
				WSPasswordCallback pc = (WSPasswordCallback) callbacks[i];
				String password = getPassword(pc.getIdentifer());
				pc.setPassword(password);
			} else {
				throw new UnsupportedCallbackException(callbacks[i],
						"Unrecognized Callback");
			}
		}
	}
	
	private String getPassword(String userName) {
		String password = "";
		try {
			BankInfoHome home = (BankInfoHome) IDOLookup.getHome(BankInfo.class);
			BankInfo info = home.findByUserNameAndBankShortName(userName, "ISB");
			password = info.getPassword();
		} catch (IDOLookupException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}

		System.out.println("userName = " + userName);
		System.out.println("password = " + password);
		
		return password;
	}

	// get claim status
	public void getClaimStatusFromBank(int batchNumber, int groupId, Date from,
			Date to) {
	}

	public void getClaimStatusFromBank(int batchNumber, BankInfo info,
			Date from, Date to) {
	}

	// delete claim
	public void deleteClaim(int groupId, int claimNumber, Date dueDate,
			String payersSSN) {
	}

	public void deleteClaim(BankInfo info, int claimNumber, Date dueDate,
			String payersSSN) {
	}
}