<?php
include "../common.php";  // Works.


$userid = $_REQUEST[userid]; 
$prcnt = $_REQUEST[count]; 

$tag = $_REQUEST[tag]; 
$snum = $_REQUEST[serial];
$size = $_REQUEST[size]; 
$color = $_REQUEST[color]; 
$now_key = $snum.$color.$size;

/*
$userid='LDCC2';
$prcnt=3;
$now_key='AD12WHITEM';
*/


$sql ="SELECT PR_STOCK FROM TB_PRODUCTS where PR_KEY='$now_key'";
$result = mysql_query($sql, $connect);
$total_record = mysql_num_rows($result);//DB에 추출된 결과행이 몇개인지 세어주는 변수

$row = mysql_fetch_array($result);
$stock = $row[PR_STOCK];

if($total_record!=1) {
	echo "{\"status\":\"NO\",\"num_results1\":\"$total_record\"}";
}
else {
if($stock-$prcnt<0 || $prcnt==0) {
	echo "{\"status\":\"NO\",\"num_results2\":\"$total_record\"}";
}
else {
        //셀렉트 올을 해서 동일한게 있는게 확인하고 , 행의 수 확인하는거 그걸로
        $sql ="SELECT * FROM TB_CART where CA_PRKEY = '$now_key' and CA_USID='$userid' and CA_PAID=0";
        $result = mysql_query($sql, $connect);	
        $total_record = mysql_num_rows($result);

        //구매 경험 확인
        if($total_record >0) {
              $sql="UPDATE TB_CART SET CA_PRCNT=CA_PRCNT+'$prcnt' where CA_PRKEY='$now_key' and CA_USID='$userid' and CA_PAID=0";
              mysql_query($sql, $connect);
              echo "{\"status\":\"OK\",\"num_results\":\"$total_record\"}";
        }
        else{
                $sql="INSERT INTO TB_CART(CA_USID, CA_PRCNT, CA_PAID, CA_PRKEY) VALUES('$userid', '$prcnt', 0, '$now_key')";
                mysql_query($sql, $connect);
                echo "{\"status\":\"OK\",\"num_results\":\"$total_record\"}";
        }
}
}


?>