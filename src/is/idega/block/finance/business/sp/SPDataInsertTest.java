/*
 * $Id: SPDataInsertTest.java,v 1.1.2.4 2006/02/14 18:46:19 palli Exp $ Created on Nov 2,
 * 2005
 * 
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 * 
 * This software is the proprietary information of Idega hf. Use is subject to
 * license terms.
 */
package is.idega.block.finance.business.sp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.MultipartPostMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.params.HttpMethodParams;

import com.idega.block.finance.business.BankFileManager;
import com.idega.block.finance.business.BankInvoiceFileManager;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.util.IWTimestamp;

public class SPDataInsertTest extends Block {

	private static String SITE = "https://heimabanki.spar.is";

//	private static String POST_METHOD = "/HBScripts/ftbspar.dll?InnheimtaStofnaKrofuSkra";
	private static String POST_METHOD = "/HBScripts/ftbspar.dll?InnheimtaSendaPrufuSkra";

	private static String POST_ANSWER_METHOD = "/HBScripts/ftbspar.dll?InnheimtaSaekjaInnborganir";

	private static String FILE_NAME = "/Users/bluebottle/sp.txt";

	private String emptyString = new String();

	private String zeroString = new String();

	public static void main(String[] args) {
		System.out.println("Starting test");
		SPDataInsertTest test = new SPDataInsertTest();
		test.sendCreateClaimsRequest();
	}
	
	public void main(IWContext iwc) throws Exception {
		Form f = new Form();
		SubmitButton sb = new SubmitButton("submit", "submit", "submit");
		SubmitButton gd = new SubmitButton("getdata", "getdata", "getdata");
		f.add(sb);
		f.add(gd);
		if (iwc.getParameter("submit") != null
				&& !iwc.getParameter("submit").equals("")
				&& iwc.getParameter("submit").equals("submit")) { // getSessionId();
			createClaimsInBank(1);
		}
		if (iwc.getParameter("getdata") != null
				&& !iwc.getParameter("getdata").equals("")
				&& iwc.getParameter("getdata").equals("getdata")) {
		}
		add(f);
	} /*
		 * (non-Javadoc)
		 * 
		 * @see com.idega.block.finance.business.InvoiceDataInsert#insertData(int,
		 *      int)
		 */

