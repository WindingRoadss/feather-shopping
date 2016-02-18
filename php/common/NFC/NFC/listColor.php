<?php
include "../common.php";  // Works.


$brand = $_REQUEST[brand]; 
$snum = $_REQUEST[serial];
$size = $_REQUEST[size]; 

//DISTINCT : list로 하나씩만 보여주기 위해 중복값 처리.
$sql ="SELECT DISTINCT PR_COLOR FROM TB_PRODUCTS 
		where PR_BRAND='$brand' and PR_SNUM='$snum' and PR_SIZE='$size'";

$result = mysql_query($sql, $connect);
$total_record = mysql_num_rows($result);

if($total_record != 0)
{
	echo "{\"status\":\"OK\",\"num_results\":\"$total_record\",\"message\":\"완료\",\"results\":[";
	
	for($i=0; $i< $total_record; $i++) 
	{
		  mysql_data_seek($result, $i);       
		  $row = mysql_fetch_array($result);
		  echo "{\"color\":\"$row[PR_COLOR]\"}";
		if($i<$total_record-1){
			echo ",";
		}
	}
	echo "]}";
}

//선택된 값에 해당하는 상품이 없는 경우
else
{
	echo "{\"status\":\"OK\",\"num_results\":\"0\",\"message\":\"완료\"}";
}

?>