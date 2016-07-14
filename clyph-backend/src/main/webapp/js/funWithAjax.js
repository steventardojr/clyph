$(document).ready(function(){

	$("#doNotClick").click(function(){
	    $.ajax({url: "../api/home?text="+ $(this).text(), success: function(result){
	        $("#doNotClick").html(result);
	    }});
	});

});