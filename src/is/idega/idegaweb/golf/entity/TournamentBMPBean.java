//idega 2000 - Tryggvi Larusson
/*
*Copyright 2000 idega.is All Rights Reserved.
*/

package is.idega.idegaweb.golf.entity;

import java.sql.*;
import com.idega.data.*;
import com.idega.util.*;

/**
*@author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
*@version 1.2
*/
public class TournamentBMPBean extends GenericEntity implements Tournament{

	public void initializeAttributes(){

		addAttribute(getIDColumnName());
		addAttribute("name","Nafn móts",true,true,"java.lang.String");
                addAttribute("registration_type","Skráningarform",false,false,"java.lang.String");
		addAttribute("union_id","Haldið á vegum",true,true,"java.lang.Integer","many-to-one","is.idega.idegaweb.golf.entity.Union");
		addAttribute("field_id","Völlur",true,true,"java.lang.Integer","many-to-one","is.idega.idegaweb.golf.entity.Field");
		addAttribute("registration_online","Leyfa rástímaskráningu á netinu",true,true,"java.lang.Boolean");
		addAttribute("group_tournament","Flokkamót",true,true,"java.lang.Boolean");
		addAttribute("open_tournament","Opið mót",true,true,"java.lang.Boolean");
		addAttribute("tournament_type_id","Keppnisform",true,true,"java.lang.Integer","many-to-one","is.idega.idegaweb.golf.entity.TournamentType");
		addAttribute("registration_fee","Skráningargjald",true,true,"java.lang.Integer");
		addAttribute("creation_date","Búið til",false,false,"java.sql.Timestamp");
		addAttribute("start_time","Mót byrjar",true,true,"java.sql.Timestamp");
		addAttribute("number_of_days","Lengd í dögum",true,true,"java.lang.Integer");
		addAttribute("rounds","Fjöldi hringja",true,true,"java.lang.Integer");
                addAttribute("holes","Fjöldi hola á hring",true,true,"java.lang.Integer");
		addAttribute("last_registration_date","Síðasti skráningardagur",true,true,"java.sql.Timestamp");
	        addAttribute("tournament_form_id","Tegund",true,true,"java.lang.Integer","many-to-one","is.idega.idegaweb.golf.entity.TournamentForm");
                addAttribute("extra_text","Upplýsingatexti",false,false,"java.lang.String",4000);

		// added 3.4.2001
		addAttribute("direct_registration","Skrá beint í rástima",true,true,"java.lang.Boolean");
		addAttribute("number_in_group","Fjöldi í holli",true,true,"java.lang.Integer");
		addAttribute("interval","Bið milli ráshopa",true,true,"java.lang.Integer");

		// added 25.4.2001
		addAttribute("FIRST_REGISTRATION_DATE","Fyrsti skráningardagur",true,true,"java.sql.Timestamp");
		addAttribute("max_handicap","Hámarksforgjöf karla",true,true,"java.lang.Float");
		// added 27.4.2001
		addAttribute("max_female_handicap","Hámarksforgjöf kvenna",true,true,"java.lang.Float");
		// added 15.6.2001
		addAttribute("closed","Gert upp",true,true,"java.lang.Boolean");
		addAttribute("closed_date","Gert upp hvenær",true,true,"java.sql.Timestamp");

                // added  31.9.2001
                addManyToManyRelationShip(TournamentGroup.class,"tournament_tournament_group");
                addManyToManyRelationShip(TeeColor.class,"tournament_tee_color");
				addManyToManyRelationShip(Member.class,"tournament_member");
        }

