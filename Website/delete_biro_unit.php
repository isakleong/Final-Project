<?php 
session_start();
if(empty($_SESSION['admin'])){
	header("location:login");
}

include 'assets/database/db.php';

$con->query("DELETE from user_biro_unit where id_user_biro_unit='$_GET[id]'");

echo "<script> alert('Data Biro Unit Berhasil Dihapus'); </script>";
echo "<script> location='biro_unit'; </script>";
?>