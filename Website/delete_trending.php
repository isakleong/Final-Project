<?php 
session_start();
if(empty($_SESSION['admin'])){
	header("location:login");
}

include 'assets/database/db.php';

$con->query("DELETE from trending where id_trending='$_GET[id]'");

echo "<script> alert('Data Trending Berhasil Dihapus'); </script>";
echo "<script> location='trending'; </script>";
?>