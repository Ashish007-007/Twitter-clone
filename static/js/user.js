$(".follow-member").click( function(){
  var button=this;

  var is_followed=$(button).hasClass("followed-button");
  if(is_followed){
       $.ajax({
           type: "POST",
           url: "/user/un-follow-member",
           data: this.getAttribute("member-id"),
           success: function(response){
               if(!!response){
                  $(button).removeClass("followed-button");
                  button.querySelector("span").innerText="Follow";
               }
           },
           contentType: 'application/json'
         });
  }else{
      $.ajax({
      type: "POST",
      url: "/user/follow-member",
      data: this.getAttribute("member-id"),
      success: function(response){
          if(!!response){
             $(button).addClass("followed-button");
             button.querySelector("span").innerText="Followed";
          }
      },
      contentType: 'application/json'
    });

  }


 });