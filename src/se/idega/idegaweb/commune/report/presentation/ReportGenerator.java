package se.idega.idegaweb.commune.report.presentation;

import com.idega.block.reports.business.ReportFinder;
import com.idega.block.reports.data.Report;
import com.idega.idegaweb.IWMainApplication;
import com.idega.io.*;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import java.io.*;
import javax.servlet.http.HttpServletRequest;
import se.idega.idegaweb.commune.report.business.Fetcher;

/**
 * Document generator class that creates reports.
 * <p>
 * Last modified: $Date: 2003/04/02 09:39:00 $ by $Author: staffan $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.5 $
 * @see com.idega.block.reports.data.Report
 * @see se.idega.idegaweb.commune.report.business.Fetcher
 */
public class ReportGenerator implements MediaWritable
{
    final public static String REPORT_ID = "report_id";
    private MemoryFileBuffer report;
    private Report reportInfo = null;

    public void init (final HttpServletRequest request,
                      final IWMainApplication not_used) {
        final String reportIdString = request.getParameter (REPORT_ID);
        if (reportIdString != null) {
            try {
                final int reportId = Integer.parseInt (reportIdString);
                reportInfo = ReportFinder.getReport (reportId);
                final Fetcher.FetchResult result
                        = Fetcher.fetchFromDatabase (reportInfo.getSQL ());
                report = getXlsReport (result.getData (), reportInfo);
            } catch (Exception e) {
                System.err.println
                        (getClass ().getName () + " (" + REPORT_ID + "="
                         + reportIdString + "). " + e.getMessage ());
                e.printStackTrace ();
            }
        }
    }

    public String getMimeType() {
        return report != null ? report.getMimeType() : "text/plain";
    }

    public void writeTo(final OutputStream out) throws IOException {
        try {
            if (report != null) {
                final MemoryInputStream mis = new MemoryInputStream (report);
                final ByteArrayOutputStream baos = new ByteArrayOutputStream ();
                while (mis.available() > 0) {
                    baos.write (mis.read ());
                }
                baos.writeTo(out);
            } else {
                System.err.println("ReportGenerator: buffer is null");
            }
        } catch (IOException e) {
            e.printStackTrace ();
            throw e;
        }
    }

    private static MemoryFileBuffer getXlsReport (final String [][] data,
                                                  final Report reportInfo) {
        final MemoryFileBuffer report = new MemoryFileBuffer ();
        final MemoryOutputStream reportStream = new MemoryOutputStream (report);
        final int columnCount = data != null && data.length > 0
                ? data [0].length : 0;
        reportStream.write (new String (reportInfo.getName ()
                                        + "\n\n").getBytes ());

        final String [] headers = reportInfo.getHeaders ();
        for (int col = 0; col < headers.length; col++) {
            reportStream.write ((headers [col] + '\t').getBytes());
        }
        reportStream.write ("\n".getBytes());
        for (int row = 0; row < data.length; row++) {
            final StringBuffer rowData = new StringBuffer ();
            for (int col = 0; col < columnCount; col++) {
                rowData.append (data [row][col] + '\t');
            }
            rowData.append ('\n');
            reportStream.write (rowData.toString().getBytes());
        }

        reportStream.close ();
        report.setMimeType("application/x-msexcel");

        return report;
    }
}
