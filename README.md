# Finance App

### Nathan Gawith

### University of Missouri – Kansas City
# **Vision Statement**

Scroll down to view Original Mockup Drawings

Finance App is inspired by my mother&#39;s small business needs for bookkeeping but could also be used as a personal finance tracker. The app&#39;s main functionality is to allow the user to input expenses and income, view reports over month/year timelines, and reconcile balances between bank accounts and what the user has input into the system. Every transaction will allow the user to associate a date, description, category, and amount so that later reports can be ran based on those amounts, dates, and categories.

When the user opens the application, he/she will be greeted with a login screen which they will type in their username/password into and then move on to the main welcome screen. From the welcome screen the user will be able to perform 5 actions:

1. Accounts - View current account balances and totals
2. Income - Log an income transaction (ex: client payment for a service)
3. Expense - Log an expense transaction (ex: hotel, rental car, travel food, paychecks)
4. Register - View a log of all: open/close account transactions, income/expense transactions
5. Reports - View a concise report 1) over the selected time period, 2) broken out based on accounts or categories

Stretch Goals:

1. Sign in with Google.
2. Add a page specifically for transfers rather than logging a separate expense/income.
3. Filter register based on category, description, etc.
4. Specify specific categories/accounts to include in a report rather than all categories/accounts.
5. Prompt to transfer balance to another account when deleting an account.
6. Subcategories (Travel category could include Gas, Hotel, and Food subcategories)
7. Allow reports to be generated through a website so that they can easily be printed from a desktop or laptop.
8. Scheduled transactions (ex: paycheck for each employee every two weeks)
9. Attach a photo of a receipt to each income/expense transaction
10. Localization (allow for non-English languages throughout the application)

# **Requirements**

| **Actor** | Goal |
| --- | --- |
| Administrator | Should be able to set up an instance of the api and database on a new linux or windows system in less than 30 minutes. |
| Administrator | Should be able to create a new user account via a simple insert statement until Google authentication is implemented. |
| User | Should be able to log in to the application with a username/password or with Google authentication. |
| User | Should be able to edit accounts, income categories, expense categories, and their first/last name as well as their password from the Settings screen. |
| User | Should be able to track an income transaction along with the following items: Amount, Category, Date, Account, Description from the Income screen. |
| User | Should be able to track an expense transaction along with the following items: Amount, Category, Date, Account, Description from the Expense screen. |
| User | Should be able to view a list of all their configured accounts and each account&#39;s total from the Accounts screen. |
| User | Should be able to view a list of all their transactions and filter by category/account from the Register screen. |
| User | Should be able to transfer money from one account to another with a specified Date, Description, From/To Account, and Amount from the Transfer screen. |
| User | Should be able to easily generate a report over a specified date range by category or by account from the Reports screen. |

### Product Backlog

| **Story ID** | **Story** | Story Points | Priority | Status |
| --- | --- | --- | --- | --- |
| 1 | Create Api Project, Android Project, and Database DDL scripts, and verify they can all talk to each other | 6 | 1.0 | Sprint1 |
| 2 | Create Login screen, allow user to log in to the application securely, and configure API URL | 4 | 1.0 | Sprint1 |
| 3 | Create Settings screen that allows for: - Creation of new accounts - Creation of new income categories - Creation of new expense categories | 6 | 1.1 | TODO |
| 4 | Create the Income/Expense screen with: - Amount money field - Category dropdown - Date calendar input - Account dropdown - Description text field | 6 | 1.2 | TODO |
| 5 | Create the Register screen with: - Total balance at top - List of transactions - Buttons to link to income/expense/transfer screens | 8 | 1.3 | TODO |
| 6 | Create Reports screen with: - Start/End date calendar inputs - Breakpoint selection (Month, Year) - Type selection (Category, Account) | 2 | 1.4 | TODO |
| 7 | Create Report view screen with: - Table view of the report configured in the Reports screen | 6 | 1.5 | TODO |
| 8 | Allow the user to edit/delete an account name, income/expense category or first/last name from the Settings screen | 2 | 2.0 | TODO |
| 9 | Create Transfer screen with: - Same fields as Income/Expense screen - And To/From account dropdowns | 4 | 2.1 | TODO |
| 10 | Integrate Google authentication | 3 | 3.0 | TODO |
| 11 | Filter Register based on Category, Description, Amount, etc. | 3 | 4.0 | TODO |
| 12 | Filter Reports based on specific categories/accounts | 4 | 4.1 | TODO |
| 13 | Prompt to transfer balance to another account when deleting an account | 1.5 | 4.2 | TODO |
| 14 | Subcategories | 6 | 4.3 | TODO |
| 14 | Allow reports to be generated through a website accessible on a laptop or desktop | 7 | 4.3 | TODO |
| 15 | Scheduled Transactions | 5 | 4.4 | TODO |
| 16 | Attach a photo of a receipt to each income/expense transaction | 5 | 4.5 | TODO |
| 17 | Localization | 10 | 4.6 | TODO |

