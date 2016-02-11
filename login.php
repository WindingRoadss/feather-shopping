<?php
//변경
include "common.php";  // Works.

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