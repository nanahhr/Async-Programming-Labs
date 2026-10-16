import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

// Клас управління станом кухні та чергою замовлень
public class Kitchen {
    private final Queue<String> orderQueue = new LinkedList<>();
    // Використання AtomicBoolean для потокобезпечного прапорця
    private final AtomicBoolean isOpen = new AtomicBoolean(true);

    public synchronized void addOrder(String order) {
        if (isOpen.get()) {
            orderQueue.add(order);
            System.out.println("  + Додано до черги: " + order);
        } else {
            System.out.println("  x ВIДХИЛЕНО (кухня закрита): " + order);
        }
    }

    public synchronized String getNextOrder() {
        return orderQueue.poll();
    }

    public synchronized boolean hasOrders() {
        return !orderQueue.isEmpty();
    }

    public boolean isOpen() {
        return isOpen.get();
    }

    public void closeKitchen() {
        isOpen.set(false);
        System.out.println("\n------------------------------------------------------------------");
        System.out.println("   ПОДIЯ: КУХНЯ ЗАЧИНЕНА. Новi замовлення бiльше не приймаються.");
        System.out.println("------------------------------------------------------------------\n");
    }
}