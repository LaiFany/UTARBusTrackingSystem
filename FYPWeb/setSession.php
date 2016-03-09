<?php
	
	if (session_status() == PHP_SESSION_NONE) {
		session_start();
	}
	
	if(isset($_POST['infoId']) && isset($_POST['routeNo']) && isset($_POST['routeName']) && isset($_POST['bus']) && isset($_POST['waypoint']) && isset($_POST['stopNames'])){
		$_SESSION['infoId'] = $_POST['infoId'];
		$_SESSION['routeNo'] = $_POST['routeNo'];
		$_SESSION['routeName'] = $_POST['routeName'];
		$_SESSION['bus'] = $_POST['bus'];
		$_SESSION['waypoint'] = $_POST['waypoint'];
		$_SESSION['stopNames'] = $_POST['stopNames'];
	}
	
	if(isset($_POST['newsId']) && isset($_POST['newsTitle']) && isset($_POST['newsDesc']) && isset($_POST['newsContent']) && isset($_POST['date'])){
		$_SESSION['newsId'] = $_POST['newsId'];
		$_SESSION['newsTitle'] = $_POST['newsTitle'];
		$_SESSION['newsDesc'] = $_POST['newsDesc'];
		$_SESSION['newsContent'] = $_POST['newsContent'];
		$_SESSION['date'] = $_POST['date'];
	}
	
	if(isset($_POST['scheduleId']) && isset($_POST['scheduleRoute']) && isset($_POST['scheduleBus']) && isset($_POST['scheduleTopNote']) && isset($_POST['scheduleBottomNote']) && isset($_POST['scheduleTimetable']) && isset($_POST['scheduleDate'])){
		$_SESSION['scheduleId'] = $_POST['scheduleId'];
		$_SESSION['scheduleRoute'] = $_POST['scheduleRoute'];
		$_SESSION['scheduleBus'] = $_POST['scheduleBus'];
		$_SESSION['scheduleTopNote'] = $_POST['scheduleTopNote'];
		$_SESSION['scheduleBottomNote'] = $_POST['scheduleBottomNote'];
		$_SESSION['scheduleTimetable'] = $_POST['scheduleTimetable'];
		$_SESSION['scheduleDate'] = $_POST['scheduleDate'];
	}
	
	if(isset($_POST['userId']) && isset($_POST['userUsername']) && isset($_POST['userPassword']) && isset($_POST['userPrivilege'])){
		$_SESSION['userId'] = $_POST['userId'];
		$_SESSION['userUsername'] = $_POST['userUsername'];
		$_SESSION['userPassword'] = $_POST['userPassword'];
		$_SESSION['userPrivilege'] = $_POST['userPrivilege'];
	}
	
	if(isset($_POST['sessionLink'])){
		$_SESSION['link'] = $_POST['sessionLink'];
	}
	
?>