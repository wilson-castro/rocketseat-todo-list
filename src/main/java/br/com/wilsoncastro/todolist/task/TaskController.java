package br.com.wilsoncastro.todolist.task;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.wilsoncastro.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/tasks")
public class TaskController {

  @Autowired
  private ITaskRepository taskRepository;


  @PostMapping("/")
  public ResponseEntity<?> create(@RequestBody TaskModel taskModel, HttpServletRequest request) {
    taskModel.setIdUser(UUID.fromString(request.getAttribute("idUser").toString()));

    var currentDate = LocalDateTime.now();

    if (currentDate.isAfter(taskModel.getStartAt()) || currentDate.isAfter(taskModel.getEndAt())) {
      return ResponseEntity.badRequest().body("As datas devem ser maior que a data atual");
    }

    if (taskModel.getStartAt().isAfter(taskModel.getEndAt())) {
      return ResponseEntity.badRequest().body("A data de término deve ser depois da data de início");
    }

    var task = this.taskRepository.save(taskModel);
    return ResponseEntity.status(201).body(task);
  }

  @GetMapping("/")
  public ResponseEntity<?> findAll(HttpServletRequest request) {
    return ResponseEntity.ok().body(taskRepository.findByIdUser((UUID) request.getAttribute("idUser")));
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> update(@RequestBody TaskModel taskModel, HttpServletRequest request, @PathVariable UUID id) {

    var taskToUpdate = taskRepository.findById(id).get();
    var idUser = (UUID) request.getAttribute("idUser");

    if (!taskToUpdate.getIdUser().equals(idUser))
      return ResponseEntity.badRequest().body("Usuário não tem permissão para alterar essa tarefa");

    Utils.copyNonNullProperties(taskModel, taskToUpdate);

    return ResponseEntity.ok().body(taskRepository.save(taskToUpdate));
  }
  
}
