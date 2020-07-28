<?php 
header('Content-Type: application/json');

session_start();
if(empty($_SESSION['admin'])){
	header("location:login");
}

include 'assets/database/db.php';

if($_SESSION['admin']=='Administrator'){
	$sql="SELECT user_biro_unit.nama_biro_unit, COUNT(DISTINCT aspirasi.id_aspirasi) AS aspirasi_count FROM user_biro_unit LEFT JOIN aspirasi ON user_biro_unit.id_user_biro_unit = aspirasi.id_user_biro_unit WHERE aspirasi.status=0 GROUP BY 1";
	$res=$con->query($sql);

	$data = array();

	while($row = $res->fetch_assoc()){
		$data[] = $row;
	}
}

echo json_encode($data);

?>