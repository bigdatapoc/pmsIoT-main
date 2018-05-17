$(document).ready(function(){
	
	//alert("In SUCCESSxxsxsdxs ");
	showallemployee();
	
	$('#btnSearch').click(function(){
    	//alert("In Search ");
		
		var search = $('#searchKey').val();

		$.ajax({
        	type:"GET",
            url:"http://localhost:8052/byid/"+search,
            success:function(data, textStatus, jqXHR){
            mymap(data.latitude,data.longitude,data.userId);
           	
            },
	         error:function( jqXhr, textStatus, errorThrown ) {           
	        	 alert("In error ");
	             
	        }
        });
				
	});
	

	
	
	$('#btnallemployee').click(function(){
    	//alert("In SUCCESS ");

		$.ajax({
        	type:"GET",
            url:"http://localhost:8052/getallemployee",
            success:function(data, textStatus, jqXHR){
            document.getElementById('listid').style.display="none";
            document.getElementById('listemp').style.display="block";
            mymapallemp(data);
            for(i=0; i<data.length; i++)
			{
            	//alert("In SUCCESS ");
				$('#listemployee').append("<li>" + data[i].userId+ "   "+data[i].latitude+"   "+data[i].longitude+"</li>");
				
			}
				             
            },
	         error:function( jqXhr, textStatus, errorThrown ) {           
	        	 alert("In error ");
	             
	         }
        });
			
	});
	
	function mymap(){
		
		  //alert("mymap");
		  var latitude  = arguments[0];
		  var longitude = arguments[1];
		  var userId = arguments[2];
		  var mapCanvas = document.getElementById("mapwithid");
		  var myCenter = new google.maps.LatLng(latitude,longitude); 
		  var mapOptions = {center: myCenter, zoom: 20};
		  var map = new google.maps.Map(mapCanvas,mapOptions);
		  var marker = new google.maps.Marker({
		    position: myCenter,
		    icon: "pinkball.png",
		    //animation: google.maps.Animation.BOUNCE
		  });
		  marker.setMap(map);
		  
		  var infowindow = new google.maps.InfoWindow({
			    content: "EMPLOYEE SAPID : "+userId
			  });
			  infowindow.open(map,marker);

		google.maps.event.addListener(marker,'click',function() {
		 map.setZoom(22);
		 map.setCenter(marker.getPosition());
		 });
		  
	}
	
	function mymapallemp(){
		
		 //alert("fun");
		    var locations = arguments[0];
		    var latitude  = 28.53590728969859;
			var longitude = 77.3436963558197;

		    var map = new google.maps.Map(document.getElementById('map'), {
		      zoom: 18,
		      center: new google.maps.LatLng(latitude,longitude),
		    });

		    var infowindow = new google.maps.InfoWindow();
		    var marker, i;
		    for (i = 0; i < locations.length; i++) { 
		      marker = new google.maps.Marker({
		        position: new google.maps.LatLng(locations[i].latitude, locations[i].longitude),
		        icon: "images/marker-person.png",
		        animation:google.maps.Animation.BOUNCE,
		        map: map
		      });

		      google.maps.event.addListener(marker, 'click', (function(marker, i) {
		        return function() {
		          map.setZoom(20);
		          map.setCenter(marker.getPosition());
		          infowindow.setContent('EMPLOYEE SAPID : '+locations[i].userId);
		          infowindow.open(map, marker);
		        }
		      })(marker, i));
		    }
		  
		  
	}
	
    function showallemployee(){
		
		//alert("fun");
    	$.ajax({
        	type:"GET",
            url:"http://localhost:8052/allemployee",
            success:function(data, textStatus, jqXHR){
            mymapallemp(data);
           			             
            },
	         error:function( jqXhr, textStatus, errorThrown ) {           
	        	 alert("In error ");
	             
	         }
        });
		  
	}
	
	
	
	
	
});
		        

	
		