package com.project.blog.repository;

import com.project.blog.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Long, Image> {
}
