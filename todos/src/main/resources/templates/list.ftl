<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8" />
    <title>To-Do: Your List</title>
    <link rel="stylesheet" href="/static/todo.css" />
</head>
<body>
<h1>To-Do: ${list.name}</h1>
<ul>
<#list items as item>
    <li>
        <a href="/lists/${list.id}/items/${item.id}">
            <#if item.isComplete()>
            <s>
            </#if>
            ${item.name}
            <#if item.isComplete()>
            </s>
            </#if>
        </a>
    </li>
</#list>
    <li>
        <form method="POST">
            <input type="text" name="name" required placeholder="New item, Enter to save" />
        </form>
    </li>
</ul>

<p><a href="/">&larr; Other lists</a></p>

</body>
</html>
