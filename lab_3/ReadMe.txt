BaseFinancialProduct 5 5 -> 1 Price
Credit 5 2 -> 1 Price
Leasing 7 6 -> Price
CreditPayment 2 3 -> 2 Credit, Price
LeasingPayment 2 3 ->2 Leasing, Price
Period 2 2
Price 4 5 -> 2 Period, PropertyType
PropertySale 8 4 ->4  Property, Landlord, Tenant, Price    
SalePaymentService 2 8 -> 4 PropertySale, PaymentMethod, CreditPayment, LeasingPayment

BasicScoreCalculator 0 1
PropertyAllocationResult 3 4 ->2 Tenant, PropertyMatch
PropertyManager 2 12 -> 4 Property, PropertyType, Apartment, House
PropertyMatch 2 3 -> 1 Property
PropertyPreference 4 4 -> 1 PropertyType
PublicationManager 2 13  -> 4 Publication, NewsArticle, Advice, Author
SmartPropertyAllocator 1 7 -> 3  Tenant, PropertyPreference, PropertyManager

Advice 3 1
Author 7 3 ->1 Publication 
NewsArticle 2 1 -> 1 Property
Publication 7 5 -> 2 Author, PublicationStatus

FavorriteNotifier 1 3 -> 3 Liked, Property, NotificationManager
Notification 5 1
NotificationManager 1 4 ->1  Notification
NotificationService 2 5 -> Notifier, NotificationManager
PublicationNotifier 1 3 -> 3 Publication, Author, NotificationManager


Property 6 6 -> 3 Address, PropertyType, IsAvailable
Address 3 2
IsAvailable 2 4
AvailabilityCalendar  2 11 -> 3 Property, ReservationPeriod, CalendarManager
ReservationPeriod 4 3 
Apartment 8 2
House 11 3
Hotel 8 2
Office 8 2
Commercial 11 3


Person 4 1
Tenant 6 3 -> 3 RentalAgreement, RentalService, Property
Landlord 6 7 ->2 Property, RentalAgreement
Liked 2 5 ->2 Tenant, Property
LikedCleaner 0 4 ->2 Liked, Property
RentalService 0 4 ->5 Property, Tenant, Landlord, RentalAgreement, Price
RentalAgreement 5 2 ->3 Property, Tenant, Price
Review 7 4 ->3 Property, Tenant, RentalAgreement
ReviewCreationService 0 4 ->4 Property, Tenant, Review, RentalAgreement
ReviewModerationService  0 2 ->1 Review
PropertyStatistics  4 5 ->2  Property, Review  

WaterMeterReading 5 1 
ElectricityMeterReading 3 1
HeatingMeterReading 3 1
Tariff 5 0
WaterCostCalculator 2 6 -> 3 Property, WaterMeterReading, Tariff
ElectricityCostCalculator 2 6 -> 3 Property, ElectricityMeterReading, Tariff
HeatingCostCalculator 2 6 ->3 Property, HeatingMeterReading, Tariff

всего поля: 201, методы:208 , ассоциации: 90

12 исключений:

AvailabilityConflictException
InvalidPeriodException
InvalidPreferenceException
InvalidPriceException
InvalidReadingException
InvalidReviewException
NoPropertyMatchException
PaymentException
PropertyNotAvailableException
PropertySaleException
ReviewException
UtilityServiceException




