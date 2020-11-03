/**
 * Used for clearning up the database
 *
 * Wipe out the existing database collection LoginInformation
 * Create a new one with five users with basic information 
 *
 * @author Emma He
 */

// Connect to the mongoDB server
var connectMongo = new Mongo("localhost:27017");
var db = connectMongo.getDB("myDb");

// Delete the entire LoginInformation Collection if it exist
db.LoginInformation.drop();

// Create a new instance of LoginInformation Collection and insert initial info
db.createCollection("LoginInformation");

db.LoginInformation.insert([
	{username: "victim", password: "thevictim", points: 6000.0},
	{username: "Bucky", password: "badger", points: 5000.0},
	{username: "Miku", password: "vocal", points: 4900.0},
	{username: "Joe", password: "Doe", points: 4700.0},
	{username: "attacker", password: "theattacker", points: 500.0}
]);

// print out the data to check if the insertion is correct
myCursor = db.LoginInformation.find();
while (myCursor.hasNext()){
        printjson(myCursor.next());
}

