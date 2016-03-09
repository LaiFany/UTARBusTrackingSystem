<?php
 
// array for JSON response
$response = array();
 
// include db connect class
$con=mysqli_connect('localhost', 'root', '', 'bustrackerdb');
	mysqli_select_db($con, "bustrackerdb");
 
// get a data from test table
    $result = mysqli_query($con, "SELECT * FROM info");
 
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
				//$rows["row"] = $r;
				array_push($response["data"], $r);
			}
			
			//echo result as json
			echo json_encode($response);
        } else {
            // no data found
            $response["success"] = 0;
            $response["message"] = "No data found";
 
            // echo no users JSON
            echo json_encode($response);
        }
    } else {
        // no product found
        $response["success"] = 0;
        $response["message"] = "No data found";
 
        // echo no users JSON
        echo json_encode($response);
    }
?>