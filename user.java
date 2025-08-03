import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class User {
    private String userId;
    private String name;
    private String email;
    private Map<String, LocalDate> borrowedBooks;  // bookId -> dueDate
    private double fines;
    
    public User(String userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.borrowedBooks = new HashMap<>();
        this.fines = 0.0;
    }
    
    // Getters
    public String getUserId() { return userId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public Map<String, LocalDate> getBorrowedBooks() { return borrowedBooks; }
    public double getFines() { return fines; }
    
    // Book management
    public void borrowBook(String bookId, LocalDate dueDate) {
        borrowedBooks.put(bookId, dueDate);
    }
    
    public void returnBook(String bookId) {
        borrowedBooks.remove(bookId);
    }
    
    // Fine management
    public void addFine(double amount) { 
        fines += amount; 
    }
    
    public void payFine(double amount) { 
        fines = Math.max(0, fines - amount); 
    }
    
    @Override
    public String toString() {
        return String.format("User ID: %-6s | Name: %-20s | Email: %-25s | Fines: $%.2f | Books Borrowed: %d",
                userId, name, email, fines, borrowedBooks.size());
    }
}