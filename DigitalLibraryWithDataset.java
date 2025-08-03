import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class DigitalLibraryWithDataset {
    private static Library library = new Library();
    private static Scanner scanner = new Scanner(System.in);
    private static User currentUser = null;
    private static boolean isAdmin = false;
    
    public static void main(String[] args) {
        // Load initial data from datasets
        try {
            library.loadBooksFromCSV("books.csv");
            library.loadUsersFromCSV("users.csv");
            System.out.println("Initial data loaded successfully.");
        } catch (IOException e) {
            System.out.println("Error loading initial data: " + e.getMessage());
            System.out.println("Using empty library.");
        }
        
        System.out.println("Welcome to Digital Library Management System");
        
        while (true) {
            if (currentUser == null) {
                loginMenu();
            } else {
                if (isAdmin) {
                    adminMenu();
                } else {
                    userMenu();
                }
            }
        }
    }

    //  MENU METHODS 
    private static void loginMenu() {
        System.out.println("\n=== LOGIN MENU ===");
        System.out.println("1. Admin Login");
        System.out.println("2. User Login");
        System.out.println("3. Exit");
        System.out.print("Enter your choice: ");
        
        int choice = getIntInput();
        
        switch (choice) {
            case 1:
                adminLogin();
                break;
            case 2:
                userLogin();
                break;
            case 3:
                System.out.println("Thank you for using the system. Goodbye!");
                System.exit(0);
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private static void adminLogin() {
        System.out.print("\nEnter Admin User ID: ");
        String userId = scanner.nextLine();
        
        if (library.getAllUsers().containsKey(userId)) {
            currentUser = library.getAllUsers().get(userId);
            isAdmin = true;
            System.out.println("Admin login successful! Welcome " + currentUser.getName());
        } else {
            System.out.println("Invalid admin credentials.");
        }
    }

    private static void userLogin() {
        System.out.print("\nEnter User ID: ");
        String userId = scanner.nextLine();
        
        if (library.getAllUsers().containsKey(userId)) {
            currentUser = library.getAllUsers().get(userId);
            isAdmin = false;
            System.out.println("Login successful! Welcome " + currentUser.getName());
        } else {
            System.out.println("User not found.");
        }
    }

    private static void adminMenu() {
        while (true) {
            System.out.println("\n=== ADMIN MENU ===");
            System.out.println("1. Add Book");
            System.out.println("2. Remove Book");
            System.out.println("3. Update Book");
            System.out.println("4. Add User");
            System.out.println("5. Remove User");
            System.out.println("6. View All Books");
            System.out.println("7. View All Users");
            System.out.println("8. Generate Reports");
            System.out.println("9. Logout");
            System.out.print("Enter your choice: ");
            
            int choice = getIntInput();
            
            switch (choice) {
                case 1:
                    addBook();
                    break;
                case 2:
                    removeBook();
                    break;
                case 3:
                    updateBook();
                    break;
                case 4:
                    addUser();
                    break;
                case 5:
                    removeUser();
                    break;
                case 6:
                    viewAllBooks();
                    break;
                case 7:
                    viewAllUsers();
                    break;
                case 8:
                    generateReports();
                    break;
                case 9:
                    currentUser = null;
                    isAdmin = false;
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void userMenu() {
        while (true) {
            System.out.println("\n=== USER MENU ===");
            System.out.println("1. Search Books");
            System.out.println("2. Browse by Category");
            System.out.println("3. Reserve Book");
            System.out.println("4. Cancel Reservation");
            System.out.println("5. View My Books");
            System.out.println("6. Pay Fines");
            System.out.println("7. Logout");
            System.out.print("Enter your choice: ");
            
            int choice = getIntInput();
            
            switch (choice) {
                case 1:
                    searchBooks();
                    break;
                case 2:
                    browseByCategory();
                    break;
                case 3:
                    reserveBook();
                    break;
                case 4:
                    cancelReservation();
                    break;
                case 5:
                    viewMyBooks();
                    break;
                case 6:
                    payFines();
                    break;
                case 7:
                    currentUser = null;
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // OPERATION 
    private static void addBook() {
        System.out.println("\n=== ADD NEW BOOK ===");
        System.out.print("Enter Book ID: ");
        String id = scanner.nextLine();
        
        if (library.getAllBooks().containsKey(id)) {
            System.out.println("Book with this ID already exists!");
            return;
        }
        
        System.out.print("Enter Title: ");
        String title = scanner.nextLine();
        System.out.print("Enter Author: ");
        String author = scanner.nextLine();
        System.out.print("Enter Category: ");
        String category = scanner.nextLine();
        
        library.addBook(new Book(id, title, author, category));
        System.out.println("Book added successfully!");
    }

    private static void removeBook() {
        System.out.print("\nEnter Book ID to remove: ");
        String bookId = scanner.nextLine();
        
        if (library.removeBook(bookId)) {
            System.out.println("Book removed successfully.");
        } else {
            System.out.println("Book not found or could not be removed.");
        }
    }

    private static void updateBook() {
        System.out.print("\nEnter Book ID to update: ");
        String bookId = scanner.nextLine();
        
        if (!library.getAllBooks().containsKey(bookId)) {
            System.out.println("Book not found!");
            return;
        }
        
        Book book = library.getAllBooks().get(bookId);
        System.out.println("Current details:");
        System.out.println(book);
        
        System.out.print("Enter new Title (leave blank to keep current): ");
        String title = scanner.nextLine();
        if (!title.isEmpty()) {
            book.setTitle(title);
        }
        
        System.out.print("Enter new Author (leave blank to keep current): ");
        String author = scanner.nextLine();
        if (!author.isEmpty()) {
            book.setAuthor(author);
        }
        
        System.out.print("Enter new Category (leave blank to keep current): ");
        String category = scanner.nextLine();
        if (!category.isEmpty()) {
            book.setCategory(category);
        }
        
        System.out.println("Book updated successfully!");
    }

    private static void addUser() {
        System.out.println("\n=== ADD NEW USER ===");
        System.out.print("Enter User ID: ");
        String id = scanner.nextLine();
        
        if (library.getAllUsers().containsKey(id)) {
            System.out.println("User with this ID already exists!");
            return;
        }
        
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        
        library.addUser(new User(id, name, email));
        System.out.println("User added successfully!");
    }

    private static void removeUser() {
        System.out.print("\nEnter User ID to remove: ");
        String userId = scanner.nextLine();
        
        if (library.removeUser(userId)) {
            System.out.println("User removed successfully.");
        } else {
            System.out.println("User not found or could not be removed.");
        }
    }

    private static void viewAllBooks() {
        System.out.println("\n=== ALL BOOKS ===");
        if (library.getAllBooks().isEmpty()) {
            System.out.println("No books available.");
            return;
        }
        
        library.getAllBooks().values().forEach(book -> {
            System.out.println(book);
            if (!book.isAvailable()) {
                System.out.println("  - Currently checked out");
                if (!book.getReservationQueue().isEmpty()) {
                    System.out.println("  - Reservations: " + book.getReservationQueue().size());
                }
            }
            System.out.println("----------------------");
        });
    }

    private static void viewAllUsers() {
        System.out.println("\n=== ALL USERS ===");
        if (library.getAllUsers().isEmpty()) {
            System.out.println("No users registered.");
            return;
        }
        
        library.getAllUsers().values().forEach(user -> {
            System.out.println(user);
            if (!user.getBorrowedBooks().isEmpty()) {
                System.out.println("  Borrowed Books:");
                user.getBorrowedBooks().forEach((bookId, dueDate) -> {
                    System.out.println("    Book ID: " + bookId + " (Due: " + dueDate + ")");
                    if (LocalDate.now().isAfter(dueDate)) {
                        long daysOverdue = ChronoUnit.DAYS.between(dueDate, LocalDate.now());
                        System.out.println("      OVERDUE by " + daysOverdue + " days!");
                    }
                });
            }
            System.out.println("----------------------");
        });
    }

    private static void searchBooks() {
        System.out.print("\nEnter search term (title/author/category): ");
        String query = scanner.nextLine();
        
        List<Book> results = library.searchBooks(query);
        
        System.out.println("\n=== SEARCH RESULTS ===");
        if (results.isEmpty()) {
            System.out.println("No books found matching your search.");
        } else {
            results.forEach(book -> {
                System.out.println(book);
                System.out.println("----------------------");
            });
        }
    }

    private static void browseByCategory() {
        System.out.print("\nEnter category to browse: ");
        String category = scanner.nextLine();
        
        List<Book> results = library.getBooksByCategory(category);
        
        System.out.println("\n=== BOOKS IN CATEGORY: " + category + " ===");
        if (results.isEmpty()) {
            System.out.println("No books found in this category.");
        } else {
            results.forEach(book -> {
                System.out.println(book);
                System.out.println("----------------------");
            });
        }
    }

    private static void reserveBook() {
        System.out.print("\nEnter Book ID to reserve: ");
        String bookId = scanner.nextLine();
        
        if (!library.getAllBooks().containsKey(bookId)) {
            System.out.println("Book not found!");
            return;
        }
        
        Book book = library.getAllBooks().get(bookId);
        
        if (book.isAvailable()) {
            System.out.print("Book is available. Would you like to borrow it instead? (Y/N): ");
            String choice = scanner.nextLine();
            if (choice.equalsIgnoreCase("Y")) {
                if (library.issueBook(currentUser.getUserId(), bookId)) {
                    System.out.println("Book borrowed successfully! Due in 14 days.");
                } else {
                    System.out.println("Failed to borrow book. You may have outstanding fines.");
                }
            }
            return;
        }
        
        if (library.reserveBook(currentUser.getUserId(), bookId)) {
            System.out.println("Reservation successful! You are number " + 
                             (book.getReservationQueue().size()) + " in queue.");
        } else {
            System.out.println("Failed to reserve book. You may have already reserved it.");
        }
    }

    private static void cancelReservation() {
        System.out.print("\nEnter Book ID to cancel reservation: ");
        String bookId = scanner.nextLine();
        
        if (library.cancelReservation(currentUser.getUserId(), bookId)) {
            System.out.println("Reservation cancelled successfully.");
        } else {
            System.out.println("No active reservation found for this book.");
        }
    }

    private static void viewMyBooks() {
        System.out.println("\n=== YOUR BORROWED BOOKS ===");
        Map<String, LocalDate> borrowedBooks = currentUser.getBorrowedBooks();
        
        if (borrowedBooks.isEmpty()) {
            System.out.println("You haven't borrowed any books.");
            return;
        }
        
        borrowedBooks.forEach((bookId, dueDate) -> {
            Book book = library.getAllBooks().get(bookId);
            System.out.println(book);
            System.out.println("Due Date: " + dueDate);
            if (LocalDate.now().isAfter(dueDate)) {
                long daysOverdue = ChronoUnit.DAYS.between(dueDate, LocalDate.now());
                double fine = daysOverdue * Library.DAILY_FINE;
                System.out.printf("OVERDUE by %d days! Fine: $%.2f%n", daysOverdue, fine);
            }
            System.out.println("----------------------");
        });
    }

    private static void payFines() {
        double currentFines = currentUser.getFines();
        System.out.printf("\nYour current fines: $%.2f%n", currentFines);
        
        if (currentFines <= 0) {
            System.out.println("You don't have any outstanding fines.");
            return;
        }
        
        System.out.print("Enter amount to pay: ");
        try {
            double amount = Double.parseDouble(scanner.nextLine());
            if (amount <= 0) {
                System.out.println("Amount must be positive.");
                return;
            }
            
            currentUser.payFine(amount);
            System.out.printf("Payment processed. Remaining fines: $%.2f%n", currentUser.getFines());
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount entered.");
        }
    }

    private static void generateReports() {
        System.out.println("\n=== REPORTS MENU ===");
        System.out.println("1. Overdue Books Report");
        System.out.println("2. Popular Books Report");
        System.out.println("3. User Activity Report");
        System.out.print("Enter your choice: ");
        
        int choice = getIntInput();
        
        switch (choice) {
            case 1:
                library.generateOverdueReport();
                break;
            case 2:
                System.out.print("Enter number of top books to show: ");
                int topN = getIntInput();
                library.generatePopularBooksReport(topN);
                break;
            case 3:
                library.generateUserActivityReport();
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    
    private static int getIntInput() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a number: ");
            }
        }
    }
}