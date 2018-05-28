$(document).ready(function(){
	
	//This method get call initially 
	//showallemployee();
	
	//button click event to find employee using sap id
	$('#btnSearch').click(function(){
    	
		var search = $('#pac-input').val();
				
		$.ajax({
        	type:"GET",
            url:"/byid/"+search,
            success:function(data, textStatus, jqXHR){
            if(data!="")
            	{
            	showById(data.latitude,data.longitude,data.userId);
            	}
            else{
            	  alert("NO RECORD FOUND");
            	}
            },
	         error:function( jqXhr, textStatus, errorThrown ) {           
	        	 alert("Please Enter SAP ID");
	             
	        }
        });
				
	});
	

	//function to create map and show employee on map
	function showById(){
		
		  //alert("mymap");
		  var latitude  = arguments[0];
		  var longitude = arguments[1];
		  var userId = arguments[2];
		  var mapCanvas = document.getElementById("map");
		  var myCenter = new google.maps.LatLng(latitude,longitude); 
		  var mapOptions = {center: myCenter, zoom: 20};
		  var map = new google.maps.Map(mapCanvas,mapOptions);
	

		  var marker = new google.maps.Marker({
		    position: myCenter,
		    icon: "images/marker-person.png",
	       // animation:google.maps.Animation.BOUNCE,
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
	
	
	//function to create map and show  all employee's on map
	function showAllEmployeeMap(){
		
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
		       // animation:google.maps.Animation.BOUNCE,
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
	
	//Function to show all employees on map
    function showallemployee(){
		
    	$.ajax({
        	type:"GET",
            url:"/allemployee",
            success:function(data, textStatus, jqXHR){
            showAllEmployeeMap(data);
           			             
            },
	         error:function( jqXhr, textStatus, errorThrown ) {           
	        	 alert("IN ERROR");
	             
	         }
        });
		  
	}
		
});
		        

	
		