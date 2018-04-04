package com.nonobank.group.repository;

import com.nonobank.group.entity.db.TestGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by tangrubei on 2018/3/14.
 */
public interface TestGroupRepository extends JpaRepository<TestGroup,Integer> {
    List<TestGroup> findByPIdAndOptstatusEquals(Integer pid,short optstatus);

    List<TestGroup> findByOptstatusNotAndJobTimeIsNotNull(short optstatus);

}
