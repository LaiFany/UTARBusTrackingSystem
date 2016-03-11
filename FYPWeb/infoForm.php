<?php
	//session
	session_start();
	
	$_SESSION['link'] = 'info';
	
	$infoId = '';
	$routeNo = '';
	$routeName = '';
	$bus = '';
	$waypoint = '';
	$stopNames = '';
	
	$lat = array('', '', '', '', '', '', '', '');
	$lng = array('', '', '', '', '', '', '', '');
	
	$eachStopName = array('', '', '', '', '', '', '', '');
	
	 if(isset($_SESSION['infoId'])){
		 $infoId = $_SESSION['infoId'];
	 }
	 if(isset($_SESSION['routeNo'])){
		 $routeNo = $_SESSION['routeNo'];
	 }
	 if(isset($_SESSION['routeName'])){
		 $routeName = $_SESSION['routeName'];
	 }
	 if(isset($_SESSION['bus'])){
		 $bus = $_SESSION['bus'];
	 }
	 if(isset($_SESSION['waypoint'])){
		 $waypoint = $_SESSION['waypoint'];
	 }
	 if(isset($_SESSION['stopNames'])){
		 $stopNames = $_SESSION['stopNames'];
	 }
	 
	 //split waypoint coordinates
	 if($waypoint != ''){
		 $coordinates = explode('|', $waypoint);
		 $length = sizeOf($coordinates);
		 for($i = 0; $i < $length; $i++){
			 $data = explode(',', $coordinates[$i]);
			 $lat[$i] = $data[0];
			 $lng[$i] = $data[1];
		 }
	 }
	 
	 //split stopNames
	 if(!empty($stopNames)){
		 $stopName = explode('|', $stopNames);
		 $length2 = sizeOf($stopName);
		 for($i = 0; $i < $length2; $i++){
			 $eachStopName[$i] = $stopName[$i];
		 }
	 }
?>
<?php include 'navBar.php'; ?>

<html>
<head>
	<meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
  <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
  <script src="http://ajax.aspnetcdn.com/ajax/jquery.validate/1.11.1/jquery.validate.js"></script>
</head>
<body>
<!--img src = "img.jpg" style = "width : 100%; height : 50%;">-->
<div class="container">
<!--
<form action="upload.php" method="post"
enctype="multipart/form-data">
<label for="file">Filename:</label>
<input type="file" name="file" id="file"><br>
<input type="submit" name="submit" value="Submit" class="btn btn-default">
</form>-->

