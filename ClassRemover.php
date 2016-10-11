<?php
scan(__DIR__);
function scan($path)
{
	$files = scandir($path, SCANDIR_SORT_DESCENDING);
	for ($i=0; $i < count($files); $i++) { 
		$file = $path."/".$files[$i];
		if(substr($file, -6) == ".class")
		{
			unlink($file);
		}

		//上層ファイルに行かないように
		if(substr($file, -2) == "..")
		{
			continue;
		}

		//エラー回避
		if(substr($file, -1) == ".")
		{
			continue;
		}

		if(is_dir($file))
		{
			scan($file);
		}
	}
}