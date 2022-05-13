import java.util.ArrayList;

public class AllData {
    static ArrayList<User> users = new ArrayList<User>();
    static ArrayList<Product> products = new ArrayList<Product>();

    static ArrayList<Order> pendingOrders = new ArrayList<Order>();
    static ArrayList<Order> completedOrders = new ArrayList<Order>();

    static ArrayList<Sale> pendingSales= new ArrayList<Sale>();
    static ArrayList<Sale> completedSales= new ArrayList<Sale>();



    public AllData() {
    }

    public AllData(ArrayList<User> users, ArrayList<Product> products, ArrayList<Order> pendingOrders,
                   ArrayList<Order> completedOrders,ArrayList<Sale> pendingSales, ArrayList<Sale> completedSales ) {
        this.users = users;
        this.products = products;
        this.pendingOrders = pendingOrders;
        this.completedOrders = completedOrders;
        this.pendingSales = pendingSales;
        this.completedSales = completedSales;
    }

    public static ArrayList<User> getUsers() {
        return users;
    }

    public static void setUsers(ArrayList<User> users) {
        AllData.users = users;
    }

    public static ArrayList<Product> getProducts() {
        return products;
    }

    public static void setProducts(ArrayList<Product> products) {
        AllData.products = products;
    }

    public static ArrayList<Order> getPendingOrders() {
        return pendingOrders;
    }

    public static void setPendingOrders(ArrayList<Order> pendingOrders) {
        AllData.pendingOrders = pendingOrders;
    }

    public static ArrayList<Order> getCompletedOrders() {
        return completedOrders;
    }

    public static void setCompletedOrders(ArrayList<Order> completedOrders) {
        AllData.completedOrders = completedOrders;
    }
}