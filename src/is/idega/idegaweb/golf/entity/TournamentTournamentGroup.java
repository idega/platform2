//idega 2000 - Tryggvi Larusson
/*
*Copyright 2000 idega.is All Rights Reserved.
*/

package is.idega.idegaweb.golf.entity;

import java.sql.*;


/**
*@author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
*@version 1.2
*/
public class TournamentTournamentGroup extends GolfEntity{

	public TournamentTournamentGroup(){
		super();
	}

	public TournamentTournamentGroup(int id)throws SQLException{
		super(id);
	}

	public void initializeAttributes(){
		addAttribute("tournament_id","Mót",false,false,"java.lang.Integer","many-to-one","is.idega.idegaweb.golf.entity.Tournament");
		addAttribute("tournament_group_id","Hópur",false,false,"java.lang.Integer","many-to-one","is.idega.idegaweb.golf.entity.TournamentGroup");
                addAttribute("registration_fee","Gjald",true,true,"java.lang.Integer");
                addAttribute("tee_color_id","Litur teigs",true,true,"java.lang.Integer","many-to-one","is.idega.idegaweb.golf.entity.TeeColor");
          }

	public String getEntityName(){
		return "tournament_tournament_group";
	}

        public String getIDColumnName() {
                return "tournament_id";
        }


        public void setTournamentId(int tournament_id) {
            setColumn("tournament_id",tournament_id);
        }
        public void setTournamentGroupId(int tournament_group_id) {
            setColumn("tournament_group_id",tournament_group_id);
        }
        public void setRegistrationFee(int registration_fee) {
            setColumn("registration_fee",registration_fee);
        }
        public void setTeeColorId(int tee_color_id) {
            setColumn("tee_color_id",tee_color_id);
        }

        public int getTournamentId() {
            return getIntColumnValue("tournament_id");
        }
        public int getTournamentGroupId() {
            return getIntColumnValue("tournament_group_id");
        }
        public int getRegistrationFee() {
            return getIntColumnValue("registration_fee");
        }
        public int getTeeColorId() {
            return getIntColumnValue("tee_color_id");
        }

        public void update() {
            Connection conn = null;
            try {
              conn = getConnection();
              Statement Stmt = conn.createStatement();

                  Stmt.executeUpdate("UPDATE "+this.getTableName()+" SET tournament_id="+this.getTournamentId()+" AND tournament_group_id="+this.getTournamentGroupId()+" AND registration_fee="+this.getRegistrationFee()+" AND tee_color_id="+this.getTeeColorId());

              Stmt.close();

            }
            catch (SQLException sq) {
                sq.printStackTrace(System.err);
            }
            finally {
              if (conn != null){
                freeConnection(conn);
              }
            }
        }


        public void insert() {
          Connection conn = null;
            try {
              conn = getConnection();
              Statement Stmt = conn.createStatement();

                  Stmt.executeUpdate("INSERT INTO "+this.getTableName()+" VALUES ("+this.getTournamentGroupId()+","+this.getTournamentId()+","+this.getRegistrationFee()+","+this.getTeeColorId()+")");

              Stmt.close();

            }
            catch (SQLException sq) {
                sq.printStackTrace(System.err);
            }
            finally {
              if (conn != null){
                freeConnection(conn);
              }
            }

        }


}
