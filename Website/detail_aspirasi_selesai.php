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
        $model_predict = $row_data['model_predict'];
    }

    else{
        $teks_aspirasi = $row_data['teks_aspirasi'];
        $tgl_aspirasi = $row_data['tgl_aspirasi'];
    }
}

if($_SESSION['admin']!='Administrator'){
    //get data resolusi aspirasi
    // $sql_resolusi="SELECT resolusi_aspirasi.*, foto_resolusi.* FROM resolusi_aspirasi LEFT JOIN foto_resolusi ON resolusi_aspirasi.id_resolusi_aspirasi=foto_resolusi.id_resolusi_aspirasi WHERE id_aspirasi='$id_aspirasi'";

    $sql_resolusi="SELECT * FROM resolusi_aspirasi WHERE id_aspirasi='$id_aspirasi'";

    $res_resolusi = $con->query($sql_resolusi);

    while($row_resolusi = $res_resolusi->fetch_assoc()){
        $id_resolusi_aspirasi = $row_resolusi['id_resolusi_aspirasi'];
        $teks_resolusi = $row_resolusi['teks_resolusi'];
        $tgl_resolusi = $row_resolusi['tgl_resolusi'];
    }
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
                            require 'navbar/admin/navbar_aspirasi_selesai.php';
                        }
                        else{
                            require 'navbar/birounit/navbar_aspirasi_selesai.php';
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
                                <li><a href="aspirasi_selesai">Aspirasi Selesai Diproses</a></li>
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

                                        <h4 class="header-title">Data Detail Aspirasi Selesai Diproses</h4>
                                        <p class="text-muted font-14 mb-4">Silahkan periksa aspirasi baru dari civitas akademika untuk diteruskan ke biro / unit terkait</p>
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
                                            <div class="form-group">
                                                <?php
                                                    $sql_foto="SELECT * FROM foto_aspirasi WHERE id_aspirasi='$id_aspirasi' ";
                                                    $res_foto = $con->query($sql_foto);

                                                    $filename_path ="assets/images/uploads/aspirasi/".md5(time().uniqid()).".jpg";

                                                    while($row_foto = $res_foto->fetch_assoc()) {
                                                        // $row_foto['foto_aspirasi'] = htmlspecialchars(strip_tags($row_foto['foto_aspirasi']));
                                                        $image = base64_to_jpeg($row_foto['foto_aspirasi'], $filename_path);
                                                        // echo "<img src='$image' ">;
                                                        echo "<img width='200' src=".$image."><br><br>";
                                                        echo "<input class='form-control' type='text' value=".$row_foto['detail_foto']." name='teks_aspirasi' id='example-text-input' disabled><br><br>";
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
                                            <!-- <button type="submit" name="assign" class="btn btn-primary mt-4 pr-4 pl-4">Assign Aspirasi</button> -->
                                            <?php } ?>

                                            <?php
                                                if($_SESSION['admin']!='Administrator'){
                                            ?>

                                            <h4 class="header-title">Data Detail Aspirasi Selesai Diproses</h4>
                                            <p class="text-muted font-14 mb-4">Terima kasih sudah memproses aspirasi civitas akademika</p>
                                            <div class="form-group">
                                                <label for="example-text-input" class="col-form-label">Teks Aspirasi</label>
                                                <input class="form-control" type="text" value="<?php echo $teks_aspirasi; ?>" name="teks_aspirasi" id="example-text-input" disabled>
                                            </div>
                                            <div class="form-group">
                                                <label for="example-datetime-local-input" class="col-form-label">Waktu Aspirasi Dibuat</label>
                                                <input class="form-control" type="text" value="<?php echo $tgl_aspirasi; ?>" id="example-datetime-local-input" disabled>
                                            </div>
                                            <div class="form-group">
                                                <?php 
                                                    $sql="SELECT tgl_status FROM detail_status_aspirasi WHERE id_aspirasi='$id_aspirasi' AND id_status_aspirasi=4";
                                                    $res=$con->query($sql);
                                                    while($row=$res->fetch_assoc()){
                                                        $tgl_status_mulai=$row['tgl_status'];
                                                    }

                                                    $sql="SELECT tgl_status FROM detail_status_aspirasi WHERE id_aspirasi='$id_aspirasi' AND id_status_aspirasi=5";
                                                    $res=$con->query($sql);
                                                    while($row=$res->fetch_assoc()){
                                                        $tgl_status_selesai=$row['tgl_status'];
                                                    }
                                                ?>
                                                <label for="example-datetime-local-input" class="col-form-label">Waktu Aspirasi Mulai Diproses</label>
                                                <input class="form-control" type="text" value="<?php echo $tgl_status_mulai; ?>" id="example-datetime-local-input" disabled>
                                            </div>
                                            <div class="form-group">
                                                <label for="example-datetime-local-input" class="col-form-label">Waktu Aspirasi Selesai Diproses</label>
                                                <input class="form-control" type="text" value="<?php echo $tgl_status_selesai; ?>" id="example-datetime-local-input" disabled>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-form-label">Foto Aspirasi</label>
                                            </div>
                                            <!-- <div class="form-group"> -->
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

                                            <br><br>
                                            <h4 class="header-title">Resolusi Aspirasi</h4>
                                            <div class="form-group">
                                                <label for="example-text-input" class="col-form-label">Teks Resolusi Aspirasi</label>
                                                <input class="form-control" type="text" value="<?php echo $teks_resolusi; ?>" name="teks_resolusi" id="example-text-input" disabled>
                                            </div>

                                            <div class="form-group">
                                                <label for="example-datetime-local-input" class="col-form-label">Tanggal Resolusi</label>
                                                <input class="form-control" type="text" value="<?php echo $tgl_resolusi; ?>" id="example-datetime-local-input" disabled>
                                            </div>

                                            <div class="form-group">
                                                <label class="col-form-label">Foto Resolusi</label>
                                            </div>

                                            <div class="row">
                                                <?php
                                                    $sql_foto="SELECT * FROM foto_resolusi WHERE id_resolusi_aspirasi='$id_resolusi_aspirasi' ";
                                                    $res_foto = $con->query($sql_foto);

                                                    while($row_foto = $res_foto->fetch_assoc()) {
                                                        
                                                        echo "<div class='col-lg-4 col-md-6'>";

                                                        echo "<div class='card-bordered'>";

                                                        $filename_path ="assets/images/uploads/resolusi/".md5(time().uniqid()).".jpg";
                                                        $image = base64_to_jpeg($row_foto['foto_resolusi'], $filename_path);

                                                        echo "<img class='card-img-top img-fluid' src=".$image.">";

                                                        echo "<div class='card-body'>";
                                                        echo "<p class='card-text'>".$row_foto['detail_foto']."</p>";

                                                        echo "</div>";
                                                        echo "</div>";
                                                        echo "</div>";
                                                    }
                                                ?>
                                            </div>

                                        <?php } ?>
                                        </form>

                                        <?php
                                            //get data feedback
                                            $sql_feedback="SELECT feedback_resolusi.*, resolusi_aspirasi.id_aspirasi FROM feedback_resolusi LEFT JOIN resolusi_aspirasi ON feedback_resolusi.id_resolusi_aspirasi=resolusi_aspirasi.id_resolusi_aspirasi GROUP BY feedback_resolusi.id_feedback_resolusi HAVING resolusi_aspirasi.id_aspirasi='$id_aspirasi'";
                                            $res_feedback=$con->query($sql_feedback);

                                            $teks_feedback="None";
                                            $tgl_feedback="None";
                                            $rating="None";
                                            $detail_rating="None";

                                            while($row_feedback=$res_feedback->fetch_assoc()){
                                                $id_feedback_resolusi=$row_feedback['id_feedback_resolusi'];
                                                $teks_feedback=$row_feedback['teks_feedback'];
                                                $tgl_feedback=$row_feedback['tgl_feedback'];
                                                $rating=$row_feedback['rating'];
                                                $detail_rating=$row_feedback['detail_rating'];
                                            }
                                        ?>

                                         <!-- <div class="row"> -->
                                             <div class="col-lg-12 mt-5">
                                                <div class="card">
                                                    <div class="card-body">
                                                        <h4 class="header-title">Feedback Resolusi Aspirasi</h4>
                                                        <div class="additional-content">
                                                            <div class="alert alert-primary" role="alert">
                                                                <?php
                                                                echo "<p>".$teks_feedback."</p>";
                                                                ?>
                                                                <hr>
                                                                <?php
                                                                echo "<p class='mb-0'> Rating:  ".$rating."</p>";
                                                                echo "<p class='mb-0'> Detail rating:  ".$detail_rating."</p>";
                                                                ?>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                             </div>
                                         <!-- </div> -->

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

    <div id="resolusi" class="modal fade" role="dialog">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title"> Data Resolusi Aspirasi </h4>
                    <button class="close" data-dismiss="modal"> x </button>
                </div>
                <form method="post" action="upload_resolusi.php" enctype="multipart/form-data">
                    <div class="modal-body">
                        <input type="hidden" name="id_aspirasi" value="<?php echo $id_aspirasi; ?>">
                        <div class="form-group">
                            <label> Teks Resolusi </label>
                            <textarea class="form-control" name="teks_resolusi" rows="10" required></textarea>
                        </div>
                        <div class="form-group">
                            <label class="col-form-label"> Foto Resolusi </label>
                            <input id="upload_file" type="file" onchange="preview_image();" class="form-control" name="files[]" multiple>
                        </div>
                        <div class="form-group" id="image_preview"></div>
                    </div>
                    <div class="modal-footer">
                        <input type="submit" class="btn btn-success" name="save" value="Add Data">
                        <button type="reset" onclick="delete_image()" class="btn btn-danger"> Reset </button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <script type="text/javascript">
        function preview_image(){
            var total_file=document.getElementById("upload_file").files.length;
            for(var i=0;i<total_file;i++){
                $('#image_preview').append("<img src='"+URL.createObjectURL(event.target.files[i])+"'><br><br>");
                $('#image_preview').append("<input class='form-control' type='text' name='detail_foto_"+i+"'><br>");
            }
        }

        function delete_image(){
            $('#image_preview').empty();
        }
    </script>

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
