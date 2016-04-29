package message;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

/**
 * Servlet implementation class message
 */
@WebServlet("/message")
public class message extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public message() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//get the message
		JSONObject resp = new JSONObject();
		int total = 0;
		database db = new database();
		Connection con = db.connect();
		Statement stmt = null;
		//String reciever = request.getParameter("reciever");
		String reciever = "9";
		System.out.println("Start getting message for reciever: "+ reciever);
		//get the messages
		String stmtQuery = "select * from messages u where ( u.reciever_id ="+ reciever +") and isView = 'false' ORDER BY message_id;";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(stmtQuery);
			rs = pstmt.executeQuery();
			
			
			while (rs.next()){
				total ++;
				String r = rs.getString("reciever_id");
			    String s = rs.getString("sender_id");
			    String b = rs.getString("body");
			    String recieverNum = "reciever" + total;
			    String senderNum = "sender" + total;
			    String bodyNum = "body" + total;
			   
			    resp.put(recieverNum, r);
			    resp.put(senderNum, s);
			    resp.put(bodyNum, b);
		    }
			resp.put("total", total);
			resp.put("status", "successful");
	        rs.close();
	        pstmt.close();
			
	        //UPDATE `messages` SET `isView`='true';
	        
	        String sql2 = "UPDATE `messages` SET `isView`='true' WHERE reciever_id = "+ reciever;
	        Statement stmt2 = null;
	        ResultSet rs2 = null;
	        stmt2 = con.prepareStatement(sql2);
		    stmt2.executeUpdate(sql2);
		    stmt2.close();
			con.close();
			System.out.println("getting message sucessful for reciever : " + reciever);
			
		
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			resp.put("status", "failed");
			resp.put("user_id", "");
			System.out.println("getting message fail for reciever : " + reciever);
			System.out.println("Error: First Query");
			e1.printStackTrace();
		}
        
		
		response.getWriter().append(resp.toString());
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//saving message
		JSONObject resp = new JSONObject();
		database db = new database();
		Connection con = db.connect();
		Statement stmt = null;
		String sender = request.getParameter("sender");
		String reciever = request.getParameter("reciever");
		String body = request.getParameter("body");
		System.out.println("Start saving message from "+ sender + " to "+ reciever);
		try {
			stmt = con.createStatement();
		//save message
	    String sql = "INSERT INTO `messages`(sender_id,reciever_id ,body) VALUE ("+sender+","+reciever+",'"+body+"');";
	    stmt.executeUpdate(sql);
	    stmt.close();
	    con.close();
	    resp.put("status", "sucessful");
	    System.out.println("Sending message from "+ sender + " to "+ reciever + " successfully");
		} catch (SQLException e) {
			resp.put("status", "failed");
			System.out.println("saving message from "+ sender + " to "+ reciever + " failed");
		}
		response.getWriter().append(resp.toString());
		
		
	}

}
