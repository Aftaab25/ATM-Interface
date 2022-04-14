import java.util.ArrayList;
import java.util.Random;
//import java.util.Scanner;

public class Bank {
	
	/**
	 * The name of the bank.
	 */
	private String name;
	
	/**
	 * The account holders of the bank.
	 */
	private ArrayList<User> users;
	
	/**
	 * The accounts of the bank.
	 */
	private ArrayList<Account> accounts;
	
	/**
	 * to create a new Bank object with empty user and accounts list.
	 */
	public Bank(String name) {
		
		this.name = name;
		
		// init users and accounts
		users = new ArrayList<User>();
		accounts = new ArrayList<Account>();
		
	}
	
	/**
	 * Generate a new universally unique ID for a user.
	 */
	public String getNewUserUUID() {
		
		// inits
		String uuid;
		Random rng = new Random();
		int len = 6;
		boolean notUnique;
		
		// looping until we get a unique ID
		do {
			
			// generate the number
			uuid = "";
			for (int i=0; i<len; i++) {
				uuid += ((Integer)rng.nextInt(10)).toString();
			}
			
			//to make sure it's unique
			notUnique = false;
			for (User u : this.users) {
				if (uuid.compareTo(u.getUUID()) == 0) {
					notUnique = true;
					break;
				}
			}
			
		} while (notUnique);
		
		return uuid;
	}
	
	/**
	 * Generate a new universally unique ID for an account.
	 */
	public String getNewAccountUUID() {
		
		// inits
		String uuid;
		Random rng = new Random();
		int len = 10;
		boolean notUnique = false;
		
		// looping until we get a unique ID
		do {
			
			// generate the number
			uuid = "";
			for (int i=0; i<len; i++) {
				uuid += ((Integer)rng.nextInt(10)).toString();
			}
			
			// check to make sure it's unique
			for (Account a : this.accounts) {
				if (uuid.compareTo(a.getUUID()) == 0) {
					notUnique = true;
					break;
				}
			}
			
		} while (notUnique);
		
		return uuid;
				
	}

	/**
	 * Create a new user of the bank.
	 */
	public User addUser(String firstName, String lastName, String pin) {
		
		// create a new User object 
		User newUser = new User(firstName, lastName, pin, this);
		this.users.add(newUser);
		
		// savings account for the user 
		Account newAccount = new Account("Savings", newUser, this);
		newUser.addAccount(newAccount);
		this.accounts.add(newAccount);
		
		return newUser;
		
	}
	
	/**
	 * Add an existing account for the User.
	 */
	public void addAccount(Account newAccount) {
		this.accounts.add(newAccount);
	}
	
	/**
	 * Get the User object associated with a particular userID and pin, if they
	 * are valid.
	 */
	public User userLogin(String userID, String pin) {
		
		// search through list of users
		for (User u : this.users) {
			
			// if we find the user, and the pin is correct, return User object
			if (u.getUUID().compareTo(userID) == 0 && u.validatePin(pin)) {
				return u;
			}
		}
		
		// return null if user not found or incorrect password...		
		return null;
		
	}
	
	/**
	 * Get the name of the bank.
	 */
	public String getName() {
		return this.name;
	}

}
