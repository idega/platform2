package com.idega.block.reports.business;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import com.idega.block.reports.data.Report;
import com.idega.block.reports.data.ReportInfo;
import com.idega.io.MediaWritable;
import com.idega.io.MemoryFileBuffer;
import com.idega.io.MemoryInputStream;
import com.idega.io.MemoryOutputStream;
import com.idega.presentation.IWContext;
import com.idega.util.database.ConnectionBroker;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfWriter;
/**

 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0

 */
public class ReportWriter implements MediaWritable {

	private Report eReport;
	private ReportInfo eReportInfo;
	private String mimeType;
	private MemoryFileBuffer buffer = null;
	public final static String prmReportId = "repid";
	public final static String prmReportInfoId = "repifid";
	public final static String prmPrintType = "reptype";
	public final static String XLS = "xls";
	public final static String PDF = "pdf";
	public final static String TXT = "txt";
	public ReportWriter() {
	}
	public void init(HttpServletRequest req, IWContext iwma) {
		if (req.getParameter(prmReportId) != null) {
			eReport = ReportFinder.getReport(Integer.parseInt(req.getParameter(prmReportId)));
			if (req.getParameter(prmReportInfoId) != null) {
				eReportInfo = ReportFinder.getReportInfo(Integer.parseInt(req.getParameter(prmReportInfoId)));
				if (eReportInfo.getType().equals("sticker"))
					buffer = StickerReport.writeStickerList(eReport, eReportInfo);
				else if (eReportInfo.getType().equals("columns"))
					System.err.println("not sticker could it be " + eReportInfo.getType());
				else
					System.err.println("not sticker could it be " + eReportInfo.getType());
			}
			else if (req.getParameter(prmPrintType) != null) {
				String type = req.getParameter(prmPrintType);
				if (type.equals(PDF)) {
					buffer = writePDF(eReport);
				}
				else if (type.equals(XLS)) {
					buffer = writeXLS(eReport);
				}
				else if (type.equals(TXT)) {
					buffer = writeTXT(eReport);
				}
			}
		}
	}
	public String getMimeType() {
		if (buffer != null)
			return buffer.getMimeType();
		return "application/pdf";
	}
	public void writeTo(OutputStream out) throws IOException {
		if (buffer != null) {
			MemoryInputStream mis = new MemoryInputStream(buffer);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			// Read the entire contents of the file.
			while (mis.available() > 0) {
				baos.write(mis.read());
			}
			baos.writeTo(out);
		}
		else
			System.err.println("buffer is null");
	}
	public static boolean writeXLSReport(String[] Headers, String[][] Content, OutputStream out) {
		boolean returner = false;
		try {
			OutputStreamWriter fout = new OutputStreamWriter(out);
			StringBuffer data;
			int len = Content.length;
			data = new StringBuffer();
			for (int j = 0; j < Headers.length; j++) {
				data.append(Headers[j]);
				data.append("\t");
			}
			data.append("\n");
			fout.write(data.toString());
			for (int i = 0; i < len; i++) {
				data = new StringBuffer();
				for (int j = 0; j < Content[i].length; j++) {
					data.append(Content[i][j]);
					data.append("\t");
				}
				data.append("\n");
				fout.write(data.toString());
			}
			returner = true;
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		finally {
			try {
				out.close();
			}
			catch (IOException io) {
				io.printStackTrace();
				returner = false;
			}
			
		}
		return returner;
	}
	public static boolean writePDFReport(String[] Headers, String[][] Content, OutputStream out) {
		return false;
	}
	public static MemoryFileBuffer writeXLS(Report report) {
		return writeTabDelimited(report, XLS);
	}
	public static MemoryFileBuffer writeTXT(Report report) {
		return writeTabDelimited(report, TXT);
	}
	private static MemoryFileBuffer writeTabDelimited(Report report, String type) {
		return writeDelimited(report.getSQL(), report.getHeaders(), type, "\t");
	}
	private static MemoryFileBuffer writeDelimited(String sql, String[] headers, String type, String delimiter) {
		Connection Conn = null;
		ResultSet RS = null;
		Statement stmt = null;
		MemoryFileBuffer buffer = new MemoryFileBuffer();
		MemoryOutputStream mos = new MemoryOutputStream(buffer);
		
		try {
			//String file = realpath;
			//FileWriter out = new FileWriter(file);
			Conn = com.idega.util.database.ConnectionBroker.getConnection();
			stmt = Conn.createStatement();
			RS = stmt.executeQuery(sql);
			ResultSetMetaData MD = RS.getMetaData();
			int count = MD.getColumnCount();
			if (headers == null) {
				headers = new String[count];
				for (int i = 0; i < count; i++) {
					headers[i] = MD.getColumnLabel(i + 1);
				}
			}
			String temp;
			StringBuffer data = new StringBuffer();
			if (headers != null) {
				for (int i = 0; i < headers.length; i++) {
					data.append(headers[i]);
					data.append("\t");
				}
			}
			data.append("\n");
			mos.write(data.toString().getBytes());
			while (RS.next()) {
				data = new StringBuffer();
				for (int i = 1; i <= count; i++) {
					temp = RS.getString(i);
					temp = temp != null ? temp : "";
					data.append(temp);
					data.append("\t");
				}
				data.append("\n");
				mos.write(data.toString().getBytes());
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	    finally {
	    	// do not hide an existing exception
	    	try { 
	    		if (RS != null) {
	    			RS.close();
		      	}
	    	}
		    catch (SQLException resultCloseEx) {
		    	System.err.println("[ReportWriter] result set could not be closed");
		     	resultCloseEx.printStackTrace(System.err);
		    }
		    // do not hide an existing exception
		    try {
		    	if (stmt != null)  {
		    		stmt.close();
		    	    com.idega.util.database.ConnectionBroker.freeConnection(Conn);
		    	}
		    }
	 	    catch (SQLException statementCloseEx) {
		     	System.err.println("[ReportWriter] statement could not be closed");
		     	statementCloseEx.printStackTrace(System.err);
		    }    	
 	    	mos.close();
	    }
		if (type.equals(XLS))
			buffer.setMimeType("application/x-msexcel");
		else
			buffer.setMimeType("text/plain");
		return buffer;
	}
	public static MemoryFileBuffer writePDF(Report report) {
		Connection Conn = null;
		MemoryFileBuffer buffer = new MemoryFileBuffer();
		MemoryOutputStream mos = new MemoryOutputStream(buffer);
		Statement stmt = null;
		ResultSet RS = null;
		try {
			String[] Headers = report.getHeaders();
			int Hlen = Headers.length;
			String sql = report.getSQL();
			String info = report.getColInfo();
			String columnWidths = null;
			int[] sizes = null;
			if (info != null) {
				int first = info.indexOf("#");
				if (first != -1) {
					if (info.length() > first) {
						int second = info.indexOf("#", first + 1);
						if (second != -1) {
							columnWidths = info.substring(first + 1, second);
							StringTokenizer tok = new StringTokenizer(columnWidths, ";");
							int size = tok.countTokens();
							if (size > 0) {
								sizes = new int[size];
								int i = 0;
								while (tok.hasMoreTokens()) {
									sizes[i++] = Integer.parseInt(tok.nextToken());
								}
							}
						}
					}
				}
			}
			Document document = new Document(PageSize.A4, 50, 50, 50, 50);
			PdfWriter writer = PdfWriter.getInstance(document, mos);
			document.addTitle(report.getName());
			document.addAuthor("Idega Reports");
			document.addSubject(report.getInfo());
			document.open();
			Conn = com.idega.util.database.ConnectionBroker.getConnection();
			stmt = Conn.createStatement();
			RS = stmt.executeQuery(sql);
			String temp = null;
			Table datatable = getTable(Headers, sizes);
			while (RS.next()) {
				for (int i = 1; i <= Hlen; i++) {
					temp = RS.getString(i);
					temp = temp != null ? temp : "";
					Cell cell = new Cell(new Phrase(temp, new Font(Font.HELVETICA, 10, Font.BOLD)));
					cell.setBorder(Rectangle.NO_BORDER);
					datatable.addCell(cell);
				}
				if (!writer.fitsPage(datatable)) {
					datatable.deleteLastRow();
					document.add(datatable);
					document.newPage();
					datatable = getTable(Headers, sizes);
				}
			}
			document.add(datatable);
			document.close();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		
	    finally {
	    	// do not hide an existing exception
	    	try { 
	    		if (RS != null) {
	    			RS.close();
		      	}
	    	}
		    catch (SQLException resultCloseEx) {
		    	System.err.println("[ReportWriter] result set could not be closed");
		     	resultCloseEx.printStackTrace(System.err);
		    }
		    // do not hide an existing exception
		    try {
		    	if (stmt != null)  {
		    		stmt.close();
					if (Conn != null)
						ConnectionBroker.freeConnection(Conn);
		    	}
		    }
	 	    catch (SQLException statementCloseEx) {
		     	System.err.println("[ReportWriter] statement could not be closed");
		     	statementCloseEx.printStackTrace(System.err);
		    }
	    }
		buffer.setMimeType("application/pdf");
		return buffer;
	}
	private static Table getTable(String[] headers, int[] sizes) throws BadElementException, DocumentException {
		Table datatable = new Table(headers.length);
		datatable.setPadding(0.0f);
		datatable.setSpacing(0.0f);
		datatable.setBorder(Rectangle.NO_BORDER);
		datatable.setWidth(100);
		if (sizes != null)
			datatable.setWidths(sizes);
		for (int i = 0; i < headers.length; i++) {
			//datatable.addCell(Headers[i]);
			Cell cell = new Cell(new Phrase(headers[i], new Font(Font.HELVETICA, 12, Font.BOLD)));
			cell.setBorder(Rectangle.BOTTOM);
			datatable.addCell(cell);
		}
		// the first cell spans 10 columns
		datatable.setDefaultCellBorderWidth(0);
		datatable.setDefaultCellBorder(Rectangle.NO_BORDER);
		datatable.setDefaultRowspan(1);
		return datatable;
	}
}