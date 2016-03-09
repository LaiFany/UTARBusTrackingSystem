<?php

	$con=mysqli_connect('localhost', 'root', '', 'bustrackerdb');
	mysqli_select_db($con, "bustrackerdb");
	
	if(isset($_POST["infoId"]) && isset($_POST["routeNo"]) && isset($_POST["routeName"]) && isset($_POST["bus"])){
		$infoId = trim($_POST['infoId']);
		$routeNo = trim($_POST['routeNo']);
		$routeName = trim($_POST['routeName']);
		$bus = trim($_POST['bus']);
		$waypoint = '';
		$name = '';

		$waypoint = parseWaypoint($waypoint);
		$name = parseStopNames($name);
		
		$result = mysqli_query($con, "SELECT * FROM info WHERE id='".$infoId."'");
		if(mysqli_fetch_array($result) != false){
			mysqli_query($con, "UPDATE info SET routeNo='".$routeNo."', routeName='".$routeName."', bus='".$bus."', waypoint='".$waypoint."', stopNames='".$name."' WHERE id='".$infoId."'");
		}else{
			mysqli_query($con, "insert into info(routeNo, routeName, bus, waypoint, stopNames) values('{$routeNo}', '{$routeName}', '{$bus}', '{$waypoint}', '{$name}')");
		}

		header("Location:home.php");
	}
	else{
		echo "Missing required fields";
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