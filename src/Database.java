import java.lang.Object;
import java.lang.Math;
import java.util.*;
import java.lang.Integer.*;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.*;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Sorts;
import org.bson.Document;
import static com.mongodb.client.model.Filters.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

/**
 * Set up MongoDB JDBC driver to connect to the database
 *
 * @author Emma  He
 */
public class Database {

	// The number of users on the ranking.
	static final int RANK_NUM = 5;

	/*
	 * Create an instance of MongoDB database
	 */
	private static MongoDatabase getDatabase(){
		int port_number = 27017;
		String host = "localhost";

		MongoClient mongoClient = new MongoClient(host, port_number);
		return mongoClient.getDatabase("myDb");
	}

	/*
         * (non-Javadoc)
         *
         * Create a connect to the database and check if the inpur username and password are valid
         */
	public static boolean authenticateUser(String username, String password){
		boolean login = false;	
		MongoCollection<Document> collection = getDatabase().getCollection("LoginInformation");
		Document cursorName = collection.find(eq("username", username)).first();
		
		// If the username is not found, create a new account in the database
		if(cursorName == null){
			Document user = new Document("username", username);
			user.append("password", password);
			user.append("points", 0.0);
			collection.insertOne(user);
			login = true;
		}
		
		// if the username is found, check the password
		if(cursorName != null && cursorName.getString("password").equals(password)){
			login = true;
		}
			
		return login;
	}
	
	/* 
	 * (non-Javadoc)
	 * 
	 * Return the credits of the user
	 */
	public static int getPoints(String username) {
		MongoCollection<Document> collection = getDatabase().getCollection("LoginInformation");
		Document cursorName = collection.find(eq("username", username)).first();

		int curPoints = -1;

		if(cursorName == null){
			curPoints = -1;
		} else {
			Double num = cursorName.getDouble("points");
			curPoints = (int)Math.round(num);
		}
		
		return curPoints;
	}

	/*
	 * (non-Javadoc)
	 *
	 * Add credits to the user, return true if success
	 */
	public static boolean setPoints(String username, int points){
		MongoCollection<Document> collection = getDatabase().getCollection("LoginInformation");
		Document cursorName = collection.find(eq("username", username)).first();

		if(cursorName == null){
			System.out.println("Can not find username " + username);
			return false;
			
		} else {
			try {
				collection.updateOne(eq("username", username), new Document("$inc", new Document("points",points)));
				return true;	
			} catch(Exception ex){
				ex.printStackTrace();
				return false;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * Transfer points from one user to another, return true if success
	 *
	 */
	public static int transferPoints(String from, String to, int addPoints){
		int deductPoints = 0 - addPoints;
		
		MongoCollection<Document> collection = getDatabase().getCollection("LoginInformation");

		Document cursorName = collection.find(eq("username", from)).first();
		if(cursorName == null){
			return -1;
		}
		
		try {
			Double fromCurPoints = collection.find(eq("username", from)).first().getDouble("points");
			if(fromCurPoints < addPoints){
                                System.out.println("fromCurPoints:" + fromCurPoints + " and addPoints is: " + addPoints);
				return -1;
			}

			Double toCurPoints = collection.find(eq("username", to)).first().getDouble("points");
			
			try {
				collection.updateOne(eq("username", from), new Document("$inc", new Document("points", deductPoints)));
				collection.updateOne(eq("username", to), new Document("$inc", new Document("points", addPoints)));
				return (int)(fromCurPoints - addPoints);
			} catch(Exception ex){
				collection.updateOne(eq("username", from), new Document("$set", new Document("points", fromCurPoints)));
				collection.updateOne(eq("username", to), new Document("$set", new Document("points", toCurPoints)));

				return -1;
			}
		} catch(Exception e) {
			return -1;
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * Return the the list of top-five highest credits users
	 *
	 */
	public static ArrayList<String[]> getRankList(){
		MongoCollection<Document> collection = getDatabase().getCollection("LoginInformation");
		AggregateIterable<Document> documents = collection.aggregate(
			Arrays.asList(
				Aggregates.sort(Sorts.descending("points"))
			)
		);
		
		ArrayList<String[]> rankList = new ArrayList<String[]>();
		int counter = 0;
		for(Document document: documents) {
			counter++;
			String[] list = new String[2];
			int curPoints = (int)Math.round(document.getDouble("points"));
			list[0] = document.getString("username");
			list[1] = Integer.toString(curPoints);
			rankList.add(list);
			if(counter == RANK_NUM){
				break;
			}
		}

		return rankList;
	}

	/*
	 * (non-Javadoc)
	 *
	 * Return the profile content of the user
	 */
	public static String getProfile(String username){
		MongoCollection<Document> collection = getDatabase().getCollection("LoginInformation");
		
		try {
			Document cursorName = collection.find(eq("username", username)).first();
			return cursorName.getString("profile");
		} catch(Exception ex){
			return "";
		}	
	}

	/*
	 * (non-Javadoc)
	 *
	 * Return the link of the user
	 */
	public static String getLink(String username){
		MongoCollection<Document> collection = getDatabase().getCollection("LoginInformation");
		
		try {
			Document cursorName = collection.find(eq("username", username)).first();
			return cursorName.getString("link");
		} catch(Exception ex){
			return "";
		}	
	}

	/*
	 * (non-Javadoc)
	 *
	 * Add profile contents or link to the user
	 */
	public static boolean addAccountInfo(String username, String profile, String link, String pageLink){
		MongoCollection<Document> collection = getDatabase().getCollection("LoginInformation");

		try {	
			if(profile != null && profile.replace(" ", "").length() != 0){
				collection.updateOne(Filters.eq("username", username), Updates.set("profile", profile));
			}
	
			if(link != null && link.replace(" ", "").length() != 0){
				collection.updateOne(Filters.eq("username", username), Updates.set("link", link));
			}
	
			if(pageLink != null && pageLink.replace(" ", "").length() != 0){
				collection.updateOne(Filters.eq("username", username), Updates.set("pageLink", pageLink));
			}
			return true;
		} catch(Exception ex){
			return false;
		}
	}
}
