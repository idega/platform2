package com.idega.block.importer.business;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import com.idega.block.importer.data.ImportFile;
import com.idega.block.importer.data.ImportFileClass;
import com.idega.block.importer.data.ImportFileClassHome;
import com.idega.block.importer.data.ImportHandler;
import com.idega.block.importer.data.ImportHandlerHome;
import com.idega.business.IBOServiceBean;
import com.idega.business.IBOSession;
import com.idega.core.file.data.ICFile;
import com.idega.core.file.data.ICFileHome;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWUserContext;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.user.business.GroupBusiness;
import com.idega.util.IWTimestamp;
import com.idega.util.text.TextSoap;


/**
 * <p>Title: IdegaWeb classes</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Idega Software</p>
 * @author <a href="mailto:eiki@idega.is"> Eirikur Sveinn Hrafnsson</a>
 * @version 1.0
 */

public class ImportBusinessBean extends IBOServiceBean implements ImportBusiness{
	
	public ImportBusinessBean() {
	}
	
	/**
	 * @see com.idega.block.importer.business.ImportBusiness#getImportHandlers()
	 */
	public Collection getImportHandlers() throws RemoteException{
		
		Collection col = null;
		try {
			col = ((ImportHandlerHome) this.getIDOHome(ImportHandler.class)).findAllImportHandlers();
		}
		catch (FinderException e) {
		}
		
		return  col;
	}
	
	/**
	 * @see com.idega.block.importer.business.ImportBusiness#getImportFileTypes()
	 */
	public Collection getImportFileTypes() throws RemoteException {
		Collection col = null;
		try {
			col = ((ImportFileClassHome) this.getIDOHome(ImportFileClass.class)).findAllImportFileClasses();
			
		}
		catch (FinderException e) {
		}
		
		return  col;
	}
	
	
	
	/**
	 * @see com.idega.block.importer.business.ImportBusiness#importRecords(String, String, String, Integer)
	 */
	public boolean importRecords(String handlerClass,String fileClass,String filePath,Integer groupId, IWUserContext iwuc, List failedRecords)throws RemoteException {
		try{
			boolean status = false;
			
			ImportFileHandler handler = this.getImportFileHandler(handlerClass,iwuc);
			ImportFile file = this.getImportFile(fileClass);
			
			file.setFile(new File(filePath));
			
			handler.setImportFile(file);
			handler.setRootGroup(getGroupBusiness().getGroupByGroupID(groupId.intValue()));
			
			status = handler.handleRecords();
			failedRecords.addAll(handler.getFailedRecords());
			return status;
		}
		catch(NoRecordsException ex){
			ex.printStackTrace();
			return false;
		}
		catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
	}
	
	/**
	 * @see com.idega.block.importer.business.ImportBusiness#importRecords(String, String, String)
	 */
	public boolean importRecords(String handlerClass,String fileClass,String filePath, IWUserContext iwuc)throws RemoteException {
		try{
			boolean status = false;
			ImportFileHandler handler = this.getImportFileHandler(handlerClass,iwuc);
			ImportFile file = this.getImportFile(fileClass);
			
			file.setFile(new File(filePath));
			
			handler.setImportFile(file);
			
			status = handler.handleRecords();
			
			return status;
		}
		catch(NoRecordsException ex){
			ex.printStackTrace();
			return false;
		}
		catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
	}
	
	public GroupBusiness getGroupBusiness() throws Exception{
		return (GroupBusiness) this.getServiceInstance(GroupBusiness.class);
	}
	
	public ImportFileHandler getImportFileHandler(String handlerClass, IWUserContext iwuc) throws Exception{
		Class importHandlerInterfaceClass = Class.forName(handlerClass);
		Class[] interfaces = importHandlerInterfaceClass.getInterfaces();
		boolean isSessionBean = false;
		for (int i = 0; i < interfaces.length; i++) {
			Class class1 = interfaces[i];
			if( class1.equals(IBOSession.class)){
				isSessionBean = true;
				break;
			}				
		}
		
		ImportFileHandler handler;
		
		if( isSessionBean ){
			handler = (ImportFileHandler)  getSessionInstance(iwuc,importHandlerInterfaceClass);
		}
		else{
			handler = (ImportFileHandler)  getServiceInstance(importHandlerInterfaceClass);
		}
		
		return handler;
	}
	
	public ImportFile getImportFile(String fileClass) throws Exception{
		return (ImportFile)Class.forName(fileClass).newInstance();
	}
	
	public DropdownMenu getImportHandlers(IWContext iwc, String name) throws RemoteException {
		DropdownMenu menu = new DropdownMenu(name);
		Collection col = getImportHandlers();
		Iterator iter = col.iterator();
		while (iter.hasNext()) {
			ImportHandler element = (ImportHandler) iter.next();
			menu.addMenuElement(element.getClassName(), element.getName());
		}
		return menu;
	}
	public DropdownMenu getImportFileClasses(IWContext iwc, String name) throws RemoteException {
		DropdownMenu menu = new DropdownMenu(name);
		Collection col = getImportFileTypes();
		Iterator iter = col.iterator();
		while (iter.hasNext()) {
			ImportFileClass element = (ImportFileClass) iter.next();
			menu.addMenuElement(element.getClassName(), element.getName());
		}
		return menu;
	}
	
