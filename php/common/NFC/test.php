<?php  

$con=mysqli_connect("mysql.czrx0p0th7zk.us-west-2.rds.amazonaws.com","dbmaster","Rltxjf11","FS_MySQL");  
mysqli_set_charset($con,"utf8");
if (mysqli_connect_errno($con))  
{  
   echo "Failed to connect to MySQL: " . mysqli_connect_error();  
}  
  
  
   
$res = mysqli_query($con,"select * from TB_PRODUCTS");  
   
$result = array();  
   
while($row = mysqli_fetch_array($res)){  
  array_push($result,  
    array('PR_NAME'=>$row[4],'PR_SIZE'=>$row[3],'PR_COLOR'=>$row[2],'PR_BRAND'=>$row[5], 'PR_IMAGE'=>$row[8]  
    ));  
}  
   
echo json_encode(array("result"=>$result));  
   
mysqli_close($con);  
   
?> 

