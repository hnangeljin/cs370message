package message;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

/**
 * Servlet implementation class friend
 */
@WebServlet("/friend")
public class friend extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public friend() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//getting the friendlist
		int total = 0;
		JSONObject resp = new JSONObject();
		database db = new database();
		Connection con = db.connect();
		String f1 = request.getParameter("friend1");
		String loginToken = request.getParameter("loginToken");
//		String f1 = "16";
//		String loginToken = "Fu+/liyaXDZBR4ysZX8tIg==";

		try{
			String sql = "select * from messageusers u where u.login_token = ?;";
			PreparedStatement p = con.prepareStatement(sql);
			p.setString(1, loginToken);
			ResultSet result = p.executeQuery();
			result.next();
			String mid = result.getString("user_id");
			result.close();
			p.close();
			if(mid.equals(f1)){
				System.out.println("Start geting friend list for "+ f1);
				//save message
				String sql2 = "select * from messagefriends u where u.friend1 = ?;";
				PreparedStatement stmt = con.prepareStatement(sql2);
				int n1 = 0;//if the sender and reciever is not convertible to int, error will be handle in sql
				try {
					n1 = Integer.parseInt(f1);
				} catch (NumberFormatException e) {
					System.out.println("Error in the get data");
				}
				stmt.setInt(1, n1);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()){
					total++;
					String f = rs.getString("friend2");
					String friendNum = "friend" + total;
    			    resp.put(friendNum, f);
				}
				rs.close();
				stmt.close();
				con.close();
				resp.put("total", total);
				resp.put("status", "sucessful");
				System.out.println("Start geting friend list for "+ f1 + " successfully");
			}else{
				resp.put("status", "failed");
				System.out.println("Start geting friend list for "+ f1 + " failed");
			}
		} catch (SQLException e) {
			resp.put("status", "failed");
			System.out.println("Start geting friend list for "+ f1 + " failed");
		}
		response.getWriter().append(resp.toString());
	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//insert the friendlist
			JSONObject resp = new JSONObject();
			database db = new database();
			Connection con = db.connect();
			String f1 = request.getParameter("friend1");
			String f2 = request.getParameter("friend2");
			String loginToken = request.getParameter("loginToken");
//			String f1 = "16";
//			String f2 = "17";
//			String loginToken = "Fu+/liyaXDZBR4ysZX8tIg==";

			try{
				String sql = "select * from messageusers u where u.login_token = ?";
				PreparedStatement p = con.prepareStatement(sql);
				p.setString(1, loginToken);
				ResultSet result = p.executeQuery();
				result.next();
				String mid = result.getString("user_id");
				result.close();
				p.close();
				if(mid.equals(f1)){
					System.out.println("Start saving friend list for "+ f1);
					//save message
					String sql2 = "insert into messagefriends(friend1, friend2) value (?,?);";
					PreparedStatement stmt = con.prepareStatement(sql2);
					int n1 = 0;//if the f1 and f2 is not convertible to int, error will be handle in sql
					int n2 = 0;
					try {
						n1 = Integer.parseInt(f1);
						n2 = Integer.parseInt(f2);
					} catch (NumberFormatException e) {
						System.out.println("Error in the post data");
					}
					stmt.setInt(1, n1);
					stmt.setInt(2, n2);
					stmt.execute();
					stmt.close();
					String sql3 = "insert into messagefriends(friend1, friend2) value (?,?);";
					PreparedStatement stmt2 = con.prepareStatement(sql3);
					stmt2.setInt(1, n2);
					stmt2.setInt(2, n1);
					stmt2.execute();
					stmt2.close();
					con.close();
					resp.put("status", "sucessful");
					System.out.println("Start saving friend list for "+ f1 + " successfully");
				}else{
					resp.put("status", "failed");
					System.out.println("Start saving friend list for "+ f1 + " failed");
				}
			} catch (SQLException e) {
				resp.put("status", "failed");
				System.out.println("Start geting friend list for "+ f1 + " failed");
			}
			response.getWriter().append(resp.toString());
	}

}
