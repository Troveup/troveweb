use troveweb;

#Count of the number of items that have been troved by the user
select DISTINCT EMAIL, FIRST_NAME, USERNAME, COUNT(*) from USER_ACCOUNTS UA JOIN GROUPEDITEM GI ON UA.USERID=GI.TROVEDITEMS_USERID_OWN
  WHERE GI.TROVEDITEMS_USERID_OWN is not NULL AND EMAIL not like "%@troveup.com%" GROUP BY EMAIL;