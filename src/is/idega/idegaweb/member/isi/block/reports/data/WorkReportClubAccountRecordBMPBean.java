/*
 * Created on May 19, 2003
 */
package is.idega.idegaweb.member.isi.block.reports.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOQuery;

/**
 * Description: Financial records for a club for a specified work report <br>
 * Copyright: Idega Software 2003 <br>
 * Company: Idega Software <br>
 * 
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson </a>
 */
public class WorkReportClubAccountRecordBMPBean extends GenericEntity implements
        WorkReportClubAccountRecord {

    protected final static String ENTITY_NAME = "ISI_WR_CLUB_ACC_REC";

    protected final static String COLUMN_NAME_REPORT_ID = "ISI_WORK_REPORT_ID";

    protected final static String COLUMN_NAME_WORK_REPORT_GROUP = "ISI_WR_GROUP_ID";

    protected final static String COLUMN_NAME_AMOUNT = "ACC_AMOUNT";

    protected final static String COLUMN_NAME_ACCOUNT_KEY_ID = "ACC_KEY_ID";

    public WorkReportClubAccountRecordBMPBean() {
        super();
    }

    public void initializeAttributes() {
        addAttribute(getIDColumnName());
        addAttribute(COLUMN_NAME_REPORT_ID, "Id of the work report", true,
                true, Integer.class, "many-to-one", WorkReport.class);
        addAttribute(COLUMN_NAME_WORK_REPORT_GROUP, "The league id / club",
                true, true, Integer.class, "many-to-one", WorkReportGroup.class);
        addAttribute(COLUMN_NAME_AMOUNT, "Amount", true, true, Double.class);
        addAttribute(COLUMN_NAME_ACCOUNT_KEY_ID, "Account key", true, true,
                Integer.class, "many-to-one", WorkReportAccountKey.class);

        this.addManyToOneRelationship(COLUMN_NAME_WORK_REPORT_GROUP,
                WorkReportGroup.class);
    }

    public String getEntityName() {
        return ENTITY_NAME;
    }

    public void setWorkReportGroupId(int wrGroupId) {
        setColumn(COLUMN_NAME_WORK_REPORT_GROUP, wrGroupId);
    }

    public void setWorkReportGroup(WorkReportGroup group) {
        setColumn(COLUMN_NAME_WORK_REPORT_GROUP, group);
    }

    public int getWorkReportGroupId() {
        return getIntColumnValue(COLUMN_NAME_WORK_REPORT_GROUP);
    }

    public int getReportId() {
        return getIntColumnValue(COLUMN_NAME_REPORT_ID);
    }

    public void setReportId(int reportId) {
        setColumn(COLUMN_NAME_REPORT_ID, reportId);
    }

    public void setReport(WorkReport report) {
        setColumn(COLUMN_NAME_REPORT_ID, report);
    }

    public double getAmount() {
        return getDoubleColumnValue(COLUMN_NAME_AMOUNT, 0);
    }

    public void setAmount(double amount) {
        setColumn(COLUMN_NAME_AMOUNT, amount);
    }

    public int getAccountKeyId() {
        return getIntColumnValue(COLUMN_NAME_ACCOUNT_KEY_ID);
    }

    public void setAccountKeyId(int accountKeyId) {
        setColumn(COLUMN_NAME_ACCOUNT_KEY_ID, accountKeyId);
    }

    public void setAccountKey(WorkReportAccountKey key) {
        setColumn(COLUMN_NAME_ACCOUNT_KEY_ID, key);
    }

    public Collection ejbFindAllRecordsByWorkReportId(int reportId)
            throws FinderException {
        return idoFindAllIDsByColumnOrderedBySQL(COLUMN_NAME_REPORT_ID,
                reportId);
    }

    public Collection ejbFindAllRecordsByWorkReportIdAndWorkReportGroupId(
            int reportId, int wrGroupId) throws FinderException {
        return idoFindAllIDsByColumnsBySQL(COLUMN_NAME_REPORT_ID, reportId,
                COLUMN_NAME_WORK_REPORT_GROUP, wrGroupId);
    }

    public Collection ejbFindAllRecordsByWorkReportIdAndWorkReportGroupIdAndWorkReportAccountKeyCollection(
            int reportId, int wrGroupId, Collection keys)
            throws FinderException {
        IDOQuery sql = idoQuery();
        sql.appendSelectAllFrom(this).appendWhere().appendEquals(
                COLUMN_NAME_REPORT_ID, reportId).appendAnd().appendEquals(
                COLUMN_NAME_WORK_REPORT_GROUP, wrGroupId).appendAnd().append(
                COLUMN_NAME_ACCOUNT_KEY_ID).appendInCollection(keys);

        return idoFindPKsByQuery(sql);
    }

    public Integer ejbFindRecordByWorkReportIdAndWorkReportGroupIdAndWorkReportAccountKeyId(
            int reportId, int wrGroupId, int accountKeyId)
            throws FinderException {
        IDOQuery sql = idoQuery();
        sql.appendSelectAllFrom(this).appendWhere().appendEquals(
                COLUMN_NAME_REPORT_ID, reportId).appendAnd().appendEquals(
                COLUMN_NAME_WORK_REPORT_GROUP, wrGroupId).appendAnd()
                .appendEquals(COLUMN_NAME_ACCOUNT_KEY_ID, accountKeyId);

        return (Integer) idoFindOnePKByQuery(sql);
    }

}