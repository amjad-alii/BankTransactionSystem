# Bank Transaction System 🚀

A simple Java-based **Bank Transaction System** using JDBC and MySQL.

## 📌 Features
- Perform **money transfers** securely between accounts.
- Implements **transaction management** using JDBC.
- Ensures **data consistency** with `commit` and `rollback`.
- Uses **MySQL database** for account details.

## 🛠️ Technologies Used
- **Java (JDBC)**
- **MySQL**
- **Git & GitHub**

## 🚀 Getting Started

1️⃣ Clone the Repository**
```bash
git clone https://github.com/amjad-alii/BankTransactionSystem.git
cd BankTransactionSystem

2️⃣ Setup MySQL Database
Create a MySQL database:
CREATE DATABASE us_bank;

Create the account Table
CREATE TABLE account (
    account_no INT PRIMARY KEY,
    balance INT NOT NULL
);

Insert sample data:
INSERT INTO account (account_no, balance) VALUES (100, 5000), (101, 3000);

3️⃣ Run the Application
javac Main.java
java Main

