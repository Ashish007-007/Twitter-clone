<html>
<body>
    USER is trying to login
    <br>
    <br>
    <div id="time">

    </div>

    <br>
    <br>
    <div>
      <marquee>Made with love by Narendra</marquee>
    </div>

    <script type="text/javascript">
       function updateTime(){
           document.getElementById("time").innerText=new Date().toString();
       }
       setInterval( updateTime , 1000 );
    </script>
</body>
</html>