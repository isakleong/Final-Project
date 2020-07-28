<?php
session_start();
if(empty($_SESSION['admin'])){
    header("location:login.php");
}
include 'assets/database/db.php';
?>

<!doctype html>
<html class="no-js" lang="en">

<head>
    <meta charset="utf-8">
    <meta http-equiv="x-ua-compatible" content="ie=edge">
    <title>Aspirasi Dalam Proses - Petra Complaint</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="shortcut icon" type="image/png" href="assets/images/icon/favicon.ico">
    <link rel="stylesheet" href="assets/css/bootstrap.min.css">
    <link rel="stylesheet" href="assets/css/font-awesome.min.css">
    <link rel="stylesheet" href="assets/css/themify-icons.css">
    <link rel="stylesheet" href="assets/css/metisMenu.css">
    <link rel="stylesheet" href="assets/css/owl.carousel.min.css">
    <link rel="stylesheet" href="assets/css/slicknav.min.css">
    <!-- amcharts css -->
    <link rel="stylesheet" href="https://www.amcharts.com/lib/3/plugins/export/export.css" type="text/css" media="all" />
    <!-- Start datatable css -->
    <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.10.19/css/jquery.dataTables.css">
    <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.10.18/css/dataTables.bootstrap4.min.css">
    <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/responsive/2.2.3/css/responsive.bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/responsive/2.2.3/css/responsive.jqueryui.min.css">
    <!-- style css -->
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
                    <a href="home"><img src="assets/images/icon/logoapp.png" alt="logo"></a>
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
                                <li><span>Aspirasi Selesai Diproses</span></li>
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
                                <a class="dropdown-item" href="logout.php">Log Out</a>
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
                    <!-- Dark table start -->
                    <div class="col-12 mt-5">
                        <div class="card">
                            <div class="card-body">
                                <h4 class="header-title">Data Aspirasi Selesai Diproses</h4>
                                <div class="data-tables datatable-dark">
                                    <table id="dataTable3" class="text-center">
                                        <thead class="text-capitalize">
                                            <tr>
                                                <?php if($_SESSION['admin']!='Administrator'){
                                                ?>
                                                <th>No</th>
                                                <th>Teks Aspirasi</th>
                                                <th>Waktu Aspirasi</th>
                                                <th>Waktu Mulai Proses</th>
                                                <th>Waktu Selesai Proses</th>
                                                <th>User Civitas</th>
                                                <th>Detail Aspirasi</th>
                                                <th>Reviewed</th>

                                                <?php } ?>

                                                <?php if($_SESSION['admin']=='Administrator'){ ?>
                                                    <th>No</th>
                                                    <th>Teks Aspirasi</th>
                                                    <th>Waktu Aspirasi</th>
                                                    <th>Biro/Unit</th>
                                                    <th>Model_Predict</th>
                                                    <th>Detail Aspirasi</th>
                                                <?php } ?>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <?php $nomor = 1; ?>

                                            <?php
                                                if($_SESSION['admin']!='Administrator'){
                                                    $id_user_biro_unit=$_SESSION['id_admin'];
                                                    $sql="SELECT asp.*, id_status_aspirasi, user.nama_lengkap, user.nrp FROM ((aspirasi asp LEFT JOIN (SELECT id_aspirasi, MAX(id_status_aspirasi) AS id_status_aspirasi FROM detail_status_aspirasi GROUP BY id_aspirasi) det ON det.id_aspirasi = asp.id_aspirasi) LEFT JOIN user_civitas user ON user.id_user_civitas=asp.id_user_civitas) HAVING asp.id_user_biro_unit='$id_user_biro_unit' AND asp.status=2";
                                                }

                                                else{
                                                    $sql="SELECT asp.*, id_status_aspirasi, user.nama_biro_unit FROM ((aspirasi asp LEFT JOIN (SELECT id_aspirasi, MAX(id_status_aspirasi) AS id_status_aspirasi FROM detail_status_aspirasi GROUP BY id_aspirasi) det ON det.id_aspirasi=asp.id_aspirasi) LEFT JOIN user_biro_unit user ON user.id_user_biro_unit=asp.id_user_biro_unit) HAVING asp.status=1";
                                                }


                                            ?>

                                            <?php $res = $con->query($sql); ?>

                                            <?php if($_SESSION['admin']=='Administrator'){
                                                    ?>

                                            

                                            <?php while($row = $res->fetch_assoc()) { ?>
                                            <tr>
                                                <td> <?php echo $nomor; ?> </td>
                                                <td> <?php echo $row['teks_aspirasi']; ?> </td>
                                                <td> <?php echo $row['tgl_aspirasi']; ?> </td>
                                                <td> <?php echo $row['nama_biro_unit']; ?> </td>
                                                <td> <?php echo $row['model_predict']; ?> </td>

                                                <td> <a href="detail_aspirasi_selesai?id=<?php echo $row['id_aspirasi']; ?>"> <button class="btn btn-lg btn-success" data-target="update"><i class="fa fa-eye"></i> </button> </a> </td>

                                            </tr>
                                                <?php $nomor++; ?>
                                            <?php } ?> 
                                            <?php } ?> 

                                            <?php if($_SESSION['admin']!='Administrator'){
                                                    ?>

                                            <?php while($row = $res->fetch_assoc()) { ?>
                                            <tr>
                                                <td> <?php echo $nomor; ?> </td>
                                                <td> <?php echo $row['teks_aspirasi']; ?> </td>
                                                <td> <?php echo $row['tgl_aspirasi']; ?> </td>

                                                <?php 
                                                    $id_aspirasi=$row['id_aspirasi'];
                                                    $sql_tgl="SELECT tgl_status FROM detail_status_aspirasi WHERE id_aspirasi='$id_aspirasi' AND id_status_aspirasi=4";
                                                    $res_tgl=$con->query($sql_tgl);
                                                    while($row_tgl=$res_tgl->fetch_assoc()){
                                                        $tgl_status_mulai=$row_tgl['tgl_status'];
                                                    }

                                                    $sql_tgl="SELECT tgl_status FROM detail_status_aspirasi WHERE id_aspirasi='$id_aspirasi' AND id_status_aspirasi=5";
                                                    $res_tgl=$con->query($sql_tgl);
                                                    while($row_tgl=$res_tgl->fetch_assoc()){
                                                        $tgl_status_selesai=$row_tgl['tgl_status'];
                                                    }
                                                ?>

                                                <td> <?php echo $tgl_status_mulai; ?> </td>
                                                <td> <?php echo $tgl_status_selesai; ?> </td>

                                                 <?php 
                                                    if($row['anonym']==0){
                                                        $row['nama_lengkap']="Anonymous";
                                                    }
                                                    else{
                                                        $row['nama_lengkap']=$row['nama_lengkap']." -- ".$row['nrp'];
                                                    }
                                                 ?>

                                                <td> <?php echo $row['nama_lengkap']; ?> </td>

                                                <?php
                                                    if($row['id_status_aspirasi']==6){
                                                        $reviewed="Yes";
                                                    }
                                                    else{
                                                        $reviewed="No";
                                                    }
                                                ?>

                                                <td> <a href="detail_aspirasi_selesai?id=<?php echo $row['id_aspirasi']; ?>"> <button class="btn btn-lg btn-success" data-target="update"><i class="fa fa-eye"></i> </button> </a> </td>

                                                <td> <?php echo $reviewed; ?> </td>
                                            </tr>
                                            <?php $nomor++; ?>
                                            <?php } ?>        
                                            <?php } ?> 

                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- Dark table end -->
                </div>
            </div>
        </div>
        <!-- main content area end -->
        <!-- footer area start-->
        <footer>
            <div class="footer-area">
                <p>© Copyright 2020. Petra Complaint Management System</p>
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

    <!-- Start datatable js -->
    <script src="https://cdn.datatables.net/1.10.19/js/jquery.dataTables.js"></script>
    <script src="https://cdn.datatables.net/1.10.18/js/jquery.dataTables.min.js"></script>
    <script src="https://cdn.datatables.net/1.10.18/js/dataTables.bootstrap4.min.js"></script>
    <script src="https://cdn.datatables.net/responsive/2.2.3/js/dataTables.responsive.min.js"></script>
    <script src="https://cdn.datatables.net/responsive/2.2.3/js/responsive.bootstrap.min.js"></script>
    <!-- others plugins -->
    <script src="assets/js/plugins.js"></script>
    <script src="assets/js/scripts.js"></script>
</body>

</html>
