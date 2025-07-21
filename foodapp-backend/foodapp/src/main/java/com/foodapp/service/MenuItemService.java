package com.foodapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foodapp.model.MenuItem;
import com.foodapp.repository.MenuItemRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MenuItemService {

    @Autowired
    private MenuItemRepository menuItemRepository;

    public MenuItem addItem(MenuItem item) {
        log.info("Adding menu item: {}", item.getName());
        return menuItemRepository.save(item);
    }

    public List<MenuItem> getAllItems() {
        log.info("Fetching all menu items");
        return menuItemRepository.findAll();
    }

    public void deleteItem(Long id) {
        log.info("Deleting menu item with ID: {}", id);
        menuItemRepository.deleteById(id);
    }
}
