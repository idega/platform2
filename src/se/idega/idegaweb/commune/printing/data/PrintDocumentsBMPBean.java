package se.idega.idegaweb.commune.printing.data;

import java.sql.Timestamp;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.core.file.data.ICFile;
import com.idega.data.GenericEntity;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.SelectQuery;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

/**
 * Title: Description: A data object to view documents that are printed or
 * scheduled for printing Copyright: Copyright (c) 2002 Company: idega Software
 * 
 * @author <a href="tryggvi@idega.is">Tryggvi Larusson </a>
 * @version 1.0
 */
public class PrintDocumentsBMPBean extends GenericEntity implements
        PrintDocuments {
    private static final String COLUMN_DOCUMENT = "DOCUMENT_FILE_ID";

    private static final String COLUMN_NUMBER_OF_SUBDOCUMENTS = "SUB_DOCS_COUNT";

    private static final String COLUMN_TYPE = "DOC_TYPE";

    private static final String COLUMN_DATE_CREATED = "DATE_CREATED";

    private static final String COLUMN_CREATOR = "CREATOR_USER_ID";

    private static final String COLUMN_IS_PRINTED = "IS_PRINTED";

    private static final String COLUMN_LAST_PRINTED_DATE = "LAST_PRINTED_DATE";

    private static final String TABLE_NAME = "COMM_PRINT_DOCUMENTS";

    private static final String TYPE_PRINTED_LETTER = "PRL_TYPE";

    public String getEntityName() {
        return TABLE_NAME;
    }

    public void initializeAttributes() {
        this.addAttribute(this.getIDColumnName());
        this.addManyToOneRelationship(COLUMN_DOCUMENT, "Document",ICFile.class);
        this.addAttribute(COLUMN_NUMBER_OF_SUBDOCUMENTS,"Number of subdocuments", Integer.class);
        this.addAttribute(COLUMN_TYPE, "Type", String.class);
        this.addManyToOneRelationship(COLUMN_CREATOR, "Creator", User.class);
        this.addAttribute(COLUMN_DATE_CREATED, "Created", Timestamp.class);
        this.addAttribute(COLUMN_IS_PRINTED, "Printed?", Boolean.class);
        this.addAttribute(COLUMN_LAST_PRINTED_DATE, "Last printed",Timestamp.class);

    }

    public void setDefaultValues() {
        //super.setDefaultValues();
        setCreated(IWTimestamp.getTimestampRightNow());
        setLastPrinted(IWTimestamp.getTimestampRightNow());
    }

    public void setNumberOfSubDocuments(int number) {
        this.setColumn(COLUMN_NUMBER_OF_SUBDOCUMENTS, number);
    }

    public int getNumberOfSubDocuments() {
        return this.getIntColumnValue(COLUMN_NUMBER_OF_SUBDOCUMENTS);
    }

    public String getType() {
        return this.getStringColumnValue(COLUMN_TYPE);
    }

    public void setType(String type) {
        this.setColumn(COLUMN_TYPE, type);
    }

    public void setAsPrintedLetter() {
        setType(TYPE_PRINTED_LETTER);
    }

    public ICFile getDocument() {
        return (ICFile) this.getColumnValue(COLUMN_DOCUMENT); //Replace this
        // later
    }

    public int getDocumentFileID() {
        return this.getIntColumnValue(COLUMN_DOCUMENT);
    }

    public void setDocument(ICFile file) {
        this.setColumn(COLUMN_DOCUMENT, file);
    }

    public void setDocument(int fileID) {
        this.setColumn(COLUMN_DOCUMENT, fileID);
    }

    public void setCreated(Timestamp created) {
        this.setColumn(COLUMN_DATE_CREATED, created);
    }

    public Timestamp getCreated() {
        return (Timestamp) getColumnValue(COLUMN_DATE_CREATED);
    }

    public Collection ejbFindAllPrintedLetterDocuments() throws FinderException {
        return ejbFindAllDocumentByType(TYPE_PRINTED_LETTER);
    }

    protected SelectQuery idoSelectQueryGetSelectWithType(String type) {
        SelectQuery query = super.idoSelectQuery();
        query.addCriteria(new MatchCriteria(idoQueryTable(), COLUMN_TYPE,
                MatchCriteria.EQUALS, type, true));
        return query;
    }

    protected SelectQuery idoSelectQueryGetSelectWithType(String type,
            IWTimestamp fromDate, IWTimestamp toDate) {
        SelectQuery sql = idoSelectQueryGetSelectWithType(type);
        IWTimestamp from = new IWTimestamp(fromDate);
        IWTimestamp to = new IWTimestamp(toDate);
        to.setHour(23);
        to.setMinute(59);
        to.setSecond(59);
        from.setHour(0);
        from.setMinute(0);
        from.setSecond(0);
        sql.addCriteria(new MatchCriteria(idoQueryTable(), COLUMN_DATE_CREATED,
                MatchCriteria.GREATEREQUAL, from.getTimestamp()));
        sql.addCriteria(new MatchCriteria(idoQueryTable(), COLUMN_DATE_CREATED,
                MatchCriteria.LESSEQUAL, to.getTimestamp()));
        sql.addOrder(idoQueryTable(), COLUMN_DATE_CREATED, false);

        return sql;
    }

    public Collection ejbFindAllDocumentByType(String type)
            throws FinderException {
        return super.idoFindPKsByQuery(idoSelectQueryGetSelectWithType(type));
    }

    public Collection ejbFindAllDocumentByType(String type, IWTimestamp from,
            IWTimestamp to) throws FinderException {
        return super.idoFindPKsByQuery(idoSelectQueryGetSelectWithType(type,
                from, to));
    }

    public Collection ejbFindAllDocumentByType(String type, int resultSize,
            int startingIndex) throws FinderException {
        return super.idoFindPKsByQuery(idoSelectQueryGetSelectWithType(type),
                resultSize, startingIndex);
    }

    public Collection ejbFindAllDocumentByType(String type, IWTimestamp from,
            IWTimestamp to, int resultSize, int startingIndex)
            throws FinderException {
        return super.idoFindPKsByQuery(idoSelectQueryGetSelectWithType(type,
                from, to), resultSize, startingIndex);
    }

    /**
     * Returns the creator.
     * 
     * @return User
     */
    public User getCreator() {
        return (User) getColumnValue(COLUMN_CREATOR);
    }

    /**
     * Returns the creator.
     * 
     * @return User
     */
    public int getCreatorUserID() {
        return getIntColumnValue(COLUMN_CREATOR);
    }

    /**
     * Returns the ifPrinted.
     * 
     * @return boolean
     */
    public boolean isIfPrinted() {
        return getBooleanColumnValue(COLUMN_IS_PRINTED);
    }

    /**
     * Returns the lastPrinted.
     * 
     * @return Timestamp
     */
    public Timestamp getLastPrinted() {
        return (Timestamp) getColumnValue(COLUMN_LAST_PRINTED_DATE);
    }

    /**
     * Sets the creator.
     * 
     * @param creator
     *            The creator to set
     */
    public void setCreator(User creator) {
        setColumn(COLUMN_CREATOR, creator);
    }

    /**
     * Sets the creator.
     * 
     * @param creator
     *            The creator to set
     */
    public void setCreator(int creatorUserID) {
        setColumn(COLUMN_CREATOR, creatorUserID);
    }

    /**
     * Sets the ifPrinted.
     * 
     * @param ifPrinted
     *            The ifPrinted to set
     */
    public void setIfPrinted(boolean ifPrinted) {
        setColumn(COLUMN_IS_PRINTED, ifPrinted);
    }

    /**
     * Sets the lastPrinted.
     * 
     * @param lastPrinted
     *            The lastPrinted to set
     */
    public void setLastPrinted(Timestamp lastPrinted) {
        setColumn(COLUMN_LAST_PRINTED_DATE, lastPrinted);
    }

}