	public void createClaimsInBank(int batchNumber) {
		for (int i = 0; i < 200; i++) {
			emptyString += " ";
			zeroString += "0";
		}
		// BankFileManager bfm = new BankInvoiceFileManager();
		StringBuffer buffer = new StringBuffer();

		String claimantSSN = "6812933379";// bfm.getClaimantSSN(groupId);
		if (claimantSSN != null && !claimantSSN.equals("")) {
			if (claimantSSN.length() < 10)
				claimantSSN = zeroString
						.substring(0, 10 - claimantSSN.length())
						+ claimantSSN;
		} else {
			claimantSSN = zeroString.substring(0, 10);
		}

		String bankNr = "1135";// bfm.getBankBranchNumber(groupId);
		if (bankNr != null && !bankNr.equals("")) {
			if (bankNr.length() < 4)
				bankNr = zeroString.substring(0, 4 - bankNr.length()) + bankNr;
		} else {
			bankNr = zeroString.substring(0, 4);
		}

		String claimantName = "fri";// bfm.getClaimantName(groupId);
		if (claimantName != null && !claimantName.equals("")) {
			if (claimantName.length() < 8)
				claimantName = claimantName
						+ emptyString.substring(0, 8 - claimantName.length());
		} else {
			claimantName = emptyString.substring(0, 8);
		}

		String claimantAccountId = "007";// bfm.getClaimantsAccountId(groupId);
		if (claimantAccountId != null && !claimantAccountId.equals("")) {
			if (claimantAccountId.length() < 3)
				claimantAccountId = zeroString.substring(0,
						3 - claimantAccountId.length());
		} else {
			claimantAccountId = zeroString.substring(0, 3);
		}

		// start the header string: "hausfaersla"
		/* Kt_krofuhafa - 10 */
		buffer.append(claimantSSN);// "5709902259"
		/* Utgafa - 4 */
		buffer.append("0140");// the version number of the data sending to RB
		/* Faerslugerd - 1 */
		buffer.append("H");// the type of the first line is: "hausfaersla" (H)
		/* Keyrsludagur - 8 */
		buffer.append(new IWTimestamp(Calendar.getInstance().getTime())
				.getDateString("yyyyMMdd"));// the
		// run
		// date
		// -
		// today!
		/* Verkefni - 2 */
		buffer.append("IK");// a code for "verkefni"
		/* autt - 8 */
		buffer.append(emptyString.substring(0, 8));// 8 empty spaces
		/* Gjaldkeranumer - 4 */
		buffer.append("SP01");// a code for "gjaldkeranumer"
		/* autt - 7 */
		buffer.append(emptyString.substring(0, 7));
		/* Iban - 4 */
		buffer.append(bankNr);// this is the number of the bank that sends the
		// claims
		/* Skjar - 8 */
		buffer.append(claimantName);
		/* autt - 50 */
		buffer.append(emptyString.substring(0, 50));
		buffer.append("\n");
		// "hausfaersla" ends

		Integer[] krofuNumer = new Integer[1];// bfm.getInvoiceNumbers(batchNumber);//
		// new
		// Integer[2];
		krofuNumer[0] = new Integer(1);
		// krofuNumer[1] = new Integer(2);
		// krofuNumer[2] = new Integer(3);

		String[] payersSSNArray = new String[1];

		// payersSSNArray[0] = new String("0405563469");
		payersSSNArray[0] = new String("0610703899");
		// payersSSNArray[2] = new String("2012643759");

		String numberOfClaims = String.valueOf(krofuNumer.length);
		if (numberOfClaims.length() < 6)
			numberOfClaims = zeroString.substring(0, 6 - numberOfClaims
					.length())
					+ numberOfClaims;

		int totalAmount = 0;
		String amountString = "10000";

		for (int i = 0; i < krofuNumer.length; i++) {
			int number = krofuNumer[i].intValue();

			totalAmount += Integer.valueOf(amountString).intValue(); // bfm.getAmount(number)

			String payersSSN = payersSSNArray[i]; // bfm.getPayersSSN(number);
			if (payersSSN != null && !payersSSN.equals("")) {
				if (payersSSN.length() < 10)
					payersSSN = zeroString
							.substring(0, 10 - payersSSN.length())
							+ payersSSN;
			} else {
				payersSSN = zeroString.substring(0, 10);
			}

			String numberString = String.valueOf(number);
			if (numberString.length() < 6)
				numberString = zeroString.substring(0, 6 - numberString
						.length())
						+ numberString;

			String amount = amountString;// bfm.getAmount(number);
			if (amount != null && !amount.equals("")) {
				if (amount.length() < 11)
					amount = zeroString.substring(0, 11 - amount.length())
							+ amount;
			}

			String batchString = String.valueOf(batchNumber);// String.valueOf(12);
			if (batchString.length() < 16)
				batchString = batchString
						+ zeroString.substring(0, 16 - batchString.length());

			String noteNumber = null; // bfm.getNoteNumber(number);
			if (noteNumber != null && !noteNumber.equals("")) {
				if (noteNumber.length() < 7)
					noteNumber = zeroString.substring(0, 7 - noteNumber
							.length())
							+ noteNumber;
			} else {
				noteNumber = zeroString.substring(0, 7);
			}

			/* Kt_krofuhafa - 10 */
			buffer.append(claimantSSN);// "5709902259"
			/* Utgafa - 4 */
			buffer.append("0140");// the version number of the data sending to
			// RB
			/* Faerslugerd - 1 */
			buffer.append("K");// the type of the second line is: "stofnun a
			// krofum" (K)
			/* Gjalddagi - 8 */
			// buffer.append(new
			// IWTimestamp(bfm.getDueDate(number).getTime()).getDateString("yyyyMMdd"));//
			// "20040801"
			buffer.append("20051101");// "20040801"
			// Calendar nidurfellingardagur = bfm.getDueDate(number);
			// nidurfellingardagur.add(Calendar.YEAR, 4);
			/* Nidurfellingardagur - 8 */
			buffer.append("20091101");// "20050801"
			/* Audkenni (Visir) - 3 */
			buffer.append(claimantAccountId);// "0IH"
			/* Kt_greidanda - 10 */
			buffer.append(payersSSN);// "5904002970"
			/* Bankanumer - 4 */
			buffer.append(bankNr);// "1158"
			/* Hofudbok - 2 */
			buffer.append("66");// "66"
			/* Numer - 6 */
			buffer.append(numberString);// "10028" + i
			/* Upphaed - 11 */
			buffer.append(amount);// "00000000100"
			/* Tilvisun - 16 */
			buffer.append(batchString);// "5904002970000000"
			/* Sedilnumer - 7 */
			buffer.append(noteNumber);// "0000000"
			/* Vidskiptanumer - 16 */
			buffer.append(emptyString.substring(0, 6) + payersSSN);// "5904002970"
			/* Eindagi - 8 */
			// buffer.append(new
			// IWTimestamp(bfm.getFinalDueDate(number).getTime()).getDateString("yyyyMMdd"));//
			// "20040815"
			buffer.append("20051131");// "20040815"

			/* information not needed for now */
			/* Tilkynningar- og greidslugjald 1 - 11 */
			buffer.append("00000000000");// bfm.getNotificationAndPaymentFee1(number));
			/* Tilkynningar- og greidslugjald 2 - 11 */
			buffer.append("00000000000");// bfm.getNotificationAndPaymentFee2(number));
			/* Fyrra vanskilagjald - 11 */
			buffer.append("00000000000");
			/* Seinna vanskilagjald - 11 */
			buffer.append("00000000000");
			/* Dagafjoldi fyrra vanskilagjalds - 2 */
			buffer.append("00");
			/* Dagafjoldi seinna vanskilagjalds - 2 */
			buffer.append("00");
			/* Vanskilagjaldskodi - 1 */
			buffer.append(" ");
			/* Annar kostnadur - 11 */
			buffer.append("00000000000");// bfm.getOtherCost(number));
			/* Annar vanskilakostnadur - 11 */
			buffer.append("00000000000");// bfm.getOtherOverdueCost(number));
			/* Drattarvaxtaprosenta - 7 */
			buffer.append("0000000");
			/* Drattarvaxtaregla - 1 */
			buffer.append("0");
			/* Drattarvaxtastofnskodi - 1 */
			buffer.append("0");
			/* Gengistegund - 1 */
			buffer.append("0");
			/* Mynt - 3 */
			buffer.append("000");
			/* Gengisbanki - 2 */
			buffer.append("00");
			/* Gengiskodi - 1 */
			buffer.append("0");
			// Autt
			buffer.append(" ");
			/* Greidslukodi - 1 */
			buffer.append(" ");
			/* Fyrri afslattur - 11 */
			buffer.append("00000000000");
			/* Seinni afslattur - 11 */
			buffer.append("00000000000");
			/* Dagafjoldi fyrri afslattar - 2 */
			buffer.append("00");
			/* Dagafjoldi seinni afslattar - 2 */
			buffer.append("00");
			/* Afslattarkodi - 1 */
			buffer.append(" ");
			/* Innborgunarkodi - 1 */
			buffer.append("1");
			/* Birtingarkodi - 1 */
			buffer.append("1");
			/* URL - 200 */
			buffer.append(emptyString);
			buffer.append("\n");

		}

		String totalString = String.valueOf(totalAmount);
		if (totalString.length() < 15)
			totalString = zeroString.substring(0, 15 - totalString.length())
					+ totalString;

		// "lokafaersla" begins
		/* Kt_krofuhafa - 10 */
		buffer.append(claimantSSN);// "5709902259"
		/* autt - 4 */
		buffer.append(emptyString.substring(0, 4));
		/* Faerslugerd - 1 */
		buffer.append("L");// the type of the last line is: "lokafaersla" (L)
		/* autt - 31 */
		buffer.append(emptyString.substring(0, 31));
		/* Fjoldi - 6 */
		buffer.append(numberOfClaims);
		/* Upphaed 15 */
		buffer.append(totalString);
		/* autt - 39 */
		buffer.append(emptyString.substring(0, 39));
		// "lokafaersla" ends

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(
					FILE_NAME, false));
			writer.write(buffer.toString());
			writer.close();

