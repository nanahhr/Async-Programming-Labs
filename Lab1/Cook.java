import java.util.concurrent.Semaphore;

// Клас кухаря, що реалізує інтерфейс Runnable відповідно до вимог завдання
public class Cook implements Runnable {
    private final String name;
    private final Semaphore stoveSemaphore;
    private final Kitchen kitchen;

    public Cook(String name, Semaphore stoveSemaphore, Kitchen system) {
        this.name = name;
        this.stoveSemaphore = stoveSemaphore;
        this.kitchen = system;
    }

    @Override
    public void run() {
        try {
            // Кухар працює доти, доки кухня відкрита або в черзі ще є замовлення
            while (kitchen.isOpen() || kitchen.hasOrders()) {
                String order = kitchen.getNextOrder();

                if (order == null) {
                    Thread.sleep(300); // Очікування нових замовлень
                    continue;
                }

                Thread current = Thread.currentThread();
                System.out.printf("[ЗАМОВЛЕННЯ: %s]     Кухар %s узяв(-ла): %s%n",
                        current.getName(), name, order);

                // Запит доступу до плити через семафор (обмеження: 4 страви одночасно)
                stoveSemaphore.acquire();
                try {
                    System.out.printf(
                            "[ГОТУЄТЬСЯ: %s]      Кухар %s готує: %s  [ПЛИТА: %d/4 вiльних]%n",
                            current.getName(), name, order, stoveSemaphore.availablePermits());

                    // Симуляція процесу приготування (потік переходить у стан TIMED_WAITING)
                    Thread.sleep(1500);

                    System.out.printf("[ГОТОВО: %s]         Кухар %s закiнчив(-ла): %s%n",
                            current.getName(), name, order);
                } finally {
                    // Обов'язкове звільнення ресурсу плити
                    stoveSemaphore.release();
                }

                Thread.sleep(200);
            }

            System.out.printf("[ЗМIНА ЗАВЕРШЕНА]        Кухар %s пiшов(-ла) додому.%n", name);

        } catch (InterruptedException e) {
            System.err.println("Помилка виконання потоку кухаря " + name + ": " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}