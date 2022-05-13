import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Product {
    private String productName; //ima proverka
    private Integer productQuantity;
    private String productDescription;//ima proverka
    private Double productPrice;//ima proverka
    private Double productSellPrice;
    private String productCategory;//ima proverka
    private Integer productID;

    Scanner input = new Scanner(System.in);
    // TODO: 13.2.2022 г.  sell price

    public Product(String productName, Integer productQuantity, String productDescription, Double productPrice, Double productSellPrice, String productCategory) {
        this.setProductName(productName);
        this.setProductQuantity(productQuantity);
        this.setProductDescription(productDescription);
        this.setProductPrice(productPrice);
        this.setProductSellPrice(productSellPrice);
        this.setProductCategory(productCategory);
        AllData.products.add(this);
        setProductID(AllData.products.size());

        DBconnection connection = new DBconnection();
       // connection.productsConnection(this);


    }

    public static boolean
    isValidCheck(String checkString, String regex) {

        Pattern p = Pattern.compile(regex);

        if (checkString == null) {
            return false;
        }

        Matcher m = p.matcher(checkString);

        return m.matches();
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        if (productName.isEmpty()==false) {
            this.productName = productName;
        }else if(productName.isEmpty()==true){
            while (productName.isEmpty()==true){
                System.out.println("Enter new product name:");
                productName = input.nextLine();
            }
            this.productName = productName;
        }
    }

    public Integer getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(Integer productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public Double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
    }

    public Double getProductSalePrice() {
        return productSellPrice;
    }

    public void setProductSellPrice(Double productSellPrice) {
        this.productSellPrice = productSellPrice;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public Integer getProductID() {
        return productID;
    }

    public void setProductID(Integer productID) {
        this.productID = productID;
    }

    @Override
    public String toString() {
        return "Product: "+
                "Product Name: " + productName  +
                ", Product Quantity: " + productQuantity +
                ", Product Description: " + productDescription +
                ", Product Price: " + productPrice +
                ", Category: " + productCategory +
                ", ID: " + productID;
    }


}
