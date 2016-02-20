<?php
include "../common.php";  // Works.


$userid = $_REQUEST[userid]; 
$prcnt = $_REQUEST[count];

$snum = $_REQUEST[serial];
$size = $_REQUEST[size]; 
$color = $_REQUEST[color]; 
$now_key = $snum.$color.$size;
 
/*
$userid='LDCC1';
$prcnt=3;
$now_key='CC123BLACKF';
*/

//현재 넘겨진 상품 정보를 검색하여 받아옴 
$sql ="SELECT * FROM TB_PRODUCTS where PR_KEY='$now_key'";
$result = mysql_query($sql, $connect);
$total_record = mysql_num_rows($result);

//tagid로 등록된 상품이 2개 이상인 경우 num_results=0으로 결과 반환. 관리자 오류
if($total_record != 1) {
   echo "{\"status\":\"NO\",\"num_results\":\"0\",\"message\":\"관리 오류\"}";
}
else {
	
	$sql = "INSERT TB_DELIVERY(DL_USID, DL_PRKEY, DL_PRCNT) 
			VALUE('$userid', '$now_key', '$prcnt')";
	mysql_query($sql, $connect);	
	
	echo "{\"status\":\"OK\",\"num_results\":\"0\",\"message\":\"업데이트 완료\"}";

}

?>