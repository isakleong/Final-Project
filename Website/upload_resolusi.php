<?php 
session_start();
if(empty($_SESSION['admin'])){
	header("location:login");
}

if(isset($_POST['save'])){
	include 'assets/database/db.php';
	// $id_aspirasi=$_GET['id'];

	$id_aspirasi = $_POST['id_aspirasi'];

	//insert teks resolusi
	$date = date('Y-m-d H:i:s');
    $con->query("INSERT INTO resolusi_aspirasi VALUES(default,'$_POST[teks_resolusi]', '$date', '$id_aspirasi')");
    
    $targetDir = "assets/images/resolusi/";
    $fileNames = array_filter($_FILES['files']['name']); 
    $allowTypes = array('jpg','png','jpeg','gif'); 
    
    $counter_image=0;

    if(!empty($fileNames)){
    	foreach($_FILES['files']['name'] as $key=>$val){
    		// File upload path 
            $fileName = basename($_FILES['files']['name'][$key]); 
            $targetFilePath = $targetDir . $fileName;

            // Check whether file type is valid 
            $fileType = pathinfo($targetFilePath, PATHINFO_EXTENSION);
            if(in_array($fileType, $allowTypes)){
            	// Upload file to server
            	if(move_uploaded_file($_FILES["files"]["tmp_name"][$key], $targetFilePath)){ 
                    // Image db insert sql 
                    $data_image = file_get_contents($targetFilePath);
                    $base64 = 'data:image/' . $fileType . ';base64,' . base64_encode($data_image);

                    $sql="SELECT * FROM resolusi_aspirasi ORDER BY id_resolusi_aspirasi DESC LIMIT 1";
					$res=$con->query($sql);
					while($row = $res->fetch_assoc()){
						$id_resolusi_aspirasi = $row['id_resolusi_aspirasi'];
					}

					$arr_det_foto = 'detail_foto_'.$counter_image;
					$detail_foto = $_POST[$arr_det_foto];

                    $con->query("INSERT INTO foto_resolusi VALUES (default,'$base64', '$detail_foto', '$id_resolusi_aspirasi')");

                }
            }

            $counter_image++;
    	}
    }

    //update status aspirasi to 2 -- completed
    $sql_update="UPDATE aspirasi SET status='2' WHERE id_aspirasi='$id_aspirasi'";
	$res_update=$con->query($sql_update);

	//insert detail status 4 -- resolusi dikirim ke user civitas
    $sql_detail_status="INSERT INTO detail_status_aspirasi VALUES(default,'$date', '5', '$id_aspirasi')";
    $res_detail_status=$con->query($sql_detail_status);

    //send notification to user civitas
    //handle notification sent
    define('API_ACCESS_KEY','AAAAFrqG_sU:APA91bGnUkKbbHMgSpYYQ5ShSMMn_E06qxidjVfNifSyEwBG52ONG2oBrciSSVY-zIneSWDgpYhzfwJlnvhP-OuHW4f2PePxd0nOX9XZ5P_bMYAsUq7hqeJXGMmrbq_bhFtZmOQAUqWZ');
    $fcmUrl = 'https://fcm.googleapis.com/fcm/send';

    //get token
    $sql_token="SELECT user_civitas.token_fcm FROM user_civitas LEFT JOIN aspirasi ON user_civitas.id_user_civitas=aspirasi.id_user_civitas WHERE aspirasi.id_aspirasi='$id_aspirasi'";
    $res_token=$con->query($sql_token);

    while($row_token= $res_token->fetch_assoc()) {
        $token=$row_token['token_fcm'];
    }

    $notification = [
        'title' =>'Hai Petranesian!',
        'body' => 'Data aspirasimu sudah mendapat resolusi oleh '.$_SESSION['admin'],
    ];

    $extraNotificationData = [
        'id_aspirasi' => $id_aspirasi,
        'status_aspirasi' => '2'
    ];

    $fcmNotification = [
        //'registration_ids' => $tokenList, //multple token array
        'to'        => $token, //single token
        'notification' => $notification,
        'data' => $extraNotificationData
    ];

    $headers = [
        'Authorization: key=' . API_ACCESS_KEY,
        'Content-Type: application/json'
    ];

    $ch = curl_init();
    curl_setopt($ch, CURLOPT_URL,$fcmUrl);
    curl_setopt($ch, CURLOPT_POST, true);
    curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
    curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fcmNotification));
    $result = curl_exec($ch);
    curl_close($ch);


	echo "<script> alert('Data Resolusi Aspirasi Tersimpan'); </script>";
	echo "<script> location='aspirasi_proses'; </script>";
}
?>