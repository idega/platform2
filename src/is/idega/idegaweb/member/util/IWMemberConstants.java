package is.idega.idegaweb.member.util;

import com.idega.user.data.GroupTypeConstants;
import com.idega.user.util.ICUserConstants;

/**
 * A collection of the static variables used in the member system like group type names.
 * 
 * @author <a href="mailto:eiki@idega.is">Eirikur Hrafnsson</a>
 *
 */

// uses the constants in GroupTypeConstants and ICUserConstants to declare the constants in this class
public class IWMemberConstants {
	
	public static final String REQUEST_PARAMETER_SELECTED_GROUP_ID = "r_sel_gr_id";

	public static final String APPLICATION_PARAMETER_ADMINISTRATOR_MAIN_EMAIL = "IW_MEMBER_ADMINISTRATOR_MAIN_EMAIL";
	public static final String APPLICATION_PARAMETER_MAIL_SERVER = "IW_MEMBER_MAIL_SERVER_ADDRESS";
	
	public static final String GROUP_RELATION_TYPE_PARENT = "GROUP_PARENT";
	
	public static final String META_DATA_DIVISION_LEAGUE_CONNECTION ="CLUBDIV_CONN";
	public static final String META_DATA_DIVISION_BOARD = "CLUBDIV_BOARD";
	public static final String META_DATA_DIVISION_NUMBER ="CLUBDIV_NUMBER";
	public static final String META_DATA_DIVISION_SSN = "CLUBDIV_SSN";
	public static final String META_DATA_DIVISION_FOUNDED = "CLUBDIV_FOUNDED";
	
	public static final String META_DATA_CLUB_LEAGUE_CONNECTION = "CLUBINFO_CONN";
	public static final String META_DATA_CLUB_USING_SYSTEM = "CLUBINFO_SYSTEM";
	public static final String META_DATA_CLUB_OPERATION = "CLUBINFO_OPERATION";
	public static final String META_DATA_CLUB_STATUS = "CLUBINFO_STATUS";
	public static final String META_DATA_CLUB_MAKE = "CLUBINFO_MAKE";
	public static final String META_DATA_CLUB_IN_UMFI = "CLUBINFO_MEMBER";
	public static final String META_DATA_CLUB_TYPE = "CLUBINFO_TYPE";
	public static final String META_DATA_CLUB_FOUNDED = "CLUBINFO_FOUNDED";

	public static final String META_DATA_CLUB_SSN = "CLUBINFO_SSN";
	public static final String META_DATA_CLUB_NUMBER = ICUserConstants.META_DATA_GROUP_NUMBER;
	
	public static final String META_DATA_CLUB_STATUS_MULTI_DIVISION_CLUB = "1";
	public static final String META_DATA_CLUB_STATUS_SINGLE_DIVISION_CLUB = "2";
	public static final String META_DATA_CLUB_STATUS_NO_MEMBERS_CLUB = "3";


	public static final String META_DATA_CLUB_STATE_INACTIVE = "INACTIVE";
	public static final String META_DATA_CLUB_STATE_ACTIVE = "ACTIVE";
	public static final String META_DATA_CLUB_STATE_COMPETITION_BAN = "COMP_BAN";
	
	
	public static final String META_DATA_USER_CLUB_MEMBER_NUMBER_PREFIX = "CLUB_MEMB_NR_";//suffix with club id (group)
	
	public static final String GROUP_TYPE_ALIAS = "alias";
	
