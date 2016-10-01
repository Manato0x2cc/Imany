<?php

$gamma = 1.5;
for ($i=0; $i < 256; $i++) { 
	$r = (int) ((($i / 255) ** (1/$gamma)) * 255);
	$r.=",";
	echo $r;
}
echo "\n".$i;