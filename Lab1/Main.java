import java.util.concurrent.Semaphore;

public class Main {
    public static void main(String[] args) {
        System.out.println("================================================================");
        System.out.println("               ОБРОБКА ЗАМОВЛЕНЬ НА КУХНI              ");
        System.out.println("================================================================\n");

        // Семафор для обмеження кількості страв на плиті (максимум 4)
        Semaphore stoveSemaphore = new Semaphore(4, true);
        Kitchen kitchen = new Kitchen();

        System.out.println("------------- Прийом початкових замовлень клiєнтiв -------------");
        kitchen.addOrder("Стейк з лосося");
        kitchen.addOrder("Пiца Маргарита");
        kitchen.addOrder("Цезар з куркою");
        kitchen.addOrder("Паста Карбонара");
        kitchen.addOrder("Борщ український");
        kitchen.addOrder("Мексиканський бурiто");
        kitchen.addOrder("Картопля фрi");
        System.out.println("----------------------------------------------------------------\n");

        // Створення потоків-кухарів через інтерфейс Runnable
        Thread cook1 = new Thread(new Cook("Катерина", stoveSemaphore, kitchen), "Cook-1");
        Thread cook2 = new Thread(new Cook("Вiкторiя", stoveSemaphore, kitchen), "Cook-2");
        Thread cook3 = new Thread(new Cook("Олег", stoveSemaphore, kitchen), "Cook-3");

        // Демонстрація початкового стану потоку (NEW)
        System.out.println("Стан потоку Cook-1 до запуску: " + cook1.getState());
        System.out.println("\n---------------- ЗАПУСК РОБОТИ ПОТОКIВ-КУХАРIВ -----------------\n");

        cook1.start();
        cook2.start();
        cook3.start();

        try {
            // Симуляція робочого часу кухні
            Thread.sleep(2500);

            System.out.println("\n----------- НАДIЙШЛИ ДОДАТКОВI ЗАМОВЛЕННЯ ПIД ЧАС ЗМIНИ --------");
            kitchen.addOrder("Грецький салат");
            kitchen.addOrder("Суп мiсо");
            System.out.println("----------------------------------------------------------------\n");

            Thread.sleep(2000);

            // Закриття кухні (нові замовлення не приймаються, але поточна черга
            // доготовлюється)
            kitchen.closeKitchen();

            // Спроба зробити замовлення після закриття
            kitchen.addOrder("Морозиво пломбiр");

            // Очікування завершення роботи всіх потоків (join)
            cook1.join();
            cook2.join();
            cook3.join();

        } catch (InterruptedException e) {
            System.err.println("Головний потiк було перервано: " + e.getMessage());
            Thread.currentThread().interrupt();
        }

        System.out.println("\n=============================================================");
        System.out.println("Фiнальний стан потоку Cook-1: " + cook1.getState()); // TERMINATED
        System.out.println("Фiнальний стан потоку Cook-2: " + cook2.getState());
        System.out.println("Фiнальний стан потоку Cook-3: " + cook3.getState());
        System.out.println("СИМУЛЯЦIЮ РОБОТИ КУХНI УСПIШНО ЗАВЕРШЕНО");
        System.out.println("=============================================================");
    }
}