$(document).ready(function(){

	var extensions = ["image/jpeg", "image/png", "image/bmp", "image/gif"];
	
	$("#upload").click(function(){
		var convert = $('input[name=format]:checked', '#picForm').val()
		
		if (extensions.indexOf($('#picForm input[name=pic]').prop('files')[0].type) == -1) {
			$("#message").html("File type not supported");
			$("#message").show();
			return false;
		} else if ($('#picForm input[name=pic]').prop('files')[0] == null) {
			$("#message").html("You didn't select an image");
			$("#message").show();
			return false;
		} else if (convert == null || convert == "") {
			$("#message").html("You didn't select a new format");
			$("#message").show();
			return false;
		}
		
		$(this).hide();
		$("#message").html("Converting...Hitting Refresh will cancel the conversion");
		$("#message").show();
		$.ajax({
			url: "../api/imageConversion?toFormat=" + convert,
			type: 'POST',
			headers: {'Content-Type': $('#picForm input[name=pic]').prop('files')[0].type},
			data: $('#picForm input[name=pic]').prop('files')[0],
			cache: false,
			processData: false,
			contentType: false,
			success: function(result){
				if (result == null || result == "") {
					$("#message").html("Error converting image");
				} else {
					$("#downloadImage").attr("href", result);
					$("#downloadImage").find('span').trigger('click');
					$("#message").html("Success");
				}
			},
			error: function(){
				$("#message").html("Error converting image");
			}
	    });
		$(this).show();
	});

});