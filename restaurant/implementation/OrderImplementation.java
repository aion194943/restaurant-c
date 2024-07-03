package ro.ase.ppoo.implementation;

import ro.ase.ppoo.classes.Order;
import ro.ase.ppoo.classes.PaymentType;
import ro.ase.ppoo.utils.UtilValues;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class OrderImplementation {
    private List<Order> orders;

    private void saveOrders() {
        List<String> ordersToString = orders.stream().map(Order::toString).toList();
        try {
            Files.write(Paths.get(UtilValues.dbLocation), ordersToString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }


    public List<Order> getOrders() {
        return orders;
    }

    private void saveAllOrders(List<Order> orders) {

        Path pathToDB = Paths.get(UtilValues.dbLocation);
        if (Files.exists(pathToDB)) {
            try {
                List<String> orderEntries = Files.readAllLines(pathToDB);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


    }

    public void loadOrders() {
        Path pathToDB = Paths.get(UtilValues.dbLocation);
        List<Order> loadedOrders = new ArrayList<>();

        if (Files.exists(pathToDB)) {
            try {
                List<String> orderEntries = Files.readAllLines(pathToDB);
                for (String entry : orderEntries) {
                    if (entry.trim().isEmpty()) {
                        continue;
                    }

                    String[] parts = entry.split(", ");
                    if (parts.length < 7) {
                        System.err.println("Skipping malformed entry: " + entry);
                        continue;
                    }

                    String clientName = parseStringField(parts[0]);
                    String address = parseStringField(parts[1]);
                    String idOrder = parseStringField(parts[2]);
                    LocalDate orderDate = parseDateField(parts[3]);
                    int totalAmount = parseIntField(parts[4]);
                    String menuList = parseStringField(parts[5]);
                    PaymentType type = parsePaymentTypeField(parts[6]);

                    Order order = new Order(clientName, address, idOrder, orderDate, totalAmount, menuList, type);
                    loadedOrders.add(order);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        orders = loadedOrders;
    }

    private String parseStringField(String part) {
        try {
            return part.split("'")[1];
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Error parsing string field: " + part);
            return "";
        }
    }

    private LocalDate parseDateField(String part) {
        try {
            return LocalDate.parse(part.split("=")[1], DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (Exception e) {
            System.err.println("Error parsing date field: " + part);
            return LocalDate.now();
        }
    }

    private int parseIntField(String part) {
        try {
            return Integer.parseInt(part.split("=")[1]);
        } catch (NumberFormatException e) {
            System.err.println("Error parsing int field: " + part);
            return 0;
        }
    }

    private List<String> parseListField(String part) {
        try {
            return Arrays.asList(part.split("=")[1].replace("[", "").replace("]", "").split(", "));
        } catch (Exception e) {
            System.err.println("Error parsing list field: " + part);
            return new ArrayList<>();
        }
    }

    private PaymentType parsePaymentTypeField(String part) {
        try {
            String paymentTypeStr = part.split("=")[1].trim().replace("}", "");
            return PaymentType.valueOf(paymentTypeStr);
        } catch (IllegalArgumentException e) {
            System.err.println("Error parsing payment type field: " + part);
            return PaymentType.NONE;
        }
    }

    public void makeAnOrder() {
        Order order = null;
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter Client Name");
            String clientName = scanner.nextLine();
            checkUserInput(clientName);
            System.out.println("Enter Address");
            String address = scanner.nextLine();
            checkUserInput(address);
            System.out.println("Enter Id Of Oder");
            String oderId = scanner.nextLine();
            checkUserInput(oderId);
            System.out.println("Enter Order Date");
            String oderDateString = scanner.nextLine();
            LocalDate oderDate = LocalDate.parse(oderDateString);
            System.out.println("Enter Chosen Menu");
            String chosenMenu = scanner.nextLine();
            checkUserInput(chosenMenu);
            System.out.println("Enter Payment Type");
            String paymentTypeString = scanner.nextLine();
            PaymentType paymentType = PaymentType.valueOf(paymentTypeString.toUpperCase());
            System.out.println("Enter Total Amount");
            int oderAmount = scanner.nextInt();
            order = new Order(clientName, address, oderId, oderDate, oderAmount, chosenMenu, paymentType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        orders.add(order);
        System.out.println("Order added!");
    }


    public void updateDatabase() {
        saveOrders();
    }

    public void checkUserInput(String userInput) throws Exception {
        if (userInput.contains(",")) throw new Exception("there should be no \",\" in the inputs");
    }

    public void printOrders() {
        if (orders != null) {
            System.out.println(orders);
        } else {
            System.out.println("No orders yet! Get some clients");
        }

    }

    public void mostFrequentPurchases() {

        var entrySet = orders.stream().collect(Collectors.groupingBy(Order::getChosenMenu, Collectors.counting()))
                .entrySet().stream().sorted(Map.Entry.<String, Long>comparingByValue().reversed()).toList();
        entrySet.forEach(
                (menuOrdered) -> System.out.println("Menu " + menuOrdered.getKey() + " has been purchased " + menuOrdered.getValue() + " times!")
        );

        System.out.println();
    }

    public void clientHistory() {
        System.out.println("Enter Client Name");
        Scanner scanner = new Scanner(System.in);
        String clientName = scanner.nextLine();
        List<Order> clientOrders;
        try {
            clientOrders = orders.stream().filter(order -> order.getClientName().equalsIgnoreCase(clientName)).toList();
            if (clientOrders.isEmpty()) {
                throw new Exception("No client with such name!");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        System.out.println("Client :" + clientName + " has the following orders: \n");
        System.out.println(clientOrders);
    }

    public void dailyRevenues() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Payment Type");
        String paymentTypeString = scanner.nextLine();
        PaymentType paymentType = PaymentType.valueOf(paymentTypeString.toUpperCase());

        Map<LocalDate, Integer> datesWithTheirSums = orders.stream().filter(order -> order.getType().equals(paymentType))
                .collect(Collectors.groupingBy(Order::getOrderDate,
                        Collectors.summingInt(Order::getTotalAmount)));

        datesWithTheirSums.forEach((date, totalAmount) ->
                System.out.println("Date: " + date + ", Total Amount: " + totalAmount));
    }

}
