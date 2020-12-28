<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8" />
    <title>To-Do</title>
    <link rel="stylesheet" href="/static/todo.css" />
</head>
<body>
    <h1>To-Do: Your Lists</h1>
    <div class="lists">
        <#list lists as list>
            <div class="list-tile">
                <h2>${list.name}</h2>
                <p>
                    <a href="/lists/${list.id}">Open</a> ⋅
                    <a href="/lists/${list.id}/delete">Delete</a>
                </p>
            </div>
        </#list>
        <form class="list-tile" method="POST">
            <h2><input type="text" name="name" required placeholder="New list name" /></h2>
            <p><button>Create</button></p>
        </form>
    </div>
</body>
</html>
