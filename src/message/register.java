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
		System.out.println("user: " + userName +"\n" + " password: " +password);
		String salt = BCrypt.gensalt(12);
		String hashed_password = BCrypt.hashpw(password, salt);
		try {
			stmt = con.createStatement();
		    String sql = "INSERT INTO `messageusers`(username,password,salt) VALUE ('"+userName+"','"+hashed_password+"','"+salt+"');";
		    stmt.executeUpdate(sql);
		    con.close();
		    resp.put("status", "successful");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			resp.put("status", "failed");
		}
		response.getWriter().append(resp.toString());
	}

}
