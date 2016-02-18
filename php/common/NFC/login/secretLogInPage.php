<?php

include "../common.php";  // Works.
include "security.php";  // ���Ⱥκ�

//$id = $_REQUEST[id];
//$pwd = $_REQUEST[pwd];

$id = 'user1';
$pwd = 'User1';


//����ִ� ��
if(empty($id) || empty($pwd)) {
    echo "{\"status\":\"NO\"}";
}

$sql ="SELECT * FROM test WHERE id = '$id'";

$result_pwd = mysql_query($sql, $connect);
$total_record = mysql_num_rows($result_pwd);

//�������� �ʴ� ID
if ($total_record!=1) {
    echo "{\"status\":\"NO\"}";
}

else
{
	$row= mysql_fetch_array($result_pwd);
	$db_pwd=$row[pwd];
	
	$key = "this is a key for encryption";     // ������ �� �� �ִ� key ��
	$encryption = encryption($pwd, $key);        //encryption
	
	$str = strcmp($db_pwd, $pwd); //문자열 비교
	if($str){ //다른 경우
		echo "{\"status\":\"OK\",\"num_results\":\"$total_record\"}";
	}
	else
	{
		echo "{\"status\":\"NO\"}";
	}

}

?>