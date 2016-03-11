<?php
	//session
	session_start();
	
	$_SESSION['link'] = 'bus';
	
	$busBusId = '';
	$busBusNo = '';
	$busBusNoPlate = '';
	
	 if(isset($_SESSION['busBusId'])){
		 $busBusId = $_SESSION['busBusId'];
	 }
	 if(isset($_SESSION['busBusNo'])){
		 $busBusNo = $_SESSION['busBusNo'];
	 }
	 if(isset($_SESSION['busBusNoPlate'])){
		 $busBusNoPlate = $_SESSION['busBusNoPlate'];
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
      <div class="panel-heading">Adding Bus</div>
      <div class="panel-body">
		<form id = "busForm" action="postWebOperation.php" method="post"
		enctype="multipart/form-data">
		<div class = "row form-group has-feedback">
			<label for="file" class="col-sm-2">ID No.</label>
			<div class="col-sm-4 controls">
				<input type="text" name="busBusId" id="busBusId" class="form-control" value = "<?php echo $busBusId; ?>" readonly>
			</div>
		</div>
		<div class = "row form-group has-feedback">
			<label for="file" class="col-sm-2">Bus No.</label>
			<div class="col-sm-4 controls">
				<input type="text" name="busBusNo" id="busBusNo" class="form-control" value = "<?php echo $busBusNo; ?>" required>
			</div>
		</div>
		<div class = "row form-group has-feedback">
			<label for="file" class="col-sm-2">Bus No. Plate</label>
			<div class="col-sm-4 controls">
				<input type="text" name="busBusNoPlate" id="busBusNoPlate" class="form-control" value = "<?php echo $busBusNoPlate; ?>" required>
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
            busBusNo: {
                required: true
            },
            busBusNoPlate: {
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