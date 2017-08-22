<?php
    $jsonObj = new stdClass();
    $jsonObj->newversion = 4;
    $jsonObj->title = "Available on Playstore";
    $jsonObj->message = "You can now download this application on Google Playstore. \n* Search for 'UoM Wireless Login'";
    $jsonObj->positivebutton = "Download";
    $jsonObj->negativebutton = "Cancel";
    // $jsonObj->apkurl = "https://play.google.com/store/apps/details?id=lk.cse13.www.uomwireless";
    $jsonObj->apkurl = "http://13.58.202.127/UoM_Wireless_App/UoM_Wireless.apk";
    echo json_encode($jsonObj);




if (isset($_GET["device"])) {
    $device = $_GET["device"];

	$servername = "localhost";
	$username = "root";
	$password = "1234";
	$dbname = "uom_wireless";
	// Create connection
	$conn = new mysqli($servername, $username, $password, $dbname);
	$conn->set_charset("utf8");
	// Check connection
	if ($conn->connect_error) {
	    die("Connection failed: " . $conn->connect_error);
	}



    $sql = "insert into log (device,time) values (?, NOW())";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param('s', $device);
    $stmt->execute();

}
?>


