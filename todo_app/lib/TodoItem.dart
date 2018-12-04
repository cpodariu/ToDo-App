final String tableTodoItem = 'todo';
final String columnId = '_id';
final String columnTitle = 'title';

class TodoItem {
  int id;
  String title;

  Map<String, dynamic> toMap() {
    var map = <String, dynamic>{
      columnTitle: title,
    };
    if (id != null) {
      map[columnId] = id;
    }
    return map;
  }

  TodoItem();

  TodoItem.fromMap(Map<String, dynamic> map) {
    id = map[columnId];
    title = map[columnTitle];
  }
}