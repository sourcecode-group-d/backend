package com.sourcecode.infrastructure;

import com.sourcecode.models.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request , Long> {

    @Query("SELECT f FROM Request f ORDER BY f.likesCounter DESC" )
    public List<Request> findAllByMostLikes();

}
