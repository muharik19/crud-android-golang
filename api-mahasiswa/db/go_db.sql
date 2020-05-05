-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 05, 2020 at 03:30 PM
-- Server version: 10.4.11-MariaDB
-- PHP Version: 7.4.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `go_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `tbl_mahasiswa`
--

CREATE TABLE `tbl_mahasiswa` (
  `id` int(11) NOT NULL,
  `nama` varchar(150) DEFAULT NULL,
  `photo` varchar(255) DEFAULT NULL,
  `nim` varchar(20) DEFAULT NULL,
  `jurusan` varchar(150) DEFAULT NULL,
  `no_hp` varchar(13) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `tbl_mahasiswa`
--

INSERT INTO `tbl_mahasiswa` (`id`, `nama`, `photo`, `nim`, `jurusan`, `no_hp`) VALUES
(1, 'Ahmad Muharik', 'http://192.168.43.237:83/unpam/v1/images/20200505195143cropped8826569138148907602.jpg', '2011140023', 'Teknik Informatika', '6287772488816'),
(2, 'Doraemon', 'http://192.168.43.237:83/unpam/v1/images/20200505195344cropped4385767898721845537.jpg', '2010578495', 'Teknik Sastra Jepang', '6285687541380'),
(3, 'Konoha Maru', 'http://192.168.43.237:83/unpam/v1/images/20200505195443cropped7943072380772325172.jpg', '2018546708', 'Teknik Ninja', '6285754231079'),
(4, 'Hinata', 'http://192.168.43.237:83/unpam/v1/images/20200505195552cropped8309469995534444598.jpg', '2013457849', 'Management Informatika', '6285854648751'),
(6, 'Sarah Mara', 'http://192.168.43.237:83/unpam/v1/images/20200505202104cropped598403333494231879.jpg', '2011150026', 'Sastra Indonesia', '6285687543129');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `tbl_mahasiswa`
--
ALTER TABLE `tbl_mahasiswa`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `tbl_mahasiswa`
--
ALTER TABLE `tbl_mahasiswa`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
