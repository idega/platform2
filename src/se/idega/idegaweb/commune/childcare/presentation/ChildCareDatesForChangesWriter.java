package se.idega.idegaweb.commune.childcare.presentation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.idega.io.DownloadWriter;
import com.idega.io.MediaWritable;
import com.idega.io.MemoryFileBuffer;
import com.idega.io.MemoryInputStream;
import com.idega.io.MemoryOutputStream;
import com.idega.presentation.IWContext;

/**
 * This class generates Excel output for ChildCareProviderDatesForChanges block
 * 
 * @author Dainis Brjuhoveckis
 * 
 */
public class ChildCareDatesForChangesWriter extends DownloadWriter implements
		MediaWritable {
	
	public final static String PARAMETER_PROVIDER_ID = "provider_id";

	private MemoryFileBuffer buffer = null;

	public void init(HttpServletRequest req, IWContext iwc) {
		try {
			// parse params

			// get data from business

			// genereate xls
			buffer = writeXLS(iwc);
			setAsDownload(iwc, "childcare_queue.xls", buffer.length());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getMimeType() {
		if (buffer != null)
			return buffer.getMimeType();
		return super.getMimeType();
	}
	
	public void writeTo(OutputStream out) throws IOException {
		if (buffer != null) {
			MemoryInputStream mis = new MemoryInputStream(buffer);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while (mis.available() > 0) {
				baos.write(mis.read());
			}
			baos.writeTo(out);
		}
		else
			System.err.println("buffer is null");
	}	

	
	private MemoryFileBuffer writeXLS(IWContext iwc) throws Exception {

		MemoryFileBuffer buffer = new MemoryFileBuffer();
		MemoryOutputStream mos = new MemoryOutputStream(buffer);

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("Worksheet");
		
		HSSFFont font = wb.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setFontHeightInPoints((short) 12);
        HSSFCellStyle style = wb.createCellStyle();
        style.setFont(font);
		
		//now we create headers for columns
		int cellRow = 0;
        HSSFRow row = sheet.createRow((short) cellRow++);
		
        

		wb.write(mos);

		buffer.setMimeType("application/x-msexcel");
		return buffer;
	}

}
