import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User {
   private String firstName;
   private String lastName;
   private String userName;
   private String password;
   private String phoneNumber;
   private int levelOfAccess;
   private int userID;

   Scanner input = new Scanner(System.in);

   public User(String firstName, String lastName, String userName, String password, String phoneNumber, int levelOfAccess) throws CustomException {
      this.setFirstName(firstName); //ima proverka
      this.setLastName(lastName);//ima proverka
      this.setUserName(userName); //ima proverka
      this.setPassword(password);//ima proverka
      this.setPhoneNumber(phoneNumber); //ima proverka
      this.setLevelOfAccess(levelOfAccess);


      AllData.users.add(this);
      setuserID(AllData.users.size());

      DBconnection connection = new DBconnection();
      //connection.usersConnection(this);
   }

   public static boolean
   isValidCheck(String checkString, String regex) {


      // Compile the ReGex
      Pattern p = Pattern.compile(regex);

      // If the password is empty
      // return false
      if (checkString == null) {
         return false;
      }

      // Pattern class contains matcher() method
      // to find matching between given password
      // and regular expression.
      Matcher m = p.matcher(checkString);

      // Return if the password
      // matched the ReGex
      return m.matches();
   }

   public String getFirstName() {
      return firstName;
   }

   public void setFirstName(String firstName){

      String regex = "(?i)"
              + "(?=.*[a-z])"
              + "(?=\\S+$).{1,100}$";

      this.firstName = firstName;

      while (isValidCheck(firstName, regex) == false) {
         System.out.println();
         System.out.println("Incorrect first name");
         System.out.print("First name: ");
         firstName = input.nextLine();
      }
      this.firstName = firstName;
   }

   public void setFirstName2(String firstName){
      this.firstName = firstName;
   }

   public String getLastName() {
      return lastName;
   }

   public void setLastName(String lastName){

      String regex = "(?i)"
              + "(?=.*[a-z])"
              + "(?=\\S+$).{1,100}$";

      this.lastName = lastName;

      while (isValidCheck(lastName, regex) == false) {
            System.out.println();
         System.out.println("Incorrect last name");
            System.out.print("Last name: ");
            lastName = input.nextLine();
         }


            this.lastName = lastName;

      }

   public void setLastName2(String lastName){
      this.lastName = lastName;

   }

   public String getUserName() {
      return userName;
   }

   public void setUserName(String userName){

      String regex = "(?i)"
              + "(?=.*[a-z])"
              + "(?=\\S+$).{4,100}$";

      this.userName = userName;

      while (isValidCheck(userName, regex) == false) {
         System.out.println();
         System.out.println("Incorrect username");
         System.out.print("Username: ");
         userName = input.nextLine();
      }
      this.userName = userName;
   }

   public void setUserName2(String userName){
      this.userName = userName;
   }

   public String getPassword() {
      return password;
   }

   public void setPassword(String password) {

      String regex = "(?i)" + "^(?=.*[0-9])"
              + "(?=.*[a-z])"
              + "(?=\\S+$).{4,100}$";

      this.password = password;

      while (isValidCheck(password, regex) == false) {
         System.out.println();
         System.out.println("Incorrect password");
         System.out.print("Password: ");
         password= input.nextLine();
      }
      this.password = password;
   }

   public void setPassword2(String password) {
      this.password = password;
   }

   public String getPhoneNumber() {
      return phoneNumber;

   }

   public void setPhoneNumber(String phoneNumber) {

      String regex = "^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$";

      this.phoneNumber = phoneNumber;

      while (isValidCheck(phoneNumber, regex) == false) {
         System.out.println();
         System.out.println("Incorrect phone number");
         System.out.print("Phone number:");
         phoneNumber= input.nextLine();
      }
      this.phoneNumber = phoneNumber;

   }

   public void setPhoneNumber2(String phoneNumber) {
      this.phoneNumber = phoneNumber;

   }

   public int getLevelOfAccess() {
      return levelOfAccess;
   }

   public void setLevelOfAccess(int levelOfAccess) {

      this.levelOfAccess = levelOfAccess;

      while (levelOfAccess <1 || levelOfAccess>=4){
         System.out.println();
         System.out.print("Access level: ");
         levelOfAccess= input.nextInt();
      }
      this.levelOfAccess = levelOfAccess;
   }

   public void setLevelOfAccess2(int levelOfAccess) {
      this.levelOfAccess = levelOfAccess;
   }



   public int getuserID(){
      return userID;
   }

   public void setuserID(int userID) {
      this.userID = userID;
   }

   @Override
   public String toString() {
      return "User: " +
              "First Name: " + firstName  +
              ", Last Name: " + lastName  +
              ", Username: " + userName +
              ", Password: " + password  +
              ", Phone number: " + phoneNumber  +
              ", Level of access: " + levelOfAccess +
              ", ID: " + userID;
   }
}
