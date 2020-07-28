<?php 
session_start();
if(empty($_SESSION['admin'])){
	header("location:login");
}

if(isset($_POST['save'])){
	include 'assets/database/db.php';

	$con->query("INSERT INTO trending VALUES (default,'$_POST[teks_trending]','$_POST[biro_unit]')");

	echo "<script> alert('Data Trending Tersimpan'); </script>";
	echo "<script> location='trending'; </script>";
}
?>