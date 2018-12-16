package com.example.demo

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api")
class TodoController(private val todoRepository: TodoRepository) {

	@GetMapping("/networkCheck")
	fun check(): ResponseEntity<Void> {
		return ResponseEntity.ok().build();
	}

	@GetMapping("/todos")
	fun getAllTodos(): List<TodoItem> =
			todoRepository.findAll()


	@PostMapping("/todos")
	fun createNewTodo(@Valid @RequestBody todoItem: TodoItem): TodoItem =
			todoRepository.save(todoItem)


	@GetMapping("/todos/{id}")
	fun getTodoById(@PathVariable(value = "id") todoId: Long): ResponseEntity<TodoItem> {
		return todoRepository.findById(todoId).map { todoItem ->
			ResponseEntity.ok(todoItem)
		}.orElse(ResponseEntity.notFound().build())
	}

	@PutMapping("/todos")
	fun updateTodoById(@Valid @RequestBody todoItem: TodoItem): ResponseEntity<TodoItem> {

		return todoRepository.findById(todoItem.id).map { existingTodo ->
			val updatedTodo: TodoItem = existingTodo
					.copy(value = todoItem.value)
			ResponseEntity.ok().body(todoRepository.save(updatedTodo))
		}.orElse(ResponseEntity.notFound().build())

	}

	@DeleteMapping("/todos/{id}")
	fun deleteTodoById(@PathVariable(value = "id") todoId: Long): ResponseEntity<Void> {

		return todoRepository.findById(todoId).map { todoItem  ->
			todoRepository.delete(todoItem)
			ResponseEntity<Void>(HttpStatus.OK)
		}.orElse(ResponseEntity.notFound().build())
	}
}