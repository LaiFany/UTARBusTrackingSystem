<?php
	//session
	if(!isset($_SESSION)){ 
			session_start(); 
	} 
	
	$_SESSION['link'] = 'news';
	
	$newsId = '';
	$newsTitle = '';
	$newsDesc = '';
	$newsContent = '';
	$cancelledRoute = '';
	$cancelledBus = '';
	$fromDate = '';
	$toDate = '';
	$fromTime = '';
	$toTime = '';
	
	 if(isset($_SESSION['newsId'])){
		 $newsId = $_SESSION['newsId'];
	 }
	 if(isset($_SESSION['newsTitle'])){
		 $newsTitle = $_SESSION['newsTitle'];
	 }
	 if(isset($_SESSION['newsDesc'])){
		 $newsDesc = $_SESSION['newsDesc'];
	 }
	 if(isset($_SESSION['newsContent'])){
		 $newsContent = $_SESSION['newsContent'];
	 }
	 if(isset($_SESSION['cancelledRoute'])){
		 $cancelledRoute = $_SESSION['cancelledRoute'];
	 }
	 if(isset($_SESSION['cancelledBus'])){
		 $cancelledBus = $_SESSION['cancelledBus'];
	 }
	 if(isset($_SESSION['fromDate'])){
		 $fromDate = $_SESSION['fromDate'];
	 }
	 if(isset($_SESSION['toDate'])){
		 $toDate = $_SESSION['toDate'];
	 }
	 if(isset($_SESSION['fromTime'])){
		 $fromTime = $_SESSION['fromTime'];
	 }
	 if(isset($_SESSION['toTime'])){
		 $toTime = $_SESSION['toTime'];
	 }
?>
<?php include 'navBar.php'; ?>

<html>
<head>
	<meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
  <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
  <script src="http://ajax.aspnetcdn.com/ajax/jquery.validate/1.11.1/jquery.validate.js"></script>
  
  <!-- Include Bootstrap Datepicker -->
  <link rel="stylesheet" href="//cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.3.0/css/datepicker.min.css" />
  <link rel="stylesheet" href="//cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.3.0/css/datepicker3.min.css" />
  <script src="//cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.3.0/js/bootstrap-datepicker.min.js"></script>
  
  <!--- Include Bootstrap Timepicker -->
  <link rel="stylesheet" type="text/css" href="http://weareoutman.github.io/clockpicker/dist/jquery-clockpicker.min.css">
  <script type="text/javascript" src="http://weareoutman.github.io/clockpicker/dist/jquery-clockpicker.min.js"></script>
