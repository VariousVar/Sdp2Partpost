<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <title></title>
    <style>
        .status.green {
            color: green;
        }
        .status.red {
            color: red;
        }
    </style>
</head>
<body>
<h1>Адресаты</h1>
<#list addresses as addressee>
<p>
    Имя: ${addressee.name}<br>
    Адрес: ${addressee.address}<br>
    Пол: ${addressee.gender}<br>
    Статус сохранения:
    <#if addressee.status == true>
        <span class="status.green">сохранен</span>
    <#else>
        <span class="status.red">не сохранен</span><br>
        <span>Причина: ${addressee.reason}</span>
    </#if>
</p>
</#list>
</body>
</html>