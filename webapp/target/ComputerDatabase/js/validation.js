$(document).ready(function(){
    var $computerName = $('#computerName'),
        $introduced = $('#introduced'),
        $discontinued = $('#discontinued'),
        $companyId = $('#companyId'),
        $champ = $('.form-control'),
    	$nameError = $('#nameError');


    $computerName.focusout(function(){
    	if($(this).val().length < 5){
    		$(this).css({
    			color : 'red'
            });
    		$(":submit").prop('disabled', true );
    	}
    	else {
    		$(this).css({
    			color : 'black'
            });
    		$(":submit").prop('disabled', false );
    	}
    });

    $computerName.focusin(function(){
    	$(this).css({
			color : 'black'
        });
    });
});