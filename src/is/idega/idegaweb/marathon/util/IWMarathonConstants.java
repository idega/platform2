/*
 * Created on Jun 30, 2004
 */
package is.idega.idegaweb.marathon.util;


/**
 * Description: <br>
 * Copyright: Idega Software 2004 <br>
 * Company: Idega Software <br>
 * @author birna
 */
public class IWMarathonConstants {
  
  public final static String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.marathon";
  
  public static final String GROUP_TYPE_RUN = "iwma_run";
/*  public static final String GROUP_TYPE_RUN_MARATHON = "iwma_run_marathon";
  public static final String GROUP_TYPE_RUN_LAUGAVEGUR = "iwma_run_laugavegur";
  public static final String GROUP_TYPE_RUN_MIDNIGHT = "iwma_run_midnight";*/
  public static final String GROUP_TYPE_RUN_YEAR = "iwma_run_year";
  public static final String GROUP_TYPE_RUN_DISTANCE = "iwma_run_distance";
  public static final String GROUP_TYPE_RUN_GROUP = "iwma_run_group";
  public static final String PARAMETER_SORT_BY = "iwma_sort_by";
  
  public static final String PARAMETER_NAME = "prm_name";
  public static final String PARAMETER_NATIONALITY = "prm_nationality";
  public static final String PARAMETER_SSN_IS = "prm_ssn_is";
  public static final String PARAMETER_SSN = "prm_ssn";
  public static final String PARAMETER_GENDER = "prm_gender";
  public static final String PARAMETER_FEMALE = "2";
  public static final String PARAMETER_MALE = "1";
  public static final String PARAMETER_ADDRESS = "prm_address";
  public static final String PARAMETER_POSTAL = "prm_postal";
  public static final String PARAMETER_CITY = "prm_city";
  public static final String PARAMETER_COUNTRY = "prm_country";
  public static final String PARAMETER_TEL ="prm_tel";
  public static final String PARAMETER_MOBILE = "prm_mobile";
  public static final String PARAMETER_EMAIL = "prm_email";
  public static final String PARAMETER_TSHIRT = "prm_tshirt";
  public static final String PARAMETER_TSHIRT_S = "prm_small";
  public static final String PARAMETER_TSHIRT_M = "prm_medium";
  public static final String PARAMETER_TSHIRT_L = "prm_large";
  public static final String PARAMETER_TSHIRT_XL = "prm_xlarge";
  public static final String PARAMETER_TSHIRT_XXL = "prm_xxlarge";
  public static final String PARAMETER_CHIP = "prm_chip";
  public static final String PARAMETER_OWN_CHIP = "prm_own_chip";
  public static final String PARAMETER_BUY_CHIP = "prm_buy_chip";
  public static final String PARAMETER_RENT_CHIP = "prm_rent_chip";
  public static final String PARAMETER_CHIP_NUMBER = "prm_chip_num";
  public static final String PARAMETER_GROUP_COMP = "prm_group_comp";
  public static final String PARAMETER_GROUP_NAME = "prm_group_name";
  public static final String PARAMETER_BEST_TIME = "prm_best_time";
  public static final String PARAMETER_GOAL_TIME = "prm_goal_time";
  public static final String PARAMETER_TOTAL = "prm_total";
  public static final String PARAMETER_GROUPS = "prm_groups";
  public static final String PARAMETER_GROUPS_COMPETITION = "prm_groups_competition";
  public static final String PARAMETER_AGREEMENT = "prm_agreement";
  public static final String PARAMETER_AGREE = Boolean.TRUE.toString();
  public static final String PARAMETER_DISAGREE = Boolean.FALSE.toString();
  
