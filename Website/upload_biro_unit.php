<?php 
session_start();
if(empty($_SESSION['admin'])){
	header("location:login");
}

if(isset($_POST['save'])){
	include 'assets/database/db.php';

	$file = $_FILES['file'];
    $fileName = $_FILES['file']['name'];
    $fileTmpName = $_FILES['file']['tmp_name'];
    $fileDes = 'assets/images/birounit/'.$fileName;

    if(!empty($fileTmpName)){
    	move_uploaded_file($fileTmpName, $fileDes);

        $type = pathinfo($fileDes, PATHINFO_EXTENSION);
        $data_image = file_get_contents($fileDes);
        $base64 = 'data:image/' . $type . ';base64,' . base64_encode($data_image);

        $con->query("INSERT INTO user_biro_unit VALUES (default,'$_POST[nama_biro_unit]', '$_POST[email]', '$_POST[password]', '$_POST[informasi_job_desc]', '', '$base64')");
    }
    else{
    	$con->query("INSERT INTO user_biro_unit VALUES (default,'$_POST[nama_biro_unit]', '$_POST[email]', '$_POST[password]', '$_POST[informasi_job_desc]', '', '')");
    }	

	echo "<script> alert('Data Biro Unit Tersimpan'); </script>";
	echo "<script> location='biro_unit'; </script>";
}
?>