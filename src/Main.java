import java.util.*;

// STRATEGY

// 1. Strategy Interface
interface PaymentStrategy {
    void pay(double amount);
}

// 2. Concrete Strategies

class CreditCardPayment implements PaymentStrategy {
    private String cardNumber;

    public CreditCardPayment(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    @Override
    public void pay(double amount) {
        System.out.println("Оплата " + amount + " тенге банковской картой: " + cardNumber);
    }
}

class PayPalPayment implements PaymentStrategy {
    private String email;

    public PayPalPayment(String email) {
        this.email = email;
    }

    @Override
    public void pay(double amount) {
        System.out.println("Оплата " + amount + " тенге через PayPal: " + email);
    }
}

class CryptoPayment implements PaymentStrategy {
    private String walletAddress;

    public CryptoPayment(String walletAddress) {
        this.walletAddress = walletAddress;
    }

    @Override
    public void pay(double amount) {
        System.out.println("Оплата " + amount + " тенге криптовалютой: " + walletAddress);
    }
}

// 3. Context
class PaymentContext {
    private PaymentStrategy strategy;

    public void setStrategy(PaymentStrategy strategy) {
        this.strategy = strategy;
    }

    public void executePayment(double amount) {
        if (strategy == null) {
            System.out.println("Стратегия оплаты не выбрана!");
        } else {
            strategy.pay(amount);
        }
    }
}

// OBSERVER

// 1. Observer Interface
interface Observer {
    void update(double usdRate);
}

// 2. Subject Interface
interface Subject {
    void addObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers();
}

// 3. Concrete Subject
class CurrencyExchange implements Subject {
    private List<Observer> observers = new ArrayList<>();
    private double usdRate;

    public void setUsdRate(double usdRate) {
        this.usdRate = usdRate;
        System.out.println("\nНовый курс USD: " + usdRate);
        notifyObservers();
    }

    public double getUsdRate() {
        return usdRate;
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(usdRate);
        }
    }
}

// 4. Concrete Observers

class BankObserver implements Observer {
    @Override
    public void update(double usdRate) {
        System.out.println("Банк получил обновление. Новый курс: " + usdRate);
    }
}

class InvestorObserver implements Observer {
    @Override
    public void update(double usdRate) {
        if (usdRate > 500) {
            System.out.println("Инвестор: Курс высокий, пора продавать!");
        } else {
            System.out.println("Инвестор: Курс низкий, можно покупать!");
        }
    }
}

class ExchangeOfficeObserver implements Observer {
    @Override
    public void update(double usdRate) {
        System.out.println("Обменный пункт обновил табло. USD = " + usdRate);
    }
}

// CLIENT (MAIN)

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        // STRATEGY DEMO

        System.out.println("=== ВЫБЕРИТЕ СПОСОБ ОПЛАТЫ ===");
        System.out.println("1 - Банковская карта");
        System.out.println("2 - PayPal");
        System.out.println("3 - Криптовалюта");

        int choice = scanner.nextInt();
        scanner.nextLine();

        PaymentContext context = new PaymentContext();

        switch (choice) {
            case 1:
                System.out.print("Введите номер карты: ");
                String card = scanner.nextLine();
                context.setStrategy(new CreditCardPayment(card));
                break;
            case 2:
                System.out.print("Введите email PayPal: ");
                String email = scanner.nextLine();
                context.setStrategy(new PayPalPayment(email));
                break;
            case 3:
                System.out.print("Введите адрес криптокошелька: ");
                String wallet = scanner.nextLine();
                context.setStrategy(new CryptoPayment(wallet));
                break;
            default:
                System.out.println("Неверный выбор!");
                return;
        }

        System.out.print("Введите сумму оплаты: ");
        double amount = scanner.nextDouble();

        context.executePayment(amount);

        // OBSERVER DEMO

        System.out.println("\nДЕМОНСТРАЦИЯ НАБЛЮДАТЕЛЯ");

        CurrencyExchange exchange = new CurrencyExchange();

        Observer bank = new BankObserver();
        Observer investor = new InvestorObserver();
        Observer office = new ExchangeOfficeObserver();

        exchange.addObserver(bank);
        exchange.addObserver(investor);
        exchange.addObserver(office);

        exchange.setUsdRate(495.0);
        exchange.setUsdRate(510.0);

        exchange.removeObserver(bank);

        System.out.println("\nУдалили банк из подписчиков.");

        exchange.setUsdRate(530.0);

        scanner.close();
    }
}
