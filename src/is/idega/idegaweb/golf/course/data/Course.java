package is.idega.idegaweb.golf.course.data;

import com.idega.block.venue.data.Venue;


public interface Course extends Venue {
	
	public static final String ENTITY_NAME = "golf_course";

	public static final String COLUMN_COURSE_ID = "course_id";
	public static final String COLUMN_NUMBER_OF_HOLES = "number_of_holes";
	
	public int getNumberOfHoles();
	public void setNumberOfHoles(int p0);
}