	public static final String GROUP_TYPE_GENERAL = GroupTypeConstants.GROUP_TYPE_GENERAL;
	public static final String GROUP_TYPE_FEDERATION = "iwme_federation";
	public static final String GROUP_TYPE_FEDERATION_STAFF = "iwme_federation_staff";
	public static final String GROUP_TYPE_FEDERATION_COMMITTEE = "iwme_federation_committee";
	public static final String GROUP_TYPE_UNION = "iwme_union";
	public static final String GROUP_TYPE_UNION_STAFF = "iwme_union_staff";
	public static final String GROUP_TYPE_UNION_COMMITTEE = "iwme_union_committee";
	public static final String GROUP_TYPE_REGIONAL_UNION = "iwme_regional_union";
	public static final String GROUP_TYPE_REGIONAL_UNION_STAFF = "iwme_regional_union_staff";
	public static final String GROUP_TYPE_REGIONAL_UNION_COMMITTEE = "iwme_regional_union_committee";
	public static final String GROUP_TYPE_REGIONAL_UNION_COLLECTION = "iwme_regional_union_collection";
	public static final String GROUP_TYPE_LEAGUE = GroupTypeConstants.GROUP_TYPE_LEAGUE;
	public static final String GROUP_TYPE_LEAGUE_STAFF = "iwme_league_staff";
	public static final String GROUP_TYPE_LEAGUE_COMMITTEE = "iwme_league_committee";
	public static final String GROUP_TYPE_LEAGUE_COLLECTION = "iwme_league_collection";
	public static final String GROUP_TYPE_LEAGUE_CLUB_COLLECTION = "iwme_league_club_collection";
	public static final String GROUP_TYPE_LEAGUE_CLUB_DIVISION = "iwme_league_club_division";
	public static final String GROUP_TYPE_CLUB = GroupTypeConstants.GROUP_TYPE_CLUB;
	public static final String GROUP_TYPE_CLUB_MEMBER = "iwme_club_member";
	public static final String GROUP_TYPE_CLUB_PLAYER = "iwme_club_player";
	public static final String GROUP_TYPE_CLUB_PLAYER_TEMPLATE = "iwme_club_player_template";
	public static final String GROUP_TYPE_CLUB_TRAINER = "iwme_club_trainer";
	public static final String GROUP_TYPE_CLUB_STAFF = "iwme_club_staff";
	public static final String GROUP_TYPE_CLUB_COMMITTEE = "iwme_club_committee";
	public static final String GROUP_TYPE_CLUB_COMMITTEE_MAIN = "iwme_club_main_committee";
	public static final String GROUP_TYPE_CLUB_DIVISION = "iwme_club_division";
	public static final String GROUP_TYPE_CLUB_DIVISION_TRAINER = "iwme_club_division_trainer";
	public static final String GROUP_TYPE_CLUB_DIVISION_STAFF = "iwme_club_division_staff";
	public static final String GROUP_TYPE_CLUB_DIVISION_COMMITTEE = "iwme_club_division_committee";
	public static final String GROUP_TYPE_CLUB_DIVISION_TEMPLATE = "iwme_club_division_template";
	public static final String GROUP_TYPE_TEMPORARY = "iwme_temporary";
	public static final String GROUP_TYPE_CLUB_PRACTICE_PLAYER = "iwme_club_practice_player";
	

	public static final String MEMBER_BOARD_CHAIR_MAN = "STAT_B_CHAIR"; //"chairman";
	public static final String MEMBER_BOARD_VICE_CHAIRMAN = "STAT_B_VICECHAIR"; //"vice_chairman";
	public static final String MEMBER_CASHIER =  "STAT_B_CASH"; //"cashier";
	public static final String MEMBER_SECRETARY = "STAT_B_SECR"; //"secretary";
	public static final String MEMBER_BOARD_MEMBER = "STAT_B_MAINBOARD"; //"board_member";
	public static final String MEMBER_EXTRA_BOARD = "STAT_B_EXTRABOARD"; //"extra_board";
	public static final String MEMBER_STAND_IN = "STAT_B_EXTRA"; //"stand_in";
	public static final String MEMBER_CO_CHIEF = "STAT_B_COCHIEF"; //"co-chief";
	public static final String MEMBER_CEO = "STAT_B_CEO"; //"ceo";
	public static final String MEMBER_PRESIDENT = "STAT_B_PRES"; //"president";
	public static final String MEMBER_VICE_PRESIDENT = "STAT_B_VICEPRES"; //"vice_president";
	
	public static final String STATUS_COACH = "STAT_COACH";
	public static final String STATUS_ASSISTANT_COACH = "STAT_ASSCOACH";
	
	public static final String ORDER_BY_NAME = "name_order";
	public static final String ORDER_BY_GROUP_PATH = "group_path_order";
	public static final String ORDER_BY_GROUP_TYPE = "group_type_order";
	public static final String ORDER_BY_DATE_OF_BIRTH = "date_of_birth_order";
	public static final String ORDER_BY_USER_STATUS = "user_status_order";
	public static final String ORDER_BY_ADDRESS = "address_order";
	public static final String ORDER_BY_POSTAL_ADDRESS = "postal_address_order";
	public static final String ORDER_BY_POSTAL_CODE = "postal_code_order";
	public static final String ORDER_BY_GROUP_NAME = "group_name_order";
	public static final String ORDER_BY_ENTRY_DATE = "entry_date_order";
	
  public static final String[] STATUS = {MEMBER_BOARD_CHAIR_MAN,MEMBER_BOARD_VICE_CHAIRMAN,MEMBER_CASHIER,MEMBER_SECRETARY,MEMBER_BOARD_MEMBER,MEMBER_EXTRA_BOARD,MEMBER_STAND_IN,MEMBER_CO_CHIEF,MEMBER_CEO,MEMBER_PRESIDENT,MEMBER_VICE_PRESIDENT};
}
