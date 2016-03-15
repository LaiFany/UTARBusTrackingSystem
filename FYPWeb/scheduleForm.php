<?php
	//session
	if(!isset($_SESSION)){ 
			session_start(); 
	} 
	
	$_SESSION['link'] = 'schedule';
	
	$scheduleId = '';
	$scheduleRoute = '';
	$scheduleBus = '';
	$scheduleTopNote = '';
	$scheduleBottomNote = '';
	$scheduleTimetable = '';
	$scheduleDate = '';
	
	 if(isset($_SESSION['scheduleId'])){
		 $scheduleId = $_SESSION['scheduleId'];
	 }
	 if(isset($_SESSION['scheduleRoute'])){
		 $scheduleRoute = $_SESSION['scheduleRoute'];
	 }
	 if(isset($_SESSION['scheduleBus'])){
		 $scheduleBus = $_SESSION['scheduleBus'];
	 }
	 if(isset($_SESSION['scheduleTopNote'])){
		 $scheduleTopNote = $_SESSION['scheduleTopNote'];
	 }
	 if(isset($_SESSION['scheduleBottomNote'])){
		 $scheduleBottomNote = $_SESSION['scheduleBottomNote'];
	 }
	 if(isset($_SESSION['scheduleTimetable'])){
		 $scheduleTimetable = $_SESSION['scheduleTimetable'];
	 }
	 if(isset($_SESSION['scheduleDate'])){
		 $scheduleDate = $_SESSION['scheduleDate'];
	 }
	 
	 //plotting schedule
	 if($scheduleTimetable != ''){
		 $one = 1;
		 $row = explode('/', $scheduleTimetable);
		 $length = sizeOf($row);
		 for($i = 0; $i < $length; $i++){
			 if($row[$i] != ''){
				 $col = explode('|', $row[$i]);
				 $length2 = sizeOf($col);
				 for($j = 0; $j < $length2; $j++){
					 if($col[$j] != ''){
						 ${'r'.($i + $one).'c'.($j + $one)} = $col[$j];
					 }
				 }
			 }
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
<div class="container">

<div class="panel panel-primary">
      <div class="panel-heading">Schedule</div>
      <div class="panel-body">
	  <form id = "noteForm" action="postWebOperations.php" method="post"
		enctype="multipart/form-data">
		<div class="panel panel-info">
		  <div class="panel-heading">Information</div>
		  <div class="panel-body">
		  <div class = "row form-group has-feedback">
				<label for="file" class="col-sm-2">ID No.</label>
				<div class="col-sm-4 controls">
					<input type="text" name="scheduleId" id="scheduleId" class="form-control" value = "<?php echo $scheduleId; ?>" readonly>
				</div>
			</div>
	  <?php
		$con=mysqli_connect('localhost', 'root', '', 'bustrackerdb');
		mysqli_select_db($con, "bustrackerdb");
		
		//get updated data in info table
		$routeResult = mysqli_query($con, "SELECT routeNo, routeName FROM info");
		$busResult = mysqli_query($con, "SELECT bus FROM info");
		
		if(!empty($routeResult) && !empty($busResult)){
	  ?>
		<label for="route">Select Route (select one):</label>
		<select class="form-control" name = "route" id="route">
		<?php
			while($row = mysqli_fetch_array($routeResult)){
		?>
				<option value = "<?php echo 'Route '.$row['routeNo'].' : '.$row['routeName'];?>" <?php if($scheduleRoute == ('Route '.$row['routeNo'].' : '.$row['routeName'])) echo 'selected';?>><?php echo 'Route '.$row['routeNo'].' : '.$row['routeName'];?></option>
		<?php
			}
		?>
		</select>
		
		<label for="route">Select Bus (select one):</label>
		<select class="form-control" name = "bus" id="bus">
		<?php
			while($row2 = mysqli_fetch_array($busResult)){
		?>
				<option value = "<?php echo 'Bus '.$row2['bus'];?>" <?php if($scheduleBus == ('Bus '.$row2['bus'])) echo 'selected';?>><?php echo 'Bus '.$row2['bus'];?></option>
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
      <div class="panel-heading">Notes</div>
      <div class="panel-body">
		<div class = "row form-group has-feedback">
			<label for="file" class="col-sm-2">Top Notes</label>
			<div class="col-sm-4 controls">
				<textarea rows="4" cols="50" name="topNote" id="topNote" class="form-control"><?php echo $scheduleTopNote;?></textarea>
			</div>
		</div>
	  </div>
    </div>

	<div class="panel panel-info">
      <div class="panel-heading">Timetable</div>
      <div class="panel-body">
		<div class = "table-responsive">
			<p> Do avoid using '|' and '/', as they are used as indicators in the table</p>
			<table class = "table table-hover">
				<thead>
					<?php
						for($i = 1; $i < 2; $i++){
							$row = 'r'.$i;
							echo '<tr>';
							for($j = 1; $j < 12; $j++){
								$col = 'c'.$j;
								if(array_key_exists($row.$col, $GLOBALS)){
									echo '<th><input type="text" name="'.$row.$col.'" id="'.$row.$col.'" class = "form-control" value = "'.${$row.$col}.'"></th>';
								}else{
									echo '<th><input type="text" name="'.$row.$col.'" id="'.$row.$col.'" class = "form-control" ></th>';
								}
							}
							echo '</tr>';
						}
					?>
				</thead>
				<tbody>
					<?php
						for($i = 2; $i < 16; $i++){
							$row = 'r'.$i;
							echo '<tr>';
							for($j = 1; $j < 12; $j++){
								$col = 'c'.$j;
								if(array_key_exists($row.$col, $GLOBALS)){
									echo '<td><input type="text" name="'.$row.$col.'" id="'.$row.$col.'" class = "form-control" value = "'.${$row.$col}.'"></td>';
								}else{
									echo '<td><input type="text" name="'.$row.$col.'" id="'.$row.$col.'" class = "form-control" ></td>';
								}
							}
							echo '</tr>';
						}
					?>
				</tbody>
			</table>
		</div>
	  </div>
    </div>
	
	<div class="panel panel-info">
      <div class="panel-heading">Notes</div>
      <div class="panel-body">
		<div class = "row form-group has-feedback">
			<label for="file" class="col-sm-2">Bottom Notes</label>
			<div class="col-sm-4 controls">
				<textarea rows="4" cols="50" name="bottomNote" id="bottomNote" class="form-control"><?php echo $scheduleBottomNote; ?></textarea>
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

</script>

<style>

	body { padding-top: 70px; }

</style>