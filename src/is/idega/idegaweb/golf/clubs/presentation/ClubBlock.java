/*
 * Created on 30.5.2004
 */
package is.idega.idegaweb.golf.clubs.presentation;

import is.idega.idegaweb.golf.presentation.GolfBlock;

/**
 * @author laddi
 */
public abstract class ClubBlock extends GolfBlock {

	public static final String PARAMETER_CLUB_LOCATION = "i_golf_field_location";
	
	public String getLocationNameKey(int location) {
		String returner = "";
		switch (location) {
			case 1 :
				returner = "capital_area";
				break;
			case 2 :
				returner = "reykjanes";
				break;
			case 3 :
				returner = "west";
				break;
			case 4 :
				returner = "westfords";
				break;
			case 5 :
				returner = "north_west";
				break;
			case 6 :
				returner = "north_east";
				break;
			case 7 :
				returner = "east";
				break;
			case 8 :
				returner = "south";
				break;
			case 10 :
				returner = "the_whole_country";
				break;
			case 12 :
				returner = "others";
				break;
		}
		return returner;
	}
}
