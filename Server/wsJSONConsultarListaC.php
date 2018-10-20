<?PHP
$hostname_localhost="localhost";
$database_localhost="id7451289_bd_restaurante";
$username_localhost="id7451289_grupo02";
$password_localhost="123456";

$json=array();
				
		$conexion = mysqli_connect($hostname_localhost,$username_localhost,$password_localhost,$database_localhost);
		mysqli_set_charset($conexion,"utf8"); // Agregar UTF-8

		$consulta="select * from comida";
		$resultado=mysqli_query($conexion,$consulta);
		
		while($registro=mysqli_fetch_array($resultado)){
			$json['comidaArrJson'][]=$registro;
		}
		mysqli_close($conexion);
		echo json_encode($json, JSON_UNESCAPED_UNICODE);  // Agregar JSON_UNESCAPED_UNICODE
		

?>