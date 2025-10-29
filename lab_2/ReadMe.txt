
CardPaymentProcessor 2 3 -> 1, CreditCard
Cash 2 2 ->
CashPaymentProcessor 2 3 -> 1, Cash
CreditCard 3 2 ->
Discount 1 0 ->
Order 3 5 -> 3, Customer, Price, List<Item>
Payment 4 5 ->3, Price, CardPaymentProcessor, CashPaymentProcessor
PaymentHistoryService 1 6 -> 1( PaymentRecord 6 1-> 3, Customer, Order, Price)
Period 1 2
Price 4 2 -> 1 Discount
Status 2 1 -> 2 Order, Period

BookedDelivery 9 4 ->
Delivery 7 5 -> 
DeliveryManager 2 8 -> 3 Delivery, BookedDelivery, Tracking
Tracking 3 5 -> 2 Delivery,TrackingEvent 
TrackingEvent 3 1 ->

Item 6 4 ->
SportItem 8 2 ->
ElectronicItem 8 2 ->
Warehouse 4 4 -> 3 ElectronicItem, SportItem, Item

Account 4 5 -> 1 Customer
Address 4 5 ->
Age 1 3
Customer 6 5 -> 1 Adddress 
LoginHistory 5 5 -> 1 Account
SMSNotification 3 2 -> 1 Customer

AgeCoefficientCalculator 0 1 ->
BasePremiumCalculator 0 1 ->
CoverageCoefficientCalculator 0 1 ->
CustomerLoyaltyService 3 4 
CustomerProfileService 3 3 -> 3 Customer RentalHistory Notification
DefaultInsuranceCalculator 3 2 -> 3 BasePremiumCalculator, AgeCoefficientCalculator, CoverageCoefficientCalculator 0 1 ->
InsurancePolicy 8 3 ->
InsuranceRequest 5 1 -> 2 Vehicle, Age 
InventoryManager 1 7 -> 1 Item
RentalAgreement 7 3 -> 2 Customer, Order
RentalHistoryImpl 2 8 -> 2 Customer, RentalRecord
RentalLoyaltyManager 3 3 -> 2 CustomerLoyaltyService, RentalHistory
RentalOrderService 4 5 -> 4 PaymentProcessor InsuranceCalculator RentalHistory Delivery
RentalRecord 6 3 ->
RentalReportService 3 3 ->  1 RentalRecord
RentalService 1 4 -> 1 RentalHistory 
VehicleAvailabilityService 3 2 -> 2 VehicleMaintenance RentalRecord

Vehicle 8 4 -> 2 Item, Fuel
Fuel 1 0 ->
DriverLicense 3 1 ->
Car 11 2 ->
ElectroCar 3 3 ->
Scooter 10 2->
ElectroScooter 3 5 ->
VehicleMaintenance 5 5 ->1 Vehicle

DeliveryException
InsufficientCashException
InsuranceCalculationException
InsuranceProcessingException
InsuranceValidationException
InvalidAddressException
InvalidAgeException
InvalidRentalDurationException
LoginHistoryException
OrderException
PaymentException
RentalHistoryException 
12 исключений

Total:
51 класс 200 полей 169 методов 53 ассоциации




 



 
