import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

class Car {
  private String carId;
  private String brand;
  private String model;
  private double basePricePerDay; // boolean mein price ko le sake
  private boolean isAvailable;

  public Car(String carId, String brand, String model, double basePricePerDay) {
    this.carId = carId;
    this.brand = brand;
    this.model = model;
    this.basePricePerDay = basePricePerDay;
    this.isAvailable = true;
  }

  public String getCarId() {
    return carId;
  }

  public String getBrand() {
    return brand;
  }

  public String getModel() {
    return model;
  }

  public double calculatePrice(int rentalPrice) {
    return basePricePerDay * rentalPrice;
  }

  public boolean isAvailable() {
    return isAvailable;
  }

  public void rent() {
    isAvailable = false;
  }

  public void returnCar() {
    isAvailable = true;
  }
}

class Customer {
  private String customerId;
  private String customerName;

  public Customer(String cutomerId, String customerName) {
    this.customerId = cutomerId;
    this.customerName = customerName;
  }

  public String getCustomerId() {
    return customerId;
  }

  public String getCustomerName() {
    return customerName;
  }
}

class Rental {
  private Car car;
  private Customer customer;
  private int days;

  public Rental(Car car, Customer customer, int days) {
    this.car = car;
    this.customer = customer;
    this.days = days;
  }

  public Car getCar() {
    return car;
  }

  public Customer getCustomer() {
    return customer;
  }

  public int getDays() {
    return days;
  }
}

class CarRentalSystem {
  // Car ka data store rahega
  private List<Car> cars;

  // Customer ka data store rahega
  private List<Customer> customers;

  // Kis customer ne kon si car rent pe li hai wo data store rahega
  private List<Rental> rentals;

  // Ab upar banaye sare list ko memory allocate kar diye. Ab ready hai data ko
  // store karne ke liye
  public CarRentalSystem() {
    cars = new ArrayList<>();
    customers = new ArrayList<>();
    rentals = new ArrayList<>();
  }

  public void addCar(Car car) {
    cars.add(car);
  }

  public void addCustomer(Customer customer) {
    customers.add(customer);
  }

  // Kon customer, kon sa car kitne dino ke liye rent pe rak raha hai
  public void rentCar(Car car, Customer customer, int days) {
    if (car.isAvailable()) {
      car.rent();
      rentals.add(new Rental(car, customer, days));
    }

    else {
      System.out.println("Car is not available for rent");
    }
  }

  public void returnCar(Car car) {
    car.returnCar();
    Rental rentalToRemove = null;
    for (Rental rental : rentals) {
      if (rental.getCar() == car) {
        rentalToRemove = rental;
        break;
      }
    }

    if (rentalToRemove != null) {
      rentals.remove(rentalToRemove);
      System.out.println("Car returned succefully.");
    }

    else {
      System.out.println("Car was not rented.");
    }
  }

  public void meun() {
    Scanner sc = new Scanner(System.in);

    // while(true) is liye kiye hai taki main menu mein tab tak rahega jab tak khud
    // se user na bol de ki use exit karna hai.
    while (true) {
      System.out.println("==== Car Rental System ====");
      System.out.println("1. Rent a Car");
      System.out.println("2. Return a Car");
      System.out.println("3. Exit");
      System.out.println("Enter your choice: ");

      int choice = sc.nextInt();
      sc.nextLine(); // Consume new line.

      if (choice == 1) {
        System.out.println("== Rent a Car ==");
        System.out.println("Enter your name: ");
        String customerName = sc.nextLine();

        System.out.println("Available Cars");
        for (Car car : cars) {
          if (car.isAvailable()) {
            System.out.println(car.getCarId() + " - " + car.getBrand() + " " + car.getModel());
          }
        }

        System.out.println("Enter the car ID you want to rent: ");
        String carId = sc.nextLine();

        System.out.println("Enter the number of days for rental: ");
        int rentalDays = sc.nextInt();
        sc.nextLine();

        Customer newCustomer = new Customer("CUS" + (customers.size() + 1), customerName);
        addCustomer(newCustomer);

        Car selectedCar = null;
        for (Car car : cars) {
          if (car.getCarId().equals(carId) && car.isAvailable()) {
            selectedCar = car;
            break;
          }
        }

        if (selectedCar != null) {
          double totalPrice = selectedCar.calculatePrice(rentalDays);
          System.out.println("== Rental Information ==");
          System.out.println("Customer ID: " + newCustomer.getCustomerId());
          System.out.println("Customer Name: " + newCustomer.getCustomerName());
          System.out.println("Car: " + selectedCar.getBrand() + " " + selectedCar.getModel());
          System.out.println("Rental Days: " + rentalDays);
          System.out.printf("Total Price: Rs%.2f%n", totalPrice);

          System.out.println("Confirm rental (Y/N): ");
          String confirm = sc.nextLine();

          if (confirm.equalsIgnoreCase("Y")) {
            rentCar(selectedCar, newCustomer, rentalDays);
            System.out.println("Car rented successfully");
          }

          else {
            System.out.println("Rental cancelled.");
          }
        }

        else {
          System.out.println("Invalid car selection or car not available for rent.");
        }
      }

      else if (choice == 2) {
        System.out.println("== Return a Car ==");
        System.out.println("Enter the car ID you want to return: ");
        String carId = sc.nextLine();

        Car carToReturn = null;
        for (Car car : cars) {
          if (car.getCarId().equals(carId) && !car.isAvailable()) {
            carToReturn = car;
            break;
          }
        }

        if (carToReturn != null) {
          Customer customer = null;
          for (Rental rental : rentals) {
            if (rental.getCar() == carToReturn) {
              customer = rental.getCustomer();
              break;
            }
          }

          if (customer != null) {
            returnCar(carToReturn);
            System.out.println("Car returned successfully by " + customer.getCustomerName());
          }

          else {
            System.out.println("Car was not rented or rental information is missing.");
          }
        }

        else {
          System.out.println("Invalid car ID or car is not rented");
        }
      }

      else if (choice == 3) {
        break;
      }

      else {
        System.out.println("Invalid choice. Please enter a valid option.");
      }
    }

    System.out.println("Thank you for using the Car Rental System!");
  }
}

public class Main {
  public static void main(String[] args) {
    CarRentalSystem rentalSystem = new CarRentalSystem();

    Car car1 = new Car("C001", "Toyota", "Fortuner", 150.0);
    Car car2 = new Car("C002", "Honda", "City", 70.0);
    Car car3 = new Car("C003", "Hundayi", "Verna", 90.0);
    Car car4 = new Car("C004", "Tata", "Harrier", 110.0);

    rentalSystem.addCar(car1);
    rentalSystem.addCar(car2);
    rentalSystem.addCar(car3);
    rentalSystem.addCar(car4);

    rentalSystem.meun();
  }
}