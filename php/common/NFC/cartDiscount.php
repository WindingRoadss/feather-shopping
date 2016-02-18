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
  
   
$res = mysqli_query($con,"select * from TB_CART where CA_USID='$usid' and CA_PAID=0");
$result_num = mysqli_num_rows($res);  

$result = array();

for($i=0;$i<$result_num;$i++) {
	mysqli_data_seek($res, $i);       
	$row = mysqli_fetch_array($res);

	$prkey = $row[CA_PRKEY];
	$prcnt = $row[CA_PRCNT];
	
	$sql = "SELECT * FROM TB_PRODUCTS LEFT JOIN TB_BRAND ON TB_PRODUCTS.PR_BRAND=TB_BRAND.BR_NAME
	where TB_PRODUCTS.PR_KEY='$prkey'";
	$res_2 = mysqli_query($con,$sql);
	$prrow = mysqli_fetch_array($res_2);

	array_push($result,  
    array('PR_NAME'=>$prrow[PR_NAME],'PR_SIZE'=>$prrow[PR_SIZE],'PR_COLOR'=>$prrow[PR_COLOR]
    	,'PR_BRAND'=>$prrow[PR_BRAND], 'PR_IMAGE'=>$prrow[PR_IMAGE], 'PR_PRICE'=>$prrow[PR_PRICE] 
    )); 

}
   
echo json_encode(array("result"=>$result));  
echo "{\"status\":\"OK\",\"num_results\":\"0\",\"message\":\"$prrow[BR_RATE]\"}";

mysqli_close($con);  
   
?> 

