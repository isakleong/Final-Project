<?php
session_start();
if(empty($_SESSION['admin'])){
    header("location:login.php");
}

include 'assets/database/db.php';
$id_aspirasi = $_GET['id'];

function base64_to_jpeg( $base64_string, $output_file ) {
    $ifp = fopen( $output_file, "wb" ); 
    $data = explode(',', $base64_string);
    fwrite($ifp, base64_decode($data[1]));
    fclose( $ifp ); 
    return( $output_file); 
}

//get data aspirasi
$sql_data="SELECT asp.*, id_status_aspirasi, user.nama_biro_unit FROM ((aspirasi asp LEFT JOIN (SELECT id_aspirasi, MAX(id_status_aspirasi) AS id_status_aspirasi FROM detail_status_aspirasi GROUP BY id_aspirasi) det ON det.id_aspirasi=asp.id_aspirasi) LEFT JOIN user_biro_unit user ON user.id_user_biro_unit=asp.id_user_biro_unit) HAVING asp.id_aspirasi='$id_aspirasi'";
$res_data = $con->query($sql_data);

while($row_data = $res_data->fetch_assoc()) {
    if($_SESSION['admin']=='Administrator'){
        $teks_aspirasi = $row_data['teks_aspirasi'];
        $tgl_aspirasi = $row_data['tgl_aspirasi'];
        $nama_biro_unit = $row_data['nama_biro_unit'];

        if(empty($row['nama_biro_unit'])){
            $nama_biro_unit="Not Decided";                            
        }

        if($row_data['model_predict']=='uppk'){
            $row_data['model_predict']='Unit Pelayanan dan Pemeliharaan Kampus';
        }
        elseif ($row_data['model_predict']=='puskom') {
            $row_data['model_predict']='Pusat Komputer';
        }
        elseif ($row_data['model_predict']=='baka') {
            $row_data['model_predict']='Biro Administrasi Kemahasiswaan dan Alumni';
        }
        elseif ($row_data['model_predict']=='baak') {
            $row_data['model_predict']='Biro Administrasi Akademik dan Kemahasiswaan';
        }
        $model_predict = $row_data['model_predict'];
    }

    else{
        $teks_aspirasi = $row_data['teks_aspirasi'];
        $tgl_aspirasi = $row_data['tgl_aspirasi'];
    }

}



