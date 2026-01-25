Feature: PayPal Sandbox Checkout

Scenario Outline: Execute PayPal checkout flow for order order creation and completion
  Given TPP have a valid PayPal access token
  When TPP create a checkout order using data from excel for "<createOrderPayload>"
  Then TPP check order is succesfully created with "<createOrderResCode>"
  And TPP confirm the checkout order and response code should be "<confirmOrderResCode>" and "<createOrderPayload>"
  Then TPP redirect PSU to login on paypal  to approve the order with "<user_email>" and "<user_password>"
  And PSU select account "<payer_account>" on accounts page and submit the payment
  Then TPP authorize order after user approval through paypal with payload "<authOrderPayload>" and response code should be "<authorizeOrderResCode>"
  And the order status should be "<orderStatus>"
  Examples:
   | createOrderPayload | authOrderPayload |createOrderResCode|user_email|user_password|payer_account|updateOrderPayload|uploadOrderResCode|confirmOrderResCode|authorizeOrderResCode|orderStatus|
   | ORDER_1 |ORDER_1_AUTH|200|id4@gmail.com|idan4@123|Visa|ORDER_1_Update|201|200|201|COMPLETED|
   | ORDER_2 |ORDER_2_AUTH|200|id3@gmail.com|idan3@123|Visa|ORDER_2_Update|201|200|201|COMPLETED|
   | ORDER_3 |ORDER_3_AUTH|200|id2@gmail.com|idan2@123|Visa|ORDER_3_Update|201|200|201|COMPLETED|
   | ORDER_4 |ORDER_4_AUTH|200|id1@gmail.com|idan1@123|Visa|ORDER_4_Update|201|200|201|COMPLETED|
   | ORDER_5 |ORDER_5_AUTH|200|id4@gmail.com|idan4@123|Visa|ORDER_5_Update|201|200|201|COMPLETED|
   | ORDER_6 |ORDER_6_AUTH|200|id3@gmail.com|idan3@123|Visa|ORDER_6_Update|201|200|201|COMPLETED|
   | ORDER_7 |ORDER_7_AUTH|200|id2@gmail.com|idan2@123|Visa|ORDER_7_Update|201|200|201|COMPLETED|
   | ORDER_8 |ORDER_8_AUTH|200|id1@gmail.com|idan1@123|Visa|ORDER_8_Update|201|200|201|COMPLETED|
   | ORDER_9 |ORDER_9_AUTH|200|id4@gmail.com|idan4@123|Visa|ORDER_9_Update|201|200|201|COMPLETED|

   
   
