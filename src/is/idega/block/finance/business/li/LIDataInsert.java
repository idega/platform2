/*
 * Created on Jan 13, 2005
 *
 */
package is.idega.block.finance.business.li;

import is.idega.block.finance.business.li.claims.Krafa;
import is.idega.block.finance.business.li.claims.Krofur;
import is.idega.block.finance.business.li.claims.LI_Innheimta_fyrirspurn_krofur;
import is.idega.block.finance.business.li.claims.LI_Innheimta_fyrirspurn_krofur_svar;
import is.idega.block.finance.business.li.claims.LI_Innheimta_krofur_stofna;
import is.idega.block.finance.business.li.claims.Upphaed;
import is.idega.block.finance.business.li.claims_delete.LI_Innheimta_krofur_eyda;
import is.idega.block.finance.business.li.sign_in.LI_Innskra;
import is.idega.block.finance.business.li.sign_in_answer.LIInnskraSvar;
import is.idega.block.finance.business.li.sign_out.LIUtskra;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Calendar;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.exolab.castor.types.Date;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import com.idega.block.finance.business.BankFileManager;
import com.idega.block.finance.business.BankInvoiceFileManager;
import com.idega.block.finance.business.InvoiceDataInsert;
import com.idega.block.finance.data.BankInfo;

/**
 * @author birna
 * 
 */
public class LIDataInsert /* extends Window */implements InvoiceDataInsert {

	private static String POST_METHOD = "https://b2b.fbl.is/lib2b.dll?processXML";

	private static String SIGN_IN_SCHEMA = "https://b2b.fbl.is/schema/LI_Innskra.xsd";

	private static String SIGN_OUT_SCHEMA = "https://b2b.fbl.is/schema/LIUtskra.xsd";

	private static String CREATE_CLAIMS_SCHEMA = "https://b2b.fbl.is/schema/LI_Innheimta_krofur_stofna.xsd";

	private static String GET_UPDATED_CLAIMS_SCHEMA = "https://b2b.fbl.is/schema/LI_Innheimta_fyrirspurn_krofur.xsd";

	private static String DELETE_CLAIM_SCHEMA = "https://b2b.fbl.is/schema/LI_Innheimta_krofur_eyda.xsd";

	private static String SIGN_IN = "iw_cache/signin.xml";

	private static String CREATE_CLAIMS = "iw_cache/claims.xml";

	private static String UPDATED_CLAIMS = "iw_cache/updclaims.xml";

	private static String DELETE_CLAIM = "iw_cache/delclaim.xml";

	private static String SIGN_OUT = "iw_cache/signout.xml";

	private String emptyString = new String();

	private String zeroString = new String();

	public void createClaimsInBank(int batchNumber, BankInfo info) {
		BankFileManager bfm = new BankInvoiceFileManager(info);

		createClaimsInBank(batchNumber, bfm);
	}

	public void createClaimsInBank(int batchNumber, int groupId) {
		BankFileManager bfm = new BankInvoiceFileManager(groupId);

		createClaimsInBank(batchNumber, bfm);
	}

