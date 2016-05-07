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
		//senderName
		//get the message
		JSONObject resp = new JSONObject();
		int total = 0;
		database db = new database();
		Connection con = db.connect();
		Statement stmt = null;
		String reciever = request.getParameter("reciever");
		String loginToken = request.getParameter("loginToken");
//		String reciever = "16";
//		String loginToken = "Fu+/liyaXDZBR4ysZX8tIg==";
		try {
			String sql = "select * from messageusers u where u.login_token = ?";
			PreparedStatement p = con.prepareStatement(sql);
			p.setString(1, loginToken);
			ResultSet result = p.executeQuery();
			result.next();
			String mid = result.getString("user_id");
			result.close();
			p.close();
			if(mid.equals(reciever)){
				System.out.println("Start getting message for reciever: "+ reciever);
				//get the messages
				String stmtQuery = "select * from messages u where ( u.reciever_id =?) and isView = 'false' ORDER BY message_id;";
				PreparedStatement pstmt = null;
				ResultSet rs = null;
    			pstmt = con.prepareStatement(stmtQuery);
    			pstmt.setString(1, reciever);
    			rs = pstmt.executeQuery();
    			while (rs.next()){
    				total ++;
    				String r = rs.getString("reciever_id");
    			    String s = rs.getString("sender_id");
    			    String b = rs.getString("body");
//    			    String fn = rs.getString("firstName");
//    			    String ln = rs.getString("lastName");
    			    String id = rs.getString("message_id");
    			  //UPDATE `messages` SET `isView`='true';
    		        
    		        String sql2 = "UPDATE `messages` SET `isView`='true' WHERE message_id =?;";
    		        PreparedStatement stmt2 = null;
    		        ResultSet rs2 = null;
    		        stmt2 = con.prepareStatement(sql2);
    		        stmt2.setString(1, id);
    			    stmt2.executeUpdate(sql2);
    			    stmt2.close();
    			    //get the name of the messageusers
    			    String sql3 = "select * from `messageusers` where user_id =?;";
    			    PreparedStatement pstmt3 = null;
    				ResultSet rs3 = null;
    			    pstmt3 = con.prepareStatement(sql3);
    			    pstmt3.setString(1, s);
    				rs3 = pstmt3.executeQuery();
    		        rs3.next();
    			    String fn = rs3.getString("firstName");
    			    String ln = rs3.getString("lastName");
    			    pstmt3.close();
    			    String name = ln + " " + fn;
    			    String recieverNum = "reciever" + total;
    			    String senderNum = "sender" + total;
    			    String bodyNum = "body" + total;
    			    String senderNameNum = "senderName" + total;
    			    resp.put(recieverNum, r);
    			    resp.put(senderNum, s);
    			    resp.put(bodyNum, b);
    			    resp.put(senderNameNum, name);
    		    }
    			resp.put("total", total);
    			resp.put("status", "successful");
    	        rs.close();
    	        pstmt.close();
    			con.close();
    			System.out.println("getting message sucessful for reciever : " + reciever);
			}else{
				resp.put("status", "failed");
				System.out.println("getting message fail for reciever : " + reciever);
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			resp.put("status", "failed");
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
		String sender = request.getParameter("sender");
		String reciever = request.getParameter("reciever");
		String body = request.getParameter("body");
		String loginToken = request.getParameter("loginToken");
//		String sender = "16";
//		String reciever = "17";
//		String body = "send a message 2";
//		String loginToken = "Fu+/==";
		try{
			String sql = "select * from messageusers u where u.login_token = ?";
			PreparedStatement p = con.prepareStatement(sql);
			p.setString(1, loginToken);
			ResultSet result = p.executeQuery();
			result.next();
			String mid = result.getString("user_id");
			result.close();
			p.close();
			if(mid.equals(sender)){
				System.out.println("Start saving message from "+ sender + " to "+ reciever);
				//save message
				String sql2 = "INSERT INTO `messages`(sender_id,reciever_id,body) VALUE (?,?,?);";
				PreparedStatement stmt = con.prepareStatement(sql2);
				int n1 = 0;//if the sender and reciever is not convertible to int, error will be handle in sql
				int n2 = 0;
				try {
					n1 = Integer.parseInt(sender);
					n2 = Integer.parseInt(reciever);
				} catch (NumberFormatException e) {
					System.out.println("Error in the post data");
				}
				stmt.setInt(1, n1);
				stmt.setInt(2, n2);
				stmt.setString(3, body);
				stmt.execute();
				stmt.close();
				con.close();
				resp.put("status", "sucessful");
				System.out.println("Sending message from "+ sender + " to "+ reciever + " successfully");
			}else{
				resp.put("status", "failed");
				System.out.println("saving message fail from "+ sender + " to "+ reciever);
			}
		} catch (SQLException e) {
			resp.put("status", "failed");
			System.out.println("saving message from "+ sender + " to "+ reciever + " failed");
		}
		response.getWriter().append(resp.toString());
	}

}
