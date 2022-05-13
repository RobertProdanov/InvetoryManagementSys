# InvetoryManagementSys

##Български език

За да се стартира проекта са нужни: XAAMP(за базата данни), MySQL Java connector(.jar файл, отговорен за връзката между базата данни и Java програмата) и IDE по избор.

След като всичко нужно е инсталирано и подготвено, първото нещо което трябва да направим е стартирането на phpMyAdmin, където ще бъда съхранявана базата данни. Това се прави като отворим XAAMP и стартираме Apache и MySQL сървири. След това в браузър по желание въвеждаме следния линк: http://localhost/phpmyadmin/index.php ,който ни отвежда в phpMyAdmin, където създаваме база данни с името "inventorymanagementsystem", която съдържа следните таблици:

- users - Полета в нея: FirstName, LastName, Username, Password, PhoneNumber, LevelOfAccess, ID 

- products - Полета в нея: ProductName, ProductQuantity, ProductDescription, ProductPrice, ProductSalePrice, ProductCategory, ID

- pendingOrders - Полета в нея: DateOfOrder, ProductsID, ProductsAmount, OrderCost, ExpectedDeliveryDate, OrderID 

- pendingSales - Полета в нея: DateOfSale, ProductsID, ProductsAmount, SaleCost, SaleID

- completedOrders - Полета в нея: DateOfOrder, ProductsID, ProductsAmount, OrderCost, ExpectedDeliveryDate, OrderID 

- completedSales - Полета в нея: DateOfSale, ProductsID, ProductsAmount, SaleCost, SaleID

- lastID - Полета в нея: Object, LastID, ID

  В тази таблица трябва да добавим предварително информацията:

Това става чрез следната SQL заявка: INSERT INTO `lastid` (`Object`, `LastID`, `ID`) VALUES ('LastUserID', 0, 1),('LastProductID', 0, 2),('LastOrderID', 0, 3),
('LastSaleID', 0, 4); COMMIT;



След като създадем един от основните компоненти на програмата, следва нейното стартиране, което е малко по-особено. За да изпозваме пълноценно програмата ни трябва потребителски профил, който да трябва да бъде по специфичен начин, поради криптирането на таблицата с потребители. За да го направим, трябва да отворим програмата през IDE по избор. След това правим промяна в кода(ред 93) int menuNumber = 1; =>>> int menuNumber = 2;. По този начин прескачаме Login часта и директно можем да добавим потребител: Users ==> Add User. След като вече имаме потребителски профил в базата данни, връщаме стойната на menuNumber на 1, след което можем да стартираме програмата правилно, за да ползваме всички нейни функции.
Отваряме cmd(или друг терминал по избор) и навигираме до директорията на програмата чрез командата -cd-. След отворим папката на програмата, трябва да компилираме java кода. Това става с командата -javac -cp D:\InventoryManagementSys\.idea\libraries\mysql-connector-java-8.0.23.jar;. java- като тук е довавена локацията на .jar файла, Който прави връзката между програмата и базата данни. След като компилираме успешно кода стартираме програмата. Това става с командата java -cp D:\InventoryManagementSys\.idea\libraries\mysql-connector-java-8.0.23.jar;. App.








