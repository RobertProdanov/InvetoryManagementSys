import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class App {

    private static SecretKeySpec secretKey;
    private static byte[] key;

    public static void setKey(final String myKey) {
        MessageDigest sha = null;
        try {
            key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static String encrypt(final String strToEncrypt, final String secret) {
        try {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder()
                    .encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        } catch (Exception e) {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }

    public static String decrypt(final String strToDecrypt, final String secret) {
        try {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder()
                    .decode(strToDecrypt)));
        } catch (Exception e) {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }

    public static void main(String[] args) throws CustomException, InterruptedException, IOException {
        Scanner input = new Scanner(System.in);

        final String secretKey = "ssshhhhhhhhhhh!!!!";
        
        //java -cp D:\InventoryManagementSys\.idea\libraries\mysql-connector-java-8.0.23.jar;. App

        //javac -cp D:\InventoryManagementSys\.idea\libraries\mysql-connector-java-8.0.23.jar;. *java

        DBconnection connection = new DBconnection();

        ArrayList<User> users = new ArrayList<User>();
        ArrayList<Product> products = new ArrayList<Product>();

        ArrayList<Order> pendingOrders = new ArrayList<Order>();
        ArrayList<Order> completedOrders = new ArrayList<Order>();

        ArrayList<Sale> pendingSales = new ArrayList<Sale>();
        ArrayList<Sale> completedSales = new ArrayList<Sale>();

        AllData allData = new AllData(users, products, pendingOrders, completedOrders, pendingSales, completedSales);

        dataSyncUser(users);
        dataSyncProducts(products);
        dataSyncPendingOrders(pendingOrders);
        dataSyncCompeltedOrders(completedOrders);
        dataSyncPendingSales(pendingSales);
        dataSyncCompletedSales(completedSales);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        int menuNumber = 1;
        String menuCheck = "0";
        int currentUserID = 0;

        int page1 = 0;
        int page2 = 0;
        int page3 = 0;
        int page4 = 0;
        int page5 = 0;
        int page6 = 0;

        int accessLevel = 0;

        clearScreen();

        while (true) {
            switch (menuNumber) {

                case 1:
                    boolean loginCheck = false;

                    System.out.println("*******************************************\n*                                         *");
                    System.out.println("* Inventory management system      v1.0.0 *\n*                                         *");
                    System.out.println("*******************************************\n");

                    System.out.println("Login Screen\n");

                    System.out.print("Enter username: ");
                    String userName = input.nextLine();
                    System.out.print("Enter password: ");
                    String password = input.nextLine();

                    for (int i = 0; i < allData.users.size(); i++) {
                        if (allData.users.get(i).getUserName().equals(userName)) {
                            loginCheck = true;
                            if (allData.users.get(i).getPassword().equals(password)) {

                                accessLevel = allData.users.get(i).getLevelOfAccess();
                                currentUserID = allData.users.get(i).getuserID();

                                //След направените синхронизации и проверки, компютъра на потребителя може да се нужда от малка почивка
                                //която е осигурена чрез формата на плацебо проверка
                                Random rand = new Random();
                                int randomNum = rand.nextInt((5 - 2) + 1) + 2;

                                System.out.println("\nChecking if everything is good. Expected time: " + randomNum + " seconds.");
                                TimeUnit.SECONDS.sleep(randomNum);
                                clearScreen();
                                menuNumber = 2;
                                break;
                            } else {
                                loginCheck = false;
                            }
                        } else {
                            loginCheck = false;
                        }
                    }

                    if (loginCheck == false) {
                        System.out.println("\nWrong login info!");
                        TimeUnit.SECONDS.sleep(2);
                        clearScreen();
                        menuNumber = 1;
                    }
                    break;

                case 2:
                    System.out.println("Main menu\n");
                    System.out.println("1. Users" + "\n" + "2. Products" + "\n" + "3. Orders" + "\n" + "4. Sells" + "\n" + "5. Log out");
                    System.out.println("");
                    System.out.print("Choose option: ");

                    menuCheck = input.nextLine();

                    if (isNumeric(menuCheck) == true) {
                        menuNumber = Integer.parseInt(menuCheck);

                        if (menuNumber == 1) {
                            clearScreen();
                            menuNumber = 3;
                            break;
                        } else if (menuNumber == 2) {
                            clearScreen();
                            menuNumber = 9;
                            break;
                        } else if (menuNumber == 3) {
                            clearScreen();
                            menuNumber = 15;
                            break;
                        } else if (menuNumber == 4) {
                            clearScreen();
                            menuNumber = 21;
                            break;
                        } else if (menuNumber == 5) {
                            clearScreen();
                            menuNumber = 1;
                        } else {
                            System.out.println("\nInvalid option!");
                            TimeUnit.SECONDS.sleep(2);
                            clearScreen();
                            menuNumber = 2;
                            break;
                        }
                    } else if (menuCheck.isEmpty() == true) {
                        System.out.println("\nInvalid input!");
                        TimeUnit.SECONDS.sleep(2);
                        clearScreen();
                        menuNumber = 2;
                        break;
                    } else {
                        System.out.println("\nInvalid input!");
                        TimeUnit.SECONDS.sleep(2);
                        clearScreen();
                        menuNumber = 2;
                        break;
                    }
                    break;

                case 3:
                    accessLevel = 3;
                    System.out.println("User screen\n");
                    System.out.println("1. Manage Users" + "\n" + "2. Add users" + "\n" + "3. Remove users" + "\n" + "4. Edit users" + "\n" + "5. Help" + "\n" + "6. Back to main menu");
                    System.out.println("");
                    System.out.print("Choose option: ");

                    menuCheck = input.nextLine();

                    if (isNumeric(menuCheck) == true) {
                        // System.out.println(menuCheck);
                        menuNumber = Integer.parseInt(menuCheck);
                        if (menuNumber == 1) {
                            clearScreen();
                            menuNumber = 4;
                            break;

                            //dobavqne na potrebitel
                        } else if (menuNumber == 2 && accessLevel >= 2) {
                            clearScreen();
                            menuNumber = 5;
                            break;
                        } else if (menuNumber == 2 && accessLevel < 2) {
                            System.out.println("\nYou don't have permission to add users!");
                            TimeUnit.SECONDS.sleep(3);
                            clearScreen();
                            menuNumber = 3;
                            break;
                        }

                        //premahvene na potrebitel
                        else if (menuNumber == 3 && accessLevel == 3) {
                            clearScreen();
                            menuNumber = 6;
                            break;
                        } else if (menuNumber == 3 && accessLevel < 3) {
                            System.out.println("\nYou don't have permission to remove users!");
                            TimeUnit.SECONDS.sleep(3);
                            clearScreen();
                            menuNumber = 3;
                            break;
                        }

                        //edit na potrebitel
                        else if (menuNumber == 4 && accessLevel == 3) {
                            clearScreen();
                            menuNumber = 7;
                            break;
                        } else if (menuNumber == 4 && accessLevel < 3) {
                            System.out.println("\nYou don't have permission to edit users!");
                            TimeUnit.SECONDS.sleep(3);
                            clearScreen();
                            menuNumber = 3;
                            break;
                        }

                        //help
                        else if (menuNumber == 5) {
                            clearScreen();
                            menuNumber = 8;
                            break;
                            //back
                        } else if (menuNumber == 6) {
                            clearScreen();
                            menuNumber = 2;
                            break;
                        } else {
                            System.out.println("\nInvalid option!");
                            TimeUnit.SECONDS.sleep(2);
                            clearScreen();
                            menuNumber = 3;
                            break;
                        }
                    } else if (isNumeric(menuCheck) == false) {
                        System.out.println("\nInvalid input!");
                        TimeUnit.SECONDS.sleep(2);
                        clearScreen();
                        menuNumber = 3;
                        break;
                    }

                case 4:
                    //manage users
                    System.out.println("All registered users \n");

                    Double amountOfPages = users.size() / 3.0;

                    amountOfPages = Math.ceil(amountOfPages);

                    System.out.printf("Amount of pages: " + String.format("%.0f", amountOfPages) + "\n" + "\n");

                    if (amountOfPages == 0) {
                        System.out.println("Nothing to show\n");
                        System.out.print("1. Back\n\n");
                        System.out.print("Choose option: ");
                        menuCheck = input.nextLine();

                        if (isNumeric(menuCheck) == true) {
                            menuNumber = Integer.parseInt(menuCheck);
                            if (menuNumber == 1) {
                                page1 = 0;
                                clearScreen();
                                menuNumber = 3;
                                break;
                            } else {
                                System.out.println("\nInvalid option");
                                TimeUnit.SECONDS.sleep(2);
                                clearScreen();
                                menuNumber = 4;
                                break;
                            }
                        } else if (isNumeric(menuCheck) == false) {
                            System.out.println("\nInvalid input");
                            TimeUnit.SECONDS.sleep(2);
                            clearScreen();
                            menuNumber = 4;
                            break;
                        }

                    } else {

                        int chunkSize = 3;
                        AtomicInteger counter = new AtomicInteger();
                        final Collection<List<User>> partitionedList =
                                users.stream().collect(Collectors.groupingBy(i -> counter.getAndIncrement() / chunkSize))
                                        .values();

                        for (int i = 0; i < partitionedList.stream().skip(page1).findFirst().orElse(null).size(); i++) {
                            System.out.println(partitionedList.stream().skip(page1).findFirst().orElse(null).get(i));
                        }

                        if (amountOfPages == 1) {

                            System.out.print("\n1. Back\n\n");
                            System.out.print("Choose option: ");
                            menuCheck = input.nextLine();


                            if (isNumeric(menuCheck) == true) {
                                menuNumber = Integer.parseInt(menuCheck);
                                if (menuNumber == 1) {
                                    page1 = 0;
                                    clearScreen();
                                    menuNumber = 3;
                                    break;
                                } else {
                                    System.out.println("\nInvalid option");
                                    TimeUnit.SECONDS.sleep(2);
                                    clearScreen();
                                    menuNumber = 4;
                                    break;
                                }
                            } else if (isNumeric(menuCheck) == false) {
                                System.out.println("\nInvalid input");
                                TimeUnit.SECONDS.sleep(2);
                                clearScreen();
                                menuNumber = 4;
                                break;

                            }
                        }

                        if (page1 == 0) {

                            System.out.print("\n1. Back 2. Next Page\n\n");
                            System.out.print("Choose option: ");
                            menuCheck = input.nextLine();

                            if (isNumeric(menuCheck) == true) {
                                menuNumber = Integer.parseInt(menuCheck);
                                if (menuNumber == 1) {
                                    page1 = 0;
                                    clearScreen();
                                    menuNumber = 3;
                                    break;
                                } else if (menuNumber == 2) {
                                    clearScreen();
                                    menuNumber = 4;
                                    page1 += 1;
                                    break;
                                } else {
                                    System.out.println("\nInvalid option");
                                    TimeUnit.SECONDS.sleep(2);
                                    clearScreen();
                                    menuNumber = 4;
                                    break;
                                }
                            } else if (isNumeric(menuCheck) == false) {
                                System.out.println("\nInvalid input");
                                TimeUnit.SECONDS.sleep(2);
                                clearScreen();
                                menuNumber = 4;
                                break;
                            }

                        } else if (page1 > 0 && page1 < amountOfPages - 1) {

                            System.out.print("\n1. Back 2. Next Page 3. Back Page\n\n");
                            System.out.print("Choose option: ");
                            menuCheck = input.nextLine();

                            if (isNumeric(menuCheck) == true) {
                                menuNumber = Integer.parseInt(menuCheck);
                                if (menuNumber == 1) {
                                    page1 = 0;
                                    clearScreen();
                                    menuNumber = 3;
                                    break;
                                } else if (menuNumber == 2) {
                                    page1 += 1;
                                    clearScreen();
                                    menuNumber = 4;
                                    break;
                                } else if (menuNumber == 3) {
                                    page1 -= 1;
                                    clearScreen();
                                    menuNumber = 4;
                                    break;
                                } else {
                                    System.out.println("\nInvalid option");
                                    TimeUnit.SECONDS.sleep(2);
                                    clearScreen();
                                    menuNumber = 4;
                                    break;
                                }
                            } else if (isNumeric(menuCheck) == false) {
                                System.out.println("\nInvalid input");
                                TimeUnit.SECONDS.sleep(2);
                                clearScreen();
                                menuNumber = 4;
                                break;
                            } else {
                                break;
                            }

                        } else if (page1 == amountOfPages - 1) {

                            System.out.print("\n1. Back 2. Back Page\n\n");
                            System.out.print("Choose option: ");
                            menuCheck = input.nextLine();

                            if (isNumeric(menuCheck) == true) {
                                menuNumber = Integer.parseInt(menuCheck);
                                if (menuNumber == 1) {
                                    page1 = 0;
                                    clearScreen();
                                    menuNumber = 3;
                                    break;
                                } else if (menuNumber == 2) {
                                    page1 -= 1;
                                    clearScreen();
                                    menuNumber = 4;
                                    break;
                                } else {
                                    System.out.println("\nInvalid option");
                                    TimeUnit.SECONDS.sleep(2);
                                    clearScreen();
                                    menuNumber = 4;
                                    break;
                                }
                            } else if (isNumeric(menuCheck) == false) {
                                System.out.println("\nInvalid input");
                                TimeUnit.SECONDS.sleep(2);
                                clearScreen();
                                menuNumber = 4;
                                break;
                            }
                        }
                    }
                    break;

                case 5:
                    //add users
                    //clearScreen();
                    int addUserMenuOption;
                    boolean accessLevelCorrect = false;
                    System.out.println("Adding users menu\n");
                    System.out.println("1. Continue 2. Back\n");
                    System.out.print("Choose option: ");

                    menuCheck = input.nextLine();

                    if (isNumeric(menuCheck) == true) {
                        addUserMenuOption = Integer.parseInt(menuCheck);

                        int levelOfAccess = 0;

                        if (addUserMenuOption == 1) {
                            clearScreen();

                            System.out.print("First name:");
                            String firstName = input.nextLine();
                            System.out.println(" ");

                            System.out.print("Second name:");
                            String secondName = input.nextLine();
                            System.out.println(" ");

                            System.out.print("Username:");
                            String username = input.nextLine();
                            System.out.println(" ");

                            System.out.print("Password:");
                            String password1 = input.nextLine();
                            System.out.println(" ");

                            System.out.print("Phone number:");
                            String phoneNumber = input.nextLine();
                            System.out.println(" ");

                            while (accessLevelCorrect == false) {
                                //System.out.println();
                                System.out.print("Level of access: ");
                                String levelOfAccessCheck = input.nextLine();

                                if (isNumeric(levelOfAccessCheck)) {
                                    levelOfAccess = Integer.parseInt(levelOfAccessCheck);

                                    if (levelOfAccess < 1 || levelOfAccess > 3) {
                                        System.out.println("Incorrect level of access\n");
                                        accessLevelCorrect = false;
                                    } else {
                                        accessLevelCorrect = true;
                                    }

                                } else {
                                    System.out.println("Incorrect level of access");
                                    accessLevelCorrect = false;
                                }
                            }

                            User b = new User (firstName ,secondName,username,password1, phoneNumber, levelOfAccess);

                            int userID=0;
                            userID = connection.pullLastID(1,userID)+1;
                            b.setuserID(userID);

                            connection.usersConnection(b);

                            connection.userEdit("lastID", "LastID", String.valueOf(userID),1);



                            connection.userEdit("users", "FirstName",encrypt(firstName,secretKey) ,b.getuserID());
                            connection.userEdit("users", "LastName",encrypt(secondName,secretKey) ,b.getuserID());
                            connection.userEdit("users", "Username",encrypt(username,secretKey) ,b.getuserID());
                            connection.userEdit("users", "Password",encrypt(password1,secretKey) ,b.getuserID());
                            connection.userEdit("users", "PhoneNumber",encrypt(phoneNumber,secretKey) ,b.getuserID());

                            Calendar c = Calendar.getInstance();
                            String userAddingDate = sdf.format(c.getTime());

                            String info = AllData.users.get(users.size() - 1).toString() + " Date of adding: " + userAddingDate +
                                    " Added by user with id: " + currentUserID + "\n";
                            printToFile("D:\\InventoryManagementSys\\textfile/AddUserLogFile.txt", info);

                            System.out.println("\nNew user was added!");
                            TimeUnit.SECONDS.sleep(2);
                            clearScreen();
                            menuNumber = 5;
                            break;
                        } else if (addUserMenuOption == 2) {
                            clearScreen();
                            menuNumber = 3;
                            break;
                        } else {
                            System.out.println("\nNot valid option!");
                            TimeUnit.SECONDS.sleep(2);
                            clearScreen();
                            menuNumber = 5;
                            break;
                        }
                    } else if (isNumeric(menuCheck) == false) {
                        System.out.println("\nInvalid input");
                        TimeUnit.SECONDS.sleep(2);
                        clearScreen();
                        menuNumber = 5;
                        break;
                    }

                case 6:
                    //remove user

                    menuCheck = "";
                    boolean removeUserResult = false;

                    System.out.println("Removing users menu\n");
                    System.out.println("1. Continue 2. Back\n");
                    System.out.print("Choose option: ");
                    int removeUserMenuOption;

                    menuCheck = input.nextLine();


                    if (isNumeric(menuCheck) == true) {
                        removeUserMenuOption = Integer.parseInt(menuCheck);

                        if (removeUserMenuOption == 1) {

                            clearScreen();

                            System.out.println("Enter the user ID to remove");
                            System.out.println();
                            int removeUserId;
                            System.out.print("ID: ");
                            menuCheck = input.nextLine();


                            if (isNumeric(menuCheck) == true) {

                                removeUserId = Integer.parseInt(menuCheck);

                                for (int i = 0; i < users.size(); i++) {
                                    if (removeUserId == users.get(i).getuserID()) {

                                        Calendar c = Calendar.getInstance();
                                        String userRemoveDate = sdf.format(c.getTime());

                                        String info = users.get(i) + " Date of removing: " + userRemoveDate +
                                                " Removed by user with id: " + currentUserID + "\n";
                                        printToFile("D:\\InventoryManagementSys\\textfile/RemoveUserLogFile.txt", info);

                                        users.remove(i);
                                        connection.removeUser(removeUserId);
                                        removeUserResult = true;
                                        break;
                                    }
                                    if (removeUserId != users.get(i).getuserID()) {
                                        removeUserResult = false;

                                    }

                                    menuNumber = 6;

                                }
                                if (removeUserResult == true) {
                                    System.out.println("\nUser was removed");
                                    TimeUnit.SECONDS.sleep(2);
                                    clearScreen();
                                    menuNumber = 6;
                                } else if (removeUserResult == false) {
                                    System.out.println("\nThis user does not exist or is already removed!");
                                    TimeUnit.SECONDS.sleep(2);
                                    clearScreen();
                                    menuNumber = 6;
                                }
                            } else if (isNumeric(menuCheck) == false) {
                                System.out.println("\nInvalid input");
                                TimeUnit.SECONDS.sleep(2);
                                clearScreen();
                                menuNumber = 6;
                                break;
                            }
                            break;
                        } else if (removeUserMenuOption == 2) {
                            clearScreen();
                            menuNumber = 3;
                        } else {
                            System.out.println("\nInvalid option");
                            TimeUnit.SECONDS.sleep(2);
                            clearScreen();
                            menuNumber = 6;
                            break;
                        }
                    } else if (isNumeric(menuCheck) == false) {
                        System.out.println("\nInvalid input");
                        TimeUnit.SECONDS.sleep(2);
                        clearScreen();
                        menuNumber = 6;
                        break;
                    }
                    break;

                case 7:
                    //edit users
                    int editUserOperation = 0;
                    int editID = 0;
                    boolean editUsers = true;
                    int editUserMenuOption = 0;
                    int userIndex = 0;


                    while (editUsers == true) {
                        switch (editUserMenuOption) {

                            case 0:
                                System.out.println("Edit users menu\n");
                                System.out.println("1. Continue 2. Back\n");
                                System.out.print("Choose option: ");


                                menuCheck = input.nextLine();

                                if (isNumeric(menuCheck) == true) {
                                    editUserMenuOption = Integer.parseInt(menuCheck);
                                    if ((editUserMenuOption == 1)) {
                                        clearScreen();
                                        editUserMenuOption = 1;
                                        break;
                                    } else if (editUserMenuOption == 2) {
                                        clearScreen();
                                        menuNumber = 3;
                                        editUsers = false;
                                        break;
                                    } else {
                                        System.out.println("\nInvalid option");
                                        TimeUnit.SECONDS.sleep(2);
                                        clearScreen();
                                        editUserMenuOption =0;
                                        menuNumber = 7;
                                        break;
                                    }
                                } else if (isNumeric(menuCheck) == false) {
                                    System.out.println("\nInvalid input");
                                    TimeUnit.SECONDS.sleep(2);
                                    clearScreen();
                                    menuNumber = 7;
                                    break;
                                }

                                break;


                            case 1:
                                boolean validID = false;
                                System.out.println("Edit users menu\n");
                                System.out.print("Type in the user ID you want to edit: ");

                                menuCheck = input.nextLine();

                                if (isNumeric(menuCheck) == true) {
                                    editID = Integer.parseInt(menuCheck);
                                } else if (isNumeric(menuCheck) == false) {
                                    System.out.println("\nInvalid input");
                                    TimeUnit.SECONDS.sleep(2);
                                    clearScreen();
                                    editUserMenuOption = 0;
                                    break;
                                }


                                for (int i = 0; i < users.size(); i++) {
                                    if (users.get(i).getuserID() == editID) {
                                        clearScreen();
                                        editUserMenuOption = 2;
                                        validID = true;
                                        userIndex = i;
                                        break;
                                    } else if (users.get(i).getuserID() != editID) {
                                        editUserMenuOption = 0;
                                        validID = false;

                                    }
                                }

                                if (validID == false) {
                                    System.out.println("\nThis ID does not exist!");
                                    TimeUnit.SECONDS.sleep(2);
                                    clearScreen();
                                    menuNumber = 7;
                                }
                                break;


                            case 2:
                                System.out.println("Current editing ID: " + editID + "\n");
                                System.out.println("Select want you want to edit : 1.First name 2.Last name 3.Username 4.Password 5.Phone number 6.Access level 7. Back\n");
                                System.out.print("Choose option: ");

                                menuCheck = input.nextLine();

                                if (isNumeric(menuCheck) == true) {
                                    editUserOperation = Integer.parseInt(menuCheck);
                                } else if (isNumeric(menuCheck) == false) {
                                    System.out.println("\nInvalid input");
                                    TimeUnit.SECONDS.sleep(2);
                                    clearScreen();
                                    editUserMenuOption = 2;
                                    break;
                                }


                                if (editUserOperation == 1) {
                                    clearScreen();
                                    editUserMenuOption = 3;
                                    break;
                                } else if (editUserOperation == 2) {
                                    clearScreen();
                                    editUserMenuOption = 4;
                                    break;
                                } else if (editUserOperation == 3) {
                                    clearScreen();
                                    editUserMenuOption = 5;
                                    break;
                                } else if (editUserOperation == 4) {
                                    clearScreen();
                                    editUserMenuOption = 6;
                                    break;
                                } else if (editUserOperation == 5) {
                                    clearScreen();
                                    editUserMenuOption = 7;
                                    break;
                                } else if (editUserOperation == 6) {
                                    clearScreen();
                                    editUserMenuOption = 8;
                                    break;
                                } else if (editUserOperation == 7) {
                                    clearScreen();
                                    editUserMenuOption = 9;
                                    break;
                                }else {
                                    System.out.println("\nInvalid option");
                                    TimeUnit.SECONDS.sleep(2);
                                    clearScreen();
                                    editUserMenuOption = 2;
                                    break;
                                }

                            case 3:
                                //edit first name
                                String oldFirstName = users.get(userIndex).getFirstName();

                                System.out.println("Current first name: " + users.get(userIndex).getFirstName() + "\n ");
                                System.out.print("Enter new first name: ");

                                //input.nextLine();
                                String newFirstName = input.nextLine();

                                Calendar c = Calendar.getInstance();
                                String userEditDate = sdf.format(c.getTime());


                                users.get(userIndex).setFirstName(newFirstName);
                                connection.userEdit("users", "FirstName", encrypt(users.get(userIndex).getFirstName(),secretKey), editID);

                                //dobavqne v log file-a
                                String info = users.get(userIndex) + " Date of editing: " + userEditDate +
                                        " Edited by user with id: " + currentUserID + " Change made - Changed first name - Old first name: " +
                                        oldFirstName + " || New first name:" + users.get(userIndex).getFirstName() + "\n";
                                printToFile("D:\\InventoryManagementSys\\textfile/EditUserLogFile.txt", info);

                                clearScreen();

                                editUserMenuOption = 2;
                                break;


                            case 4:
                                //edit last name

                                String oldLastName = users.get(userIndex).getLastName();

                                System.out.println("Current last name: " + users.get(userIndex).getLastName() + "\n");
                                System.out.print("Enter new last name: ");

                                //input.nextLine();
                                String newLastName = input.nextLine();

                                c = Calendar.getInstance();
                                userEditDate = sdf.format(c.getTime());

                                users.get(userIndex).setLastName(newLastName);

                                connection.userEdit("users", "LastName", encrypt(users.get(userIndex).getLastName(), secretKey), editID);

                                //dobavqne v log file-a
                                info = users.get(userIndex) + " Date of editing: " + userEditDate +
                                        " Edited by user with id: " + currentUserID + " Change made - Changed last name - Old last name: " +
                                        oldLastName + " || New last name:" + users.get(userIndex).getLastName() + "\n";
                                printToFile("D:\\InventoryManagementSys\\textfile/EditUserLogFile.txt", info);

                                clearScreen();
                                editUserMenuOption = 2;
                                break;

                            case 5:
                                //edit username

                                String oldUsername = users.get(userIndex).getUserName();

                                System.out.println("Current username: " + users.get(userIndex).getUserName() + "\n");
                                System.out.print("Enter new username: ");

                                //input.nextLine();
                                String newUsername = input.nextLine();

                                c = Calendar.getInstance();
                                userEditDate = sdf.format(c.getTime());

                                users.get(userIndex).setUserName(newUsername);
                                connection.userEdit("users", "Username",encrypt(users.get(userIndex).getUserName(), secretKey), editID);

                                //dobavqne v log file-a
                                info = users.get(userIndex) + " Date of editing: " + userEditDate +
                                        " Edited by user with id: " + currentUserID + " Change made - Changed username - Old username: " +
                                        oldUsername + " || New username:" + users.get(userIndex).getUserName() + "\n";
                                printToFile("D:\\InventoryManagementSys\\textfile/EditUserLogFile.txt", info);

                                clearScreen();
                                editUserMenuOption = 2;

                                break;
                            case 6:
                                //edit password

                                String oldPassword = users.get(userIndex).getPassword();
                                System.out.println("Current password: " + users.get(userIndex).getPassword() + "\n");
                                System.out.print("Enter new password: ");

                                //input.nextLine();
                                String newPassword = input.nextLine();

                                c = Calendar.getInstance();
                                userEditDate = sdf.format(c.getTime());

                                users.get(userIndex).setPassword(newPassword);
                                connection.userEdit("users", "Password", encrypt(users.get(userIndex).getPassword(),secretKey), editID);


                                //dobavqne v log file-a
                                info = users.get(userIndex) + " Date of editing: " + userEditDate +
                                        " Edited by user with id: " + currentUserID + " Change made - Changed password - Old password: " +
                                        oldPassword + " || New password:" + users.get(userIndex).getPassword() + "\n";
                                printToFile("D:\\InventoryManagementSys\\textfile/EditUserLogFile.txt", info);

                                clearScreen();
                                editUserMenuOption = 2;

                                break;

                            case 7:
                                //edit phone number
                                String oldPhoneNumber = users.get(userIndex).getPhoneNumber();
                                System.out.println("Current phone number:  " + users.get(userIndex).getPhoneNumber() + "\n");
                                System.out.print("Enter new phone number: ");

                                //input.nextLine();
                                String newPhoneNumber = input.nextLine();

                                c = Calendar.getInstance();
                                userEditDate = sdf.format(c.getTime());

                                users.get(userIndex).setPhoneNumber(newPhoneNumber);
                                connection.userEdit("users", "PhoneNumber", encrypt(users.get(userIndex).getPhoneNumber(),secretKey), editID);

                                //dobavqne v log file-a
                                info = users.get(userIndex) + " Date of editing: " + userEditDate +
                                        " Edited by user with id: " + currentUserID + " Change made - Changed phone number - Old phone number: " +
                                        oldPhoneNumber + " || New phone number:" + users.get(userIndex).getPhoneNumber() + "\n";
                                printToFile("D:\\InventoryManagementSys\\textfile/EditUserLogFile.txt", info);

                                clearScreen();
                                editUserMenuOption = 2;
                                break;

                            case 8:
                                //edit access level
                                int newAccessLevel;

                                System.out.println("Current access level:  " + users.get(userIndex).getLevelOfAccess() + "\n");
                                System.out.print("Enter new access level: ");


                                menuCheck = input.nextLine();


                                if (isNumeric(menuCheck) == true) {
                                    String oldAccessLevel = String.valueOf(users.get(userIndex).getLevelOfAccess());

                                    newAccessLevel = Integer.parseInt(menuCheck);
                                    String newAccessLevel2 = String.valueOf(newAccessLevel);

                                    c = Calendar.getInstance();
                                    userEditDate = sdf.format(c.getTime());

                                    users.get(userIndex).setLevelOfAccess(newAccessLevel);
                                    connection.userEdit("users", "LevelOfAccess", newAccessLevel2, editID);

                                    //dobavqne v log file-a
                                    info = users.get(userIndex) + " Date of editing: " + userEditDate +
                                            " Edited by user with id: " + currentUserID + " Change made - Changed access level - Old access level: " +
                                            oldAccessLevel + " || New access level:" + users.get(userIndex).getLevelOfAccess() + "\n";
                                    printToFile("D:\\InventoryManagementSys\\textfile/EditUserLogFile.txt", info);

                                    clearScreen();
                                    editUserMenuOption = 2;

                                    break;
                                } else {
                                    System.out.println("Incorrect access level, the value should be number!");
                                    clearScreen();
                                    editUserMenuOption = 8;
                                    break;
                                }
                            case 9:
                                editUsers = false;
                                menuNumber = 7;
                                break;
                        }
                    }
                    break;

                case 8:
                    //help screen
                    int helpMenuOption;

                    System.out.println("Help menu\n");
                    System.out.println("In the user menu you can see all registered users in the system, add new users," +
                            "\nedit or remove the already existing ones depending of your access level. \n");
                    System.out.println("1. Back \n");
                    System.out.print("Choose option: ");

                    menuCheck = input.nextLine();

                    if (isNumeric(menuCheck) == true) {
                        helpMenuOption = Integer.parseInt(menuCheck);

                        if (helpMenuOption == 1) {
                            clearScreen();
                            menuNumber = 3;
                            break;
                        } else {
                            System.out.println("\nIncorrect option");
                            TimeUnit.SECONDS.sleep(2);
                            clearScreen();
                            menuNumber = 8;
                            break;
                        }
                    } else if (isNumeric(menuCheck) == false) {
                        System.out.println("\nIncorrect input");
                        TimeUnit.SECONDS.sleep(2);
                        clearScreen();
                        menuNumber = 8;
                        break;
                    }


                case 9:

                    accessLevel = 3;
                    System.out.println("Product screen\n");
                    System.out.println("1. Manage products" + "\n" + "2. Add products" + "\n" + "3. Remove products" + "\n" + "4. Edit products" + "\n" + "5. Help" + "\n" + "6. Back to main menu");
                    System.out.println("");
                    System.out.print("Choose option: ");

                    menuCheck = input.nextLine();


                    if (isNumeric(menuCheck) == true) {
                        menuNumber = Integer.parseInt(menuCheck);

                        //Manage products screen
                        if (menuNumber == 1 && accessLevel >= 2) {
                            clearScreen();
                            menuNumber = 10;
                            break;
                        } else if (menuNumber == 1 && accessLevel < 2) {
                            System.out.println("\nYou dont have the permission to see the list of products!");
                            TimeUnit.SECONDS.sleep(2);
                            clearScreen();
                            menuNumber = 9;
                            break;

                            //Add products screen
                        } else if (menuNumber == 2 && accessLevel >= 2) {
                            clearScreen();
                            menuNumber = 11;
                            break;
                        } else if (menuNumber == 2 && accessLevel < 2) {
                            System.out.println("\nYou dont have the permission to add products!");
                            TimeUnit.SECONDS.sleep(2);
                            clearScreen();
                            menuNumber = 9;
                            break;
                        }

                        //Remove users Screen
                        else if (menuNumber == 3 && accessLevel > 2) {
                            clearScreen();
                            menuNumber = 12;
                            break;
                        } else if (menuNumber == 3 && accessLevel < 3) {
                            System.out.println("\nYou dont have the permission to remove products!");
                            TimeUnit.SECONDS.sleep(2);
                            clearScreen();
                            menuNumber = 9;
                            break;
                        }

                        //Edit user screen
                        else if (menuNumber == 4 && accessLevel > 2) {
                            clearScreen();
                            menuNumber = 13;
                            break;
                        } else if (menuNumber == 4 && accessLevel < 3) {
                            System.out.println("\nYou dont have the permission to edit products!");
                            TimeUnit.SECONDS.sleep(2);
                            clearScreen();
                            menuNumber = 9;
                            break;

                            //help menu
                        } else if (menuNumber == 5) {
                            clearScreen();
                            menuNumber = 14;
                            break;

                            //exit
                        } else if (menuNumber == 6) {
                            clearScreen();
                            menuNumber = 2;
                            break;
                        } else {
                            System.out.println("\nInvalid option!");
                            TimeUnit.SECONDS.sleep(2);
                            clearScreen();
                            menuNumber = 9;
                            break;
                        }

                    } else if (isNumeric(menuCheck) == false) {
                        System.out.println("\nInvalid input!");
                        TimeUnit.SECONDS.sleep(2);
                        clearScreen();
                        menuNumber = 9;
                        break;
                    }
                    break;

                case 10:
                    //manage product

                    System.out.println("All products\n");

                    Double amountOfPages2 = products.size() / 3.0;


                    amountOfPages2 = Math.ceil(amountOfPages2);

                    System.out.printf("Amount of pages: " + String.format("%.0f", amountOfPages2) + "\n" + "\n");


                    if (amountOfPages2 == 0) {
                        System.out.println("Nothing to show\n");
                        System.out.print("1. Back\n\n");
                        System.out.print("Choose option: ");
                        menuCheck = input.nextLine();


                        if (menuCheck.isEmpty() == true) {
                            menuCheck = input.nextLine();
                        }

                        if (isNumeric(menuCheck) == true) {
                            menuNumber = Integer.parseInt(menuCheck);
                            if (menuNumber == 1) {
                                page2 = 0;
                                clearScreen();
                                menuNumber = 9;
                                break;
                            } else {
                                System.out.println("\nInvalid option");
                                TimeUnit.SECONDS.sleep(2);
                                clearScreen();
                                menuNumber = 10;
                                break;
                            }
                        } else if (isNumeric(menuCheck) == false) {
                            System.out.println("\nInvalid input");
                            TimeUnit.SECONDS.sleep(2);
                            clearScreen();
                            menuNumber = 10;
                            break;
                        }
                    } else {


                        int chunkSize2 = 3;
                        AtomicInteger counter2 = new AtomicInteger();
                        final Collection<List<Product>> partitionedList2 =
                                products.stream().collect(Collectors.groupingBy(i -> counter2.getAndIncrement() / chunkSize2))
                                        .values();

                        for (int i = 0; i < partitionedList2.stream().skip(page2).findFirst().orElse(null).size(); i++) {
                            System.out.println(partitionedList2.stream().skip(page2).findFirst().orElse(null).get(i));
                        }

                        if (amountOfPages2 == 1) {


                            System.out.print("\n1. Back\n\n");
                            System.out.print("Choose option: ");
                            menuCheck = input.nextLine();


                            if (isNumeric(menuCheck) == true) {
                                menuNumber = Integer.parseInt(menuCheck);
                                if (menuNumber == 1) {
                                    page2 = 0;
                                    clearScreen();
                                    menuNumber = 9;
                                    break;
                                } else {
                                    System.out.println("\nInvalid option");
                                    TimeUnit.SECONDS.sleep(2);
                                    clearScreen();
                                    menuNumber = 10;
                                    break;
                                }
                            } else if (isNumeric(menuCheck) == false) {
                                System.out.println("\nInvalid input");
                                TimeUnit.SECONDS.sleep(2);
                                clearScreen();
                                menuNumber = 10;
                                break;
                            }
                        }

                        if (page2 == 0) {

                            System.out.print("\n1. Back 2. Next Page\n\n");
                            System.out.print("Choose option: ");
                            menuCheck = input.nextLine();


                            if (isNumeric(menuCheck) == true) {
                                menuNumber = Integer.parseInt(menuCheck);
                                if (menuNumber == 1) {
                                    page2 = 0;
                                    clearScreen();
                                    menuNumber = 9;
                                    break;
                                } else if (menuNumber == 2) {
                                    clearScreen();
                                    menuNumber = 10;
                                    page2 += 1;
                                    break;
                                } else {
                                    System.out.println("\nInvalid option");
                                    TimeUnit.SECONDS.sleep(2);
                                    clearScreen();
                                    menuNumber = 10;
                                    break;
                                }
                            } else if (isNumeric(menuCheck) == false) {
                                System.out.println("\nInvalid input");
                                TimeUnit.SECONDS.sleep(2);
                                clearScreen();
                                menuNumber = 10;
                                break;
                            }

                        } else if (page2 > 0 && page2 < amountOfPages2 - 1) {


                            System.out.print("\n1. Back 2. Next Page 3. Back Page\n\n");
                            System.out.print("Choose option: ");
                            menuCheck = input.nextLine();


                            if (menuCheck.isEmpty() == true) {
                                menuCheck = input.nextLine();
                            }

                            if (isNumeric(menuCheck) == true) {
                                menuNumber = Integer.parseInt(menuCheck);
                                if (menuNumber == 1) {
                                    page2 = 0;
                                    clearScreen();
                                    menuNumber = 9;
                                    break;
                                } else if (menuNumber == 2) {
                                    page2 += 1;
                                    clearScreen();
                                    menuNumber = 10;
                                    break;
                                } else if (menuNumber == 3) {
                                    page2 -= 1;
                                    clearScreen();
                                    menuNumber = 10;
                                    break;
                                } else {
                                    System.out.println("\nInvalid option");
                                    TimeUnit.SECONDS.sleep(2);
                                    clearScreen();
                                    menuNumber = 10;
                                    break;
                                }
                            } else if (isNumeric(menuCheck) == false) {
                                System.out.println("\nInvalid input");
                                TimeUnit.SECONDS.sleep(2);
                                clearScreen();
                                menuNumber = 10;
                                break;
                            }

                        } else if (page2 == amountOfPages2 - 1) {

                            System.out.print("\n1. Back 2. Back Page\n\n");
                            System.out.print("Choose option: ");
                            menuCheck = input.nextLine();


                            if (isNumeric(menuCheck) == true) {
                                menuNumber = Integer.parseInt(menuCheck);
                                if (menuNumber == 1) {
                                    page2 = 0;
                                    clearScreen();
                                    menuNumber = 9;
                                    break;
                                } else if (menuNumber == 2) {
                                    page2 -= 1;
                                    clearScreen();
                                    menuNumber = 10;
                                    break;
                                } else {
                                    System.out.println("\nInvalid option");
                                    TimeUnit.SECONDS.sleep(2);
                                    clearScreen();
                                    menuNumber = 10;
                                    break;
                                }
                            } else if (isNumeric(menuCheck) == false) {
                                System.out.println("\nInvalid input");
                                TimeUnit.SECONDS.sleep(2);
                                clearScreen();
                                menuNumber = 10;
                                break;
                            }
                        }
                    }
                    break;
                case 11:
                    //add products

                    int addProductMenuOption;
                    System.out.println("Adding product menu\n");
                    System.out.println("1. Continue 2. Back\n");
                    System.out.print("Choose option: ");

                    menuCheck = input.nextLine();

                    if (isNumeric(menuCheck) == true) {

                        addProductMenuOption = Integer.parseInt(menuCheck);

                        if (addProductMenuOption == 1) {
                            clearScreen();

                            // ime na produkta
                            System.out.print("Enter product name: ");
                            String productName = input.nextLine();
                            System.out.println("");

                            //kolichestvo na produkta
                            System.out.print("Enter product quantity: ");
                            int productQuantity;
                            String productQuantityCheck = input.nextLine();

                            if (productQuantityCheck.isEmpty() == true) {
                                System.out.println();
                                System.out.println("Incorrect quantity!");
                                System.out.print("Enter product quantity: ");
                                productQuantityCheck = input.nextLine();
                            }

                            if (isNumeric(productQuantityCheck) == true) {
                                productQuantity = Integer.parseInt(productQuantityCheck);
                            } else {
                                while (isNumeric(productQuantityCheck) == false) {
                                    System.out.println();
                                    System.out.println("Incorrect quantity!");
                                    System.out.print("Enter product quantity: ");
                                    productQuantityCheck = input.nextLine();

                                }
                                productQuantity = Integer.parseInt(productQuantityCheck);
                            }

                            //opisasanie na produkta
                            System.out.println();
                            System.out.print("Enter product description: ");
                            String productDescription = input.nextLine();
                            System.out.println("");

                            //dostavna cena na produkta
                            System.out.print("Enter product price:");
                            Double productPrice;
                            String productPriceCheck = input.nextLine();
                            productPriceCheck = productPriceCheck.replace(",", ".");

                            if (productPriceCheck.isEmpty() == true) {
                                System.out.println();
                                System.out.println("Incorrect product price!");
                                System.out.print("Enter product price: ");
                                productPriceCheck = input.nextLine();
                                productPriceCheck = productPriceCheck.replace(",", ".");
                            }

                            if (isDouble(productPriceCheck) == true) {
                                productPrice = Double.parseDouble(productPriceCheck);
                            } else {
                                while (isDouble(productPriceCheck) == false) {
                                    System.out.println();
                                    System.out.println("Incorrect product price!");
                                    System.out.print("Enter product price: ");
                                    productPriceCheck = input.nextLine();
                                    productPriceCheck = productPriceCheck.replace(",", ".");
                                }
                                productPrice = Double.parseDouble(productPriceCheck);
                            }


                            //prodajna cena na produkta
                            System.out.println();
                            System.out.print("Enter product sale price: ");
                            Double productSalePrice;
                            String productSalePriceCheck = input.nextLine();
                            productSalePriceCheck = productSalePriceCheck.replace(",", ".");

                            if (productSalePriceCheck.isEmpty() == true) {
                                System.out.println();
                                System.out.println("Incorrect product sale price!");
                                System.out.print("Enter product sale price: ");
                                productSalePriceCheck = input.nextLine();
                                productSalePriceCheck = productSalePriceCheck.replace(",", ".");
                            }

                            if (isDouble(productSalePriceCheck) == true) {
                                productSalePrice = Double.parseDouble(productSalePriceCheck);
                            } else {
                                while (isDouble(productSalePriceCheck) == false) {
                                    System.out.println();
                                    System.out.println("Incorrect product sale price!");
                                    System.out.print("Enter product sale price: ");
                                    productSalePriceCheck = input.nextLine();
                                    productSalePriceCheck = productSalePriceCheck.replace(",", ".");
                                }
                                productSalePrice = Double.parseDouble(productSalePriceCheck);
                            }


                            //categoriq na produkta
                            System.out.println();
                            System.out.print("Enter product category: ");
                            String productCategory = input.nextLine();
                            System.out.println("");

                            //syzdavane na produkta
                            Product b = new Product(productName, productQuantity, productDescription, productPrice, productSalePrice, productCategory);


                            //User b = new User (firstName ,secondName,username,password1, phoneNumber, levelOfAccess);

                            int productID=0;
                            productID = connection.pullLastID(2,productID)+1;
                            b.setProductID(productID);

                            connection.productsConnection(b);

                            connection.userEdit("lastID", "LastID", String.valueOf(productID),2);


                            //dobavqne v log fail-a
                            Calendar c = Calendar.getInstance();
                            String productAddingDate = sdf.format(c.getTime());

                            String info = AllData.products.get(products.size() - 1).toString() + " Date of adding: " + productAddingDate +
                                    " Added by user with id: " + currentUserID + "\n";
                            printToFile("D:\\InventoryManagementSys\\textfile/AddProductLogFile.txt", info);

                            System.out.println("New product added!");
                            TimeUnit.SECONDS.sleep(2);
                            clearScreen();
                            menuNumber = 11;
                            break;
                        } else if (addProductMenuOption == 2) {
                            clearScreen();
                            menuNumber = 9;
                            break;
                        } else {
                            System.out.println("\nInvalid option");
                            TimeUnit.SECONDS.sleep(2);
                            clearScreen();
                            menuNumber = 11;
                            break;
                        }
                    } else if (isNumeric(menuCheck) == false) {
                        System.out.println("\nInvalid input");
                        TimeUnit.SECONDS.sleep(2);
                        clearScreen();
                        menuNumber = 11;
                        break;
                    }
                case 12:
                    //remove product
                    boolean removeProductResult = false;

                    System.out.println("Removing products menu\n");
                    System.out.println("1. Continue 2. Back\n");
                    System.out.print("Choose option: ");
                    int removeProductMenuOption;

                    menuCheck = input.nextLine();


                    if (isNumeric(menuCheck) == true) {
                        removeProductMenuOption = Integer.parseInt(menuCheck);

                        if (removeProductMenuOption == 1) {

                            clearScreen();

                            System.out.println("Enter the product ID to remove");
                            System.out.println();
                            int removeProductId;
                            System.out.print("ID: ");

                            menuCheck = input.nextLine();


                            if (isNumeric(menuCheck) == true) {

                                removeProductId = Integer.parseInt(menuCheck);

                                for (int i = 0; i < products.size(); i++) {
                                    if (removeProductId == products.get(i).getProductID()) {

                                        //dobavqne v log fail-a
                                        Calendar c = Calendar.getInstance();
                                        String productRemovingDate = sdf.format(c.getTime());

                                        String info = AllData.products.get(i).toString() + " Date of removing: " + productRemovingDate +
                                                " Removed by user with id: " + currentUserID + "\n";
                                        printToFile("D:\\InventoryManagementSys\\textfile/RemoveProductLogFile.txt", info);

                                        connection.removeProducts(removeProductId);
                                        products.remove(i);

                                        removeProductResult = true;
                                        break;
                                    }
                                    if (removeProductId != products.get(i).getProductID()) {
                                        removeProductResult = false;

                                    }

                                    menuNumber = 12;

                                }
                                if (removeProductResult == true) {
                                    System.out.println("\nProduct was removed");
                                    TimeUnit.SECONDS.sleep(2);
                                    clearScreen();
                                    menuNumber = 12;
                                } else if (removeProductResult == false) {
                                    System.out.println("\nThis product does not exist or is already removed!");
                                    TimeUnit.SECONDS.sleep(2);
                                    clearScreen();
                                    menuNumber = 12;
                                }
                            } else if (isNumeric(menuCheck) == false) {
                                System.out.println("\nInvalid input");
                                TimeUnit.SECONDS.sleep(2);
                                clearScreen();
                                menuNumber = 12;
                                break;
                            }
                        } else if (removeProductMenuOption == 2) {
                            clearScreen();
                            menuNumber = 9;
                            break;
                        } else {
                            System.out.println("\nIncorrect option");
                            TimeUnit.SECONDS.sleep(2);
                            clearScreen();
                            menuNumber = 12;
                            break;
                        }
                        break;

                    } else if (isNumeric(menuCheck) == false) {
                        System.out.println("\nIncorrect input");
                        TimeUnit.SECONDS.sleep(2);
                        clearScreen();
                        menuNumber = 12;
                        break;
                    }
                    break;
                case 13:
                    //edit products

                    int editID2 = 0;
                    boolean editProducts = true;
                    int editProductMenuOption = 0;
                    int productIndex = 0;


                    while (editProducts == true) {
                        switch (editProductMenuOption) {

                            case 0:
                                System.out.println("Edit products menu\n");
                                System.out.println("1. Continue 2. Back\n");
                                System.out.print("Choose option: ");

                                menuCheck = input.nextLine();

                                if (isNumeric(menuCheck) == true) {
                                    editProductMenuOption = Integer.parseInt(menuCheck);
                                    if ((editProductMenuOption == 1)) {
                                        clearScreen();
                                        editProductMenuOption = 1;
                                        break;
                                    } else if (editProductMenuOption == 2) {
                                        clearScreen();
                                        menuNumber = 9;
                                        editProducts = false;
                                        break;
                                    } else {
                                        System.out.println("\nInvalid option");
                                        TimeUnit.SECONDS.sleep(2);
                                        clearScreen();
                                        editProductMenuOption=0;
                                        menuNumber = 13;
                                        break;
                                    }
                                } else if (isNumeric(menuCheck) == false) {
                                    System.out.println("\nInvalid input");
                                    TimeUnit.SECONDS.sleep(2);
                                    clearScreen();
                                    menuNumber = 13;
                                    break;
                                }

                            case 1:
                                boolean validID = false;
                                System.out.println("Type in the product ID you want to edit:\n");
                                System.out.print("ID: ");
                                editID2 = 0;

                                menuCheck = input.nextLine();


                                if (isNumeric(menuCheck) == true) {
                                    editID2 = Integer.parseInt(menuCheck);

                                } else {
                                    System.out.println("\nInvalid input");
                                    TimeUnit.SECONDS.sleep(2);
                                    clearScreen();
                                    editUserMenuOption = 0;
                                    break;
                                }

                                for (int i = 0; i < products.size(); i++) {
                                    if (products.get(i).getProductID() == editID2) {
                                        clearScreen();
                                        editProductMenuOption = 2;
                                        productIndex = i;
                                        validID = true;
                                        break;
                                    } else if (products.get(i).getProductID() != editID2) {
                                        editProductMenuOption = 0;
                                        validID = false;

                                    }
                                }

                                if (validID == false) {
                                    System.out.println("\nThis ID does not exist!");
                                    TimeUnit.SECONDS.sleep(2);
                                    clearScreen();
                                    menuNumber = 13;
                                }
                                break;


                            case 2:
                                System.out.println("Current editing ID: " + editID2 + "\n");
                                System.out.println("Select want you want to edit : 1.Product name 2.Product Quantity 3.Product description 4.Product price 5.Product sell price 6.Product category 7. Back\n");
                                System.out.print("Choose option: ");
                                //editProductMenuOption = input.nextInt();

                                menuCheck = input.nextLine();


                                if (isNumeric(menuCheck) == true) {
                                    editProductMenuOption = Integer.parseInt(menuCheck);

                                } else {
                                    System.out.println("\nInvalid input");
                                    TimeUnit.SECONDS.sleep(2);
                                    clearScreen();
                                    editProductMenuOption = 2;
                                    break;
                                }

                                if (editProductMenuOption == 1) {
                                    clearScreen();
                                    editProductMenuOption = 3;
                                    break;
                                } else if (editProductMenuOption == 2) {
                                    clearScreen();
                                    editProductMenuOption = 4;
                                    break;
                                } else if (editProductMenuOption == 3) {
                                    clearScreen();
                                    editProductMenuOption = 5;
                                    break;
                                } else if (editProductMenuOption == 4) {
                                    clearScreen();
                                    editProductMenuOption = 6;
                                    break;
                                } else if (editProductMenuOption == 5) {
                                    clearScreen();
                                    editProductMenuOption = 7;
                                    break;
                                } else if (editProductMenuOption == 6) {
                                    clearScreen();
                                    editProductMenuOption = 8;
                                    break;
                                } else if (editProductMenuOption == 7) {
                                    clearScreen();
                                    editProductMenuOption = 9;
                                    break;
                                }else {
                                    System.out.println("\nInvalid option!");
                                    TimeUnit.SECONDS.sleep(2);
                                    clearScreen();
                                    editProductMenuOption = 2;
                                    break;
                                }

                            case 3:

                                String oldProductName = products.get(productIndex).getProductName();

                                System.out.println("Current product name: " + products.get(productIndex).getProductName() + "\n\n");
                                System.out.print("Enter new product name: ");

                                //input.nextLine();
                                String newProductName = input.nextLine();

                                Calendar c = Calendar.getInstance();
                                String productEditDate = sdf.format(c.getTime());


                                products.get(productIndex).setProductName(newProductName);
                                connection.userEdit("products", "ProductName", products.get(productIndex).getProductName(), editID2);

                                //dobavqne v log file-a
                                String info = products.get(productIndex) + " Date of editing: " + productEditDate +
                                        " Edited by user with id: " + currentUserID + " Change made - Changed product name - Old product name: " +
                                        oldProductName + " || New product name:" + products.get(productIndex).getProductName() + "\n";
                                printToFile("D:\\InventoryManagementSys\\textfile/EditProductLogFile.txt", info);

                                clearScreen();
                                editUserMenuOption = 2;


                                //}
                                // }

                                editProductMenuOption = 2;
                                break;

                            case 4:
                                //edit product quantity
                                // System.out.println(products.get(productIndex).getProductID());
                                // System.out.println(editID2);


                                String oldProductQauntity = products.get(productIndex).getProductQuantity().toString();

                                System.out.println("Current product quantity: " + products.get(productIndex).getProductQuantity() + "\n\n");
                                System.out.print("Enter new product quantity: ");

                                //input.nextLine();
                                int newProductQuantity;

                                menuCheck = input.nextLine();


                                if (isNumeric(menuCheck) == true) {
                                    newProductQuantity = Integer.parseInt(menuCheck);

                                } else {
                                    System.out.println("Invalid option");
                                    TimeUnit.SECONDS.sleep(2);
                                    clearScreen();
                                    editProductMenuOption = 4;
                                    break;
                                }

                                //for (int i = 0; i < products.size(); i++) {
                                if (products.get(productIndex).getProductID() == editID2) {

                                    c = Calendar.getInstance();
                                    productEditDate = sdf.format(c.getTime());

                                    // System.out.println("check1");
                                    products.get(productIndex).setProductQuantity(newProductQuantity);
                                    connection.userEdit("products", "ProductQuantity", products.get(productIndex).getProductQuantity().toString(), editID2);

                                    //dobavqne v log file-a
                                    info = products.get(productIndex) + " Date of editing: " + productEditDate +
                                            " Edited by user with id: " + currentUserID + " Change made - Changed product quantity - Old product quantity: " +
                                            oldProductQauntity + " || New product quantity:" + products.get(productIndex).getProductQuantity() + "\n";
                                    printToFile("D:\\InventoryManagementSys\\textfile/EditProductLogFile.txt", info);
                                    System.out.println("check1");
                                    clearScreen();
                                    editUserMenuOption = 2;


                                }


                                editProductMenuOption = 2;
                                break;

                            case 5:
                                //edit product description
                                //for (int i = 0; i < products.size(); i++) {
                                // if (products.get(i).getProductID() == editID2) {

                                String oldProductDescription = products.get(productIndex).getProductDescription();

                                System.out.println("Current product description: " + products.get(productIndex).getProductDescription() + "\n\n");
                                System.out.print("Enter new product description: ");

                                //input.nextLine();
                                String newProductDescription = input.nextLine();

                                c = Calendar.getInstance();
                                productEditDate = sdf.format(c.getTime());

                                products.get(productIndex).setProductDescription(newProductDescription);
                                connection.userEdit("products", "ProductDescription", products.get(productIndex).getProductDescription(), editID2);

                                //dobavqne v log file-a
                                info = products.get(productIndex) + " Date of editing: " + productEditDate +
                                        " Edited by user with id: " + currentUserID + " Change made - Changed product description - Old product description: " +
                                        oldProductDescription + " || New product description:  " + products.get(productIndex).getProductDescription() + "\n";
                                printToFile("D:\\InventoryManagementSys\\textfile/EditProductLogFile.txt", info);

                                clearScreen();
                                editUserMenuOption = 2;
                                //}
                                // }

                                editProductMenuOption = 2;
                                break;

                            case 6:
                                //edit product price
                                // for (int i = 0; i < products.size(); i++) {
                                //  if (products.get(i).getProductID() == editID2) {

                                String oldProductPrice = products.get(productIndex).getProductPrice().toString();

                                System.out.println("Current product price: " + products.get(productIndex).getProductPrice() + "\n\n");
                                System.out.print("Enter new product price: ");
                                Double newProductPrice = 0.0;

                                menuCheck = input.nextLine();
                                menuCheck = menuCheck.replace(",", ".");


                                if (isDouble(menuCheck) == true) {
                                    newProductPrice = Double.parseDouble(menuCheck);

                                } else {
                                    System.out.println("Invalid option");
                                    TimeUnit.SECONDS.sleep(2);
                                    clearScreen();
                                    editProductMenuOption = 6;
                                    break;
                                }


                                c = Calendar.getInstance();
                                productEditDate = sdf.format(c.getTime());

                                products.get(productIndex).setProductPrice(newProductPrice);
                                connection.userEdit("products", "ProductPrice", products.get(productIndex).getProductPrice().toString(), editID2);

                                //dobavqne v log file-a
                                info = products.get(productIndex) + " Date of editing: " + productEditDate +
                                        " Edited by user with id: " + currentUserID + " Change made - Changed product price - Old product price: " +
                                        oldProductPrice + " || New product price:" + products.get(productIndex).getProductPrice() + "\n";
                                printToFile("D:\\InventoryManagementSys\\textfile/EditProductLogFile.txt", info);


                                //   }
                                //   }

                                clearScreen();
                                editProductMenuOption = 2;
                                break;


                            case 7:
                                //edit product sell price
                                //for (int i = 0; i < products.size(); i++) {
                                //if (products.get(i).getProductID() == editID2) {

                                String oldProductSellPrice = products.get(productIndex).getProductSalePrice().toString();

                                System.out.println("Current product sell price: " + products.get(productIndex).getProductSalePrice() + "\n\n");
                                System.out.print("Enter new product sell price: ");


                                Double newProductSellPrice = 0.0;
                                menuCheck = input.nextLine();
                                menuCheck = menuCheck.replace(",", ".");


                                if (isDouble(menuCheck) == true) {
                                    newProductSellPrice = Double.parseDouble(menuCheck);

                                } else {
                                    System.out.println("Invalid option");
                                    TimeUnit.SECONDS.sleep(2);
                                    clearScreen();
                                    editProductMenuOption = 7;
                                    break;
                                }

                                c = Calendar.getInstance();
                                productEditDate = sdf.format(c.getTime());

                                products.get(productIndex).setProductSellPrice(newProductSellPrice);
                                connection.userEdit("products", "ProductSalePrice", products.get(productIndex).getProductSalePrice().toString(), editID2);

                                //dobavqne v log file-a
                                info = products.get(productIndex) + " Date of editing: " + productEditDate +
                                        " Edited by user with id: " + currentUserID + " Change made - Changed product sell price - Old product sell price: " +
                                        oldProductSellPrice + " || New product sell price:" + products.get(productIndex).getProductSalePrice() + "\n";
                                printToFile("D:\\InventoryManagementSys\\textfile/EditProductLogFile.txt", info);


                                //  }
                                // }

                                clearScreen();
                                editProductMenuOption = 2;
                                break;


                            case 8:
                                String oldProductCategoty = products.get(productIndex).getProductCategory();

                                System.out.println("Current product category: " + products.get(productIndex).getProductCategory() + "\n\n");
                                System.out.print("Enter new product category: ");

                                //input.nextLine();
                                String newProductCategory = input.nextLine();

                                c = Calendar.getInstance();
                                productEditDate = sdf.format(c.getTime());

                                products.get(productIndex).setProductCategory(newProductCategory);
                                connection.userEdit("products", "ProductCategory", products.get(productIndex).getProductCategory(), editID2);

                                //dobavqne v log file-a
                                info = products.get(productIndex) + " Date of editing: " + productEditDate +
                                        " Edited by user with id: " + currentUserID + " Change made - Changed product category - Old product category: " +
                                        oldProductCategoty + " || New product category:" + products.get(productIndex).getProductCategory() + "\n";
                                printToFile("D:\\InventoryManagementSys\\textfile/EditProductLogFile.txt", info);

                                clearScreen();
                                editProductMenuOption = 2;
                                break;


                            case 9:
                                editProducts = false;
                                //menuNumber=13;
                                break;
                        }


                    }
                    break;


                case 14:
                    int helpMenuOption2;

                    //help screen
                    //int helpMenuOption;

                    System.out.println("Help menu\n");
                    System.out.println("In the product menu you can see all products in the system, add new products," +
                            "\nedit or remove the already existing ones depending of your access level. \n");
                    System.out.println("1. Back \n");
                    System.out.print("Choose option: ");

                    menuCheck = input.nextLine();

                    if (isNumeric(menuCheck) == true) {
                        helpMenuOption2 = Integer.parseInt(menuCheck);

                        if (helpMenuOption2 == 1) {
                            clearScreen();
                            menuNumber = 9;
                            break;
                        } else {
                            System.out.println("\nIncorrect option");
                            TimeUnit.SECONDS.sleep(2);
                            clearScreen();
                            menuNumber = 14;
                            break;
                        }
                    } else if (isNumeric(menuCheck) == false) {
                        System.out.println("\nIncorrect input");
                        TimeUnit.SECONDS.sleep(2);
                        clearScreen();
                        menuNumber = 14;
                        break;
                    }

                case 15:
                    System.out.println("Orders menu\n");
                    System.out.println("1. Make order \n2. Pending Orders \n3. Complete order \n4. Order history \n5. Remove from pending orders \n6. Back");
                    System.out.println("");
                    System.out.print("Choose option: ");

                    menuCheck = input.nextLine();

                    if (isNumeric(menuCheck) == true) {
                        System.out.println(menuCheck);
                        menuNumber = Integer.parseInt(menuCheck);
                        if (menuNumber == 1) {
                            clearScreen();
                            menuNumber = 16;
                            break;
                        } else if (menuNumber == 2) {
                            clearScreen();
                            menuNumber = 17;
                            break;
                        } else if (menuNumber == 3) {
                            clearScreen();
                            menuNumber = 18;
                            break;
                        } else if (menuNumber == 4) {
                            clearScreen();
                            menuNumber = 19;
                            break;
                        } else if (menuNumber == 5) {
                            clearScreen();
                            menuNumber = 20;
                            break;
                        } else if (menuNumber == 6) {
                            clearScreen();
                            menuNumber = 2;
                            break;
                        } else {
                            System.out.println("\nInvalid option");
                            TimeUnit.SECONDS.sleep(2);
                            clearScreen();
                            menuNumber = 15;
                            break;
                        }
                    } else if (isNumeric(menuCheck) == false) {
                        System.out.println("\nInvalid input");
                        TimeUnit.SECONDS.sleep(2);
                        clearScreen();
                        menuNumber = 15;
                        break;
                    }

                case 16:
                    //make order menu

                    Order order = new Order();

                    int orderID=0;
                    orderID = connection.pullLastID(3,orderID)+1;
                    order.setOrderID(orderID);
                    connection.userEdit("lastID", "LastID", String.valueOf(orderID),3);

                    //order.setOrderID();

                    order.products.clear();
                    order.amountOfProduct.clear();

                    String orderInput = "";
                    String orderInput2 = "";
                    int productID = 0;
                    int amountOfProduct = 0;
                    boolean makeOrderLoop = true;
                    boolean incorrectInputLoop = true;
                    boolean productExist = true;
                    int makeOrderMenu = 0;

                    boolean productIDcheck = false;

                    System.out.println("Make order menu \n\n1. Continue  2. Back");
                    System.out.println("");
                    System.out.print("Choose option: ");


                    menuCheck = input.nextLine();

                    if (isNumeric(menuCheck)) {
                        makeOrderMenu = Integer.parseInt(menuCheck);
                    } else if (isNumeric(menuCheck) == false) {
                        System.out.println("Invalid input");
                        TimeUnit.SECONDS.sleep(2);
                        clearScreen();
                        menuNumber = 16;
                        break;
                    }

                    if (makeOrderMenu == 1) {

                        while (makeOrderLoop == true) {
                            clearScreen();
                            System.out.println("Products IDs: " + order.getProductsID());
                            System.out.println("Products amounts" + order.amountOfProduct + "\n");
                            System.out.print("Enter product ID: ");

                            orderInput = input.nextLine().toLowerCase();

                            if (isNumeric(orderInput)) {
                                productID = Integer.parseInt(orderInput);
                            } else if (orderInput.toLowerCase().equals("end")) {
                                if (order.products.size() >= 1) {
                                    System.out.println();
                                    System.out.print("Expected days to deliver: ");
                                    //proverka

                                    int daysToDeliver = 0;
                                    menuCheck = input.nextLine();

                                    if (isNumeric(menuCheck)) {
                                        daysToDeliver = Integer.parseInt(menuCheck);
                                    } else if (isNumeric(menuCheck) == false) {
                                        while (true) {
                                            System.out.println();
                                            System.out.print("Expected days to deliver: ");
                                            menuCheck = input.nextLine();

                                            if (isNumeric(menuCheck)) {
                                                daysToDeliver = Integer.parseInt(menuCheck);
                                                break;
                                            } else if (isNumeric(menuCheck) == false) {
                                            }
                                        }
                                    }
                                    //= input.nextInt();
                                    order.deliveryDate(daysToDeliver);
                                    connection.createOrder(order);
                                    pendingOrders.add(order);

                                    //dobavqne v log fail-a
                                    Calendar c = Calendar.getInstance();
                                    String orderAddDate = sdf.format(c.getTime());


                                    String info = "Order ID: " + order.getOrderID() + " || Created by user with ID: " + currentUserID +
                                            " || Date of creating " + orderAddDate + " || Order cost: " + order.getOrderCost() +
                                            " || Order expected delivery date: " + order.getDeliveryDate() +
                                            " || Ordered product/s ID/s " + order.getProductsList() + " || Amount of ordered products " + order.getProductsAmount() + "\n";
                                    printToFile("D:\\InventoryManagementSys\\textfile/AddOrderLogFile.txt", info);
                                    clearScreen();
                                    menuNumber = 16;
                                    break;

                                } else if (order.products.size() < 1) {
                                    System.out.println("Order is not created");
                                    TimeUnit.SECONDS.sleep(2);
                                    clearScreen();
                                    //makeOrderLoop=false;
                                    menuNumber = 16;
                                    break;
                                }
                            } else if (isNumeric(orderInput) == false) {
                                boolean check = false;

                                while (check == false) {
                                    System.out.print("Enter product ID: ");

                                    orderInput = input.nextLine().toLowerCase();

                                    if (isNumeric(orderInput)) {
                                        productID = Integer.parseInt(orderInput);
                                        check = true;
                                    } else if (orderInput.toLowerCase().equals("end")) {
                                        if (order.products.size() >= 1) {
                                            System.out.println();
                                            System.out.print("Expected days to deliver: ");
                                            //proverka

                                            int daysToDeliver = 0;
                                            menuCheck = input.nextLine();

                                            if (isNumeric(menuCheck)) {
                                                daysToDeliver = Integer.parseInt(menuCheck);
                                            } else if (isNumeric(menuCheck) == false) {
                                                while (true) {
                                                    System.out.println();
                                                    System.out.print("Expected days to deliver: ");
                                                    menuCheck = input.nextLine();

                                                    if (isNumeric(menuCheck)) {
                                                        daysToDeliver = Integer.parseInt(menuCheck);
                                                        break;
                                                    } else if (isNumeric(menuCheck) == false) {
                                                    }
                                                }
                                            }
                                            //= input.nextInt();
                                            order.deliveryDate(daysToDeliver);
                                            connection.createOrder(order);
                                            pendingOrders.add(order);

                                            //dobavqne v log fail-a
                                            Calendar c = Calendar.getInstance();
                                            String orderAddDate = sdf.format(c.getTime());


                                            String info = "Order ID: " + order.getOrderID() + " || Created by user with ID: " + currentUserID +
                                                    " || Date of creating " + orderAddDate + " || Order cost: " + order.getOrderCost() +
                                                    " || Order expected delivery date: " + order.getDeliveryDate() +
                                                    " || Ordered product/s ID/s " + order.getProductsList() + " || Amount of ordered products " + order.getProductsAmount() + "\n";
                                            printToFile("D:\\InventoryManagementSys\\textfile/AddOrderLogFile.txt", info);
                                            clearScreen();
                                            menuNumber = 16;
                                            break;

                                        } else if (order.products.size() < 1) {
                                            System.out.println("Order is not created");
                                            TimeUnit.SECONDS.sleep(2);
                                            clearScreen();
                                            makeOrderLoop = false;
                                            menuNumber = 16;
                                            break;
                                        }

                                    }
                                }

                                if (isNumeric(orderInput) == false) {
                                    break;
                                } else {

                                }

                            }


                            //amount

                            clearScreen();
                            System.out.println("Products IDs: " + order.getProductsID());
                            System.out.println("Products amounts" + order.amountOfProduct + "\n");
                            System.out.print("Enter product amount: ");

                            orderInput2 = input.nextLine().toLowerCase();

                            if (isNumeric(orderInput2)) {
                                amountOfProduct = Integer.parseInt(orderInput2);
                            } else if (orderInput2.toLowerCase().equals("end")) {
                                if (order.products.size() >= 1) {
                                    System.out.println();
                                    System.out.print("Expected days to deliver: ");
                                    //proverka

                                    int daysToDeliver = 0;
                                    menuCheck = input.nextLine();

                                    if (isNumeric(menuCheck)) {
                                        daysToDeliver = Integer.parseInt(menuCheck);
                                    } else if (isNumeric(menuCheck) == false) {
                                        while (true) {
                                            System.out.println();
                                            System.out.print("Expected days to deliver: ");
                                            menuCheck = input.nextLine();

                                            if (isNumeric(menuCheck)) {
                                                daysToDeliver = Integer.parseInt(menuCheck);
                                                break;
                                            } else if (isNumeric(menuCheck) == false) {
                                            }
                                        }
                                    }
                                    //= input.nextInt();
                                    order.deliveryDate(daysToDeliver);
                                    connection.createOrder(order);
                                    pendingOrders.add(order);

                                    //dobavqne v log fail-a
                                    Calendar c = Calendar.getInstance();
                                    String orderAddDate = sdf.format(c.getTime());


                                    String info = "Order ID: " + order.getOrderID() + " || Created by user with ID: " + currentUserID +
                                            " || Date of creating " + orderAddDate + " || Order cost: " + order.getOrderCost() +
                                            " || Order expected delivery date: " + order.getDeliveryDate() +
                                            " || Ordered product/s ID/s " + order.getProductsList() + " || Amount of ordered products " + order.getProductsAmount() + "\n";
                                    printToFile("D:\\InventoryManagementSys\\textfile/AddOrderLogFile.txt", info);
                                    clearScreen();
                                    menuNumber = 16;
                                    break;

                                } else if (order.products.size() < 1) {
                                    System.out.println("Order is not created");
                                    TimeUnit.SECONDS.sleep(2);
                                    clearScreen();
                                    //makeOrderLoop=false;
                                    menuNumber = 16;
                                    break;
                                }
                            } else if (isNumeric(orderInput2) == false) {
                                boolean check1 = false;

                                while (check1 == false) {
                                    System.out.print("Enter products amount : ");

                                    orderInput2 = input.nextLine().toLowerCase();

                                    if (isNumeric(orderInput2)) {
                                        amountOfProduct = Integer.parseInt(orderInput2);
                                        check1 = true;
                                    } else if (orderInput2.toLowerCase().equals("end")) {
                                        if (order.products.size() >= 1) {
                                            System.out.println();
                                            System.out.print("Expected days to deliver: ");
                                            //proverka

                                            int daysToDeliver = 0;
                                            menuCheck = input.nextLine();

                                            if (isNumeric(menuCheck)) {
                                                daysToDeliver = Integer.parseInt(menuCheck);
                                            } else if (isNumeric(menuCheck) == false) {
                                                while (true) {
                                                    System.out.println();
                                                    System.out.print("Expected days to deliver: ");
                                                    menuCheck = input.nextLine();

                                                    if (isNumeric(menuCheck)) {
                                                        daysToDeliver = Integer.parseInt(menuCheck);
                                                        break;
                                                    } else if (isNumeric(menuCheck) == false) {
                                                    }
                                                }
                                            }
                                            //= input.nextInt();
                                            order.deliveryDate(daysToDeliver);
                                            connection.createOrder(order);
                                            pendingOrders.add(order);

                                            //dobavqne v log fail-a
                                            Calendar c = Calendar.getInstance();
                                            String orderAddDate = sdf.format(c.getTime());


                                            String info = "Order ID: " + order.getOrderID() + " || Created by user with ID: " + currentUserID +
                                                    " || Date of creating " + orderAddDate + " || Order cost: " + order.getOrderCost() +
                                                    " || Order expected delivery date: " + order.getDeliveryDate() +
                                                    " || Ordered product/s ID/s " + order.getProductsList() + " || Amount of ordered products " + order.getProductsAmount() + "\n";
                                            printToFile("D:\\InventoryManagementSys\\textfile/AddOrderLogFile.txt", info);
                                            clearScreen();
                                            menuNumber = 16;
                                            break;

                                        } else if (order.products.size() < 1) {
                                            System.out.println("Order is not created");
                                            TimeUnit.SECONDS.sleep(2);
                                            clearScreen();
                                            makeOrderLoop = false;
                                            menuNumber = 16;
                                            break;
                                        }
                                    }
                                }
                                if (isNumeric(orderInput2) == false) {
                                    break;
                                } else {

                                }
                            }
                            //adding to order

                            if (isNumeric(orderInput) && isNumeric(orderInput2)) {
                                //System.out.println(products.toString());
                                for (int i = 0; i < products.size(); i++) {
                                    // System.out.println(i);
                                    if (products.get(i).getProductID() == productID) {

                                        System.out.println(i);
                                        order.products.add(products.get(i));
                                        order.amountOfProduct.add(amountOfProduct);
                                        //System.out.println("added to the list");

                                        System.out.println(order.products.toString());
                                        System.out.println(order.amountOfProduct);

                                        productExist = true;
                                        break;
                                    } else if (products.get(i).getProductID() != productID) {
                                        productExist = false;
                                        // System.out.println("not added");
                                    }
                                }
                                if (productExist == true) {
                                    //System.out.println("added to order");
                                    //System.out.println(order.getProductsList());


                                } else if (productExist != true) {
                                    System.out.println("This product does not exist!");
                                    TimeUnit.SECONDS.sleep(2);

                                }
                            }
                        }
                    } else if (makeOrderMenu == 2) {
                        clearScreen();
                        menuNumber = 15;
                    } else {
                        System.out.println("\nInvalid option");
                        TimeUnit.SECONDS.sleep(2);
                        clearScreen();
                        menuNumber = 16;
                        break;
                    }
                    break;


                case 17:
                    System.out.println("All pending orders\n");

                    Double amountOfPages3 = pendingOrders.size() / 3.0;

                    amountOfPages3 = Math.ceil(amountOfPages3);

                    System.out.printf("Amount of pages: " + String.format("%.0f", amountOfPages3) + "\n" + "\n");

                    if (amountOfPages3 == 0) {
                        System.out.println("Nothing to show\n");
                        System.out.print("1. Back\n\n");
                        System.out.print("Choose option: ");
                        menuCheck = input.nextLine();

                        if (isNumeric(menuCheck) == true) {
                            menuNumber = Integer.parseInt(menuCheck);
                            if (menuNumber == 1) {
                                page3 = 0;
                                clearScreen();
                                menuNumber = 15;
                                break;
                            } else {
                                System.out.println("\nInvalid option");
                                TimeUnit.SECONDS.sleep(2);
                                clearScreen();
                                menuNumber = 17;
                                break;
                            }
                        } else if (isNumeric(menuCheck) == false) {
                            System.out.println("\nInvalid input");
                            TimeUnit.SECONDS.sleep(2);
                            clearScreen();
                            menuNumber = 17;
                            break;
                        }

                    } else {

                        int chunkSize = 3;
                        AtomicInteger counter = new AtomicInteger();
                        final Collection<List<Order>> partitionedList =
                                pendingOrders.stream().collect(Collectors.groupingBy(i -> counter.getAndIncrement() / chunkSize))
                                        .values();

                        for (int i = 0; i < partitionedList.stream().skip(page3).findFirst().orElse(null).size(); i++) {
                            System.out.println(partitionedList.stream().skip(page3).findFirst().orElse(null).get(i));
                        }

                        if (amountOfPages3 == 1) {

                            System.out.print("\n1. Back\n\n");
                            System.out.print("Choose option: ");
                            menuCheck = input.nextLine();


                            if (isNumeric(menuCheck) == true) {
                                menuNumber = Integer.parseInt(menuCheck);
                                if (menuNumber == 1) {
                                    page3 = 0;
                                    clearScreen();
                                    menuNumber = 15;
                                    break;
                                } else {
                                    System.out.println("\nInvalid option");
                                    TimeUnit.SECONDS.sleep(2);
                                    clearScreen();
                                    menuNumber = 17;
                                    break;
                                }
                            } else if (isNumeric(menuCheck) == false) {
                                System.out.println("\nInvalid input");
                                TimeUnit.SECONDS.sleep(2);
                                clearScreen();
                                menuNumber = 17;
                                break;

                            }
                        }

                        if (page3 == 0) {

                            System.out.print("\n1. Back 2. Next Page\n\n");
                            System.out.print("Choose option: ");
                            menuCheck = input.nextLine();

                            if (isNumeric(menuCheck) == true) {
                                menuNumber = Integer.parseInt(menuCheck);
                                if (menuNumber == 1) {
                                    page3 = 0;
                                    clearScreen();
                                    menuNumber = 15;
                                    break;
                                } else if (menuNumber == 2) {
                                    clearScreen();
                                    menuNumber = 17;
                                    page3 += 1;
                                    break;
                                } else {
                                    System.out.println("\nInvalid option");
                                    TimeUnit.SECONDS.sleep(2);
                                    clearScreen();
                                    menuNumber = 17;
                                    break;
                                }
                            } else if (isNumeric(menuCheck) == false) {
                                System.out.println("\nInvalid input");
                                TimeUnit.SECONDS.sleep(2);
                                clearScreen();
                                menuNumber = 17;
                                break;
                            }

                        } else if (page3 > 0 && page3 < amountOfPages3 - 1) {

                            System.out.print("\n1. Back 2. Next Page 3. Back Page\n\n");
                            System.out.print("Choose option: ");
                            menuCheck = input.nextLine();

                            if (isNumeric(menuCheck) == true) {
                                menuNumber = Integer.parseInt(menuCheck);
                                if (menuNumber == 1) {
                                    page3 = 0;
                                    clearScreen();
                                    menuNumber = 15;
                                    break;
                                } else if (menuNumber == 2) {
                                    page3 += 1;
                                    clearScreen();
                                    menuNumber = 17;
                                    break;
                                } else if (menuNumber == 3) {
                                    page3 -= 1;
                                    clearScreen();
                                    menuNumber = 17;
                                    break;
                                } else {
                                    System.out.println("\nInvalid option");
                                    TimeUnit.SECONDS.sleep(2);
                                    clearScreen();
                                    menuNumber = 17;
                                    break;
                                }
                            } else if (isNumeric(menuCheck) == false) {
                                System.out.println("\nInvalid input");
                                TimeUnit.SECONDS.sleep(2);
                                clearScreen();
                                menuNumber = 17;
                                break;
                            } else {
                                break;
                            }

                        } else if (page3 == amountOfPages3 - 1) {

                            System.out.print("\n1. Back 2. Back Page\n\n");
                            System.out.print("Choose option: ");
                            menuCheck = input.nextLine();

                            if (isNumeric(menuCheck) == true) {
                                menuNumber = Integer.parseInt(menuCheck);
                                if (menuNumber == 1) {
                                    page3 = 0;
                                    clearScreen();
                                    menuNumber = 15;
                                    break;
                                } else if (menuNumber == 2) {
                                    page3 -= 1;
                                    clearScreen();
                                    menuNumber = 17;
                                    break;
                                } else {
                                    System.out.println("\nInvalid option");
                                    TimeUnit.SECONDS.sleep(2);
                                    clearScreen();
                                    menuNumber = 17;
                                    break;
                                }
                            } else if (isNumeric(menuCheck) == false) {
                                System.out.println("\nInvalid input");
                                TimeUnit.SECONDS.sleep(2);
                                clearScreen();
                                menuNumber = 17;
                                break;

                            }
                        }
                    }


                case 18:

                    // TODO: 24.2.2022 г.  za tazi data shte e gotovo
                    //while (true) {
                    int completeOrderMenu = 1;
                    int completeOrderIndex = 0;
                    boolean orderMove = false;

                    //proverka za int

                    clearScreen();

                    switch (completeOrderMenu) {


                        case 1:
                            System.out.println("Complete order menu \n\n1. Continue  2. Back \n");
                            System.out.print("Choose option: ");

                            //completeOrderMenu = input.nextInt();

                            menuCheck = input.nextLine();

                            if (isNumeric(menuCheck)) {
                                completeOrderMenu = Integer.parseInt(menuCheck);
                            } else {
                                System.out.println("\nIncorrect input");
                                TimeUnit.SECONDS.sleep(2);
                                menuNumber = 18;
                                break;
                            }

                            if (completeOrderMenu == 1) {
                                completeOrderMenu = 2;


                            } else if (completeOrderMenu == 2) {
                                clearScreen();
                                menuNumber = 15;
                                break;
                            } else {
                                System.out.println("\nIncorrect option");
                                TimeUnit.SECONDS.sleep(2);
                                menuNumber = 18;
                                break;
                            }

                            System.out.println(completeOrderMenu);


                        case 2:
                            //System.out.println("check1");

                            clearScreen();


                            System.out.print("Enter the ID of the order you want to complete: ");
                            System.out.println();
                            //int removeUserId;
                            System.out.print("\nID: ");

                            //proverka
                            int moveID = 0;

                            menuCheck = input.nextLine();

                            if (isNumeric(menuCheck)) {
                                moveID = Integer.parseInt(menuCheck);
                            } else {
                                System.out.println("\nIncorrect input");
                                TimeUnit.SECONDS.sleep(2);
                                completeOrderMenu = 2;
                                break;

                            }


                            if (pendingOrders.size() > 0) {
                                for (int i = 0; i < pendingOrders.size(); i++) {
                                    if (pendingOrders.get(i).getOrderID() == moveID) {
                                        completeOrderIndex = i;
                                        orderMove = true;
                                        break;
                                    } else if (pendingOrders.get(i).getOrderID() != moveID) {
                                        orderMove = false;
                                    }
                                }

                                if (orderMove == true) {
                                    //System.out.println("order is moved");
                                    completeOrderMenu = 3;

                                } else if (orderMove == false) {
                                    System.out.println("\nOrder does not exist");
                                    TimeUnit.SECONDS.sleep(2);
                                    menuNumber = 18;
                                    break;
                                }

                            } else {
                                System.out.println("\nNothing to complete");
                                TimeUnit.SECONDS.sleep(2);
                                menuNumber = 18;
                                break;
                            }

                        case 3:
                            System.out.println();
                            System.out.print("Product IDs: ");
                            // System.out.println(pendingOrders.get(i).getProductsList());

                            ArrayList<String> productsListTemp = new ArrayList(Arrays.asList(pendingOrders.get(completeOrderIndex).getProductsList().split(",")));
                            List<Integer> productsList = new ArrayList<Integer>();

                            productsListTemp.remove(0);

                            for (int x = 0; x < productsListTemp.size(); x++) {
                                productsList.add(Integer.parseInt(productsListTemp.get(x)));
                            }

                            System.out.println(productsList.toString());


                            //System.out.println(productsList.toString());
                            //////////////////////////////////////////////////////////////

                            System.out.println();
                            System.out.print("Products amounts:");

                            ArrayList<String> amountListTemp = new ArrayList(Arrays.asList(pendingOrders.get(completeOrderIndex).getProductsAmount().split(",")));
                            List<Integer> amountList = new ArrayList<Integer>();

                            amountListTemp.remove(0);

                            for (int x = 0; x < amountListTemp.size(); x++) {
                                amountList.add(Integer.parseInt(amountListTemp.get(x)));
                            }

                            System.out.println(amountList.toString());
                            System.out.println("");


                            for (int n = 0; n < products.size(); n++) {
                                for (int x = 0; x < productsList.size(); x++) {

                                    //System.out.println(productsList.size());

                                    if (products.get(n).getProductID() == productsList.get(x)) {
                                        //System.out.println("ok");
                                        System.out.println("Product id: " + products.get(n).getProductID() + "  amount :" + products.get(n).getProductQuantity());

                                        int newQuantity = products.get(n).getProductQuantity() + amountList.get(x);

                                        products.get(n).setProductQuantity(newQuantity);

                                        System.out.println("Product id: " + products.get(n).getProductID() + "  amount :" + products.get(n).getProductQuantity());

                                        String edit = String.valueOf(newQuantity);

                                        connection.userEdit("products", "ProductQuantity", edit, products.get(n).getProductID());

                                        //System.out.println(x);
                                        //System.out.println(n);
                                        System.out.println();
                                    }
                                }
                            }

                            //

                            Calendar c = Calendar.getInstance();
                            String orderAddDate = sdf.format(c.getTime());

                            String info = "Order ID: " + pendingOrders.get(completeOrderIndex).getOrderID() + " || Completed by user with ID: " + currentUserID +
                                    " || Date of completing " + orderAddDate + " || Order cost: " + pendingOrders.get(completeOrderIndex).getOrderCost() +
                                    " || Order expected delivery date: " + pendingOrders.get(completeOrderIndex).getDeliveryDate() +
                                    " || Ordered product/s ID/s " + pendingOrders.get(completeOrderIndex).getProductsList() + " || Amount of ordered products " + pendingOrders.get(completeOrderIndex).getProductsAmount() + "\n";
                            printToFile("D:\\InventoryManagementSys\\textfile/CompleteOrderLogFile.txt", info);


                            orderMove = true;
                            connection.addToCompletedOrders((pendingOrders.get(completeOrderIndex)));
                            connection.removeOrderFromPending(pendingOrders.get(completeOrderIndex).getOrderID());

                            completedOrders.add(pendingOrders.get(completeOrderIndex));
                            pendingOrders.remove(completeOrderIndex);

                            System.out.println("\nPress ENTER to continue");
                            String xxx = input.nextLine();
                    }

                    break;

                case 19:
                    //show completed orders
                    System.out.println("All completed orders: \n");

                    /*
                    System.out.println("completed orders: " + pendingOrders.size());
                    */
                    Double amountOfPages4 = completedOrders.size() / 3.0;

                    amountOfPages4 = Math.ceil(amountOfPages4);

                    System.out.printf("Amount of pages: " + String.format("%.0f", amountOfPages4) + "\n" + "\n");

                    if (amountOfPages4 == 0) {
                        System.out.println("Nothing to show\n");
                        System.out.print("1. Back\n\n");
                        System.out.print("Choose option: ");
                        menuCheck = input.nextLine();

                        if (isNumeric(menuCheck) == true) {
                            menuNumber = Integer.parseInt(menuCheck);
                            if (menuNumber == 1) {
                                page4 = 0;
                                clearScreen();
                                menuNumber = 15;
                                break;
                            } else {
                                System.out.println("\nInvalid option");
                                TimeUnit.SECONDS.sleep(2);
                                clearScreen();
                                menuNumber = 19;
                                break;
                            }
                        } else if (isNumeric(menuCheck) == false) {
                            System.out.println("\nInvalid input");
                            TimeUnit.SECONDS.sleep(2);
                            clearScreen();
                            menuNumber = 19;
                            break;
                        }

                    } else {

                        int chunkSize = 3;
                        AtomicInteger counter = new AtomicInteger();
                        final Collection<List<Order>> partitionedList =
                                completedOrders.stream().collect(Collectors.groupingBy(i -> counter.getAndIncrement() / chunkSize))
                                        .values();

                        for (int i = 0; i < partitionedList.stream().skip(page4).findFirst().orElse(null).size(); i++) {
                            System.out.println(partitionedList.stream().skip(page4).findFirst().orElse(null).get(i));
                        }

                        if (amountOfPages4 == 1) {

                            System.out.print("\n1. Back\n\n");
                            System.out.print("Choose option: ");
                            menuCheck = input.nextLine();


                            if (isNumeric(menuCheck) == true) {
                                menuNumber = Integer.parseInt(menuCheck);
                                if (menuNumber == 1) {
                                    page4 = 0;
                                    clearScreen();
                                    menuNumber = 15;
                                    break;
                                } else {
                                    System.out.println("\nInvalid option");
                                    TimeUnit.SECONDS.sleep(2);
                                    clearScreen();
                                    menuNumber = 19;
                                    break;
                                }
                            } else if (isNumeric(menuCheck) == false) {
                                System.out.println("\nInvalid input");
                                TimeUnit.SECONDS.sleep(2);
                                clearScreen();
                                menuNumber = 19;
                                break;
                            }
                        }

                        if (page4 == 0) {

                            System.out.print("\n1. Back 2. Next Page\n\n");
                            System.out.print("Choose option: ");
                            menuCheck = input.nextLine();

                            if (isNumeric(menuCheck) == true) {
                                menuNumber = Integer.parseInt(menuCheck);
                                if (menuNumber == 1) {
                                    page4 = 0;
                                    clearScreen();
                                    menuNumber = 15;
                                    break;
                                } else if (menuNumber == 2) {
                                    clearScreen();
                                    menuNumber = 19;
                                    page4 += 1;
                                    break;
                                } else {
                                    System.out.println("\nInvalid option");
                                    TimeUnit.SECONDS.sleep(2);
                                    clearScreen();
                                    menuNumber = 19;
                                    break;
                                }
                            } else if (isNumeric(menuCheck) == false) {
                                System.out.println("\nInvalid input");
                                TimeUnit.SECONDS.sleep(2);
                                clearScreen();
                                menuNumber = 19;
                                break;
                            }

                        } else if (page4 > 0 && page4 < amountOfPages4 - 1) {

                            System.out.print("\n1. Back 2. Next Page 3. Back Page\n\n");
                            System.out.print("Choose option: ");
                            menuCheck = input.nextLine();

                            if (isNumeric(menuCheck) == true) {
                                menuNumber = Integer.parseInt(menuCheck);
                                if (menuNumber == 1) {
                                    page4 = 0;
                                    clearScreen();
                                    menuNumber = 15;
                                    break;
                                } else if (menuNumber == 2) {
                                    page4 += 1;
                                    clearScreen();
                                    menuNumber = 19;
                                    break;
                                } else if (menuNumber == 3) {
                                    page4 -= 1;
                                    clearScreen();
                                    menuNumber = 19;
                                    break;
                                } else {
                                    System.out.println("\nInvalid option");
                                    TimeUnit.SECONDS.sleep(2);
                                    clearScreen();
                                    menuNumber = 19;
                                    break;
                                }
                            } else if (isNumeric(menuCheck) == false) {
                                System.out.println("\nInvalid input");
                                TimeUnit.SECONDS.sleep(2);
                                clearScreen();
                                menuNumber = 19;
                                break;
                            } else {
                                break;
                            }

                        } else if (page4 == amountOfPages4 - 1) {

                            System.out.print("\n1. Back 2. Back Page\n\n");
                            System.out.print("Choose option: ");
                            menuCheck = input.nextLine();

                            if (isNumeric(menuCheck) == true) {
                                menuNumber = Integer.parseInt(menuCheck);
                                if (menuNumber == 1) {
                                    page4 = 0;
                                    clearScreen();
                                    menuNumber = 15;
                                    break;
                                } else if (menuNumber == 2) {
                                    page4 -= 1;
                                    clearScreen();
                                    menuNumber = 19;
                                    break;
                                } else {
                                    System.out.println("\nInvalid option");
                                    TimeUnit.SECONDS.sleep(2);
                                    clearScreen();
                                    menuNumber = 19;
                                    break;
                                }
                            } else if (isNumeric(menuCheck) == false) {
                                System.out.println("\nInvalid input");
                                TimeUnit.SECONDS.sleep(2);
                                clearScreen();
                                menuNumber = 19;
                                break;

                            }
                        }
                    }

                case 20:
                    //remove from pending
                    int removeFromPendingMenu;
                    boolean removeOrderResult = false;
                    System.out.println("Remove from pending orders \n\n1. Continue 2.Back\n");

                    System.out.print("Choose option: ");

                    menuCheck = input.nextLine();

                    if (isNumeric(menuCheck) == true) {
                        removeFromPendingMenu = Integer.parseInt(menuCheck);
                        if (removeFromPendingMenu == 1) {
                            clearScreen();
                            System.out.print("Enter the order id to remove: \n\n");
                            System.out.print("ID: ");

                            int removeOrderId;

                            menuCheck = input.nextLine();

                            if (isNumeric(menuCheck) == true) {

                                removeOrderId = Integer.parseInt(menuCheck);
                                if (pendingOrders.size() > 0) {
                                    for (int i = 0; i < pendingOrders.size(); i++) {


                                        if (removeOrderId == pendingOrders.get(i).getOrderID()) {

                                            Calendar c = Calendar.getInstance();
                                            String removeFromPendingOrdersDate = sdf.format(c.getTime());

                                            String info = "Order ID: " + pendingOrders.get(i).getOrderID() + " || Completed by user with ID: " + currentUserID +
                                                    " || Date of completing " + removeFromPendingOrdersDate + " || Order cost: " + pendingOrders.get(i).getOrderCost() +
                                                    " || Order expected delivery date: " + pendingOrders.get(i).getDeliveryDate() +
                                                    " || Ordered product/s ID/s " + pendingOrders.get(i).getProductsList() + " || Amount of ordered products " + pendingOrders.get(i).getProductsAmount() + "\n";
                                            printToFile("D:\\InventoryManagementSys\\textfile/ RemoveFromPendingOrdersLogFile.txt", info);

                                            connection.removeOrderFromPending(pendingOrders.get(i).getOrderID());
                                            pendingOrders.remove(i);
                                            removeOrderResult = true;
                                            break;
                                        }
                                        if (removeOrderId != pendingOrders.get(i).getOrderID()) {
                                            removeOrderResult = false;
                                        }

                                    }
                                    if (removeOrderResult == true) {
                                        System.out.println("\nOrder was removed");
                                        TimeUnit.SECONDS.sleep(2);
                                        clearScreen();
                                        break;
                                    } else if (removeOrderResult == false) {
                                        System.out.println("\nOrder was not removed");
                                        TimeUnit.SECONDS.sleep(2);
                                        clearScreen();
                                        break;
                                    }
                                } else {
                                    System.out.println("\nNo pending orders");
                                    TimeUnit.SECONDS.sleep(2);
                                    clearScreen();
                                    menuNumber = 20;
                                    break;
                                }

                            } else {
                                System.out.println("\nInvalid option");
                                TimeUnit.SECONDS.sleep(2);
                                clearScreen();
                                menuNumber = 20;
                                break;
                            }
                        } else if (removeFromPendingMenu == 2) {
                            TimeUnit.SECONDS.sleep(2);
                            clearScreen();
                            menuNumber = 15;
                            break;
                        } else {
                            System.out.println("\nInvalid option");
                            TimeUnit.SECONDS.sleep(2);
                            clearScreen();
                            menuNumber = 20;
                            break;
                        }
                    } else {
                        System.out.println("\nInvalid input");
                        TimeUnit.SECONDS.sleep(2);
                        clearScreen();
                        menuNumber = 20;
                        break;
                    }
                    break;

                case 21:
                    //Sale menu

                    System.out.println("Sales menu\n");
                    System.out.println("1. Make sale \n2. Pending Sales \n3. Complete sale \n4. Sales history \n5. Remove from pending \n6. Back");
                    System.out.println("");
                    System.out.print("Choose option: ");

                    menuCheck = input.nextLine();

                    if (isNumeric(menuCheck) == true) {
                        //System.out.println(menuCheck);
                        menuNumber = Integer.parseInt(menuCheck);
                        if (menuNumber == 1) {
                            clearScreen();
                            menuNumber = 22;
                        } else if (menuNumber == 2) {
                            clearScreen();
                            menuNumber = 23;
                        } else if (menuNumber == 3) {
                            clearScreen();
                            menuNumber = 24;
                        } else if (menuNumber == 4) {
                            clearScreen();
                            menuNumber = 25;
                        } else if (menuNumber == 5) {
                            clearScreen();
                            menuNumber = 26;
                        } else if (menuNumber == 6) {
                            clearScreen();
                            menuNumber = 2;
                        } else {
                            System.out.println("\nInvalid option");
                            TimeUnit.SECONDS.sleep(2);
                            clearScreen();
                            menuNumber = 21;
                            break;
                        }
                    } else if (isNumeric(menuCheck) == false) {
                        System.out.println("\nInvalid input");
                        TimeUnit.SECONDS.sleep(2);
                        clearScreen();
                        menuNumber = 21;
                        break;
                    }

                    break;

                case 22:
                    //make sell

                    Sale sale = new Sale();

                    int saleID=0;
                    saleID = connection.pullLastID(4,saleID)+1;
                    sale.setSaleID(saleID);
                    connection.userEdit("lastID", "LastID", String.valueOf(saleID),4);

                    sale.products.clear();
                    sale.amountOfProduct.clear();

                    String saleInput = "";
                    String saleInput2 = "";
                    int productID2 = 0;
                    int amountOfProduct2 = 0;
                    boolean makeOrderLoop2 = true;
                    boolean incorrectInputLoop2 = true;
                    boolean productExist2 = true;
                    int makeSaleMenu = 0;

                    System.out.println("Make sale menu \n\n1. Continue  2. Back");
                    System.out.println("");
                    System.out.print("Choose option: ");

                    menuCheck = input.nextLine();

                    if (isNumeric(menuCheck)) {
                        makeSaleMenu = Integer.parseInt(menuCheck);
                    } else if (isNumeric(menuCheck) == false) {
                        System.out.println("\nInvalid input");
                        TimeUnit.SECONDS.sleep(2);
                        clearScreen();
                        menuNumber = 16;
                        break;
                    }

                    if (makeSaleMenu == 1) {

                        while (makeOrderLoop2 == true) {
                            clearScreen();
                            System.out.println("Products IDs: " + sale.getProductsID());
                            System.out.println("Products amounts" + sale.amountOfProduct + "\n");
                            System.out.print("Enter product ID: ");

                            saleInput = input.nextLine().toLowerCase();

                            if (isNumeric(saleInput)) {
                                productID2 = Integer.parseInt(saleInput);
                            } else if (saleInput.toLowerCase().equals("end")) {
                                if (sale.products.size() >= 1) {
                                    System.out.println();
                                    System.out.print("Expected days to deliver: ");
                                    //proverka

                                    int daysToDeliver = 0;
                                    menuCheck = input.nextLine();

                                    if (isNumeric(menuCheck)) {
                                        daysToDeliver = Integer.parseInt(menuCheck);
                                    } else if (isNumeric(menuCheck) == false) {
                                        while (true) {
                                            System.out.println();
                                            System.out.print("Expected days to deliver: ");
                                            menuCheck = input.nextLine();

                                            if (isNumeric(menuCheck)) {
                                                daysToDeliver = Integer.parseInt(menuCheck);
                                                break;
                                            } else if (isNumeric(menuCheck) == false) {
                                            }
                                        }
                                    }
                                    connection.createSale(sale);
                                    pendingSales.add(sale);

                                    //dobavqne v log fail-a
                                    Calendar c = Calendar.getInstance();
                                    String saleAddDate = sdf.format(c.getTime());

                                    String info = "Sale ID: " + sale.getSaleID() + " || Created by user with ID: " + currentUserID +
                                            " || Date of creating " + saleAddDate + " || Sale cost: " + sale.getSaleCost() +
                                            /*" || Order expected delivery date: " + order.getDeliveryDate()*/
                                            " || Sold product/s ID/s " + sale.productsList + " || Amount of sold products " + sale.productsAmount + "\n";
                                    printToFile("D:\\InventoryManagementSys\\textfile/AddSaleLogFile.txt", info);
                                    clearScreen();
                                    menuNumber = 22;
                                    break;

                                } else if (sale.products.size() < 1) {
                                    System.out.println("\nSale is not created");
                                    TimeUnit.SECONDS.sleep(2);
                                    clearScreen();
                                    //makeOrderLoop=false;
                                    menuNumber = 22;
                                    break;
                                }
                            } else if (isNumeric(saleInput) == false) {
                                boolean check = false;

                                while (check == false) {
                                    System.out.print("Enter product ID: ");

                                    saleInput = input.nextLine().toLowerCase();

                                    if (isNumeric(saleInput)) {
                                        productID2 = Integer.parseInt(saleInput);
                                        check = true;
                                    } else if (saleInput.toLowerCase().equals("end")) {
                                        if (sale.products.size() >= 1) {
                                            System.out.println();
                                            System.out.print("Expected days to deliver: ");
                                            //proverka

                                            int daysToDeliver = 0;
                                            menuCheck = input.nextLine();

                                            if (isNumeric(menuCheck)) {
                                                daysToDeliver = Integer.parseInt(menuCheck);
                                            } else if (isNumeric(menuCheck) == false) {
                                                while (true) {
                                                    System.out.println();
                                                    System.out.print("Expected days to deliver: ");
                                                    menuCheck = input.nextLine();

                                                    if (isNumeric(menuCheck)) {
                                                        daysToDeliver = Integer.parseInt(menuCheck);
                                                        break;
                                                    } else if (isNumeric(menuCheck) == false) {
                                                    }
                                                }
                                            }

                                            connection.createSale(sale);
                                            pendingSales.add(sale);

                                            //dobavqne v log fail-a
                                            Calendar c = Calendar.getInstance();
                                            String saleAddDate = sdf.format(c.getTime());


                                            String info = "Sale ID: " + sale.getSaleID() + " || Created by user with ID: " + currentUserID +
                                                    " || Date of creating " + saleAddDate + " || Sale cost: " + sale.getSaleCost() +
                                                    /*" || Order expected delivery date: " + order.getDeliveryDate()*/
                                                    " || Sold product/s ID/s " + sale.productsList + " || Amount of sold products " + sale.productsAmount + "\n";
                                            printToFile("D:\\InventoryManagementSys\\textfile/AddSaleLogFile.txt", info);
                                            clearScreen();
                                            menuNumber = 22;
                                            break;

                                        } else if (sale.products.size() < 1) {
                                            System.out.println("\nOrder is not created");
                                            TimeUnit.SECONDS.sleep(2);
                                            clearScreen();
                                            makeOrderLoop = false;
                                            menuNumber = 22;
                                            break;
                                        }
                                    }
                                }
                                if (isNumeric(saleInput) == false) {
                                    break;
                                } else {

                                }
                            }

                            clearScreen();
                            System.out.println("Products IDs: " + sale.getProductsID());
                            System.out.println("Products amounts" + sale.amountOfProduct + "\n");
                            System.out.print("Enter product amount: ");

                            saleInput2 = input.nextLine().toLowerCase();

                            if (isNumeric(saleInput2)) {
                                amountOfProduct2 = Integer.parseInt(saleInput2);
                            } else if (saleInput2.toLowerCase().equals("end")) {
                                if (sale.products.size() >= 1) {
                                    System.out.println();
                                    System.out.print("Expected days to deliver: ");

                                    int daysToDeliver = 0;
                                    menuCheck = input.nextLine();

                                    if (isNumeric(menuCheck)) {
                                        daysToDeliver = Integer.parseInt(menuCheck);
                                    } else if (isNumeric(menuCheck) == false) {
                                        while (true) {
                                            System.out.println();
                                            System.out.print("Expected days to deliver: ");
                                            menuCheck = input.nextLine();

                                            if (isNumeric(menuCheck)) {
                                                daysToDeliver = Integer.parseInt(menuCheck);
                                                break;
                                            } else if (isNumeric(menuCheck) == false) {
                                            }
                                        }
                                    }
                                    connection.createSale(sale);
                                    pendingSales.add(sale);

                                    //dobavqne v log fail-a
                                    Calendar c = Calendar.getInstance();
                                    String saleAddDate = sdf.format(c.getTime());

                                    String info = "Sale ID: " + sale.getSaleID() + " || Created by user with ID: " + currentUserID +
                                            " || Date of creating " + saleAddDate + " || Sale cost: " + sale.getSaleCost() +
                                            /*" || Order expected delivery date: " + order.getDeliveryDate()*/
                                            " || Sold product/s ID/s " + sale.productsList + " || Amount of sold products " + sale.productsAmount + "\n";
                                    printToFile("D:\\InventoryManagementSys\\textfile/AddSaleLogFile.txt", info);
                                    clearScreen();
                                    menuNumber = 22;
                                    break;

                                } else if (sale.products.size() < 1) {
                                    System.out.println("\nOrder is not created");
                                    TimeUnit.SECONDS.sleep(2);
                                    clearScreen();
                                    menuNumber = 22;
                                    break;
                                }
                            } else if (isNumeric(saleInput2) == false) {
                                boolean check1 = false;

                                while (check1 == false) {
                                    System.out.print("Enter products amount : ");

                                    saleInput2 = input.nextLine().toLowerCase();

                                    if (isNumeric(saleInput2)) {
                                        amountOfProduct = Integer.parseInt(saleInput2);
                                        check1 = true;
                                    } else if (saleInput2.toLowerCase().equals("end")) {
                                        if (sale.products.size() >= 1) {
                                            System.out.println();
                                            System.out.print("Expected days to deliver: ");
                                            //proverka

                                            int daysToDeliver = 0;
                                            menuCheck = input.nextLine();

                                            if (isNumeric(menuCheck)) {
                                                daysToDeliver = Integer.parseInt(menuCheck);
                                            } else if (isNumeric(menuCheck) == false) {
                                                while (true) {
                                                    System.out.println();
                                                    System.out.print("Expected days to deliver: ");
                                                    menuCheck = input.nextLine();

                                                    if (isNumeric(menuCheck)) {
                                                        daysToDeliver = Integer.parseInt(menuCheck);
                                                        break;
                                                    } else if (isNumeric(menuCheck) == false) {
                                                    }
                                                }
                                            }

                                            connection.createSale(sale);
                                            pendingSales.add(sale);

                                            //dobavqne v log fail-a
                                            Calendar c = Calendar.getInstance();
                                            String saleAddDate = sdf.format(c.getTime());

                                            String info = "Sale ID: " + sale.getSaleID() + " || Created by user with ID: " + currentUserID +
                                                    " || Date of creating " + saleAddDate + " || Sale cost: " + sale.getSaleCost() +
                                                    /*" || Order expected delivery date: " + order.getDeliveryDate()*/
                                                    " || Sold product/s ID/s " + sale.productsList + " || Amount of sold products " + sale.productsAmount + "\n";
                                            printToFile("D:\\InventoryManagementSys\\textfile/AddSaleLogFile.txt", info);
                                            clearScreen();
                                            menuNumber = 22;
                                            break;

                                        } else if (sale.products.size() < 1) {
                                            System.out.println("\nOrder is not created");
                                            TimeUnit.SECONDS.sleep(2);
                                            clearScreen();
                                            makeOrderLoop = false;
                                            menuNumber = 22;
                                            break;
                                        }
                                    }
                                }
                                if (isNumeric(saleInput2) == false) {
                                    break;
                                } else {

                                }
                            }
                            //adding to order

                            if (isNumeric(saleInput) && isNumeric(saleInput2)) {
                                for (int i = 0; i < products.size(); i++) {
                                    if (products.get(i).getProductID() == productID2) {

                                        //System.out.println(i);
                                        sale.products.add(products.get(i));
                                        sale.amountOfProduct.add(amountOfProduct2);
                                        //System.out.println("added to the list");

                                        //System.out.println(sale.products.toString());
                                        //System.out.println(sale.amountOfProduct);

                                        productExist2 = true;
                                        break;
                                    } else if (products.get(i).getProductID() != productID2) {
                                        productExist2 = false;
                                    }
                                }
                                if (productExist2 == true) {
                                    //System.out.println("added to order");
                                    //System.out.println(order.getProductsList());


                                } else if (productExist2 != true) {
                                    System.out.println("\nThis product does not exist!");
                                    TimeUnit.SECONDS.sleep(2);
                                }
                            }
                        }
                    } else if (makeSaleMenu == 2) {
                        clearScreen();
                        menuNumber = 21;
                    } else {
                        System.out.println("\nInvalid option");
                        TimeUnit.SECONDS.sleep(2);
                        clearScreen();
                        menuNumber = 22;
                        break;
                    }
                    break;

                case 23:
                    //pending sales

                    //manage users
                    System.out.println("All pending sales: \n");

                    Double amountOfPages5 = pendingSales.size() / 3.0;

                    amountOfPages5 = Math.ceil(amountOfPages5);

                    System.out.printf("Amount of pages: " + String.format("%.0f", amountOfPages5) + "\n" + "\n");

                    if (amountOfPages5 == 0) {
                        System.out.println("Nothing to show\n");
                        System.out.print("1. Back\n\n");
                        System.out.print("Choose option: ");
                        menuCheck = input.nextLine();

                        if (isNumeric(menuCheck) == true) {
                            menuNumber = Integer.parseInt(menuCheck);
                            if (menuNumber == 1) {
                                page5 = 0;
                                clearScreen();
                                menuNumber = 21;
                                break;
                            } else {
                                System.out.println("\nInvalid option");
                                TimeUnit.SECONDS.sleep(2);
                                clearScreen();
                                menuNumber = 23;
                                break;
                            }
                        } else if (isNumeric(menuCheck) == false) {
                            System.out.println("\nInvalid input");
                            TimeUnit.SECONDS.sleep(2);
                            clearScreen();
                            menuNumber = 23;
                            break;
                        }

                    } else {

                        int chunkSize = 3;
                        AtomicInteger counter = new AtomicInteger();
                        final Collection<List<Sale>> partitionedList =
                                pendingSales.stream().collect(Collectors.groupingBy(i -> counter.getAndIncrement() / chunkSize))
                                        .values();

                        for (int i = 0; i < partitionedList.stream().skip(page5).findFirst().orElse(null).size(); i++) {
                            System.out.println(partitionedList.stream().skip(page5).findFirst().orElse(null).get(i));
                        }

                        if (amountOfPages5 == 1) {

                            System.out.print("\n1. Back\n\n");
                            System.out.print("Choose option: ");
                            menuCheck = input.nextLine();


                            if (isNumeric(menuCheck) == true) {
                                menuNumber = Integer.parseInt(menuCheck);
                                if (menuNumber == 1) {
                                    page5 = 0;
                                    clearScreen();
                                    menuNumber = 21;
                                    break;
                                } else {
                                    System.out.println("\nInvalid option");
                                    TimeUnit.SECONDS.sleep(2);
                                    clearScreen();
                                    menuNumber = 23;
                                    break;
                                }
                            } else if (isNumeric(menuCheck) == false) {
                                System.out.println("\nInvalid input");
                                TimeUnit.SECONDS.sleep(2);
                                clearScreen();
                                menuNumber = 23;
                                break;
                            }
                        }

                        if (page5 == 0) {

                            System.out.print("\n1. Back 2. Next Page\n\n");
                            System.out.print("Choose option: ");
                            menuCheck = input.nextLine();

                            if (isNumeric(menuCheck) == true) {
                                menuNumber = Integer.parseInt(menuCheck);
                                if (menuNumber == 1) {
                                    page5 = 0;
                                    clearScreen();
                                    menuNumber = 21;
                                    break;
                                } else if (menuNumber == 2) {
                                    clearScreen();
                                    menuNumber = 23;
                                    page5 += 1;
                                    break;
                                } else {
                                    System.out.println("\nInvalid option");
                                    TimeUnit.SECONDS.sleep(2);
                                    clearScreen();
                                    menuNumber = 23;
                                    break;
                                }
                            } else if (isNumeric(menuCheck) == false) {
                                System.out.println("\nInvalid input");
                                TimeUnit.SECONDS.sleep(2);
                                clearScreen();
                                menuNumber = 23;
                                break;
                            }

                        } else if (page5 > 0 && page5 < amountOfPages5 - 1) {

                            System.out.print("\n1. Back 2. Next Page 3. Back Page\n\n");
                            System.out.print("Choose option: ");
                            menuCheck = input.nextLine();

                            if (isNumeric(menuCheck) == true) {
                                menuNumber = Integer.parseInt(menuCheck);
                                if (menuNumber == 1) {
                                    page5 = 0;
                                    clearScreen();
                                    menuNumber = 21;
                                    break;
                                } else if (menuNumber == 2) {
                                    page5 += 1;
                                    clearScreen();
                                    menuNumber = 23;
                                    break;
                                } else if (menuNumber == 3) {
                                    page5 -= 1;
                                    clearScreen();
                                    menuNumber = 23;
                                    break;
                                } else {
                                    System.out.println("\nInvalid option");
                                    TimeUnit.SECONDS.sleep(2);
                                    clearScreen();
                                    menuNumber = 23;
                                    break;
                                }
                            } else if (isNumeric(menuCheck) == false) {
                                System.out.println("\nInvalid input");
                                TimeUnit.SECONDS.sleep(2);
                                clearScreen();
                                menuNumber = 23;
                                break;
                            } else {
                                break;
                            }

                        } else if (page5 == amountOfPages5 - 1) {

                            System.out.print("\n1. Back 2. Back Page\n\n");
                            System.out.print("Choose option: ");
                            menuCheck = input.nextLine();

                            if (isNumeric(menuCheck) == true) {
                                menuNumber = Integer.parseInt(menuCheck);
                                if (menuNumber == 1) {
                                    page5 = 0;
                                    clearScreen();
                                    menuNumber = 21;
                                    break;
                                } else if (menuNumber == 2) {
                                    page5 -= 1;
                                    clearScreen();
                                    menuNumber = 23;
                                    break;
                                } else {
                                    System.out.println("\nInvalid option");
                                    TimeUnit.SECONDS.sleep(2);
                                    clearScreen();
                                    menuNumber = 23;
                                    break;
                                }
                            } else if (isNumeric(menuCheck) == false) {
                                System.out.println("\nInvalid input");
                                TimeUnit.SECONDS.sleep(2);
                                clearScreen();
                                menuNumber = 23;
                                break;
                            }
                        }
                    }
                    break;


                case 24:
                    //complete sale
                    int completeSaleMenu = 1;
                    int completeSaleIndex = 0;
                    boolean saleMove = false;

                    clearScreen();

                    switch (completeSaleMenu) {

                        case 1:
                            System.out.println("Complete sale menu \n\n1. Continue  2. Back\n");
                            System.out.print("Choose option: ");

                            menuCheck = input.nextLine();

                            if (isNumeric(menuCheck)) {
                                completeSaleMenu = Integer.parseInt(menuCheck);
                            } else {
                                System.out.println("\nIncorrect input");
                                TimeUnit.SECONDS.sleep(2);
                                menuNumber = 24;
                                break;
                            }

                            if (completeSaleMenu == 1) {
                                clearScreen();
                                completeSaleMenu = 2;


                            } else if (completeSaleMenu == 2) {
                                clearScreen();
                                menuNumber = 15;
                                break;
                            } else {
                                System.out.println("\nIncorrect input");
                                TimeUnit.SECONDS.sleep(2);
                                menuNumber = 24;
                                break;
                            }

                            System.out.println(completeSaleMenu);

                        case 2:
                            clearScreen();

                            System.out.println("Enter the ID of the sale you want to complete\n\n");
                            System.out.print("ID: ");
                            int moveID = input.nextInt();


                            if (pendingSales.size() > 0) {
                                for (int i = 0; i < pendingSales.size(); i++) {
                                    if (pendingSales.get(i).getSaleID() == moveID) {
                                        completeSaleIndex = i;
                                        saleMove = true;
                                        break;
                                    } else if (pendingSales.get(i).getSaleID() != moveID) {
                                        saleMove = false;
                                    }
                                }

                                if (saleMove == true) {
                                    System.out.println("\nSale is moved");
                                    completeSaleMenu = 3;

                                } else if (saleMove == false) {
                                    System.out.println("\nSale does not exist");
                                    TimeUnit.SECONDS.sleep(2);
                                    menuNumber = 24;
                                    break;
                                }

                            } else {
                                System.out.println("\nNothing to complete");
                                TimeUnit.SECONDS.sleep(2);
                                menuNumber = 24;
                                break;
                            }

                        case 3:

                            System.out.println("Product ids");

                            ArrayList<String> productsListTemp = new ArrayList(Arrays.asList(pendingSales.get(completeSaleIndex).getProductsList().split(",")));
                            List<Integer> productsList = new ArrayList<Integer>();

                            productsListTemp.remove(0);

                            for (int x = 0; x < productsListTemp.size(); x++) {
                                productsList.add(Integer.parseInt(productsListTemp.get(x)));
                            }

                            System.out.println(productsList.toString());

                            System.out.println();
                            System.out.println("Product amount");

                            ArrayList<String> amountListTemp = new ArrayList(Arrays.asList(pendingSales.get(completeSaleIndex).getProductsAmount().split(",")));
                            List<Integer> amountList = new ArrayList<Integer>();

                            amountListTemp.remove(0);

                            for (int x = 0; x < amountListTemp.size(); x++) {
                                amountList.add(Integer.parseInt(amountListTemp.get(x)));
                            }

                            System.out.println(amountList.toString());

                            for (int n = 0; n < products.size(); n++) {
                                for (int x = 0; x < productsList.size(); x++) {

                                    if (products.get(n).getProductID() == productsList.get(x)) {

                                        System.out.println("Product id: " + products.get(n).getProductID() + "  amount :" + products.get(n).getProductQuantity());

                                        int newQuantity = products.get(n).getProductQuantity() - amountList.get(x);

                                        products.get(n).setProductQuantity(newQuantity);

                                        System.out.println("Product id: " + products.get(n).getProductID() + "  amount :" + products.get(n).getProductQuantity());

                                        String edit = String.valueOf(newQuantity);

                                        connection.userEdit("products", "ProductQuantity", edit, products.get(n).getProductID());

                                        //System.out.println(x);
                                        //System.out.println(n);
                                        // System.out.println();
                                    }
                                }
                            }

                            Calendar c = Calendar.getInstance();
                            String saleCompleteDate = sdf.format(c.getTime());

                            String info = "Sale ID: " + pendingSales.get(completeSaleIndex).getSaleID() + " || Completed by user with ID: " + currentUserID +
                                    " || Date of completing " + saleCompleteDate + " || Sale cost: " + pendingSales.get(completeSaleIndex).getSaleCost() +
                                    " || Sold product/s ID/s " + pendingSales.get(completeSaleIndex).productsList + " || Amount of sold products " + pendingSales.get(completeSaleIndex).productsAmount + "\n";
                            printToFile("D:\\InventoryManagementSys\\textfile/CompleteSaleLogFile.txt", info);


                            orderMove = true;
                            connection.addToCompletedSales(pendingSales.get(completeSaleIndex));
                            connection.removeSaleFromPending(pendingSales.get(completeSaleIndex).getSaleID());

                            completedSales.add(pendingSales.get(completeSaleIndex));
                            pendingSales.remove(completeSaleIndex);

                            System.out.println("\nPress ENTER to continue");
                            input.nextLine();
                            String xxx = input.nextLine();
                    }
                    break;

                case 25:
                    System.out.println("All completed sales: \n");

                    Double amountOfPages6 = completedSales.size() / 3.0;

                    amountOfPages6 = Math.ceil(amountOfPages6);

                    System.out.printf("Amount of pages: " + String.format("%.0f", amountOfPages6) + "\n" + "\n");

                    if (amountOfPages6 == 0) {
                        System.out.println("Nothing to show\n");
                        System.out.print("1. Back\n\n");
                        System.out.print("Choose option: ");
                        menuCheck = input.nextLine();

                        if (isNumeric(menuCheck) == true) {
                            menuNumber = Integer.parseInt(menuCheck);
                            if (menuNumber == 1) {
                                page6 = 0;
                                clearScreen();
                                menuNumber = 21;
                                break;
                            } else {
                                System.out.println("\nInvalid option");
                                TimeUnit.SECONDS.sleep(2);
                                clearScreen();
                                menuNumber = 25;
                                break;
                            }
                        } else if (isNumeric(menuCheck) == false) {
                            System.out.println("\nInvalid input");
                            TimeUnit.SECONDS.sleep(2);
                            clearScreen();
                            menuNumber = 25;
                            break;
                        }

                    } else {

                        int chunkSize = 3;
                        AtomicInteger counter = new AtomicInteger();
                        final Collection<List<Sale>> partitionedList =
                                completedSales.stream().collect(Collectors.groupingBy(i -> counter.getAndIncrement() / chunkSize))
                                        .values();

                        for (int i = 0; i < partitionedList.stream().skip(page6).findFirst().orElse(null).size(); i++) {
                            System.out.println(partitionedList.stream().skip(page6).findFirst().orElse(null).get(i));
                        }

                        if (amountOfPages6 == 1) {

                            System.out.print("\n1. Back\n\n");
                            System.out.print("Choose option: ");
                            menuCheck = input.nextLine();


                            if (isNumeric(menuCheck) == true) {
                                menuNumber = Integer.parseInt(menuCheck);
                                if (menuNumber == 1) {
                                    page6 = 0;
                                    clearScreen();
                                    menuNumber = 21;
                                    break;
                                } else {
                                    System.out.println("\nInvalid option");
                                    TimeUnit.SECONDS.sleep(2);
                                    clearScreen();
                                    menuNumber = 25;
                                    break;
                                }
                            } else if (isNumeric(menuCheck) == false) {
                                System.out.println("\nInvalid input");
                                TimeUnit.SECONDS.sleep(2);
                                clearScreen();
                                menuNumber = 25;
                                break;
                            }
                        }

                        if (page6 == 0) {

                            System.out.print("\n1. Back 2. Next Page\n\n");
                            System.out.print("Choose option: ");
                            menuCheck = input.nextLine();

                            if (isNumeric(menuCheck) == true) {
                                menuNumber = Integer.parseInt(menuCheck);
                                if (menuNumber == 1) {
                                    page6 = 0;
                                    clearScreen();
                                    menuNumber = 21;
                                    break;
                                } else if (menuNumber == 2) {
                                    clearScreen();
                                    menuNumber = 25;
                                    page6 += 1;
                                    break;
                                } else {
                                    System.out.println("\nInvalid option");
                                    TimeUnit.SECONDS.sleep(2);
                                    clearScreen();
                                    menuNumber = 25;
                                    break;
                                }
                            } else if (isNumeric(menuCheck) == false) {
                                System.out.println("\nInvalid input");
                                TimeUnit.SECONDS.sleep(2);
                                clearScreen();
                                menuNumber = 25;
                                break;
                            }

                        } else if (page6 > 0 && page6 < amountOfPages6 - 1) {

                            System.out.print("\n1. Back 2. Next Page 3. Back Page\n\n");
                            System.out.print("Choose option: ");
                            menuCheck = input.nextLine();

                            if (isNumeric(menuCheck) == true) {
                                menuNumber = Integer.parseInt(menuCheck);
                                if (menuNumber == 1) {
                                    page6 = 0;
                                    clearScreen();
                                    menuNumber = 21;
                                    break;
                                } else if (menuNumber == 2) {
                                    page6 += 1;
                                    clearScreen();
                                    menuNumber = 25;
                                    break;
                                } else if (menuNumber == 3) {
                                    page6 -= 1;
                                    clearScreen();
                                    menuNumber = 25;
                                    break;
                                } else {
                                    System.out.println("\nInvalid option");
                                    TimeUnit.SECONDS.sleep(2);
                                    clearScreen();
                                    menuNumber = 25;
                                    break;
                                }
                            } else if (isNumeric(menuCheck) == false) {
                                System.out.println("\nInvalid input");
                                TimeUnit.SECONDS.sleep(2);
                                clearScreen();
                                menuNumber = 25;
                                break;
                            } else {
                                break;
                            }

                        } else if (page6 == amountOfPages6 - 1) {

                            System.out.print("\n1. Back 2. Back Page\n\n");
                            System.out.print("Choose option: ");
                            menuCheck = input.nextLine();

                            if (isNumeric(menuCheck) == true) {
                                menuNumber = Integer.parseInt(menuCheck);
                                if (menuNumber == 1) {
                                    page6 = 0;
                                    clearScreen();
                                    menuNumber = 21;
                                    break;
                                } else if (menuNumber == 2) {
                                    page6 -= 1;
                                    clearScreen();
                                    menuNumber = 25;
                                } else {
                                    System.out.println("\nInvalid option");
                                    TimeUnit.SECONDS.sleep(2);
                                    clearScreen();
                                    menuNumber = 25;
                                    break;
                                }
                            } else if (isNumeric(menuCheck) == false) {
                                System.out.println("\nInvalid input");
                                TimeUnit.SECONDS.sleep(2);
                                clearScreen();
                                menuNumber = 25;
                                break;

                            }
                        }
                    }

                    break;

                case 26:
                    //remove from pendingSales

                    int removeFromPendingMenu2;
                    boolean removeOrderResult2 = false;
                    System.out.println("Remove from pending sales \n\n1. Countine 2.Back\n");
                    System.out.print("Choose option: ");

                    menuCheck = input.nextLine();

                    if (isNumeric(menuCheck) == true) {
                        removeFromPendingMenu2 = Integer.parseInt(menuCheck);
                        if (removeFromPendingMenu2 == 1) {
                            clearScreen();
                            System.out.print("Enter the sale id to remove");
                            System.out.print("\n\nID: ");
                            int removeSaleId;

                            menuCheck = input.nextLine();

                            if (isNumeric(menuCheck) == true) {

                                removeSaleId = Integer.parseInt(menuCheck);
                                if (pendingSales.size() > 0) {
                                    for (int i = 0; i < pendingSales.size(); i++) {


                                        if (removeSaleId == pendingSales.get(i).getSaleID()) {

                                            Calendar c = Calendar.getInstance();
                                            String saleRemoveDate = sdf.format(c.getTime());

                                            String info = "Sale ID: " + pendingSales.get(i).getSaleID() + " || Removed from pending by user with ID: " + currentUserID +
                                                    " || Date of removing " + saleRemoveDate + " || Sale cost: " + pendingSales.get(i).getSaleCost() +
                                                    " || Sold product/s ID/s " + pendingSales.get(i).productsList + " || Amount of sold products " + pendingSales.get(i).productsAmount + "\n";
                                            printToFile("D:\\InventoryManagementSys\\textfile/RemoveFromPendingSaleLogFile.txt", info);

                                            connection.removeSaleFromPending(pendingSales.get(i).getSaleID());
                                            pendingOrders.remove(i);
                                            removeOrderResult2 = true;
                                            break;
                                        }
                                        if (removeSaleId != pendingSales.get(i).getSaleID()) {
                                            removeOrderResult2 = false;

                                        }

                                    }
                                    if (removeOrderResult2 == true) {
                                        System.out.println("\nSale was removed");
                                        TimeUnit.SECONDS.sleep(2);
                                        clearScreen();
                                        break;
                                    } else if (removeOrderResult2 == false) {
                                        System.out.println("\nSale was not removed");
                                        TimeUnit.SECONDS.sleep(2);
                                        clearScreen();
                                        break;

                                    }
                                } else {

                                    System.out.println("\nNo pending sales");
                                    TimeUnit.SECONDS.sleep(2);
                                    clearScreen();
                                    menuNumber = 25;
                                    break;
                                }
                            } else {
                                System.out.println("\nInvalid input");
                                TimeUnit.SECONDS.sleep(2);
                                clearScreen();
                                menuNumber = 26;
                            }
                        } else if (removeFromPendingMenu2 == 2) {
                            clearScreen();
                            menuNumber = 21;
                            break;
                        } else {
                            System.out.println("\nInvalid option");
                            TimeUnit.SECONDS.sleep(2);
                            clearScreen();
                            menuNumber = 26;
                        }
                    } else {
                        System.out.println("\nInvalid input");
                        TimeUnit.SECONDS.sleep(2);
                        clearScreen();
                        menuNumber = 26;
                    }
                    break;
            }
        }
    }

    public final static void clearScreen() throws IOException, InterruptedException {
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
    }

    public static void dataSyncUser(ArrayList<User> users) throws CustomException {
        try {

            final String secretKey = "ssshhhhhhhhhhh!!!!";

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connect = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/inventorymanagementsystem", "root", "");

            String query = "SELECT * FROM users";
            Statement st = connect.createStatement();

            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {

                String firstName = decrypt( rs.getString("FirstName"),secretKey);
                String lastName = decrypt(rs.getString("LastName"), secretKey);
                String username = decrypt(rs.getString("Username"), secretKey);
                String password = decrypt(rs.getString("Password"), secretKey);
                String phoneNumber = decrypt(rs.getString("PhoneNumber"), secretKey);
                Integer levelOfAccess = rs.getInt("LevelOfAccess");
                Integer id = rs.getInt("ID");

                DBconnection connection = new DBconnection();

                User b = new User(firstName, lastName, username, password, phoneNumber, levelOfAccess);
                b.setuserID(id);

                connection.usersConnection(b);

                Statement query1 = connect.createStatement();
                query1.executeUpdate("delete from users");



                for (int i = 0; i < users.size(); i++) {
                    connection.usersConnection(users.get(i));


                    connection.userEdit("users", "FirstName",encrypt(users.get(i).getFirstName(),secretKey) ,users.get(i).getuserID());
                    connection.userEdit("users", "LastName",encrypt(users.get(i).getLastName(),secretKey) ,users.get(i).getuserID());
                    connection.userEdit("users", "Username",encrypt(users.get(i).getUserName(),secretKey) ,users.get(i).getuserID());
                    connection.userEdit("users", "Password",encrypt(users.get(i).getPassword(),secretKey) ,users.get(i).getuserID());
                    connection.userEdit("users", "PhoneNumber",encrypt(users.get(i).getPhoneNumber(),secretKey) ,users.get(i).getuserID());
                }
            }
            st.close();
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }

    public static void dataSyncProducts(ArrayList<Product> products) throws CustomException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connect = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/inventorymanagementsystem", "root", "");

            String query = "SELECT * FROM products";
            Statement st = connect.createStatement();
            DBconnection connection = new DBconnection();

            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {

                String productName = rs.getString("ProductName");
                Integer productQuantity = rs.getInt("ProductQuantity");
                String productDescription = rs.getString("ProductDescription");
                Double productPrice = rs.getDouble("ProductPrice");
                Double productSalePrice = rs.getDouble("ProductSalePrice");
                String productCategoty = rs.getString("ProductCategory");
                Integer id = rs.getInt("ID");

                Product b = new Product(productName, productQuantity, productDescription, productPrice, productSalePrice, productCategoty);
                b.setProductID(id);
                connection.productsConnection(b);



                Statement query1 = connect.createStatement();
                query1.executeUpdate("delete from products");

                //DBconnection connection = new DBconnection();

                for (int i = 0; i < products.size(); i++) {
                    connection.productsConnection(products.get(i));
                }
            }
            st.close();
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }

    public static void dataSyncPendingOrders(ArrayList<Order> pendingOrders) throws CustomException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connect = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/inventorymanagementsystem", "root", "");

            String query = "SELECT * FROM pendingOrders";
            Statement st = connect.createStatement();

            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {

                String dateOfOrder = rs.getString("DateOfOrder");
                String productsID = rs.getString("ProductsID");
                String productsAmount = rs.getString("ProductsAmount");
                Double orderCost = rs.getDouble("OrderCost");
                String еxpectedDeliveryDate = rs.getString("ExpectedDeliveryDate");
                Integer orderID = rs.getInt("OrderID");


                Order b = new Order();
                b.setNewDate(dateOfOrder);
                b.setProductsList(productsID);
                b.setProductsAmount(productsAmount);
                b.setOrderCost(orderCost);
                b.setDeliveryDate(еxpectedDeliveryDate);
                b.setOrderID(orderID);

                AllData.pendingOrders.add(b);
            }

            Statement query1 = connect.createStatement();
            query1.executeUpdate("delete from pendingOrders");

            DBconnection connection = new DBconnection();

            for (int i = 0; i < pendingOrders.size(); i++) {
                connection.createOrder(pendingOrders.get(i));
            }

            st.close();
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }


    }

    public static void dataSyncCompeltedOrders(ArrayList<Order> completedOrders) throws CustomException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connect = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/inventorymanagementsystem", "root", "");

            String query = "SELECT * FROM completedOrders";
            Statement st = connect.createStatement();

            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {

                String dateOfOrder = rs.getString("DateOfOrder");
                String productsID = rs.getString("ProductsID");
                String productsAmount = rs.getString("ProductsAmount");
                Double orderCost = rs.getDouble("OrderCost");
                String еxpectedDeliveryDate = rs.getString("ExpectedDeliveryDate");
                Integer orderID = rs.getInt("OrderID");

                Order b = new Order();
                b.setNewDate(dateOfOrder);
                b.setProductsList(productsID);
                b.setProductsAmount(productsAmount);
                b.setOrderCost(orderCost);
                b.setDeliveryDate(еxpectedDeliveryDate);
                b.setOrderID(orderID);

                completedOrders.add(b);
            }

            Statement query1 = connect.createStatement();

            query1.executeUpdate("delete from completedOrders");

            DBconnection connection = new DBconnection();

            for (int i = 0; i < completedOrders.size(); i++) {
                connection.addToCompletedOrders(completedOrders.get(i));
            }

            st.close();
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }

    public static void dataSyncPendingSales(ArrayList<Sale> pendingSales) throws CustomException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connect = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/inventorymanagementsystem", "root", "");

            String query = "SELECT * FROM pendingSales";
            Statement st = connect.createStatement();

            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {

                String dateOfOrder = rs.getString("DateOfSale");
                String productsID = rs.getString("ProductsID");
                String productsAmount = rs.getString("ProductsAmount");
                Double saleCost = rs.getDouble("SaleCost");
                Integer saleID = rs.getInt("SaleID");

                Sale b = new Sale();

                b.setNewDate(dateOfOrder);
                b.setProductsList(productsID);
                b.setProductsAmount(productsAmount);
                b.setSaleCost(saleCost);
                b.setSaleID(saleID);

                pendingSales.add(b);
            }
            Statement query1 = connect.createStatement();

            query1.executeUpdate("delete from pendingSales");

            DBconnection connection = new DBconnection();

            for (int i = 0; i < pendingSales.size(); i++) {
                connection.createSale(pendingSales.get(i));
            }

            st.close();
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }

    public static void dataSyncCompletedSales(ArrayList<Sale> completedSales) throws CustomException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connect = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/inventorymanagementsystem", "root", "");

            String query = "SELECT * FROM completedSales";
            Statement st = connect.createStatement();

            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {

                String dateOfOrder = rs.getString("DateOfSale");
                String productsID = rs.getString("ProductsID");
                String productsAmount = rs.getString("ProductsAmount");
                Double saleCost = rs.getDouble("SaleCost");
                Integer saleID = rs.getInt("SaleID");

                Sale b = new Sale();

                b.setNewDate(dateOfOrder);
                b.setProductsList(productsID);
                b.setProductsAmount(productsAmount);
                b.setSaleCost(saleCost);
                b.setSaleID(saleID);

                completedSales.add(b);
            }
            Statement query1 = connect.createStatement();

            query1.executeUpdate("delete from completedSales");

            DBconnection connection = new DBconnection();

            for (int i = 0; i < completedSales.size(); i++) {
                connection.addToCompletedSales(completedSales.get(i));
            }

            st.close();
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }

    public static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static void printToFile(String fileName, String text) {
        try {
            String filename = fileName;
            FileWriter fw = new FileWriter(filename, true); //the true will append the new data
            fw.write(text);//appends the string to the file
            fw.close();
        } catch (IOException ioe) {
            System.err.println("IOException: " + ioe.getMessage());
        }
    }

    }





