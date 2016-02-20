<?php
include "../common.php";  // Works.
/* NFC 태깅 후 상품을 확인하고 장바구니 버튼을 눌렀을 경우에 실행. 해당 상품을 TB_CART에 넣어준다. */

$userid = $_REQUEST[userid]; 
$prcnt = $_REQUEST[count];

$snum = $_REQUEST[serial];
$size = $_REQUEST[size]; 
$color = $_REQUEST[color]; 
$now_key = $snum.$color.$size;

/*
$userid='LDCC2';
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
	
	$row = mysql_fetch_array($result);
	$stock = $row[PR_STOCK];
	$price = $row[PR_PRICE];
	$tot_price = $prcnt * $price; //선택된 수량 * 상품 가격
	
	//재고가 부족한 경우.
	if($stock-$prcnt < 0) {
		echo "{\"status\":\"NO\",\"num_results\":\"0\",\"message\":\"재고가 부족합니다\"}";
	}
	
	else {
		
		$sql = "SELECT * FROM TB_CART where CA_USID='$userid' and CA_PRKEY='$now_key' and CA_PAID=0";
		$result = mysql_query($sql, $connect);	
		$total_record = mysql_num_rows($result);
		
		//같은 사용자가 같은 상품을 추가 등록했을 경우
		if($total_record>0) {
			$sql = "UPDATE TB_CART SET CA_PRCNT=CA_PRCNT+'$prcnt', CA_TOT_PRICE=CA_TOT_PRICE+'$tot_price' 
					where CA_USID='$userid' and CA_PRKEY='$now_key' and CA_PAID=0";
			mysql_query($sql, $connect);
		}
		//사용자가 새로운 상품을 등록했을 경우
		else {
			$sql = "INSERT TB_CART(CA_USID, CA_PRCNT, CA_PAID, CA_PRKEY, CA_TOT_PRICE) 
					VALUE('$userid', '$prcnt', 0, '$now_key', '$tot_price')";
			mysql_query($sql, $connect);			
		}
		echo "{\"status\":\"OK\",\"num_results\":\"0\",\"message\":\"업데이트 완료\"}";
	}
}

?>