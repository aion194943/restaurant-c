package ro.ase.ppoo.main;

import ro.ase.ppoo.classes.Order;
import ro.ase.ppoo.classes.PaymentType;
import ro.ase.ppoo.implementation.OrderImplementation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Main {
    private static void MainMenuText() {
        System.out.println("----------Bine ati venit!---------");
        System.out.println("----------Menu---------");
        System.out.println("1. Make an oder!");
        System.out.println("2. View all orders!");
        System.out.println("3. Most frequent purchases ");
        System.out.println("4. Client History");
        System.out.println("5. Total daily revenues");
        System.out.println("0. Close The App");
        System.out.println("   Please choose an option   ");
        System.out.println("-----------------------------");
    }

    private static void MainMenu(OrderImplementation orderImplementation) {
        boolean appRunning = true;

        while (appRunning) {
            MainMenuText();
            Scanner scanner = new Scanner(System.in);
            int chosenOption = scanner.nextInt();

            switch (chosenOption) {
                case 1:
                    orderImplementation.makeAnOrder();
                    orderImplementation.updateDatabase();
                    break;

                case 2:
                    orderImplementation.printOrders();
                    break;

                case 3:
                    orderImplementation.mostFrequentPurchases();
                    break;
                case 4:
                    orderImplementation.clientHistory();
                    break;
                case 5:
                    orderImplementation.dailyRevenues();
                    break;
                case 0:
                    appRunning = false;
                    break;
                default:
                    System.out.println("Wrong number");
            }
        }
    }

    public static void main(String[] args) {

        OrderImplementation orderImplementation = new OrderImplementation();
        orderImplementation.loadOrders();
        MainMenu(orderImplementation);

    }
}
