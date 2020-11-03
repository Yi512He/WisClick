import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletException;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import static com.mongodb.client.model.Filters.*;

/**
 * Java servlet for displaying and editing the account page.
 * 
 * @author Joseph Eichenhofer, Emma He
 */
public class AccountPageServlet extends HttpServlet {

        /*
         * (non-Javadoc)
         * 
         * @see
         * javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
         * javax.servlet.http.HttpServletResponse)
         * 
         * Retrieve the session from HttpServletRequest.
	 * Get username from session to identify viewer's identity, 
	 * and thisUsername from Request to identify whose account page is displaying.
	 * If this viewer's identity is the same as the account page, 
         * allow the viewer to edit the account.
	 * Otherwise, the viewer can only view the page without editing. 
         * 
         */
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
                // Get session information
		HttpSession session = req.getSession(false);

		// if no session ,go to welcome page; otherwise, write out the account page.
		if(session == null){
			res.sendRedirect("/welcome");
		} else {
			// Get viewer's name from session, and user of the account page from request
			// then check if this page belongs to the viewer
			String username = (String) session.getAttribute("username");
			String thisUsername = req.getParameter("username");
			String pageUsername = username;
			if(thisUsername != null && !thisUsername.equals(username)){
				pageUsername = thisUsername;
			}

			// Set up the response content
			PrintWriter content = res.getWriter();
			res.setContentType("text/html; charset=htf-8");
			res.setStatus(HttpServletResponse.SC_OK);
			
			// Header ot eh HTML page, declare the title and css files
			content.println("<!DOCTYPE html>");
			content.println("<html>");
			content.println("<head>");
			content.println("<title>Account Page</title>");
    			content.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"AccountPage.css\">");
    			content.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"Navbar.css\">");
			content.println("</head>");

			// Body of the HTML page
			content.println("<body>");

			// Logo, course name, and navigation bar
			content.println("<div id=\"headerNav\">");
			content.print("<img src=\"./color-flush-reverse-UWlogo-print.png\" width=\"270\" height=\"90\" class=\"d-inline-block align-top\" alt=\"\">");
    			content.println("<span class=\"hello\">Logged in as "+ username +"</span>");
			content.println("<div id=\"stripe\">");
			content.println("<form action=\"logout\" method=\"POST\" class=\"logoutForm\"");
			content.println("accept-charset=\"utf-8\">");
			content.print("<a class=\"nav\" href=\"account\">My Account</a>");
			content.print("<a class=\"nav\" href=\"earn\">Earn Credits</a>");
			content.print("<a class=\"nav\" href=\"transfer\">Transfer Credits</a>");
			content.print("<a class=\"nav\" href=\"rank\">Ranking</a>");
                        content.print("<input value=\"Log Out\" type=\"submit\" class=\"logoutInput nav\"></form>");
			content.println("</div></div>");

			// Headline
			content.println("<h1>");
			content.println("This is " + pageUsername + "'s Page!");
			content.println("</h1>");

			// Display points of this account
			content.println("<div class=\"wrapper\">");
    			content.println("<div class=\"container short\">");
        		content.println("<h2 class=\"header\">" + pageUsername + "'s Credits</h2>");

       			content.println("<h3 class=\"oneLine\">");			
			content.println(Database.getPoints(pageUsername) + " Credits</h3>");
    			content.println("</div>");

			// Display the profile content of this account
    			content.println("<div class=\"container long\">");
        		content.println("<h2 class=\"header\">" + pageUsername + "'s Profile</h2>");
			String profile = Database.getProfile(pageUsername);		
			if(profile == null){
				profile = "";
			}
        		content.println("<div class=\"sevralLine\" id=\"profile\" >" + profile + "</div>");
			if(thisUsername == null || thisUsername.equals(username)){
        			content.println("<textarea placeholder=\"Change Your Profile\" class=\"inputField\">" + profile  + "</textarea>");
				content.println("<button id=\"submit\">Submit</button>");
				content.println("<button id=\"edit\">Edit</button>");
			}
    			content.println("</div>");
			
			
			// Define the js file, and close tags for this page
 			content.println("<script type=\"text/javascript\" src=\"./Account.js\"></script>");
			content.println("</body>");
			content.println("</html>");
			
                }
	}

 	/*
         * (non-Javadoc)
         * 
         * @see
         * javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest,
         * javax.servlet.http.HttpServletResponse)
         * 
         * Retrieve the session from HttpServletRequest.
	 * Retrieve the data from XMLHttpRequest. 
	 * Add the data to the database, and send a message with response.
         * 
         */
	@Override
        protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
                // Get session information
		HttpSession session = req.getSession(false);

		// if no session , go to the welcome page, otherwise, proceed updating data in account page.
                if(session == null){
                        res.sendRedirect("/welcome");
                } else {
			// get the username from session
			String username = (String) session.getAttribute("username");

			// Set up the response content
			res.setContentType("text/html; charset=utf-8");
			res.setStatus(HttpServletResponse.SC_OK);
                        res.setHeader("Cache-Control", "no-cache");
                        res.setHeader("Pragma", "no-cache");
                        PrintWriter out = res.getWriter();
                        
			// Get the data from request
                        BufferedReader reader = req.getReader();
                        StringBuilder sb = new StringBuilder();
			String line = reader.readLine();
                        while(line != null){
                                sb.append(line + "\n");
                                line = reader.readLine();
                        }
                        reader.close();
                        String params = sb.toString();
				
			// Parse the data into two parameters: type and value
                        String value = params.substring("value=".length(), params.length());

			// If data is added successfully, response with success message.
			// Otherwise, response with failure message.
			boolean result = false;
			result = Database.addAccountInfo(username, value, null, null);

			if(result){
                        	out.print("Success!");
			}else {
				out.print("Fail!");
			}
                }
        }
}
