Feature: PayPal Sandbox Checkout

Scenario Outline: Execute PayPal checkout flow
  Given I have a valid PayPal access token
  When I create a checkout order using data from excel "<orderId>"
  And I confirm a checkout order "<orderId>"
  And I attempt to approve the order based on test case true
  Then I authorize order after user approval
  And the order status should be COMPLETED
  Examples:
   | path | orderId |
   | exce.xlsx   | ORDER_1 |
