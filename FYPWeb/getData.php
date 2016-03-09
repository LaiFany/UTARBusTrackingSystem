<?php
 
// array for JSON response
$response = array();
 
// include db connect class
$con=mysqli_connect('localhost', 'root', '', 'bustrackerdb');
	mysqli_select_db($con, "bustrackerdb");
 
// check for post data
if (isset($_GET["routeNo"])) {
    $routeNo = $_GET['routeNo'];
 
    // get a data from test table
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
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    // echoing JSON response
    echo json_encode($response);
}
?>