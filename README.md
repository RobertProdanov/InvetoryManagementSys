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



След като създадем един от основните компоненти на програмата, следва нейното стартиране, което е малко по-особено.






