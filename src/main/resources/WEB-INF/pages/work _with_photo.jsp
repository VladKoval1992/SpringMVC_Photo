<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Prog.kiev.ua</title>
</head>
<body>
<style>
    .demotable {
        border-collapse: collapse;
        counter-reset: numeration;
    }
    .demotable num {
        counter-increment: numeration;
    }
    .demotable num:before {
        content: counter(numeration);
        display: table-cell;
        vertical-align: middle;
        color: #000000;
    }
</style>
<form name="photo" action="/deletesomephoto" method="POST">

    <table class="demotable" border="1" width="600px" cellpadding="5">

        <thead bgcolor="#ccc">
        <tr>
            <td>№</td>
            <td>Назва фалйлу</td>
            <td>Фото</td>
        </tr>
        </thead>

        <c:forEach  var="x" items="${allphoto}">
            <tr>
                <td> <num>
                <td> <input type = "checkbox" name = "check[]" value = "${x}"/>${x}<br> </td>
                <td> <img src="/photo/${x}" style= "height:240px" /> </td>
            </tr>
        </c:forEach>

    </table>
    <input type="submit" value="Видалити" />

</form>
<input type="submit" value="Назад" onclick="window.location='/';" />

</body>
</html>