			BufferedReader reader = new BufferedReader(
					new FileReader(FILE_NAME));
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		sendCreateClaimsRequest();
	}

	public void getClaimStatusFromBank(int batchNumber, int groupId,
			java.util.Date from, java.util.Date to) {
		BankFileManager bfm = new BankInvoiceFileManager(groupId);
		String response = sendGetClaimStatusRequest(bfm, groupId,
				new IWTimestamp(from).getDateString("yyyyMMdd"),
				new IWTimestamp(to).getDateString("yyyyMMdd"));
		String number = response.substring(6, 12);
		String faerslugerd = response.substring(20, 21);

		bfm.setInvoiceStatus(faerslugerd, Integer.parseInt(number));
	}

	public void deleteClaim(int groupId, int claimNumber,
			java.util.Date dueDate, String payersSSN) {

	}

	private MultipartPostMethod sendCreateClaimsRequest() {
		/*
		 * HttpClient client = new HttpClient(); client.setStrictMode(false);
		 * MultipartPostMethod post = new MultipartPostMethod(SITE +
		 * POST_METHOD); File file = new File(FILE_NAME);
		 * 
		 * try { post.addParameter("notendanafn", "lolo7452");// "aistest");
		 * post.addParameter("password", "12345felix");
		 * post.addParameter("KtFelags", "6812933379");// "5709902259");
		 * post.addParameter("Skra", file);
		 * 
		 * post.setDoAuthentication(false); client.executeMethod(post);
		 * 
		 * System.out.println("responseString: " +
		 * post.getResponseBodyAsString()); } catch (FileNotFoundException e1) {
		 * e1.printStackTrace(); } catch (IOException e2) {
		 * e2.printStackTrace(); } finally { post.releaseConnection(); } return
		 * post;
		 */

		PostMethod filePost = new PostMethod(SITE + POST_METHOD);
		File file = new File(FILE_NAME);
		
		filePost.getParams().setBooleanParameter(
				HttpMethodParams.USE_EXPECT_CONTINUE, true);

		try {
			StringPart userPart = new StringPart("notendanafn", "lolo7452");
			StringPart pwdPart = new StringPart("password", "12345felix");
			StringPart clubssnPart = new StringPart("KtFelags", "6812933379");
			FilePart filePart = new FilePart("Skra", file);
			
			Part[] parts = { userPart, pwdPart, clubssnPart, filePart };
			filePost.setRequestEntity(new MultipartRequestEntity(parts,
					filePost.getParams()));
			HttpClient client = new HttpClient();
			client.getHttpConnectionManager().getParams().setConnectionTimeout(
					5000);

			int status = client.executeMethod(filePost);
			if (status == HttpStatus.SC_OK) {
				System.out.println("Upload complete, response="
						+ filePost.getResponseBodyAsString());
			} else {
				System.out.println("Upload failed, response="
						+ HttpStatus.getStatusText(status));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			filePost.releaseConnection();
		}

		return null;
		
		/*
		 * PostMethod authpost = new PostMethod(SITE + POST_METHOD);
		 * 
		 * authpost.getParams().setBooleanParameter(HttpMethodParams.USE_EXPECT_CONTINUE,
		 * true); try {
		 * 
		 * HttpClient client = new HttpClient();
		 * client.getHttpConnectionManager().getParams().setConnectionTimeout(
		 * 5000);
		 * 
		 * File file = new File(FILE_NAME); // Prepare login parameters
		 * NameValuePair inputFile = new NameValuePair("xmldata", file);
		 * authpost.setRequestBody(new NameValuePair[] { inputFile });
		 * 
		 * int status = client.executeMethod(authpost); System.out.println("Form
		 * post: " + authpost.getStatusLine().toString()); if (status ==
		 * HttpStatus.SC_OK) { System.out.println("Submit complete, response=" +
		 * authpost.getResponseBodyAsString()); } else {
		 * System.out.println("Submit failed, response=" +
		 * HttpStatus.getStatusText(status)); } } catch (Exception ex) {
		 * ex.printStackTrace(); } finally { authpost.releaseConnection(); }
		 */

	}

	/**
	 * Sends a http post method to Sparisj to get the status of claims
	 * 
	 * @param bfm
	 * @param groupId
	 * @return
	 */
	private String sendGetClaimStatusRequest(BankFileManager bfm, int groupId,
			String fromDate, String toDate) {
		/*
		 * Protocol easyhttps = new Protocol("https", new
		 * EasySSLProtocolSocketFactory(), 443);
		 */

		HttpClient client = new HttpClient();
		// client.getHostConfiguration().setHost(SITE, 443, easyhttps);
		PostMethod post = new PostMethod(SITE + POST_ANSWER_METHOD);

		String response = new String();
		try {
			post.addParameter("notendanafn", bfm.getUsername());
			post.addParameter("password", bfm.getPassword());
			post.addParameter("KtFelags", bfm.getClaimantSSN());
			post.addParameter("Reikningsnr", "");
			String claimantAccountId = bfm.getClaimantsAccountId();
			if (claimantAccountId != null && !claimantAccountId.equals("")) {
				if (claimantAccountId.length() < 3)
					claimantAccountId = zeroString.substring(0,
							3 - claimantAccountId.length());
			} else {
				claimantAccountId = zeroString.substring(0, 3);
			}
			post.addParameter("Audkenni", claimantAccountId);
			post.addParameter("DagsFra", fromDate);// "20030701"
			post.addParameter("DagsTil", toDate);// "20040823"
			/*
			 * Snidmat can be: 230 - print out 240 - Excel 250 - booking file 62
			 * 251 - booking file 66 252 - XML
			 */
			post.addParameter("Snidmat", "230");
			client.executeMethod(post);

			response = post.getResponseBodyAsString();

		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			post.releaseConnection();
		}
		// System.out.println("response: " + response);
		return response;
	}
}