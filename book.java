import java.util.ArrayList;
import java.util.List;

public class Book {
    private String id;
    private String title;
    private String author;
    private String category;
    private boolean available;
    private List<String> reservationQueue;  // Stores user IDs of those waiting for the book
    
    public Book(String id, String title, String author, String category) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.category = category;
        this.available = true;
        this.reservationQueue = new ArrayList<>();
    }
    
    // Getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getCategory() { return category; }
    public boolean isAvailable() { return available; }
    public List<String> getReservationQueue() { return reservationQueue; }
    
    // Setters
    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setCategory(String category) { this.category = category; }
    public void setAvailable(boolean available) { this.available = available; }
    
    // Reservation management
    public void addReservation(String userId) {
        if (!reservationQueue.contains(userId)) {
            reservationQueue.add(userId);
        }
    }
    
    public void removeReservation(String userId) {
        reservationQueue.remove(userId);
    }
    
    @Override
    public String toString() {
        return String.format("ID: %-6s | Title: %-25s | Author: %-20s | Category: %-15s | Available: %-5s | Reservations: %d",
                id, title, author, category, available, reservationQueue.size());
    }
}