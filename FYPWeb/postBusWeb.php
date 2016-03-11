<?php

	if (session_status() == PHP_SESSION_NONE) {
		session_start();
	}

	$con=mysqli_connect('localhost', 'root', '', 'bustrackerdb');
	mysqli_select_db($con, "bustrackerdb");
	
	if(isset($_POST['busBusId']) && isset($_POST['busBusNo']) && isset($_POST['busBusNoPlate'])){
		$busBusId = $_POST['busBusId'];
		$busBusNo = $_POST['busBusNo'];
		$busBusNoPlate = $_POST['busBusNoPlate'];
		
		$result = mysqli_query($con, "SELECT * FROM bus WHERE id='".$busBusId."'");
		if(mysqli_fetch_array($result) != false){
			mysqli_query($con, "UPDATE bus SET busNo='".$busBusNo."', busNoPlate='".$busBusNoPlate."'WHERE id='".$busBusId."'");
		}else{
			mysqli_query($con, "insert into bus(busNo, busNoPlate) values('{$busBusNo}', '{$busBusNoPlate}')");
		}
		
		header('Location:home.php');
	}

?>