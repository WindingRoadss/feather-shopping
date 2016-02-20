<?php  
mysql_query("set session character_set_connection=utf8;");
mysql_query("set session character_set_results=utf8;");
mysql_query("set session character_set_client=utf8;");
$con=mysqli_connect("mysql.czrx0p0th7zk.us-west-2.rds.amazonaws.com","dbmaster","Rltxjf11","FS_MySQL");  
mysqli_set_charset($con,"utf8");
if (mysqli_connect_errno($con))  
{  
   echo "Failed to connect to MySQL: " . mysqli_connect_error();  
}  

$usid = 'LDCC1';  
  
   
$res = mysqli_query($con,"select * from TB_CART where CA_USID='$usid'");
$result_num = mysql_num_rows($res);  

$result = array();

for($i=0;$i<$result_num;$i++) {
	mysql_data_seek($res, $i);       
	$row = mysql_fetch_array($res);

	$prkey = $row[CA_PRKEY];
	$prcnt = $row[CA_PRCNT];

	$res_2 = mysqli_query($con,"select * from TB_PRODUCTS where PR_KEY='$prkey'");
	$prrow = mysql_fetch_array($res_2);

	array_push($result,  
    array('PR_NAME'=>$prrow[PR_NAME],'PR_SIZE'=>$prrow[PR_SIZE],'PR_COLOR'=>$prrow[PR_COLOR]
    	,'PR_BRAND'=>$prrow[PR_BRAND], 'PR_IMAGE'=>$prrow[PR_IMAGE], 'PR_PRICE'=>$prrow[PR_PRICE] 
    )); 

}
   
echo json_encode(array("result"=>$result));  
   
mysqli_close($con);  
   
?> 

