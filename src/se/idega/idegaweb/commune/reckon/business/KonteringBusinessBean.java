/*
 * Created on 10.7.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package se.idega.idegaweb.commune.reckon.business;

import java.rmi.RemoteException;
import java.sql.Date;
import java.util.ArrayList;
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
	 * 
	 * @param first
	 * @param second
	 * @param date
	 * @return
	 * @throws RemoteException
	 */
	public String generateString(String first, String second, Date date) throws RemoteException {
		StringBuffer ret = new StringBuffer();
		String temp;
		
		KonteringStringHome ksHome = getKonteringStringHome();
		KonteringFieldHome kfHome = getKonteringFieldHome();
		try {
			KonteringString kontering = ksHome.findKonteringStringByDate(date);
			ArrayList list = (ArrayList)kfHome.findAllFieldsByKonteringString(Integer.parseInt(kontering.getPrimaryKey().toString()));
			Iterator iter = list.iterator();
			while (iter.hasNext())
			{
				KonteringField field = (KonteringField)iter.next();
				temp = getField(first.substring(0,field.getLength()),field);
				if(temp.length()==0)
				{
					temp = getField(second.substring(0,field.getLength()),field);
				}
				temp = pad(temp,field);
				ret.append(temp);
			}
		} catch (Exception e1) {
			// Todo JJ Throw own exception
			System.out.println("Error: No definition for the contering string found for the date: "+date.toString());
			e1.printStackTrace();
		}
		
		return ret.toString();
	}
	
	/**
	 * 
	 * @param in
	 * @param kf
	 * @return
	 */
	private String pad(String in, KonteringField kf){
		StringBuffer ret = new StringBuffer(in);
		if(kf.getJustification() == JUSTIFY_LEFT) {
			while(ret.length()<kf.getLength()){
				ret.append(kf.getPadChar());
			}
		} else {
			while(ret.length()<kf.getLength()){
				ret.insert(0,kf.getPadChar());
			}
		}
		return ret.toString();
	}
	
	/**
	 * 
	 * @param in
	 * @param kf
	 * @return
	 */
	private String getField(String in, KonteringField kf) {
		//TODO JJ catch index out of bounds!!!
		String ret = "";
		int i;
		if(kf.getJustification() == JUSTIFY_LEFT){
			for(i=kf.getLength();i>0 && in.charAt(i) == kf.getPadChar();i++){}
			ret = in.substring(0,i);
		}else{
			for(i=0;i<kf.getLength() && in.charAt(i) == kf.getPadChar();i++){}
			ret = in.substring(i,kf.getLength());
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