	private void createClaimsInBank(int batchNumber, BankFileManager bfm) {
		for (int i = 0; i < 200; i++) {
			emptyString += " ";
			zeroString += "0";
		}
		Integer[] krofuNumer = bfm.getInvoiceNumbers(batchNumber);

/*		Integer[] krofuNumer = new Integer[2];
		krofuNumer[0] = new Integer(1);
		krofuNumer[1] = new Integer(2);*/

		LI_Innheimta_krofur_stofna stofnaKrofur = new LI_Innheimta_krofur_stofna();
		Krofur krofur = new Krofur();
		for (int i = 0; i < krofuNumer.length; i++) {
			int number = krofuNumer[i].intValue();

			String numberString = String.valueOf(number);
			if (numberString.length() < 6)
				numberString = zeroString.substring(0, 6 - numberString
						.length())
						+ numberString;

			String batchString = String.valueOf(batchNumber);
			if (batchString.length() < 16)
				batchString = batchString
						+ zeroString.substring(0, 16 - batchString.length());

			Krafa krafa = new Krafa();
			krafa.setKt_krofuhafa(bfm.getClaimantSSN());// "7101002090");
			krafa.setBanki(bfm.getBankBranchNumber());// "0132");
			krafa.setHofudbok(String.valueOf(bfm.getAccountBook()));// "66");
			krafa.setNumer(numberString);
			krafa.setGjalddagi(new Date(bfm.getDueDate(number).getTime()));
			krafa.setKt_greidanda(bfm.getPayersSSN(number));
			Calendar nidurfellingardagur = Calendar.getInstance();// bfm.getDueDate(number);
			nidurfellingardagur.add(Calendar.YEAR, 4);
			krafa
					.setNidurfellingardagur(new Date(nidurfellingardagur
							.getTime()));
			krafa.setAudkenni(bfm.getClaimantsAccountId());// "037");
			Upphaed upphaed = new Upphaed();
//			upphaed.setContent(new BigDecimal(100));// bfm.getAmount(number)));
			upphaed.setContent(BigDecimal.valueOf(Long.parseLong(bfm.getAmount(number))));
			krafa.setUpphaed(upphaed);
			krafa.setTilvisunarnumer(batchString);
//			krafa.setEindagi(new Date(Calendar.getInstance().getTime()));// bfm.getFinalDueDate(number).getTime()));
			krafa.setEindagi(new Date(bfm.getFinalDueDate(number).getTime()));
			krafa.setAnnar_kostnadur(0);

			/* Fields not needed... */
			/*
			 * krafa.setSedilnumer("1234567");//bfm.getNoteNumber(number));
			 * krafa.setVidskiptanumer("6210779029");//bfm.getPayersSSN(number));
			 * krafa.setTilkynningar_og_greidslugjald_1(100);//(int)bfm.getNotificationAndPaymentFee1(number));
			 * krafa.setTilkynningar_og_greidslugjald_2(100);//(int)bfm.getNotificationAndPaymentFee2(number));
			 * krafa.setAnnar_kostnadur(100);//(int)bfm.getOtherCost(number));
			 * krafa.setAnnar_vanskila_kostnadur(100);//Integer.valueOf(bfm.getOtherOverdueCost(number)).intValue());
			 * //LI_IK_innborgunarregla_type.VALUE_1 is: "ekki ma greida inn a
			 * krofu" (deposit on invoice not allowed)
			 * krafa.setInnborgunarregla(LI_IK_innborgunarregla_type.VALUE_1);//bfm.getPaymentCode(number));
			 * //LI_IK_greidsluregla_type.VALUE_0 is: "ma greida eldri
			 * gjalddaga" (payment allowed for older invoices)
			 * krafa.setGreidsluregla(LI_IK_greidsluregla_type.VALUE_0);//bfm.getPaymentCode(number));
			 * Vanskilagjald vanskilagjald = new Vanskilagjald();
			 * vanskilagjald.setVidmidun(LI_IK_vidmidun_dags_type.EINDAGI);
			 * vanskilagjald.setTegund_vanskilagjalds(LI_IK_tegund_afslattar_og_vanskilagjalds_type.VALUE_0);
			 * krafa.setVanskilagjald(vanskilagjald); Drattarvextir
			 * drattarvextir = new Drattarvextir(); drattarvextir.setProsent(new
			 * BigDecimal(10));//bfm.getPenalIntrestProsent(number)));
			 * //LI_IK_vaxtastofnkodi_type.VALUE_0 is: "upphaed" (amount)
			 * drattarvextir.setVaxtastofn(LI_IK_vaxtastofnkodi_type.VALUE_0);
			 * //ReglaType.VALUE_0 is: 360/360
			 * drattarvextir.setRegla(ReglaType.VALUE_0);
			 * krafa.setDrattarvextir(drattarvextir); Gengiskrafa gengiskrafa =
			 * new Gengiskrafa(); //GengistegundType.VALUE_0 is: "almennt gengi
			 * vidskiptabanka"
			 * gengiskrafa.setGengistegund(GengistegundType.VALUE_0);
			 * //LI_IK_mynt_type.ISK is: ISK obviously!
			 * gengiskrafa.setMynt(LI_IK_mynt_type.ISK);
			 * //GengisbankiType.VALUE_1 is: Landsbankinn
			 * gengiskrafa.setGengisbanki(GengisbankiType.VALUE_1);
			 * //GengisreglaType.GJALDDAGAGENGI is:
			 * gengiskrafa.setGengisregla(GengisreglaType.GJALDDAGAGENGI);
			 * krafa.setGengiskrafa(gengiskrafa); Afslattur afslattur = new
			 * Afslattur();
			 * afslattur.setVidmidun(LI_IK_vidmidun_dags_type.EINDAGI);
			 * afslattur.setTegund_afslattar(LI_IK_tegund_afslattar_og_vanskilagjalds_type.VALUE_0);
			 * krafa.setAfslattur(afslattur); Birtingarkerfi birtingarkerfi =
			 * new Birtingarkerfi();
			 * birtingarkerfi.setTegund("A");//bfm.getDisplayFormType(number));
			 * birtingarkerfi.setSlod("");//bfm.getDisplayFormURL(number));
			 * krafa.setBirtingarkerfi(birtingarkerfi);
			 */

			krofur.addKrafa(krafa);
		}
		String sessionId = signIn(bfm);
		stofnaKrofur.setVersion(new BigDecimal("1.1"));
		stofnaKrofur.setSession_id(sessionId);
		stofnaKrofur.setKrofur(krofur);

		try {
			stofnaKrofur.marshal(new FileWriter(CREATE_CLAIMS),
					CREATE_CLAIMS_SCHEMA);
			sendRequest(CREATE_CLAIMS);
			signOut(sessionId);
		} catch (MarshalException e) {
			e.printStackTrace();
		} catch (ValidationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

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
		LI_Innheimta_fyrirspurn_krofur fyrirspurn = new LI_Innheimta_fyrirspurn_krofur();
		fyrirspurn.setKt_krofuhafa(bfm.getClaimantSSN());// "7101002090");
		fyrirspurn.setGjalddagi_fra(new Date(from));
		fyrirspurn.setGjalddagi_til(new Date(to));

		LI_Innheimta_fyrirspurn_krofur_svar svar = new LI_Innheimta_fyrirspurn_krofur_svar();

		String sessionId = signIn(bfm);
		fyrirspurn.setSession_id(sessionId);
		fyrirspurn.setVersion(new BigDecimal("1.1"));

		try {
			fyrirspurn.marshal(new FileWriter(UPDATED_CLAIMS),
					GET_UPDATED_CLAIMS_SCHEMA);
			// the InputStreamReader must be constructed with ISO-8859-1
			// because the bank is returning that encoding, containing icelandic
			// characters!
			svar = (LI_Innheimta_fyrirspurn_krofur_svar) svar
					.unmarshal(new InputStreamReader(
							sendRequest(UPDATED_CLAIMS)
									.getResponseBodyAsStream(), "ISO-8859-1"));
			signOut(sessionId);
		} catch (MarshalException e) {
			e.printStackTrace();
		} catch (ValidationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Krofur krofur = svar.getKrofur();
		for (int i = 0; i < krofur.getKrafaCount(); i++) {
			Krafa krafa = krofur.getKrafa(i);
			bfm.setInvoiceStatus(krafa.getAstand().toString(), Integer.valueOf(
					krafa.getNumer()).intValue());
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
		LI_Innheimta_krofur_eyda delete = new LI_Innheimta_krofur_eyda();
		is.idega.block.finance.business.li.claims_delete.Krofur krofur = new is.idega.block.finance.business.li.claims_delete.Krofur();
		is.idega.block.finance.business.li.claims_delete.Krafa krafa = new is.idega.block.finance.business.li.claims_delete.Krafa();
		krafa.setKt_krofuhafa(bfm.getClaimantSSN());
		krafa.setBanki(bfm.getBankBranchNumber());
		krafa.setHofudbok(String.valueOf(bfm.getAccountBook()));
		krafa.setNumer(String.valueOf(claimNumber));
		krafa.setGjalddagi(new Date(dueDate));
		krafa.setKt_greidanda(payersSSN);
		krofur.addKrafa(krafa);
		delete.setKrofur(krofur);

		String sessionId = signIn(bfm);
		delete.setSession_id(sessionId);
		delete.setVersion(new BigDecimal("1.1"));

		try {
			delete.marshal(new FileWriter(DELETE_CLAIM), DELETE_CLAIM_SCHEMA);
			sendRequest(DELETE_CLAIM);
			signOut(sessionId);
		} catch (MarshalException e) {
			e.printStackTrace();
		} catch (ValidationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String signIn(BankFileManager bfm) {
		LI_Innskra innskra = new LI_Innskra();
		innskra.setNotandanafn(bfm.getUsername());
		innskra.setLykilord(bfm.getPassword());
		innskra.setVersion(new BigDecimal("1.1"));
		LIInnskraSvar svar = new LIInnskraSvar();

		try {
			innskra.marshal(new FileWriter(SIGN_IN), SIGN_IN_SCHEMA);
			svar = (LIInnskraSvar) svar.unmarshal(new InputStreamReader(
					sendRequest(SIGN_IN).getResponseBodyAsStream()));
		} catch (MarshalException e) {
			e.printStackTrace();
		} catch (ValidationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return svar.getSeta();
	}

	private void signOut(String sessionId) {
		LIUtskra utskra = new LIUtskra();
		utskra.setVersion(new BigDecimal("1.1"));
		utskra.setSeta(sessionId);
		try {
			utskra.marshal(new FileWriter(SIGN_OUT), SIGN_OUT_SCHEMA);
			sendRequest(SIGN_OUT);
		} catch (MarshalException e) {
			e.printStackTrace();
		} catch (ValidationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	private PostMethod sendRequest(String fileName) {
		HttpClient client = new HttpClient();
		PostMethod post = new PostMethod(POST_METHOD);
		post.getParams().setBooleanParameter(
				HttpMethodParams.USE_EXPECT_CONTINUE, true);

		try {
			//post.setRequestBody(new FileInputStream(fileName));
			post.setRequestEntity(new InputStreamRequestEntity(new FileInputStream(fileName)));
			client.executeMethod(post);
			System.out.println("responseString: " + post.getResponseBodyAsString());
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		} finally {
			post.releaseConnection();
		}
		return post;
	}
}
