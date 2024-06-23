<html>
<link rel="shortcut icon" type="image/png" href="/static/images/favicon.ico"/>

<head>

    <script src="https://code.jquery.com/jquery-3.7.1.min.js" integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo=" crossorigin="anonymous"></script>

    <style>
          @import url('https://fonts.googleapis.com/css2?family=Lato:wght@400;700;900&display=swap');

          *{
            margin:0;
            padding:0;
          }

          html{
            font-family: 'Lato',Arial,Helvetica,sans-serif;
            font-size: 10px;
            font-weight: 400;
          }

          body{
            min-height: 100vh;
            min-width: 100vw;
            background-image: -webkit-linear-gradient(45deg,#00acee 0% ,#00acee 100%);
            background-image: -moz-linear-gradient(45deg,#00acee 0%, #00acee 100%);
            background-image: linear-gradient(45deg,#00acee 0%, #00acee 100%);
            display: flex;
            justify-content: center;
            align-items: center;
          }

          #signup-box{
            background-color: #fff;
            border-radius: 30px;
            -webkit-box-shadow: 0px 0px 76px 0px rgba(0, 0, 0,0.20);
            -moz-box-shadow: 0px 0px 76px 0px rgba(0,0,0,0.20);
            box-shadow: 0px 0px 76px 0px rgba(0,0,0,0.20);
            text-align: center;
            height: 100%;
            max-width: 422px;
            width: 45%;
          }

          #signup-box h1{
            color: #00acee;
            font-size: 1.8rem;
            font-width:700;
            margin-top: 40px
          }

          #signup-box p{
            color: #222;
            font-size: 1.6rem;
            margin: 5px 0 30px 0;
            padding: 0 15%;
          }

          #signup-form{
            width: 70%;
            margin:auto;
            text-align: left;
            font-size: 14px;
          }

          #signup-form label{
            color: #222;
            font-weight: 700;
            padding-left: 10px;
          }

          #signup-form input{
            height: 35px;
            width: 290px;
            border: 1px solid #b4b4b4;
            border-radius: 5px;
            margin: 6px 0 20px 0;
            padding-left: 10px;
            outline: none;
          }

          #signup-form input::placeholder{
            color:#b4b4b4;
          }

          #signup-form button{
            background-image: -webkit-linear-gradient(-45deg,#00acee 0%,#00acee 100%);
            background-image: -moz-linear-gradient(-45deg,#00acee 0%, #00acee 100%);
            background-image: linear-gradient(-45deg,#00acee 0%, #00acee 100%);
            border: none;
            border-radius: 30px;
            color: #fff;
            cursor: pointer;
            outline: none;
            height: 45px;
            width: 100px;
            display:block;
            margin: auto;
            margin-bottom: 40px;
            text-transform: uppercase;
            font-size: 1.6rem;
            font-weight: 900;
          }

          #signup-form button:hover{
             background-image: -webkit-linear-gradient(45deg,#00acee 0%,#00acee 100%);
             background-image: -moz-linear-gradient(45deg,#00acee 0%, #00acee 100%);
             background-image: linear-gradient(45deg,#00acee 0%, #00acee 100%);
             -webkit-box-shadow: 0px 0px 20px 0px rgba(0, 0, 0,0.20);
             -moz-box-shadow: 0px 0px 20px 0px rgba(0,0,0,0.20);
             box-shadow: 0px 0px 20px 0px rgba(0,0,0,0.20);
          }

           @media screen and (max-width: 600px){
              #signup-box{
                  width: 70%;
              }
           }

          @media screen and (max-width: 600px){
            #signup-box{
                max-width: none;
                width: 100%;
                border-radius: unset;
                box-shadow: none;
            }
          }

    </style>
</head>
<body>
    <section id="signup-box">
        <h1>Create your account</h1>
        <p>Welcome to the social network of coder by Mission Helix</p>
        <form id="signup-form">
            <label for="name">Name</label><br>
            <input id="signup-name" type="text" name="name" placeholder="Andrea"><br>
            <label for="email">Email</label><br>
            <input id="signup-email" type="email" name="email" placeholder="Fill your email"><br>
            <label for="password">Password</label><br>
            <input id="signup-password" type="password" name="name" placeholder="Fill your password"><br>

            <p style="color:red; display:none" id="signup-error">evvrvrevreverv</p>
            <button type="button" id="btn-signup">Signup</button>
        </form>
    </section>


    <script>//make sure we add script after button otherwise we wont be able to apply js over here. whenever this button is clicked then it will call a fxn
        function validateSignupForm(){ //js
            var name = $("#signup-name").val();    //$ sign because we use jquery
            var email= $("#signup-email").val();
            var password= $("#signup-password").val();

            var error= "";
            //check if any of them is empty. !name means if name is empty. !!name means if name is not empty
            if(!name){
                error +="Name is empty.";
            }
            if(!email){
                error +="Email is empty.";
            }
            if(!password){
                error +="Password is empty.";
            }
            //if password is not empty and password length must be 5 characters
            if(password.length <= 3){
                error +="Password length must be greater than 3 characters ";
            }

            $("#signup-error").html(error); // pass error over here and we got error like name, error ,password is empty like that
            //$("#signup-error").show();

            //check error . if error length > 0 the false otherwise true
            if(error.length>0){
                return false;
            }
            //we want to make sure nothing is write and it will gonna hide up if everything is good
            //$("#signup-error").hide();
            return true;
        }
        $("#btn-signup").click(function(){

            var isFormValid = validateSignupForm(); // everything in js is in var

            if(isFormValid){

                $("#signup-error").hide();
                //alert("The button was clicked.");
                 var name = $("#signup-name").val();    //$ sign because we use jquery
                 var email= $("#signup-email").val();
                 var password= $("#signup-password").val();

                var user={ // user will have name ,email,password
                    "name": name,
                    "email": email,
                    "password": password
                };
                //post request
                $.ajax({
                  type: "POST",
                  url: "/signup", // we make same url for get and post request different
                  data: JSON.stringify(user), //data right now and we have create user now . and we r doing json.stringify ,we r stringifying it and sending it to the server. json stringinfy cob=vert object into string
                  success: function(response){

                        if(!!response){ // if not empty
                            if(response.user_created === true){//recommended 3 =
                                alert(response.message);
                            }else{
                                   alert(response.message);
                            }
                        }

                   }, // if it happen successfully . to handle something like data got submitted andwhatever receive as a response from there, we receive as data over here
                  contentType: "application/json"
                });
            }else{
                $("#signup-error").show();
                //alert("Form is invalid.");
            }
        });

    </script>
</body>
</html>