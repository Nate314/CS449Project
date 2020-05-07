# Installation guide

## Note

--------

I am aware that this is a lot of setup to do to get the app running. I just saw that I had to do the writeup on the installation at the end of the final sprint.

If you just want to run the app and see it working, you can use these credentials with the API URL http://pi.nathangawith.com:900/ :

- username: "hare"
- password: "brianharepassword"

and you can download the FinanceApp0.6.3.apk file (attached to the assignment) to whatever device you are testing on if you don't want to go through pulling the app project into android studio.

If for some reason the API is not operational when using http://pi.nathangawith.com:900/ let me know at nmg3hw@mail.umkc.edu and I can make sure it is running when you are grading this final itteration.

## Full Installation Guide

--------

There are three different parts of this applation that all need to work together in order to experience it in a custom dev environment.

1) The Database
    - To create the database schema, run the CS449Project/db/ddl.sql file in an installation of MySQL. This will set up a database schema named "CS449Project" with all of the necessary tables used from the API including:
        - Users
        - Categories
        - Accounts
        - Transactions
        - Transfers
2) The API
    - To run the api, you will need maven installed since there are some maven dependencies needed to run the api.
    - Before running the api, modify the CS449Project/api/config.json file to make sure that you have the correct dbConnectionURL, dbUser, and dbPassword for your system.
    - Open CS449Project/api in eclipse, right click the src/main/java/com/nathangawith/umkc/API.java file, and run as a Java Application. This will boot up the Java Spring REST API which is used to interface between the Java app and the Database.
    - Alternatively, you can run the api from the command line with the command:
        - `mvn spring-boot:run`
3) The App
    - To use the app, open the CS449Project/app/Financeapp folder in android studio and run the app on an android phone.
    - Since I have been testing by deploying the API on my raspberry pi or on my laptop the whole semester, those are the only options for the API dropdown on the login screen. If you want to add in an option for whatever your current IP address is, you will need to add your machine's ip address to the CS449Project/app/FinanceApp/app/src/main/java/com/nathangawith/umkc/financeapp/activites/LoginActivity.java file.
    - In the else block on lines 71-74, add an entry that correlates with your instance of the API project. This will allow you to select your computer to be used for the API behind the application.

```java
} else {
    spinnerApiUrlOptions.add("http://{Your IP Address}:9090/");
    // You should add the above line with your IP address.
    spinnerApiUrlOptions.add("http://pi.nathangawith.com:900/");
    spinnerApiUrlOptions.add("http://10.0.0.26:9090/");
    spinnerApiUrlOptions.add("http://nathang2018:9090/");
}
```

Finally, in order to log in, you will need a user. To do so, you can manually create a user by sending a POST request to your local API:

POST http://{Your IP Address}/auth/createaccount

With a JSON body: {"username": "hare", "password": "brianharepassword"}

Finally, if you are running this on a fresh database, make sure to add 3-4 accounts, income categories, and expense categories on the "Settings" screen before navigating to any of the other screens. If we had a Sprint7, the next story I would work on would fix the issues associated to trying to use any of the other screens without these items set up first.

Also, if I had a Sprint7, I would add Google Authentication so that you wouldn't have to manually create a user in the unsecure way listed above.

