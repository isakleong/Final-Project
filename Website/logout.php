<?php 
include 'assets/database/db.php';
session_start();
session_destroy();
echo "<script> alert('You have been logout'); </script>";
echo "<script> location='login.php'; </script>";
?>