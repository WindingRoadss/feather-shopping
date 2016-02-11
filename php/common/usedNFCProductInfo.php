<?php
include "common.php";  // Works.

$tag = $_REQUEST[tag]; //tag 값 받아오기
 
$sql ="SELECT * FROM TB_PRODUCTS where PR_TAGID = '$tag'";

$result = mysql_query($sql, $connect);	
$total_record = mysql_num_rows($result);
$row = mysql_fetch_array($result);

$brand = $row[PR_BRAND];
$name = $row[PR_NAME];
$size = $row[PR_SIZE]; 
$color = $row[PR_COLOR]; 
$price = $row[PR_PRICE];
$stock = $row[PR_STOCK];

//현재 저장된 물품정보를 수정하는 경우 KEY 값을 저장해 두어 해당 row에 tagid를 지워주도록 한다.
$key = $row[PR_KEY];

//tagID를 가지고 있는 row 가 두개 이상인 경우 -> 에러 처리 필요 ***
if($total_record>1) {
	echo "{\"status\":\"OK\",\"num_results\":\"$total_record\"}";
}

echo "{\"status\":\"OK\",\"num_results\":\"$total_record\",\"results\":[";
echo "{\"brand\":\"$brand\",\"name\":\"$name\",\"size\":\"$size\",\"color\":\"$color\",
	\"price\":\"$price\",\"stock\":\"$stock\",\"key\":\"$key\"}]}"; 

?>