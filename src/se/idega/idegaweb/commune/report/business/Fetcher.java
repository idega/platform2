package se.idega.idegaweb.commune.report.business;

import com.idega.util.database.ConnectionBroker;
import java.sql.*;
import java.util.*;

/**
 * Fetches data dynamically from the IdegaWeb database using a jdbc connection
 * retreived from IdegaWeb's {@link com.idega.util.database.ConnectionBroker}.
 * <p>
 * Last modified: $Date: 2003/03/17 11:01:37 $ by $Author: staffan $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.2 $
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
    public static String [][] fetchFromDatabase (final String sql)
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
    public static String [][] fetchFromDatabase
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
    private static String [][] fetchFromDatabase
        (final String sql, final boolean fetchAllRows, final int maxRows)
        throws FetchException {
        Connection connection = null;
        String [][] result = new String [0][0];

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
            final java.util.List [] data = new java.util.List [columnCount];
            for (int i = 0; i < columnCount; i++) {
                data [i] = new ArrayList ();
                data [i].add (metaData.getColumnLabel (i + 1));
                data [i].add (metaData.getColumnName (i + 1));
            }
            for (int row = 0;
                 resultSet.next () && (fetchAllRows || row < maxRows); row++) {
                for (int i = 0; i < columnCount; i++) {
                    data [i].add (resultSet.getString (i + 1));
                }
            }
            resultSet.close ();
            statement.close ();
            final int rowCount = data [0].size ();
            result = new String [rowCount][columnCount];
            for (int col = 0; col < columnCount; col++) {
                int row = 0;
                for (Iterator i = data [col].iterator (); i.hasNext (); row++) {
                    final String cell = (String) i.next ();
                    result [row][col] = cell != null ? cell.trim () : "(null)";
                }
            }
        } catch (SQLException e) {
            throw new FetchException (e.getMessage ());
        } finally {
            ConnectionBroker.freeConnection (connection);
        }
        
        return result;
    }

    /**
     * Exception message wrapper thrown by methods in
     * {@link se.idega.idegaweb.commune.report.business.Fetcher}. The point is
     * not to throw any {@link java.sql.SQLException} to clients, since they are
     * at wrong abstraction level.
     * <p>
     * Last modified: $Date: 2003/03/17 11:01:37 $ by $Author: staffan $
     *
     * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
     * @version $Revision: 1.2 $
     */
    public static class FetchException extends Exception {
        public FetchException (final String message) {
            super (message);
        }
    }
}