</head>
<body>
<div class="container">

	<div class="panel panel-primary">
      <div class="panel-heading">Adding News</div>
      <div class="panel-body">
		<form id = "newsForm" action="postWebOperations.php" method="post"
		enctype="multipart/form-data">
		<div class = "row form-group has-feedback">
			<label for="file" class="col-sm-2">ID No.</label>
			<div class="col-sm-4 controls">
				<input type="text" name="newsId" id="newsId" class="form-control" value = "<?php echo $newsId; ?>" readonly>
			</div>
		</div>
		<div class = "row form-group has-feedback">
			<label for="file" class="col-sm-2">News Title</label>
			<div class="col-sm-4 controls">
				<input type="text" name="newsTitle" id="newsTitle" class="form-control" value = "<?php echo $newsTitle; ?>">
			</div>
		</div>
		<div class = "row form-group has-feedback">
			<label for="file" class="col-sm-2">News Description</label>
			<div class="col-sm-4 controls">
				<input type="text" name="newsDesc" id="newsDesc" class="form-control" value = "<?php echo $newsDesc; ?>">
			</div>
		</div>
		
		<div class="panel panel-info">
			<div class="panel-heading">Bus Cancellation</div>
				<div class="panel-body">
				
					<div class = "row form-group has-feedback">
						<label for="file" class="col-sm-2">Route</label>
						<div class="col-sm-4 controls">
							<label for="route">Select Route (select one):</label>
							<select class="form-control" name="cancelledRoute" id="cancelledRoute" required>
								<option value = "none" <?php if($cancelledRoute == "none") echo 'selected';?>>None</option>
						<?php
							$con=mysqli_connect('localhost', 'root', '', 'bustrackerdb');
							mysqli_select_db($con, "bustrackerdb");
							
							//get updated data in info table
							$routeResult = mysqli_query($con, "SELECT routeNo, routeName FROM info");
							$busResult = mysqli_query($con, "SELECT bus FROM info");
							
							if(!empty($routeResult)){
								while($row = mysqli_fetch_array($routeResult)){
							?>
									<option value = "<?php echo 'Route '.$row['routeNo'].' : '.$row['routeName'];?>" <?php if($cancelledRoute == ('Route '.$row['routeNo'].' : '.$row['routeName'])) echo 'selected';?>><?php echo 'Route '.$row['routeNo'].' : '.$row['routeName'];?></option>
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
							<select class="form-control" name="cancelledBus" id="cancelledBus" required>
								<option value = "none" <?php if($cancelledBus == "none") echo 'selected';?>>None</option>
							<?php
							if(!empty($busResult)){
								while($row2 = mysqli_fetch_array($busResult)){
							?>
									<option value = "<?php echo 'Bus '.$row2['bus'];?>" <?php if($cancelledBus == ('Bus '.$row2['bus'])) echo 'selected';?>><?php echo 'Bus '.$row2['bus'];?></option>
							<?php
								}
							?>
							</select>
							<?php
							}
							?>
						</div>
					</div>
				
					<div class="row form-group has-feedback">
						<label class="col-sm-2 control-label">From</label>
						<div class="col-sm-4 date">
							<div class="input-group input-append date"  id = "fromDatePicker">
								<input type="text" class="form-control" id="fromDate" name="fromDate" value = "<?php echo $fromDate; ?>" readonly style="background-color:white;"/>
								<span class="input-group-addon add-on"><span class="glyphicon glyphicon-calendar"></span></span>
							</div>
						</div>
						<div class="col-sm-4 input-group" id = "fromTimePicker">
							<input type="text" class="form-control" id="fromTime" name="fromTime" value="<?php echo $fromTime; ?>" readonly style="background-color:white;">
							<span class="input-group-addon">
								<span class="glyphicon glyphicon-time"></span>
							</span>
						</div>
					</div>
					<div class="row form-group has-feedback">
						<label class="col-sm-2 control-label">To</label>
						<div class="col-sm-4 date">
							<div class="input-group input-append date" id = "toDatePicker">
								<input type="text" class="form-control" id="toDate" name="toDate" value = "<?php echo $toDate; ?>" readonly style="background-color:white;"/>
								<span class="input-group-addon add-on"><span class="glyphicon glyphicon-calendar"></span></span>
							</div>
						</div>
						<div class="col-sm-4 input-group" id = "toTimePicker">
							<input type="text" class="form-control" id="toTime" name="toTime" value="<?php echo $toTime; ?>" readonly style="background-color:white;">
							<span class="input-group-addon">
								<span class="glyphicon glyphicon-time"></span>
							</span>
						</div>
					</div>
				</div>
		</div>
		<div class = "row form-group has-feedback">
			<label for="file" class="col-sm-2">News Content</label>
			<div class="col-sm-4 controls">
				<textarea rows="4" cols="50" name="newsContent" id="newsContent" class="form-control"><?php echo $newsContent; ?></textarea>
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

	$('form').validate({
        rules: {
            newsTitle: {
                required: true
            },
            newsContent: {
                required: true
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

<script>
$(document).ready(function() {
    $('#fromDatePicker')
        .datepicker({
            format: 'dd MM yyyy',
			startDate: '0d',
			autoclose: true
        })
        .on('changeDate', function(e) {
			//change starting date of fromDate
			//$("#toDatePicker").datepicker("setDate", $('#fromDate').val());
			//$('#toDate').val($('#fromDate').val());
			$('#toDatePicker').datepicker('setStartDate', $('#fromDate').val());
			if($('#fromDate').val() == ""){
				$('#toDate').val("");
			}
        });
		
	$('#toDatePicker')
        .datepicker({
            format: 'dd MM yyyy',
			startDate: '0d',
			autoclose: true
        })
        .on('changeDate', function(e) {
        });
		
	$('#fromTimePicker').clockpicker({
		autoclose: true
	});
	
	$('#toTimePicker').clockpicker({
		autoclose: true
	});
});
</script>

<style>

	body { padding-top: 70px; }

</style>