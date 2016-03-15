<?php
	//session
	if(!isset($_SESSION)){ 
			session_start(); 
	} 
	
	$_SESSION['link'] = 'route';
	
	$routeRouteId = '';
	$routeRouteNo = '';
	$routeRouteName = '';
	
	 if(isset($_SESSION['routeRouteId'])){
		 $routeRouteId = $_SESSION['routeRouteId'];
	 }
	 if(isset($_SESSION['routeRouteNo'])){
		 $routeRouteNo = $_SESSION['routeRouteNo'];
	 }
	 if(isset($_SESSION['routeRouteName'])){
		 $routeRouteName = $_SESSION['routeRouteName'];
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
      <div class="panel-heading">Adding Route</div>
      <div class="panel-body">
		<form id = "routeForm" action="postWebOperations.php" method="post"
		enctype="multipart/form-data">
		<div class = "row form-group has-feedback">
			<label for="file" class="col-sm-2">ID No.</label>
			<div class="col-sm-4 controls">
				<input type="text" name="routeRouteId" id="routeRouteId" class="form-control" value = "<?php echo $routeRouteId; ?>" readonly>
			</div>
		</div>
		<div class = "row form-group has-feedback">
			<label for="file" class="col-sm-2">Route No.</label>
			<div class="col-sm-4 controls">
				<input type="text" name="routeRouteNo" id="routeRouteNo" class="form-control" value = "<?php echo $routeRouteNo; ?>" required>
			</div>
		</div>
		<div class = "row form-group has-feedback">
			<label for="file" class="col-sm-2">Route Name</label>
			<div class="col-sm-4 controls">
				<input type="text" name="routeRouteName" id="routeRouteName" class="form-control" value = "<?php echo $routeRouteName; ?>" required>
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
            routeRouteNo: {
                required: true
            },
            routeRouteName: {
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

<style>

	body { padding-top: 70px; }

</style>