	public void setDefaultValues(){
		setCreationDate(com.idega.util.IWTimestamp.getTimestampRightNow());

                com.idega.util.IWTimestamp start = com.idega.util.IWTimestamp.RightNow();
                start.addDays(2);
                start.setHour(9);
                start.setMinute(0);
                start.setSecond(0);
		setStartTime(start.getTimestamp());

                com.idega.util.IWTimestamp end = com.idega.util.IWTimestamp.RightNow();
                end.addDays(1);
                end.setHour(23);
                end.setMinute(59);
                end.setSecond(59);
		setLastRegistrationDate(end.getTimestamp());
                setNumberOfHoles(18);
                setMaxHandicap(36);
                setMaxFemaleHandicap(36);
                setIsClosed(false);

    }

	public String getEntityName(){
		return "tournament";
	}

	public void setName(String name){
		setColumn("name",name);
	}

	public String getName(){
		return (String) getColumnValue("name");
	}

	public void setMinHandicap(int minHandicap){
		setColumn("handicap_min",new Integer(minHandicap));
	}

	public int getMinHandicap(){
		return getIntColumnValue("handicap_min");
	}

	public void setMaxHandicap(float maxHandicap){
		setColumn("max_handicap",maxHandicap);
	}

	public float getMaxHandicap(){
		return getFloatColumnValue("max_handicap");
	}

	public void setMaxFemaleHandicap(float maxFemaleHandicap){
		setColumn("max_female_handicap",maxFemaleHandicap);
	}

	public float getFemaleMaxHandicap(){
		return getFloatColumnValue("max_female_handicap");
	}

	public void setRegistrationForm(String form){
		setColumn("registration_form",form);
	}

	public String getRegistrationForm(){
		return (String) getColumnValue("registration_form");
	}


	public void setRegistrationType(String type){
		setColumn("registration_type",type);
	}

	public String getRegistrationType(){
		return (String) getColumnValue("registration_type");
	}

	public void setTournamentTypeID(int type_id){
		//System.out.println("Kalla a setTournamentType i Tournament");
		setColumn("tournament_type_id",type_id);
	}


	public void setTournamentType(TournamentType type){
		//System.out.println("Kalla a setTournamentType i Tournament");
		setColumn("tournament_type_id",type);
	}

	public TournamentType getTournamentType()throws SQLException{
		return (TournamentType)getColumnValue("tournament_type_id");
	}

	public int getTournamentTypeId(){
		return getIntColumnValue("tournament_type_id");
	}

	public void setTournamentFormID(int form_id){
		//System.out.println("Kalla a setTournamentType i Tournament");
		setColumn("tournament_form_id",form_id);
	}

        public void setRegistrationOnline(boolean registration_online) {
            setColumn("registration_online",registration_online);
        }

        public boolean getIfRegistrationOnline() {
            return getBooleanColumnValue("registration_online");
        }



	public void setTournamentForm(TournamentForm form){
		//System.out.println("Kalla a setTournamentType i Tournament");
		setColumn("tournament_form_id",form);
	}

	public TournamentForm getTournamentForm()throws SQLException{
		return (TournamentForm)getColumnValue("tournament_form_id");
	}

	public int getTournamentFormId(){
		return getIntColumnValue("tournament_form_id");
	}


	public void setUnion(Union union){
		setColumn("union_id",new Integer(union.getID()));
	}

	public Union getUnion()throws SQLException{
		return (Union) getColumnValue("union_id");
	}

	//þessi föll mega fara þegar tryggvi er búinn að laga addColumnName
	public void setUnionId(Integer unionID){
                setColumn("union_id", unionID);
	}

	public void setUnionId(int unionID){
                setColumn("union_id", unionID);
	}

	public int getUnionId(){
		return getIntColumnValue("union_id");
	}

	public void setFieldId(Integer field_id){
		setColumn("field_id", field_id);
	}

	public int getFieldId(){
		return getIntColumnValue("field_id");
	}
	/////////////////////////////////////////////////////////////////////

	public void setField(Field field){
		setColumn("field_id",field);
	}

	public Field getField()throws SQLException{
		return (Field)getColumnValue("field_id");
	}


	public void setCreationDate(Timestamp creationDate){
		setColumn("creation_date",creationDate);
	}

