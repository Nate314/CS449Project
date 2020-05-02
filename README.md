# Finance App

### Nathan Gawith
### University of Missouri – Kansas City

# Table of Contents
- [Finance App](#finance-app)
- [Table of Contents](#table-of-contents)
- [Vision Statement](#vision-statement)
- [Requirements](#requirements)
- [Product Backlog](#product-backlog)
- [Original Mockups](#original-mockups)

| Sprints 1-3 | Sprints 4-6 |
| --- | --- |
| - [Sprint 1](#sprint-1) - [Review](#sprint-1-review) - [Retrospective](#sprint-1-retrospective) - [Screenshots](#sprint-1-screenshots) | - [Sprint 4](#sprint-4) - [Review](#sprint-4-review) - [Retrospective](#sprint-4-retrospective) - [Screenshots](#sprint-4-screenshots)  |
| - [Sprint 2](#sprint-2) - [Review](#sprint-2-review) - [Retrospective](#sprint-2-retrospective) - [Screenshots](#sprint-2-screenshots) | - [Sprint 5](#sprint-5) - [Review](#sprint-5-review) - [Retrospective](#sprint-5-retrospective) - [Screenshots](#sprint-5-screenshots)  |
| - [Sprint 3](#sprint-3) - [Review](#sprint-3-review) - [Retrospective](#sprint-3-retrospective) - [Screenshots](#sprint-3-screenshots) | - [Sprint 6](#sprint-6) - [Review](#sprint-6-review) - [Retrospective](#sprint-6-retrospective) - [Screenshots](#sprint-6-screenshots)  |
<!--
console.log(Array(3).fill(null).map((v, index) => {
    let result = '';
    let i;
    i = index + 1;
    result += `| - [Sprint ${i}](#sprint-${i}) - [Review](#sprint-${i}-review) - [Retrospective](#sprint-${i}-retrospective) - [Screenshots](#sprint-${i}-screenshots) `;
    i = index + 4;
    result += `| - [Sprint ${i}](#sprint-${i}) - [Review](#sprint-${i}-review) - [Retrospective](#sprint-${i}-retrospective) - [Screenshots](#sprint-${i}-screenshots) `;
    return result + ' |';
}).join('\n'));
-->

# Vision Statement

 [Original Mockups](#original-mockups)

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

# Requirements

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

# Product Backlog

| **Story ID** | **Story** | Story Points | Priority | Status |
| --- | --- | --- | --- | --- |
| ~~1~~ | ~~Create Api Project, Android Project, and Database DDL scripts, and verify they can all talk to each other~~ | ~~6~~ | ~~1.0~~ | ~~Sprint1~~ |
| ~~2~~ | ~~Create Login screen, allow user to log in to the application securely, and configure API URL~~ | ~~4~~ | ~~1.0~~ | ~~Sprint1~~ |
| ~~3~~ | ~~Create Settings screen that allows for: - Creation of new accounts - Creation of new income categories - Creation of new expense categories~~ | ~~6~~ | ~~1.1~~ | ~~Sprint2~~ |
| ~~4~~ | ~~Create the Income/Expense screen with: - Amount money field - Category dropdown - Date calendar input - Account dropdown - Description text field~~ | ~~6~~ | ~~1.2~~ | ~~Sprint2~~ |
| ~~5~~ | ~~Create the Register screen with: - Total balance at top - List of transactions - Buttons to link to income/expense/transfer screens~~ | ~~10~~ | ~~1.3~~ | ~~Sprint3~~ |
| ~~6~~ | ~~Create Reports screen with: - Start/End date calendar inputs - Breakpoint selection (Month, Year) - Type selection (Category, Account)~~ | ~~3~~ | ~~1.4~~ | ~~Sprint4~~ |
| ~~7~~ | ~~Create Report view screen with: - Table view of the report configured in the Reports screen~~ | ~~7~~ | ~~1.5~~ | ~~Sprint4~~ |
| ~~8~~ | ~~Allow the user to edit/delete an account name, income/expense category from the Settings screen~~ | ~~5~~ | ~~2.0~~ | ~~Sprint5~~ |
| ~~9~~ | ~~Create Account/Category Transfer screens with: - Same fields as Income/Expense screen - And To/From account/category dropdowns~~ | ~~5~~ | ~~2.1~~ | ~~Sprint5~~ |
| 19 | Edit/Delete income/expense/transfers from register screen | 5 | 2.3 | Sprint6 |
| 20 | Fix all bugs found in Sprint5 | 3.5 | 2.3 | Sprint6 |
| 21 | Add Side Navigation Menu | 1.5 | 2.4 | Sprint6 |
| 18 | Allow the user to edit their first/last name from the settings screen | 1.5 | 2.4 | TODO |
| 10 | Integrate Google authentication | 4 | 3.0 | TODO |
| 11 | Filter Register based on Category, Description, Amount, etc. | 3 | 4.0 | TODO |
| 12 | Filter Reports based on specific categories/accounts | 4 | 4.1 | TODO |
| ~~13~~ | ~~Prompt to transfer balance to another account when deleting an account~~ | ~~1.5~~ | ~~4.2~~ | ~~Sprint5~~ |
| 14 | Subcategories | 6 | 4.3 | TODO |
| 14 | Allow reports to be generated through a website accessible on a laptop or desktop | 7 | 4.3 | TODO |
| 15 | Scheduled Transactions | 5 | 4.4 | TODO |
| 16 | Attach a photo of a receipt to each income/expense transaction | 5 | 4.5 | TODO |
| 17 | Localization | 10 | 4.6 | TODO |

# Sprint 1

**Sprint Goal: Get project set up so that screens can easily be created in future sprints**

Sprint Backlog

| Story ID | Story / Task | Estimated Hours | Actual Hours |
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

## Sprint 1 Review

Video showing that the login screen works and if you enter the wrong password, an error message is displayed: https://youtu.be/ewMNUNOdaCw

As you can see from the [screenshots below](#sprint-1-screenshots), what I achieved this sprint was:
- Created API, DB, and APP projects.
- Login works and a JWT is issued to the client if login is valid.
- Text boxes and button is disabled during api call so that the user can’t spam the api with login requests.
- If login is invalid, then an error message is displayed to the user.

## Sprint 1 Retrospective

What went well:
- Completed all tasks that I brought into the sprint with unit tests
- Set up working API on my raspberry pi so I can use the API on http://pi.nathangawith.com:900/
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

# Sprint 2

**Sprint Goal: Create Settings screen and Income/Expense Screen**

Sprint Backlog

| Story ID | Story / Task | Estimated Hours | Actual Hours |
| --- | --- | --- | --- |
| 3 | Create Settings screen |||
| 3.1 | Settings screen - Creation of new Accounts | 3:00 | 3:28 |
| 3.2 | Settings screen - Creation of new Income Categories | 2:00 | 2:13 |
| 3.3 | Settings screen - Creation of new Expense Categories | 1:00 | 2:13 |
| 4 | Create the Income/Expense screen |||
| 4.1 | Income/Expense screen - Amount money field, Description Text | 2:00 | 1:51 |
| 4.2 | Income/Expense screen - Category Dropdown, Account Dropdown | 2:00 | 1:49 |
| 4.3 | Income/Expense screen - Date Calendar Input | 2:00 | 1:47 |
| --- | --- | --- | --- |
|  | Total | 12:00 | 13:23 |

## Sprint 2 Review

Video showing that the menu, settings, and income/expense screens work with error messages: https://youtu.be/2ErMOpIAIXg

As you can see from the [screenshots below](#sprint-2-screenshots), what I achieved this sprint was:
- Created a Menu screen to navigate to
  - Settings screen
  - Income screen
  - Expense screen
- Created Settings screen, which allows for
  - creation of accounts
  - creation of income categories
  - creation of expense categories
- Created Income/Expense screen
  - creation of transaction record with
    - Amount
    - Description
    - From/To Account
    - From/To Category
    - Date of Transaction
- Unit Tests for the API
- Android Instrumentation Tests for the UI

## Sprint 2 Retrospective

What went well:
- Although my estimates were still low for the second sprint, I wasn't as far off as I was for the first sprint. For the first sprint, the actual time spent was about 30% above the estimate, but for this second sprint the actual time spent was about 11% above the estimate.
- I think this is due to the fact that this sprint I was able to build on top of code I had already written in the previous sprint, and I the new things I had to learn were not as challenging as the previous sprint's challenges which included integrating the API, UI, and DB as well as learning how to write unit tests for a Java Spring Framework project as well as for Android.

What didn't go well:
- Overall, this sprint went well. The new components I had to learn for the UI (Calendar and Spinner inputs) were not as challenging as the previous sprint's technical challenges.

Changes for future sprints:
- The work load this sprint was a lot with other responsibilities including other classes and work, So I will try to limit my estimates to about 10 hours for future sprints.

# Sprint 3

**Sprint Goal: Create Register Screen and allow all screens to scroll for smaller screens**

Sprint Backlog

| Story ID | Story / Task | Estimated Hours | Actual Hours |
| --- | --- | --- | --- |
| 5 | Create the Register screen |||
| 5.1 | Register Screen - Total balance at top | 1:30 | 2:07 |
| 5.2 | Register Screen - List of transactions | 7:00 | 6:22 |
| 5.3 | Register Screen - Buttons to link to income/expense/transfer screens | 1:30 | 2:04 |
| --- | --- | --- | --- |
|  | Total | 10:00 | 10:33 |

## Sprint 3 Review

Video showing that the register screen works and links to the income/expense screens: https://youtu.be/wF2EpcTHk8U

As you can see from the [screenshots below](#sprint-3-screenshots), what I achieved this sprint was:
- Created Register screen
  - Total displays at the top
  - Each transaction is displayed in descending date order with:
    - description
    - amount
    - date
    - income/expense icon
    - from/to category
    - from/to account
  - Register can be refreshed by swiping down
  - 
- Added Functionality to other screens
  - Added register total to main menu
  - Converted all text field inputs to material inputs with floating labels
  - Login works with enter keypress
  - Username is case insensitive
  - All inputs can scroll up and down
- Unit Tests for the API
- Android Instrumentation Tests for the UI

## Sprint 3 Retrospective

What went well:
- Estimates were close overall to the amount of time spent.
- Total time spent was only 33 minutes over estimate of 10 hours.

What didn't go well:
- I thought I would be able to add more functionallity than I did to the Registration screen like sorting and filtering, but that will have to wait until a future task.

Changes for future sprints:
- None, this sprint was good overall.

# Sprint 4

**Sprint Goal: Create "Report" and "Report View" screens**

Sprint Backlog

| Story ID | Story / Task | Estimated Hours | Actual Hours |
| --- | --- | --- | --- |
| 6 | Create Reports screen |  |  |
| 6.1 | Reports screen: Start/End date calendar inputs | 1:00 | 1:16 |
| 6.2 | Reports screen: Breakpoint selection (Month, Year) | 1:00 | 1:16 |
| 6.3 | Reports screen: Type selection (Category, Account) | 1:00 | 1:16 |
| 7 | Create Report View screen |  |  |
| 7.1 | Report View screen: Table view of the report configured in the Reports screen | 7:00 | 4:44 |
| --- | --- | --- | --- |
|  | Total | 10:00 | 8:32 |

## Sprint 4 Review

Video showing that the report screen works and button to get there was added to the Menu screen: https://youtu.be/aKEIY-rZlQg

As you can see from the [screenshots below](#sprint-4-screenshots), what I achieved this sprint was:
- Created Report Screen
  - Initially, you are presented with:
    - Start Date Selection
    - End Date Selection
    - Breakpoint Selection
    - Type Selection
  - After pressing the submit button:
    - Table is generated to display the information querried for
    - Date Breakpoints are used for the rows
    - Type (Accounts/Categories) are used for the columns
- Added Functionality to other screens
  - Added logo to login screen
- Unit Tests for the API
- Android Instrumentation Tests for the UI

## Sprint 4 Retrospective

What went well:
- This was the first sprint that I finished up all of my tasks in less time than I initially estimated.
Although I finished in less time than extimated, I did not take on any additional tasks because I was
only 1.5 hours under my 10 hour estimate.
- I am feeling much more comfortable with the Android framework

What didn't go well:
- For future sprints, I will take into account my overestimate for estimating future tasks.

Changes for future sprints:
- None, this sprint was good overall.

# Sprint 5

**Sprint Goal: Create Transaction Scren and Add functionality to Settings Screen**

Sprint Backlog

| Story ID | Story / Task | Estimated Hours | Actual Hours |
| --- | --- | --- | --- |
| 8 | Settings screen enhancements |  |  |
| 8.1 | Settings screen: edit/delete an account name | 2:00 | 2:23 |
| 8.2 | Settings screen: edit/delete income/expense category | 2:00 | 2:23 |
| 13 | Prompt to transfer balance to another account when deleting an account | 1:30 | 2:23 |
| ~~8.3~~ | ~~Settings screen: edit first/last/user name~~ | ~~1:00~~ | ~~0:00~~ |
| 9 | Create Transfer screens |  |  |
| 9.1 | Create Account Transfer screen with: - Same fields as Income/Expense screen - And To/From account dropdowns | 3:00 | 2:09 |
| 9.2 | Create Category Transfer screen with: - Same fields as Income/Expense screen - And To/From category dropdowns | 2:00 | 2:09 |
| --- | --- | --- | --- |
|  | Total | Initial: 10:00, Final: 10:30 | 11:27 |

## Sprint 5 Review

Video showing that the transfer screen works for both accounts and categories, and buttons to get there were added to the Menu screen. Also, added the ability to edit the name of or delete any account or category and added logic to warn the user if necessary: https://www.youtube.com/watch?v=lpD4e1Z2ZkE

As you can see from the [screenshots below](#sprint-5-screenshots), what I achieved this sprint was:
- Created Transfer Screens
  - Built off of the Income/Espense screen and contains these fields:
    - Amount
    - Description
    - From (Account/Category)
    - To (Account/Category)
  - After pressing the submit button:
    - The API creates two TRANSACTIONS in the transaction table
      - one record is for the 'from' part of the transfer
      - the other record is for the 'to' part of the transfer
    - And one entry in the TRANSFERS table
      - The entry in the TRANSFERS table links the two entries in the TRANSACTIONS table
- Added Functionality to other screens
  - Made sure that totals show with two decimal places in the report and on the Menu screen
  - Modified Register query to work with transfers and display a new icon for transfers
- Unit Tests for the API

## Sprint 5 Retrospective

What went well:
- I enjoyed working on these tasks since they had a good mix of UI/API/DB work
- I am feeling much more comfortable with the Android framework

What didn't go well:
- The amount of logic around deleting and renaming accounts/categories was more than expected.
  - once I started thinking about it after the sprint started, lots of edge cases came to mind
- Accidentally worked on task 13 in addition to task 8, and didn't complete task 8.3
  - This is why there are two total times in the above table for this sprint
  - So, I have created another task in the backlog (18) for what I missed on 8.3

Changes for future sprints:
- More planning needs to happen before the sprint starts so that I don't underestimate again
- I am going to meed with my parents as part of planning for the final sprint, since I met with them at the beginning to create the inital mock ups. I will plan the final sprint for this course (Sprint 6) based on suggestions they make to improve the app tonight

# Sprint 6

**Sprint Goal: Fix bugs and work on final features for the end of the semester**

Sprint Backlog

| Story ID | Story / Task | Estimated Hours | Actual Hours |
| --- | --- | --- | --- |
| 19 | Edit/Delete income/expense/transfers from register screen | 5:00 |  |
| 20 | Fix all bugs found in Sprint5 | 3:30 |  |
| 20.1 | Report screen not working sometimes |  |  |
| 20.2 | Register in the wrong order |  |  |
| 20.3 | Don't show the account transfering from on transfer screen and settings |  |  |
| 20.4 | Put ("From {ACCOUNT}") on next line |  |  |
| 20.5 | No transactions over 1 million dollars |  |  |
| 20.6 | Modify Messages |  |  |
| 21 | Add Side Navigation Menu | 1:30 |  |
| ~~18~~ | ~~Allow the user to edit their first/last name from the settings screen~~ | ~~1:30~~ |  |
| --- | --- | --- | --- |
|  | Total | 10:00 |  |

## Sprint 6 Review

[TODO]

## Sprint 6 Retrospective

[TODO]

# Sprint 1 Screenshots:

.|.
:----:|:----:
![iter1_login_blank](readme_assets/iter1_login_blank.png?raw=true)|![iter1_login_loading](readme_assets/iter1_login_loading.png?raw=true)
![iter1_login_loading](readme_assets/iter1_login_loading.png?raw=true)|![iter1_login_token_received](readme_assets/iter1_login_token_received.png?raw=true)
![iter1_login_invalid](readme_assets/iter1_login_invalid.png?raw=true)|

# Sprint 2 Screenshots:

.|.
:----:|:----:
![iter2_menu.png](readme_assets/iter2_menu.png?raw=true)|![iter2_settings.png](readme_assets/iter2_settings.png?raw=true)
![iter2_settings_typed.png](readme_assets/iter2_settings_typed.png?raw=true)|![iter2_settings_added.png](readme_assets/iter2_settings_added.png?raw=true)
![iter2_incomeexpense.png](readme_assets/iter2_incomeexpense.png?raw=true)|![iter2_incomeexpense_amount.png](readme_assets/iter2_incomeexpense_amount.png?raw=true)
![iter2_incomeexpense_description.png](readme_assets/iter2_incomeexpense_description.png?raw=true)|![iter2_incomeexpense_account.png](readme_assets/iter2_incomeexpense_account.png?raw=true)
![iter2_incomeexpense_category.png](readme_assets/iter2_incomeexpense_category.png?raw=true)|![iter2_incomeexpense_calendar.png](readme_assets/iter2_incomeexpense_calendar.png?raw=true)
![iter2_incomeexpense_filledin.png](readme_assets/iter2_incomeexpense_filledin.png?raw=true)|![iter2_db.png](readme_assets/iter2_db.png?raw=true)

# Sprint 3 Screenshots:

.|.
:----:|:----:
![iter3_material_inputs.png](readme_assets/iter3_material_inputs.png?raw=true)|![iter3_menu_with_total.png](readme_assets/iter3_menu_with_total.png?raw=true)
![iter3_register_actions.png](readme_assets/iter3_register_actions.png?raw=true)|![iter3_register_refreshing.png](readme_assets/iter3_register_refreshing.png?raw=true)

# Sprint 4 Screenshots:

.|.
:----:|:----:
![iter4_updated_login.png](readme_assets/iter4_updated_login.png?raw=true)|![iter4_updated_menu.png](readme_assets/iter4_updated_menu.png?raw=true)
![iter4_report_screen.png](readme_assets/iter4_report_screen.png?raw=true)|![iter4_report_options.png](readme_assets/iter4_report_options.png?raw=true)
![iter4_report_table_accounts.png](readme_assets/iter4_report_table_accounts.png?raw=true)|![iter4_report_table_categories.png](readme_assets/iter4_report_table_categories.png?raw=true)


# Sprint 5 Screenshots:

.|.
:----:|:----:
![iter5_updated_menu](readme_assets/iter5_updated_menu.png?raw=true)|![iter5_register](readme_assets/iter5_register.png?raw=true)
![iter5_account_transfer](readme_assets/iter5_account_transfer.png?raw=true)|![iter5_category_transfer](readme_assets/iter5_category_transfer.png?raw=true)
![iter5_settings_warning_1](readme_assets/iter5_settings_warning_1.png?raw=true)|![iter5_settings_warning_2](readme_assets/iter5_settings_warning_2.png?raw=true)
![iter5_settings_edit_from_account](readme_assets/iter5_settings_edit_from_account.png?raw=true)|![iter5_settings_edit](readme_assets/iter5_settings_edit.png?raw=true)

# Sprint 6 Screenshots:

.|.
:----:|:----:
[TODO]|[TODO]

# Original Mockups

.|.
:----:|:----:
![login](readme_assets/login.jpg?raw=true)|![accounts](readme_assets/accounts.jpg?raw=true)
![dev_settings](readme_assets/dev_settings.jpg?raw=true)|![home](readme_assets/home.jpg?raw=true)
![income_expense](readme_assets/income_expense.jpg?raw=true)|![register](readme_assets/register.jpg?raw=true)
![report_view](readme_assets/report_view.jpg?raw=true)|![reports](readme_assets/reports.jpg?raw=true)
![settings](readme_assets/settings.jpg?raw=true)|![transfer](readme_assets/transfer.jpg?raw=true)
