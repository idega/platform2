/*
 * Created on 10.7.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package se.idega.idegaweb.commune.reckon.business;

import java.rmi.RemoteException;
import java.sql.Date;
import java.util.Collection;
import java.util.Iterator;

import se.idega.idegaweb.commune.reckon.data.KonteringField;
import se.idega.idegaweb.commune.reckon.data.KonteringFieldHome;
import se.idega.idegaweb.commune.reckon.data.KonteringString;
import se.idega.idegaweb.commune.reckon.data.KonteringStringHome;

/**
 * @author Joakim
 *
 * 
 * 
 */
public class KonteringBusinessBean {
	
	public static final int JUSTIFY_LEFT = 0;
	public static final int JUSTIFY_RIGHT = 1;

	/**
	 * Merges two kontering strings according to 15.2 and 15.3 in the Kravspecification Check & Peng
	 * @param first kontering string
	 * @param second kontering string
	 * @param date for valid time of kontering rules
	 * @return the merged kontering strings
	 * @throws RemoteException
	 */
	public String generateString(String first, String second, Date date) throws RemoteException {
		StringBuffer ret = new StringBuffer();		//used to build together returnstring
		String temp;
		int readPointer = 0;						//Pointer to place where next field is
		int fieldLength=0;							//Length of next field. Fetched from the definition 

		KonteringStringHome ksHome = getKonteringStringHome();
		KonteringFieldHome kfHome = getKonteringFieldHome();
		try {
			KonteringString kontering = ksHome.findKonteringStringByDate(date);
			Collection list = kfHome.findAllFieldsByKonteringString(Integer.parseInt(kontering.getPrimaryKey().toString()));
			Iterator iter = list.iterator();
			while (iter.hasNext())
			{
				KonteringField field = (KonteringField)iter.next();
				fieldLength = field.getLen();
				temp = trim(first.substring(readPointer,readPointer+fieldLength),field);
				if(temp.length()==0)
				{
					temp = trim(second.substring(readPointer,readPointer+fieldLength),field);
				}
				temp = pad(temp,field);
				ret.append(temp);
				readPointer += fieldLength;
			}
			if(readPointer != first.length()){
				System.out.println("Error: Wrong length of the string used for kontering. Expected: "+readPointer+
				"  Actual: "+first.length());
			}
		} catch (Exception e) {
			// Todo JJ Throw own exception
			System.out.println("Error: The konteringt definition and the kontering strings did not match.");
			System.out.println("First Kontering string: '"+first+"'");
			System.out.println("Second Kontering string:'"+second+"'");
			System.out.println("Date for kontering rule: "+date.toString());
			e.printStackTrace();
		}
		return ret.toString();
	}
	
	/**
	 * Pads the string according to the rules in Kontering field
	 * @param in the string to pad
	 * @param konteringField holds the rules for padding
	 * @return the padded string
	 */
	private String pad(String in, KonteringField konteringField){
		StringBuffer ret = new StringBuffer(in);
		//Add the padding character on right side of the string until it is of right size
		if(konteringField.getJustification() == JUSTIFY_LEFT) {
			while(ret.length()<konteringField.getLen()){
				ret.append(konteringField.getPadChar());
			}
		} else {
			while(ret.length()<konteringField.getLen()){
				ret.insert(0,konteringField.getPadChar());
			}
		}
		return ret.toString();
	}
	
	/**
	 * Trims the string according to the rules in Kontering field
	 * @param in the string to trim
	 * @param konteringField holds the rules for trimming 
	 * @return the trimmed string
	 */
	private String trim(String in, KonteringField konteringField) {
		String ret = "";
		int i;
		//Remove all padding characters until a non padding character is encountered
		if(konteringField.getJustification() == JUSTIFY_LEFT){
			for(i=konteringField.getLen();i>0 && in.charAt(i) == konteringField.getPadChar();i++){}
			ret = in.substring(0,i);
		}else{
			for(i=0;i<konteringField.getLen() && in.charAt(i) == konteringField.getPadChar();i++){}
			ret = in.substring(i,konteringField.getLen());
		}
		return ret;
	}
	
	protected KonteringStringHome getKonteringStringHome() throws RemoteException
	{
		return (KonteringStringHome) com.idega.data.IDOLookup.getHome(KonteringString.class);
	}
	
	protected KonteringFieldHome getKonteringFieldHome() throws RemoteException
	{
		return (KonteringFieldHome) com.idega.data.IDOLookup.getHome(KonteringField.class);
	}
}