	public Timestamp getCreationDate(){
		return (Timestamp) getColumnValue("creation_date");
	}


	public void setLastRegistrationDate(Timestamp registrationDate){
		setColumn("last_registration_date",registrationDate);
	}

	public Timestamp getLastRegistrationDate(){
		return (Timestamp) getColumnValue("last_registration_date");
	}
	public void setFirstRegistrationDate(Timestamp firstRegistrationDate){
		setColumn("FIRST_REGISTRATION_DATE",firstRegistrationDate);
	}

	public Timestamp getFirstRegistrationDate(){
		return (Timestamp) getColumnValue("FIRST_REGISTRATION_DATE");
	}


	public void setStartTime(Timestamp startTime){
		setColumn("start_time",startTime);
	}

	public Timestamp getStartTime(){
		return (Timestamp) getColumnValue("start_time");
	}

	public void setEndTime(Timestamp endTime){
		setColumn("end_time",endTime);
	}

	public Timestamp getEndTime(){
		return (Timestamp) getColumnValue("end_time");
	}

	public void setNumberOfRounds(int number){
		setColumn("rounds",number);
	}

	public int getNumberOfRounds(){
		return getIntColumnValue("rounds");
	}

	public void setNumberOfDays(int number){
		setColumn("number_of_days",number);
	}

	public int getNumberOfDays(){
		return getIntColumnValue("number_of_days");
	}

	public void setRegistrationFee(int fee){
		setColumn("registration_fee",fee);
	}

	public int getRegistrationFee(){
		return getIntColumnValue("registration_fee");
	}

	public void setGroupTournament(boolean ifGroupTournament){
		setColumn("group_tournament",ifGroupTournament);
	}

	public boolean getIfGroupTournament(){
		return getBooleanColumnValue("group_tournament");
	}

	public void setOpenTournament(boolean ifOpenTournament){
		setColumn("open_tournament",ifOpenTournament);
	}

	public boolean getIfOpenTournament(){
		return getBooleanColumnValue("open_tournament");
	}

	public TournamentRound[] getTournamentRounds()throws SQLException{
		return (TournamentRound[]) ((TournamentRound) IDOLookup.instanciateEntity(TournamentRound.class)).findAllByColumnOrdered("tournament_id",Integer.toString(this.getID()),"ROUND_NUMBER");
	}

	public TournamentGroup[] getTournamentGroups()throws SQLException{
		return (TournamentGroup[]) findRelated((TournamentGroup) IDOLookup.instanciateEntity(TournamentGroup.class));
	}

        public void setNumberOfHoles(int holes){
          setColumn("holes",holes);
        }

        public int getNumberOfHoles(){
          return getIntColumnValue("holes");
        }

        public void setExtraText(String text){
          setColumn("extra_text",text);
        }

        public String getExtraText(){
          return getStringColumnValue("extra_text");
        }


      public Tournament[] getMostRecentTournaments(int number)throws SQLException{
          //idegaTimestamp stamp = new idegaTimestamp(idegaTimestamp.getTimestampRightNow());
          //return (Tournament[])findAll("select * from "+this.getTableName()+" where start_time > '"+stamp.toSQLDateString()+"'");
          return (Tournament[])this.findAll("select * from "+this.getTableName()+" order by start_time");
      }


    public Tournament[] getForthcomingTournaments()throws SQLException{
          IWTimestamp stamp = new IWTimestamp(IWTimestamp.getTimestampRightNow());
          return (Tournament[])findAll("select * from "+this.getTableName()+" where start_time > '"+stamp.toSQLDateString()+"'");
    }


    public boolean isTournamentFinished(){
      return getIsClosed();
      /*
      idegaTimestamp stampNow = new idegaTimestamp(idegaTimestamp.getTimestampRightNow());
      idegaTimestamp stampTournBegin = new idegaTimestamp(getStartTime());
      return stampNow.isLaterThan(stampTournBegin);
      */
    }

