package is.idega.idegaweb.member.isi.block.reports.util;

import is.idega.idegaweb.member.util.IWMemberConstants;

/**
 * A collection of the static variables used in the workreport system. Extends
 * IWMemberConstants for convenience.
 * 
 * @author <a href="mailto:eiki@idega.is">Eirikur Hrafnsson </a>
 *  
 */
public class WorkReportConstants extends IWMemberConstants {

    public static final String WR_STATUS_NOT_DONE = "NOT_DONE";

    public static final String WR_STATUS_SOME_DONE = "SOME_DONE";

    public static final String WR_STATUS_DONE = "DONE";

    public static final String WR_STATUS_CONTINUANCE = "CONTINUANCE";

    public static final String WR_STATUS_COMPETITION_BAN = "COMPETITION_BAN";

    public static final String WR_STATUS_AT_REGIONAL_UNION = "AT_REGIONAL_UNION";

    public static final String WR_STATUS_INVESTIGATE = "INVESTIGATE";

    public static final String WR_STATUS_NO_REPORT = "NO_REPORT";

    public static final String WR_STATUS_SHOULD_BE_BANNED = "SHOULD_BE_BANNED";

    public static final String WR_USER_TYPE_CLUB = "CLUB";

    public static final String WR_USER_TYPE_REGIONAL_UNION = "REGIONAL_UNION";

    public static final String WR_USER_TYPE_UNION = "UNION";

    public static final String WR_USER_TYPE_FEDERATION = "FEDERATION";

    public static final String WR_USER_TYPE_LEAGUE = "LEAGUE";

    public static final String WR_SESSION_PARAM_CLUB_ID = "wr_sess_cl_id";

    public static final String WR_SESSION_PARAM_REGIONAL_UNION_ID = "wr_sess_ru_id";

    public static final String WR_SESSION_PARAM_WORK_REPORT_ID = "wr_sess_wr_id";

    public static final String WR_SESSION_PARAM_WORK_REPORT_YEAR = "wr_sess_wr_yr";

    public static final String WR_SESSION_CLEAR = "wr_sess_clear";

    // application property name
    public static final String WR_MAIN_BOARD_NAME = "ISI_MAIN_BOARD_NAME";

    public static final String WR_BUNDLE_PARAM_FROM_DATE = "WORK_REPORTS_FROM_DATE";

    public static final String WR_BUNDLE_PARAM_TO_DATE = "WORK_REPORTS_TO_DATE";

    public static final String WR_BUNDLE_PARAM_TEMP_CLOSED = "WORK_REPORTS_TEMPORARELY_CLOSED";

    public static final String INCOME_SUM_KEY = "FIN_income_sum";

    public static final String EXPENSES_SUM_KEY = "FIN_expenses_sum";

    public static final String INCOME_EXPENSES_SUB_SUM_KEY = "FIN_income_expenses_sub_sum";

    public static final String INCOME_EXPENSES_SUM_KEY = "FIN_income_expenses_sum";

    public static final String ASSET_SUM_KEY = "FIN_asset_sum";

    public static final String DEBT_SUM_KEY = "FIN_debt_sum";

    public static final String[] NOT_EDITABLE_FIN_NAMES = { "FIN_29998, FIN_74999, FIN_75000, FIN_99998"};
}