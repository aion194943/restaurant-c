package ro.ase.ppoo.classes;

import java.time.LocalDate;
import java.util.List;

public class Order {
    private String clientName;
    private String address;
    private String idOrder;
    private LocalDate orderDate;
    private int totalAmount;
    String chosenMenu;
    PaymentType type;

    public Order(String clientName, String address, String idOrder, LocalDate orderDate, int totalAmount, String chosenMenu, PaymentType type) {
        this.clientName = clientName;
        this.address = address;
        this.idOrder = idOrder;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.chosenMenu = chosenMenu;
        this.type = type;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(String idOrder) {
        this.idOrder = idOrder;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getMenuList() {
        return chosenMenu;
    }

    public void setMenuList(String menuList) {
        this.chosenMenu = menuList;
    }

    public PaymentType getType() {
        return type;
    }

    public void setType(PaymentType type) {
        this.type = type;
    }


    public String getChosenMenu() {
        return chosenMenu;
    }

    public void setChosenMenu(String chosenMenu) {
        this.chosenMenu = chosenMenu;
    }

    @Override
    public String toString() {
        return "Order{" +
                "clientName='" + clientName + '\'' +
                ", address='" + address + '\'' +
                ", idOrder='" + idOrder + '\'' +
                ", orderDate=" + orderDate +
                ", totalAmount=" + totalAmount +
                ", chosenMenu='" + chosenMenu + '\'' +
                ", type=" + type +
                '}';
    }
}
