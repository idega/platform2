package is.idega.idegaweb.golf;



public class TableInfo {



  private int row_n, col_n, first_pic_hour, first_pic_min, first_gr, interval_var, day_time, field, menu_day;               // heldur utan um fjolda lina og dalka og fyrstu klukkumynd og fjölda daga til ağ skra

  String day;



  // Smiğir



  public TableInfo() {

    row_n = 0;

    col_n = 0;

    first_pic_min = 0;

    first_pic_hour = 0;

    first_gr = 0;

    interval_var = 0;

    day_time = -1;

	field = 0;

	day = "2000-01-01";

    menu_day = 0;

  }





  public TableInfo(int col_num, int row_num, int first_picture_hour, int first_picture_min, int first_group, int interval, int daytime, int field_id, String date, int num_of_days) {

    row_n = row_num;

    col_n = col_num;

    first_pic_hour = first_picture_hour;

    first_pic_min = first_picture_min;

    first_gr = first_group;

    interval_var = interval;

    day_time = daytime;

	field = field_id;

	day = date;

     menu_day = num_of_days;

  }





  // Föll





  public int get_row_num(){

    return row_n;

  }



  public int get_col_num(){

    return col_n;

  }



  public int get_first_pic_hour(){

    return first_pic_hour;

  }



  public int get_first_pic_min(){

    return first_pic_min;

  }



  public int get_first_group(){

    return first_gr;

  }



  public int get_interval(){

    return interval_var;

  }



  public int get_daytime(){

    return day_time;

  }



  public int get_field_id(){

    return field;

  }





 public int get_menu_days(){

    return menu_day;

  }





  public String get_date(){

    return day;

  }







  public void set_row_num(int row_num){

    row_n = row_num;

  }



  public void set_col_num(int col_num){

    col_n = col_num;

  }



  public void set_first_pic_hour(int first_picture_hour){

    first_pic_hour = first_picture_hour;

  }



  public void set_first_pic_min(int first_picture_min){

    first_pic_min = first_picture_min;

  }



  public void set_first_group(int first_group){

    first_gr = first_group;

  }



  public void set_interval (int interval){

    interval_var = interval;

  }



  public void set_daytime (int daytime){

    day_time = daytime;

  }



  public void set_field_id (int field_id){

    field = field_id;

  }



  public void set_menu_days (int num_of_days){

    menu_day = num_of_days;

  }



  public void set_date (String date){

    day = date;

  }





}