    public boolean isTournamentOngoing(){
      IWTimestamp stampNow = IWTimestamp.RightNow();
      IWTimestamp stampTournBegin = new IWTimestamp(getStartTime());
      IWTimestamp stampTournEnd = new IWTimestamp(getStartTime());
      stampTournEnd.addDays(this.getNumberOfDays());
      //System.out.println("stampNow.isLaterThan(stampTournBegin):"+stampNow.isLaterThan(stampTournBegin)+"stampTournBegin:"+stampTournBegin.getTimestamp().toString());
      //System.out.println("stampTournEnd.isLaterThan(stampNow):"+stampTournEnd.isLaterThan(stampNow)+"stampTournEnd:"+stampTournEnd.getTimestamp().toString());

      return stampNow.isLaterThan(stampTournBegin) && stampTournEnd.isLaterThan(stampNow);
    }


   public TeeColor[] getTeeColors()throws SQLException{
      return (TeeColor[])findRelated((TeeColor) IDOLookup.instanciateEntity(TeeColor.class));
   }

	public void insert()throws SQLException{
		super.insert();
		if (getNumberOfRounds() != -1){
		//System.out.println("Er i insert i Tournament");
		//System.out.println("rounds:"+getNumberOfRounds());
			for (int i = 1; i <= getNumberOfRounds();i++){
				//System.out.println("Er i insert i Tournament og round med i: "+i);
				IWTimestamp stamp = new IWTimestamp(getStartTime());
				stamp.addDays(i+1);
				
				TournamentRound round = (TournamentRound) IDOLookup.instanciateEntity(TournamentRound.class);
				round.setRoundNumber(i);
				round.setTournament(this);
				round.setRoundDate(stamp.getTimestamp());
                                round.setIncreaseHandicap(true);
                                round.setDecreaseHandicap(true);
                                round.setRoundEndDate(stamp.getTimestamp());
				round.insert();
			}
		}
	}


        /**
         * Delete the tournament if there is no registered data to it - Throws an SQLException if there is data (scorecards) registered to it
         */
	public void delete()throws SQLException{
            deleteWithAllData();
	}


        /**
         * Deletes all data including scorecards,registrations etc.
         */
        public void deleteAllData() throws SQLException {
            try {
            		((Member) IDOLookup.instanciateEntity(Member.class)).reverseRemoveFrom(this);
            }
            catch (Exception e) {e.printStackTrace(System.err);}


            try {
                TeeColor[] teeColors = this.getTeeColors();
                for (int i = 0; i < teeColors.length; i++) {
                    this.removeFrom(teeColors[i]);
                }
            } catch (SQLException sql) {sql.printStackTrace(System.err);}


            try {
                TournamentRound[] rounds = getTournamentRounds();
                if(rounds!=null){
                  for(int i=0;i<rounds.length;i++){
                      try {
                          Connection conn = this.getConnection();
                          Statement Stmt = conn.createStatement();

                          Stmt.executeUpdate("DELETE FROM TOURNAMENT_ROUND_STARTINGTIME where TOURNAMENT_ROUND_ID = "+rounds[i].getID());

                          Stmt.close();
                          freeConnection(conn);
                      }
                      catch (Exception e) {System.err.println("Tournament : deleteAllData() : Deleting tournament_round_startingtime");}

                      rounds[i].delete();
                  }
                }
            }
            catch (Exception e) {e.printStackTrace(System.err);}

             try {
                StartingtimeFieldConfig[] sFieldConfig = (StartingtimeFieldConfig[]) ((StartingtimeFieldConfig) IDOLookup.instanciateEntity(StartingtimeFieldConfig.class)).findAllByColumn("tournament_id",this.getID()+"") ;
                for (int i = 0; i < sFieldConfig.length; i++) {
                    sFieldConfig[i].delete();
                }
            }
            catch (Exception e) {e.printStackTrace(System.err);}

            try {
                TournamentGroup group = (TournamentGroup) IDOLookup.instanciateEntity(TournamentGroup.class);
                group.reverseRemoveFrom(this);
                        }
            catch (Exception e) {e.printStackTrace(System.err);}

            try{
                ((TeeColor) IDOLookup.instanciateEntity(TeeColor.class)).removeFrom(this);
            }
            catch (Exception e) {e.printStackTrace(System.err);}


            try {
                try {
                    Connection conn = this.getConnection();
                    Statement Stmt = conn.createStatement();

                    Stmt.executeUpdate("DELETE FROM TOURNAMENT_STARTINGTIME where TOURNAMENT_ID = "+this.getID());

                    Stmt.close();
                    freeConnection(conn);
                }
                catch (Exception e) {System.err.println("Tournament : deleteAllData() : Deleting tournament_startingtime");}

                Startingtime[] stimes = (Startingtime[]) ((Startingtime) IDOLookup.instanciateEntity(Startingtime.class)).findAll("Select startingtime.* from startingtime ,tournament_startingtime where startingtime.startingtime_id = tournament_startingtime.startingtime_id AND tournament_id = "+this.getID());
                if (stimes != null) {
                    for (int i = 0; i < stimes.length; i++) {
                        stimes[i].removeFrom(this);
                        stimes[i].delete();
                    }
                }
            }
            catch (Exception e) {e.printStackTrace(System.err);}

        }


