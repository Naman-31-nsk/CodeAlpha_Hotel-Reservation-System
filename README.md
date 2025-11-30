# CodeAlpha_Hotel-Reservation-System
A comprehensive command-line hotel reservation system built in Java with OOP principles and file-based persistence.
Features
Core Functionality

✅ Search Rooms - Find available rooms by category
✅ Book Rooms - Create reservations with guest information
✅ Manage Reservations - View, cancel, and track bookings
✅ Payment Processing - Simulate payment transactions
✅ Guest Management - Add and manage guest profiles
✅ Data Persistence - All data saved to files automatically

Room Categories
CategoryPrice/NightCapacityStandard$100.002 guestsDeluxe$200.003 guestsSuite$350.004 guests
Technical Features

Object-Oriented Design (OOP)
File I/O using Java Serialization
Data persistence across sessions
Automatic room availability management
Total cost calculation based on nights stayed

Installation & Setup
Prerequisites

Java Development Kit (JDK) 8 or higher
Terminal/Command Prompt access

Installation Steps

Save the code to a file named HotelReservationSystem.java
Compile the program:

bash   javac HotelReservationSystem.java

Run the program:

bash   java HotelReservationSystem
Usage Guide
Main Menu Options
1. Search Available Rooms
2. Book a Room
3. View All Reservations
4. View Booking Details
5. Cancel Reservation
6. Process Payment
7. Add Guest
8. View All Rooms
9. Exit
How to Book a Room

Select option 2 from the main menu
Choose to use an existing guest or create a new one
If creating new guest, enter:

Name
Email
Phone number


Select a room from the available list
Enter check-in date (format: yyyy-MM-dd, example: 2024-12-25)
Enter check-out date (format: yyyy-MM-dd, example: 2024-12-30)
System confirms booking and displays reservation ID

How to Process Payment

Select option 6 from the main menu
Enter your Reservation ID (example: RES1234567890)
Choose payment method:

Credit Card
Debit Card
Cash


System processes payment and updates status

How to Cancel Reservation

Select option 5 from the main menu
Enter Reservation ID
Confirm cancellation when prompted
Room becomes available again automatically

Data Files
The system creates three data files automatically:

rooms.dat - Stores room information and availability
reservations.dat - Stores all reservations
guests.dat - Stores guest profiles

Note: These files are created automatically on first run. Do not delete them while using the system.
Sample Workflow
Complete Booking Example
1. Start program
2. Choose "2. Book a Room"
3. Select "2. New Guest"
4. Enter guest details:
   - Name: John Doe
   - Email: john.doe@email.com
   - Phone: 555-1234
5. Select room: 1 (Room 101 - Standard)
6. Check-in: 2024-12-20
7. Check-out: 2024-12-23
8. Note Reservation ID: RES1734567890123
9. Choose "6. Process Payment"
10. Enter Reservation ID
11. Select payment method: 1 (Credit Card)
12. Payment complete!
Date Format
Important: All dates must be entered in the format yyyy-MM-dd
Valid Examples:

2024-12-25 (December 25, 2024)
2025-01-15 (January 15, 2025)
2024-06-01 (June 1, 2024)

Invalid Examples:

12/25/2024 ❌
25-12-2024 ❌
2024/12/25 ❌

Error Handling
The system handles common errors:

Invalid menu choices
Incorrect date formats
Empty input fields
Invalid reservation IDs
Out-of-range selections

Initial Room Setup
On first run, the system automatically creates:

5 Standard rooms (101-105)
5 Deluxe rooms (201-205)
3 Suite rooms (301-303)

Total: 13 rooms
Reservation Status Types

PENDING - Reservation created, awaiting confirmation
CONFIRMED - Booking confirmed
CANCELLED - Reservation cancelled
COMPLETED - Guest checked out

Payment Methods

Credit Card - Visa, Mastercard, etc.
Debit Card - Bank debit card
Cash - Cash payment at front desk

Note: This is a simulation. No real payment processing occurs.

Features Overview
Object-Oriented Design
The system uses four main classes:

Room - Represents hotel rooms
Guest - Stores guest information
Reservation - Manages bookings
FileManager - Handles data persistence

Future Enhancements
Potential improvements for future versions:

Multi-user support with authentication
Email confirmations
Advanced search filters
Discount codes and promotions
Room service orders
Report generation
GUI interface


