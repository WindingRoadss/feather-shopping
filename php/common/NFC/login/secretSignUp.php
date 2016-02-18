<?php

include "../common.php";  // Works.
include "security.php";  // ���Ⱥκ�

//$id = $_REQUEST[id];
//$pwd = $_REQUEST[pwd];

$id = 'user2';
$pwd = 'User2';


if(empty($id) || empty($pwd))
    echo "{\"status\":\"NO\"}";

else
{

$key = "this is a key for encryption";     // ������ �� �� �ִ� key ��
$encryption = encryption($pwd, $key);        //encryption

$sql ="INSERT INTO test(id, pwd, user_name, email) VALUE('$id', '$encryption', 'user1', 'user1@lotte.net')";

mysql_query($sql, $connect);
echo "{\"status\":\"OK\",\"num_results\":\"0\",\"message\":\"회원가입 완료\"}";


}

?>