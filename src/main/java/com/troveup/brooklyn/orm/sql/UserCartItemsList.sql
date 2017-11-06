use troveweb;

#Get all cart items that are currently in the cart for each user
select UA.EMAIL, MATERIAL_NAME, FINISH_NAME, FROZENITEMNAME, CARTITEMID from USER_ACCOUNTS UA
  JOIN CART CA ON CA.CARTOWNER_USERID_OID=UA.USERID
  JOIN CARTITEM CI ON CA.CARTID=CI.CART_CARTID_OID WHERE EMAIL not like "%@troveup.com%";