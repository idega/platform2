package is.idega.idegaweb.campus.block.finance.business;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;

import com.idega.io.DownloadWriter;
import com.idega.io.MediaWritable;
import com.idega.io.MemoryFileBuffer;
import com.idega.io.MemoryInputStream;
import com.idega.io.MemoryOutputStream;
import com.idega.presentation.IWContext;

public class CampusTariffReportWriter extends DownloadWriter implements
		MediaWritable {

	public static final String BUFFER = "excel_buffer";
	public static final String FILE_NAME = "excel_file_name";

	protected MemoryFileBuffer buffer = null;

	public String getMimeType() {
		if (this.buffer != null) {
			return this.buffer.getMimeType();
		} else {
			return super.getMimeType();
		}
	}

	public void writeTo(OutputStream out) throws IOException {
		if (this.buffer != null) {
			MemoryInputStream mis = new MemoryInputStream(this.buffer);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while (mis.available() > 0) {
				baos.write(mis.read());
			}
			baos.writeTo(out);
		}
		else {
			System.err.println("buffer is null");
		}
	}
	
	public void init(HttpServletRequest req, IWContext iwc) {
		try {

			if (req.getParameter(BUFFER) != null) {
				String inputBuffer = req.getParameter(BUFFER);
				String name = req.getParameter(FILE_NAME);
				this.buffer = new MemoryFileBuffer();
				MemoryOutputStream out = new MemoryOutputStream(buffer);
				out.write(inputBuffer.getBytes());
				setAsDownload(iwc, name, this.buffer.length());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
