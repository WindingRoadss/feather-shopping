<?php

$db_host = "mysql.czrx0p0th7zk.us-west-2.rds.amazonaws.com:3306";
$db_user = "dbmaster";
$db_pass = "Rltxjf11";
$db_name = "FS_MySQL";

$connect = mysql_connect($db_host, $db_user, $db_pass) or die("DB 접속시 에러가 발생했습니다.");
mysql_query("set session character_set_connection=utf8;");
mysql_query("set session character_set_results=utf8;");
mysql_query("set session character_set_client=utf8;");
mysql_select_db($db_name, $connect) or die("DB Select 에러가 발생했습니다");

$id = $_REQUEST[id];
$pwd = $_REQUEST[pwd];

//$id = 'dbmaster';
//$pwd = 'Rltxjf11';

$sql ="SELECT * FROM test WHERE id = '$id'";

$result_pwd = mysql_query($sql, $connect);

$total_record = mysql_num_rows($result_pwd);
$row= mysql_fetch_array($result_pwd);
if($total_record==0 || empty($id) || empty($pwd))
    echo "{\"status\":\"NO\"}";
else
{
    $db_pwd = $row[pwd];
    if($db_pwd==$pwd)
    {
        $result = $row[user_name];
        $rtemail = $row[email];
        echo "{\"status\":\"OK\",\"num_results\":\"$total_record\",\"results\":[";
        echo "{\"user_name\":\"$result\",\"email\":\"$rtemail\"}]}";
    }
    else
    {
        echo "{\"status\":\"NO\"}";
    }
}

?>