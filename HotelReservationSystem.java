import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

// Enum for Room Categories
enum RoomCategory {
    STANDARD(100.0),
    DELUXE(200.0),
    SUITE(350.0);
    
    private final double basePrice;
    
    RoomCategory(double basePrice) {
        this.basePrice = basePrice;
    }
    
    public double getBasePrice() {
        return basePrice;
    }
}

// Enum for Reservation Status
enum ReservationStatus {
    PENDING, CONFIRMED, CANCELLED, COMPLETED
}

// Room Class
class Room implements Serializable {
    private static final long serialVersionUID = 1L;
    private int roomNumber;
    private RoomCategory category;
    private boolean isAvailable;
    private int capacity;
    
    public Room(int roomNumber, RoomCategory category, int capacity) {
        this.roomNumber = roomNumber;
        this.category = category;
        this.capacity = capacity;
        this.isAvailable = true;
    }
    
    public int getRoomNumber() { return roomNumber; }
    public RoomCategory getCategory() { return category; }
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }
    public int getCapacity() { return capacity; }
    
    @Override
    public String toString() {
        return String.format("Room %d | %s | $%.2f/night | Capacity: %d | %s",
            roomNumber, category, category.getBasePrice(), capacity,
            isAvailable ? "Available" : "Occupied");
    }
}

// Guest Class
class Guest implements Serializable {
    private static final long serialVersionUID = 1L;
    private String guestId;
    private String name;
    private String email;
    private String phone;
    
    public Guest(String guestId, String name, String email, String phone) {
        this.guestId = guestId;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }
    
    public String getGuestId() { return guestId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    
    @Override
    public String toString() {
        return String.format("%s (ID: %s) | %s | %s", name, guestId, email, phone);
    }
}

// Reservation Class
class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;
    private String reservationId;
    private Guest guest;
    private Room room;
    private Date checkInDate;
    private Date checkOutDate;
    private ReservationStatus status;
    private double totalAmount;
    private boolean paymentCompleted;
    
    public Reservation(String reservationId, Guest guest, Room room, 
                      Date checkInDate, Date checkOutDate) {
        this.reservationId = reservationId;
        this.guest = guest;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.status = ReservationStatus.PENDING;
        this.paymentCompleted = false;
        calculateTotalAmount();
    }
    
    private void calculateTotalAmount() {
        long diffInMillies = checkOutDate.getTime() - checkInDate.getTime();
        int nights = (int) (diffInMillies / (1000 * 60 * 60 * 24));
        if (nights < 1) nights = 1;
        totalAmount = nights * room.getCategory().getBasePrice();
    }
    
    public String getReservationId() { return reservationId; }
    public Guest getGuest() { return guest; }
    public Room getRoom() { return room; }
    public Date getCheckInDate() { return checkInDate; }
    public Date getCheckOutDate() { return checkOutDate; }
    public ReservationStatus getStatus() { return status; }
    public void setStatus(ReservationStatus status) { this.status = status; }
    public double getTotalAmount() { return totalAmount; }
    public boolean isPaymentCompleted() { return paymentCompleted; }
    public void setPaymentCompleted(boolean completed) { paymentCompleted = completed; }
    
    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return String.format("Reservation ID: %s\nGuest: %s\nRoom: %d (%s)\nCheck-in: %s\nCheck-out: %s\nTotal: $%.2f\nStatus: %s\nPayment: %s",
            reservationId, guest.getName(), room.getRoomNumber(), room.getCategory(),
            sdf.format(checkInDate), sdf.format(checkOutDate), totalAmount, status,
            paymentCompleted ? "Completed" : "Pending");
    }
}

// File Manager for persistence
class FileManager {
    private static final String ROOMS_FILE = "rooms.dat";
    private static final String RESERVATIONS_FILE = "reservations.dat";
    private static final String GUESTS_FILE = "guests.dat";
    
    public static void saveRooms(List<Room> rooms) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ROOMS_FILE))) {
            oos.writeObject(rooms);
        } catch (IOException e) {
            System.out.println("Error saving rooms: " + e.getMessage());
        }
    }
    
    @SuppressWarnings("unchecked")
    public static List<Room> loadRooms() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ROOMS_FILE))) {
            return (List<Room>) ois.readObject();
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading rooms: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    public static void saveReservations(List<Reservation> reservations) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(RESERVATIONS_FILE))) {
            oos.writeObject(reservations);
        } catch (IOException e) {
            System.out.println("Error saving reservations: " + e.getMessage());
        }
    }
    
    @SuppressWarnings("unchecked")
    public static List<Reservation> loadReservations() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(RESERVATIONS_FILE))) {
            return (List<Reservation>) ois.readObject();
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading reservations: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    public static void saveGuests(List<Guest> guests) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(GUESTS_FILE))) {
            oos.writeObject(guests);
        } catch (IOException e) {
            System.out.println("Error saving guests: " + e.getMessage());
        }
    }
    
    @SuppressWarnings("unchecked")
    public static List<Guest> loadGuests() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(GUESTS_FILE))) {
            return (List<Guest>) ois.readObject();
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading guests: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}

