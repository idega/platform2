/*
 * $Id: Report.java,v 1.2 2003/07/01 14:07:24 gummi Exp $
 *
 * Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package se.idega.util;

import java.io.ByteArrayInputStream;
import java.sql.SQLException;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.core.data.ICFile;
import com.idega.core.data.ICFileBMPBean;
import com.idega.core.data.ICFileHome;
import com.idega.data.IDOLookupException;

/**
 * A class used for creating report files. Currently used in the file import functions
 * 
 * @author <a href="joakim@idega.is">Joakim Johnson</a>
 * @version 1.0
 */
public class Report
{
	private StringBuffer text;
//	private ImportFile file;
	private String filename;
	
	public Report(String fn)
	{
		filename = fn;
		text = new StringBuffer();
	}
	
	public void init()
	{
		text = new StringBuffer();
	}
	
	public void append(String s)
	{
		text.append(s+'\n');
	}
	
	public void store()
	{
		System.out.println("\n**REPORT**\n\n" + text + "\n**END OF REPORT**\n\n");
		//Creating the report file in the DB filesystem.
		System.out.println("Attempting to access the reports folder");
		ICFile reportFolder = null;
		ICFileHome fileHome;
		try {
			fileHome = (ICFileHome) com.idega.data.IDOLookup.getHome(ICFile.class);
			try {
				reportFolder = fileHome.findByFileName("Reports");
				System.out.println("Reports folder found");
			} catch (FinderException e) {
				System.out.println("Reports folder not found, attempting to create folder");
				try {
					ICFile root = fileHome.findByFileName(ICFileBMPBean.IC_ROOT_FOLDER_NAME);
					System.out.println("Rootfolder found");
					reportFolder = fileHome.create();
					reportFolder.setName("Reports");
					reportFolder.setMimeType("application/vnd.iw-folder");
					reportFolder.store();
					root.addChild(reportFolder);
					System.out.println("Reports folder created");
				} catch (FinderException e1) {
					System.out.println("Error creating Reports folder.");
				}
			}
		} catch (SQLException e) {
			System.out.println("Error creating Reports folder.");
		} catch (CreateException e) {
			System.out.println("Error creating Reports folder.");
		} catch (IDOLookupException e2) {
			System.out.println("Error creating Reports folder.");
		}
			
		ICFile reportFile;
		try {
			reportFile = ((com.idega.core.data.ICFileHome)com.idega.data.IDOLookup.getHome(ICFile.class)).create();
			byte[] bytes = text.toString().getBytes();

			ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
			reportFile.setFileValue(bais);
			reportFile.setMimeType("text/plain");
			//Todo (jj) Have to find the name of the importfile, and add that here.
//			String filename = file.getFile().getName();
			int i = filename.indexOf('_');
			if(i>0)
			{
				filename = filename.substring(i+1);
			}
			i = filename.lastIndexOf('.');
			if(i>0)
			{
				filename = filename.substring(0,i);
			}
			reportFile.setName(filename+".report");
			reportFile.setFileSize(text.length());
			reportFile.store();
			if(reportFolder!=null)
			{
				reportFolder.addChild(reportFile);
				System.out.println("Report added to folder.");
			}
		}
		catch (SQLException ex) {
		  ex.printStackTrace();
		} catch (IDOLookupException e) {
			e.printStackTrace();
		} catch (CreateException e) {
			e.printStackTrace();
		}
	}
}