# Sprint #1

**Sprint Goal: Get project set up so that screens can easily be created in future sprints**

Sprint Backlog

| Story ID | Story / Task | EstimatedHours | ActualHours |
| --- | --- | --- | --- |
| 1 | Create Api, App, and DB, verify they can all talk to each other |||
| 1.1 | Create API Project | 1:00 | 3:54 |
| 1.2 | Create Android Project | 1:00 | 1:21 |
| 1.3 | Create Database DDL scripts | 2:00 | 0:21 |
| 1.4 | Verify API, App, and DB can talk with each other | 2:00 | 1:36 |
| 2 | Create Login screen, allow user to log in to the application securely |||
| 2.1 | Create Login screen, add theming, logo, username/password text fields | 1:30 | 3:19 |
| 2.2 | Allow the user to log in securely given the user has been set up by an admin in the database | 2:00 | 1:21 |
| 2.3 | Allow the API URL to be configured from the Login screen | 0:30 | 1:10 |
| --- | --- | --- | --- |
|| Total | 10:00 | 13:02 |

## Review

Video showing that the login screen works and if you enter the wrong password, an error message is displayed: https://youtu.be/ewMNUNOdaCw

As you can see from the screenshots below, what I achieved this sprint was:
- Created API, DB, and APP projects.
- Login works and a JWT is issued to the client if login is valid.
- Text boxes and button is disabled during api call so that the user can’t spam the api with login requests.
- If login is invalid, then an error message is displayed to the user.

## Retrospective

What went well:
- Completed all tasks that I brought into the sprint with unit tests
- Set up working API on my raspberry pi so I can use the API on http://pi.nathangawith.com/ 
- Met with client (mother), showed progress, and got feedback
    - Feedback:
    - Button to view password
    - Enter key should log in
    - Update loading bar to cleaner looking loading bar
    - Remove api url from login screen (this will probably stay during development)

What didn’t go well:
- Learning how to write unit tests with the spring framework in Java for the API took a few hours
- Learning how to write unit tests for android took a few hours
- Hopefully these hours were well spent and will help speed up unit testing for future sprints.
- Estimates were a little bit low (estimated 10 hours, spent 13 hours)

Changes for future sprints:
- Although time estimates were low this sprint, I think that estimates will be better for the next sprint since I won’t have the overhead of learning unit tests.
- I would like to finish my tasks in time so that I can implement customer feedback during the next sprint.

# Sprint 1 Screenshots:

![iter1_login_blank](readme_assets/iter1_login_blank.png?raw=true)
![iter1_login_loading](readme_assets/iter1_login_loading.png?raw=true)
![iter1_login_token_received](readme_assets/iter1_login_token_received.png?raw=true)
![iter1_login_invalid](readme_assets/iter1_login_invalid.png?raw=true)


# Original Mockup Drawings

![login](readme_assets/login.jpg?raw=true)
![accounts](readme_assets/accounts.jpg?raw=true)
![dev_settings](readme_assets/dev_settings.jpg?raw=true)
![home](readme_assets/home.jpg?raw=true)
![income_expense](readme_assets/income_expense.jpg?raw=true)
![register](readme_assets/register.jpg?raw=true)
![report_view](readme_assets/report_view.jpg?raw=true)
![reports](readme_assets/reports.jpg?raw=true)
![settings](readme_assets/settings.jpg?raw=true)
![transfer](readme_assets/transfer.jpg?raw=true)
![login](readme_assets/login.jpg?raw=true)