//assign aspirasi query
if(isset ($_POST['assign'])){

    //get id biro unit assigned
    $biro_unit = $_POST['biro_unit'];

    //update biro unit tujuan aspirasi
    $sql_update="UPDATE aspirasi SET id_user_biro_unit='$biro_unit' WHERE id_aspirasi='$id_aspirasi'";
    $res_update=$con->query($sql_update);

    //insert detail status aspirasi to database
    $date = date('Y-m-d H:i:s');

    $sql="INSERT INTO detail_status_aspirasi VALUES(default,'$date', '3', '$id_aspirasi')";

    $res=$con->query($sql);

    //handle notification sent
    define('API_ACCESS_KEY','AAAAFrqG_sU:APA91bGnUkKbbHMgSpYYQ5ShSMMn_E06qxidjVfNifSyEwBG52ONG2oBrciSSVY-zIneSWDgpYhzfwJlnvhP-OuHW4f2PePxd0nOX9XZ5P_bMYAsUq7hqeJXGMmrbq_bhFtZmOQAUqWZ');
    $fcmUrl = 'https://fcm.googleapis.com/fcm/send';

    //get token
    $sql_token="SELECT user_civitas.token_fcm FROM user_civitas LEFT JOIN aspirasi ON user_civitas.id_user_civitas=aspirasi.id_user_civitas WHERE aspirasi.id_aspirasi='$id_aspirasi'";
    $res_token=$con->query($sql_token);

    while($row_token= $res_token->fetch_assoc()) {
        $token=$row_token['token_fcm'];
    }

    //get nama biro unit
    $sql_biro_unit="SELECT nama_biro_unit FROM user_biro_unit WHERE id_user_biro_unit='$biro_unit'";
    $res_sql_biro_unit = $con->query($sql_biro_unit);
    $data=$res_sql_biro_unit->fetch_assoc();
    $nama_biro_unit_proses=$data['nama_biro_unit'];

    $notification = [
        'title' =>'Hai Petranesian!',
        'body' => 'Data aspirasimu telah diteruskan ke '.$nama_biro_unit_proses.' untuk diproses lebih lanjut'
    ];

    $extraNotificationData = [
        'id_aspirasi' => $id_aspirasi,
        'status_aspirasi' => '0'
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

    echo "<script> alert('Aspirasi berhasil diteruskan ke biro atau unit terkait') </script>";
    echo "<script> location='aspirasi_baru'; </script>";
}

//
elseif (isset($_POST['process'])) {
    //update biro unit tujuan aspirasi
    $sql_update="UPDATE aspirasi SET status='1' WHERE id_aspirasi='$id_aspirasi'";
    $res_update=$con->query($sql_update);

    //insert detail status 4 -- sedang diproses
    $date = date('Y-m-d H:i:s');

    $sql="INSERT INTO detail_status_aspirasi VALUES(default,'$date', '4', '$id_aspirasi')";

    $res=$con->query($sql);

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
        'body' => 'Data aspirasimu sedang diproses oleh '.$_SESSION['admin']
    ];

    //get data aspirasi
    // $sql_detail_status = "SELECT asp.*, id_status_aspirasi, user.nama_biro_unit, user.profile_picture FROM ((aspirasi asp LEFT JOIN (SELECT id_aspirasi, MAX(id_status_aspirasi) AS id_status_aspirasi FROM detail_status_aspirasi GROUP BY id_aspirasi) det ON det.id_aspirasi=asp.id_aspirasi) LEFT JOIN user_biro_unit user ON user.id_user_biro_unit=asp.id_user_biro_unit) HAVING asp.id_aspirasi='$id_aspirasi' AND asp.status=1";
    // $res_detail_status = $con->query($sql_detail_status);

    // while($row_det=$res_detail_status->fetch_assoc()) {
    //     $contentIdDetailStatus=$row_det['id_status_aspirasi'];
    //     $contentTeksAspirasi=$row_det['teks_aspirasi'];
    //     $contentTglAspirasi=$row_det['tgl_aspirasi'];
    //     $contentPhotoAvatarBiroUnit=$row_det['profile_picture'];
    // }

    $extraNotificationData = [
        'id_aspirasi' => $id_aspirasi,
        'status_aspirasi' => '1'
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

    echo "<script> alert('Aspirasi berhasil diproses') </script>";
    echo "<script> location='aspirasi_baru'; </script>";
    
}


?>

<!doctype html>
<html class="no-js" lang="en">

<head>
    <meta charset="utf-8">
    <meta http-equiv="x-ua-compatible" content="ie=edge">
    <title>Detail Aspirasi - Petra Complaint</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="shortcut icon" type="image/png" href="assets/images/icon/favicon.ico">
    <link rel="stylesheet" href="assets/css/bootstrap.min.css">
    <link rel="stylesheet" href="assets/css/font-awesome.min.css">
    <link rel="stylesheet" href="assets/css/themify-icons.css">
    <link rel="stylesheet" href="assets/css/metisMenu.css">
    <link rel="stylesheet" href="assets/css/owl.carousel.min.css">
    <link rel="stylesheet" href="assets/css/slicknav.min.css">
    <!-- amchart css -->
    <link rel="stylesheet" href="https://www.amcharts.com/lib/3/plugins/export/export.css" type="text/css" media="all" />
    <!-- others css -->
    <link rel="stylesheet" href="assets/css/typography.css">
    <link rel="stylesheet" href="assets/css/default-css.css">
    <link rel="stylesheet" href="assets/css/styles.css">
    <link rel="stylesheet" href="assets/css/responsive.css">
    <!-- modernizr css -->
    <script src="assets/js/vendor/modernizr-2.8.3.min.js"></script>
</head>

<body>
    <!--[if lt IE 8]>
            <p class="browserupgrade">You are using an <strong>outdated</strong> browser. Please <a href="http://browsehappy.com/">upgrade your browser</a> to improve your experience.</p>
        <![endif]-->
    <!-- preloader area start -->
    <div id="preloader">
        <div class="loader"></div>
    </div>
    <!-- preloader area end -->
    <!-- page container area start -->
    <div class="page-container">
        <!-- sidebar menu area start -->
        <div class="sidebar-menu">
            <div class="sidebar-header">
                <div class="logo">
                    <a href="index.html"><img src="assets/images/icon/logoapp.png" alt="logo"></a>
                </div>
            </div>
            <div class="main-menu">
                <div class="menu-inner">
                    <?php
                        if($_SESSION['admin']=="Administrator"){
                            require 'navbar/admin/navbar_aspirasi_baru.php';
                        }
                        else{
                            require 'navbar/birounit/navbar_aspirasi_baru.php';
                        }
                    ?>
                </div>
            </div>
        </div>
        <!-- sidebar menu area end -->
        <!-- main content area start -->
        <div class="main-content">
            <!-- header area start -->
            <div class="header-area">
                <div class="row align-items-center">
                    <!-- nav and search button -->
                    <div class="col-md-6 col-sm-8 clearfix">
                        <div class="nav-btn pull-left">
                            <span></span>
                            <span></span>
                            <span></span>
                        </div>
                    </div>
                    <!-- profile info & task notification -->
                    <div class="col-md-6 col-sm-4 clearfix">
                        <ul class="notification-area pull-right">
                            <li id="full-view"><i class="ti-fullscreen"></i></li>
                            <li id="full-view-exit"><i class="ti-zoom-out"></i></li>
                            
                        </ul>
                    </div>
                </div>
            </div>
            <!-- header area end -->
            <!-- page title area start -->
            <div class="page-title-area">
                <div class="row align-items-center">
                    <div class="col-sm-6">
                        <div class="breadcrumbs-area clearfix">
                            <h4 class="page-title pull-left">Manage Data</h4>
                            <ul class="breadcrumbs pull-left">
                                <li><a href="home">Home</a></li>
                                <li><a href="aspirasi_baru">Aspirasi Baru</a></li>
                                <li><span>Detail Aspirasi</span></li>
                            </ul>
                        </div>
                    </div>
                    <div class="col-sm-6 clearfix">
                        <div class="user-profile pull-right">
                            <img class="avatar user-thumb" src="assets/images/author/avatar.png" alt="avatar">
                            <h4 class="user-name dropdown-toggle" data-toggle="dropdown">
                                <?php  
                                    if(isset($_SESSION['admin'])){
                                        echo $_SESSION['admin'];
                                    }
                                ?> 
                                <i class="fa fa-angle-down"></i></h4>
                            <div class="dropdown-menu">
                                <a class="dropdown-item" href="logout">Log Out</a>
                                <?php  
                                    if($_SESSION['admin']!='Administrator'){
                                        echo "<a class='dropdown-item' href='profile'>Edit Profile</a>";
                                    }
                                ?> 
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <!-- page title area end -->
            <div class="main-content-inner">
                <div class="row">
                    <div class="col-lg-6 col-ml-12">
                        <div class="row">
                            <!-- Textual inputs start -->
                            <div class="col-12 mt-5">
                                <div class="card">
                                    <div class="card-body">
                                        <?php
                                            if($_SESSION['admin']=='Administrator'){
                                        ?>

                                        <h4 class="header-title">Data Detail Aspirasi Baru</h4>
                                        <p class="text-muted font-14 mb-4">Silahkan periksa prediksi model kelas aspirasi baru dari civitas akademika untuk diteruskan ke biro / unit terkait</p>
                                        <form method="POST" enctype="multipart/form-data">
                                            <div class="form-group">
                                                <label for="example-text-input" class="col-form-label">Teks Aspirasi</label>
                                                <input class="form-control" type="text" value="<?php echo $teks_aspirasi; ?>" name="teks_aspirasi" id="example-text-input" disabled>
                                            </div>
                                            <div class="form-group">
                                                <label for="example-datetime-local-input" class="col-form-label">Waktu Aspirasi Dibuat</label>
                                                <input class="form-control" type="text" value="<?php echo $tgl_aspirasi; ?>" id="example-datetime-local-input" disabled>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-form-label">Foto Aspirasi</label>
                                            </div>
                                            <div class="row">
                                                <?php
                                                    $sql_foto="SELECT * FROM foto_aspirasi WHERE id_aspirasi='$id_aspirasi' ";
                                                    $res_foto = $con->query($sql_foto);

                                                    while($row_foto = $res_foto->fetch_assoc()) {
                                                        
                                                        echo "<div class='col-lg-4 col-md-6'>";

                                                        echo "<div class='card-bordered'>";

                                                        $filename_path ="assets/images/uploads/aspirasi/".md5(time().uniqid()).".jpg";
                                                        $image = base64_to_jpeg($row_foto['foto_aspirasi'], $filename_path);

                                                        echo "<img class='card-img-top img-fluid' src=".$image.">";

                                                        echo "<div class='card-body'>";
                                                        echo "<p class='card-text'>".$row_foto['detail_foto']."</p>";

                                                        echo "</div>";
                                                        echo "</div>";
                                                        echo "</div>";
                                                    }
                                                ?>
                                            </div>

                                            <div class="form-group">
                                                <label for="example-email-input" class="col-form-label">Biro / Unit Tujuan</label>
                                                <input class="form-control" type="email" value="<?php echo $nama_biro_unit; ?>" id="example-email-input" disabled>
                                            </div>
                                            <div class="form-group">
                                                <label for="example-url-input" class="col-form-label">Hasil Prediksi Model</label>
                                                <input class="form-control" type="url" value="<?php echo $model_predict; ?>" id="example-url-input" disabled>
                                            </div>
                                        
                                            <div class="form-group">
                                                <label class="col-form-label">Assign To Biro / Unit:</label>
                                                <select name='biro_unit' class="form-control mb-4">
                                                    <?php
                                                        $sql_biro="SELECT * FROM user_biro_unit";
                                                        $res_biro=$con->query($sql_biro);
                                                        while($row_biro = $res_biro->fetch_assoc()) {
                                                            echo "<option value=$row_biro[id_user_biro_unit]> ".$row_biro['nama_biro_unit']."</option>";
                                                        }
                                                    ?>
                                                </select>
                                            </div>
                                            <button type="submit" name="assign" class="btn btn-primary mt-4 pr-4 pl-4">Assign Aspirasi</button>
                                            <?php } ?>

                                            <?php
                                                if($_SESSION['admin']!='Administrator'){
                                            ?>

                                            <h4 class="header-title">Data Detail Aspirasi Baru</h4>
                                            <p class="text-muted font-14 mb-4">Selamat memproses aspirasi</p>
                                            <form method="POST" enctype="multipart/form-data">
                                                <div class="form-group">
                                                    <label for="example-text-input" class="col-form-label">Teks Aspirasi</label>
                                                    <input class="form-control" type="text" value="<?php echo $teks_aspirasi; ?>" name="teks_aspirasi" id="example-text-input" disabled>
                                                </div>
                                                <div class="form-group">
                                                    <label for="example-datetime-local-input" class="col-form-label">Waktu Aspirasi Dibuat</label>
                                                    <input class="form-control" type="text" value="<?php echo $tgl_aspirasi; ?>" id="example-datetime-local-input" disabled>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-form-label">Foto Aspirasi</label>
                                                </div>
                                                <div class="row">
                                                <?php
                                                    $sql_foto="SELECT * FROM foto_aspirasi WHERE id_aspirasi='$id_aspirasi' ";
                                                    $res_foto = $con->query($sql_foto);

                                                    while($row_foto = $res_foto->fetch_assoc()) {
                                                        
                                                        echo "<div class='col-lg-4 col-md-6'>";

                                                        echo "<div class='card-bordered'>";

                                                        $filename_path ="assets/images/uploads/aspirasi/".md5(time().uniqid()).".jpg";
                                                        $image = base64_to_jpeg($row_foto['foto_aspirasi'], $filename_path);

                                                        echo "<img class='card-img-top img-fluid' src=".$image.">";

                                                        echo "<div class='card-body'>";
                                                        echo "<p class='card-text'>".$row_foto['detail_foto']."</p>";

                                                        echo "</div>";
                                                        echo "</div>";
                                                        echo "</div>";
                                                        
                                                    }
                                                ?>
                                            </div>
                                                <button type="submit" name="process" class="btn btn-primary mt-4 pr-4 pl-4">Process Aspirasi</button>
                                            <?php } ?>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!-- main content area end -->
        <!-- footer area start-->
        <footer>
            <div class="footer-area">
                <p>© Copyright 2020. Petra Compaint Management System</p>
            </div>
        </footer>
        <!-- footer area end-->
    </div>
    <!-- page container area end -->
    <!-- offset area start -->
    <div class="offset-area">
        <div class="offset-close"><i class="ti-close"></i></div>
        <ul class="nav offset-menu-tab">
            <li><a class="active" data-toggle="tab" href="#activity">Activity</a></li>
            <li><a data-toggle="tab" href="#settings">Settings</a></li>
        </ul>
        <div class="offset-content tab-content">
            <div id="activity" class="tab-pane fade in show active">
                <div class="recent-activity">
                    <div class="timeline-task">
                        <div class="icon bg1">
                            <i class="fa fa-envelope"></i>
                        </div>
                        <div class="tm-title">
                            <h4>Rashed sent you an email</h4>
                            <span class="time"><i class="ti-time"></i>09:35</span>
                        </div>
                        <p>Lorem ipsum dolor sit amet consectetur adipisicing elit. Esse distinctio itaque at.
                        </p>
                    </div>
                    <div class="timeline-task">
                        <div class="icon bg2">
                            <i class="fa fa-check"></i>
                        </div>
                        <div class="tm-title">
                            <h4>Added</h4>
                            <span class="time"><i class="ti-time"></i>7 Minutes Ago</span>
                        </div>
                        <p>Lorem ipsum dolor sit amet consectetur.
                        </p>
                    </div>
                    <div class="timeline-task">
                        <div class="icon bg2">
                            <i class="fa fa-exclamation-triangle"></i>
                        </div>
                        <div class="tm-title">
                            <h4>You missed you Password!</h4>
                            <span class="time"><i class="ti-time"></i>09:20 Am</span>
                        </div>
                    </div>
                    <div class="timeline-task">
                        <div class="icon bg3">
                            <i class="fa fa-bomb"></i>
                        </div>
                        <div class="tm-title">
                            <h4>Member waiting for you Attention</h4>
                            <span class="time"><i class="ti-time"></i>09:35</span>
                        </div>
                        <p>Lorem ipsum dolor sit amet consectetur adipisicing elit. Esse distinctio itaque at.
                        </p>
                    </div>
                    <div class="timeline-task">
                        <div class="icon bg3">
                            <i class="ti-signal"></i>
                        </div>
                        <div class="tm-title">
                            <h4>You Added Kaji Patha few minutes ago</h4>
                            <span class="time"><i class="ti-time"></i>01 minutes ago</span>
                        </div>
                        <p>Lorem ipsum dolor sit amet consectetur adipisicing elit. Esse distinctio itaque at.
                        </p>
                    </div>
                    <div class="timeline-task">
                        <div class="icon bg1">
                            <i class="fa fa-envelope"></i>
                        </div>
                        <div class="tm-title">
                            <h4>Ratul Hamba sent you an email</h4>
                            <span class="time"><i class="ti-time"></i>09:35</span>
                        </div>
                        <p>Hello sir , where are you, i am egerly waiting for you.
                        </p>
                    </div>
                    <div class="timeline-task">
                        <div class="icon bg2">
                            <i class="fa fa-exclamation-triangle"></i>
                        </div>
                        <div class="tm-title">
                            <h4>Rashed sent you an email</h4>
                            <span class="time"><i class="ti-time"></i>09:35</span>
                        </div>
                        <p>Lorem ipsum dolor sit amet consectetur adipisicing elit. Esse distinctio itaque at.
                        </p>
                    </div>
                    <div class="timeline-task">
                        <div class="icon bg2">
                            <i class="fa fa-exclamation-triangle"></i>
                        </div>
                        <div class="tm-title">
                            <h4>Rashed sent you an email</h4>
                            <span class="time"><i class="ti-time"></i>09:35</span>
                        </div>
                    </div>
                    <div class="timeline-task">
                        <div class="icon bg3">
                            <i class="fa fa-bomb"></i>
                        </div>
                        <div class="tm-title">
                            <h4>Rashed sent you an email</h4>
                            <span class="time"><i class="ti-time"></i>09:35</span>
                        </div>
                        <p>Lorem ipsum dolor sit amet consectetur adipisicing elit. Esse distinctio itaque at.
                        </p>
                    </div>
                    <div class="timeline-task">
                        <div class="icon bg3">
                            <i class="ti-signal"></i>
                        </div>
                        <div class="tm-title">
                            <h4>Rashed sent you an email</h4>
                            <span class="time"><i class="ti-time"></i>09:35</span>
                        </div>
                        <p>Lorem ipsum dolor sit amet consectetur adipisicing elit. Esse distinctio itaque at.
                        </p>
                    </div>
                </div>
            </div>
            <div id="settings" class="tab-pane fade">
                <div class="offset-settings">
                    <h4>General Settings</h4>
                    <div class="settings-list">
                        <div class="s-settings">
                            <div class="s-sw-title">
                                <h5>Notifications</h5>
                                <div class="s-swtich">
                                    <input type="checkbox" id="switch1" />
                                    <label for="switch1">Toggle</label>
                                </div>
                            </div>
                            <p>Keep it 'On' When you want to get all the notification.</p>
                        </div>
                        <div class="s-settings">
                            <div class="s-sw-title">
                                <h5>Show recent activity</h5>
                                <div class="s-swtich">
                                    <input type="checkbox" id="switch2" />
                                    <label for="switch2">Toggle</label>
                                </div>
                            </div>
                            <p>The for attribute is necessary to bind our custom checkbox with the input.</p>
                        </div>
                        <div class="s-settings">
                            <div class="s-sw-title">
                                <h5>Show your emails</h5>
                                <div class="s-swtich">
                                    <input type="checkbox" id="switch3" />
                                    <label for="switch3">Toggle</label>
                                </div>
                            </div>
                            <p>Show email so that easily find you.</p>
                        </div>
                        <div class="s-settings">
                            <div class="s-sw-title">
                                <h5>Show Task statistics</h5>
                                <div class="s-swtich">
                                    <input type="checkbox" id="switch4" />
                                    <label for="switch4">Toggle</label>
                                </div>
                            </div>
                            <p>The for attribute is necessary to bind our custom checkbox with the input.</p>
                        </div>
                        <div class="s-settings">
                            <div class="s-sw-title">
                                <h5>Notifications</h5>
                                <div class="s-swtich">
                                    <input type="checkbox" id="switch5" />
                                    <label for="switch5">Toggle</label>
                                </div>
                            </div>
                            <p>Use checkboxes when looking for yes or no answers.</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- offset area end -->
    <!-- jquery latest version -->
    <script src="assets/js/vendor/jquery-2.2.4.min.js"></script>
    <!-- bootstrap 4 js -->
    <script src="assets/js/popper.min.js"></script>
    <script src="assets/js/bootstrap.min.js"></script>
    <script src="assets/js/owl.carousel.min.js"></script>
    <script src="assets/js/metisMenu.min.js"></script>
    <script src="assets/js/jquery.slimscroll.min.js"></script>
    <script src="assets/js/jquery.slicknav.min.js"></script>

    <!-- others plugins -->
    <script src="assets/js/plugins.js"></script>
    <script src="assets/js/scripts.js"></script>
</body>

</html>
