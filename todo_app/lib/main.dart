import 'package:flutter/material.dart';
import 'package:todo_app/TodoItem.dart';
import 'package:todo_app/controller.dart';

void main() => runApp(new TodoApp());

class TodoApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return new MaterialApp(title: 'Todo List', home: new TodoList());
  }
}

class TodoList extends StatefulWidget {
  @override
  createState() => new TodoListState();
}

class TodoListState extends State<TodoList> {
  TodoItemController controller = new TodoItemController();
  List<TodoItem> items = [];

  @override
  initState() {
    super.initState();
    controller.open("TODOAPPFLUTTER").then((i) {
      getItems();
    });
  }

  Future getItems() async {
    print("getItems");
    items.clear();
    controller.getTodoItems().then((res) {
      res.forEach((i) {
        items.add(i);
      });
    }).then((x) {
      setState(() {});
    });
  }

  void _addTodoItem(String task) {
    if (task.length > 0) {
      TodoItem item = new TodoItem();
      item.title = task;
      controller.insert(item).then((it) {
        getItems();
      });
    }
  }

  Widget _buildTodoList() {
    return new ListView.builder(
      itemCount: items.length,
      itemBuilder: (context, index) {
        if (index < items.length) {
          return _buildTodoItem(items[index]);
        }
      },
    );
  }

  Widget _buildTodoItem(TodoItem todoItem) {
    return Dismissible(
      key: Key(todoItem.id.toString()),
      onDismissed: (direction) {
        var text = todoItem.title;
        items.remove(todoItem);
        controller.delete(todoItem.id).then((i) {
          getItems();
        });
//        Scaffold.of(context)
//            .showSnackBar(SnackBar(content: Text("$text dismissed")));
      },
      background: Container(color: Colors.red),
      child: ListTile(
          title: Text(todoItem.title), onLongPress: () => editItem(todoItem)),
    );
  }

  void editItem(TodoItem item) {
    Navigator.of(context).push(new MaterialPageRoute(builder: (context) {
      return new Scaffold(
          appBar: new AppBar(title: new Text('Edit item')),
          body: new TextField(
            autofocus: true,
            onSubmitted: (val) {
//              items[index] = val;
              item.title = val;
              controller.update(item).then((i) {
                getItems();
              });
              Navigator.pop(context);
            },
            decoration: new InputDecoration(
                hintText: item.title,
                contentPadding: const EdgeInsets.all(16.0)),
          ));
    }));
  }

  @override
  Widget build(BuildContext context) {
    print("build");

    return new Scaffold(
      appBar: new AppBar(title: new Text('Todo List')),
      body: _buildTodoList(),
      floatingActionButton: new FloatingActionButton(
          onPressed: _pushAddTodoScreen,
          tooltip: 'Add task',
          child: new Icon(Icons.add)),
    );
  }

  void _pushAddTodoScreen() {
    Navigator.of(context).push(new MaterialPageRoute(builder: (context) {
      return new Scaffold(
          appBar: new AppBar(title: new Text('Add a new task')),
          body: new TextField(
            autofocus: true,
            onSubmitted: (val) {
              _addTodoItem(val);
              Navigator.pop(context);
            },
            decoration: new InputDecoration(
                hintText: 'Enter something to do...',
                contentPadding: const EdgeInsets.all(16.0)),
          ));
    }));
  }
}
