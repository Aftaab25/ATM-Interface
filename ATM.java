import java.util.Scanner;

public class ATM {
	public static void main(String[] args) {
		
		// init Scanner
		Scanner sc = new Scanner(System.in);
		
		// init Bank
		Bank bank_ = new Bank("Bank of Drausin");
		
		// add a user
		User aUser = bank_.addUser("John", "Doe", "1234");
		
		// add a checking account 
		Account newAccount = new Account("Checking", aUser, bank_);
		aUser.addAccount(newAccount);
		bank_.addAccount(newAccount);
		User curUser;
		
		// continue looping forever
		while (true) {	
			// stay in login prompt until successful login
			curUser = ATM.mainMenuPrompt(bank_, sc);			
			// stay in main menu until user quits
			ATM.printUserMenu(curUser, sc);			
		}
	}
	
	/**
	 * Print the ATM's login menu.
	 */
	public static User mainMenuPrompt(Bank bank_, Scanner sc) {
		// inits
		String userID;
		String pin;
		User authUser;
		
		// prompt user for user ID/pin combo until a correct one is reached
		do {
			System.out.printf("\n\nWelcome to %s\n\n", bank_.getName());		
			System.out.print("Enter user ID: ");
			userID = sc.nextLine();
			System.out.print("Enter pin: ");
			pin = sc.nextLine();
			
			// to get user object according to the user ID and pin combination
			authUser = bank_.userLogin(userID, pin);
			if (authUser == null) {
				System.out.println("Incorrect user ID/pin combination. " + 
						"Please try again");
			}
		} while(authUser == null); 
		return authUser;
	}
	
	/**
	 * Print the ATM's menu for user actions.
	 */
	public static void printUserMenu(User theUser, Scanner sc) {
		// print a summary of the user's accounts
		theUser.printAccountsSummary();
		int choice;

		// user menu
		outer: do {
			System.out.println("What would you like to do?");
			System.out.println("  1) Show account transaction history");
			System.out.println("  2) Withdraw");
			System.out.println("  3) Deposit");
			System.out.println("  4) Transfer");
			System.out.println("  5) Quit");
			System.out.println();
			System.out.print("Enter choice: ");
			choice = sc.nextInt();
		
		// process the choice
		switch (choice) {
		case 1:
			ATM.showTransHistory(theUser, sc);
			break;
		case 2:
			ATM.withdrawFunds(theUser, sc);
			break;
		case 3:
			ATM.depositFunds(theUser, sc);
			break;
		case 4:
			ATM.transferFunds(theUser, sc);
			break;
		case 5:
			break outer;
		default:
			System.out.println("Invalid Input!");
		}
	} while (1!=0);
}
	
	/**
	 * transferring funds from one account to another.
	 */
	public static void transferFunds(User theUser, Scanner sc) {
		int fromAcc;
		int toAcc;
		double amount;
		double accBalance;
		
		// get account to transfer from
		do {
			System.out.printf("Enter the account number (1-%d) to " + 
					"transfer from: ", theUser.numAccounts());
			fromAcc = sc.nextInt()-1;
			if (fromAcc < 0 || fromAcc >= theUser.numAccounts()) {
				System.out.println("Invalid account. Please try again.");
			}
		} while (fromAcc < 0 || fromAcc >= theUser.numAccounts());
		accBalance = theUser.getAccBalance(fromAcc);
		
		// get account to transfer to
		do {
			System.out.printf("Enter the account number (1-%d) to " + 
					"transfer to: ", theUser.numAccounts());
			toAcc = sc.nextInt()-1;
			if (toAcc < 0 || toAcc >= theUser.numAccounts()) {
				System.out.println("Invalid account. Please try again.");
			}
		} while (toAcc < 0 || toAcc >= theUser.numAccounts());
		
		// get amount to transfer
		do {
			System.out.printf("Enter the amount to transfer (max Rs.%.02f): Rs.", 
					accBalance);
			amount = sc.nextDouble();
			if (amount < 0) {
				System.out.println("Amount must be a positive integer.");
			} else if (amount > accBalance) {
				System.out.printf("Amount must be less than balance " +
						"of Rs..02f.\n", accBalance);
			}
		} while (amount < 0 || amount > accBalance);
		
		// finally, do the transfer 
		theUser.addAccTransaction(fromAcc, -1*amount, String.format(
				"Transfer to account %s", theUser.getAcc_uuid(toAcc)));
		theUser.addAccTransaction(toAcc, amount, String.format(
				"Transfer from account %s", theUser.getAcc_uuid(fromAcc)));
	}
	