// Main Hotel Reservation System
public class HotelReservationSystem {
    private List<Room> rooms;
    private List<Reservation> reservations;
    private List<Guest> guests;
    private Scanner scanner;
    
    public HotelReservationSystem() {
        rooms = FileManager.loadRooms();
        reservations = FileManager.loadReservations();
        guests = FileManager.loadGuests();
        scanner = new Scanner(System.in);
        
        // Initialize rooms if empty
        if (rooms.isEmpty()) {
            initializeRooms();
        }
    }
    
    private void initializeRooms() {
        for (int i = 1; i <= 5; i++) {
            rooms.add(new Room(100 + i, RoomCategory.STANDARD, 2));
        }
        for (int i = 1; i <= 5; i++) {
            rooms.add(new Room(200 + i, RoomCategory.DELUXE, 3));
        }
        for (int i = 1; i <= 3; i++) {
            rooms.add(new Room(300 + i, RoomCategory.SUITE, 4));
        }
        FileManager.saveRooms(rooms);
        System.out.println("Hotel rooms initialized successfully!");
    }
    
    private void displayMenu() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("       HOTEL RESERVATION SYSTEM MENU");
        System.out.println("=".repeat(50));
        System.out.println("1. Search Available Rooms");
        System.out.println("2. Book a Room");
        System.out.println("3. View All Reservations");
        System.out.println("4. View Booking Details");
        System.out.println("5. Cancel Reservation");
        System.out.println("6. Process Payment");
        System.out.println("7. Add Guest");
        System.out.println("8. View All Rooms");
        System.out.println("9. Exit");
        System.out.println("=".repeat(50));
        System.out.print("Enter your choice (1-9): ");
    }
    
    private void searchAvailableRooms() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("           SEARCH AVAILABLE ROOMS");
        System.out.println("=".repeat(50));
        System.out.print("Filter by category? (Y/N): ");
        String filter = scanner.nextLine().trim().toUpperCase();
        
        RoomCategory selectedCategory = null;
        if (filter.equals("Y")) {
            System.out.println("\nSelect category:");
            int index = 1;
            for (RoomCategory cat : RoomCategory.values()) {
                System.out.printf("%d. %s ($%.2f/night)%n", index++, cat, cat.getBasePrice());
            }
            System.out.print("Enter choice: ");
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                if (choice >= 1 && choice <= RoomCategory.values().length) {
                    selectedCategory = RoomCategory.values()[choice - 1];
                } else {
                    System.out.println("Invalid choice. Showing all categories.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Showing all categories.");
            }
        }
        
        System.out.println("\nAvailable Rooms:");
        System.out.println("-".repeat(70));
        boolean found = false;
        for (Room room : rooms) {
            if (room.isAvailable() && 
                (selectedCategory == null || room.getCategory() == selectedCategory)) {
                System.out.println(room);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No available rooms found.");
        }
        System.out.println("-".repeat(70));
    }
    
    private void bookRoom() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("              BOOK A ROOM");
        System.out.println("=".repeat(50));
        
        // Select or create guest
        Guest guest = selectOrCreateGuest();
        if (guest == null) {
            System.out.println("Booking cancelled.");
            return;
        }
        
        // Display available rooms
        System.out.println("\nAvailable Rooms:");
        System.out.println("-".repeat(70));
        List<Room> availableRooms = new ArrayList<>();
        for (Room room : rooms) {
            if (room.isAvailable()) {
                availableRooms.add(room);
                System.out.printf("%d. %s%n", availableRooms.size(), room);
            }
        }
        System.out.println("-".repeat(70));
        
        if (availableRooms.isEmpty()) {
            System.out.println("No rooms available.");
            return;
        }
        
        System.out.print("\nSelect room number (1-" + availableRooms.size() + "): ");
        int roomChoice;
        try {
            roomChoice = Integer.parseInt(scanner.nextLine()) - 1;
            if (roomChoice < 0 || roomChoice >= availableRooms.size()) {
                System.out.println("Invalid room selection.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }
        
        Room selectedRoom = availableRooms.get(roomChoice);
        
        // Get dates
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        Date checkIn = null, checkOut = null;
        
        try {
            System.out.print("Check-in date (yyyy-MM-dd): ");
            String checkInStr = scanner.nextLine();
            checkIn = sdf.parse(checkInStr);
            
            System.out.print("Check-out date (yyyy-MM-dd): ");
            String checkOutStr = scanner.nextLine();
            checkOut = sdf.parse(checkOutStr);
            
            if (checkOut.before(checkIn) || checkOut.equals(checkIn)) {
                System.out.println("Error: Check-out must be after check-in date.");
                return;
            }
        } catch (ParseException e) {
            System.out.println("Error: Invalid date format. Please use yyyy-MM-dd (e.g., 2024-12-25)");
            return;
        }
        
        // Create reservation
        String reservationId = "RES" + System.currentTimeMillis();
        Reservation reservation = new Reservation(reservationId, guest, selectedRoom, checkIn, checkOut);
        reservation.setStatus(ReservationStatus.CONFIRMED);
        selectedRoom.setAvailable(false);
        
        reservations.add(reservation);
        FileManager.saveReservations(reservations);
        FileManager.saveRooms(rooms);
        
        System.out.println("\n" + "=".repeat(50));
        System.out.println("  RESERVATION CREATED SUCCESSFULLY!");
        System.out.println("=".repeat(50));
        System.out.println(reservation);
        System.out.println("=".repeat(50));
    }
    
    private Guest selectOrCreateGuest() {
        System.out.println("\nGuest Information:");
        System.out.println("1. Existing Guest");
        System.out.println("2. New Guest");
        System.out.print("Enter choice (1 or 2): ");
        
        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return null;
        }
        
        if (choice == 1) {
            if (guests.isEmpty()) {
                System.out.println("No existing guests found. Please create a new guest.");
                return createNewGuest();
            }
            System.out.println("\nExisting Guests:");
            System.out.println("-".repeat(70));
            for (int i = 0; i < guests.size(); i++) {
                System.out.printf("%d. %s%n", i + 1, guests.get(i));
            }
            System.out.println("-".repeat(70));
            System.out.print("Select guest (1-" + guests.size() + "): ");
            try {
                int guestChoice = Integer.parseInt(scanner.nextLine()) - 1;
                if (guestChoice >= 0 && guestChoice < guests.size()) {
                    return guests.get(guestChoice);
                } else {
                    System.out.println("Invalid selection.");
                    return null;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input.");
                return null;
            }
        } else if (choice == 2) {
            return createNewGuest();
        } else {
            System.out.println("Invalid choice.");
            return null;
        }
    }
    
    private Guest createNewGuest() {
        System.out.println("\nEnter Guest Details:");
        System.out.print("Name: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Name cannot be empty.");
            return null;
        }
        
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        if (email.isEmpty()) {
            System.out.println("Email cannot be empty.");
            return null;
        }
        
        System.out.print("Phone: ");
        String phone = scanner.nextLine().trim();
        if (phone.isEmpty()) {
            System.out.println("Phone cannot be empty.");
            return null;
        }
        
        String guestId = "G" + System.currentTimeMillis();
        Guest guest = new Guest(guestId, name, email, phone);
        guests.add(guest);
        FileManager.saveGuests(guests);
        
        System.out.println("\nGuest added successfully!");
        System.out.println(guest);
        return guest;
    }
    
    private void viewAllReservations() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("           ALL RESERVATIONS");
        System.out.println("=".repeat(50));
        
        if (reservations.isEmpty()) {
            System.out.println("No reservations found.");
            return;
        }
        
        for (int i = 0; i < reservations.size(); i++) {
            System.out.println("\nReservation #" + (i + 1));
            System.out.println("-".repeat(50));
            System.out.println(reservations.get(i));
        }
        System.out.println("=".repeat(50));
    }
    
    private void viewBookingDetails() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("          VIEW BOOKING DETAILS");
        System.out.println("=".repeat(50));
        System.out.print("Enter Reservation ID: ");
        String resId = scanner.nextLine().trim();
        
        Reservation found = findReservation(resId);
        if (found != null) {
            System.out.println("\n" + "=".repeat(50));
            System.out.println(found);
            System.out.println("=".repeat(50));
        } else {
            System.out.println("Reservation not found with ID: " + resId);
        }
    }
    
    private void cancelReservation() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("          CANCEL RESERVATION");
        System.out.println("=".repeat(50));
        System.out.print("Enter Reservation ID to cancel: ");
        String resId = scanner.nextLine().trim();
        
        Reservation found = findReservation(resId);
        if (found != null) {
            if (found.getStatus() == ReservationStatus.CANCELLED) {
                System.out.println("This reservation is already cancelled.");
                return;
            }
            
            System.out.println("\nReservation Details:");
            System.out.println(found);
            System.out.print("\nAre you sure you want to cancel? (Y/N): ");
            String confirm = scanner.nextLine().trim().toUpperCase();
            
            if (confirm.equals("Y")) {
                found.setStatus(ReservationStatus.CANCELLED);
                found.getRoom().setAvailable(true);
                FileManager.saveReservations(reservations);
                FileManager.saveRooms(rooms);
                System.out.println("\nReservation cancelled successfully!");
            } else {
                System.out.println("Cancellation aborted.");
            }
        } else {
            System.out.println("Reservation not found with ID: " + resId);
        }
    }
    
    private void processPayment() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("          PROCESS PAYMENT");
        System.out.println("=".repeat(50));
        System.out.print("Enter Reservation ID: ");
        String resId = scanner.nextLine().trim();
        
        Reservation found = findReservation(resId);
        if (found == null) {
            System.out.println("Reservation not found with ID: " + resId);
            return;
        }
        
        if (found.getStatus() == ReservationStatus.CANCELLED) {
            System.out.println("Cannot process payment for cancelled reservation.");
            return;
        }
        
        if (found.isPaymentCompleted()) {
            System.out.println("Payment already completed for this reservation.");
            System.out.printf("Amount Paid: $%.2f%n", found.getTotalAmount());
            return;
        }
        
        System.out.println("\n--- Payment Details ---");
        System.out.printf("Total Amount: $%.2f%n", found.getTotalAmount());
        System.out.println("\nPayment Methods:");
        System.out.println("1. Credit Card");
        System.out.println("2. Debit Card");
        System.out.println("3. Cash");
        System.out.print("Select payment method (1-3): ");
        
        int method;
        try {
            method = Integer.parseInt(scanner.nextLine());
            if (method < 1 || method > 3) {
                System.out.println("Invalid payment method.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }
        
        String paymentMethod = method == 1 ? "Credit Card" : method == 2 ? "Debit Card" : "Cash";
        
        System.out.println("\nProcessing payment...");
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        found.setPaymentCompleted(true);
        FileManager.saveReservations(reservations);
        
        System.out.println("\n" + "=".repeat(50));
        System.out.println("  PAYMENT SUCCESSFUL!");
        System.out.println("=".repeat(50));
        System.out.printf("Amount Paid: $%.2f%n", found.getTotalAmount());
        System.out.println("Payment Method: " + paymentMethod);
        System.out.println("Reservation Status: " + found.getStatus());
        System.out.println("=".repeat(50));
    }
    
    private void addGuest() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("             ADD NEW GUEST");
        System.out.println("=".repeat(50));
        createNewGuest();
    }
    
    private void viewAllRooms() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("             ALL ROOMS");
        System.out.println("=".repeat(50));
        System.out.println("-".repeat(70));
        for (Room room : rooms) {
            System.out.println(room);
        }
        System.out.println("-".repeat(70));
        
        int available = 0;
        for (Room room : rooms) {
            if (room.isAvailable()) available++;
        }
        System.out.printf("\nTotal Rooms: %d | Available: %d | Occupied: %d%n", 
                         rooms.size(), available, rooms.size() - available);
    }
    
    private Reservation findReservation(String reservationId) {
        for (Reservation res : reservations) {
            if (res.getReservationId().equalsIgnoreCase(reservationId.trim())) {
                return res;
            }
        }
        return null;
    }
    
    public void run() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("  Welcome to the Hotel Reservation System");
        System.out.println("=".repeat(50));
        
        while (true) {
            displayMenu();
            String input = scanner.nextLine().trim();
            
            if (input.isEmpty()) {
                System.out.println("Please enter a valid choice.");
                continue;
            }
            
            try {
                int choice = Integer.parseInt(input);
                
                switch (choice) {
                    case 1: 
                        searchAvailableRooms(); 
                        break;
                    case 2: 
                        bookRoom(); 
                        break;
                    case 3: 
                        viewAllReservations(); 
                        break;
                    case 4: 
                        viewBookingDetails(); 
                        break;
                    case 5: 
                        cancelReservation(); 
                        break;
                    case 6: 
                        processPayment(); 
                        break;
                    case 7: 
                        addGuest(); 
                        break;
                    case 8: 
                        viewAllRooms(); 
                        break;
                    case 9:
                        System.out.println("\n" + "=".repeat(50));
                        System.out.println("  Thank you for using Hotel Reservation System!");
                        System.out.println("=".repeat(50));
                        scanner.close();
                        return;
                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 9.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 9.");
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    public static void main(String[] args) {
        HotelReservationSystem system = new HotelReservationSystem();
        system.run();
    }
}
