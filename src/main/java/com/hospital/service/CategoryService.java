package com.hospital.service;

import com.hospital.entity.Category;
import com.hospital.repository.CategoryRepository;
import com.hospital.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.xml.crypto.NoSuchMechanismException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;

    public boolean isMemberAllowedToModify(Long memberId, Long categoryId){
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()-> new IllegalArgumentException("카테고리를 찾을 수 없습니다."));
        return category.getOwnerId().equals(memberId);
    }
    // 카테고리 추가
    public Category createCategory(String name) {
        Category category = new Category();
        category.setName(name);
        return categoryRepository.save(category);
    }
    // 카테고리 수정
    public Category updateCategory(Long id, String newName) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NoSuchMechanismException("카테고리를 찾을 수 없습니다."));
        category.setName(newName);
        return categoryRepository.save(category);
    }
    // 카테고리 삭제
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new NoSuchMechanismException("카테고리를 찾을 수 없습니다.");
        }
        categoryRepository.deleteById(id);
    }
    // 전체 카테고리 조회
    public List<Category> getAllCategories(){
        return categoryRepository.findAll();
    }
}
