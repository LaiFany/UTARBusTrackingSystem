<?php

	// array for JSON response
	$response = array();
	 
	// include db connect class
	$con=mysqli_connect('localhost', 'root', '', 'bustrackerdb');
		mysqli_select_db($con, "bustrackerdb");

	if(isset($_GET['info']) || isset($_GET['news']) || isset($_GET['schedule']) ||  isset($_GET['user'])){
		//get from info table
		if(isset($_GET['info'])){
			$result = mysqli_query($con, "SELECT * FROM info");
		}
		
		//get from news table
		if(isset($_GET['news'])){
			$result = mysqli_query($con, "SELECT * FROM news");
		}
		
		//get from schedule table
		if(isset($_GET['schedule'])){
			$result = mysqli_query($con, "SELECT * FROM schedule");
		}
		
		//get from user table
		if(isset($_GET['user'])){
			$result = mysqli_query($con, "SELECT * FROM user");
		}
		
		if (!empty($result)) {
				// check for empty result
				if (mysqli_num_rows($result) > 0) {
					
					$rows = array();
					// success
						$response["success"] = 1;
					// user node
					$response["data"] = array();
					
					//retrieve and print every record
					while($r = mysqli_fetch_assoc($result)){
						array_push($response["data"], $r);
					}
					
					//echo result as json
					echo json_encode($response);
				} else {
					// no data found
					$response["success"] = 0;
					$response["message"] = "No data found";
		 
					// echo no data JSON
					echo json_encode($response);
				}
		} else {
				// no data found
				$response["success"] = 0;
				$response["message"] = "No data found";
		 
				// echo no data JSON
				echo json_encode($response);
		}
	}else if(isset($_GET['routeNo'])){
		$routeNo = $_GET['routeNo'];
	 
		// get from location table
		$result = mysqli_query($con, "SELECT * FROM location WHERE routeNo = '".$routeNo."' ORDER BY id DESC LIMIT 1");
	 
		if (!empty($result)) {
			// check for empty result
			if (mysqli_num_rows($result) > 0) {
	 
				$result = mysqli_fetch_array($result);
	 
				$data = array();
				$data["id"] = $result["id"];
				$data["routeNo"] = $result["routeNo"];
				$data["routeName"] = $result["routeName"];
				$data["bus"] = $result["bus"];
				$data["lat"] = $result["lat"];
				$data["lng"] = $result["lng"];
				$data["passengers"] = $result["passengers"];
				// success
				$response["success"] = 1;
	 
				// user node
				$response["data"] = array();
	 
				array_push($response["data"], $data);
	 
				// echoing JSON response
				echo json_encode($response);
			} else {
				// no data found
				$response["success"] = 0;
				$response["message"] = "No data found";
	 
				// echo no data JSON
				echo json_encode($response);
			}
		} else {
			// no data found
			$response["success"] = 0;
			$response["message"] = "No data found";
	 
			// echo no data JSON
			echo json_encode($response);
		}
	}
		
	
?>