        /**
         * Deletes the tournament with all data including scorecards,registrations etc.
         */
	public void deleteWithAllData()throws SQLException{
            deleteAllData();
            super.delete();
	}


	public int getTournamentGroupId(Member member)throws SQLException{
            return getTournamentGroupId(member.getID());
        }


	public int getTournamentGroupId(int member_id)throws SQLException{
		Connection conn= null;
		Statement Stmt= null;
                int returner = -1;
		try{
                    conn = this.getConnection();
                    Stmt=conn.createStatement();

                    ResultSet RS = Stmt.executeQuery("Select TOURNAMENT_GROUP_ID from TOURNAMENT_MEMBER where TOURNAMENT_ID = "+this.getID()+" AND MEMBER_ID ="+member_id);
                    if (RS.next()) {
                        returner = RS.getInt("TOURNAMENT_GROUP_ID");
                    }

		}
		finally{
			if (Stmt != null){
				Stmt.close();
			}
			if (conn != null){
				freeConnection(conn);
			}
		}

                return returner;

	}


	public int getPosition(Member member) throws SQLException {
		return getPosition(member.getID());
	}

	public int getPosition(int member_id)throws SQLException{
		Connection conn= null;
		Statement Stmt= null;
                int returner = -1;
		try{
                    conn = this.getConnection();
                    Stmt=conn.createStatement();

                    ResultSet RS = Stmt.executeQuery("Select TOURNAMENT_POSITION from TOURNAMENT_MEMBER where TOURNAMENT_ID = "+this.getID()+" AND MEMBER_ID ="+member_id);
                    if (RS.next()) {
                        returner = RS.getInt("TOURNAMENT_POSITION");
                    }

		}
		finally{
			if (Stmt != null){
				Stmt.close();
			}
			if (conn != null){
				freeConnection(conn);
			}
		}

                return returner;

	}

	public int getDismissal(Member member) throws SQLException {
		return getDismissal(member.getID());
	}

	public int getDismissal(int member_id)throws SQLException{
		Connection conn= null;
		Statement Stmt= null;
                int returner = 0;
		try{
                    conn = this.getConnection();
                    Stmt=conn.createStatement();

                    ResultSet RS = Stmt.executeQuery("Select DISMISSAL_ID from TOURNAMENT_MEMBER where TOURNAMENT_ID = "+this.getID()+" AND MEMBER_ID ="+member_id);
                    if (RS.next()) {
                        returner = RS.getInt("DISMISSAL_ID");
                    }

		}
		finally{
			if (Stmt != null){
				Stmt.close();
			}
			if (conn != null){
				freeConnection(conn);
			}
		}

                return returner;

	}



