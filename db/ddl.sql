-- Create schema
CREATE SCHEMA CS449Project;
USE CS449Project;

-- Users Table
CREATE TABLE Users (
    UserID INT NOT NULL AUTO_INCREMENT,
    Username VARCHAR(20) NOT NULL,
    Password VARCHAR(200) NOT NULL,
    PRIMARY KEY (UserID)
);

-- Categories Table
CREATE TABLE Categories (
    CategoryID INT NOT NULL AUTO_INCREMENT,
    UserID INT NOT NULL,
    Description VARCHAR(20) NOT NULL,
    PRIMARY KEY (CategoryID),
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
);

-- Accounts Table
CREATE TABLE Accounts (
    AccountID INT NOT NULL AUTO_INCREMENT,
    UserID INT NOT NULL,
    Description VARCHAR(20) NOT NULL,
    PRIMARY KEY (AccountID),
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
);

-- Transactions Table
CREATE TABLE Transactions (
    TransactionID INT NOT NULL AUTO_INCREMENT,
    UserID INT NOT NULL,
    AccountID INT NOT NULL,
    CategoryID INT NOT NULL,
    Description VARCHAR(20) NOT NULL,
    Amount FLOAT NOT NULL,
    Date DATE NOT NULL,
    PRIMARY KEY (TransactionID),
    FOREIGN KEY (UserID) REFERENCES Users(UserID),
    FOREIGN KEY (AccountID) REFERENCES Accounts(AccountID),
    FOREIGN KEY (CategoryID) REFERENCES Categories(CategoryID)
);

-- Type column added to Categories table in Sprint2
ALTER TABLE Categories ADD Type VARCHAR(20);

-- These views allow mariadb to work with lowercase table names
CREATE VIEW users AS SELECT * FROM Users;
CREATE VIEW categories AS SELECT * FROM Categories;
CREATE VIEW accounts AS SELECT * FROM Accounts;
CREATE VIEW transactions AS SELECT * FROM Transactions;

-- delete statements to remove android instrumentation test data
DELETE FROM accounts WHERE Description LIKE '@%';
DELETE FROM categories WHERE Description LIKE '@%';
DELETE FROM transactions WHERE Description LIKE '@%';

-- change amount column type
ALTER TABLE Transactions MODIFY amount DECIMAL(15,2);
