<?php
    $jsonObj = new stdClass();
    $jsonObj->newversion = 0;
    $jsonObj->title = "New Update";
    $jsonObj->message = "A new update is available for this application.\n * Performance improved.";
    $jsonObj->positivebutton = "Update";
    $jsonObj->negativebutton = "Cancel";
    $jsonObj->apkurl = "https://www.dropbox.com/sh/bmmfxrx2yfpjf59/AACIXoJ7_RnSnmTeoZEpkEeDa?dl=0";
    echo json_encode($jsonObj);
?>


