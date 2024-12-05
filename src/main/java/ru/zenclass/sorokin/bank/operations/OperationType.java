package ru.zenclass.sorokin.bank.operations;

public enum OperationType {
    /**
     * Create a new user
     */
    USER_CREATE,
    /**
     * Show all created users
     */
    SHOW_ALL_USERS,
    /**
     * Create a new account for the user
     */
    ACCOUNT_CREATE,
    /**
     * Close a users account
     */
    ACCOUNT_CLOSE,
    /**
     * Deposit amount money to the account
     */
    ACCOUNT_DEPOSIT,
    /**
     * Transfer amount money from source account to target account
     */
    ACCOUNT_TRANSFER,
    /**
     * Withdraw amount money to the account
     */
    ACCOUNT_WITHDRAW,
    /**
     * Exit from the program
     */
    EXIT
}
