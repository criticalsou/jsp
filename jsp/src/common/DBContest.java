package common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DBContest {
	
	public static void main(String[] args)
	{
		Connection con;
		try
		{
			DBCon db = new DBCon();
			con = db.getCon();
			System.out.println("연결성공");
			
			String sql="select * from user";
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next())
			{
				System.out.print(rs.getString("id") + " ,");
				System.out.print(rs.getString("pwd") + " ,");
				System.out.print(rs.getString("name"));
				System.out.println();

			}
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
