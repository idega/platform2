package is.idega.idegaweb.marathon.business;

import is.idega.idegaweb.marathon.util.IWMarathonConstants;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import com.idega.block.importer.data.ImportFile;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.business.IBOServiceBean;
import com.idega.core.localisation.business.LocaleSwitcher;
import com.idega.core.location.data.Country;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.user.data.Group;
import com.idega.util.IWTimestamp;
import com.idega.util.LocaleUtil;
import com.idega.util.text.TextSoap;


/**
 * @author gimmi
 */
public class MarathonFileImportHandlerBean extends IBOServiceBean  implements MarathonFileImportHandler{

	ImportFile file;
	Collection countries;
	RunBusiness business;
	Locale englishLocale;
	Locale icelandicLocale;
	
	public static void main(String[] args) throws RemoteException {
		/*File fFile = new File("/Users/gimmi/Desktop/Marathonhlauparar.csv");
		ImportFile ifile = new ColumnSeparatedImportFile(fFile);
		
		MarathonFileImportHandlerBean m = new MarathonFileImportHandlerBean();
		m.setImportFile(ifile);
		m.handleRecords();*/
	}
	
	public boolean handleRecords() throws RemoteException {
		business = getBusiness(getIWApplicationContext());
		countries = business.getCountries();
		englishLocale = LocaleUtil.getLocale(LocaleSwitcher.englishParameterString);
		icelandicLocale = LocaleUtil.getLocale(LocaleSwitcher.icelandicParameterString);
		
		Vector errors = new Vector();
		if (file != null) {
			String line = (String) file.getNextRecord();
			int counter = 1;
			while (line != null && !"".equals(line)) {
				++counter;
				if (counter % 100 == 0) {
					System.out.println("Counter = "+counter);
				}
				if (!handleLine(line)) {
					errors.add(line);
				}
				line = (String) file.getNextRecord();
			}
			System.out.println(counter);
			
		}
		
		if (!errors.isEmpty()) {
			System.out.println("Errors in the following lines :");
			Iterator iter = errors.iterator();
			while (iter.hasNext()) {
				System.out.println((String) iter.next());
			}
		}
		return true;
	}
	