	public void setPosition(Member member, int position) throws SQLException{
		setPosition(member.getID(),position);
	}

	public void setPosition(int member_id, int position) throws SQLException{
		Connection conn= null;
		Statement Stmt= null;
		try{
                    conn = this.getConnection();
                    Stmt=conn.createStatement();

                    Stmt.executeUpdate("UPDATE TOURNAMENT_MEMBER set tournament_position = "+position+" where TOURNAMENT_ID = "+this.getID()+" AND MEMBER_ID ="+member_id);

		}
		finally{
			if (Stmt != null){
				Stmt.close();
			}
			if (conn != null){
				freeConnection(conn);
			}
		}
	}

	public void setDismissal(Member member, int dismissalID) throws SQLException{
		setDismissal(member.getID(),dismissalID);
	}

	public void setDismissal(int member_id, int dismissalID) throws SQLException{
		Connection conn= null;
		Statement Stmt= null;
		try{
                    conn = this.getConnection();
                    Stmt=conn.createStatement();

                    Stmt.executeUpdate("UPDATE TOURNAMENT_MEMBER set dismissal_id = "+dismissalID+" where TOURNAMENT_ID = "+this.getID()+" AND MEMBER_ID ="+member_id);

		}
		finally{
			if (Stmt != null){
				Stmt.close();
			}
			if (conn != null){
				freeConnection(conn);
			}
		}
	}

	public int getTournamentMemberUnionId(Member member)throws SQLException{
            return getTournamentMemberUnionId(member.getID());
        }

        /**
         * @deprecated
         */
	public int getTournamentMemberUnionId(int member_id)throws SQLException{
		Connection conn= null;
                int returner = -1;

                try {
                    conn = this.getConnection();

                    if (conn != null) {
                        returner = getTournamentMemberUnionId(conn,member_id);
                    }

                }
                catch (Exception e) {}
		finally{
			if (conn != null){
				freeConnection(conn);
			}
		}
            return returner;

	}

	public int getTournamentMemberUnionId(Connection conn, int member_id)throws SQLException{
                int returner = -1;
                ResultSet RS = null;
                Statement Stmt = null;
		try{
    		    Stmt = conn.createStatement();

                    RS = Stmt.executeQuery("Select UNION_ID from TOURNAMENT_MEMBER where TOURNAMENT_ID = "+this.getID()+" AND MEMBER_ID ="+member_id);
                    if (RS.next()) {
                        returner = RS.getInt("UNION_ID");
                    }
		}
		finally{
			if (Stmt != null){
				Stmt.close();
			}
                        if (RS != null) {
                          RS.close();
                        }
		}

                return returner;

	}




        public boolean isDirectRegistration() {
            return getBooleanColumnValue("direct_registration");
        }

        public void setIsDirectRegistration(boolean is_direct) {
            setColumn("direct_registration",is_direct);
        }


        public int getNumberInGroup() {
            return getIntColumnValue("number_in_group");
        }

        public void setNumberInGroup(int number_in_group) {
            setColumn("number_in_group",number_in_group);
        }

        public int getInterval() {
            return getIntColumnValue("interval");
        }

        public void setInterval(int interval) {
            setColumn("interval",interval);
        }


        public void setIsClosed(boolean isClosed) {
            setColumn("closed",isClosed);
        }

        public boolean getIsClosed() {
            return getBooleanColumnValue("closed");
        }

	public void setClosedDate(Timestamp closedDate){
		setColumn("closed_date",closedDate);
	}

        public Timestamp getClosedDate() {
            return (Timestamp) getColumnValue("closed_date");
        }

	//todo :
	//change the update function so it changes as well the tournament_round table



}
