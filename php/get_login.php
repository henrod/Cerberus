<?php
	$con = mysql_connect("localhost", "root", "12345");
	mysql_select_db("cerberus_db", $con);
	$login_java = $_GET['login_java'];
	$senha_java = $_GET['senha_java'];
	$result = mysql_query("SELECT id_rasp FROM login_id WHERE login =\"" . $login_java . "\" AND passwd = \"" . $senha_java . "\"");
	$arr = array();

	while($record = mysql_fetch_array($result)){
		$id_rasp = $record['id_rasp'];
		$arr = array('id_rasp' => $id_rasp);
	}
	//create json array here
	echo json_encode($arr);
?>
