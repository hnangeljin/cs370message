package message;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import message.database;

/**
 * Servlet implementation class login
 */
@WebServlet("/login")
public class login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public login() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		database db = new database();
		Connection con = db.connect();
		JSONObject resp = new JSONObject();
		String userName = request.getParameter("userName");
		String password = request.getParameter("password");
//		String userName = "ddddd";
//		String password = "1212";
		System.out.println("login for user : " + userName);
		String stmtQuery = "select * from messageusers u where u.username = ?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(stmtQuery);
			pstmt.setString(1, userName);
			rs = pstmt.executeQuery();
			rs.next();
	        String p = rs.getString("password");
	        String s = rs.getString("salt");
	        String id = rs.getString("user_id");
	        String token = rs.getString("login_token");;
	        rs.close();
	        pstmt.close();
			con.close();
			String hashed_password = BCrypt.hashpw(password, s);
			if (hashed_password.equals(p)){
				resp.put("loginToken", token);
				resp.put("status", "sucessful");
				resp.put("user_id", id);
				System.out.println("login successful for user : " + userName);
			}else{
				resp.put("status", "failed");
				System.out.println("login fail for user : " + userName);
				System.out.println("Error: Worng password");
			}
			
			
		
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			resp.put("status", "failed");
			System.out.println("login fail for user : " + userName);
			System.out.println("Error: First Query");
		
		}
        
		
		response.getWriter().append(resp.toString());
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
