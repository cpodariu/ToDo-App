import 'package:sqflite/sqflite.dart';
import 'TodoItem.dart';

class TodoItemController {
  Database db;

  Future open(String path) async {
    db = await openDatabase(path, version: 1,
        onCreate: (Database db, int version) async {
      await db.execute('''
  create table $tableTodoItem ( 
  $columnId integer primary key autoincrement, 
  $columnTitle text not null)
  ''');
    });
  }

  Future<TodoItem> insert(TodoItem todo) async {
    todo.id = await db.insert(tableTodoItem, todo.toMap());
    return todo;
  }

  Future<TodoItem> getTodoItem(int id) async {
    List<Map> maps = await db.query(tableTodoItem,
        columns: [columnId, columnTitle],
        where: '$columnId = ?',
        whereArgs: [id]);
    if (maps.length > 0) {
      return TodoItem.fromMap(maps.first);
    }
    return null;
  }

  Future<Iterable<TodoItem>> getTodoItems() async {
    print("getTodoItems");
    List<Map> maps = await db.query(tableTodoItem,
        columns: [columnId, columnTitle]);
    if (maps.length > 0) {
      return maps.map((x) {return TodoItem.fromMap(x);});
    }
    return null;
  }

  Future<int> delete(int id) async {
    return await db
        .delete(tableTodoItem, where: '$columnId = ?', whereArgs: [id]);
  }

  Future<int> update(TodoItem todo) async {
    return await db.update(tableTodoItem, todo.toMap(),
        where: '$columnId = ?', whereArgs: [todo.id]);
  }

  Future close() async => db.close();
}
