<?php

	session_start();
	
	$con=mysqli_connect('localhost', 'root', '', 'bustrackerdb');
	mysqli_select_db($con, "bustrackerdb");
	
	//login
	if(isset($_POST['username']) && isset($_POST['password'])){
		$username = testInput($_POST['username']);
		$password = testInput($_POST['password']);
		
		$result = mysqli_query($con, "SELECT * FROM user WHERE username='".$username."' AND password='".$password."' AND privilege='admin'");
		if(mysqli_fetch_array($result) != false){
			$_SESSION['user'] = $username;
			header('Location:index.php');
		}else{
			$_SESSION['loginNotFound'] = 'true';
			header('Location:login.php');
		}
	}
	
	//route table
	if(isset($_POST['routeRouteId']) && isset($_POST['routeRouteNo']) && isset($_POST['routeRouteName'])){
		$routeRouteId = $_POST['routeRouteId'];
		$routeRouteNo = $_POST['routeRouteNo'];
		$routeRouteName = $_POST['routeRouteName'];
		
		$result = mysqli_query($con, "SELECT * FROM route WHERE id='".$routeRouteId."'");
		if(mysqli_fetch_array($result) != false){
			mysqli_query($con, "UPDATE route SET routeNo='".$routeRouteNo."', routeName='".$routeRouteName."'WHERE id='".$routeRouteId."'");
		}else{
			mysqli_query($con, "insert into route(routeNo, routeName) values('{$routeRouteNo}', '{$routeRouteName}')");
		}
		
		header('Location:index.php');
	}
	
	//bus table
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
		
		header('Location:index.php');
	}
	
	//user table
	if(isset($_POST['userId']) && isset($_POST['username']) && isset($_POST['password']) && isset($_POST['privilege'])){
		$userId = $_POST['userId'];
		$username = $_POST['username'];
		$password = $_POST['password'];
		$privilege = $_POST['privilege'];
		$defaultRoute = '';
		$defaultBus = '';
		if(isset($_POST['defaultRoute'])){
			$defaultRoute = $_POST['defaultRoute'];
		}
		if(isset($_POST['defaultBus'])){
			$defaultBus = $_POST['defaultBus'];
		}
		
		$editResult = mysqli_query($con, "SELECT * FROM user WHERE id='".$userId."'");
		$addResult = mysqli_query($con, "SELECT * FROM user WHERE username='".$username."'");
		if(mysqli_fetch_array($editResult) != false){
			mysqli_query($con, "UPDATE user SET username='".$username."', password='".$password."', privilege='".$privilege."', defaultRoute='".$defaultRoute."', defaultBus='".$defaultBus."' WHERE id='".$userId."'");
			header('Location:index.php');
		}else{
			if(mysqli_fetch_array($addResult) != false){
				$_SESSION['usernameDuplicate'] = 'true';
				header('Location:userForm.php');
			}else{
				mysqli_query($con, "insert into user(username, password, privilege, defaultRoute, defaultBus) values('{$username}', '{$password}', '{$privilege}', '{$defaultRoute}', '{$defaultBus}')");
				header('Location:index.php');
			}
		}
	}
	
	//info table
	if(isset($_POST["infoId"]) && isset($_POST["route"]) && isset($_POST["bus"])){
		$infoId = trim($_POST['infoId']);
		$route = trim($_POST['route']);
		$bus = trim($_POST['bus']);
		$routeNo = '';
		$routeName = '';
		$waypoint = '';
		$name = '';
		
		$temp = explode(':', $route);
		$routeNo = trim(substr($temp[0], 6));
		$routeName = trim($temp[1]);

		$waypoint = parseWaypoint($waypoint);
		$name = parseStopNames($name);
		
		$result = mysqli_query($con, "SELECT * FROM info WHERE id='".$infoId."'");
		if(mysqli_fetch_array($result) != false){
			mysqli_query($con, "UPDATE info SET routeNo='".$routeNo."', routeName='".$routeName."', bus='".$bus."', waypoint='".$waypoint."', stopNames='".$name."' WHERE id='".$infoId."'");
		}else{
			mysqli_query($con, "insert into info(routeNo, routeName, bus, waypoint, stopNames) values('{$routeNo}', '{$routeName}', '{$bus}', '{$waypoint}', '{$name}')");
		}

		header("Location:index.php");
	}
	else{
		echo "Missing required fields";
	}
	
	//schedule table
	if(isset($_POST["scheduleId"]) && isset($_POST["route"]) && isset($_POST["bus"])){
		$scheduleId = trim($_POST['scheduleId']);
		$route = trim($_POST['route']);
		$bus = trim($_POST['bus']);
		$topNote = trim($_POST['topNote']);
		$bottomNote = trim($_POST['bottomNote']);
		$timetable = '';
		
		//schedule boxes
		
		for($i = 1; $i < 16; $i++){
			if($timetable != '' && substr($timetable, -1) != '|' && substr($timetable, -1) != '/'){
				$timetable .= '/';
			}
			$row = 'r'.$i;
			for($j = 1; $j < 12; $j++){
				$col = 'c'.$j;
				${$row.$col} = trim($_POST[$row.$col]);
				if(${$row.$col} != ''){
					if($timetable != '' && substr($timetable, -1) != '|' && substr($timetable, -1) != '/'){
						$timetable .= '|'.${$row.$col};
					}else{
						$timetable .= ${$row.$col};
					}
				}
			}
		}
		
		//get current date
		// Return date/time info of a timestamp; then format the output
		$mydate=getdate(date("U"));
		$date = $mydate[month]." ".$mydate[mday].", ". $mydate[year];
		
		$result = mysqli_query($con, "SELECT * FROM schedule WHERE id='".$scheduleId."'");
		if(mysqli_fetch_array($result) != false){
			mysqli_query($con, "UPDATE schedule SET route='".$route."', bus='".$bus."', date='".$date."', topNote='".$topNote."', bottomNote='".$bottomNote."', timetable='".$timetable."' WHERE id='".$scheduleId."'");
		}else{
			mysqli_query($con, "insert into schedule(route, bus, date, topNote, bottomNote, timetable) values('{$route}', '{$bus}', '{$date}', '{$topNote}', '{$bottomNote}', '{$timetable}')");
		}
		
		header("Location:index.php");
	}
	else{
		echo "Missing required fields";
	}
	
	//news table
	if(isset($_POST["newsId"]) && isset($_POST["newsTitle"]) && isset($_POST["newsContent"])){
		$newsId = trim($_POST['newsId']);
		$newsTitle = trim($_POST['newsTitle']);
		$newsContent = trim($_POST['newsContent']);
		$newsDesc = trim($_POST['newsDesc']);
		$cancelledRoute = '';
		$cancelledBus = '';
		$fromDate = '';
		$toDate = '';
		$fromTime = '';
		$toTime = '';
		
		if(isset($_POST['cancelledRoute']) && isset($_POST['cancelledBus']) && isset($_POST['fromDate'])){
			$cancelledRoute = $_POST['cancelledRoute'];
			$cancelledBus = $_POST['cancelledBus'];
			$fromDate = $_POST['fromDate'];
			if(!isset($_POST['toDate']) || $_POST['toDate'] == ''){
				$toDate = $fromDate;
			}else{
				$toDate = $_POST['toDate'];
			}
			if(isset($_POST['fromTime']) && isset($_POST['toTime'])){
				$fromTime = $_POST['fromTime'];
				$toTime = $_POST['toTime'];
				
				if($fromDate == $toDate){
					if($fromTime > $toTime){
						$fromTime = '';
						$toTime = '';
					}
				}
			}
		}
		
		//get current date
		// Return date/time info of a timestamp; then format the output
		$mydate=getdate(date("U"));
		$date = $mydate[month]." ".$mydate[mday].", ". $mydate[year];
		
		$result = mysqli_query($con, "SELECT * FROM news WHERE id='".$newsId."'");
		if(mysqli_fetch_array($result) != false){
			mysqli_query($con, "UPDATE news SET newsTitle='".$newsTitle."', newsDesc='".$newsDesc."', newsContent='".$newsContent."', date='".$date."', cancelledRoute='".$cancelledRoute."', cancelledBus='".$cancelledBus."', fromDate='".$fromDate."', toDate='".$toDate."', fromTime='".$fromTime."', toTime='".$toTime."' WHERE id='".$newsId."'");
		}else{
			mysqli_query($con, "insert into news(newsTitle, newsDesc, newsContent, date, cancelledRoute, cancelledBus, fromDate, toDate, fromTime, toTime) values('{$newsTitle}', '{$newsDesc}', '{$newsContent}', '{$date}', '{$cancelledRoute}', '{$cancelledBus}', '{$fromDate}', '{$toDate}', '{$fromTime}', '{$toTime}')");
		}
		
		header("Location:index.php");
	}
	else{
		echo "Missing required fields";
	}
	
	function testInput($data) {
		 $data = trim($data);
		 $data = stripslashes($data);
	     $data = htmlspecialchars($data);
		 return $data;
	}
	
	function parseWaypoint($waypoint){
		$waypoint1 = '';
		$waypoint2 = '';
		$waypoint3 = '';
		$waypoint4 = '';
		$waypoint5 = '';
		$waypoint6 = '';
		$waypoint7 = '';
		$waypoint8 = '';
		//setting waypoint format operations
		if(isset($_POST["name1"]) && trim($_POST['name1']) != '' && isset($_POST["lat1"]) && isset($_POST["lng1"])){
			$lat1 = trim($_POST['lat1']);
			$lng1 = trim($_POST['lng1']);
			if($lat1 != '' && $lng1 != ''){
				$waypoint1 = $lat1 . ',' . $lng1;
			}
		}
		if(isset($_POST["name2"]) && trim($_POST['name2']) != '' && isset($_POST["lat2"]) && isset($_POST["lng2"])){
			$lat2 = trim($_POST['lat2']);
			$lng2 = trim($_POST['lng2']);
			if($lat2 != '' && $lng2 != ''){
				$waypoint2 = $lat2 . ',' . $lng2;
			}
		}
		if(isset($_POST["name3"]) && trim($_POST['name3']) != '' && isset($_POST["lat3"]) && isset($_POST["lng3"])){
			$lat3 = trim($_POST['lat3']);
			$lng3 = trim($_POST['lng3']);
			if($lat3 != '' && $lng3 != ''){
				$waypoint3 = $lat3 . ',' . $lng3;
			}
		}
		if(isset($_POST["name4"]) && trim($_POST['name4']) != '' && isset($_POST["lat4"]) && isset($_POST["lng4"])){
			$lat4 = trim($_POST['lat4']);
			$lng4 = trim($_POST['lng4']);
			if($lat4 != '' && $lng4 != ''){
				$waypoint4 = $lat4 . ',' . $lng4;
			}
		}
		if(isset($_POST["name5"]) && trim($_POST['name5']) != '' && isset($_POST["lat5"]) && isset($_POST["lng5"])){
			$lat5 = trim($_POST['lat5']);
			$lng5 = trim($_POST['lng5']);
			if($lat5 != '' && $lng5 != ''){
				$waypoint5 = $lat5 . ',' . $lng5;
			}
		}
		if(isset($_POST["name6"]) && trim($_POST['name6']) != '' && isset($_POST["lat6"]) && isset($_POST["lng6"])){
			$lat6 = trim($_POST['lat6']);
			$lng6 = trim($_POST['lng6']);
			if($lat6 != '' && $lng6 != ''){
				$waypoint6 = $lat6 . ',' . $lng6;
			}
		}
		if(isset($_POST["name7"]) && trim($_POST['name7']) != '' && isset($_POST["lat7"]) && isset($_POST["lng7"])){
			$lat7 = trim($_POST['lat7']);
			$lng7 = trim($_POST['lng7']);
			if($lat7 != '' && $lng7 != ''){
				$waypoint7 = $lat7 . ',' . $lng7;
			}
		}
		if(isset($_POST["name8"]) && trim($_POST['name8']) != '' && isset($_POST["lat8"]) && isset($_POST["lng8"])){
			$lat8 = trim($_POST['lat8']);
			$lng8 = trim($_POST['lng8']);
			if($lat8 != '' && $lng8 != ''){
				$waypoint8 = $lat8 . ',' . $lng8;
			}
		}
		
		if($waypoint1 != ''){
			if($waypoint == ''){
				$waypoint = $waypoint1;
			}
		}
		if($waypoint2 != ''){
			if($waypoint == ''){
				$waypoint = $waypoint2;
			}
			else{
				$waypoint .= '|' . $waypoint2; 
			}
		}
		if($waypoint3 != ''){
			if($waypoint == ''){
				$waypoint = $waypoint3;
			}
			else{
				$waypoint .= '|' . $waypoint3; 
			}
		}
		if($waypoint4 != ''){
			if($waypoint == ''){
				$waypoint = $waypoint4;
			}
			else{
				$waypoint .= '|' . $waypoint4; 
			}
		}
		if($waypoint5 != ''){
			if($waypoint == ''){
				$waypoint = $waypoint5;
			}
			else{
				$waypoint .= '|' . $waypoint5; 
			}
		}
		if($waypoint6 != ''){
			if($waypoint == ''){
				$waypoint = $waypoint6;
			}
			else{
				$waypoint .= '|' . $waypoint6; 
			}
		}
		if($waypoint7 != ''){
			if($waypoint == ''){
				$waypoint = $waypoint7;
			}
			else{
				$waypoint .= '|' . $waypoint7; 
			}
		}
		if($waypoint8 != ''){
			if($waypoint == ''){
				$waypoint = $waypoint8;
			}
			else{
				$waypoint .= '|' . $waypoint8; 
			}
		}
		
		return $waypoint;
		//end of waypoint format operations
	}
	
	function parseStopNames($name){
		//setting stopNames format operations
		if(isset($_POST["name1"]) && isset($_POST["lat1"]) && isset($_POST["lng1"]) && trim($_POST["lat1"]) != '' && trim($_POST["lng1"]) != ''){
			$name1 = trim($_POST['name1']);
		}else{
			$name1 = '';
		}
		if(isset($_POST["name2"]) && isset($_POST["lat2"]) && isset($_POST["lng2"]) && trim($_POST["lat2"]) != '' && trim($_POST["lng2"]) != ''){
			$name2 = trim($_POST['name2']);
		}else{
			$name2 = '';
		}
		if(isset($_POST["name3"]) && isset($_POST["lat3"]) && isset($_POST["lng3"]) && trim($_POST["lat3"]) != '' && trim($_POST["lng3"]) != ''){
			$name3 = trim($_POST['name3']);
		}else{
			$name3 = '';
		}
		if(isset($_POST["name4"]) && isset($_POST["lat4"]) && isset($_POST["lng4"]) && trim($_POST["lat4"]) != '' && trim($_POST["lng4"]) != ''){
			$name4 = trim($_POST['name4']);
		}else{
			$name4 = '';
		}
		if(isset($_POST["name5"]) && isset($_POST["lat5"]) && isset($_POST["lng5"]) && trim($_POST["lat5"]) != '' && trim($_POST["lng5"]) != ''){
			$name5 = trim($_POST['name5']);
		}else{
			$name5 = '';
		}
		if(isset($_POST["name6"]) && isset($_POST["lat6"]) && isset($_POST["lng6"]) && trim($_POST["lat6"]) != '' && trim($_POST["lng6"]) != ''){
			$name6 = trim($_POST['name6']);
		}else{
			$name6 = '';
		}
		if(isset($_POST["name7"]) && isset($_POST["lat7"]) && isset($_POST["lng7"]) && trim($_POST["lat7"]) != '' && trim($_POST["lng7"]) != ''){
			$name7 = trim($_POST['name7']);
		}else{
			$name7 = '';
		}
		if(isset($_POST["name8"]) && isset($_POST["lat8"]) && isset($_POST["lng8"]) && trim($_POST["lat8"]) != '' && trim($_POST["lng8"]) != ''){
			$name8 = trim($_POST['name8']);
		}else{
			$name8 = '';
		}
		
		if($name1 != ''){
			if($name == ''){
				$name = $name1;
			}
		}
		if($name2 != ''){
			if($name == ''){
				$name = $name2;
			}
			else{
				$name .= '|' . $name2; 
			}
		}
		if($name3 != ''){
			if($name == ''){
				$name = $name3;
			}
			else{
				$name .= '|' . $name3; 
			}
		}
		if($name4 != ''){
			if($name == ''){
				$name = $name4;
			}
			else{
				$name .= '|' . $name4; 
			}
		}
		if($name5 != ''){
			if($name == ''){
				$name = $name5;
			}
			else{
				$name .= '|' . $name5; 
			}
		}
		if($name6 != ''){
			if($name == ''){
				$name = $name6;
			}
			else{
				$name .= '|' . $name6; 
			}
		}
		if($name7 != ''){
			if($name == ''){
				$name = $name7;
			}
			else{
				$name .= '|' . $name7; 
			}
		}
		if($name8 != ''){
			if($name == ''){
				$name = $name8;
			}
			else{
				$name .= '|' . $name8; 
			}
		}
		
		return $name;
		//end of stopNames format operations
	}

?>