<div class="panel panel-primary" >
      <div class="panel-heading">Adding a new Route, Bus and Waypoint</div>
      <div class="panel-body">
		<form action="postWebOperation.php" method="post"
		enctype="multipart/form-data">
		
		<div class="panel panel-info">
		  <div class="panel-heading">Route</div>
		  <div class="panel-body">
			<div class = "row form-group has-feedback">
				<label for="file" class="col-sm-2">ID No.</label>
				<div class="col-sm-4 controls">
					<input type="text" name="infoId" id="infoId" class="form-control" value = "<?php echo $infoId; ?>" readonly>
				</div>
			</div>
			<!--<div class = "row form-group has-feedback">
				<label for="file" class="col-sm-2">Route No.</label>
				<div class="col-sm-4">
					<input type="text" name="routeNo" id="routeNo" class="form-control" value = "<?php echo $routeNo; ?>">
				</div>
			</div>
			<div class = "row form-group has-feedback">
				<label for="file" class="col-sm-2">Route Name</label>
				<div class="col-sm-4">
					<input type="text" name="routeName" id="routeName" class="form-control" value = "<?php echo $routeName; ?>">
				</div>
			</div>
		  </div>
		</div>
		
		<div class="panel panel-info">
		  <div class="panel-heading">Bus</div>
		  <div class="panel-body">
			<div class = "row form-group has-feedback">
				<label for="file" class="col-sm-2">Bus</label>
				<div class="col-sm-4">
					<input type="text" name="bus" id="bus" class="form-control" value = "<?php echo $bus; ?>">
				</div>
			</div>
		  </div>
		</div>-->
		
		<div class = "row form-group has-feedback">
			<label for="file" class="col-sm-2">Route</label>
			<div class="col-sm-4 controls">
				<label for="route">Select Route (select one):</label>
				<select class="form-control" name = "route" id="route" required>
			<?php
				$con=mysqli_connect('localhost', 'root', '', 'bustrackerdb');
				mysqli_select_db($con, "bustrackerdb");
				
				//get updated data in info table
				$routeResult = mysqli_query($con, "SELECT routeNo, routeName FROM route");
				$busResult = mysqli_query($con, "SELECT busNo, busNoPlate FROM bus");
				
				if(!empty($routeResult)){
					while($row = mysqli_fetch_array($routeResult)){
				?>
						<option value = "<?php echo 'Route '.$row['routeNo'].' : '.$row['routeName'];?>" <?php if(('Route '.$routeNo.' : '.$routeName) == ('Route '.$row['routeNo'].' : '.$row['routeName'])) echo 'selected';?>><?php echo 'Route '.$row['routeNo'].' : '.$row['routeName'];?></option>
				<?php
					}
				}
				?>
				</select>
			</div>
		</div>
				
		<div class = "row form-group has-feedback">
			<label for="file" class="col-sm-2">Bus</label>
			<div class="col-sm-4 controls">
				<label for="route">Select Bus (select one):</label>
				<select class="form-control" name = "bus" id="bus" required>
				<?php
				if(!empty($busResult)){
					while($row2 = mysqli_fetch_array($busResult)){
				?>
						<option value = "<?php echo $row2['busNo'].' ('.$row2['busNoPlate'].')';?>" <?php if($bus == $row2['busNo'].' ('.$row2['busNoPlate'].')') echo 'selected';?>><?php echo 'Bus '.$row2['busNo'].' ('.$row2['busNoPlate'].')';?></option>
				<?php
					}
				?>
				</select>
				<?php
				}
				?>
			</div>
		</div>

		<div class="panel panel-info">
		  <div class="panel-heading">Waypoints</div>
		  <div class="panel-body">
		  <p>Waypoints are currently entered in decimal format (lat, lng). Convert your DMS here. <a href="http://andrew.hedges.name/experiments/convert_lat_long/">link</a></p>
			<div class="panel panel-default">
		  <div class="panel-heading">Waypoint 1</div>
		  <div class="panel-body">
			<div class = "row form-group has-feedback">
				<label for="file" class="col-sm-2">Name</label>
				<div class="col-sm-4">
					<input type="text" name="name1" id="name1" class="form-control" value = "<?php echo $eachStopName[0]; ?>">
				</div>
			</div>
			<div class = "row form-group has-feedback">
				<label for="file" class="col-sm-2">Latitude</label>
				<div class="col-sm-4">
					<input type="text" name="lat1" id="lat1" class="form-control" value = "<?php echo $lat[0]; ?>">
				</div>
			</div>
			<div class = "row form-group has-feedback">
				<label for="file" class="col-sm-2">Longitude</label>
				<div class="col-sm-4">
					<input type="text" name="lng1" id="lng1" class="form-control" value = "<?php echo $lng[0]; ?>">
				</div>
			</div>
		  </div>
		</div>
		
		<div class="panel panel-default">
		  <div class="panel-heading">Waypoint 2</div>
		  <div class="panel-body">
			<div class = "row form-group has-feedback">
				<label for="file" class="col-sm-2">Name</label>
				<div class="col-sm-4">
					<input type="text" name="name2" id="name2" class="form-control" value = "<?php echo $eachStopName[1]; ?>">
				</div>
			</div>
			<div class = "row form-group has-feedback">
				<label for="file" class="col-sm-2">Latitude</label>
				<div class="col-sm-4">
					<input type="text" name="lat2" id="lat2" class="form-control" value = "<?php echo $lat[1]; ?>">
				</div>
			</div>
			<div class = "row form-group has-feedback">
				<label for="file" class="col-sm-2">Longitude</label>
				<div class="col-sm-4">
					<input type="text" name="lng2" id="lng2" class="form-control" value = "<?php echo $lng[1]; ?>">
				</div>
			</div>
		  </div>
		</div>
		
		<div class="panel panel-default">
		  <div class="panel-heading">Waypoint 3</div>
		  <div class="panel-body">
			<div class = "row form-group has-feedback">
				<label for="file" class="col-sm-2">Name</label>
				<div class="col-sm-4">
					<input type="text" name="name3" id="name3" class="form-control" value = "<?php echo $eachStopName[2]; ?>">
				</div>
			</div>
			<div class = "row form-group has-feedback">
				<label for="file" class="col-sm-2">Latitude</label>
				<div class="col-sm-4">
					<input type="text" name="lat3" id="lat3" class="form-control" value = "<?php echo $lat[2]; ?>">
				</div>
			</div>
			<div class = "row form-group has-feedback">
				<label for="file" class="col-sm-2">Longitude</label>
				<div class="col-sm-4">
					<input type="text" name="lng3" id="lng3" class="form-control" value = "<?php echo $lng[2]; ?>">
				</div>
			</div>
		  </div>
		</div>
		
		<div class="panel panel-default">
		  <div class="panel-heading">Waypoint 4</div>
		  <div class="panel-body">
			<div class = "row form-group has-feedback">
				<label for="file" class="col-sm-2">Name</label>
				<div class="col-sm-4">
					<input type="text" name="name4" id="name4" class="form-control" value = "<?php echo $eachStopName[3]; ?>">
				</div>
			</div>
			<div class = "row form-group has-feedback">
				<label for="file" class="col-sm-2">Latitude</label>
				<div class="col-sm-4">
					<input type="text" name="lat4" id="lat4" class="form-control" value = "<?php echo $lat[3]; ?>">
				</div>
			</div>
			<div class = "row form-group has-feedback">
				<label for="file" class="col-sm-2">Longitude</label>
				<div class="col-sm-4">
					<input type="text" name="lng4" id="lng4" class="form-control" value = "<?php echo $lng[3]; ?>">
				</div>
			</div>
		  </div>
		</div>
		
		<div class="panel panel-default">
		  <div class="panel-heading">Waypoint 5</div>
		  <div class="panel-body">
			<div class = "row form-group has-feedback">
				<label for="file" class="col-sm-2">Name</label>
				<div class="col-sm-4">
					<input type="text" name="name5" id="name5" class="form-control" value = "<?php echo $eachStopName[4]; ?>">
				</div>
			</div>
			<div class = "row form-group has-feedback">
				<label for="file" class="col-sm-2">Latitude</label>
				<div class="col-sm-4">
					<input type="text" name="lat5" id="lat5" class="form-control" value = "<?php echo $lat[4]; ?>">
				</div>
			</div>
			<div class = "row form-group has-feedback">
				<label for="file" class="col-sm-2">Longitude</label>
				<div class="col-sm-4">
					<input type="text" name="lng5" id="lng5" class="form-control" value = "<?php echo $lng[4]; ?>">
				</div>
			</div>
		  </div>
		</div>
		
		<div class="panel panel-default">
		  <div class="panel-heading">Waypoint 6</div>
		  <div class="panel-body">
			<div class = "row form-group has-feedback">
				<label for="file" class="col-sm-2">Name</label>
				<div class="col-sm-4">
					<input type="text" name="name6" id="name6" class="form-control" value = "<?php echo $eachStopName[5]; ?>">
				</div>
			</div>
			<div class = "row form-group has-feedback">
				<label for="file" class="col-sm-2">Latitude</label>
				<div class="col-sm-4">
					<input type="text" name="lat6" id="lat6" class="form-control" value = "<?php echo $lat[5]; ?>">
				</div>
			</div>
			<div class = "row form-group has-feedback">
				<label for="file" class="col-sm-2">Longitude</label>
				<div class="col-sm-4">
					<input type="text" name="lng6" id="lng6" class="form-control" value = "<?php echo $lng[5]; ?>">
				</div>
			</div>
		  </div>
		</div>
		
		<div class="panel panel-default">
		  <div class="panel-heading">Waypoint 7</div>
		  <div class="panel-body">
			<div class = "row form-group has-feedback">
				<label for="file" class="col-sm-2">Name</label>
				<div class="col-sm-4">
					<input type="text" name="name7" id="name7" class="form-control" value = "<?php echo $eachStopName[6]; ?>">
				</div>
			</div>
			<div class = "row form-group has-feedback">
				<label for="file" class="col-sm-2">Latitude</label>
				<div class="col-sm-4">
					<input type="text" name="lat7" id="lat7" class="form-control" value = "<?php echo $lat[6]; ?>">
				</div>
			</div>
			<div class = "row form-group has-feedback">
				<label for="file" class="col-sm-2">Longitude</label>
				<div class="col-sm-4">
					<input type="text" name="lng7" id="lng7" class="form-control" value = "<?php echo $lng[6]; ?>">
				</div>
			</div>
		  </div>
		</div>

		<div class="panel panel-default">
		  <div class="panel-heading">Waypoint 8</div>
		  <div class="panel-body">
			<div class = "row form-group has-feedback">
				<label for="file" class="col-sm-2">Name</label>
				<div class="col-sm-4">
					<input type="text" name="name8" id="name8" class="form-control" value = "<?php echo $eachStopName[7]; ?>">
				</div>
			</div>
			<div class = "row form-group has-feedback">
				<label for="file" class="col-sm-2">Latitude</label>
				<div class="col-sm-4">
					<input type="text" name="lat8" id="lat8" class="form-control" value = "<?php echo $lat[7]; ?>">
				</div>
			</div>
			<div class = "row form-group has-feedback">
				<label for="file" class="col-sm-2">Longitude</label>
				<div class="col-sm-4">
					<input type="text" name="lng8" id="lng8" class="form-control" value = "<?php echo $lng[7]; ?>">
				</div>
			</div>
		  </div>
		</div>
		  </div>
		</div>
		
		<input type="submit" name="submit" value="Submit" class="btn btn-default pull-right">
		</form>
	  </div>
    </div>
