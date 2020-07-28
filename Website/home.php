<?php
session_start();
if(empty($_SESSION['admin'])){
    header("location:login.php");
}
include 'assets/database/db.php';

function base64_to_jpeg( $base64_string, $output_file ) {
    $ifp = fopen( $output_file, "wb" ); 
    $data = explode(',', $base64_string);
    fwrite($ifp, base64_decode($data[1]));
    fclose( $ifp ); 
    return( $output_file); 
}

?>

<!doctype html>
<html class="no-js" lang="en">

<head>
    <meta charset="utf-8">
    <meta http-equiv="x-ua-compatible" content="ie=edge">
    <title>Admin - Petra Complaint</title>
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
                    <a href="home"><img src="assets/images/icon/logoapp.png" alt="logo"></a>
                </div>
            </div>
            <div class="main-menu">
                <div class="menu-inner">
                    <?php
                        if($_SESSION['admin']=="Administrator"){
                            require 'navbar/admin/navbar_admin.php';
                        }
                        else{
                            require 'navbar/birounit/navbar_admin.php';
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
                            <h4 class="page-title pull-left">Dashboard</h4>
                            <ul class="breadcrumbs pull-left">
                                <li><a href="home">Home</a></li>
                                <li><span>Main Dashboard</span></li>
                            </ul>
                        </div>
                    </div>
                    <div class="col-sm-6 clearfix">
                        <div class="user-profile pull-right">
                            <?php
                                if($_SESSION['admin']!='Administrator'){
                                    $res_foto = $con->query("SELECT profile_picture FROM user_biro_unit WHERE nama_biro_unit LIKE '$_SESSION[admin]'"); 
                                    $row_foto = $res_foto->fetch_assoc();
                                    $filename_path ="assets/images/uploads/".md5(time().uniqid()).".jpg";
                                    $image = base64_to_jpeg($row_foto['profile_picture'], $filename_path);
                                }
                            ?>

                            <?php if($_SESSION['admin']!='Administrator'){ ?>
                                <img class="avatar user-thumb" src="<?php echo $image ?>">
                            <?php } ?>
                            <?php if($_SESSION['admin']=='Administrator'){ ?>
                                <img class="avatar user-thumb" src="assets/images/author/avatar.png" alt="avatar">
                            <?php } ?>
                            <!-- <img class="avatar user-thumb" src="assets/images/author/avatar.png" alt="avatar"> -->
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
            <div class="main-content-inner">
                <!-- data aspirasi area start -->
                <div class="sales-report-area mt-5 mb-5">
                    <div class="row">
                        <?php if($_SESSION['admin']=='Administrator'){ ?>
                        <div class="col-md-12 mb-4">
                            <div class="single-report mb-xs-30">
                                <div class="s-report-inner pr--20 pt--30 mb-3">
                                    <div class="icon"><i class="fa fa-clock-o"></i></div>
                                    <div class="s-report-title d-flex justify-content-between">
                                        <h4 class="mb-0">Data Aspirasi Baru (Not Processed)</h4>
                                    </div>
                                    <div class="d-flex justify-content-between pb-2">
                                        <span>Data aspirasi civitas akademika yang belum ditugaskan ke biro / unit terkait</span>
                                    </div>
                                    <div class="d-flex justify-content-between">
                                        <canvas id="notProcessCanvas" height="100"></canvas>
                                    </div> 
                                </div>
                            </div>
                        </div>

                        <div class="col-md-12 mb-4">
                            <div class="single-report mb-xs-30">
                                <div class="s-report-inner pr--20 pt--30 mb-3">
                                    <div class="icon"><i class="fa fa-comments"></i></div>
                                    <div class="s-report-title d-flex justify-content-between">
                                        <h4 class="mb-0">Data Aspirasi Dalam Proses (In Process)</h4>
                                    </div>
                                    <div class="d-flex justify-content-between pb-2">
                                        <span>Data aspirasi civitas akademika yang sedang diproses oleh biro / unit terkait</span>
                                    </div>
                                    <div class="d-flex justify-content-between">
                                        <canvas id="inProcessCanvas" height="100"></canvas>
                                    </div> 
                                </div>
                            </div>
                        </div>

                        <div class="col-md-12 mb-4">
                            <div class="single-report mb-xs-30">
                                <div class="s-report-inner pr--20 pt--30 mb-3">
                                    <div class="icon"><i class="fa fa-check"></i></div>
                                    <div class="s-report-title d-flex justify-content-between">
                                        <h4 class="mb-0">Data Aspirasi Selesai Proses (Completed)</h4>
                                    </div>
                                    <div class="d-flex justify-content-between pb-2">
                                        <span>Data aspirasi civitas akademika yang telah selesai diproses oleh biro / unit terkait</span>
                                    </div>
                                    <div class="d-flex justify-content-between">
                                        <canvas id="completedCanvas" height="100"></canvas>
                                    </div> 
                                </div>
                            </div>
                        </div>
                        <?php } ?>

                        <?php if($_SESSION['admin']!='Administrator'){ ?>
                        <div class="col-md-12 mb-4">
                            <div class="single-report mb-xs-30">
                                <div class="s-report-inner pr--20 pt--30 mb-3">
                                    <div class="icon"><i class="fa fa-book"></i></div>
                                    <div class="s-report-title d-flex justify-content-between">
                                        <h4 class="mb-0">Data Aspirasi</h4>
                                    </div>
                                    <div class="d-flex justify-content-between pb-2">
                                        <span>Summary jumlah data aspirasi yang masuk</span>
                                    </div>
                                    <div class="d-flex justify-content-between">
                                        <canvas id="summaryCanvas" height="100"></canvas>
                                    </div> 
                                </div>
                            </div>
                        </div>
                        <?php } ?>

                    </div>
                </div>
                <!-- data aspirasi report area end -->
            </div>
        </div>
        <!-- main content area end -->
        <!-- footer area start-->
        <footer>
            <div class="footer-area">
                <p>© Copyright 2020. Petra Complaint</p>
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

    <!-- start chart js -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.7.2/Chart.min.js"></script>
    <!-- start highcharts js -->
    <script src="https://code.highcharts.com/highcharts.js"></script>
    <!-- start zingchart js -->
    <script src="https://cdn.zingchart.com/zingchart.min.js"></script>
    <script>
    zingchart.MODULESDIR = "https://cdn.zingchart.com/modules/";
    ZC.LICENSE = ["569d52cefae586f634c54f86dc99e6a9", "ee6b7db5b51705a13dc2339db3edaf6d"];
    </script>
    <!-- all line chart activation -->
    <!-- <script src="assets/js/line-chart.js"></script> -->


    <?php if($_SESSION['admin']=='Administrator'){ ?>
    <script type="text/javascript">
        $(document).ready(function () {
            notProcessGraph();
            inProcessGraph();
            completedGraph();
        });


        function notProcessGraph()
        {
            {
                $.post("data_chart_not_process.php",
                function (data)
                {
                    console.log(data);
                    var arr_biro_unit = [];
                    var arr_aspirasi_count = [];

                    for (var i in data) {
                        arr_biro_unit.push(data[i].nama_biro_unit);
                        arr_aspirasi_count.push(data[i].aspirasi_count);
                    }

                    var chartdata = {
                        labels: arr_biro_unit,
                        datasets: [
                            {
                                label: 'Biro / Unit',
                                backgroundColor: '#49e2ff',
                                borderColor: '#46d5f1',
                                hoverBackgroundColor: '#CCCCCC',
                                hoverBorderColor: '#666666',

                                data: arr_aspirasi_count
                            }
                        ]
                    };

                    var graphTarget = $("#notProcessCanvas");

                    var barGraph = new Chart(graphTarget, {
                        type: 'horizontalBar',
                        data: chartdata
                    });
                });
            }
        }

        function inProcessGraph()
        {
            {
                $.post("data_chart_in_process.php",
                function (data)
                {
                    console.log(data);
                    var arr_biro_unit = [];
                    var arr_aspirasi_count = [];

                    for (var i in data) {
                        arr_biro_unit.push(data[i].nama_biro_unit);
                        arr_aspirasi_count.push(data[i].aspirasi_count);
                    }

                    var chartdata = {
                        labels: arr_biro_unit,
                        datasets: [
                            {
                                label: 'Biro / Unit',
                                backgroundColor: '#49e2ff',
                                borderColor: '#46d5f1',
                                hoverBackgroundColor: '#CCCCCC',
                                hoverBorderColor: '#666666',
                                data: arr_aspirasi_count
                            }
                        ]
                    };

                    var graphTarget = $("#inProcessCanvas");

                    var barGraph = new Chart(graphTarget, {
                        type: 'horizontalBar',
                        data: chartdata
                    });
                });
            }
        }

        function completedGraph()
        {
            {
                $.post("data_chart_completed.php",
                function (data)
                {
                    console.log(data);
                    var arr_biro_unit = [];
                    var arr_aspirasi_count = [];
                    var arr_month = [];
                    var arr_month_value = [];
                    var arr_year = [];

                    for (var i in data) {
                        arr_biro_unit.push(data[i].nama_biro_unit);
                        arr_aspirasi_count.push(data[i].aspirasi_count);
                    }

                    var chartdata = {
                        labels: arr_biro_unit,
                        datasets: [
                            {
                                label: 'Biro / Unit',
                                backgroundColor: '#49e2ff',
                                borderColor: '#46d5f1',
                                hoverBackgroundColor: '#CCCCCC',
                                hoverBorderColor: '#666666',
                                data: arr_aspirasi_count
                            }
                        ]
                    };

                    var graphTarget = $("#completedCanvas");

                    var barGraph = new Chart(graphTarget, {
                        type: 'horizontalBar',
                        data: chartdata
                    });
                });
            }
        }

    </script>
    <?php } ?>

    <?php if($_SESSION['admin']!='Administrator'){ ?>
    <script type="text/javascript">
        $(document).ready(function () {
            summaryGraph();
        });


        function summaryGraph()
        {
            {
                $.post("data_chart_summary.php",
                function (data)
                {
                    console.log(data);
                    var arr_month = [];
                    var arr_month_count = [];

                    for (var i in data) {
                        arr_month.push(data[i].month);
                        arr_month_count.push(data[i].count);
                    }

                    var chartdata = {
                        labels: arr_month,
                        datasets: [
                            {
                                label: 'Bulan',
                                backgroundColor: '#49e2ff',
                                borderColor: '#46d5f1',
                                hoverBackgroundColor: '#CCCCCC',
                                hoverBorderColor: '#666666',
                                data: arr_month_count
                            }
                        ]
                    };

                    var graphTarget = $("#summaryCanvas");

                    var barGraph = new Chart(graphTarget, {
                        type: 'horizontalBar',
                        data: chartdata
                    });
                });
            }
        }

    </script>
    <?php } ?>

    <script src="assets/js/bar-chart.js"></script>
    <!-- all pie chart -->
    <script src="assets/js/pie-chart.js"></script>
    <!-- others plugins -->
    <script src="assets/js/plugins.js"></script>
    <script src="assets/js/scripts.js"></script>
</body>

</html>
