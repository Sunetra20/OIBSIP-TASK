import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Library {
    private Map<String, Book> books;
    private Map<String, User> users;
    public static final double DAILY_FINE = 0.50;
    private static final int MAX_LOAN_DAYS = 14;
    
    public Library() {
        this.books = new HashMap<>();
        this.users = new HashMap<>();
    }
    
    // DATA LOADING 
    public void loadBooksFromCSV(String filePath) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length >= 4) {
                    String id = values[0].trim();
                    String title = values[1].trim();
                    String author = values[2].trim();
                    String category = values[3].trim();
                    books.put(id, new Book(id, title, author, category));
                }
            }
        }
    }
    
    public void loadUsersFromCSV(String filePath) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length >= 3) {
                    String id = values[0].trim();
                    String name = values[1].trim();
                    String email = values[2].trim();
                    users.put(id, new User(id, name, email));
                }
            }
        }
    }
    
    //  BOOK OPERATIONS 
    public void addBook(Book book) {
        books.put(book.getId(), book);
    }
    
    public boolean removeBook(String bookId) {
        if (books.containsKey(bookId)) {
            // Check if book is checked out
            Book book = books.get(bookId);
            if (!book.isAvailable()) {
                System.out.println("Cannot remove - book is currently checked out.");
                return false;
            }
            books.remove(bookId);
            return true;
        }
        return false;
    }
    
    public boolean updateBook(String bookId, Book updatedBook) {
        if (books.containsKey(bookId)) {
            books.put(bookId, updatedBook);
            return true;
        }
        return false;
    }
    
    // USER 
    public void addUser(User user) {
        users.put(user.getUserId(), user);
    }
    
    public boolean removeUser(String userId) {
        if (users.containsKey(userId)) {
            // Check if user has borrowed books
            User user = users.get(userId);
            if (!user.getBorrowedBooks().isEmpty()) {
                System.out.println("Cannot remove - user has borrowed books.");
                return false;
            }
            users.remove(userId);
            return true;
        }
        return false;
    }
    
    // LIBRARY 
    public boolean issueBook(String userId, String bookId) {
        if (!books.containsKey(bookId) || !users.containsKey(userId)) {
            return false;
        }
        
        Book book = books.get(bookId);
        User user = users.get(userId);
        
        if (!book.isAvailable() || user.getFines() > 0) {
            return false;
        }
        
        LocalDate dueDate = LocalDate.now().plusDays(MAX_LOAN_DAYS);
        book.setAvailable(false);
        user.borrowBook(bookId, dueDate);
        return true;
    }
    
    public boolean returnBook(String userId, String bookId) {
        if (!books.containsKey(bookId) || !users.containsKey(userId)) {
            return false;
        }
        
        Book book = books.get(bookId);
        User user = users.get(userId);
        
        if (!user.getBorrowedBooks().containsKey(bookId)) {
            return false;
        }
        
        // Calculate fines if overdue
        LocalDate dueDate = user.getBorrowedBooks().get(bookId);
        if (LocalDate.now().isAfter(dueDate)) {
            long daysOverdue = ChronoUnit.DAYS.between(dueDate, LocalDate.now());
            double fine = daysOverdue * DAILY_FINE;
            user.addFine(fine);
        }
        
        book.setAvailable(true);
        user.returnBook(bookId);
        
        // Notify next in reservation queue
        if (!book.getReservationQueue().isEmpty()) {
            String nextUserId = book.getReservationQueue().remove(0);
            System.out.println("Notification: Book " + bookId + " now available for user " + nextUserId);
        }
        
        return true;
    }
    
    public boolean reserveBook(String userId, String bookId) {
        if (!books.containsKey(bookId) || !users.containsKey(userId)) {
            return false;
        }
        
        Book book = books.get(bookId);
        if (book.isAvailable()) {
            return false;
        }
        
        if (book.getReservationQueue().contains(userId)) {
            return false;
        }
        
        book.addReservation(userId);
        return true;
    }
    
    public boolean cancelReservation(String userId, String bookId) {
        if (!books.containsKey(bookId)) {
            return false;
        }
        
        Book book = books.get(bookId);
        book.removeReservation(userId);
        return true;
    }
    
    
    public List<Book> searchBooks(String query) {
        List<Book> results = new ArrayList<>();
        String lowerQuery = query.toLowerCase();
        
        books.values().forEach(book -> {
            if (book.getTitle().toLowerCase().contains(lowerQuery) ||
                book.getAuthor().toLowerCase().contains(lowerQuery) ||
                book.getCategory().toLowerCase().contains(lowerQuery)) {
                results.add(book);
            }
        });
        
        return results;
    }
    
    public List<Book> getBooksByCategory(String category) {
        List<Book> results = new ArrayList<>();
        String lowerCategory = category.toLowerCase();
        
        books.values().forEach(book -> {
            if (book.getCategory().toLowerCase().equals(lowerCategory)) {
                results.add(book);
            }
        });
        
        return results;
    }
    
    // REPORT 
    public void generateOverdueReport() {
        System.out.println("\n=== OVERDUE BOOKS REPORT ===");
        boolean foundOverdue = false;
        
        for (User user : users.values()) {
            for (Map.Entry<String, LocalDate> entry : user.getBorrowedBooks().entrySet()) {
                if (LocalDate.now().isAfter(entry.getValue())) {
                    foundOverdue = true;
                    long daysOverdue = ChronoUnit.DAYS.between(entry.getValue(), LocalDate.now());
                    double fine = daysOverdue * DAILY_FINE;
                    System.out.printf("User: %-20s | Book ID: %-6s | Days Overdue: %3d | Fine: $%.2f%n",
                            user.getName(), entry.getKey(), daysOverdue, fine);
                }
            }
        }
        
        if (!foundOverdue) {
            System.out.println("No overdue books found.");
        }
    }
    
    public void generatePopularBooksReport(int topN) {
        System.out.printf("\n=== TOP %d MOST POPULAR BOOKS ===%n", topN);
        
        List<Book> sortedBooks = new ArrayList<>(books.values());
        sortedBooks.sort((b1, b2) -> 
            Integer.compare(b2.getReservationQueue().size(), b1.getReservationQueue().size()));
        
        for (int i = 0; i < Math.min(topN, sortedBooks.size()); i++) {
            Book book = sortedBooks.get(i);
            System.out.printf("%2d. %-25s by %-20s | Reservations: %2d%n",
                    i+1, book.getTitle(), book.getAuthor(), book.getReservationQueue().size());
        }
    }
    
    public void generateUserActivityReport() {
        System.out.println("\n=== USER ACTIVITY REPORT ===");
        
        users.values().stream()
            .sorted((u1, u2) -> 
                Integer.compare(u2.getBorrowedBooks().size(), u1.getBorrowedBooks().size()))
            .forEach(user -> {
                System.out.printf("%-20s | Books Borrowed: %2d | Fines: $%.2f%n",
                        user.getName(), user.getBorrowedBooks().size(), user.getFines());
            });
    }
    
    
    public Map<String, Book> getAllBooks() {
        return books;
    }
    
    public Map<String, User> getAllUsers() {
        return users;
    }
}