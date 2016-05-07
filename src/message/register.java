package message;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
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
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
//		String userName = "ddddd";
//		String password = "1212";
//		String firstName = "apple";
//		String lastName = "james";
		//generate login token
		java.security.SecureRandom random=  new java.security.SecureRandom();
		byte[] buffer = new byte[16];
		random.nextBytes(buffer);
		org.apache.commons.codec.binary.Base64 encoder;
		String b64token = Base64.encodeBase64String(buffer);
		String authToken = b64token;
		System.out.println("register for user: " + userName);
		String salt = BCrypt.gensalt(12);//use Bcrypt and salt for save password
		String hashed_password = BCrypt.hashpw(password, salt);
		try {
			
			//register user 
		    String sql = "INSERT INTO `messageusers`(username,password,salt,firstName,lastName,login_token) VALUE (?,?,?,?,?,?);";
		    PreparedStatement preparedStmt = con.prepareStatement(sql);
		    preparedStmt.setString (1, userName);
		    preparedStmt.setString (2, hashed_password);
		    preparedStmt.setString (3, salt);
		    preparedStmt.setString (4, firstName);
		    preparedStmt.setString (5, lastName);
		    preparedStmt.setString (6, authToken);
		    preparedStmt.execute();
		    preparedStmt.close();
		    //query the user_id
		    String stmtQuery2 = "select user_id from messageusers u where u.username = ?";
			PreparedStatement pstmt2 = con.prepareStatement(stmtQuery2);
			pstmt2.setString(1, userName);
			ResultSet rs2 = pstmt2.executeQuery();
			rs2.next();
			String id = rs2.getString("user_id");
			resp.put("user_id", id);
			rs2.close();
			pstmt2.close();
			con.close();
		    resp.put("status", "successful");
		    resp.put("loginToken", authToken);
		    System.out.println("register successful for user : " + userName);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("register failed for user : " + userName);
			e.printStackTrace();
			resp.put("status", "failed");
		}
		response.getWriter().append(resp.toString());
	}

}
