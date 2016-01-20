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
<h1>Список адресатов, найденных в СДП</h1>
<p>

</p>
<#list addresses as addressee>
<p>
    Имя: ${addressee.name}<br>
    Адрес: ${addressee.address}<br>
    Пол: ${addressee.gender}<br>
    Статус загрузки:
    <#if addressee.status == true>
        <span class="status.green">загружен</span>
    <#else>
        <span class="status.red">не загружен</span><br>
        <span>Причина: ${addressee.reason}</span>
    </#if>
</p>
</#list>
</body>
</html>