</div>
</body>
</html>

<script>

	jQuery.validator.addMethod("customNumeric", function(value, element) { 
        return $.isNumeric(value) || value == ''; 
    }, "Value should be numeric"); 

	$('form').validate({
        rules: {
            routeNo: {
                number: true,
                required: true
            },
            routeName: {
                required: true
            },
			bus:{
				required: true
			},
			lat1:{
				customNumeric: true
			},
			lat2:{
				customNumeric: true
			},
			lat3:{
				customNumeric: true
			},
			lat4:{
				customNumeric: true
			},
			lat5:{
				customNumeric: true
			},
			lat6:{
				customNumeric: true
			},
			lat7:{
				customNumeric: true
			},
			lat8:{
				customNumeric: true
			},
			lng1:{
				customNumeric: true
			},
			lng2:{
				customNumeric: true
			},
			lng3:{
				customNumeric: true
			},
			lng4:{
				customNumeric: true
			},
			lng5:{
				customNumeric: true
			},
			lng6:{
				customNumeric: true
			},
			lng7:{
				customNumeric: true
			},
			lng8:{
				customNumeric: true
			}
        },
        highlight: function(element) {
            $(element).closest('.form-group').addClass('has-error');
        },
        unhighlight: function(element) {
            $(element).closest('.form-group').removeClass('has-error');
        },
        errorElement: 'span',
        errorClass: 'help-block',
        errorPlacement: function(error, element) {
            error.insertAfter(element);
        }
    });

</script>

<style>

	body { padding-top: 70px; }

</style>