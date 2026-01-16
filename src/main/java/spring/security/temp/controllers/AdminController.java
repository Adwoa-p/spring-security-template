package spring.security.temp.controllers;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.security.temp.models.ResponseDto;
import spring.security.temp.models.User;
import spring.security.temp.models.UserResponseDto;
import spring.security.temp.services.AdminService;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping()
    @Operation(summary = "Returns all users in the DB")
    public ResponseEntity<Page<User>> getAllUsers(@RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                                  @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                                  @RequestParam(defaultValue = "email", required = false) String sortBy,
                                                  @RequestParam(defaultValue = "true") boolean ascending) {
        return new ResponseEntity<>(adminService.getAllUsers(pageNo, pageSize, sortBy, ascending), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Returns a user based on id")
    public ResponseEntity<ResponseDto<UserResponseDto>> getUser(@PathVariable Long id) {
        return new ResponseEntity<>(adminService.getUser(id), HttpStatus.FOUND);
    }

    // partially update user details - 1. locked 2.enable 3.update specific field(role)
    @Operation(summary = "Update User's Details - Admin")
    @PatchMapping("/{id}/status")
    public ResponseEntity<ResponseDto<String>> updateUserStatus(
            @PathVariable Long id,
            @RequestParam(required = false) Boolean locked,
            @RequestParam(required = false) Boolean enabled) {
        return new ResponseEntity<>(adminService.partialUpdateUser(id, locked, enabled), HttpStatus.OK);
    }


//    // partially update user details - 1. locked 2.enable 3.update specific field(role)
//    @Operation(summary = "Update User's Details - Admin")
//    @PatchMapping("/{id}/roles`")
//    public ResponseEntity<ResponseDto<String>> updateUserRole(
//            @PathVariable Long id,
//            @RequestParam(required = false) ) {
//        return new ResponseEntity<>(adminService.partialUpdateUser(id, locked, enabled), HttpStatus.OK);
//    }


}
