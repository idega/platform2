package se.idega.idegaweb.commune.report.business;

import com.idega.util.database.ConnectionBroker;
import java.sql.*;
import java.util.*;

/**
 * Fetches data dynamically from the IdegaWeb database using a jdbc connection
 * retreived from IdegaWeb's {@link com.idega.util.database.ConnectionBroker}.
 * <p>
 * Last modified: $Date: 2003/05/19 08:52:35 $ by $Author: laddi $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.4 $
 * @see java.sql
 * @see se.idega.idegaweb.commune.report.business.Fetcher.FetcherException
 */
public class Fetcher {

    /**
     * Fetches according to the given select statement and returns the result as
     * an two dimensional String matrix.
     *
     * @param sql select statement - must start with 'select'
     * @return the fetcheed result as an two dimensional String matrix, where
     * first row is the column name, the second row is the column label and then
     * comes the cell values
     * @throws Fetcher.FetchException if the sql doesn't start with 'select' or if an
     * exception occurs during database communication
     */
    public static FetchResult fetchFromDatabase (final String sql)
        throws FetchException {
        return fetchFromDatabase (sql, true, 0); // zero is a dummy - not used
    }
    

    /**
     * Fetches according to the given select statement a given number of rows
     * and returns the result as an two dimensional String matrix.
     *
     * @param sql select statement - must start with 'select'
     * @param maxRows not more than this will be returned
     * @return the fetcheed result as an two dimensional String matrix, where
     * first row is the column name, the second row is the column label and then
     * comes the cell values
     * @throws Fetcher.FetchException if the sql doesn't start with 'select' or if an
     * exception occurs during database communication
     */
    public static FetchResult fetchFromDatabase
        (final String sql, final int maxRows) throws FetchException {

        return fetchFromDatabase (sql, false, maxRows);
    }
    
    /**
     * Fetches according to the given select statement a given number of rows
     * and returns the result as an two dimensional String matrix.
     *
     * @param sql select statement - must start with 'select'
     * @param fetchAllRows boolean stating if maxRows should be ignored
     * @param maxRows not more than this will be returned
     * @return the fetcheed result as an two dimensional String matrix, where
     * first row is the column name, the second row is the column label and then
     * comes the cell values
     * @throws FetchException if the sql doesn't start with 'select' or if an
     * exception occurs during database communication
     */
    private static FetchResult fetchFromDatabase
        (final String sql, final boolean fetchAllRows, final int maxRows)
        throws FetchException {
        Connection connection = null;

        if (!sql.trim ().toLowerCase ().startsWith ("select")) {
            throw new FetchException
                    ("Only queries starting with 'select' are allowed here");
        }
        try {
            connection = ConnectionBroker.getConnection ();
            final Statement statement = connection.createStatement ();
            final ResultSet resultSet = statement.executeQuery (sql);
            final ResultSetMetaData metaData = resultSet.getMetaData ();
            final int columnCount = metaData.getColumnCount ();
            final String [] columnLabels = new String [columnCount];
            final java.util.List [] columns = new java.util.List [columnCount];
            for (int col = 0; col < columnCount; col++) {
                columnLabels [col] = metaData.getColumnLabel (col + 1);
                columns [col] = new ArrayList ();
            }

            for (int row = 0;
                 resultSet.next () && (fetchAllRows || row < maxRows); row++) {
                for (int col = 0; col < columnCount; col++) {
                    final String value = resultSet.getString (col + 1);
                    columns [col].add (value);
                }
            }
            if (resultSet != null) resultSet.close ();
            if (statement != null) statement.close ();
            final int rowCount = columns [0].size ();
            final String [][] data = new String [rowCount][columnCount];
            for (int col = 0; col < columnCount; col++) {
                int row = 0;
                for (Iterator i = columns [col].iterator (); i.hasNext ();
                     row++) {
                    final String cell = (String) i.next ();
                    data [row][col] = cell != null ? cell.trim () : "(null)";
                }
            }
            return new FetchResult (data, null, columnLabels);
        } catch (SQLException e) {
            throw new FetchException (e.getMessage ());
        } finally {
            ConnectionBroker.freeConnection (connection);
        }
    }

    /**
     * Exception message wrapper thrown by methods in
     * {@link se.idega.idegaweb.commune.report.business.Fetcher}. The point is
     * not to throw any {@link java.sql.SQLException} to clients, since they are
     * at wrong abstraction level.
     * <p>
     * Last modified: $Date: 2003/05/19 08:52:35 $ by $Author: laddi $
     *
     * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
     * @version $Revision: 1.4 $
     */
    public static class FetchException extends Exception {
        public FetchException (final String message) {
            super (message);
        }
    }

    public static class FetchResult {
        final private String [][] data;
        final private String [] warnings;
        final private String [] columnLabels;

        FetchResult (final String [][] data, final String [] warnings,
                             final String [] columnLabels) {
            this.data = data;
            this.warnings = warnings;
            this.columnLabels = columnLabels;
        }

        public String [][] getData () {
            return data;
        }

        public String [] getWarnings () {
            return warnings;
        }

        public String [] getColumnLabels () {
            return columnLabels;
        }
    }
}