	public ICFile getReportFolder(String importFileName, boolean createIfNotFound) throws RemoteException, CreateException {
		String reportFilename = importFileName;
		int i = reportFilename.indexOf('_');
		if(i>0)
		{
			reportFilename = reportFilename.substring(i+1);
		}
//		i = reportFilename.lastIndexOf('.');
//		if(i>0)
//		{
//			reportFilename = reportFilename.substring(0,i);
//		}
		reportFilename = reportFilename+".report";
		
		ICFile reportFile = null;
		ICFileHome fileHome = (ICFileHome) IDOLookup.getHome(ICFile.class);
		try {
			reportFile = fileHome.findByFileName(reportFilename);
			return reportFile;
		} catch (FinderException e) {
			if (createIfNotFound) {
				reportFile = fileHome.create();
				reportFile.setName(reportFilename);
				reportFile.setCreationDate(IWTimestamp.getTimestampRightNow());
				reportFile.store();
				return reportFile;
			}
		}
		
		return null;
	}
	
	public void addReport(File importFile, File reportFile) throws RemoteException, CreateException {
		boolean replace = true;
		
		ICFile folder = getReportFolder(importFile.getName(), true);
		ICFile report;
		ICFileHome fileHome = (ICFileHome) IDOLookup.getHome(ICFile.class);
		BufferedInputStream bis;
		try {
			bis = new BufferedInputStream(new FileInputStream(reportFile));
			report = fileHome.create();
			report.setName(reportFile.getName());
			report.setFileValue(bis);
			report.setCreationDate(IWTimestamp.getTimestampRightNow());
			report.store();

			if (replace) {
				Iterator children = folder.getChildrenIterator();
				ICFile child = null;
				boolean found = false;
				while (children != null && children.hasNext() && !found) {
					child = (ICFile) children.next();
					found = child.getName().equals(reportFile.getName());
				}
				
				if (found && child != null) {
					folder.removeChild(child);
				}
			}
			folder.addChild(report);

		}
		catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates a text file and adds it the the importFile. 
	 * Each collection element should contain a single Objecet or a colleciton of objects.
	 * @param separator
	 * @param data Collection containing Collection
	 */
	public void addReport(File importFile, String name, Collection data, String separator) throws RemoteException, CreateException {
		File report = getReport(importFile.getName()+"_"+name, data, separator);
		addReport(importFile, report);
		report.delete();
	}

	/**
	 * Creates an excel file and adds it the the importFile.
	 * Each collection element should contain a single Objecet or a colleciton of objects.
	 * @param separator
	 * @param data Collection containing Collection
	 */
	public void addExcelReport(File importFile, String name, Collection data, String separator) throws RemoteException, CreateException {
		File report = getExcelReport(importFile.getName()+"_"+name+".xls", data, separator);
		addReport(importFile, report);
		report.delete();
	}

	/**
	 * Creates a text file with separated columns. Each collection element should contain the data for a single line.
	 * Note. this does NOT add the report to the importFile, use addReport for that.
	 * @param name Name of the file, with full path
	 * @param data Collection containing Collection
	 * @param separator
	 */
	public File getReport(String name, Collection data, String separator) {
		if (data != null && !data.isEmpty()) {
			File file = new File(name);
			try {
				file.createNewFile();
				
				
				BufferedWriter output = null;
				try {
					output = new BufferedWriter( new FileWriter(file) );
					
					Iterator iter = data.iterator();
					Object obj;
					while (iter.hasNext()) {
						obj = iter.next();
						if (obj instanceof String) {
							output.write( obj.toString() );
						} else if (obj instanceof Collection && obj != null) {
							Iterator iter2 = ((Collection) obj).iterator();
							String string = null;
							while (iter2.hasNext()) {
								string = iter2.next().toString();
								if (!string.trim().equals("")) {
									output.write(string);
									output.write(separator);
								}
							}
						}
						output.newLine();
					}
				}
				finally {
					if (output != null) output.close();
				}
				return file;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * Creates an excel file with separated columns. Each collection element should contain the data for a single line.
	 * Note. this does NOT add the report to the importFile, use addReport for that.
	 * @param name Name of the file, with full path
	 * @param data Collection containing Collection
	 * @param separator
	 */
	public File getExcelReport(String name, Collection data, String separator) {
		if (data != null && !data.isEmpty()) {
			try {
				HSSFWorkbook wb = new HSSFWorkbook();
				File file = null;
				try {
					HSSFSheet sheet = wb.createSheet(TextSoap.encodeToValidExcelSheetName(name));
				    int rowIndex = 0;
					
					Iterator iter = data.iterator();
					Object obj;
					int cellRow = 0;
					while (iter.hasNext()) {
					    HSSFRow row = sheet.createRow((short)rowIndex++);
					    cellRow = 0;
						obj = iter.next();
						if (obj instanceof String) {
						    HSSFCell cell = row.createCell((short)cellRow++);
						    cell.setCellValue( obj.toString() );
						} else if (obj instanceof Collection && obj != null) {
							Iterator iter2 = ((Collection) obj).iterator();
							String string = null;
							while (iter2.hasNext()) {
								string = iter2.next().toString();
								if (!string.trim().equals("")) {
								    HSSFCell cell = row.createCell((short)cellRow++);
								    cell.setCellValue( string );
								    if (separator != null && separator.equalsIgnoreCase("\n")) {
								    	cellRow = 0;
								    	row = sheet.createRow((short)rowIndex++);
								    }
								}
							}
						}
					}
				}
				finally {
				    // Write the output to a file
				    FileOutputStream fileOut = new FileOutputStream(name);
				    wb.write(fileOut);
				    fileOut.close();
				    
					file = new File(name);
//					if (output != null) output.close();
				}
				return file;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}