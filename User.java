import java.security.MessageDigest;
import java.util.ArrayList;

public class User {

	/**
	 * The first name of the user.
	 */
	private String firstName;
	
	/**
	 * The last name of the user.
	 */
	private String lastName;
	
	/**
	 * The ID number of the user.
	 */
	private String uuid;
	
	/**
	 * The hash of the user's pin number.
	 */
	private byte pinHash[];
	
	/**
	 * The list of accounts for this user.
	 */
	private ArrayList<Account> accounts;
	
	/**
	 * Create new user
	 * the user's first name
	 * the user's last name
	 * the user's account pin number (as a String)
	 * the bank that the User is a customer of
	 */
	public User (String firstName, String lastName, String pin, Bank bank_) {
		
		// set user's name
		this.firstName = firstName;
		this.lastName = lastName;
		
		// store the pin's MD5 hash, instead of original value for security reasons
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			this.pinHash = md.digest(pin.getBytes());
		} catch (Exception e) {
			System.err.println("error, caught exeption : " + e.getMessage());
			System.exit(1);
		}
		
		// to get a new, universal unique ID for the user
		this.uuid = bank_.getNewUserUUID();
		
		// create empty list of accounts
		this.accounts = new ArrayList<Account>();
		
		// print log message
		System.out.printf("New user %s, %s with ID %s created.\n", 
				lastName, firstName, this.uuid);
		
	}
	
	/**
	 * Get the user ID number
	 */
	public String getUUID() {
		return this.uuid;
	}
	
	/**
	 * Add an account for the user.
	 */
	public void addAccount(Account anAcct) {
		this.accounts.add(anAcct);
	}
	
	/**
	 * Get the number of accounts the user has.
	 */
	public int numAccounts() {
		return this.accounts.size();
	}
	
	/**
	 * Get the balance of a particular account.
	 */
	public double getAccBalance(int acctIdx) {
		return this.accounts.get(acctIdx).getBalance();
	}
	
	/**
	 * Get the UUID of a particular account.
	 */
	public String getAcc_uuid(int acctIdx) {
		return this.accounts.get(acctIdx).getUUID();
	}
	
	/**
	 * Print transaction history for a particular account.
	 */
	public void printAccHistory(int acctIdx) {
		this.accounts.get(acctIdx).printTransHistory();
	}
	
	/**
	 * Add a transaction to a particular account.
	 */
	public void addAccTransaction(int acctIdx, double amount, String memo) {
		this.accounts.get(acctIdx).addTransaction(amount, memo);
	}
	
	/**
	 * to check whether a given pin matches the true User pin
	 */
	public boolean validatePin(String aPin) {
		
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			return MessageDigest.isEqual(md.digest(aPin.getBytes()), 
					this.pinHash);
		} catch (Exception e) {
			System.err.println("error, caught exeption : " + e.getMessage());
			System.exit(1);
		}
		
		return false;
	}
	
	/**
	 * Print summary for the accounts of this particular user.
	 */
	public void printAccountsSummary() {
		
		System.out.printf("\n\n%s's accounts summary\n", this.firstName);
		for (int a = 0; a < this.accounts.size(); a++) {
			System.out.printf("%d) %s\n", a+1, 
					this.accounts.get(a).getSummaryLine());
		}
		System.out.println();
		
	}
}