	private boolean handleLine(String line) {
		ArrayList values = file.getValuesFromRecordString(line);
		int size = values.size();
		boolean validLine = true;
		
		if (size == 32) {
			String nr = (String) values.get(0);
			String nafn = (String) values.get(1);
			String kt = (String) values.get(2);
			String hfang = (String) values.get(3);
			String sveitarfelag = (String) values.get(4);
			String pnr = (String) values.get(5);
			String simi = (String) values.get(6);
			String netfang = (String) values.get(7);
			String thoderni = (String) values.get(8);
			String kyn = (String) values.get(9);
			String visa = (String) values.get(10);
			String gildir = (String) values.get(11);
			String cardType = (String) values.get(12);
			String grein = (String) values.get(13);
			String vegalengd = (String) values.get(14);
			String bolur = (String) values.get(15);
			String sveitakeppni = (String) values.get(16);
			String sveit = (String) values.get(17);
			String dv = (String) values.get(18);
			String ar = (String) values.get(19);
			String eftirnafn = (String) values.get(20);
			String timi = (String) values.get(21);
			String gjald = (String) values.get(22);
			String chip = (String) values.get(23);
			String best = (String) values.get(24);
			String goal = (String) values.get(25);
			String before = (String) values.get(26);
			String others = (String) values.get(27);
			String stay = (String) values.get(28);
			String many = (String) values.get(29);
			String why = (String) values.get(30);
			String aware = (String) values.get(31);
			
			
			try {
				Integer.parseInt(nr);
			} catch (NumberFormatException n) {
				System.out.println("not using line... starts with : " +nr);
				validLine = false;
			}
			
			if (validLine) {
				String countryPK = null;
				
				Iterator iter = countries.iterator();
				Country country = null;
				boolean found = false;
				while (iter.hasNext() && !found) {
					country = (Country) iter.next();
					found = thoderni.equals(country.getIsoAbbreviation());
				}
				if (!found) {
					System.out.println("Country not found for code = "+thoderni);
				} else {
					countryPK = country.getPrimaryKey().toString();
				}
				
				kt = kt.replaceAll(" ", "");
				kt = kt.replaceAll("-", "");
				kt = kt.replaceAll("/", "");
				kt = TextSoap.findAndCut(kt, ".");
				kt = TextSoap.findAndCut(kt, ",");
				kt = kt.replaceAll("jan", "01");
				kt = kt.replaceAll("feb", "02");
				kt = kt.replaceAll("mar", "03");
				kt = kt.replaceAll("apr", "04");
				kt = kt.replaceAll("ma’", "05");
				kt = kt.replaceAll("jœn", "06");
				kt = kt.replaceAll("jœl", "07");
				kt = kt.replaceAll("‡gœ", "08");
				kt = kt.replaceAll("sep", "09");
				kt = kt.replaceAll("okt", "10");
				kt = kt.replaceAll("n—v", "11");
				kt = kt.replaceAll("des", "12");
				
				
				IWTimestamp birth = null;
				String gender = null;
				Locale locale = null;
				try {
					Long.parseLong(kt);
					if (kt.length() == 6) {
						kt = kt.substring(0, 4) + "19" +kt.substring(4, 6);
					}
					
					if (kt.length() == 8) {
						if (Integer.parseInt(kt.substring(4, 8))< 1900) {
							System.out.println("Check "+nr);
						}
						birth = new IWTimestamp(kt.substring(4,8)+"-"+kt.substring(2, 4)+"-"+kt.substring(0, 2));
						locale = englishLocale;
					} else if (kt.length() == 10) {
						birth = new IWTimestamp("19"+kt.substring(4,6)+"-"+kt.substring(2, 4)+"-"+kt.substring(0, 2));
						locale = icelandicLocale;
					} else {
						System.out.println("Error in kt length ("+kt.length()+") : "+kt+", "+nr);
						validLine = false;
					}
					//System.out.println(birth.toSQLString());
				} catch (Exception e) {
					validLine = false;
					System.out.println("Error in kt : "+kt+", "+nr);
				}

				if ("kven".equalsIgnoreCase(kyn)) {
					gender = "2";
				} else {
					gender = "1";
				}
				
				
				int runID = 4;
				int yearID = 73;
				int distanceID = -1;
				
				if (grein.equals("Marathon")) {
					distanceID = 74;
				} else if (grein.equals("halfmarathon")) {
					distanceID = 87;
				} else if (grein.equals("10km")) {
					distanceID = 100;
				} else if (grein.equals("7km")) {
					distanceID = 113;
				} else if (grein.equals("3km")) {
					distanceID = 126;
				} else {
					System.out.println("DistanceID not found : "+grein+" : "+nr);
				}
				
				/*
				 * 
				 10 k’l—metra hlaup
				 Maraßon
				 H‡lfmaraßon
				 7 k’l—metra skemmtiskokk
				 3 k’l—metra skemmtiskokk
				yearID = 73
				3 km = 126
				7 km = 113
				10km = 100
				21km = 87
				42km = 74
				*/
				
				/*
				prm_sm
				prm_
				*/
				if ("S".equals(bolur)) {
					bolur = IWMarathonConstants.PARAMETER_TSHIRT_S;
				} else if ("M".equals(bolur)) {
					bolur = IWMarathonConstants.PARAMETER_TSHIRT_M;
				} else if ("L".equals(bolur)) {
					bolur = IWMarathonConstants.PARAMETER_TSHIRT_L;
				} else if ("XL".equals(bolur)) {
					bolur = IWMarathonConstants.PARAMETER_TSHIRT_XL;
				} else if ("XXL".equals(bolur)) {
					bolur = IWMarathonConstants.PARAMETER_TSHIRT_XXL;
				} else {
					System.out.println("Unknown tshirt size : "+bolur+" ("+nr+")");
				}
				
				if (chip.length() < 5) {
					chip = "";
				}
				

				try {
					if (validLine) {
						int userID = business.saveUser(nafn, kt, birth, gender, hfang, pnr, sveitarfelag, countryPK, simi, "", netfang);
						business.saveRun(userID, "4", Integer.toString(distanceID), "73", thoderni, bolur, chip, sveit, best, goal, locale);
						return true;
					}
				}
				catch (RemoteException e1) {
					e1.printStackTrace();
					return false;
				}
			}
		} else {
			System.out.println("Too few fields !!!");
		}
		return false;
	}
	

	public void setImportFile(ImportFile file) throws RemoteException {
		this.file = file;
	}

	public void setRootGroup(Group rootGroup) throws RemoteException {
	}

	public List getFailedRecords() throws RemoteException {
		return null;
	}
	
	protected RunBusiness getBusiness(IWApplicationContext iwac) {
		try {
			return (RunBusiness) IBOLookup.getServiceInstance(iwac, RunBusiness.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}
}
