package com.hospital.controller;

import com.hospital.entity.Category;
import com.hospital.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Category> create(@RequestParam String name){
        return ResponseEntity.ok(categoryService.createCategory(name));
    }

    @PreAuthorize("hasRole('ADMIN') or (@categoryService.isMemberAllowedToModify(authentication.principal.id, #categoryId))")
    @PutMapping("/{id}")
    public ResponseEntity<Category> update(@PathVariable Long id, @RequestParam String name){
        return ResponseEntity.ok(categoryService.updateCategory(id, name));
    }

    @PreAuthorize("hasRole('ADMIN') or (@categoryService.isMemberAllowedToModify(authentication.principal.id, #categoryId))")
    @DeleteMapping("/{id}")
    public ResponseEntity<Category> delete(@PathVariable Long id){
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Category>> getALL(){
        return ResponseEntity.ok(categoryService.getAllCategories());
    }
}

