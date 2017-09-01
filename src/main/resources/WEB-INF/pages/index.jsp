<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>Prog.kiev.ua</title>
  </head>
  <body>
     <div align="center">
        <form action="/view"  method="POST" >
            Photo id: <input type="text" name="photo_id">
            <input type="submit" />
        </form>

        <form action="/add_photo" enctype="multipart/form-data" method="POST">
            Photo: <input type="file" name="photo">
            <input type="submit" />
        </form>
         <form action="/viewallphoto" method="POST">
             <input type="submit" value="Показати всі фото"/>
         </form>

         <form action="/dowloand" method="POST">
             <input type="submit" value = "Скачати ахрів"/>
         </form>





      </div>

  </body>
</html>
