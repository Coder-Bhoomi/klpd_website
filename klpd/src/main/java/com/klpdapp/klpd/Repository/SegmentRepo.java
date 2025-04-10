package com.klpdapp.klpd.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.klpdapp.klpd.model.Segment;

public interface SegmentRepo extends JpaRepository<Segment, Integer> {

}
