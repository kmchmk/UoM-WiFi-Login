<?php

$device_id = "-";
$device_model = "-";
$device_name = "-";

if (isset($_GET["device"])) {
    $device_id = $_GET["device"];
}

if (isset($_POST["device_id"])) {
    $device_id = $_POST["device_id"];
}
if (isset($_POST["device_model"])) {
    $device_model = $_POST["device_model"];
}
if (isset($_POST["device_name"])) {
    $device_name = $_POST["device_name"];
}

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

$sql = "insert into log (device_id, device_model, device_name, time) values (?, ?, ?, NOW())";
$stmt = $conn->prepare($sql);
$stmt->bind_param('sss', $device_id, $device_model, $device_name);
$stmt->execute();

?>


