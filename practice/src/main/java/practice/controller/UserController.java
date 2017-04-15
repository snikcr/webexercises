package practice.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import practice.exception.ProcessException;
import practice.model.User;
import practice.service.UserService;

/**
 * User Controller for User
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	private UserService service;

	private static final Logger logger = Logger.getLogger(UserController.class);

	@GetMapping(value = "getall", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<User>> getAll() {

		logger.info("Received getall request");

		List<User> users = new ArrayList<>();

		Future<List<User>> futureUsers = service.findAll();

		while (!futureUsers.isDone()) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				logger.warn("Wait interrupted.");
			}
		}
		try {
			users.addAll(futureUsers.get());
		} catch (InterruptedException | ExecutionException e) {
			ProcessException pex = new ProcessException(HttpStatus.INTERNAL_SERVER_ERROR, e, "error.user.retrieveall");
			throw pex;
		}

		logger.info(String.format("Returning %d users", users.size()));

		return users.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(users);
	}

	@GetMapping("get/{id}")
	public ResponseEntity<User> get(@PathVariable("id") int id) {
		
		if (id <= 0) {
			throw new ProcessException(HttpStatus.BAD_REQUEST, "error.id.minsize");
		}

		logger.info("Received get user request");

		User user = service.find(id);

		logger.info(String.format("Returning %d users", user == null ? 0 : 1));

		return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
	}

	@PostMapping(value = "create", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> create(@Validated(User.CreationValidation.class) @RequestBody User data) {

		logger.info("Received create user request");

		User user = null;

		user = service.create(data);

		logger.info(String.format("Returning %d users", user == null ? 0 : 1));
		return ResponseEntity.status(user == null ? HttpStatus.NOT_FOUND : HttpStatus.OK).body(user);
	}

	@PostMapping(value = "update", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> update(@Validated(User.DefaultValidation.class) @RequestBody User data) {

		logger.info("Received update user request");

		User user = null;

		user = service.update(data);

		logger.info(String.format("Returning %d users", user == null ? 0 : 1));

		return ResponseEntity.status(user == null ? HttpStatus.NOT_FOUND : HttpStatus.OK).body(user);
	}

	@GetMapping("remove/{id}")
	public ResponseEntity<Void> remove(@Validated @NotNull @Min(1) @PathVariable("id") int id) {

		if (id <= 0) {
			throw new ProcessException(HttpStatus.BAD_REQUEST, "error.id.minsize");
		}
		
		logger.info("Received remove user request");
		Future<Boolean> futureRemoval = service.remove(id);

		while (!futureRemoval.isDone()) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				logger.warn("Wait interrupted.");
			}
		}

		Boolean removed = Boolean.FALSE;
		try {
			removed = futureRemoval.get();
		} catch (InterruptedException | ExecutionException e) {
			ProcessException pex = new ProcessException(HttpStatus.INTERNAL_SERVER_ERROR, e, "error.user.deletion",
					String.valueOf(id));
			throw pex;
		}
		
		HttpStatus s = removed.booleanValue() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		ResponseEntity<Void> d = new ResponseEntity<Void>(s);
		return d;
	}
}
