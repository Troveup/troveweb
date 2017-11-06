use troveweb;

#Set up a password variable
set @`GENERIC_PASS` = (SELECT `PASSWORD` FROM USER_ACCOUNTS WHERE USERID = 2);

#First, scrub all e-mail addresses
UPDATE USER_ACCOUNTS SET EMAIL = CONCAT(CAST(USERID AS CHAR), "test@mailinator.com");
#Then, scrub all first name fields
UPDATE USER_ACCOUNTS SET FIRST_NAME = CONCAT(CAST(USERID AS CHAR), "test");
#Passwords are now Trove2015
UPDATE USER_ACCOUNTS SET `PASSWORD` = @`GENERIC_PASS`;
#Scrub the usernames
UPDATE USER_ACCOUNTS SET USERNAME = CONCAT(CAST(USERID AS CHAR), "test");
#Scrub Braintree User IDs
UPDATE USER_ACCOUNTS SET BRAINTREEUSERID = null;
#Scrub the address table
UPDATE ADDRESS SET
	ADDRESS_LINE_1 = "20 Exchange Pl",
    ADDRESS_LINE_2 = "APT 1604",
    CITY = "New York",
    FIRST_NAME = "Test",
    LAST_NAME = "User",
    PHONE = "5555555555",
    POSTAL_CODE = "10005",
    EMAIL = "test@mailinator.com";

#Take care of any possibly open orders from prod
UPDATE PRINTORDER SET STATUS = "COMPLETE";
UPDATE CARTITEM SET CARTITEMSTATUS = "COMPLETE";
UPDATE `ORDER` SET ORDERSTATUS = "CLOSED";
