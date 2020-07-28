<?php 
header('Content-Type: application/json');

session_start();
if(empty($_SESSION['admin'])){
	header("location:login");
}

include 'assets/database/db.php';

if($_SESSION['admin']!='Administrator'){
  $sql_biro_unit="SELECT id_user_biro_unit FROM user_biro_unit WHERE nama_biro_unit LIKE '$_SESSION[admin]' ";
  $res_biro_unit=$con->query($sql_biro_unit);
  $data=$res_biro_unit->fetch_assoc();
  $id_user_biro_unit=$data['id_user_biro_unit'];

	$sql="SELECT aspirasi.tgl_aspirasi FROM aspirasi WHERE id_user_biro_unit='$id_user_biro_unit' AND status=2 GROUP BY 1";
	$res=$con->query($sql);

	$data = array();
	$arr_data = array();

	$arr_month = array();
	$arr_year = array();

	while($row = $res->fetch_assoc()){
		$tgl_aspirasi = $row['tgl_aspirasi'];
		$explode_tgl_aspirasi = explode(" ", $tgl_aspirasi);
		$arr_explode = explode("-", $explode_tgl_aspirasi[0]);
		$year = $arr_explode[0];
		$month = $arr_explode[1];
		
		array_push($arr_month, $month);
		array_push($arr_year, $year);

		$count_arr_month = array_count_values($arr_month);
		$count_arr_year = array_count_values($arr_year);

	}

	$count_arr_month = array_count_values($arr_month);
	$count_arr_year = array_count_values($arr_year);

	foreach($count_arr_month as $x => $val) {
  		// echo "$x = $val<br>";
  		
  		//checking month
  		if($x=="01"){
  			$x="Januari";
  		}
  		elseif($x=="02"){
  			$x="Februari";
  		}
  		elseif($x=="03"){
  			$x="Maret";
  		}
  		elseif($x=="04"){
  			$x="April";
  		}
  		elseif($x=="05"){
  			$x="Mei";
  		}
  		elseif($x=="06"){
  			$x="Juni";
  		}
  		elseif($x=="07"){
  			$x="Juli";
  		}
  		elseif($x=="08"){
  			$x="Agustus";
  		}
  		elseif($x=="09"){
  			$x="September";
  		}
  		elseif($x=="10"){
  			$x="Oktober";
  		}
  		elseif($x=="11"){
  			$x="November";
  		}
  		elseif($x=="12"){
  			$x="Desember";
  		}

  		$arr_data['month'] = $x;
		$arr_data['count'] = $val;

		$data[]=$arr_data;
	}

}

echo json_encode($data);

?>