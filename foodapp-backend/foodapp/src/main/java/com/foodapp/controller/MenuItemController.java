package com.foodapp.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.foodapp.model.MenuItem;
import com.foodapp.service.MenuItemService;

@RestController
@RequestMapping("/api/menu")
@CrossOrigin(origins = "*")
public class MenuItemController {

    @Autowired
    private MenuItemService menuItemService;

    @PostMapping("/add")
    public MenuItem addItem(@RequestBody MenuItem item) {
        return menuItemService.addItem(item);
    }

    @GetMapping("/all")
    public List<MenuItem> getAllItems() {
        return menuItemService.getAllItems();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> deleteItem(@PathVariable Long id) {
        menuItemService.deleteItem(id);
        return ResponseEntity.ok(Map.of("message", "Item deleted"));
    }
}
