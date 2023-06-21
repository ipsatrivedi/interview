package com.interview.resource;

import com.interview.resource.mapper.UserResourceMapper;
import com.interview.resource.model.UserDto;
import com.interview.resource.model.UserToSaveDto;
import com.interview.service.UserService;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserResource {
    private final UserService userService;
    private final UserResourceMapper userResourceMapper;

    @Timed
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> findById(@PathVariable("id") Long userId) {
        if (userId == null || userId <= 0) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
        return ResponseEntity.ok(
                userResourceMapper.toDto(
                        userService.getBy(userId)));
    }

    @Timed
    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size,
            @RequestParam(value = "searchBy", required = false) String searchBy) {
        return ResponseEntity.ok(
                userResourceMapper.toUserDtoList(
                        userService.findAll(page, size, searchBy)));
    }

    @Timed
    @PostMapping
    public ResponseEntity<UserDto> saveNewUser(@RequestBody UserToSaveDto toUpdate) {
        return ResponseEntity.ok(
                userResourceMapper.toDto(
                        userService.saveNew(
                                userResourceMapper.toDm(toUpdate))));
    }

    @Timed
    @PutMapping
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto toUpdate) {
        return ResponseEntity.ok(
                userResourceMapper.toDto(
                        userService.update(
                                userResourceMapper.toDm(toUpdate))));
    }

    @Timed
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable("id") Long userId) {
        if (userId == null || userId <= 0) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
        userService.delete(userId);
        return ResponseEntity.ok().build();
    }
}