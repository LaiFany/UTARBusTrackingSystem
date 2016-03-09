<?php
//set directory path
								$target_path = "uploads/";
								//add filename to target path. e.g. uploads/filename.extension
								$target_path = $target_path . basename($_FILES['file']['name']);
									
										// upload file
										if (move_uploaded_file($_FILES['file']['tmp_name'], $target_path)) {
											//mysqli_query($mysqli,"INSERT INTO image (album_id, member_id, name, description, date, url) VALUES ('$album_id', {$_SESSION['id']}, '$name', '$description','$date','$url')");
											echo "The photo has been uploaded.";
										} 
										else {
											echo "Sorry, there was an error uploading your file.";
										}
?>