	/**
	 * Process a fund withdraw from an account.
	 */
	public static void withdrawFunds(User theUser, Scanner sc) {
		int fromAcc;
		double amount;
		double accBalance;
		String memo;
		
		// get account to withdraw from
		do {
			System.out.printf("Enter the account number (1-%d) to " + 
					"withdraw from: ", theUser.numAccounts());
			fromAcc = sc.nextInt()-1;
			if (fromAcc < 0 || fromAcc >= theUser.numAccounts()) {
				System.out.println("Invalid account. Please try again.");
			}
		} while (fromAcc < 0 || fromAcc >= theUser.numAccounts());
		accBalance = theUser.getAccBalance(fromAcc);
		
		// get amount to transfer
		do {
			System.out.printf("Enter the amount to withdraw (max Rs.%.02f): Rs.", 
					accBalance);
			amount = sc.nextDouble();
			if (amount < 0) {
				System.out.println("Amount cannot be zero or negative.");
			} else if (amount > accBalance) {
				System.out.printf("Amount must be less than balance " +
						"of Rs.%.02f.\n", accBalance);
			}
		} while (amount < 0 || amount > accBalance);
		
		// gobble up rest of previous input
		sc.nextLine();
		
		// get a memo
		System.out.print("Enter a memo: ");
		memo = sc.nextLine();
		
		// do the withdrwal
		theUser.addAccTransaction(fromAcc, -1*amount, memo);
	}
	
	/**
	 * Process a fund deposit to an account.
	 */
	public static void depositFunds(User theUser, Scanner sc) {
		int toAcc;
		double amount;
		String memo;
		
		// get account to withdraw from
		do {
			System.out.printf("Enter the account number (1-%d) to " + 
					"deposit to: ", theUser.numAccounts());
			toAcc = sc.nextInt()-1;
			if (toAcc < 0 || toAcc >= theUser.numAccounts()) {
				System.out.println("Invalid account. Please try again.");
			}
		} while (toAcc < 0 || toAcc >= theUser.numAccounts());
		
		// get amount to transfer
		do {
			System.out.printf("Enter the amount to deposit: Rs.");
			amount = sc.nextDouble();
			if (amount < 0) {
				System.out.println("Amount must be greater than zero.");
			} 
		} while (amount < 0);
		
		// gobble up rest of previous input
		sc.nextLine();
		
		// get a memo
		System.out.print("Enter a memo: ");
		memo = sc.nextLine();
		
		// do the deposit
		theUser.addAccTransaction(toAcc, amount, memo);
	}
	
	/**
	 * Show the transaction history for an account.
	 */
	public static void showTransHistory(User theUser, Scanner sc) {
		int theAcc;
		
		// get account whose transactions to print
		do {
			System.out.printf("Enter the account number (1-%d) \nwhose " + 
					"transactions you want to see: ", theUser.numAccounts());
			theAcc = sc.nextInt()-1;
			if (theAcc < 0 || theAcc >= theUser.numAccounts()) {
				System.out.println("Invalid account. Please try again.");
			}
		} while (theAcc < 0 || theAcc >= theUser.numAccounts());
		
		// print the transaction history
		theUser.printAccHistory(theAcc);
	}
}
