<?php
include "../common.php";  // Works.

$tag = $_REQUEST[tag]; //tag 값 받아오기

$sql ="SELECT NFC_USED FROM TB_NFC where NFC_TAGID = '$tag'";
$result = mysql_query($sql, $connect);
$row = mysql_fetch_array($result);
$used = $row[NFC_USED];

//해당 tagId에 등록된 상품정보가 없는 경우
if($used=='0') {
		echo "{\"status\":\"NO\",\"num_results\":\"0\",\"message\":\"해당 tag에 등록된 상품이 없습니다\"}";	
}


//해당 tagId에 등록된 상품정보가 있는 경우
else if($used=='1') {
	
	$sql ="SELECT * FROM TB_PRODUCTS where PR_TAGID = '$tag'";
	
	$result = mysql_query($sql, $connect);	
	$total_record = mysql_num_rows($result);
	
	//tagID를 가지고 있는 row 가 두개 이상인 경우 -> 에러 처리 필요 *** 
	if($total_record>1) {
		echo "{\"status\":\"NO\",\"num_results\":\"0\",\"message\":\"관리자 오류\"}";
	}
	else {
		
		$row = mysql_fetch_array($result);
	
		echo "{\"status\":\"OK\",\"num_results\":\"1\",\"message\":\"완료\",\"results\":[";
		echo "{\"serial\":\"$row[PR_SNUM]\",\"color\":\"$row[PR_COLOR]\",\"size\":\"$row[PR_SIZE]\"";
		echo ",\"name\":\"$row[PR_NAME]\",\"brand\":\"$row[PR_BRAND]\"";
		echo ",\"price\":\"$row[PR_PRICE]\",\"stock\":\"$row[PR_STOCK]\",\"image\":\"$row[PR_IMAGE]\"}]}"; 	
	}
}

//해당 tagId가 TB_NFC에 없는 경우
else {
	echo "{\"status\":\"NO\",\"num_results\":\"0\",\"message\":\"존재하지 않는 tag Id 입니다\"}";
}
?>

