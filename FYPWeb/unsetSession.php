<?php
		if (session_status() == PHP_SESSION_NONE) {
			session_start();
		}
		
		unset($_SESSION['infoId']);
		unset($_SESSION['routeNo']);
		unset($_SESSION['routeName']);
		unset($_SESSION['bus']);
		unset($_SESSION['waypoint']);
		unset($_SESSION['stopNames']);
		
		unset($_SESSION['newsId']);
		unset($_SESSION['newsTitle']);
		unset($_SESSION['newsDesc']);
		unset($_SESSION['newsContent']);
		unset($_SESSION['date']);
		
		unset($_SESSION['scheduleId']);
		unset($_SESSION['scheduleRoute']);
		unset($_SESSION['scheduleBus']);
		unset($_SESSION['scheduleTopNote']);
		unset($_SESSION['scheduleBottomNote']);
		unset($_SESSION['scheduleTimetable']);
		unset($_SESSION['scheduleDate']);
		
		unset($_SESSION['userId']);
		unset($_SESSION['userUsername']);
		unset($_SESSION['userPassword']);
		unset($_SESSION['userPrivilege']);
		unset($_SESSION['userDefaultRoute']);
		unset($_SESSION['userDefaultBus']);
		
		unset($_SESSION['link']);
		
		unset($_SESSION['usernameDuplicate']);
		unset($_SESSION['loginNotFound']);
		
		if(isset($_POST['user'])){
			unset($_SESSION['user']);
		}
?>