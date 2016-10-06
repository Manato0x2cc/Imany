<?php

$r = 70;
$g = 178;
$b = 109;

$rgb  = ($r << 16) | ($g << 8) | ($b);

echo $rgb;
echo "\n";

$r1 = ($rgb & 0xff0000) >> 16;
$g1 = ($rgb & 0xff00) >> 8;
$b1 = ($rgb & 0xff);

echo $r1." ".$g1." ".$b1;
echo "\n";