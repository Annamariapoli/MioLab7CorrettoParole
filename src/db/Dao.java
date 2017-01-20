package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import bean.Parola;

public class Dao {

	public List<Parola> getParoleLung(int lunghezza){
		String query ="select * from parola where Length(nome)=?";
		Connection conn=DBConnect.getConnection();
		try{
			PreparedStatement st = conn.prepareStatement(query);
			List<Parola> parole = new LinkedList<Parola>();
			st.setInt(1, lunghezza);
			ResultSet res = st.executeQuery();
			while(res.next()){
				Parola p = new Parola(res.getInt("id"), res.getString("nome"));
				parole.add(p);
			}
			conn.close();
			return parole;
		}catch(SQLException e ){
			e.printStackTrace();
			return null;
		}
	}
}
