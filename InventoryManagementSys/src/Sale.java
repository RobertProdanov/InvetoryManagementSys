import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Sale {
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    Calendar c = Calendar.getInstance();
    String newDate = sdf.format(c.getTime());
    // String deliveryDate;

    //private String  deliveryDate;
    static ArrayList<Product> products = new ArrayList<Product>();
    static ArrayList<Integer> amountOfProduct = new ArrayList<>();
    private double saleCost;
    String productsList = "";
    String productsAmount = "";
    private int saleID;


    public Sale() {
        setSaleID((AllData.pendingSales.size()));
    }



    public static ArrayList<Product> getProducts() {
        return products;
    }

    public static void setProducts(ArrayList<Product> products) {
        Order.products = products;
    }

    public static ArrayList<Integer> getNumberOfProduct() {
        return amountOfProduct;
    }

    public static void setNumberOfProduct(ArrayList<Integer> numberOfProduct) {
        Order.amountOfProduct = numberOfProduct;
    }

    public void checkDate() {
        System.out.println(newDate);
        c.add(Calendar.DAY_OF_MONTH, 1);
        newDate = sdf.format(c.getTime());
        System.out.println(newDate);
    }

    /*
    public String deliveryDate(int x){
        c.add(Calendar.DAY_OF_MONTH, x);
        deliveryDate = sdf.format(c.getTime());
        return deliveryDate;
    }

    */

     public  String getProductsID(){

        String productsID="";


        for (int i = 0; i < this.products.size(); i++) {
            productsID = productsID.concat( "," +this.products.get(i).getProductID().toString());
        }
        return productsID;
    }
    public String getProductsList(){

        for (int i = 0; i < this.products.size(); i++) {
            productsList = productsList.concat( "," +this.products.get(i).getProductID().toString() );
        }
        return productsList;
    }

    public String getProductsAmount(){

        for (int i = 0; i < this.products.size(); i++) {
            productsAmount = productsAmount.concat( "," +this.amountOfProduct.get(i) );
        }
        return productsAmount;
    }

    public double calculateSaleSum(){
        for (int i =0; i< amountOfProduct.size();i++){
            saleCost += (products.get(i).getProductPrice() * amountOfProduct.get(i));
            System.out.println(saleCost);
        }
        return saleCost;
    }

    public int getSaleID() {
        return saleID;
    }

    public void setSaleID(int saleID) {
        this.saleID = saleID;
    }

    /*
    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }
*/
    public String getNewDate() {
        return newDate;
    }

    public void setNewDate(String newDate) {
        this.newDate = newDate;
    }

    public double getSaleCost() {
        return saleCost;
    }

    public void setSaleCost(double orderCost) {
        this.saleCost = orderCost;
    }

    public void setProductsList(String productsList) {
        this.productsList = productsList;
    }

    public void setProductsAmount(String productsAmount) {
        this.productsAmount = productsAmount;
    }

    @Override
    public String toString() {
        return
                ", Date of creating: " + newDate + '\'' +
                ", Sale cost: " + saleCost +
                ", Product list: " + productsList + '\'' +
                ", Products amount: " + productsAmount + '\'' +
                ", ID: " + saleID;
    }
}




