package is.idega.idegaweb.member.isi.block.reports.util;

import is.idega.idegaweb.member.util.IWMemberConstants;

/**
 * A collection of the static variables used in the workreport system. Extends IWMemberConstants for convenience.
 * @author <a href="mailto:eiki@idega.is">Eirikur Hrafnsson</a>
 *
 */
public class WorkReportConstants extends IWMemberConstants{

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
	
  // name of the "group" that represents the main board
  public static final String MAIN_BOARD = "main_board"; 
  // artificial id of the "group" that represents the main board
  // do not use -1 because that is used to represent a new entity that is not stored yet 
  public static final Integer MAIN_BOARD_ID = new Integer(-42);
  
  public static final String INCOME_SUM_KEY = "FIN_income_sum";
  public static final String EXPENSES_SUM_KEY = "FIN_expenses_sum";
  public static final String INCOME_EXPENSES_SUM_KEY = "FIN_income_expenses_sum";
  public static final String ASSET_SUM_KEY = "FIN_asset_sum";
  public static final String DEBT_SUM_KEY = "FIN_debt_sum";
  
  public static final String[] NOT_EDITABLE_FIN_NAMES = {"FIN_40000", "FIN_92000" };	
}
