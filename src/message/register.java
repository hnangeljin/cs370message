package message;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import java.sql.*;

/**
 * Servlet implementation class register
 */
@WebServlet("/register")
public class register extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public register() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 //TODO Auto-generated method stub
		JSONObject resp = new JSONObject();
		database db = new database();
		Connection con = db.connect();
		Statement stmt = null;
		String userName = request.getParameter("userName");
		String password = request.getParameter("password");
		System.out.println("register for user: " + userName);
		String salt = BCrypt.gensalt(12);//use Bcrypt and salt for save password
		String hashed_password = BCrypt.hashpw(password, salt);
		try {
			stmt = con.createStatement();
			//register user
		    String sql = "INSERT INTO `messageusers`(username,password,salt) VALUE ('"+userName+"','"+hashed_password+"','"+salt+"');";
		    stmt.executeUpdate(sql);
		    stmt.close();
		    //query the user_id
		    String stmtQuery2 = "select user_id from messageusers u where u.username = "+ "'"+ userName + "'";
			PreparedStatement pstmt2 = null;
			ResultSet rs2 = null;
			pstmt2 = con.prepareStatement(stmtQuery2);
			rs2 = pstmt2.executeQuery();
			rs2.next();
			String id = rs2.getString("user_id");
			resp.put("user_id", id);
			rs2.close();
			pstmt2.close();
			con.close();
		    resp.put("status", "successful");
		    System.out.println("register successful for user : " + userName);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("register failed for user : " + userName);
			resp.put("status", "failed");
		}
		response.getWriter().append(resp.toString());
	}

}
