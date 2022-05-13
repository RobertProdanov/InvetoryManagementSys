-- phpMyAdmin SQL Dump
-- version 5.1.0
-- https://www.phpmyadmin.net/
--
-- Хост: 127.0.0.1
-- Време на генериране: 13 май 2022 в 18:03
-- Версия на сървъра: 10.4.18-MariaDB
-- Версия на PHP: 8.0.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- База данни: `inventorymanagementsystem`
--

-- --------------------------------------------------------

--
-- Структура на таблица `completedorders`
--

CREATE TABLE `completedorders` (
  `DateOfOrder` varchar(255) NOT NULL,
  `ProductsID` varchar(255) NOT NULL,
  `ProductsAmount` varchar(255) NOT NULL,
  `OrderCost` double NOT NULL,
  `ExpectedDeliveryDate` varchar(255) NOT NULL,
  `OrderID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Структура на таблица `completedsales`
--

CREATE TABLE `completedsales` (
  `DateOfSale` varchar(255) NOT NULL,
  `ProductsID` varchar(255) NOT NULL,
  `ProductsAmount` varchar(255) NOT NULL,
  `SaleCost` double NOT NULL,
  `SaleID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Структура на таблица `lastid`
--

CREATE TABLE `lastid` (
  `Object` varchar(255) NOT NULL,
  `LastID` int(11) NOT NULL,
  `ID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Схема на данните от таблица `lastid`
--

INSERT INTO `lastid` (`Object`, `LastID`, `ID`) VALUES
('LastUserID', 9, 1),
('LastProductID', 2, 2),
('LastOrderID', 6, 3),
('LastSaleID', 4, 4);

-- --------------------------------------------------------

--
-- Структура на таблица `pendingorders`
--

CREATE TABLE `pendingorders` (
  `DateOfOrder` varchar(255) NOT NULL,
  `ProductsID` varchar(255) NOT NULL,
  `ProductsAmount` varchar(255) NOT NULL,
  `OrderCost` double NOT NULL,
  `ExpectedDeliveryDate` varchar(255) NOT NULL,
  `OrderID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Схема на данните от таблица `pendingorders`
--

INSERT INTO `pendingorders` (`DateOfOrder`, `ProductsID`, `ProductsAmount`, `OrderCost`, `ExpectedDeliveryDate`, `OrderID`) VALUES
('11/05/2022 14:24', ',1', ',2', 1.78, '16/05/2022 14:24', 1),
('11/05/2022 14:26', ',1,2', ',5,8', 44.45, '16/05/2022 14:26', 1),
('11/05/2022 14:27', ',2,1', ',6,99', 118.11, '16/05/2022 14:27', 3),
('11/05/2022 14:27', ',1', ',1', 0.89, '15/05/2022 14:27', 4),
('11/05/2022 14:28', ',1', ',4', 3.56, '15/05/2022 14:28', 5);

-- --------------------------------------------------------

--
-- Структура на таблица `pendingsales`
--

CREATE TABLE `pendingsales` (
  `DateOfSale` varchar(255) NOT NULL,
  `ProductsID` varchar(255) NOT NULL,
  `ProductsAmount` varchar(255) NOT NULL,
  `SaleCost` double NOT NULL,
  `SaleID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Схема на данните от таблица `pendingsales`
--

INSERT INTO `pendingsales` (`DateOfSale`, `ProductsID`, `ProductsAmount`, `SaleCost`, `SaleID`) VALUES
('13/05/2022 13:01', ',2', ',1', 5, 3);

-- --------------------------------------------------------

--
-- Структура на таблица `products`
--

CREATE TABLE `products` (
  `ProductName` varchar(255) NOT NULL,
  `ProductQuantity` int(11) NOT NULL,
  `ProductDescription` varchar(255) NOT NULL,
  `ProductPrice` double NOT NULL,
  `ProductSalePrice` double NOT NULL,
  `ProductCategory` varchar(255) NOT NULL,
  `ID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Схема на данните от таблица `products`
--

INSERT INTO `products` (`ProductName`, `ProductQuantity`, `ProductDescription`, `ProductPrice`, `ProductSalePrice`, `ProductCategory`, `ID`) VALUES
('Koka kola 500ml', 12, 'gazirano', 0.89, 1.35, 'gaziranol', 1),
('Koka kola 500ml', 12, 'gazirano', 0.89, 1.35, 'gaziranol', 1),
('wg', 12, 'ggg', 5, 10, 'wg', 2);

-- --------------------------------------------------------

--
-- Структура на таблица `users`
--

CREATE TABLE `users` (
  `FirstName` varchar(255) NOT NULL,
  `LastName` varchar(255) NOT NULL,
  `Username` varchar(255) NOT NULL,
  `Password` varchar(255) NOT NULL,
  `PhoneNumber` varchar(255) NOT NULL,
  `LevelOfAccess` int(11) NOT NULL,
  `ID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Схема на данните от таблица `users`
--

INSERT INTO `users` (`FirstName`, `LastName`, `Username`, `Password`, `PhoneNumber`, `LevelOfAccess`, `ID`) VALUES
('B3Jyi6Cvq3a6Xc4utNmBhw==', 'oKNcJ3rRDas2ycIPZlSnoQ==', 'mHAahNKa+FFH8T0CqKtqXA==', 'qPVrFSHt0L7FEqVfcVypDw==', 'A8C9aAUvhBagqjpIlIXwMA==', 3, 1),
('dO4qW/9cX22gM67c/NTzrg==', 'kkoqIC7iF8FFdmV9qbMvSQ==', 'OW+ou+kMBOJiEuDXOHVDtQ==', '9GYtFF+0is5w4asvsTxvHg==', 'A8C9aAUvhBagqjpIlIXwMA==', 3, 7),
('dO4qW/9cX22gM67c/NTzrg==', 'dO4qW/9cX22gM67c/NTzrg==', 'iTMdlObmvKn3hO3dtcQ8ww==', 'tuz2rbIg+igV78/amRP4PQ==', 'E95+ATrzYhXzD0qGDgAJfw==', 2, 8);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
