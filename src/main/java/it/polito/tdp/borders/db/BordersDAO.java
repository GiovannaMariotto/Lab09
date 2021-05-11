package it.polito.tdp.borders.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.borders.model.Border;
import it.polito.tdp.borders.model.Country;

public class BordersDAO {

	public List<Country> loadAllCountries() {

		String sql = "SELECT ccode, StateAbb, StateNme FROM country ORDER BY StateAbb";
		List<Country> result = new ArrayList<Country>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				System.out.format("%d %s %s\n", rs.getInt("ccode"), rs.getString("StateAbb"), rs.getString("StateNme"));
				result.add(new Country(rs.getString("StateNme"),rs.getString("StateAbb"), rs.getInt("ccode")));
			}
			
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<Border> getCountryPairs(int anno, Map<Integer, Country> idMap) {
		String sql ="SELECT c1.year, c1.state1no, c1.state1ab, c1.state2no, c1.state2ab "
				+ "FROM contiguity c1, country c2, country c3 "
				+ "WHERE c1.state1no=c2.CCode AND c1.year<=? AND c1.conttype=? AND c3.CCode=c1.state2no AND c3.CCode!=c2.CCode "
				;
		List<Border> borderArray = new ArrayList<Border>();
		Connection conn =ConnectDB.getConnection();
		try {
			
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno); 
			st.setInt(2, 1);//conttype='1'
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				Country c1;
				Country c2;
				 if(idMap.containsKey(rs.getInt("state1no")) && idMap.containsKey(rs.getInt("state2no"))) { 
					  c1 = idMap.get(rs.getInt("state1no"));
					  c2 =idMap.get(rs.getInt("state2no"));
					  if(c1!=null && c2!=null) {
						  Border b = new Border(c1,c2,rs.getInt("year"));
						  borderArray.add(b);
					  }
				 }
				
				
			}
			rs.close();
			st.close();
			conn.close();
			
			
		}catch(SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		
		return borderArray;
	}
}
