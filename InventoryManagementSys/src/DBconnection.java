import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBconnection {

    void usersConnection(User user) {
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connect = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/inventorymanagementsystem", "root", "");

            Statement query = connect.createStatement();

            String queryPlayer = "INSERT INTO users(FirstName, LastName,Username, Password, PhoneNumber,LevelOfAccess , ID) VALUES(?,?,?,?,?,?,?)";
            PreparedStatement preparedStatement = connect.prepareStatement(queryPlayer);
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getUserName());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.setString(5, user.getPhoneNumber());
            preparedStatement.setInt(6, user.getLevelOfAccess());
            preparedStatement.setInt(7, user.getuserID());

            preparedStatement.executeUpdate();

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBconnection.class.getName()).log(Level.SEVERE, null, ex);

        } catch (SQLException ex) {
            Logger.getLogger(DBconnection.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }

    void removeUser(Integer id) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connect = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/inventorymanagementsystem", "root", "");

            Statement query = connect.createStatement();
            query.executeUpdate("DELETE FROM users WHERE ID='" + (id) + "'");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBconnection.class.getName()).log(Level.SEVERE, null, ex);


        } catch (SQLException ex) {
            Logger.getLogger(DBconnection.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }

    void userEdit(String table, String property, String edit, int id) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connect = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/inventorymanagementsystem", "root", "");

            Statement query = connect.createStatement();
            query.executeUpdate(" update " + table + " set " + property + "=" + "'" + edit + "'" + "where id =" + id);


        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBconnection.class.getName()).log(Level.SEVERE, null, ex);


        } catch (SQLException ex) {
            Logger.getLogger(DBconnection.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }

    }

    void productsConnection(Product product) {
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connect = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/inventorymanagementsystem", "root", "");

            Statement query = connect.createStatement();


            String queryPlayer = "INSERT INTO products(ProductName, ProductQuantity,ProductDescription, ProductPrice,ProductSalePrice,ProductCategory , ID) VALUES(?,?,?,?,?,?,?)";
            PreparedStatement preparedStatement = connect.prepareStatement(queryPlayer);
            preparedStatement.setString(1, product.getProductName());
            preparedStatement.setInt(2, product.getProductQuantity());
            preparedStatement.setString(3, product.getProductDescription());
            preparedStatement.setDouble(4, product.getProductPrice());
            preparedStatement.setDouble(5, product.getProductSalePrice());
            preparedStatement.setString(6, product.getProductCategory());
            preparedStatement.setInt(7, product.getProductID());

            preparedStatement.executeUpdate();


//statement vs prepare statement

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBconnection.class.getName()).log(Level.SEVERE, null, ex);

        } catch (SQLException ex) {
            Logger.getLogger(DBconnection.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }

    void removeProducts(Integer id) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connect = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/inventorymanagementsystem", "root", "");

            Statement query = connect.createStatement();
            query.executeUpdate("DELETE FROM products WHERE ID='" + (id) + "'");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBconnection.class.getName()).log(Level.SEVERE, null, ex);


        } catch (SQLException ex) {
            Logger.getLogger(DBconnection.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }

    void createOrder(Order order) {
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connect = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/inventorymanagementsystem", "root", "");

            Statement query = connect.createStatement();

            String queryPlayer = "INSERT INTO pendingOrders(DateOfOrder, ProductsID,ProductsAmount, OrderCost,ExpectedDeliveryDate,OrderID) VALUES(?,?,?,?,?,?)";

            PreparedStatement preparedStatement = connect.prepareStatement(queryPlayer);

            preparedStatement.setString(1, order.newDate);
            preparedStatement.setString(2, order.getProductsList());
            preparedStatement.setString(3, order.getProductsAmount());
            preparedStatement.setDouble(4, order.calculateOrderSum());
            preparedStatement.setString(5, order.getDeliveryDate());
            preparedStatement.setInt(6, order.getOrderID());

            preparedStatement.executeUpdate();


        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBconnection.class.getName()).log(Level.SEVERE, null, ex);

        } catch (SQLException ex) {
            Logger.getLogger(DBconnection.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }

    }

    void removeOrderFromPending(Integer id) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connect = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/inventorymanagementsystem", "root", "");

            Statement query = connect.createStatement();
            query.executeUpdate("DELETE FROM pendingOrders WHERE OrderID='" + (id) + "'");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBconnection.class.getName()).log(Level.SEVERE, null, ex);


        } catch (SQLException ex) {
            Logger.getLogger(DBconnection.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }

    void addToCompletedOrders(Order order) {
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connect = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/inventorymanagementsystem", "root", "");

            Statement query = connect.createStatement();

            String queryPlayer = "INSERT INTO completedOrders(DateOfOrder, ProductsID,ProductsAmount, OrderCost,ExpectedDeliveryDate,OrderID) VALUES(?,?,?,?,?,?)";

            PreparedStatement preparedStatement = connect.prepareStatement(queryPlayer);

            preparedStatement.setString(1, order.newDate);
            preparedStatement.setString(2, order.getProductsList());
            preparedStatement.setString(3, order.getProductsAmount());
            preparedStatement.setDouble(4, order.calculateOrderSum());
            preparedStatement.setString(5, order.getDeliveryDate());
            preparedStatement.setInt(6, order.getOrderID());

            preparedStatement.executeUpdate();


        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBconnection.class.getName()).log(Level.SEVERE, null, ex);

        } catch (SQLException ex) {
            Logger.getLogger(DBconnection.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }

    }

    void createSale(Sale sale) {
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connect = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/inventorymanagementsystem", "root", "");

            Statement query = connect.createStatement();

            String queryPlayer = "INSERT INTO pendingSales(DateOfSale, ProductsID,ProductsAmount, SaleCost,SaleID) VALUES(?,?,?,?,?)";

            PreparedStatement preparedStatement = connect.prepareStatement(queryPlayer);

            preparedStatement.setString(1, sale.newDate);
            preparedStatement.setString(2, sale.getProductsList());
            preparedStatement.setString(3, sale.getProductsAmount());
            preparedStatement.setDouble(4, sale.calculateSaleSum());
            preparedStatement.setInt(5, sale.getSaleID());

            preparedStatement.executeUpdate();

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBconnection.class.getName()).log(Level.SEVERE, null, ex);

        } catch (SQLException ex) {
            Logger.getLogger(DBconnection.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }

    }

    void removeSaleFromPending(Integer id) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connect = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/inventorymanagementsystem", "root", "");

            Statement query = connect.createStatement();
            query.executeUpdate("DELETE FROM pendingSales WHERE SaleID='" + (id) + "'");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBconnection.class.getName()).log(Level.SEVERE, null, ex);


        } catch (SQLException ex) {
            Logger.getLogger(DBconnection.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }

    void addToCompletedSales(Sale sale) {
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connect = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/inventorymanagementsystem", "root", "");

            Statement query = connect.createStatement();

            String queryPlayer = "INSERT INTO completedSales(DateOfSale, ProductsID,ProductsAmount, SaleCost,SaleID) VALUES(?,?,?,?,?)";

            PreparedStatement preparedStatement = connect.prepareStatement(queryPlayer);

            preparedStatement.setString(1, sale.newDate);
            preparedStatement.setString(2, sale.getProductsList());
            preparedStatement.setString(3, sale.getProductsAmount());
            preparedStatement.setDouble(4, sale.calculateSaleSum());
            preparedStatement.setInt(5, sale.getSaleID());

            preparedStatement.executeUpdate();

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBconnection.class.getName()).log(Level.SEVERE, null, ex);

        } catch (SQLException ex) {
            Logger.getLogger(DBconnection.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }


    }

    static Integer pullLastID(int tableID, int lastID) {
        try {
            Statement st = null;
            ResultSet rs = null;


            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connect = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/inventorymanagementsystem", "root", "");

            Statement query = connect.createStatement();

            String queryPlayer = "SELECT  `LastID` FROM `lastid` WHERE ID = " + tableID;

            st = connect.createStatement();
            rs = st.executeQuery(queryPlayer);

            while (rs.next()) {
                //System.out.println(rs.getInt("LastID"));
                lastID = (rs.getInt("LastID"));
            }





        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBconnection.class.getName()).log(Level.SEVERE, null, ex);

        } catch (SQLException ex) {
            Logger.getLogger(DBconnection.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }

        return lastID;
    }
}