  //localized strings
  public static final String RR_INFO_RED_STAR = "run_reg.info_red_star";
  public static final String RR_PRIMARY_DD = "run_reg.primary_dd_lable";
  public static final String RR_SECONDARY_DD = "run_reg.secondary_dd_label";
  public static final String RR_NAME = "run_reg.name";
  public static final String RR_NATIONALITY = "run_reg.nationality";
  public static final String RR_SSN = "run_reg.ssn";
  public static final String RR_GENDER = "run_reg.gender";
  public static final String RR_FEMALE = "run_reg.female";
  public static final String RR_MALE = "run_reg.male";
  public static final String RR_ADDRESS = "run_reg.address";
  public static final String RR_POSTAL = "run_reg.postal";
  public static final String RR_CITY = "run_reg.city";
  public static final String RR_COUNTRY = "run_reg.country";
  public static final String RR_TEL = "run_reg.tel";
  public static final String RR_MOBILE = "run_reg.mobile";
  public static final String RR_EMAIL = "run_reg.email";
  public static final String RR_TSHIRT = "run_reg.t_shirt";
  public static final String RR_CHIP_TIME = "run_reg.champion_chip_timing";
  public static final String RR_CHIP_LINK = "run_reg.champion_chip_link";
  public static final String RR_OWN_CHIP = "run_reg.own_chip";
  public static final String RR_BUY_CHIP = "run_reg.buy_chip";
  public static final String RR_RENT_CHIP = "run_reg.rent_chip";
  public static final String RR_GROUP_COMP = "run_reg.group_competition";
  public static final String RR_GROUP_NAME = "run_reg.group_name";
  public static final String RR_BEST_TIME = "run_reg.best_time";
  public static final String RR_GOAL_TIME = "run_reg.goal_time";
  public static final String RR_AGREEMENT = "run_reg.agreement";
	public static final String RR_AGREE = "run_reg.agree";
	public static final String RR_DISAGREE = "run_reg.disagree";
  public static final String RYSDD_TOTAL = "run_year_sort.total";
  public static final String RYSDD_GROUPS = "run_year_sort.groups";
  public static final String RYSDD_GROUPS_COMP = "run_year_sort.groups_comp";
  
  
  public static final String DISTANCE_55 = "55_km";
  public static final String DISTANCE_42 = "42_km";
  public static final String DISTANCE_21 = "21_km";
  public static final String DISTANCE_10 = "10_km";
  public static final String DISTANCE_7 = "7_km";
  public static final String DISTANCE_5 = "5_km";
  public static final String DISTANCE_3 = "3_km";
  
  public static final String FEMALE_14 = "female_14";
  public static final String FEMALE_15_17 = "female_15_17";
  public static final String FEMALE_18 = "female_18";
  public static final String FEMALE_18_29 = "female_18_29";
  public static final String FEMALE_18_39 = "female_18_39";
  public static final String FEMALE_19_39 = "female_19_39";
  public static final String FEMALE_30_39 = "female_30_39";
  public static final String FEMALE_40_49 = "female_40_49";
  public static final String FEMALE_50 = "female_50";
  public static final String FEMALE_50_59 = "female_50_59";
  public static final String FEMALE_60 = "female_60";
  public static final String MALE_14 = "male_14";
  public static final String MALE_15_17 = "male_15_17";
  public static final String MALE_18 = "male_18";
  public static final String MALE_18_29 = "male_18_29";
  public static final String MALE_18_39 = "male_18_39";
  public static final String MALE_19_39 = "male_19_39";
  public static final String MALE_30_39 = "male_30_39";
  public static final String MALE_40_49 = "male_40_49";
  public static final String MALE_50 = "male_50";
  public static final String MALE_50_59 = "male_50_59";
  public static final String MALE_60 = "male_60";
  
  public static final int MAX_NUMBER_DISTANCE_55 = 999;
  public static final int MIN_NUMBER_DISTANCE_55 = 1;
  public static final int MAX_NUMBER_DISTANCE_42 = 999;
  public static final int MIN_NUMBER_DISTANCE_42 = 1;
  public static final int MAX_NUMBER_DISTANCE_21 = 1999;
  public static final int MIN_NUMBER_DISTANCE_21 = 1000;
  public static final int MAX_NUMBER_DISTANCE_10 = 3999;
  public static final int MIN_NUMBER_DISTANCE_10 = 2000;
  public static final int MAX_NUMBER_DISTANCE_7 = 4999;
  public static final int MIN_NUMBER_DISTANCE_7 = 4000;
  public static final int MAX_NUMBER_DISTANCE_5 = 1999;
  public static final int MIN_NUMBER_DISTANCE_5 = 1;
  public static final int MAX_NUMBER_DISTANCE_3 = 9999;
  public static final int MIN_NUMBER_DISTANCE_3 = 5000;
}
