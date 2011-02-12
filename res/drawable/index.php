<?php
           define("NO_DATA", "./transparent.gif");
           define("ZOOM_IN", "./zoom_in.gif");
           define("ZOOM_OUT", "./zoom_out.gif");
     
           $x = $_GET["x"];
           $y = $_GET["y"];
           $z = $_GET["zoom"];
     
           $filename = "./i${x}_${y}_${z}.gif";
     
           if ( $z < 0 ) {
                 $content = file_get_contents( ZOOM_OUT );
           }else if ( $z > 5 ){
                 $content = file_get_contents( ZOOM_IN );
           }else if (is_numeric($x) && is_numeric($y) && is_numeric($z) && file_exists($filename)){
                 $content = file_get_contents( $filename );
           }else{
                 $content = file_get_contents( NO_DATA );
           }
     
           header("Content-type: image/gif");
     
           echo $content;
     ?>
