package com.idega.projects.golf;


//import com.idega.projects.golf.*;


public class GolfField{

	private int interval_var, field_num, open_h, open_m, close_h, close_m, show;
	String day;
	SqlTime time = new SqlTime("00:00:00");
        boolean publicRegister;


	/* #########  Smiðir  ########## */


	public GolfField(){
	open_h = 8;
	open_m = 0;
	close_h = 21;
	close_m = 0;
	interval_var = 10;
	field_num = 0;
	day = "2000-01-01";
	show = 3;
        publicRegister = false;
	}


	// tekur inn opnunartíma á time-formi, bil milli hópa á int-formi og númer klúbba á int-formi
	public GolfField (String open_time, String close_time, int interval, int field_id, String date, int days_shown, boolean publicRegistration){

		time.set_sqltime (open_time);
		open_h = time.get_hour();
		open_m = time.get_min();

		time.set_sqltime (close_time);
		close_h = time.get_hour();
		close_m = time.get_min();

		interval_var = interval;
		field_num = field_id;
		day = date;
		show = days_shown;
                publicRegister = publicRegistration;
	}



	// tekur inn opnunartíma á int-formi, bil milli hópa á int-formi og númer klúbba á int-formi
	public GolfField (int open_hour, int open_min, int close_hour, int close_min, int interval, int field_id, String date, int days_shown, boolean publicRegistration){

		open_h = open_hour;
		open_m = open_min;
		close_h = close_hour;
		close_m = close_min;
		interval_var = interval;
		field_num = field_id;
		day = date;
		show = days_shown;
                publicRegister = publicRegistration;
	}



		/* #########  Föll  ########## */


	public int get_open_hour(){
		return open_h;
	}


	public int get_open_min(){
		return open_m;
	}


	public int get_close_hour(){
		return close_h;
	}


	public int get_close_min(){
		return close_m;
	}


	public int get_interval(){
		return interval_var;
	}


	public int get_field_id(){
		return field_num;
	}

	public String get_date(){
		return day;
	}

	public int get_days_shown(){
		return show;
	}



	public void set_open_hour( int open_hour ){
		open_h = open_hour;
	}


	public void set_open_min( int open_min ){
		open_min = open_m;
	}


	public void set_close_hour( int close_hour ){
		close_h = close_hour;
	}


	public void set_close_min( int close_min ){
		close_m = close_min;
	}


	public void set_interval( int interval ){
		interval_var = interval;
	}


	public void set_field_id( int field_id ){
		field_num = field_id;
	}

	public void set_date( String date ){
		day = date;
	}

	public void set_days_shown( int days_shown ){
		show = days_shown;
	}



	public void set_open_time(String opening_time){

		time.set_sqltime (opening_time);
		open_h = time.get_hour();
		open_m = time.get_min();
	}


	public void set_close_time(String closening_time){

		time.set_sqltime (closening_time);
		close_h = time.get_hour();
		close_m = time.get_min();
	}

        public boolean publicRegistration(){
          return publicRegister;
        }

        public void setPublicRegistration(boolean value){
          publicRegister = value;
        }






}	//class GolfField
